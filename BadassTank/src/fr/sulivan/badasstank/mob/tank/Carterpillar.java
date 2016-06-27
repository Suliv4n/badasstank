package fr.sulivan.badasstank.mob.tank;

import org.newdawn.slick.Animation;

public class Carterpillar {
	
	private Animation animation;
	private double rotation = 0;
	private double speed;
	private double speedRotation;
	
	public Carterpillar(Animation animation, double speed, double speedRotation){
		this.animation = animation;
		this.speed = speed;
		this.speedRotation = speedRotation;
	}
	
	public int getWidth(){
		return animation.getWidth();
	}
	
	public int getHeight(){
		return animation.getHeight();
	}
	
	protected void render(int x, int y, boolean moving){
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
	
	protected void setColor(float r, float g, float b){
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
}
