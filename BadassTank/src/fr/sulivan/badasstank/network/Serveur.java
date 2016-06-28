package fr.sulivan.badasstank.network;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Serveur extends ServerSocket{

	private HashMap<String, Socket> clients;
	public boolean running = false;
	
	public Serveur() throws IOException {
		super();
		
		clients = new HashMap<String, Socket>();
	}
	
	
	public void run() throws NetworkException{
		if(running){
			throw new NetworkException("Server already running");
		}
		running = true;
		while(running){
			//todo accept client
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
	
	
}