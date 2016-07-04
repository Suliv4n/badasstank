package fr.sulivan.badasstank.mob.tank;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ParticleSystem;

import fr.sulivan.badasstank.mob.displayer.BulletDisplayer;
import fr.sulivan.badasstank.mob.displayer.Displayer;

public class Canon extends TankPiece implements Cloneable {

	private Image image;
	private Image bulletImage;
	private ParticleSystem particles;
	private float speedBullet;
	
	private int cooldown;
	private int range;
	
	private int centerX;
	private int centerY;

	public Canon(String id, Image image, int centerX, int centerY, Image bulletImage, ParticleSystem particles, float speedBullet, int range, int cooldown){
		super(id);
		this.image = image;
		image.setCenterOfRotation(centerX, centerY);
		this.bulletImage = bulletImage;
		this.particles = particles;
		this.speedBullet = speedBullet;
		
		this.centerX = centerX;
		this.centerY = centerY;
		
		this.range = range;
		this.cooldown = cooldown;
				
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
	
	public void setColor(float r, float g, float b){
		image.setColor(0, r, g, b);
		image.setColor(1, r, g, b);
		image.setColor(2, r, g, b);
		image.setColor(3, r, g, b);
	}
	
	public Displayer fire(int x1, int y1, int x2, int y2){
			return new BulletDisplayer(getBulletImage(), getParticles(), x1, y1, x2, y2, getSpeedBullet(), this.range);
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
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return clone;
	}
}
