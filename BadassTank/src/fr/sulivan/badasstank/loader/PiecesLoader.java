package fr.sulivan.badasstank.loader;

import java.util.ArrayList;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.mob.tank.Body;
import fr.sulivan.badasstank.mob.tank.Canon;
import fr.sulivan.badasstank.mob.tank.Carterpillar;

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
	
	private Image getImage(XMLElement element, int width, int height) throws SlickException{

		int indexImage = element.getIntAttribute("index");
		Image image = new Image(element.getContent(), new Color(255,0,255)).getSubImage(indexImage * width, 0, width, height);
		
		return image;
		
	}
	
	public ArrayList<Body> loadBodies() throws SlickException{
		ArrayList<Body> bodies = new ArrayList<Body>();
		
		XMLElement XMLbodies = getChildrenFromXML(Configuration.PIECES_FILE, "bodies").get(0);
		
		for(XMLElement element : getChildrenFromXMLElement(XMLbodies, "body")){
			String id = element.getAttribute("id");
			XMLElement XMLImage = element.getChildrenByName("image").get(0);
			
			Image image = getImage(XMLImage, 16, 18);
			
			Body  body = new Body(id, image);
			
			bodies.add(body);
		}
		
		return bodies;
	}
	
	private Image getImage(XMLElement element) throws SlickException {
		Image image = new Image(element.getContent(), new Color(255,0,255));
		int x = element.getIntAttribute("x",0);
		int y = element.getIntAttribute("y",0);
		
		int width = element.getIntAttribute("width", image.getWidth());
		int height = element.getIntAttribute("height", image.getHeight());
		
		return image.getSubImage(x, y, width, height);
	}
	
	
	//TODO vitesse de l'animation ?
	public ArrayList<Carterpillar> loadCarterpillars() throws SlickException{
		ArrayList<Carterpillar> carterpillars = new ArrayList<Carterpillar>();
		
		XMLElement XMLCarterpillars = getChildrenFromXML(Configuration.PIECES_FILE, "carterpillars").get(0);
		
		
		for(XMLElement element : getChildrenFromXMLElement(XMLCarterpillars, "carterpillar")){
			String id = element.getAttribute("id");
			double speed = Double.parseDouble(element.getChildrenByName("speed").get(0).getContent());
			double rotation = Double.parseDouble(element.getChildrenByName("rotation").get(0).getContent());
			
			XMLElement XMLImage = element.getChildrenByName("image").get(0);
			
			SpriteSheet sprites = new SpriteSheet(getImage(XMLImage, 21, 23), 7 ,23);
			Animation animation = new Animation(sprites, 0, 0, 2, 0,true, 100, true);

			Carterpillar carterpillar = new Carterpillar(id, animation, speed, rotation);
			
			carterpillars.add(carterpillar);
		}
		
		return carterpillars;
	}
	
	//TODO particle
	public ArrayList<Canon>  loadCanons() throws SlickException{
		ArrayList<Canon> canons = new ArrayList<Canon>();
		XMLElement XMLCanons = getChildrenFromXML(Configuration.PIECES_FILE, "canons").get(0);
		
		for(XMLElement element : getChildrenFromXMLElement(XMLCanons, "canon")){
			String id = element.getAttribute("id");
			XMLElement XMLImage = element.getChildrenByName("image").get(0);
			
			Image image = getImage(XMLImage, 11, 17);
			int centerX = XMLImage.getIntAttribute("center-x", image.getWidth() / 2);
			int centerY = XMLImage.getIntAttribute("center-y", image.getHeight() / 2);
			
			XMLElement XMLBullet = element.getChildrenByName("bullet").get(0);
			XMLElementList XMLImageBullet = XMLBullet.getChildrenByName("image");
			
			Image bulletImage = null;
			if(XMLImageBullet.size() > 0){
				bulletImage = getImage(XMLImageBullet.get(0));
			}
			
			ParticleSystem particles = null;
			
			float bulletSpeed = Float.parseFloat(XMLBullet.getChildrenByName("speed").get(0).getContent());
			int range = Integer.parseInt(XMLBullet.getChildrenByName("range").get(0).getContent());
			int cooldown = Integer.parseInt(XMLBullet.getChildrenByName("cooldown").get(0).getContent());
			
			Canon canon = new Canon(id, image, centerX, centerY, bulletImage, particles, bulletSpeed, range, cooldown);
			
			canons.add(canon);
		}
		return canons;
	}
	


	public static PiecesLoader loader(){
		if(instance == null){
			instance = new PiecesLoader();
		}
		
		return instance;
	}
}
