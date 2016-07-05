package fr.sulivan.badasstank.util.gui;

import java.awt.Button;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class TexturedButtonGUI extends ButtonGUI{

	private Image texture;
	private Image textureMouseover;
	private int width;
	private int height;
	private int border = 0;
	private Color borderColor = Color.black;
	
	public TexturedButtonGUI(Image texture, int width, int height, String label){
		this.width = width;
		this.height = height;
		this.texture = texture;	
	}
	
	public void setBorder(int width, Color color){
		border = width;
		borderColor = color;
	}
	
	@Override
	public void render(Graphics g){
		
		if(border > 0){
			for(int i=0; i<border; i++){
				g.setColor(borderColor);
				g.drawRect(getX()-i-1, getY()-i-1, width+i+1, height+i+1);
			}
		}
		
		Image textureApply = textureMouseover != null && isMouseover() ? textureMouseover : texture;
		
		g.setWorldClip(new Rectangle(getX(), getY(), width, height));
		for(int x=getX(); x < getX() + width; x+=textureApply.getWidth()){
			for(int y=getY(); y < getY() + height; y+=textureApply.getHeight()){
				g.drawImage(textureApply, x, y);
			}
		}
		g.clearClip();
	}
	
	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	public void setTextureMouseOver(Image texture) {
		textureMouseover = texture;
	}
	

}
