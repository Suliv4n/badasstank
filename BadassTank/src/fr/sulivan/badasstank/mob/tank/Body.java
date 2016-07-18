package fr.sulivan.badasstank.mob.tank;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class Body extends TankPiece implements Cloneable{
	private Image image;
	private Color color;
	
	public Body(String id, String name, Image image){
		super(id, name);
		this.image = image;
	}

	public void setRotation(float angle) {
		image.setRotation(angle);
	}

	public int getWidth() {
		return image.getWidth();
	}
	public int getHeight() {
		return image.getHeight();
	}

	public void setCenterOfRotation(int x, int y) {
		image.setCenterOfRotation(x, y);
	}
	
	public void drawCentered(int x, int y){
		image.drawCentered(x, y);
	}
	
	public void setColor(float r, float g, float b){
		color = new Color(r,g,b);
		
		image.setColor(0, r, g, b);
		image.setColor(1, r, g, b);
		image.setColor(2, r, g, b);
		image.setColor(3, r, g, b);
	}
	
	public Color getColor(){
		return color;
	}
	
	@Override
	public Object clone(){
		Body clone = null;
		try {
			clone = (Body) super.clone();
			clone.image = this.image.copy();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return clone;
	}
}
