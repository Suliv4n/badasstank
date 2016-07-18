package fr.sulivan.badasstank.util.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

@FunctionalInterface
public interface ContentRenderer {
	public void render(Graphics g, Rectangle area);
}
