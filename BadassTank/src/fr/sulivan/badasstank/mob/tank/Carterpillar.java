package fr.sulivan.badasstank.mob.tank;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.SpriteSheet;

public class Carterpillar extends TankPiece implements Cloneable{
	
	private Animation animation;
	private SpriteSheet spritesheet;
	private double speed;
	private double speedRotation;
	
	public Carterpillar(String id, String name, SpriteSheet spritesheet, double speed, double speedRotation){
		super(id, name);
		this.spritesheet = spritesheet;
		buildAnimation();
		this.speed = speed;
		this.speedRotation = speedRotation;
	}
	
	public void buildAnimation(){
		animation = new Animation(spritesheet, 0, 0, 2, 0,true, 100, true);
	}
	
	public int getWidth(){
		return animation.getWidth();
	}
	
	public int getHeight(){
		return animation.getHeight();
	}
	
	public void render(int x, int y, boolean moving){
		if(moving){
			animation.draw(x - (float)((double)animation.getWidth()/2.0), (float) ((double) y - animation.getHeight()/2.0));
		}
		else{
			animation.getCurrentFrame().drawCentered(x,y);
		}
	}
	
	protected void setRotation(double angle){
		rotation = (angle - 270) % 360;
		for(int i = 0; i<animation.getFrameCount(); i++){
			animation.getImage(i).setRotation((int) rotation);
		}
	}
	
	public void setColor(float r, float g, float b){
		super.color = new Color(r,g,b);
		
		for(int i = 0; i<animation.getFrameCount(); i++){
			animation.getImage(i).setColor(0, r, g, b);
			animation.getImage(i).setColor(1, r, g, b);
			animation.getImage(i).setColor(2, r, g, b);
			animation.getImage(i).setColor(3, r, g, b);
		}
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public double getSpeedRotation()
	{
		return speedRotation;
	}
	
	@Override
	public Object clone(){
		Carterpillar clone = null;
		try {
			clone = (Carterpillar) super.clone();
			clone.spritesheet = new SpriteSheet(spritesheet.copy(), this.spritesheet.getWidth()/this.spritesheet.getHorizontalCount(), this.spritesheet.getHeight());
			clone.buildAnimation();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return clone;
	}

	public Color getColor() {
		return color;
	}
}
