/**
 * This class takes an input stream which deliver respiration data
 * Used with the StreamCreater
 *   
 */

package no.uio.taco.pukaMatControl.pukaReduced;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class StreamGobbler implements Runnable {

	private synchronized void haltFor(long sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		haltFor(2);

		try {
			int port = 4444;
			SocketChannel channel = SocketChannel.open();

			// we open this channel in non blocking mode
			channel.configureBlocking(false);
			channel.connect(new InetSocketAddress("localhost", port));

			while (!channel.finishConnect()) {
				System.out.println("still connecting");
			}
			
			while (true) {
				// see if any message has been received
				ByteBuffer bufferA = ByteBuffer.allocate(20);
				int count = 0;
				String message = "";
				while ((count = channel.read(bufferA)) > 0) {
					// flip the buffer to start reading
					bufferA.flip();
					message += Charset.defaultCharset().decode(bufferA);

				}

				if (message.length() > 0) {
					System.out.println(message);
					// write some data into the channel
					CharBuffer buffer = CharBuffer.wrap("signal.txt, 200");
					while (buffer.hasRemaining()) {
						channel.write(Charset.defaultCharset().encode(buffer));
					}
					message = "";
					
					receiveLoop(channel);
				}

			}
		} catch (Exception e) {
			
		}

		/*
		 * Socket s = setupSocket(); if (s == null) { System.out.println(
		 * "FAILED: setup socket"); System.exit(-1); }
		 * 
		 * BufferedReader in; try { in = new BufferedReader( new
		 * InputStreamReader(s.getInputStream()));
		 * 
		 * 
		 * String input = in.readLine(); while (!input.equals("quit")) {
		 * System.out.println(input); input = in.readLine(); } } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

	}

	private void receiveLoop(SocketChannel channel) throws IOException {
		ByteBuffer bufferA = ByteBuffer.allocate(100);
		int count = 0;
		
		
		while(true) {
			
			String message = "";

			
			while ((count = channel.read(bufferA)) > 0) {
				// flip the buffer to start reading
				bufferA.flip();
				message += Charset.defaultCharset().decode(bufferA);
			}

			if (message.length() > 0) {
				System.out.println("count: " + count++ + "msg: " +  message);
			}
			else {
				// Nothing on the channel, why?
				//System.out.println("Thats it?");
			}
			
			bufferA.clear();
		}
		
	}

}

/*
 * public class Streamer{
 * 
 * }
 * 
 */