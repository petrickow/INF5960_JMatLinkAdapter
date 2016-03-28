package no.uio.taco.pukaMatControl.pukaReduced;

import java.util.Scanner;

/**
 * Replication of the respiration analysis found in puka.
 * This program allow developers to quickly run the respiration analysis done with the 
 * MATLAB scripts. The data is read from a one column text file.
 * @author 		Cato Danielsen
 * @version 	0.0.1
 *
 */
public class PukaReduced {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
	        @Override
            public void run() {
        		System.out.println("\tGot terminated, but does not show up when terminating in eclipse...");	
    			//Launcher.kill();
            }   
        }); 
		
		
		String fname = parseArgs(args);
		Scanner keyboard = new Scanner(System.in);
		printHelp();
		
		String input = ""; 
		while (!input.equalsIgnoreCase("quit")) {
			
			
			System.out.print("> ");
			input = keyboard.nextLine().trim();
			switch (input) {
				case "debug": 
					System.out.println("Debug = " + Launcher.toggleDebug());
					break;
				case "run": 
					Launcher.clean(); //just in case we already have run it once 
					Launcher.launch(fname); 
					break;
				case "help": printHelp(); break;
				case "quit": System.out.println("bye"); break;
				default: printHelp();
			}
			
			
		}
		keyboard.close();
		Launcher.kill();
	}
	
	
	private static String parseArgs(String[] args) {
		
		return "";
	}
	
	private static void printHelp() {
		System.out.println("puka test shell:\n\t"
				+ "run: execute puka test\n\t"
				+ "debug: toggles debug print from MATLAB\n\t"
				+ "help: this\n\t"
				+ "\n\tquit: close matlab and quit");
	}

}
