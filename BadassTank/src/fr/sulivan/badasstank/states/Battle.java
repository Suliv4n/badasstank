package fr.sulivan.badasstank.states;

import java.awt.Point;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.main.BadassTank;
import fr.sulivan.badasstank.map.Map;
import fr.sulivan.badasstank.mob.player.Player;
import fr.sulivan.badasstank.mob.player.PlayersSet;
import fr.sulivan.badasstank.network.Client;
import fr.sulivan.badasstank.network.Server;

public class Battle extends BasicGameState{

	private PlayersSet players;
	private Map map;
	
	private Server server;
	private Client client;
	
	private int currentPlayerPosition;
	
	private boolean hosting;
	
	private String lastUpdate;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		int cursorX = container.getInput().getAbsoluteMouseX();
		int cursorY = container.getInput().getAbsoluteMouseY();
		
		
		map.drawLayer(0);
		
		for(int pos : players.keySet()){
			players.get(pos).render(pos == currentPlayerPosition, g, map);
			players.get(pos).getHitbox().draw(new Color(0f, 0f, 0.5f, 0.5f), g);
		}
		
		map.getHitbox().draw(new Color(0.5f, 0f, 0f, 0.5f), g);
		g.drawString("Curseur : " + cursorX + " ; " + cursorY, 0, 20);
	}
	
	private String getMessageCurrentState(){
		Player player = players.get(currentPlayerPosition);
		
		String message = "update";
		message += " position="+currentPlayerPosition;
		message += " x=" + player.getX();
		message += " y=" + player.getY();
		message += " angle=" + player.getRotation();
		message += " canonrotation=" + player.getCanon().getRotation();
		
		return message;
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
		
		if(!getMessageCurrentState().equals(lastUpdate)){
			if(hosting){
				server.broadcast(getMessageCurrentState());
			}
			else{
				client.send(getMessageCurrentState());
			}
			lastUpdate = getMessageCurrentState();
		}
	}

	@Override
	public int getID() {
		return ID.BATTLE;
	}
	
	public void configureServer(Server server){
		this.server = server;
		hosting = true;
		
		server.on("update", e -> {
			int position = e.getIntParameter("position");
			int x = e.getIntParameter("x");
			int y = e.getIntParameter("y");
			float angle = e.getFloatParameter("angle");
			float angleRotation = e.getFloatParameter("canonrotation");
			
			updatePlayer(players.get(position), x, y, angle, angleRotation);
			
			server.broadcast("update", e.getParameters(), e.getSource());
		});
	}
	
	public void configureClient(Client client){
		this.client = client;
		hosting = false;
		
		client.on("update", e -> {
			
			int position = e.getIntParameter("position");
			int x = e.getIntParameter("x");
			int y = e.getIntParameter("y");
			float angle = e.getFloatParameter("angle");
			float angleRotation = e.getFloatParameter("canonrotation");
			
			updatePlayer(players.get(position), x, y, angle, angleRotation);
			
		});
	}

	private void updatePlayer(Player player, int x, int y, float angle, float angleRotation) {
		player.setCoordinates(x, y, false);
		player.setHitbox(map.getX() + x, map.getY() + y);
		player.setRotation(angle);
		player.getCanon().setRotation(angleRotation);
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
