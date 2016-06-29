package fr.sulivan.badasstank.network;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

public class Server extends ServerSocket{

	private HashMap<String, Socket> clients;
	public boolean running = false;
	
	public Server(InetAddress host, int port) throws IOException {
		super(port, 64, host);
		clients = new HashMap<String, Socket>();
	}
	
	public void start() throws NetworkException{
		new Thread(() -> {
			try{
				run();
			}
			catch(NetworkException e){
				System.err.println(e.getMessage());
			}
		});
	}
	
	private void run() throws NetworkException{
		if(running){
			throw new NetworkException("Server already running");
		}
		running = true;
		while(running){
			try {
				Socket newClient = accept();
				String key = UUID.randomUUID().toString();
				
				clients.put(key, newClient);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	public void send(String message, String to) throws IOException{
		Socket socket = clients.get(to);
		
		OutputStream outstream = socket .getOutputStream(); 
		PrintWriter out = new PrintWriter(outstream);
		
		out.println(message);
	}
	
	public void sendToAllExceptOne(String message, String except) throws IOException{
		for(String key : clients.keySet()){
			if(!key.equals(except)){
				send(message, key);
			}
		}
	}
	
	public void stop(){
		running = false;
	}
	
	
}