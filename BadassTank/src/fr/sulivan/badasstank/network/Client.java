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
	
	public Client(String host, int port) throws UnknownHostException, IOException{
		socket = new Socket(host, port);
	}
	
	public boolean send(String message){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			System.out.println("Send to server : " + message );
			pw.println(message);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public void listen(){
		new Thread(() -> {
			while(socket.isConnected()){
		    	try {
					BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream())
					);
					
			    	String message = in.readLine();
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
