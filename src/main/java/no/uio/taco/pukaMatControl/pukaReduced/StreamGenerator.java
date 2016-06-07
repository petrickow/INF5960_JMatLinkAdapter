package no.uio.taco.pukaMatControl.pukaReduced;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StreamGenerator implements Runnable {

	int port;
	
	public StreamGenerator(int port) {
		System.out.println("setting up generator on port " + port);
		this.port = port;
	}
	
	private ServerSocket setupServerSocket() throws IOException {
		return new ServerSocket(port, 0, InetAddress.getByName("localhost"))
;
	}
	
	
	private synchronized void streamLoop(PrintWriter pout) {
		boolean keepUp = true;
		
		
		while (keepUp) {
			System.out.println("sending hello");
			pout.println("hello");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		try {
			ServerSocketChannel channel = ServerSocketChannel.open();
			channel.bind(new InetSocketAddress("localhost", 9043));
			channel.configureBlocking(false);
			Selector selector = Selector.open();
			
			SelectionKey socketServerSelectionKey = channel.register(selector,
	                SelectionKey.OP_ACCEPT);
	        Map<String, String> properties = new HashMap<String, String>();
	        properties.put("channelType", "serverChannel");
	        socketServerSelectionKey.attach(properties);
			boolean keepGoing = true;
	        System.out.println("SERVER: ok");
			while (keepGoing) {
				if (selector.select() == 0) {
					continue;
				}
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
	            Iterator<SelectionKey> iterator = selectedKeys.iterator();
	            while (iterator.hasNext()) {
	                SelectionKey key = iterator.next();
	                // the selection key could either by the socketserver informing
	                // that a new connection has been made, or
	                // a socket client that is ready for read/write
	                // we use the properties object attached to the channel to find
	                // out the type of channel.
					if (((Map<String, String>) key.attachment()).get("channelType").equals(
	                        "serverChannel")) {
	                    // a new connection has been obtained. This channel is
	                    // therefore a socket server.
	                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
	                            .channel();
	                    // accept the new connection on the server socket. Since the
	                    // server socket channel is marked as non blocking
	                    // this channel will return null if no client is connected.
	                    SocketChannel clientSocketChannel = serverSocketChannel
	                            .accept();
	 
	                    if (clientSocketChannel != null) {
	                        // set the client connection to be non blocking
	                        clientSocketChannel.configureBlocking(false);
	                        SelectionKey clientKey = clientSocketChannel.register(
	                                selector, SelectionKey.OP_READ,
	                                SelectionKey.OP_WRITE);
	                        Map<String, String> clientproperties = new HashMap<String, String>();
	                        clientproperties.put("channelType", "clientChannel");
	                        clientKey.attach(clientproperties);
	 
	                        // write something to the new created client
	                        CharBuffer buffer = CharBuffer.wrap("Hello client");
	                        while (buffer.hasRemaining()) {
	                            clientSocketChannel.write(Charset.defaultCharset()
	                                    .encode(buffer));
	                        }
	                        buffer.clear();
	                    }
	 
	                } else {
	                    // data is available for read
	                    // buffer for reading
	                    ByteBuffer buffer = ByteBuffer.allocate(20);
	                    SocketChannel clientChannel = (SocketChannel) key.channel();
	                    int bytesRead = 0;
	                    if (key.isReadable()) {
	                        // the channel is non blocking so keep it open till the
	                        // count is >=0
	                        if ((bytesRead = clientChannel.read(buffer)) > 0) {
	                            buffer.flip();
	                            System.out.println(Charset.defaultCharset().decode(
	                                    buffer));
	                            buffer.clear();
	                        }
	                        if (bytesRead < 0) {
	                            // the key is automatically invalidated once the
	                            // channel is closed
	                            clientChannel.close();
	                        }
	                    }
	 
	                }
	 
	                // once a key is handled, it needs to be removed
	                iterator.remove();				
	            }
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
