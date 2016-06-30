package fr.sulivan.badasstank.loader;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.mob.tank.Body;

public class PiecesLoader {

	private static PiecesLoader instance;
	
	private ArrayList<XMLElement> getChildrenFromXML(String fileName, String children) throws SlickException{
		XMLParser parser = new XMLParser();
		XMLElement file = parser.parse(fileName);
		XMLElementList list = file.getChildrenByName(children);
		ArrayList<XMLElement> collection = new ArrayList<XMLElement>();
		list.addAllTo(collection);
		
		return collection;
	}
	
	private static ArrayList<XMLElement> getChildrenFromXMLElement(XMLElement element, String children) throws SlickException{
		
		XMLElementList list = element.getChildrenByName(children);
		ArrayList<XMLElement> collection = new ArrayList<XMLElement>();
		list.addAllTo(collection);
		
		return collection;
	}
	
	public HashMap<String, Body> loadBodies() throws SlickException{
		HashMap<String, Body> bodies = new HashMap<String, Body>();
		
		XMLElement XMLbodies = getChildrenFromXML(Configuration.PIECES_FILE, "bodies").get(0);
		
		for(XMLElement element : getChildrenFromXMLElement(XMLbodies, "body")){
			String id = element.getAttribute("id");
			System.out.println(id);
		}
		
		return bodies;
	}
	
	public static PiecesLoader loader(){
		if(instance == null){
			instance = new PiecesLoader();
		}
		
		return instance;
	}
}
