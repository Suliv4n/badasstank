package fr.sulivan.badasstank.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Server extends NetworkPoint{

	private HashMap<Socket, ServerClient> clients;
	private boolean running = false;
	private ServerSocket server;
	
	public Server(InetAddress host, int port) throws IOException {
		server = new ServerSocket(port, 64, host);
		clients = new HashMap<Socket, ServerClient>();
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
				Socket socket = server.accept();
				
				ServerClient client = new ServerClient(socket);
				clients.put(socket, client);
				
				new Thread(()->{
					
					while(running && client.getSocket().isConnected()){
					    try {
					    	System.out.println("wait for line");
					    	String message = client.in.readLine();
					    	System.out.println(message);
					    	Event event = Event.parse(message, client.getSocket());
					    	EventCallback callback = events.get(event.getName());
					    	if(callback != null){
					    		callback.call(event);
					    	}
					    	
						} catch (Exception e) {
							//e.printStackTrace();
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
		ServerClient client = clients.get(socket);
		if(client == null){
			return false;
		}

		client.out.println(message);
		client.out.flush();
		System.out.println("Send to client : " + message);
		return true;
	}
	
	public void send(String message, HashMap<String, String> parameters, Socket socket) {
		send(message + " " + createQueryString(parameters), socket);
	}
	
	private String createQueryString(HashMap<String, String> parameters) {
		if(parameters == null){
			return "";
		}
		
		String query = "";
		for(String key : parameters.keySet()){
			String value = parameters.get(key);
			if(value.contains(" ") || value.contains("\"")){
				value = value.replaceAll("\"", "\\\"");
				value = "\""+value+"\"";
			}
			query += key + "=" + value + " ";
		}
		
		return query.substring(0, query.length()-1);
	}

	public boolean broadcast(String message, Socket except){
		boolean res = true;
		for(Socket socket : clients.keySet()){
			if(!socket.equals(except)){
				res &= send(message, socket);
			}
		}
		
		return res;
	}
	
	public void stop(){
		running = false;
	}

	public boolean broadcast(String message, HashMap<String, String> parameters, Socket except) {
		String event = message + " " + createQueryString(parameters);
		return broadcast(event, except);
	}


	
	
}