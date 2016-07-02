package fr.sulivan.badasstank.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.mob.tank.Tank;
import fr.sulivan.badasstank.states.Game;
import fr.sulivan.badasstank.util.TypeBarre;
import fr.sulivan.badasstank.util.gui.BarUI;

public class HUD {
	
	private Game game;
	private BarUI healthPoints;
	
	public HUD(Game game){
		this.game = game;
		healthPoints = new BarUI(Configuration.HEALTH_BAR_COLOR, Color.black, 100, 15, game.getPlayer().getHealth(), game.getPlayer().getMaximumHealth(), TypeBarre.LEFT_TO_RIGHT,true, Configuration.BORDER_COLOR);
	}
	
	public void render(Graphics g){
		healthPoints.render(g, 20, 20);
		g.setColor(Color.white);
		g.drawString(game.getPlayer().getHealth() + "/" + game.getPlayer().getMaximumHealth(), 22, 18);
	}
	
	public void update(){
		healthPoints.setCurrent(game.getPlayer().getHealth());
	}
}
