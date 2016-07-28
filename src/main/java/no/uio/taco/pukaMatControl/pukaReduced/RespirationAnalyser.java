package no.uio.taco.pukaMatControl.pukaReduced;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.Logger;

import matlabcontrol.MatlabInvocationException;
import no.uio.taco.pukaMatControl.matControlAdapter.JMatLinkAdapter;

/** 
 * Utility class for running pukas respiration analysis without GUI. 
 * @author Cato Danielsen
 */
public class RespirationAnalyser implements Runnable {
	
	public enum AnalysisType {
	    LOCAL, STREAM
	}
	public List<String> buffer;
	
	private int step = 1; // step counter for stepInfo method
	private boolean debug = true;
	private JMatLinkAdapter engMatLab = null;
	
	
	private Settings settings;
	private History history;
	private AnalysisType type;
	//private Logger log; 
	
	/**
	 * Constructor instantiates Settings and History object
	 */
	public RespirationAnalyser() {
		//log = Logger.getLogger(this.getClass());
		//BasicConfigurator.configure();
		//log.setAdditivity(false);
		settings = new Settings(); // contains all variables for this analysis
		history = new History();
		type = AnalysisType.LOCAL; // default
		buffer = new ArrayList<String>(); 
		
	}
	
	public void run() {
		
		if (type == AnalysisType.LOCAL) {
			try {
				launchLocalFile(settings.filename);
			} catch (MatlabInvocationException e) {
				System.out.println("MATLAB session can not be reached");
			}
		}
		
		else if (type == AnalysisType.STREAM) {
			try {
				analyseWindow(buffer);
			} catch (MatlabInvocationException e) {
				System.out.println("MATLAB session can not be reached");
			}
		}
	}
	
	
	/**
	 * Launches a recreation of pukas respiration analysis
	 * with file name of local signal file which must be located in
	 * current directory. TODO: support absolute path
	 * @param fname - name of signal file
	 */
	public void launchLocalFile(String fname) throws MatlabInvocationException {
		
		// 1: Get data if specified 
		if (fname.length() > 0) {
			settings.filename = System.getProperty("user.dir") + "\\" + fname; // no fault handeling
		}

		File textDataFile = new File(settings.filename);
		//log.debug("Signal file:\t" + settings.filename + "\n============\texists: " + textDataFile.exists()); 
		
		
		if (textDataFile.exists()) {
			startMatlab();
			step = 1;
			/**
			 * Step 1, load data, set start and end time
			 */
			stepInfo("load data, set start and end time");
			loadFile(textDataFile);
			
			/* TODO, should this be in here or in analyseResp()? AND
			it should be modified to work automatically, that is if it does not
			find onset time, we need to do some magic*/
			setOnset(); 
			
			/**
			 * Step 2, peak detection, find peaks and trough 
			 */
			stepInfo("peak detection");
			peakDetection();
			
			
			/**
			 * Step 3, classify peaks -> this is done manually in puka, we need to find a way to automate this process
			 */
			stepInfo("classify peaks");
			classifyPeaks();
			
			/**
			 * Step 4,
			 */
			stepInfo("pause detection:\n\t"
					+ "Uses matlab again to detect the start and end point of the pause around the peak");
			pauseDetection();
			
			
			/**
			 * Step 5, statistical calculation 
			 */
			stepInfo("statistical calculation:\n\t"
					+ "This step looks at the information gathered in the current clip, and has\n\t"
					+ "to be modified in order to be used in a meaningful way for realtime analysis.\n\t"
					+ "Look into how to extract the events");
		} else {
			System.out.println("File not found");
		}
	}
	
	
	/***************************** ONLINE PART *************************/
	
