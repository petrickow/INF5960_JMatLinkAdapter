package no.uio.taco.pukaMatControl.pukaReduced;

import java.io.File;
import matlabcontrol.MatlabInvocationException;
import no.uio.taco.pukaMatControl.matControlAdapter.JMatLinkAdapter;

/** 
 * Utility class for running pukas respiration analysis without GUI. 
 * @author Cato Danielsen
 */
public class RespirationAnalyser {
	
	private int step = 1; // step counter for stepInfo method
	private boolean debug = true;
	private JMatLinkAdapter engMatLab = null; 
	
	public Settings settings;
	private History history;
	
	/**
	 * Launches a recreation of pukas respiration analysis
	 * with file name of local signal file which must be located in
	 * current directory. TODO: support absolute path
	 * @param fname - name of signal file
	 */
	public void launchLocalFile(String fname) {
		settings = new Settings(); // contains all variables for this analysis
		history = new History();

		// 1: Get data if specified 
		if (fname.length() > 0) {
			settings.filename = System.getProperty("user.dir") + "\\" + fname; // no fault handeling
		}
		
		File textDataFile = new File(settings.filename);
		System.out.println("Signal file:\t" + settings.filename + "\n============\texists: " + textDataFile.exists()); 
		
		if (textDataFile.exists()) {
			startMatlab();
			/**
			 * Step 1, load data, set start and end time
			 */
			stepInfo("load data, set start and end time");
			loadFile(textDataFile);
			setOnset();
			
			analyseResp();
		}
	}

	/**
	 * Initiate respiration analysis. MATLAB session has to be running,
	 * 
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
		
		
		/**
		 * Step 5, statistical calculation 
		 */
		stepInfo("statistical calculation:\n\t"
				+ "This step looks at the information gathered in the current clip, and has\n\t"
				+ "to be modified in order to be used in a meaningful way for realtime analysis.\n\t"
				+ "Look into how to extract the events");
	}
	
	private void setOnset() {
		double dblTemp = -1;

		try{
			dblTemp = engMatLab.engGetScalar("onsetTime");  //get the stimulus onset time point
		}
		catch (MatlabInvocationException e) {
			System.out.println("Error during getScalar");
			e.printStackTrace();
			System.exit(1); // TODO: better error handling
		}
		//if findOnset returns a time of -1 then it was unable to locate a good onset time
		if ((int)dblTemp < 0) { 
			System.out.println("not able to get a good onset!");  
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
	
	private void loadFile(File f) {
		engMatLab.engEvalString("clear;");  // remove all previous information in workspace, if any
		engMatLab.engEvalString("data1 = load('" + f.getPath() + "');");  // load data file
		engMatLab.engEvalString("y = data1(:, 1);");  // get trigger col into y in matlab
		
		System.out.println("Change MATLAB folder to script path: " + settings.scriptPath);
		engMatLab.engEvalString("cd ('" + settings.scriptPath + "');"); // settings path to scripts
		
		engMatLab.engEvalString("onsetTime = findOnset(y);");  //run function, put result in intNew
	}
	
	private void peakDetection() {
		/* already done 
		//frmLoadData.engMatLab.engEvalString("y = data1(:, 1);"); // get the column HARDCODED 
		
		//int startTime = frmLoadData.getStartTime(); 
		//int stopTime = frmLoadData.getStopTime();
		*/
		engMatLab.engEvalString("y = y(" + settings.intStartTime + ":" + settings.intStopTime + ");"); // trim y? TODO: document error in puka? this was startTime, stopTime... resulting in y = scalar...
		//engMatLab.engEvalString("plot(y, 'm');");  //show the respiration signal so can check it
		
		// got the graph ready Do the Peak detection
		engMatLab.engEvalString("[P,T,th,Qd] = newPT(y, .1, onsetTime, endTime)");
	}
	
	private void classifyPeaks() {
		engMatLab.engEvalString("[peakLabels,troughLabels] = classifyPeaks(Qd,P,T,th);");
		// we can select only the validated peaks and troughs? We'll see
		// look in DoApply to see how changes are made... now peak/trough contains all: valid, invalid and questionable
	}
	
	private void pauseDetection() {
		engMatLab.engEvalString("[validPeaks, validTroughs] = makeValidArrays(P,T,peakLabels, troughLabels);"); // we use the peak/troughLabels for now, no changes
		
		engMatLab.engEvalString("[newP] = markPeakPauses(Qd, validPeaks, validTroughs, th);");
		engMatLab.engEvalString("[newT] = markTroughPauses(Qd, validPeaks, validTroughs, th);");
		engMatLab.engEvalString("plotPauses(Qd, validPeaks, validTroughs, th, newP, newT);");
	}
	
	private void startMatlab() {
		if (engMatLab == null) {
			engMatLab = new JMatLinkAdapter();
		}
		engMatLab.engOpen(); // this resets if we already have a connection
		engMatLab.setDebug(debug); // whenever we start, set debug according to user perferences
	}
	
	/**
	 * Clean the launcher settings and step
	 */
	public void clean() {
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
	
	public void kill() {
		if (engMatLab != null) {
			engMatLab.kill();
		}
	}

	public boolean toggleDebug() {
		
		if (engMatLab != null) {
			engMatLab.setDebug(!debug);
		} 
		debug = !debug;
		return debug;
	}
	
	private void stepInfo(String s) {
		System.out.println("\tStep " + step++ + ": " + s + 
				"\n\t========================================="); // todo: underline s.length
	}
	
	public int getClipLength() {
		return settings.clipLength;
	}
}
