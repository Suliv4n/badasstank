package fr.sulivan.badasstank.mob.player;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;












import fr.sulivan.badasstank.mob.tank.Body;
import fr.sulivan.badasstank.mob.tank.Canon;
import fr.sulivan.badasstank.mob.tank.Carterpillar;
import fr.sulivan.badasstank.mob.tank.Tank;


public class Player extends Tank{
	
	private String remoteKey;
	
	public Player(Carterpillar carterpillar, Canon canon, Color color, Body body, String name)  {
		super(carterpillar, canon, color, body, name);
	}

	public void setRemoteKey(String key) {
		remoteKey = key;
	}

	public String getRemoteKey(){
		return remoteKey;
	}






}
