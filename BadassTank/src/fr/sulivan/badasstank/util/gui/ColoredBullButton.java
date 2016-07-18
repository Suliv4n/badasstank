package fr.sulivan.badasstank.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class ColoredBullButton extends ButtonGUI{

	private Color color = Color.white;
	private int radius;
	
	public ColoredBullButton(int radius, Color color){
		this.radius = radius;
		this.color = color;
	}
	
	@Override
	public int getHeight() {
		return radius;
	}

	@Override
	public int getWidth() {
		return radius;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillOval(getX(), getY(), radius, radius);
	}
	
	public void setLocation(int x, int y){
		setX(x);
		setY(y);
	}

	public void setColor(Color color){
		this.color = color;
	}
}
