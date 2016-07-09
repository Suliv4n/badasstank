package fr.sulivan.badasstank.network;

import java.util.HashMap;

public abstract class NetworkPoint {
	protected HashMap<String, EventCallback> events;
	
	
	public NetworkPoint(){
		events = new HashMap<String, EventCallback>();
	}
	
	public boolean unbind(String event){
		return events.remove(event) != null;
	}
	
	public void on(String event, EventCallback callback) {
		events.put(event, callback);
	}
	

}
