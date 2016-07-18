package fr.sulivan.badasstank.util.gui;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class PopupGUI {
	private final Color DEFAULT_CLOSE_COLOR = new Color(100,0,0);
	private final Color DEFAULT_BACKGROUND_COLOR = Color.black;
	
	public ContentRenderer renderer;
	
	private boolean open = false;
	private String title = "";
	private ColoredButtonGUI closeButton;
	private Color borderColor;
	private int border;
	private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
	
	private int width = 0;
	private int height = 0;
	private int x = 0;
	private int y = 0;
	
	public PopupGUI(String title, int width, int height, Image closeImage){
		this.width = width;
		this.height = height;
		
		this.title = title;
		
		if(closeImage != null){
			this.closeButton = new ColoredButtonGUI(closeImage, DEFAULT_CLOSE_COLOR);
			closeButton.setOnClick(() -> {open=false;});
		}
	}
	
	

	
	public void render(Graphics g){
		if(open){
			if(border > 0){
				g.setColor(borderColor);
				for(int i=0; i<border;i++){
					g.drawRect(x-i-1, y-i-1, width+i*2+1, height+i*2+1);
				}
			}
			
			g.setColor(backgroundColor);
			g.fillRect(x, y, width, height);
			
			if(closeButton != null){
				closeButton.setX(x+width - closeButton.getWidth()-3);
				closeButton.setY(y+3);
				closeButton.render(g);
			}
			
			
			int titleX = x + width/2 - g.getFont().getWidth(title)/2;
			int titleY = y + 5;
			int titleHeight = Math.max(g.getFont().getHeight(title), closeButton.getHeight());
			
			g.setColor(Color.white);
			g.drawString(title, titleX, titleY);
			
			g.setWorldClip(x,y + g.getFont().getHeight(title),width, height);
			if(renderer != null){
				renderer.render(g, new Rectangle(x, y + titleHeight, width, height - titleHeight));
			}
			g.clearWorldClip();
			
		}
	}
	
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setContentRenderer(ContentRenderer renderer){
		this.renderer = renderer;
	}
	
	public void setBorder(int border, Color color){
		borderColor = color;
		this.border = border;
	}


	public int getWidth() {
		return width + border * 2;
	}

	public int getHeight() {
		return height + border * 2;
	}

	public void open(){
		open = true;
	}

	public void close(){
		open = false;
	}
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
	}




	public void update(GameContainer container){
		if(closeButton != null){
			closeButton.update(container);
		}
	}
}
