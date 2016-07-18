package fr.sulivan.badasstank.util.gui;

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
	private Color labelColor = Color.white;
	private String label;
	
	public TexturedButtonGUI(Image texture, int width, int height, String label){
		this.width = width;
		this.height = height;
		this.texture = texture;	
		this.label = label;
	}
	
	public void setBorder(int width, Color color){
		border = width;
		borderColor = color;
	}
	
	public void setLabelColor(Color color){
		labelColor = color;
	}
	
	@Override
	public void render(Graphics g){
		

		
		Image textureApply = textureMouseover != null && isMouseover() ? textureMouseover : texture;
		
		g.setClip(new Rectangle(getX()-border, getY()-border, width+border*2, height+border*2));
		for(int x=getX(); x < getX() + width; x+=textureApply.getWidth()){
			for(int y=getY(); y < getY() + height; y+=textureApply.getHeight()){
				g.drawImage(textureApply, x, y);
			}
		}
		
		if(border > 0){
			if(borderColor != null){
				g.setColor(borderColor);
			}
			for(int i=0; i<border; i++){
				g.drawRect(getX()-i-1, getY()-i-1, width+i*2+1, height+i*2+1);
			}
		}
		
		int labelX = getX() + (getWidth() / 2  -  g.getFont().getWidth(label) / 2) ;
		int labelY = getY() + (getHeight() / 2  -  g.getFont().getHeight(label) / 2) ;
		
		g.drawString(label, labelX, labelY);
		
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

	public void setLocation(int x, int y) {
		setX(x);
		setY(y);
	}
	

}