	/**
	 * Fetches clip size from shared buffer and analyzes the window
	 */
	public void launchMatlabInstance() {
		startMatlab();
	}
		
	
	public void analyseWindow(List<String> buffer) throws MatlabInvocationException {
		long analyseWindowStartTime = System.currentTimeMillis();
		/**
		 * Step 1, load data, set start and end time
		 */
		step = 1;
		
		engMatLab.engEvalString("clear;");  // remove all previous information in workspace, if any

		/* STEP 1: Load data */
		double[][] data1 = new double[1][buffer.size()];
		
		for (int index = 0; index < buffer.size(); index++) {
			try {
				data1[0][index] = Double.parseDouble(buffer.get(index));
			} catch (NumberFormatException e) {
				System.out.println("Malformed entry in buffer:\n\t"+buffer.get(index));
			}
		}
		engMatLab.engPutArray("data1", data1); // load record
		engMatLab.engEvalString("y = data1(1, :);");  // get trigger col into y in matlab
		engMatLab.engEvalString("cd ('" + settings.scriptPath + "');"); // settings path to scripts
		
		/* STEP 1 cont:  find onset */
		engMatLab.engEvalString("onsetTime = findOnset(y);");  //run function, put result in intNew
	
		double onsetTime = engMatLab.engGetScalar("onsetTime");
		stepInfo("load data, set start and end time");
		setOnset(); // TODO, should this be in here or in analyseResp()?	
		
		
		analyseResp();
		
		long analyseWindowStopTime = System.currentTimeMillis();
		long endTime = analyseWindowStopTime - analyseWindowStartTime;
		System.out.println("====RESP ANALYSER: Complete anaysis ms: " + endTime);
	}
	
	
	/**
	 * Load a local file to be used in the respiration analysis. This method assumes
	 * the file is consistent with the raw data format described in the puka manual.
	 * It also assumes that we only have one signal present in the file
	 * @param f - file descriptor for the desired signal file.
	 */
	private void loadFile(File f) {
		engMatLab.engEvalString("clear;");  // remove all previous information in workspace, if any
		engMatLab.engEvalString("data1 = load('" + f.getPath() + "');");  // load data file
		engMatLab.engEvalString("y = data1(:, 1);");  // get trigger col into y in matlab
		
		//log.debug("Change MATLAB folder to script path: " + settings.scriptPath);
		engMatLab.engEvalString("cd ('" + settings.scriptPath + "');"); // settings path to scripts
		
		engMatLab.engEvalString("onsetTime = findOnset(y);");  //run function, put result in intNew
	}
	
	/**
	 * Initiate respiration analysis. MATLAB session has to be running,
	 */
	private void analyseResp() {
		
		/**
		 * Step 2, peak detection, find peaks and trough 
		 */
		stepInfo("peak detection");
		peakDetection();
		
		
		/**
		 * Step 3, classify peaks -> this is done manually in puka, we need to find a way to automate this process
		 */
		stepInfo("classify peaks");
		classifyPeaks();
		
		/**
		 * Step 4,
		 */
		stepInfo("pause detection:\n\t"
				+ "Uses matlab again to detect the start and end point of the pause around the peak");
		pauseDetection();
	}
	
	private void setOnset() {
		double dblTemp = -1;

		try{
			dblTemp = engMatLab.engGetScalar("onsetTime");  //get the stimulus onset time point
		}
		catch (MatlabInvocationException e) {
			//log.error("Error during getScalar. Unable to retrieve onset time from MATLAB");
			e.printStackTrace();
			System.exit(1); // TODO: better error handling
		}
		//if findOnset returns a time of -1 then it was unable to locate a good onset time
		
		if ((int)dblTemp < 0) { 
			//log.error("not able to get a good onset!");  
			// TODO: what to do?
			System.exit(1);
		}  
		else {
			settings.intStartTime = (int)dblTemp;  //assign start time to public variable
			settings.intStopTime = settings.clipLength + settings.intStartTime; 
			// inform MATLAB. This should include a check to make sure we do not exceed record size
			engMatLab.engPutArray("endTime", (double)settings.intStopTime);
		}
	}
	
