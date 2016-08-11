package fr.sulivan.badasstank.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class ColoredButtonGUI extends ButtonGUI{
	public static final Color DEFAULT_DISABLE_COLOR = new Color(100,100,100);
	private Image image;
	private Color color;
	private Color disableColor = DEFAULT_DISABLE_COLOR;
	

	
	public ColoredButtonGUI(Image image, Color color){
		this.image = image;
		setColor(color);
	}
	
	public ColoredButtonGUI(String image, Color color) throws SlickException {	
		this.image = new Image(image);
		setColor(color);
	}

	public void setColor(Color color) {
		this.color = color;
		
		image.setColor(Image.BOTTOM_LEFT, color.r, color.g, color.b);
		image.setColor(Image.BOTTOM_RIGHT, color.r, color.g, color.b);
		image.setColor(Image.TOP_LEFT, color.r, color.g, color.b);
		image.setColor(Image.TOP_RIGHT, color.r, color.g, color.b);
	}

	private void setActiveColor(){
		boolean active = isEnabled() && isMouseover();

		Color buttonColor = this.color;
		if(isEnabled()){
			buttonColor = color;
			if(active){
				buttonColor = this.color.brighter(0.2f);
			}
		}
		else{
			buttonColor = disableColor;
		}
		
		image.setColor(Image.BOTTOM_LEFT, buttonColor.r, buttonColor.g, buttonColor.b);
		image.setColor(Image.BOTTOM_RIGHT, buttonColor.r, buttonColor.g, buttonColor.b);
		image.setColor(Image.TOP_LEFT, buttonColor.r, buttonColor.g, buttonColor.b);
		image.setColor(Image.TOP_RIGHT, buttonColor.r, buttonColor.g, buttonColor.b);
	}
	

	public void update(GameContainer container){
		super.update(container);
		setActiveColor();
	}
	
	public void render(Graphics g){
		image.draw(getX(), getY());
	}

	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}

	public void setLocation(int x, int y) {
		setX(x);
		setY(y);
	}
}
