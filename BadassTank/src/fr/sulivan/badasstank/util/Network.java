package fr.sulivan.badasstank.util;

import java.net.InetSocketAddress;

public class Network {
	public static InetSocketAddress getSocketAddress(String host, int defaultPort){
		String hostname = host;
		int port = defaultPort;
		
		if(hostname.matches("^[^:]+:[0-9]+$")){
			String[] parts = hostname.split(":");
			hostname = parts[0];
			port = Integer.parseInt(parts[1]);
		}
		return new InetSocketAddress(hostname, port);
	}
}