	/**
	 * Run the MATLAB-script newPT, which finds all peaks and troughs
	 * in a given signal.
	 * The signal has to be pre-loaded into the MATLAB engine instance 
	 */
	private void peakDetection() {
		/* already done 
		//frmLoadData.engMatLab.engEvalString("y = data1(:, 1);"); // get the column HARDCODED 
		
		//int startTime = frmLoadData.getStartTime(); 
		//int stopTime = frmLoadData.getStopTime();
		*/
		System.out.println("SEttings: startTime: "+ settings.intStartTime + " and stopTime: "+ settings.intStopTime);
		engMatLab.engEvalString("y = y(" + settings.intStartTime + ":" + settings.intStopTime + ");"); // trim y? TODO: document error in puka? this was startTime, stopTime... resulting in y = scalar...
		//engMatLab.engEvalString("plot(y, 'm');");  //show the respiration signal so can check it
		
		// got the graph ready Do the Peak detection
		engMatLab.engEvalString("[P,T,th,Qd] = newPT(y, .1, onsetTime, endTime)");
	}
	
	
	/**
	 * Runs pukas classify peaks script, which labels all peaks and troughs found with
	 * 1,2 or 3 (index in peaksLabel corresponds to P, throughLabels to T) <- see peakDetection.
	 */
	private void classifyPeaks() {
		engMatLab.engEvalString("[peakLabels,troughLabels] = classifyPeaks(Qd,P,T,th);");
		// we can select only the validated peaks and troughs? We'll see
		// look in DoApply to see how changes are made... now peak/trough contains all: valid, invalid and questionable
	}
	
	/**
	 * This script is dependent upon validating the peaks and trough found.
	 * In puka, this validation is done by the user. If we are to improve the validation
	 * process, this is where we do it.
	 */
	private void pauseDetection() {
		engMatLab.engEvalString("[validPeaks, validTroughs] = makeValidArrays(P,T,peakLabels, troughLabels);"); // we use the peak/troughLabels for now, no changes
		
		engMatLab.engEvalString("[newP] = markPeakPauses(Qd, validPeaks, validTroughs, th);");
		engMatLab.engEvalString("[newT] = markTroughPauses(Qd, validPeaks, validTroughs, th);");
		engMatLab.engEvalString("plotPauses(Qd, validPeaks, validTroughs, th, newP, newT);");
	}
	

	/**
	 * Creates a new instance of JMatLinkAdapter and opens a connection. If an existing connection
	 * exists, this will reset the MATLAB engine.
	 */
	private void startMatlab() {
		if (engMatLab == null) {
			engMatLab = new JMatLinkAdapter();
		}
		engMatLab.engOpen(); // this resets if we already have a connection
		engMatLab.setDebug(debug); // whenever we start, set debug according to user preferences
	}
	
	/**
	 * Clean the launcher settings and step
	 */
	public void clean() {
		System.out.println("===Respiration Analyser---> Reset analysis");
		String temp;
		step = 1; // clear steps 
		if (settings != null) {
			temp = settings.filename; // keep filename
			settings = new Settings(); // clear all settings
			settings.filename = temp;
		} else {
			// initial run, we  don't have to do anything, launch creates settings
		} 
	}
	
	/**
	 * Destroy and close the MATLAB instance if it exists.
	 */
	public void kill() {
		if (engMatLab != null) {
			engMatLab.kill();
		}
	}

	/**
	 * Let the user toggle debug for the JMatLinkAdaper connection to MATLAB
	 * @return the new value, true if debug is set.
	 */
	public boolean toggleDebug() {
		
		if (engMatLab != null) {
			engMatLab.setDebug(!debug);
		} 
		debug = !debug;
		return debug;
	}
		
	/**
	 * Util method for printing debug information about which step in the respiration analysis
	 * we are in
	 * @param s - information about the step
	 */
	private void stepInfo(String s) {
		System.out.println("\tStep " + step++ + ": " + s + 
				"\n\t========================================="); // todo: underline s.length
	}
	
	/**
	 * Expose the clip length. This is also used as window size and need to be available for
	 * the stream gobbler class
	 * @return clip/window size
	 */
	public int getClipLength() {
		return settings.clipLength;
	}
	
	/** 
	 * Add buffer we want to analyse
	 */
	public void setBuffer(List<String> buffer) {
		this.buffer = buffer;
	}
	
	public void setAnalysisType(AnalysisType type) {
		this.type = type;
	}
}
