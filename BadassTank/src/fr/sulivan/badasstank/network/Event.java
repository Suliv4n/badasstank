package fr.sulivan.badasstank.network;

import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Event {
	
	private String name;
	private HashMap<String, String> parameters;
	private Socket source;
	private String key;
	
	public Event(String name, HashMap<String, String> parameters, Socket source){
		this.parameters = parameters;
		this.name= name;
		this.source = source;
	}
	
	public static Event parse(String message, Socket source){
		message = message.trim();
		String[] parts = message.split("\\s");
		String name = parts[0];
		String queryString = message.substring(name.length()).trim();
		
		Pattern pattern = Pattern.compile("([^\\s]+)=(?:(?:\"((?:[^\"]|(?<=\\\\)\")*)(?<!\\\\)\")|([^\\s]*))");
		Matcher matcher = pattern.matcher(queryString);
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		
		while(matcher.find()){
			if(matcher.group(2) != null){
				parameters.put(matcher.group(1), matcher.group(2));
			}
			else{
				parameters.put(matcher.group(1), matcher.group(3).replaceAll("\\\"", "\""));
			}
		}
		
		return new Event(name, parameters, source);
	}

	public String getName() {
		return name;
	}

	public Socket getSource() {
		return source;
	}

	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	public int getIntParameter(String name) {
		return Integer.parseInt(parameters.get(name));
	}

	public HashMap<String, String> getParameters() {
		return parameters;
	}
	
	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
}
