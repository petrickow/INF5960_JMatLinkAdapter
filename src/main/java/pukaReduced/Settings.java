package pukaReduced;

public class Settings {
	String filename;
	String scriptPath;
	
	int sampleRate = 1000; 
	
	int clipLength = 10 * sampleRate; // 10 sec at 1000 hz
	
	double dblStartTime = 0;  /** time point at which the stimulus started */
	double dblTotalTime = 0;   /** total time of the stimulus, as number of measurements. */
	int intStopTime = 0; 
	int intStartTime = 0;
	
	
	
	
	
	public Settings() {
		
		scriptPath = System.getProperty("user.dir") + "\\lib\\command\\matlabScripts";
		filename = System.getProperty("user.dir") + "\\signal.txt"; // default to signal.txt in root folder
		
	}
	

}
