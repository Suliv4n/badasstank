package fr.sulivan.badasstank.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;


public class Server extends NetworkPoint{

	private HashMap<String, Socket> clients;
	private boolean running = false;
	private ServerSocket server;
	
	public Server(InetAddress host, int port) throws IOException {
		server = new ServerSocket(port, 64, host);
		clients = new HashMap<String, Socket>();
	}
	
	public void start() throws NetworkException{
		new Thread(() -> {
			try{
				run();
			}
			catch(NetworkException e){
				running = false;
				System.err.println(e.getMessage());
			}
		}).start();
	}
	
	private void run() throws NetworkException{
		if(running){
			throw new NetworkException("Server already running");
		}
		running = true;
		while(running){
			try {
				Socket client = server.accept();
				String key = UUID.randomUUID().toString();
				clients.put(key, client);
				
				System.out.println("New Client");
				
				new Thread(()->{
					
					while(running && client.isConnected()){
					    try {
					    	BufferedReader in = new BufferedReader(
					    		new InputStreamReader(client.getInputStream())
					    	);
					    	System.out.println("wait for line");
					    	String message = in.readLine();
					    	System.out.println(message);
					    	Event event = Event.parse(message, client);
					    	EventCallback callback = events.get(event.getName());
					    	if(callback != null){
					    		callback.call(event);
					    	}
					    	
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					System.out.println("fin");
				}).start();
				
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	public boolean send(String message, Socket socket){
		
		OutputStream outstream;
		try {
			outstream = socket .getOutputStream();
			PrintWriter out = new PrintWriter(outstream);
			out.println(message);
			System.out.println("Send to client : " + message);
			return true;
		} catch (IOException e) {
			return false;
		} 
		
		
		
	}
	
	public void sendToAllExceptOne(String message, String except){
		for(String key : clients.keySet()){
			if(!key.equals(except)){
				send(message, clients.get(key));
			}
		}
	}
	
	public void stop(){
		running = false;
	}
	
	
}