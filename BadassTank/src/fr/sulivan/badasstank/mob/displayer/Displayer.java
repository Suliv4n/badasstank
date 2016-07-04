package fr.sulivan.badasstank.mob.displayer;

import fr.sulivan.badasstank.hitbox.Hitbox;
import fr.sulivan.badasstank.states.SandBox;


public abstract class Displayer {
	
	private int time = 0;
	private boolean dispose = false;
	
	private float x;
	private float y;
	
	protected Hitbox hitbox;
	
	public Displayer(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public abstract void render(float f, float g);
	public void update(int delta, SandBox game){
		time += delta;
	}
	
	public boolean isDisposed(){
		return dispose;
	}
	
	public void dispose(){
		onDispose();
		dispose = true;
	}
	
	public void onDispose(){
		
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}

	public Hitbox getHitbox() {
		return hitbox;
	}
}
