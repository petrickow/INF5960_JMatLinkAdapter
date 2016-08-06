package no.uio.taco.pukaMatControl.pukaReduced;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import com.espertech.esper.epl.join.base.HistoricalIndexLookupStrategySorted;

import matlabcontrol.MatlabInvocationException;
import no.uio.taco.pukaMatControl.matControlAdapter.JMatLinkAdapter;
import no.uio.taco.pukaMatControl.puka.RespMeasures;
//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.Logger;
import no.uio.taco.pukaMatControl.puka.frmLoadData;
import no.uio.taco.pukaMatControl.puka.frmPreferences;

/* BIG TODO! Separate offline and online analyzer!!! */

/** 
 * Utility class for running pukas respiration analysis without GUI. 
 * @author Cato Danielsen
 */
public class RespirationAnalyser implements Runnable {
	
	public enum AnalysisType {
	    LOCAL, STREAM
	}
	public List<String> buffer;
	
	public boolean exitFlag;
	private int step = 1; // step counter for stepInfo method
	private boolean debug = true;
	private JMatLinkAdapter engMatLab = null;
	private static RespMeasures rmData;
	
	
	private List<String> currentWindow;
	private Settings settings;
	private List<String> history;
	private AnalysisType type;
//	private Logger log; // TODO: 
	int offset = 0; // used for writing results
	
	public RespirationAnalyser() {
		settings = new Settings(); // contains all variables for this analysis
		history = new ArrayList<String>();
		type = AnalysisType.LOCAL; // default
	}
	
	
	/**
	 * Constructor instantiates Settings and History object
	 */
	public RespirationAnalyser(List<String> buffer) {
		//log = Logger.getLogger(this.getClass()); // TODO log4j
		//BasicConfigurator.configure();
		//log.setAdditivity(false);
		currentWindow = new ArrayList<String>();
		
		settings = new Settings(); // contains all variables for this analysis
		history = new ArrayList<String>();
		
		this.buffer = buffer;
		type = AnalysisType.STREAM; // default when creating with buffer
		exitFlag = false;
	}
	
