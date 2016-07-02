package fr.sulivan.badasstank.mob.tank;

import org.newdawn.slick.Image;

public class Body extends TankPiece{
	private Image image;
	
	public Body(String id, Image image){
		super(id);
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
	
	protected void setColor(float r, float g, float b){
		image.setColor(0, r, g, b);
		image.setColor(1, r, g, b);
		image.setColor(2, r, g, b);
		image.setColor(3, r, g, b);
	}
}
