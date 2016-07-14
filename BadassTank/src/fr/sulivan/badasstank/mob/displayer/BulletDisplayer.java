package fr.sulivan.badasstank.mob.displayer;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;

import fr.sulivan.badasstank.hitbox.Hitbox;
import fr.sulivan.badasstank.states.SandBox;
import fr.sulivan.badasstank.util.PolygonFactory;


public class BulletDisplayer extends Displayer{

	private Image image;
	private ParticleSystem particles;
	private Vector2f direction;
	
	private float distance = 0;
	private int range;
	
	private int power;
	
	public BulletDisplayer(Image image, ParticleSystem particles, int x1, int y1 , int x2, int y2, float speed, int range, int power) {
		super(x1, y1);
		double hypo = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
		double angle = Math.acos((x1-x2) / hypo);
		if(image != null){
			this.image = image;	
			image.rotate((int) Math.toDegrees(y2 - y1 < 0 ? angle : -angle) - 90);
		}
		
		hitbox = new Hitbox();
		hitbox.addShape(PolygonFactory.createRectangle(x1, y1, image.getWidth(), image.getHeight()));
		hitbox.setRotation(angle);
		
		float dx = -(float) Math.cos(y2 - y1 < 0 ? -angle : angle) * speed;
		float dy = (float) Math.sin(y2 - y1 < 0 ? -angle : angle) * speed;
		direction = new Vector2f(dx, dy);
		
		this.particles = particles;
		
		this.range = range;
		this.power = power;
	}
	
	
	@Override
	public void render(float x, float y) {
		if(image != null){
			image.drawCentered((int)x, (int)y);
		}
		if(particles != null){
			particles.render((int)x, (int)y);
		}
		
		//TODO En fonction de la map. À foutre dans update
		hitbox.setCenterX(x);
		hitbox.setCenterY(y);
	}
	
	@Override
	public void update(int delta, SandBox game) {
		super.update(delta, game);
		if(particles != null){
			particles.update(delta);
		}
		super.setX( super.getX() + direction.x );
		super.setY( super.getY() + direction.y );

		
		distance += Math.sqrt(direction.x * direction.x + direction.y * direction.y);
		
		if(game.getMap().getHitbox().intersects(hitbox)){
			dispose();
		}
		
		if(distance >= this.range){
			super.dispose();
		}
	}
}
