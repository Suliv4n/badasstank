package fr.sulivan.badasstank.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerClient {
	
	private Socket socket;
	
	private String key;
	
	public final PrintWriter out;
	public final BufferedReader in;
	
	public ServerClient(Socket socket, String key) throws IOException{
		this.socket = socket;
		OutputStream outstream = socket.getOutputStream();
		out = new PrintWriter(outstream);
    	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    	this.key = key;
	}
	
	public Socket getSocket(){
		return socket;
	}

	public String getKey() {
		return key;
	}
		
}
