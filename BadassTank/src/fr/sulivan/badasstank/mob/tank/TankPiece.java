package fr.sulivan.badasstank.mob.tank;

public abstract class TankPiece {
	private String id;
	private String name;
	protected double rotation;
	
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
