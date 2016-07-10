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
				
				/*int i=0;
				while(i<1000){
					send("TEST SEND "+ i +" TO CLIENT", socket);
					i++;
				}*/
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
	
	public void sendToAllExceptOne(String message, String except){
		/*for(String key : clients.keySet()){
			if(!key.equals(except)){
				send(message, clients.get(key).getSocket());
			}
		}*/
	}
	
	public void stop(){
		running = false;
	}
	
	
}