package fr.sulivan.badasstank.mob.tank;

import org.newdawn.slick.Image;

public class Body {
	private Image image;
	
	public Body(Image image){
		this.image = image;
	}

	public void setRotation(float angle) {
		image.rotate(angle);
	}

	public int getWidth() {
		return image.getWidth();
	}
	public int getHeight() {
		return image.getHeight();
	}

	public void setCenterOfRotation(int x, int y) {
		image.setCenterOfRotation(getWidth() / 2, getHeight()/2);
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
