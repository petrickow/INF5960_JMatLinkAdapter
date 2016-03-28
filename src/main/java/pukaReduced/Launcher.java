package pukaReduced;

import java.io.File;
import matlabcontrol.MatlabInvocationException;
import no.uio.taco.pukaMatControl.matControlAdapter.JMatLinkAdapter;

public class Launcher {
	private static int step = 1;
	private static JMatLinkAdapter engMatLab;
	
	private static Settings settings;
	
	
	public static void main(String[] args) {
		settings = new Settings(); // contains all variables
		
		// 1: Get data if specified 
		if (args.length == 1) {
			settings.filename = System.getProperty("user.dir") + "\\" + args[0]; // no fault handeling
		}
		
		
		File textDataFile = new File(settings.filename);
		System.out.println("Signal file:\t" + settings.filename + "\n============\texists: " + textDataFile.exists()); 
		
		if (textDataFile.exists()) {
			/**
			 * Step 1, load data, set start and end time
			 */
			stepInfo("load data, set start and end time");
			
			double dblTemp = -1;
			
			
			// 2: Start MATLAB instance
			System.out.println("Ask matlab to read: "+textDataFile.getPath() + "\nBut first, lets start MATLAB..."); // start loading thread?
			startMatlab();
			System.out.println("Read the file, get onsetime ");
			loadFile(textDataFile);
			try{
				dblTemp = engMatLab.engGetScalar("onsetTime");  //get the stimulus onset time point
			}
			catch (MatlabInvocationException e) {
				System.out.println("Error during getScalar");
				e.printStackTrace();
				System.exit(1);
			}
			//if findOnset returns a time of -1 then it was unable to locate a good onset time
			if ((int)dblTemp <= 0) { 
				System.out.println("not able to get a good onset!");  
				// what to do?
				System.exit(1);
			}  
			else {
				settings.intStartTime = (int)dblTemp;  //assign start time to public variable
				settings.intStopTime = settings.clipLength + settings.intStartTime; 
				// inform
				engMatLab.engPutArray("endTime", (double)settings.intStopTime);
			}
			
			/**
			 * Step 2, peak detection, find peaks and trough 
			 */
			stepInfo("peak detection");
			peakDetection();
			
		}
		/**
		 * flyt: 
		 * 1 hent inn data
		 * 2 start matlab
		 * 3 basert på pre definerte verdier, start analyse
		 * 4 lagre resultat
		 */
		
		//
	}
	
	private static void loadFile(File f) {
		engMatLab.engEvalString("clear;");  //remove all previous information in workspace, if any
		engMatLab.engEvalString("data1 = load('" + f.getPath() + "');");  //load data file
		engMatLab.engEvalString("y = data1(:, 1);");  //get trigger col into y in matlab
		
		System.out.println("Change MATLAB folder to script path: " + settings.scriptPath);
		engMatLab.engEvalString("cd ('" + settings.scriptPath + "');"); // settings path to scripts
		
		engMatLab.engEvalString("onsetTime = findOnset(y);");  //run function, put result in intNew
	}
	
	private static void peakDetection() {
		/* already done */
		//frmLoadData.engMatLab.engEvalString("y = data1(:, 1);"); // get the column HARDCODED 
		
		//int startTime = frmLoadData.getStartTime(); 
		//int stopTime = frmLoadData.getStopTime();
		engMatLab.engEvalString("y = y(" + settings.intStartTime + ":" + settings.intStopTime + ");"); // trim y? TODO: document error in puka? this was startTime, stopTime... resulting in y = scalar...
		//engMatLab.engEvalString("plot(y, 'm');");  //show the respiration signal so can check it
		
		// got the graph ready Do the Peak detection
		engMatLab.engEvalString("[P,T,th,Qd] = newPT(y, .1, onsetTime, endTime)");
	}
	
	private static void startMatlab() {
		
		engMatLab = new JMatLinkAdapter();
		engMatLab.engOpen(); 
	}
	
	private static void stepInfo(String s) {
		System.out.println("\tStep "+step++ +": " + s + "\n\t========================================="); // todo: underline s.length
	}

}
