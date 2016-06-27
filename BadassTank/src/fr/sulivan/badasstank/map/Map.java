package fr.sulivan.badasstank.map;

import java.util.ArrayList;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.tiled.TiledMap;

import fr.sulivan.badasstank.hitbox.Hitbox;
import fr.sulivan.badasstank.util.PolygonFactory;

public class Map {
	
	private TiledMap tiledMap;
	private Hitbox hitbox;
	
	private int displayedX = 0;
	private int displayedY = 0;
	
	public Map(TiledMap tiledMap){
		this.tiledMap = tiledMap;
		hitbox = new Hitbox();
		
		for(int x = 0; x < tiledMap.getWidth(); x++){
			for(int y = 0; y < tiledMap.getHeight(); y++){
				for(int l = 0; l < tiledMap.getLayerCount(); l++){
					int tileID = tiledMap.getTileId((int) x,(int) y, l);
					boolean collision = tiledMap.getTileProperty(tileID, "collision", "false").equalsIgnoreCase("true");
					
					if(collision){
						hitbox.addShape(PolygonFactory.createRectangle(x*tiledMap.getTileWidth(), y*tiledMap.getTileHeight(), tiledMap.getWidth(), tiledMap.getHeight()));
					}
					
					break;
				}
			}
		}
	}
	
	public TiledMap getTiledMap(){
		return tiledMap;
	}
	
	public Hitbox getHitbox(){
		return hitbox;
	}
	
	public void drawLayer(int layer){
		tiledMap.render(displayedX, displayedY, layer);
	}
	
	public void setDisplayedCoordinate(int x, int y){
		hitbox.moveX(x - displayedX);
		hitbox.moveY(y - displayedY);
		
		displayedX = x;
		displayedY = y;
	}
	
	
}
