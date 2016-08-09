package fr.sulivan.badasstank.mob.tank;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ParticleSystem;

import fr.sulivan.badasstank.mob.displayer.BulletDisplayer;
import fr.sulivan.badasstank.mob.displayer.Displayer;

public class Canon extends TankPiece implements Cloneable {

	private Image image;
	private Image bulletImage;
	private ParticleSystem particles;
	private float speedBullet;
	private int power;
	private int bulletPower;
	
	private int cooldown;
	private int range;
	
	private int centerX;
	private int centerY;
	
	private int currentCooldown = 0;

	public Canon(String id, String name, Image image, int centerX, int centerY, Image bulletImage, ParticleSystem particles, float speedBullet, int range, int power, int cooldown){
		super(id, name);
		this.image = image;
		image.setCenterOfRotation(centerX, centerY);
		this.bulletImage = bulletImage;
		this.particles = particles;
		this.speedBullet = speedBullet;
		
		this.centerX = centerX;
		this.centerY = centerY;
		
		this.range = range;
		this.cooldown = cooldown;
		
		this.power = power;
				
	}
	
	public void render(int x, int y){
		image.draw(x - centerX, y - centerY);
	}
	
	public void setRotation(int x1, int y1, int x2, int y2){
		double hypo = Math.sqrt((double) ((x2-x1) * (x2-x1) + (y2-y1) * (y2 -y1)));
		
		double rads = Math.acos((double) (x2 - x1) / hypo);
		if(y2 - y1 < 0){
			rads = -rads;
		}
		double angle = Math.toDegrees(rads);
		
	
		image.setRotation(((int)angle + 90) % 360);
	}
	

	public void setRotation(float angle) {
		image.setRotation(angle);
	}
	
	public void setColor(float r, float g, float b){
		color = new Color(r,g,b);
		image.setColor(0, r, g, b);
		image.setColor(1, r, g, b);
		image.setColor(2, r, g, b);
		image.setColor(3, r, g, b);
	}
	
	public Displayer fire(Tank source, int x1, int y1, int x2, int y2){
		if(canShoot()){
			currentCooldown = cooldown;
			return new BulletDisplayer(source, getBulletImage(), getParticles(), x1, y1, x2, y2, getSpeedBullet(), this.range, this.power);
		}
		
		return null;
	}
	
	//Bullet
	public Image getBulletImage(){
		return bulletImage.copy();
	}
	
	public ParticleSystem getParticles(){
		return particles;
	}

	public float getSpeedBullet() {
		return speedBullet;
	}

	public void drawCenter(int x, int y) {
		image.drawCentered(x, y);
	}
	
	@Override
	public Object clone(){
		Canon clone = null;
		try {
			clone = (Canon) super.clone();
			clone.image = this.image.copy();
			clone.image.setCenterOfRotation(centerX, centerY);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return clone;
	}

	public int getPower() {
		return power;
	}
	
	public Color getColor() {
		return color;
	}

	public float getRotation() {
		return image.getRotation();
	}

	protected void delta(int delta) {
		currentCooldown = Math.max(currentCooldown - delta, 0);
	}
	
	public boolean canShoot(){
		return currentCooldown == 0;
	}
	
	public int cooldownSpend(){
		return cooldown - currentCooldown;
	}

	public float getCooldown() {
		return cooldown;
	}
}
