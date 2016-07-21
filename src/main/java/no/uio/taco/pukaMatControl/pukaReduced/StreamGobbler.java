/**
 * This class takes an input stream which deliver respiration data
 * Used with the StreamCreater
 *   
 */

package no.uio.taco.pukaMatControl.pukaReduced;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.List;

public class StreamGobbler implements Runnable {

	private List<String> sharedBuffer;
	
	private String fileName = "signals.txt";
	private int port = 4444;

	private ByteBuffer receiveBuffer = ByteBuffer.allocate(100);


	
	public StreamGobbler(List<String> sharedBuffer) {
		this.sharedBuffer = sharedBuffer;
	}
	/*
	 * This requires the DataFeeder application to be running on the same host
	 * as it is running. We will connect using Network Channel from the Java NIO
	 * package
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		System.out.println("We now wait for connection to 'sensor'");
		haltFor(1); // just to be able to read

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
			//TODO: check error and handle appropriate (most likely connection exception)
			e.printStackTrace();
		}
	}

	private SocketChannel intiateConnection() throws IOException {

		SocketChannel channel = SocketChannel.open();	

		// we open this channel in non blocking mode
		channel.configureBlocking(false);
		channel.connect(new InetSocketAddress("localhost", port));

		while (!channel.finishConnect()) {
			System.out.println("still connecting");
			haltFor(0.5);
		}
		
		
		return channel;
	}


	private String readFromChannel(SocketChannel channel) throws IOException {
		String message = "";
		while (message.length() == 0) {
			
			int count = 0;
			while ((count = channel.read(receiveBuffer)) > 0) {
				// flip the buffer to start reading
				receiveBuffer.flip();
				message += Charset.defaultCharset().decode(receiveBuffer);
			}
			receiveBuffer.clear();
			//System.out.println("Got " + count + " bytes message");
		}
		
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
			//String[] split = message.split(","); // split and display connection status?
			CharBuffer buffer = CharBuffer.wrap(fileName + ",200");
			while (buffer.hasRemaining()) {
				channel.write(Charset.defaultCharset().encode(buffer));
			}
			return true;
		} else {
			// different status code from server, abort
			return false;
		}
		
	}

	/**
	 * The main workhorse of the class
	 * @param channel
	 * @throws IOException
	 */
	private void receiveLoop(SocketChannel channel) throws IOException {
		
		
		for(;;) {
			String line = readFromChannel(channel);
			if (line.endsWith(",400")) {
				System.out.println("Error, file not found.\t'" + line + "'");
				break;
			}
			
			//TODO: init analysis
		}
		
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
	
}
