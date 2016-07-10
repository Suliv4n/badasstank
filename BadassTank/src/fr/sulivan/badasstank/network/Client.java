package fr.sulivan.badasstank.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends NetworkPoint{
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public Client(String host, int port) throws UnknownHostException, IOException{
		socket = new Socket(host, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
	}
	
	public boolean send(String message){
		System.out.println("Send to server : " + message );
		out.println(message);
		out.flush();
		return true;
	}
	
	public void listen(){
		new Thread(() -> {
			while(socket.isConnected()){
		    	try {
			    	String message = in.readLine();
			    	System.out.println("Received from server : " + message);
			    	Event event = Event.parse(message, socket);
			    	EventCallback callback = events.get(event.getName());
			    	
			    	if(callback != null){
			    		callback.call(event);
			    	}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
