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
	
	int port = 9043;
	
	 private synchronized void haltFor(long sec) {
		 try {
			Thread.sleep(sec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
		
	@Override
	public void run() {
		
		System.out.println("We now wait for connection to 'sensor'");
		haltFor(2);
		
		
		
		/*
		Socket s = setupSocket();
		if (s == null) {
			System.out.println("FAILED: setup socket");
			System.exit(-1);
		}
		
		BufferedReader in;
		try {
			in = new BufferedReader(
			        new InputStreamReader(s.getInputStream()));
			
			
			String input = in.readLine();
			while (!input.equals("quit")) {
				System.out.println(input);
				input = in.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */
		

		
	}
	


}







/*
public class Streamer{ 
	 
}

*/