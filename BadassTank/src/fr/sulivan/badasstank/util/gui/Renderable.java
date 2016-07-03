package fr.sulivan.badasstank.util.gui;

import org.newdawn.slick.Graphics;

public interface Renderable<T> {
	
	public default void render(Graphics g, int x, int y, int index, T element){
		g.drawString(element.toString(), x, y);
	}
}
