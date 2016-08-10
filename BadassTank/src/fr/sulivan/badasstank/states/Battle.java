package fr.sulivan.badasstank.states;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.hud.HUD;
import fr.sulivan.badasstank.main.BadassTank;
import fr.sulivan.badasstank.map.Map;
import fr.sulivan.badasstank.mob.displayer.BulletDisplayer;
import fr.sulivan.badasstank.mob.player.Player;
import fr.sulivan.badasstank.mob.player.PlayersSet;
import fr.sulivan.badasstank.network.Client;
import fr.sulivan.badasstank.network.Server;
import fr.sulivan.badasstank.util.Timer;

public class Battle extends BasicGameState{

	private PlayersSet players;
	private Map map;
	
	private Server server;
	private Client client;
	
	private int currentPlayerPosition;
	
	private boolean hosting;
	
	private String lastUpdate;
	
	private ArrayList<BulletDisplayer> bullets = new ArrayList<BulletDisplayer>();
	
	private HUD hud;
	
	private HashMap<Integer, Integer> healthBackUp;
	
	private Timer respawnTimer;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		bullets = new ArrayList<BulletDisplayer>();
		healthBackUp = new HashMap<Integer, Integer>();
		respawnTimer = new Timer(Configuration.MAXIMUM_TIME_BEFORE_RESPAWN);
		respawnTimer.setOnFinish(() -> {
			respawn();
			respawnTimer.stop();
		});
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game){
		hud = new HUD(this);
		if(hosting){
			healthBackUp.clear();
			for(Integer position : players.keySet()){
				healthBackUp.put(position, players.get(position).getHealth());
			}
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		

		
		map.drawLayer(0);
		
		for(int pos : players.keySet()){
			players.get(pos).render(pos == currentPlayerPosition, g, map);
			players.get(pos).getHitbox().draw(new Color(0f, 0f, 0.5f, 0.5f), g);
		}
		
		map.getHitbox().draw(new Color(0.5f, 0f, 0f, 0.5f), g);
		//g.drawString("Curseur : " + cursorX + " ; " + cursorY, 0, 20);
		
		for(BulletDisplayer b : bullets){
			b.render( b.getX() + map.getX(),  b.getY() + map.getY() );
		}
		
		if(getPlayer().getHealth() == 0){
			g.setColor(new Color(0f,0f,0f,0.5f));
			g.fillRect(0, 0, Configuration.SCREEN_WIDTH, Configuration.SCREEN_HEIGHT);
		}
		hud.render(g);
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
		player.update(map, true, delta);
		
		if(!getMessageCurrentState().equals(lastUpdate)){
			if(hosting){
				server.broadcast(getMessageCurrentState());
			}
			else{
				client.send(getMessageCurrentState());
			}
			lastUpdate = getMessageCurrentState();
		}
		
		if(in.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			if(getPlayer().canShoot()){
				int dx = in.getAbsoluteMouseX() - map.getX();
				int dy = in.getAbsoluteMouseY() - map.getY();
				if(hosting){
					server.broadcast("fire position="+currentPlayerPosition+" dx="+dx+" dy="+dy);
				}
				else{
					client.send("fire position="+currentPlayerPosition+" dx="+dx+" dy="+dy);
				}
				
				bullets.add((BulletDisplayer) player.fire(player.getX(), player.getY(), in.getAbsoluteMouseX() - map.getX(), in.getAbsoluteMouseY() - map.getY()));
			}
		}
		
		//Update bullets
		ArrayList<BulletDisplayer> toDispose = new ArrayList<BulletDisplayer>();
		for(BulletDisplayer b : bullets){

			b.update(delta, this);
			
			if(b.isDisposed()){
				toDispose.add(b);
			}
		}
		
		for(BulletDisplayer b : toDispose){
			bullets.remove(b); 
		}
		
		if(hosting){
			for(int position : healthBackUp.keySet()){
				if(healthBackUp.get(position) != players.get(position).getHealth()){
					server.broadcast("health position="+position+" health="+players.get(position).getHealth());
					healthBackUp.put(position, players.get(position).getHealth());
				}
			}
		}
		
		hud.update();
		if(getPlayer().getHealth() == 0){
			respawnTimer.start();
			respawnTimer.update(delta);
		}
	}

	@Override
	public int getID() {
		return ID.BATTLE;
	}
	
	public void configureServer(Server server){
		server.setOnClientQuit(key -> {
			int remove = players.getPositionFromRemoteKey(key);
			map.unregisterPlayer(players.get(remove));
			healthBackUp.remove(remove);
			players.remove(remove);
		});
		
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
		
		server.on("fire", e -> {
			int position = e.getIntParameter("position");
			int dx = e.getIntParameter("dx");
			int dy = e.getIntParameter("dy");
			
			Player player = players.get(position);
			bullets.add((BulletDisplayer) players.get(position).fire( player.getX(), player.getY(), dx, dy));
			
			server.broadcast("fire", e.getParameters(), e.getSource());
		});
		
		server.on("respawn", e -> {
			int position = e.getIntParameter("position");
			
			players.get(position).fullHealth();
			server.broadcast("respawn position="+position, e.getSource());
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
		
		client.on("fire", e -> {
			int position = e.getIntParameter("position");
			int dx = e.getIntParameter("dx");
			int dy = e.getIntParameter("dy");
			
			Player player = players.get(position);
			bullets.add((BulletDisplayer) players.get(position).fire( player.getX(), player.getY(), dx, dy));
		});
		
		client.on("health", e -> {
			int position = e.getIntParameter("position");
			int health = e.getIntParameter("health");
			
			if(position != currentPlayerPosition){
				players.get(position).setHealth(health);
			}
		});
		
		client.on("respawn", e -> {
			int position = e.getIntParameter("position");
			
			players.get(position).fullHealth();
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
				if(i!=currentPlayerPosition){
					p.setHitbox(map.getX() + slots.get(i).x, map.getY() + slots.get(i).y);
				}
				map.registerPlayer(p);
			}
		}
	}
	

	public Map getMap() {
		return map;
	}

	public Player getPlayer() {
		return players.get(currentPlayerPosition);
	}
	
	private void respawn(){
		if(hosting){
			server.broadcast("respawn position="+currentPlayerPosition);
		}
		else{
			client.send("respawn position="+currentPlayerPosition);
		}
		Point slot = map.getRandomSlotLocation();
		getPlayer().setCoordinates(slot.x, slot.y, false);
		getPlayer().fullHealth();
		
		
		int mapX = Configuration.SCREEN_WIDTH / 2 - getPlayer().getX();
		int mapY = Configuration.SCREEN_HEIGHT / 2 - getPlayer().getY();
		
		map.setDisplayedCoordinate(mapX, mapY);
		
		for(Integer i: players.keySet()){
			players.get(i).setHitbox(map.getX() + players.get(i).getX(), map.getY()  + players.get(i).getY());
		}
	}
	
}
