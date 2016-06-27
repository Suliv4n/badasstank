package fr.sulivan.badasstank.hitbox;



import java.util.ArrayList;







import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.Transform;

public class Hitbox {
	private ArrayList<Shape> shapes;
	private float currentAngle = 0;
	
	public Hitbox(){
		shapes = new ArrayList<Shape>();
	}
	
	
	public Hitbox(Shape shape) {
		shapes = new ArrayList<Shape>();
		shapes.add(shape);
	}


	public void addShape(Shape shape){
		shapes.add(shape);
	}
	
	public boolean intersects(Hitbox other){
		for(Shape s : shapes){
			for(Shape o : other.shapes){
				if(s.intersects(o)){
					return true;
				}
			}
		}

		return false;
	}

	public void moveX(double d) {
		for(Shape s : shapes){
			s.setX((float) (s.getX() + d));
		}
	}
	
	public void moveY(double d) {
		for(Shape s : shapes){
			s.setY((float) (s.getY() + d));
		}
	}

	public void setX(float x) {
		for(Shape s : shapes){
			s.setX(x);
		}
	}
	
	public void setY(float y) {
		for(Shape s : shapes){
			s.setY(y);
		}
	}
	
	public void setCenterX(float x) {
		for(Shape s : shapes){
			s.setCenterX(x);
		}
	}
	
	public void setCenterY(float y) {
		for(Shape s : shapes){
			s.setCenterY(y);
		}
	}

	/**
	 * Retourne une copie de la hitbox avec un décalage.
	 * 
	 * @param dx
	 * 	Décalage abscisse
	 * @param dy
	 * 	Décalage ordonÃ©e
	 * @return
	 */
	public Hitbox copy(double dx, double dy) {
		Hitbox copy = new Hitbox();
		
		for(Shape s : shapes){
								//pseudo copy
			Shape shapeCopy = s.transform(Transform.createTranslateTransform((float)dx, (float)dy));
			copy.addShape(shapeCopy);
		}
		return copy;
	}
	
	public Hitbox copyRotation(double angle) {
		Hitbox copy = new Hitbox();
		float dAngle = (float) ((angle - currentAngle) % (Math.PI * 2));
		for(Shape p : shapes){
			copy.addShape(
					p.transform(new Transform(Transform.createRotateTransform(dAngle, p.getCenterX(), p.getCenterY())))
			);
		}
		return copy;
	}
	
	public void draw(Color color, Graphics g){
		Color current = g.getColor();
		g.setColor(color);
		for(Shape s : shapes){
			ShapeRenderer.fill(s);
			g.draw(s);
		}
		g.setColor(current);
	}

	public void setRotation(double angle) {
		ArrayList<Shape> setPolygons = new ArrayList<Shape>();
		float dAngle = (float) ((angle - currentAngle) % (Math.PI * 2));
		for(Shape p : shapes){
			setPolygons.add(p.transform(new Transform(Transform.createRotateTransform(dAngle, p.getCenterX(), p.getCenterY()))));
			System.out.println(Math.toRadians(dAngle));
		}
		currentAngle += dAngle;
		shapes = setPolygons;
	}
	
}