	private synchronized void holdUp() {
		try {
			System.out.println("Wait for buffer... " + this.toString());
			this.wait();
			System.out.println("Done waiting: " + currentWindow.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/**
	 * The thread is only run when analyzing a stream
	 */
	public void run() {
		while(true) {
			while (currentWindow.size() == 0) {
				holdUp();
			}
			try {
				if (exitFlag) {
					break;
				}
				System.out.println("Start analysis with window: " + currentWindow.size() + " History: "+ history.size());
				
				analyseRTWindow(currentWindow);
				currentWindow.clear(); 
			} catch (MatlabInvocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
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
			settings.fileName = System.getProperty("user.dir") + "\\" + fname; // no fault handeling
		}

		File textDataFile = new File(settings.fileName);
		//log.debug("Signal file:\t" + settings.filename + "\n============\texists: " + textDataFile.exists()); 
		
		
		if (textDataFile.exists()) {
			long start, end1, end2, end3, end4, end5 = 0;
			startMatlab();
			step = 1;
			/**
			 * Step 1, load data, set start and end time
			 */
			
			stepInfo("load data, set start and end time");
			start = System.nanoTime();
			loadFile(textDataFile);
			
			/* TODO, should this be in here or in analyseResp()? AND
			it should be modified to work automatically, that is if it does not
			find onset time, we need to do some magic*/
			setOnset();
			end1 = System.nanoTime();
			
			
			/**
			 * Step 2, peak detection, find peaks and trough 
			 */
			stepInfo("peak detection");
			peakDetection();
			
			end2 = System.nanoTime();
			/**
			 * Step 3, classify peaks -> this is done manually in puka, we need to find a way to automate this process
			 */
			stepInfo("classify peaks");
			classifyPeaks();
			
			end3 = System.nanoTime();
			/**
			 * Step 4,
			 */
			stepInfo("pause detection:\n\t"
					+ "Uses matlab again to detect the start and end point of the pause around the peak");
			pauseDetection();
			
			end4 = System.nanoTime();
			
			/**
			 * Step 5, statistical calculation 
			 */
			stepInfo("statistical calculation:\n\t"
					+ "This step looks at the information gathered in the current clip, and has\n\t"
					+ "to be modified in order to be used in a meaningful way for realtime analysis.\n\t"
					+ "Look into how to extract the events, CalculateResp:596");
			writeStatisticalInfo();
			end5 = System.nanoTime();
			
			printTiming(start, end1, end2, end3, end4, end5);
			
		} else {
			System.out.println("File not found");
		}
		
	}
	
	


	/***************************** ONLINE PART *************************/

	/**
	 * Fetches clip size from shared buffer and analyzes the window
	 */
	public void analyseRTWindow(List<String> buffer) throws MatlabInvocationException {
		
		long analyseWindowStartTime = System.currentTimeMillis();
		step = 1;
		engMatLab.engEvalString("clear;");  // remove all previous information in workspace, if any

		/* STEP 1: Load data */
		buffer = loadDataIntoMatlab(buffer);
		System.out.println("===analyseWindow-->\tTOTAL SIZE (including history): " + buffer.size());
		/* STEP 1 cont:  find onset */
		engMatLab.engEvalString("onsetTime = findOnset(y);");  //run function, put result in intNew
	
//		double onsetTime = engMatLab.engGetScalar("onsetTime");
		if (setOnset()) { 
			/* Why not just read to buffer.size() = endTime? */
//			if (settings.intStopTime > buffer.size()) { 
			settings.intStopTime = buffer.size();
			engMatLab.engEvalString("endTime = " + settings.intStopTime); // always analyze to the end of the buffer?
//			}
				
			analyseResp();
			engMatLab.engEvalString("writeResults(" + offset + ", newP, newT);");
			offset += settings.clipLength; // /5; // Divide by five to get the index for decimated signal, or multiply with five to get the index for raw signal (1000hz)
			
			long analyseWindowStopTime = System.currentTimeMillis();
			long endTime = analyseWindowStopTime - analyseWindowStartTime;
			
			 // clear history list
			
			System.out.println("====RESP ANALYSER: Complete analysis ms: " + endTime + "\n\t\tHistory size: " + history.size());
			preserveHistory(buffer);
			System.out.println("====History preserved: " + history.size());
			
		} else {
			history.addAll(buffer);
			System.out.println("====RESP ANALYSER: Failed to find OnsetTime in signal\n\t"
					+ "Window too small?\n\t"
					+ "Long respiration halt?");
		}
	}

	/**
	 * @param buffer
	 */
	private List<String> loadDataIntoMatlab(List<String> buffer) {
		stepInfo("load data, set start and end time");
		System.out.println(history.size()+ " + " + buffer.size() + " = " + (history.size()+ buffer.size())); 
		
		double[][] data1 = new double[1][history.size()+buffer.size()]; // convert to matlab friendly type
		ArrayList<String> concatenated =  new ArrayList<String>(history.size()+buffer.size());

		int index = 0;
		
		while (index < history.size()) {
			try {
				data1[0][index] = Double.parseDouble(history.get(index));
				concatenated.add(history.get(index++));
			}
			catch (NumberFormatException e) {
				System.out.println("Malformed entry in buffer (" + e.getMessage() + "):\n\t"+buffer.get(index));
			}
		}
		
		System.out.print("History to: " + index + " ...");
		
		for (int bufferIndex = 0; index < (history.size() + buffer.size()); index++) {
			try {
				data1[0][index] = Double.parseDouble(buffer.get(bufferIndex));
				concatenated.add(buffer.get(bufferIndex++));
			} catch (NumberFormatException e) {
				System.out.println("Malformed entry in buffer (" + e.getMessage() + "):\n\t"+buffer.get(index));
			}
		}
		System.out.println("Buffer to: " + index + " = TOTAL CLIP SIZE: " + history.size() + buffer.size());
		settings.clipLength = index; // keep track of the entire signal length
		
		engMatLab.engPutArray("data1", data1); // load record
		
		engMatLab.engEvalString("y = data1(1, :);");  // get trigger col into y in matlab
		return concatenated;
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
		
//		log.debug("Change MATLAB folder to script path: " + settings.scriptPath);
		engMatLab.engEvalString("cd ('" + settings.scriptPath + "');"); // settings path to scripts
		engMatLab.engEvalString("onsetTime = findOnset(y);");  //run function, put result in intNew
	}
	
	/**
	 * Initiate respiration analysis. MATLAB session has to be running,
	 */
	private void analyseResp() {
		/*
		 * Step 2, peak detection, find peaks and trough 
		 */
		stepInfo("peak detection");
		peakDetection();
		/*
		 * Step 3, classify peaks -> this is done manually in puka, we need to find a way to automate this process
		 */
		stepInfo("classify peaks");
		classifyPeaks();
		/*
		 * Step 4,
		 */
		stepInfo("pause detection:\n\t"
				+ "Uses matlab again to detect the start and end point of the pause around the peak");
		pauseDetection();
	}
	
	
	/******************ANALYSIS MATLAB INTERACTIONS *************************/
	
	/**
	 * Get the onset time calculated in MATLAB and
	 * set start and end time for the clip used
	 * in the respiration analysis
	 */
	private boolean setOnset() {
		double dblTemp = -1;

		try{
			dblTemp = engMatLab.engGetScalar("onsetTime");  //get the stimulus onset time point
		}
		catch (MatlabInvocationException e) {
//			log.error("Error during getScalar. Unable to retrieve onset time from MATLAB");
			e.printStackTrace();
			System.exit(1); // TODO: better error handling
		}

//		if findOnset returns a time of -1 then it was unable to locate a good onset time
		if ((int)dblTemp < 0) { 
			System.out.println("not able to get a good onset!");  
			return false; 
		}  
		else {
			System.out.println("===setOnset()--->: " + (int)dblTemp);
			settings.intStartTime = (int)dblTemp;  //assign start time to public variable

//			TODO: add check to make sure we do not exceed the buffer.size
			settings.intStopTime = settings.clipLength + settings.intStartTime;

//			inform MATLAB. This should include a check to make sure we do not exceed record size
			engMatLab.engPutArray("endTime", (double)settings.intStopTime);
			return true;
		}
	}
	
	/**
	 * Run the MATLAB-script newPT, which finds all peaks and troughs
	 * in a given signal.
	 * The signal has to be pre-loaded into the MATLAB engine instance 
	 */
	private void peakDetection() {
		/* already done in setOnset
//		int startTime = frmLoadData.getStartTime(); 
//		int stopTime = frmLoadData.getStopTime();
		*/
		
		System.out.println("Settings: startTime: "+ settings.intStartTime + " and stopTime: "+ settings.intStopTime);
		engMatLab.engEvalString("y = y(" + settings.intStartTime + ":" + settings.intStopTime + ");"); // trim y? TODO: document error in puka? this was startTime, stopTime... resulting in y = scalar...
//		engMatLab.engEvalString("plot(y, 'm');");  //show the respiration signal so can check it
		
//		got the graph ready Do the Peak detection // TODO Swap .1 with param
		engMatLab.engEvalString("[P,T,th,Qd] = newPT(y, .1, onsetTime, endTime)");
	}
	
	/**
	 * Runs pukas classify peaks script, which labels all peaks and troughs found with
	 * 1,2 or 3 (index in peaksLabel corresponds to P, throughLabels to T) <- see peakDetection.
	 */
	private void classifyPeaks() {
		engMatLab.engEvalString("[peakLabels,troughLabels] = classifyPeaks(Qd,P,T,th);");
//		we can select only the validated peaks and troughs? We'll see
//		look in DoApply to see how changes are made... now peak/trough contains all: valid, invalid and questionable
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
	
	
	/********************** UTILS ********************************/
	
	/**
	 * Looks for the last found event, and adds the remainder of the signal to
	 * the history buffer
	 */
	private void preserveHistory(List<String> buffer) { //*TODO: check to see  that we actually has anything in the arrays
		history.clear();
		
		double peakMax = 0;
		double troughMax = 0;
		
		double[][] P = engMatLab.engGetArray("P");
		double[][] T = engMatLab.engGetArray("T");
		
		if (P[0].length > 0) { peakMax = P[0][P[0].length-1]; } // get the last detected value of both p&t
		if (T[0].length > 0) { troughMax = T[0][T[0].length-1]; }
		
		System.out.println("===Preserve History--->\tTrough MAX at i: " + (T[0].length-1) + ": "+ troughMax + " P MAX at i: " + (P[0].length-1) + " : " + peakMax);
		
		if ((int)peakMax > 0 || (int)troughMax > 0) {
			double max = (peakMax > troughMax) ? peakMax : troughMax;
			int topIndex = (int) max * 5; // why 5? The matlab scripts decimate the signal with a factor of 5!
			List<String> preserve = buffer.subList(topIndex, settings.intStopTime);
			history.addAll(preserve);
//			plotHistory(); //  
		} else {
//			No information retrieved from the window, try again with more info.
			history.addAll(buffer); 
		}
	}

	/**
	 * Plots the preserved history via matlab for visual verification of the
	 * validity of the stored values
	 */
	private void plotHistory() {
		double[][] hi = new double[1][history.size()];
		
		for (int i = 0; i < history.size(); i++) {
			hi[0][i] = Double.parseDouble(history.get(i));
		}
		engMatLab.engPutArray("hist", hi);
		engMatLab.engEvalString("figure; plot(hist);");
	}
	
	/**
	 * Fluff?
	 */
	public void launchMatlabInstance() {
		startMatlab();
		initializeMatlabVariables();
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
		
		engMatLab.engEvalString("cd ('" + settings.scriptPath + "');"); // settings path to scripts
	}
	
	/**
	 * Clean the launcher settings and step
	 */
	public void clean() {
		System.out.println("===Respiration Analyser---> Reset analysis");
		String temp;
		step = 1; // clear steps 
		if (settings != null) {
			temp = settings.fileName; // keep filename
			settings = new Settings(); // clear all settings
			settings.fileName = temp;
		} else {
//			 initial run, we  don't have to do anything, launch creates settings
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
	 * Let the user toggle debug print for the JMatLinkAdaper connection to MATLAB
	 * @return the new value, true if debug is set.
	 */
	public boolean toggleDebug() {
		
		if (engMatLab != null) {
			engMatLab.setDebug(!debug);
		} 
		debug = !debug;
		return debug;
	}
	
	public synchronized void signalExit() {
		exitFlag = true;
	}
		
	/**
	 * Utility method for printing debug information about which step in the 
	 * respiration analysis we are in
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
	 * Add buffer we want to analyze
	 */
	public void setBuffer(List<String> buffer) {
		System.out.println("new window: " + buffer.size());
		this.currentWindow = buffer;
	}

	/**
	 * Not in use any more after change from running thread for online analysis 
	 * @param type
	 */
	public void setAnalysisType(AnalysisType type) {
		this.type = type;
	}
	
	private void printTiming(long start, long end1, long end2, long end3, long end4, long end5) {
		// TODO Auto-generated method stub
	}


	
	/**
	 * To reduce the analysis execution of the first window
	 * we have to set everything up in the MATLAB workspace
	 */
	private void initializeMatlabVariables() {
//		TODO: Identify why the first analysis is 10 times slower than
//		subsequent iterations	
		
	}
	
	private void writeStatisticalInfo() throws MatlabInvocationException {
		
		//sub does calculations in matlab on the troughs array - locations where done breathing out but not
	    //yet started breathing in - to find basic statistics on the respiration during the stimulus
	    double dblTemp = 0; BigDecimal jcBigDec; double dblTi = 0; double dblTtot = 0; int intSampling = 0;
			
		NumberFormat jcNumberFormat = NumberFormat.getInstance(Locale.US);  //set up NumberFormat for the USA
		jcNumberFormat.setMaximumFractionDigits(4);  //only two digits after the decimal shown
		intSampling = settings.sampleRate;
			
	    engMatLab.engEvalString("length(troughs);");  //# of breaths
	    dblTemp = engMatLab.engGetScalar("ans");
	    //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 13, 2);
			
	    engMatLab.engEvalString("min(diff(troughs));");  //shortest breath
	    dblTemp = engMatLab.engGetScalar("ans");
		dblTemp = dblTemp/intSampling;
	    
	    //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 14, 2);		
			
			engMatLab.engEvalString("max(diff(troughs));");  //longest breath
	    dblTemp = engMatLab.engGetScalar("ans");
			dblTemp = dblTemp/intSampling;
	    //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 15, 2);
	      
			engMatLab.engEvalString("mean(diff(troughs));");  //average breath length
	    dblTemp = engMatLab.engGetScalar("ans");
	    dblTemp = 60000 / dblTemp;  
			//tblResults.setValueAt(jcNumberFormat.format(dblTemp), 11, 2);
				
	    engMatLab.engEvalString("std(diff(troughs));");  //standard deviation of breath length
	    dblTemp = engMatLab.engGetScalar("ans");
			dblTemp = dblTemp/intSampling;
	    //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 12, 2);
			
			//post-inspiratory & expiratory pause calculations
	    dblTemp = engMatLab.engGetScalar("avgPI");
			dblTemp = dblTemp/intSampling;
	    //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 6, 2);
	    
			dblTemp = engMatLab.engGetScalar("stdPI");	
			dblTemp = dblTemp/intSampling;
	    //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 7, 2);			
			
			dblTemp = engMatLab.engGetScalar("avgPE");		
			dblTemp = dblTemp/intSampling;
	    //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 8, 2);
			
			dblTemp = engMatLab.engGetScalar("stdPE");	
			dblTemp = dblTemp/intSampling;
	    //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 9, 2);
			
			//total cycle time calculations
	    dblTemp = engMatLab.engGetScalar("avgTtot");
			dblTtot = dblTemp;  //save Ttotal in variable to calculate indpiration duty cycle later
			dblTemp = dblTemp/intSampling;
	    //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 0, 2);
			
	    dblTemp = engMatLab.engGetScalar("stdTtot");
			dblTemp = dblTemp/intSampling;
	    //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 1, 2);		
			
			//inspiration and expiration time calculations
	      dblTemp = engMatLab.engGetScalar("avgTI");
				dblTi = dblTemp;  //save inspiration time for inspiratory duty cycle calculation
				dblTemp = dblTemp/intSampling;
	      //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 2, 2);
				
				dblTemp = engMatLab.engGetScalar("stdTI");
				dblTemp = dblTemp/intSampling;
	      //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 3, 2);
	      
				dblTemp = engMatLab.engGetScalar("avgTE");
				dblTemp = dblTemp/intSampling;
	      //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 4, 2);
	      
				dblTemp = engMatLab.engGetScalar("stdTE");
				dblTemp = dblTemp/intSampling;
	      //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 5, 2);
				
				//inspriratory duty cycle 
				dblTemp = dblTi/dblTtot;	
	      //tblResults.setValueAt(jcNumberFormat.format(dblTemp), 10, 2);
    }

	
	static class RespMeasures {
		
		private static java.math.BigDecimal totalBreathStdDev;
		private static java.math.BigDecimal totalBreathMean;	
		private static java.math.BigDecimal inspTimeMean;	
		private static java.math.BigDecimal inspTimeStdDev;
		private static java.math.BigDecimal expTimeMean;
		private static java.math.BigDecimal expTimeStdDev;
		private static java.math.BigDecimal postInspPauseMean;
		private static java.math.BigDecimal postInspPauseStdDev;
		private static java.math.BigDecimal postExpPauseMean;
		private static java.math.BigDecimal postExpPauseStdDev;
		private static java.math.BigDecimal inspDutyTimeMean;
		private static java.math.BigDecimal inspDutyTimeStdDev;
		private static java.math.BigDecimal respRateMean;
		private static java.math.BigDecimal respRateStdDev;
		private static int subID;
		private static int sessionID;
		private static String clipID = "";
		private static boolean bolIsValid = false;
		private static String strComment = "";
		private static ArrayList peakList = new ArrayList();  //list of Integers
		private static ArrayList troughList = new ArrayList();
		private static ArrayList peakPauseList = new ArrayList();
		private static ArrayList troughPauseList = new ArrayList();
		private static int intNumBreaths = 0;
		private static java.math.BigDecimal shortestBreath;
		private static java.math.BigDecimal longestBreath;	
		
		/** Creates a new instance of RespMeasures */
		public RespMeasures() { 	}
		
		static void setPeakList(ArrayList peakList1) { peakList = peakList1; }
		static ArrayList getPeakList() { return peakList; }
		
		static void setTroughList(ArrayList troughList1) { troughList = troughList1; }
		static ArrayList getTroughList() { return troughList; }
		
		static void setPeakPauseList(ArrayList peakPauseList1) { peakPauseList = peakPauseList1; }
		static ArrayList getPeakPauseList() { return peakPauseList; }
		
		static void setTroughPauseList(ArrayList troughPauseList1) { troughPauseList = troughPauseList1; }
		static ArrayList getTroughPauseList() { return troughPauseList; }
		
		static void setComment(String strComment1) { strComment = strComment1; }
		static String getComment() { return strComment; }
		
		static void setIsValid(boolean bolValid) { bolIsValid = bolValid; }
		static boolean getIsValid() { return bolIsValid; }
		
		static void setSubID(int intSubID) { subID = intSubID; }
		static int getSubID() { return subID; }
		
		static void setClipID(String strClipID) { clipID = strClipID; }
		static String getClipID() { return clipID; }
		
		static void setSessionID(int intSessionID) { sessionID = intSessionID; }
		static int getSessionID() { return sessionID; }
		
		static java.math.BigDecimal getTotalBreathMean() { return totalBreathMean; }
		static void setTotalBreathMean(BigDecimal bdTotalBreathMean) { totalBreathMean = bdTotalBreathMean; }
		
		static java.math.BigDecimal getTotalBreathStdDev() { return totalBreathStdDev; }
		static void setTotalBreathStdDev(BigDecimal bdTotalBreathStdDev) { totalBreathStdDev = bdTotalBreathStdDev; }
		
		static void setInspTimeMean(BigDecimal bdInspTimeMean) { inspTimeMean = bdInspTimeMean; }
		static java.math.BigDecimal getInspTimeMean() { return inspTimeMean; }
		
		static void setInspTimeStdDev(java.math.BigDecimal bdInspTimeStdDev) { inspTimeStdDev = bdInspTimeStdDev; }
		static java.math.BigDecimal getInspTimeStdDev() { return inspTimeStdDev; }
		
		static void setExpTimeMean(BigDecimal bdExpTimeMean) { expTimeMean = bdExpTimeMean; }
		static java.math.BigDecimal getExpTimeMean() { return expTimeMean; }
		
		static void setExpTimeStdDev(BigDecimal bdExpTimeStdDev) { expTimeStdDev = bdExpTimeStdDev; }
		static java.math.BigDecimal getExpTimeStdDev() { return expTimeStdDev; }
		
		static void setPostInspPauseMean(BigDecimal bdPass) { postInspPauseMean = bdPass; }
		static java.math.BigDecimal getPostInspPauseMean() { return postInspPauseMean; }
		
		static void setPostInspPauseStdDev(BigDecimal bdPostInspPauseStdDev) { postInspPauseStdDev = bdPostInspPauseStdDev; }
		static java.math.BigDecimal getPostInspPauseStdDev() { return postInspPauseStdDev; }
		
		static void setPostExpPauseMean(java.math.BigDecimal bdPostExpPauseMean) { postExpPauseMean = bdPostExpPauseMean; }
		static java.math.BigDecimal getPostExpPauseMean() { return postExpPauseMean; }
		
		static void setPostExpPauseStdDev(java.math.BigDecimal bdPostExpPauseStdDev) { postExpPauseStdDev = bdPostExpPauseStdDev; }
		static java.math.BigDecimal getPostExpPauseStdDev() { return postExpPauseStdDev; }
		
		static void setInspDutyTimeMean(BigDecimal bdPass) { inspDutyTimeMean = bdPass; }	
		static java.math.BigDecimal getInspDutyTimeMean() { return inspDutyTimeMean; }
		
		static void setInspDutyTimeStdDev(java.math.BigDecimal bdInspDutyTimeStdDev) { inspDutyTimeStdDev = bdInspDutyTimeStdDev; }
		static java.math.BigDecimal getInspDutyTimeStdDev() { return inspDutyTimeStdDev; }
		
		static void setRespRateMean(java.math.BigDecimal bdRespRateMean) { respRateMean = bdRespRateMean; }
		static java.math.BigDecimal getRespRateMean() { return respRateMean; }
		
		static void setRespRateStdDev(java.math.BigDecimal bdRespRateStdDev) { respRateStdDev = bdRespRateStdDev; }
		static java.math.BigDecimal getRespRateStdDev() { return respRateStdDev; }
		
		static void setNumBreaths(int intNumBreaths1) { intNumBreaths = intNumBreaths1; }
		static int getNumBreaths() { return intNumBreaths; }

		static void setShortestBreath(BigDecimal bdShortestBreath) { shortestBreath = bdShortestBreath; }	
		static java.math.BigDecimal getShortestBreath() { return shortestBreath; }

		static void setLongestBreath(BigDecimal bdLongestBreath) { longestBreath = bdLongestBreath; }	
		static java.math.BigDecimal getLongestBreath() { return longestBreath; }

		static void BlankOutValues() {
			//set all of the things to zero
			
			totalBreathStdDev = new BigDecimal(0); totalBreathMean = new BigDecimal(0);	
			inspTimeMean = new BigDecimal(0);	inspTimeStdDev = new BigDecimal(0); 
			expTimeMean = new BigDecimal(0); expTimeStdDev = new BigDecimal(0);
			postInspPauseMean = new BigDecimal(0); postInspPauseStdDev = new BigDecimal(0);
			postExpPauseMean = new BigDecimal(0); postExpPauseStdDev = new BigDecimal(0); 
			inspDutyTimeMean = new BigDecimal(0); inspDutyTimeStdDev = new BigDecimal(0); 
			respRateMean = new BigDecimal(0); respRateStdDev = new BigDecimal(0);
			subID = 0; sessionID = 0; clipID = ""; bolIsValid = false; strComment = "";
			peakList = new ArrayList(); troughList = new ArrayList();
			peakPauseList = new ArrayList(); troughPauseList = new ArrayList();
			intNumBreaths = 0; shortestBreath = new BigDecimal(0); longestBreath = new BigDecimal(0);	
		}
		
	}
}

	
