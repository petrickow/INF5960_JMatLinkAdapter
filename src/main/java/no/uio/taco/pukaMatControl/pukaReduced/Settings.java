package no.uio.taco.pukaMatControl.pukaReduced;

/**
 * This class contains values that are used in the calculations done by
 * the puka matlab scripts. Contains both 
 * @author Cato Danielsen
 *
 */
public class Settings {
	public String fileName;
	public String scriptPath;
	
	int sampleRate = 1000; 
	
//	int windowSize; 
	int clipLength = 10 * sampleRate; // 10 sec at 1000 hz
	
	double dblStartTime = 0;  /** time point at which the stimulus started */
	double dblTotalTime = 0;   /** total time of the stimulus, as number of measurements. */
	int intStopTime = 0; 
	int intStartTime = 0;
	
	public Settings() {
		System.out.println("Creating settings object\nScript folder: " + System.getProperty("user.dir") + "\\matlabScripts" + "\nSignal folder: " +System.getProperty("user.dir"));
		scriptPath = System.getProperty("user.dir") + "\\matlabScripts";
		fileName = System.getProperty("user.dir") + "\\signal.txt"; // default to signal.txt in root folder
	}
	
	public String toString() {
		return this.toString();
	}
}
