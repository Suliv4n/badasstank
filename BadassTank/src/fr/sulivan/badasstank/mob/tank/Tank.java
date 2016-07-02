package fr.sulivan.badasstank.mob.tank;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.hitbox.Hitbox;
import fr.sulivan.badasstank.map.Map;
import fr.sulivan.badasstank.mob.displayer.Displayer;
import fr.sulivan.badasstank.mob.player.Player;

/**
 * 
 * @author Sulivan
 */
public class Tank {
	
	private Carterpillar carterpillar;
	private Body body;
	
	private int x;
	private int y;
	
	private double realX;
	private double realY;
	
	private double rotation = 0;
	
	private boolean moving = false;
	private boolean back;
	
	private Canon canon;

	private Hitbox hitbox;
	
	private String name;
	
	private int maximumBaseHealth = 10;
	private int health;
	
	public Tank(Carterpillar carterpillar, Canon canon, Color color, Body body, String name)
	{
		
		this.name = name;
		
		this.carterpillar = carterpillar; 
		this.body = body;
		this.canon = canon;
		body.setRotation(90);
		body.setCenterOfRotation(body.getWidth() / 2, body.getHeight()/2);
		carterpillar.setColor(color.r, color.g, color.b);
		canon.setColor(color.r, color.g, color.b);
		body.setColor(color.r, color.g, color.b);
	
		int heightHitbox = Math.max(carterpillar.getHeight(), body.getHeight());
		int widthHitbox = body.getWidth() + carterpillar.getWidth();
		
		/*hitbox = new Hitbox(PolygonFactory.createRectangle(
				Configuration.SCREEN_WIDTH / 2 - widthHitbox / 2, 
				Configuration.SCREEN_HEIGHT / 2 - heightHitbox / 2, 
				widthHitbox, 
				heightHitbox
		));*/
		
		hitbox = new Hitbox(new Circle(Configuration.SCREEN_WIDTH / 2, Configuration.SCREEN_HEIGHT / 2, widthHitbox / 2 ));
		setRotation(0);
		
		health = getMaximumHealth();
	}
	
	public void render(boolean center, Graphics g, Map map){
		
		int displayedX = x;
		int displayedY = y;
		
		if(center){
			displayedX = Configuration.SCREEN_WIDTH / 2;
			displayedY = Configuration.SCREEN_HEIGHT / 2;
		}
		else{
			displayedX += map.getX();
			displayedY += map.getY();
		}

		double angle = Math.toRadians(90) - Math.toRadians(rotation);

		double hypo = (double)body.getWidth() / 2.0 + (double)carterpillar.getWidth() / 2.0 - 2;
		int xC1 = displayedX - (int) (Math.cos(angle) * hypo);
		int yC1 = displayedY + (int) (Math.sin(angle) * hypo);
		int xC2 = displayedX + (int) (Math.cos(angle) * hypo);
		int yC2 = displayedY - (int) (Math.sin(angle) * hypo);
		
		carterpillar.render(xC1, yC1, moving);
		carterpillar.render(xC2, yC2, moving);
		body.drawCentered(displayedX, displayedY);
		canon.render(displayedX, displayedY);
		
		g.drawString(name, displayedX - name.length() * 10 / 2, displayedY - body.getHeight() * 2);
	}
	
	public void setRotation(double angle){
		body.setRotation(((int)angle + 90) % 360);
		carterpillar.setRotation(angle);
		rotation = angle % 360;
		
		hitbox.setRotation(Math.toRadians(rotation));
	}
	
	public double getRotation(){
		return rotation;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean isMoving(){
		return moving;
	}
	
	public void setMoving(boolean moving, boolean back){
		this.moving = moving;
		this.back = back;
	}
	
	
	public void setCoordinates(int x, int y) {
		hitbox.setX(x);
		hitbox.setY(y);
		this.x = x;
		this.y = y;
		this.realX = x;
		this.realY = y;
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	/**
	 * Retourne la largeur du tank
	 * @return 
	 * 		la largeur du tank.
	 */
	public int getWidth(){
		return carterpillar.getWidth() - 4 + body.getWidth();
	}
	
	/**
	 * Retourne la hauteur du tank
	 * @return 
	 * 		la hauteur du tank.
	 */
	public int getHeight(){
		return carterpillar.getHeight();
	}
	
	public Canon getCanon(){
		return canon;
	}
	
	public double getSpeed(){
		return carterpillar.getSpeed();
	}
	
	public double getSpeedRotation(){
		return carterpillar.getSpeedRotation();
	}
	
	public void update(Map map, boolean center){
		if(moving){
			double dx = Math.cos(Math.toRadians(rotation)) * getSpeed() * (back ? -1 : 1);
			double dy = Math.sin(Math.toRadians(rotation)) * getSpeed() * (back ? -1 : 1);
			
			boolean collides = hitbox.copy(dx, dy).intersects(map.getHitbox());
			if(!collides){
				for(Player p : map.getPlayers()){
					if(p != this){
						if(hitbox.copy(dx, dy).intersects(p.getHitbox())){
							collides = true;
							dx /= 2;
							dy /= 2;
							((Tank)p).hitbox.moveX(dx);
							((Tank)p).hitbox.moveY(dy);
							((Tank)p).realX += dx;
							((Tank)p).realY += dy;
							((Tank)p).x = (int) ((Tank)p).realX;
							((Tank)p).y = (int) ((Tank)p).realY;
						}
					}
				}
			}
			
			if(!collides){
					
				realX+=dx;
				realY+=dy;
				
				x = (int) realX;
				y = (int) realY;
				
				for(Player p : map.getPlayers()){
					if(p != this){
						p.getHitbox().moveX(-dx);
						p.getHitbox().moveY(-dy);
					}
				}
			
			}
		}
	}
	
	public Displayer fire(int x1, int y1, int x2, int y2){
		return canon.fire(x1, y1, x2, y2);
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getMaximumHealth(){
		//@todo à compléter
		return maximumBaseHealth;
	}

}
