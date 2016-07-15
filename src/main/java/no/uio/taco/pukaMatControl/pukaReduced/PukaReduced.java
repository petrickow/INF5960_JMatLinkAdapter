package no.uio.taco.pukaMatControl.pukaReduced;

import java.util.Scanner;

/**
 * Replication of the respiration analysis found in puka.
 * This program allow developers to quickly run the respiration analysis done with the 
 * MATLAB scripts. The data is read from a one column text file.
 * @author 		Cato Danielsen
 * @version 	0.0.3
 *
 */
public class PukaReduced {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
	        @Override
            public void run() {
        		System.out.println("\tGot terminated\n"
        				+ "but does not hook when terminating in eclipse...");	
    			//Launcher.kill();
            }   
        }); 
		
		String fname = parseArgs(args);
		Scanner keyboard = new Scanner(System.in);
		printHelp();
		
		String input = ""; 
		while (!input.equalsIgnoreCase("quit")) {

			System.out.print("$> ");
			input = keyboard.nextLine().trim();
			switch (input) {
				case "debug": 
					System.out.println("setting Debug to: " + Launcher.toggleDebug());
					break;
				case "run": 
					Launcher.clean(); //just in case we already have run it once 
					Launcher.launch(fname); 
					break;
				case "stream": launchStream(); break;
				case "help": printHelp(); break;
				case "quit": System.out.println("bye"); break;
				case "pwd": System.out.println(System.getProperty("user.dir")); 
				default: printHelp();
			}
		}
		keyboard.close();
		Launcher.kill();
	}
	
	private static String parseArgs(String[] args) {
		
		
		return "";
	}
	
	
	private static void launchStream() {

		System.out.println("Starting second thread");
		Thread gobbler = new Thread(new StreamGobbler());
		gobbler.start();
		

	}
	
	private static void printHelp() {
		System.out.println("puka test shell:\n\t"
				+ "run: execute puka test (using signal.txt)\n\t"
				+ "debug: toggles debug print from matlabcontrol\n\t"
				+ "help: this\n\t"
				+ "\n\tquit: close matlab and quit");
	}
}
