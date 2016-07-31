/**
 * This class takes an input stream which deliver respiration data
 * Used with the StreamCreater
 *   
 */

package no.uio.taco.pukaMatControl.pukaReduced;

import org.apache.log4j.Logger;

import matlabcontrol.MatlabInvocationException;

import org.apache.log4j.BasicConfigurator;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class StreamGobbler implements Runnable {

	String bufferHistory = "";
	
	private boolean exitFlag = false;
	
	private Logger log; 
	private List<String> readBuffer;
	private List<Double> timeStamps;
	
	private String fileName;
	
	private int port = 4444;
	private String addr = "localhost";

	private ByteBuffer receiveBuffer = ByteBuffer.allocate(20);
	RespirationAnalyser respirationAnalyser; 
	Pattern regex;
	
	/**
	 * Constructor creates logger, a shared buffer which will be passed to 
	 * the analysis class, and launches the matlab instance.
	 * @param fileName - the desired filename specified by the user via the shell
	 */
	public StreamGobbler(String fileName) {
		log = Logger.getLogger(this.getClass());
		BasicConfigurator.configure();
		log.debug("init gobbler");
		
		regex = Pattern.compile("^[a-zA-Z]+([0-9]+).*"); // TODO create a pattern that catches <x.xxx>
		
		this.readBuffer = Collections.synchronizedList(new ArrayList<String>());
		this.fileName = fileName;
		this.respirationAnalyser = new RespirationAnalyser(readBuffer);
		respirationAnalyser.toggleDebug();
		respirationAnalyser.launchMatlabInstance();
		
	}
	/**
	 * This requires the DataFeeder application to be running on the same host
	 * as it is running. We will connect using Network Channel from the Java NIO
	 * package
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		//haltFor(1); // just to be able to read

		SocketChannel channel; // connection to DataFeeder
		
		try {
			channel = intiateConnection();
			
			if (channel.isOpen()) {
				String message = readFromChannel(channel);
				if (sendAck(channel, message)) {
					receiveLoop(channel);
				}
			}
		} catch (IOException e) {
			log.error("Communication errer: '" + e.getMessage() + "'");
			resetShell();
		}
	}

	/**
	 * The main workhorse of the class TODO: remove throw and handle abrupt disconnect
	 * @param channel
	 * @throws IOException
	 */
	private void receiveLoop(SocketChannel channel) throws IOException {
		//long startTime = System.currentTimeMillis();
		//long endTime = 0; 
		log.info("Starting receive loop and analyser thread");
		
		Thread th = new Thread(respirationAnalyser);
		th.start();
		
		List<String> temp = new ArrayList<String>(readBuffer.size());
		
		for(;;) {
			
			String line = readFromChannel(channel);

			if (line.endsWith(",400") || exitFlag) {
				channel.close();
				log.error(line);
				resetShell();
				break;
			}
			
			/* Need to double check that we only have one entry pr line */
			Collection<String> splitLine = checkResult(line);
			readBuffer.addAll(splitLine);
			//timestamps.add(TODO: implicit time stamps?)

			if (readBuffer.size() == 10000) { //TODO define standard window size!
				log.info("Launch respiration analysis");
				
				synchronized (respirationAnalyser) {
					/*TODO: wait for the analysis to complete? */
					temp.addAll(readBuffer);
					respirationAnalyser.setBuffer(temp);
					respirationAnalyser.notifyAll();
					
				}
				// create new list for new arrivals
				temp = new ArrayList<String>(readBuffer.size());
				readBuffer.clear(); 	// = Collections.synchronizedList(new ArrayList<String>(respirationAnalyser.getClipLength()));
				
			}
		}
		log.debug("Receive loop done and done!");
	}

	
	// DONE: Change to read each value... parse the incoming string and pass back either on or multiple
	// entries, but make sure to keep any information not used

	/**
	 * A brute force parser. Checking that all arriving data points
	 * are contained within the protocol defined containers
	 * @param line
	 * @return
	 */
	private Collection<String> checkResult(String line) {
		List<String> res = new ArrayList<String>();
		if (bufferHistory.length() > 0) {
			line = bufferHistory + line;
			bufferHistory = "";
		}
		char[] charA = line.toCharArray();
		int start = 0;
		// very brutish, swap for regex
		for (int i = 0; i < charA.length; i++) { 
			char c = charA[i];
			if (c == '<') {
				start = i + 1;
			}
			else if (c == '>') {
				res.add(line.substring(start, i)); 
				start = 0;
			}
		}
		if (start != 0) { // we have found a '<' but no corresponding '>'
			bufferHistory = line.substring(start);
		}
		
		return res;
	}

	/**
	 * Create a socket channel 
	 * @return
	 * @throws IOException
	 */
	private SocketChannel intiateConnection() throws IOException {
		log.debug("Connection to " + addr + " on port " + port);
		
		SocketChannel channel = SocketChannel.open();	

		// we open this channel in non blocking mode
		channel.configureBlocking(false);
		channel.connect(new InetSocketAddress(addr, port));

		while (!channel.finishConnect()) {
			//log.info("still connecting"); // blocking wait
			haltFor(0.5);
		}
		return channel;
	}
	
	/**
	 * Read from the passed channel and return as string
	 * @param channel
	 * @return the content sent via channel
	 * @throws IOException
	 */
	private String readFromChannel(SocketChannel channel) throws IOException {
		String message = "";
		while (message.length() == 0) {
			while (channel.read(receiveBuffer) > 0) {
				// flip the buffer to start reading
				
				receiveBuffer.flip();
				message += Charset.defaultCharset().decode(receiveBuffer);
				
			}
			receiveBuffer.clear();
		}
//		if (message.length() > 9 || message.length() < 2)
//			System.out.println(message);
		return message;
	}

	/**
	 * Respond to initiation with ack and filename
	 * 
	 * @param message response we got from the server
	 * @return false if server has not respondend with 200, ergo nothing to ack
	 */
	private boolean sendAck(SocketChannel channel, String message) throws IOException {
		if (message.endsWith("200")) {
			// String[] split = message.split(","); // split and display connection status?
			
			CharBuffer buffer = CharBuffer.wrap(fileName + ",REQ");
			
			while (buffer.hasRemaining()) {
				channel.write(Charset.defaultCharset().encode(buffer));
			}
			return true;
		} else {
			// different status code from server, abort
			log.error(message);
			return false;
		}
	}

	/**
	 * Print new prompt to indicate the shell is ready for new input
	 */
	private void resetShell() {
		// TODO: give shell control back to user after finished
		System.out.print("$> ");
	}
	
	/**
	 * Wait method for seconds
	 * @param sec
	 */
	private synchronized void haltFor(double sec) {
		try {
			long ms = (long) sec * 1000;
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
	public synchronized void disconnect() {
		exitFlag = true;
	}
}
