package fr.sulivan.badasstank.util.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class CarrousselListGUI<T> {
	
	public static final int SLIDING_DEFAULT_DURATION = 500;
	
	private int x;
	private int y;
	private ColoredButtonGUI next;
	private ColoredButtonGUI previous;
	private int slidingDuration = SLIDING_DEFAULT_DURATION;
	
	private int index = 0;
	private int innerHeight;
	
	private ArrayList<T> elements;
	private int elementWidth;
	private int elementHeight;
	
	private Renderable<T> elementRenderer;
	
	private boolean slidingUp = false;
	private boolean slidingDown = false;
	private float slideY = 0f;
	private boolean cyclic = true;
	
	
	public CarrousselListGUI(Image nextButton, Image previousButton, Color buttonColor, int elementWidth, int elementHeight, int innerHeight){
		
		elementRenderer = new Renderable<T>() {};
		
		elements = new ArrayList<T>();
		next = new ColoredButtonGUI(nextButton, buttonColor);
		previous = new ColoredButtonGUI(previousButton, buttonColor);
		
		next.setOnClick(() -> {
			if(!isSliding()){
				slidingDown =  true;
			}
		});
		
		previous.setOnClick(() -> {
			if(!isSliding()){
				slidingUp =  true;
			}
		});
		
		this.elementHeight = elementHeight;
		this.elementWidth = elementWidth;
		this.innerHeight = innerHeight;
	}
	
	
	private boolean isSliding() {
		return slidingDown || slidingUp;
	}

	public void setX(int x){
		this.x = x;
		previous.setX(x + elementWidth / 2 - previous.getWidth() / 2);
		next.setX(x + elementWidth / 2 - next.getWidth() / 2);
	}
	
	public void setY(int y){
		this.y = y;
		previous.setY(y);
		next.setY(y + previous.getHeight() + innerHeight);
	}
	
	public void setElements(ArrayList<T> elements){
		this.elements = elements;
	}
	
	public void update(GameContainer container, int delta){
		next.update(container);
		previous.update(container);
		
		float deltaSlideY = 0.0f;
		if(isSliding()){
			deltaSlideY = (float) elementHeight / (float) slidingDuration;
		}
		
		if(slidingUp){
			slideY = Math.max(slideY - deltaSlideY * delta, -elementHeight);
		}
		else if(slidingDown){
			slideY = Math.min(slideY + deltaSlideY * delta, elementHeight);
		}
		
		if(isSliding() && Math.abs(slideY) >= elementHeight){
			index = (index + (slidingUp ? 1 : -1) + elements.size()) % elements.size();
			slideY = 0.0f;
			slidingDown = slidingUp = false;
			
			System.out.println(index);
		}

		if(!cyclic){
			updateButtons();
		}
	}
	
	private void updateButtons() {
		next.setEnabled(index > 0);
		previous.setEnabled(index < elements.size() - 1);
		System.out.println(index);
	}

	public void render(Graphics g){
		next.render(g);
		previous.render(g);
		int clipHeight = getHeight() - previous.getHeight() - next.getHeight();
		g.setClip(new Rectangle(x, y + previous.getHeight(), getWidth(), clipHeight)); 

		if(elements.size() > 0){
			
			int displayedX = x;
			int displayedY = (y + previous.getHeight() + clipHeight / 2 - elementHeight / 2) - (index * elementHeight);
			
			if(cyclic){
				displayedY -= elements.size() * elementHeight;
			}
			
			for(int i = (cyclic ? -elements.size() : 0); i < elements.size() * (cyclic ? 3 : 1) ; i++){
				T element = elements.get((i + elements.size()) % elements.size());
				elementRenderer.render(g, displayedX, displayedY + (int) slideY, i, element);
				displayedY += elementHeight;
			}
		}
		
		g.clearClip();
	}
	
	public void setElementRenderer(Renderable<T> renderer){
		this.elementRenderer = renderer;
	}
	
	public int getHeight(){
		return next.getHeight() + previous.getHeight() + innerHeight;
	}
	
	public int getWidth(){
		return elementWidth;
	}
	
	public int getIndex(){
		return index;
	}
	
	public T getElement(){
		return elements.get(index);
	}
}
