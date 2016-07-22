package no.uio.taco.pukaMatControl.pukaReduced;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;

/**
 * Replication of the respiration analysis found in puka.
 * This program allow developers to quickly run the respiration analysis done with the
 * MATLAB scripts. The data is read from a one column text file.
 * @author 		Cato Danielsen
 * @version 	0.0.3
 *
 * TODO: implement timing (http://stackoverflow.com/questions/180158/how-do-i-time-a-methods-execution-in-java):
			long startTime = System.nanoTime();
			methodToTime();
			long endTime = System.nanoTime();

			long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
 */
public class PukaReduced {
	
	static List<String> sharedBuffer;
	static List<Thread> runningThreads;
	
	public static void main(String[] args) {
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
	        @Override
            public void run() {
	        	System.out.println("Main shutting down");
        		//TODO: cleanup
            }
        });

		runningThreads = new ArrayList<Thread>();
		Scanner keyboard = new Scanner(System.in);
		
		RespirationAnalyser respAnalyser = new RespirationAnalyser();
		String input = "";
		
		printHelp();
		while (!input.equalsIgnoreCase("quit")) {

			System.out.print("$> ");
			input = keyboard.nextLine().trim();
			switch (input) {
				case "debug":
					System.out.println("setting Debug to: " + respAnalyser.toggleDebug());
					break;
				case "run":
					respAnalyser.clean(); //just in case we already have run previously
					String fname = input = keyboard.nextLine().trim();
					respAnalyser.launchLocalFile(fname);
					break;
				case "stream": connectStreamServer(); break;
				case "help": printHelp(); break;
				case "quit": System.out.println("bye"); break;
				case "pwd": System.out.println(System.getProperty("user.dir"));
				default: printHelp();
			}
		}
		keyboard.close();
		respAnalyser.kill();
	}

	private static void connectStreamServer() {
		
		sharedBuffer = Collections.synchronizedList(new LinkedList<String>());
		
		//System.out.println("Starting Gobbler thread");
		Thread gobbler = new Thread(new StreamGobbler(sharedBuffer));
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
