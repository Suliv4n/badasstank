package fr.sulivan.badasstank.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class RangeGUI {
	private int width;
	private Color color;
	
	private float min = 0;
	private float max = 0;
	
	private int x = 0;
	private int y = 0;
	
	private int cursorX = 0;
	private int cursorHeight = 4;
	
	private boolean dragging = false;
	
	public RangeGUI(Color color, int width, float min, float max){
		this.color = color;
		this.min = min;
		this.max = max;
		this.width = width;
	}
	
	public void render(Graphics g){
		g.setColor(color);
		g.drawLine(x,y + cursorHeight/2, x+width, y + cursorHeight/2);
		g.drawLine(x + cursorX, y, x + cursorX, y + cursorHeight);
	}
	
	public void update(GameContainer container){
		Input in = container.getInput();
		int mouseX = in.getAbsoluteMouseX();
		int mouseY = in .getAbsoluteMouseY();
		
		boolean mouseOver = mouseX < x + getWidth();
		mouseOver &= mouseX > x;
		mouseOver &= mouseY < y + getHeight();
		mouseOver &= mouseY > y;
		
		if(!dragging){
			if(mouseOver && in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
				dragging = true;
			}
		}
		else{
			if(!in.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
				dragging = false;
			}
			cursorX = mouseX - x;
			cursorX = Math.max(0, cursorX);
			cursorX = Math.min(getWidth(), cursorX);
		}
	}
	
	public float getValue(){
		float value = cursorX/(float)getWidth()*((float)max - min)+min;
		return value;
	}
	
	public int getIntValue(){
		return (int) getValue();
	}
	
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return cursorHeight;
	}

	public void setValue(float value) {
		value = Math.max(min, value);
		value = Math.min(max, value);
		
		cursorX = Math.round((value/max*getWidth()));
	}

	public void setLocation(int x, int y) {
		setX(x);
		setY(y);
	}

	public float getMin() {
		return min;
	}
	
	public float getMax() {
		return max;
	}

	public boolean isDragging() {
		return dragging;
	}
}
