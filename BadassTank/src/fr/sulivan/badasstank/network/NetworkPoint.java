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
	

	protected String createQueryString(HashMap<String, String> parameters) {
		if(parameters == null){
			return "";
		}
		
		String query = "";
		for(String key : parameters.keySet()){
			String value = parameters.get(key);
			if(value == null){
				System.out.println("PUTAIN DE CONNASE DE VALEUR NULL => " + key);
			}
			if(value.contains(" ") || value.contains("\"")){
				value = value.replaceAll("\"", "\\\"");
				value = "\""+value+"\"";
			}
			query += key + "=" + value + " ";
		}
		
		return query.substring(0, query.length()-1);
	}
	
}
