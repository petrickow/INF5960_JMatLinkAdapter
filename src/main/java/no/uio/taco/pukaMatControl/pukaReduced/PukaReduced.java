package no.uio.taco.pukaMatControl.pukaReduced;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import matlabcontrol.MatlabInvocationException;

import java.util.List;

/**
 * Replication of the respiration analysis found in puka. This program allow
 * developers to quickly run the respiration analysis done with the MATLAB
 * scripts. The data is read from a one column text file.
 * 
 * @author Cato Danielsen
 * @version 0.1.1
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

	static List<StreamGobbler> runningGobblers;
	static String fName = "signal.txt";
	static Pattern whiteSpacePattern;

	RespirationAnalyser respAnalyser;
	static int windowSize = 10000; // standard is 10 second 1000hz

	public static void main(String[] args) {
		whiteSpacePattern = Pattern.compile("\\s");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("===pukaReduced Shell--->\tMain shutting down");
				for (StreamGobbler sg : runningGobblers) {
					sg.disconnect();
				}
			}
		});

		runningGobblers = new ArrayList<StreamGobbler>();
		Scanner keyboard = new Scanner(System.in);

		/*
		 * Instantiate the analyzer object, used both in local and stream
		 * analysis. When using a stream, each analysis is executed in a
		 * separate thread, while for local we use the blocking method, as no
		 * user input is received during
		 */
		RespirationAnalyser respAnalyser = new RespirationAnalyser();
		String input = "";
		boolean analysisRunning = false;
		printHelp();
		
		while (!input.equalsIgnoreCase("quit")) {

			System.out.print("$> ");
			input = keyboard.nextLine().trim();
			if (!analysisRunning) {
				switch (input.toLowerCase()) { // TOOD: refactor out into separate method
				case "debug":
					System.out.println("===pukaReduced Shell--->\tsetting Debug to: " + respAnalyser.toggleDebug());
					break;

				case "run":
					respAnalyser.clean(); // reset analyser
					System.out.println("===pukaReduced Shell--->\tRequesting file '" + fName + "'");
					try {
						respAnalyser.launchLocalFile(fName);
					} catch (MatlabInvocationException e) {
						System.out.println("Lost connection to MATLAB");
					}
					break;

				case "stream":
					analysisRunning = true; 
					//respAnalyser.clean();
					StreamGobbler sg = new StreamGobbler(fName);
					sg.setWindowSize(windowSize);
					
					Thread thGobbler = new Thread(sg);
					runningGobblers.add(sg);
					thGobbler.start();
					
					break;

				case "window":
					askForWindowSize();
					String userDefinedSizeString = keyboard.nextLine().trim();
					windowSize = checkWindowSize(userDefinedSizeString);
					break;
					
				case "fname":
					askForFileName();
					String f = keyboard.nextLine().trim();
					if (checkFileName(f)) {
						fName = f;
						System.out.println("Request file set to: '" + f + "'");

					} else {
						System.out.println(
								"'" + f + "' is not a valid filename\n\tNO CHAGE: File name is still: '" + fName + "'");
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
					break;
				default:
					printHelp();
				}
			} else {
				switch (input.toLowerCase()) {
				case "end":
					closeStream();
					analysisRunning = false;
					break;
				case "stop":
					closeStream();
					analysisRunning = false;
					break;
				case "rate":
//					TODO: adjust send-rate
					break;
				case "quit":
					input = ""; // hack: to avoid stopping the whole input loop
					closeStream();
					analysisRunning = false;
					
//					cleanUp?
					break;
				default:
					System.out.println("what?");
				}
			}
		}
		keyboard.close();
		respAnalyser.kill();
	}

	

	/**
	 * Set the exit flag in the stream gobbler to true, asking
	 * the thread to close the connection and reset
	 */
	private static void closeStream() {
		Iterator<StreamGobbler> it = runningGobblers.iterator();
		while (it.hasNext()) {
			StreamGobbler sg = it.next();
			sg.disconnect();
		}
		runningGobblers = new ArrayList<StreamGobbler>();
		printHelp();
	}

	/**
	 * Prompt user for filename
	 */
	private static void askForFileName() {
		System.out.print(
				"(white space in file name not allowed)\nType desired filename (current name: '" + fName + "')\t--> ");
	}
	
	private static void askForWindowSize() {
		System.out.print(
				"(check samplerate of signal to find duration)\nType desired window size (current size: '" + windowSize + "')\t--> ");
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
	
	/**
	 * Parses inputstring and returns the valid integer from a string or default value if
	 * it is not valid
	 * @param userDefinedSizeString
	 * @return
	 */
	private static int checkWindowSize(String userDefinedSizeString) {
		try {
			return Integer.parseInt(userDefinedSizeString.trim());
		} catch (NumberFormatException e) {
			return windowSize;
		}
	}

	private static void printHelp() {
		System.out.println("===pukaReduced Shell menu--->\n\t" + "run: execute puka test (using '" + fName + "')\n\t"
				+ "stream: execute puka test (using data from Data Feeder-server)\n\n\t"

				+ "window: set the desired window size (number of samples)\n\t"
				+ "fname: set the desired filename to request from Data Feeder\n\t"
				+ "debug: toggles debug print from matlabcontrol\n\n\t"

				+ "pwd: print working directory. This is where local signals must be placed\n\t" + "help: this\n\t"
				+ "quit: close matlab and quit");
	}
}
