package no.uio.taco.pukaMatControl.pukaReduced;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

/**
 * Replication of the respiration analysis found in puka. This program allow
 * developers to quickly run the respiration analysis done with the MATLAB
 * scripts. The data is read from a one column text file.
 * 
 * @author Cato Danielsen
 * @version 0.0.3
 *
 *          TODO: implement timing
 *          (http://stackoverflow.com/questions/180158/how-do-i-time-a-methods-
 *          execution-in-java): long startTime = System.nanoTime();
 *          methodToTime(); long endTime = System.nanoTime();
 * 
 *          long duration = (endTime - startTime); //divide by 1000000 to get
 *          milliseconds.
 */
public class PukaReduced {

	static List<Thread> runningThreads;
	static String fName = "signal.txt";
	static Pattern whiteSpacePattern;

	RespirationAnalyser respAnalyser;

	public static void main(String[] args) {
		whiteSpacePattern = Pattern.compile("\\s");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("===pukaReduced Shell--->\tMain shutting down");
				for (Thread t : runningThreads) {
					// TODO: cleanup
				}
			}
		});

		runningThreads = new ArrayList<Thread>();
		Scanner keyboard = new Scanner(System.in);

		RespirationAnalyser respAnalyser = new RespirationAnalyser();
		// List<String> sharedBuffer = Collections.synchronizedList(new
		// LinkedList<String>());
		String input = "";

		printHelp();
		while (!input.equalsIgnoreCase("quit")) {

			System.out.print("$> ");
			input = keyboard.nextLine().trim();
			switch (input) {
			case "debug":
				System.out.println("===pukaReduced Shell--->\tsetting Debug to: " + respAnalyser.toggleDebug());
				break;
			case "run":
				respAnalyser.clean(); // reset
				System.out.println("===pukaReduced Shell--->\tRequesting file '" + fName+"'");
				respAnalyser.launchLocalFile(fName);
				break;
			case "stream":

				Thread gobbler = new Thread(new StreamGobbler(respAnalyser, fName));
				gobbler.start();
				break;
			case "fname":
				askForFileName();
				String f = keyboard.nextLine().trim();
				if (checkFileName(f)) {
					fName = f;
					System.out.println("Request file set to: '" + f + "'");

				} else {
					System.out.println("'" + f + "' is not a valid filename");
				}
				break;
			case "help":
				printHelp();
				break;
			case "quit":
				System.out.println("===pukaReduced Shell--->\tBye");
				break;
			case "pwd":
				System.out.println("===Print working directory--->\t" + System.getProperty("user.dir"));
			default:
				printHelp();
			}
		}
		keyboard.close();
		respAnalyser.kill();
	}

	/**
	 * Prompt user for filename
	 */
	private static void askForFileName() {
		System.out.print(
				"(white space in file name not allowed)\nType desired filename (current name: '" + fName + "')\t--> ");
	}

	/**
	 * Verify that the filename does not contain white spaces and that it is not
	 * empty.
	 * 
	 * @param s
	 *            - the filename
	 * @return boolean whether the filename meets our criteria
	 */
	private static boolean checkFileName(String s) {
		if (s.isEmpty()) {
			return false;
		}

		Matcher matcher = whiteSpacePattern.matcher(s);

		if (matcher.find()) {
			return false;
		}
		return true;

	}

	private static void printHelp() {
		System.out.println("===pukaReduced Shell menu--->\n\t" + "run: execute puka test (using signal.txt)\n\t"
				+ "stream: execute puka test (using data from Data Feeder-server)\n\n\t"

				+ "fname: set the desired filename to request from Data Feeder\n\t"
				+ "debug: toggles debug print from matlabcontrol\n\n\t"

				+ "pwd: print working directory. This is where local signals must be placed\n\t" + "help: this\n\t"
				+ "quit: close matlab and quit");
	}
}
