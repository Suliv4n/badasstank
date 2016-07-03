package fr.sulivan.badasstank.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class ColoredButtonGUI {
	public static final Color DEFAULT_DISABLE_COLOR = new Color(100,100,100);
	private Image image;
	private Color color;
	private Color disableColor = DEFAULT_DISABLE_COLOR;
	
	private int x = 0;
	private int y = 0;
	
	private boolean enabled = true;
	private boolean mouseover;

	private Runnable onClick;

	
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
		boolean active = enabled && mouseover;
		
		Color buttonColor = this.color;
		if(enabled){
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
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void update(GameContainer container){
		int mouseX = container.getInput().getMouseX();
		int mouseY = container.getInput().getMouseY();
		
		mouseover = mouseX > x && mouseX < x + image.getWidth();
		mouseover &= mouseY > y && mouseY < y + image.getHeight();
		
		setActiveColor();

		
		if(mouseover && enabled && container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			if(onClick != null){
				onClick.run();
			}
		}

	}
	
	public void render(){
		image.draw(x, y);
	}
	
	public void setOnClick(Runnable action){
		onClick = action;
	}

	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}

	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
}
