package fr.sulivan.badasstank.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends NetworkPoint{
	private Socket socket;
	
	private Client(String host, int port) throws UnknownHostException, IOException{
		socket = new Socket(host, port);
	}
	
	
	
}
