package fr.sulivan.badasstank.mob.tank;

import org.newdawn.slick.Color;

public abstract class TankPiece {
	private String id;
	private String name;
	protected double rotation;
	protected Color color = Color.white;
	
	public TankPiece(String id, String name) {
		this.id = id;
		this.name = name;
	}

	protected abstract void setColor(float r, float g, float b);
	
	public String getId(){
		return id;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
