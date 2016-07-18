package fr.sulivan.badasstank.states;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
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
	
	public void setPlayers(PlayersSet players){
		this.players = players;
	}
	
	public void setMap(Map map){
		this.map = map;
	}

}
