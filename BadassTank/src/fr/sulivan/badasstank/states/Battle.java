package fr.sulivan.badasstank.states;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.hud.HUD;
import fr.sulivan.badasstank.loader.PiecesLoader;
import fr.sulivan.badasstank.main.BadassTank;
import fr.sulivan.badasstank.map.Map;
import fr.sulivan.badasstank.mob.displayer.Displayer;
import fr.sulivan.badasstank.mob.player.Player;
import fr.sulivan.badasstank.mob.player.PlayersSet;
import fr.sulivan.badasstank.mob.tank.Body;
import fr.sulivan.badasstank.mob.tank.Canon;
import fr.sulivan.badasstank.mob.tank.Carterpillar;
import fr.sulivan.badasstank.network.Client;
import fr.sulivan.badasstank.network.Server;
import fr.sulivan.badasstank.util.gui.CarouselListGUI;
import fr.sulivan.badasstank.util.gui.ColoredButtonGUI;
import fr.sulivan.badasstank.util.gui.ElementRenderer;

public class Battle extends BasicGameState{

	private PlayersSet players;
	private Map map;
	
	private Server server;
	private Client client;
	
	private int currentPlayerPosition;
	
	private boolean hosting;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		map.drawLayer(0);
		
		for(int pos : players.keySet()){
			players.get(pos).render(pos == currentPlayerPosition, g, map);
			//players.get(pos).getHitbox().draw(new Color(0f, 0f, 0.5f, 0.5f), g);
		}
		
		//map.getHitbox().draw(new Color(0.5f, 0f, 0f, 0.5f), g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Player player = players.get(currentPlayerPosition);
		
		int mapX = Configuration.SCREEN_WIDTH / 2 - player.getX();
		int mapY = Configuration.SCREEN_HEIGHT / 2 - player.getY();
		
		map.setDisplayedCoordinate(mapX, mapY);
		
		Input in = container.getInput();
		
		//Gestion mouvement
		if(in.isKeyDown(Input.KEY_Z)){
			player.setMoving(true, false);
		}
		else if(in.isKeyDown(Input.KEY_S)){
			player.setMoving(true, true);
		}
		else{
			player.setMoving(false, false);
		}
		
		//Gestion rotation
		if(in.isKeyDown(Input.KEY_Q)){
			player.setRotation(player.getRotation() + player.getSpeedRotation());
		}
		else if(in.isKeyDown(Input.KEY_D)){
			player.setRotation(player.getRotation() - player.getSpeedRotation());
		}
		
		//Gestion FullScreen
		if(in.isKeyDown(Input.KEY_F1)){
			BadassTank.toggleFullScreen();
		}
		
		//Rotation du canon en fonction 
		player.getCanon().setRotation(Configuration.SCREEN_WIDTH / 2, Configuration.SCREEN_HEIGHT / 2 , in.getAbsoluteMouseX(), in.getAbsoluteMouseY());
		player.update(map, true);
	}

	@Override
	public int getID() {
		return ID.BATTLE;
	}
	
	public void configureServer(Server server){
		this.server = server;
		hosting = true;
	}
	
	public void configureClient(Client client){
		this.client = client;
		hosting = false;
	}


	public void setEnvironment(PlayersSet players, Map map, int position) {
		this.map = map;
		this.players = players;
		this.currentPlayerPosition = position;
		
		ArrayList<Point> slots = map.getSlotsLocation();
		
		for(int i=0; i<slots.size(); i++){
			Player p = players.get(i);
			if(p != null){
				p.setCoordinates(slots.get(i).x, slots.get(i).y, i!=currentPlayerPosition);
				map.registerPlayer(p);
			}
		}
		
		for(int pos : players.keySet()){
			players.get(pos).setHitbox(players.get(currentPlayerPosition));
		}
	}

}
