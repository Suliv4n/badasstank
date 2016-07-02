package fr.sulivan.badasstank.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class ColoredButtonGUI {
	private Image image;
	private Color color;
	
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

	private void setActiveColor(boolean active){
		
		Color color = this.color;
		if(active){
			color = this.color.brighter(0.2f);
		}
		
		image.setColor(Image.BOTTOM_LEFT, color.r, color.g, color.b);
		image.setColor(Image.BOTTOM_RIGHT, color.r, color.g, color.b);
		image.setColor(Image.TOP_LEFT, color.r, color.g, color.b);
		image.setColor(Image.TOP_RIGHT, color.r, color.g, color.b);
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
		
		setActiveColor(mouseover);

		
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
	
}
