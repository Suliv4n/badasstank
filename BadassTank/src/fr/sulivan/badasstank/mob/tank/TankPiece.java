package fr.sulivan.badasstank.mob.tank;

public abstract class TankPiece {
	private String id;
	protected double rotation;
	
	public TankPiece(String id) {
		this.id = id;
	}

	protected abstract void setColor(float r, float g, float b);
	
	public String getId(){
		return id;
	}
}
