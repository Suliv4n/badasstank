package fr.sulivan.badasstank.util.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public abstract class ButtonGUI {
	
	private int x = 0;
	private int y = 0;
	
	private boolean enabled = true;
	private boolean mouseover;

	private Runnable onClick;
	private Runnable onMouseOver;


	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	
	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}
	
	
	public void update(GameContainer container){
		int mouseX = container.getInput().getMouseX();
		int mouseY = container.getInput().getMouseY();
		
		mouseover = mouseX > x && mouseX < x + this.getWidth();
		mouseover &= mouseY > y && mouseY < y + this.getHeight();
		
		if(mouseover && onMouseOver != null){
			onMouseOver.run();
		}
		
		if(mouseover && enabled && container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			if(onClick != null){
				onClick.run();
			}
		}
	}
	
	public abstract int getHeight();
	public abstract int getWidth();


	public abstract void render(Graphics g);
	
	public void setOnClick(Runnable action){
		onClick = action;
	}
	
	public void setOnMouseOver(Runnable action){
		onMouseOver = action;
	}

	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isMouseover() {
		return mouseover;
	}
}
