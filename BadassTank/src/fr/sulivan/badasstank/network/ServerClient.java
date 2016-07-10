package fr.sulivan.badasstank.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerClient {
	
	private Socket socket;
	public final PrintWriter out;
	public final BufferedReader in;
	
	public ServerClient(Socket socket) throws IOException{
		this.socket = socket;
		OutputStream outstream = socket.getOutputStream();
		out = new PrintWriter(outstream);
    	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public Socket getSocket(){
		return socket;
	}
		
}
