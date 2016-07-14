package fr.sulivan.badasstank.states;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.XRandR.Screen;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.loader.PiecesLoader;
import fr.sulivan.badasstank.mob.player.Player;
import fr.sulivan.badasstank.mob.tank.Body;
import fr.sulivan.badasstank.mob.tank.Canon;
import fr.sulivan.badasstank.mob.tank.Carterpillar;
import fr.sulivan.badasstank.mob.tank.TankPiece;
import fr.sulivan.badasstank.network.Client;
import fr.sulivan.badasstank.network.Server;
import fr.sulivan.badasstank.util.gui.CarouselListGUI;
import fr.sulivan.badasstank.util.gui.Renderable;
import fr.sulivan.badasstank.util.gui.TexturedButtonGUI;

public class GameRoom extends BasicGameState{

	private final Color borderColor = Color.white;
	private  HashMap<Integer, Player> players;
	
	private final int headerHeight = 31;
	private int boxNumberByColumn = 4;
	
	private int currentPlayerPosition = 0;
	
	private ArrayList<Body> bodiesData;
	private CarouselListGUI<Body> bodies;
	private ArrayList<Carterpillar> carterpillarsData;
	private CarouselListGUI<Carterpillar> carterpillars;
	private ArrayList<Canon> canonsData;
	private CarouselListGUI<Canon> canons;
	
	private Server server;
	private Client client;
	private boolean hosting = false;
	
	private TexturedButtonGUI go;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		players = new HashMap<Integer, Player>();
		
		int x=150;
		int y=60;
		
		bodiesData = PiecesLoader.loader().loadBodies();
		
		bodies = new CarouselListGUI<Body>(new Image(Configuration.RESOURCES_FOLDER+"buttons/down.png"), new Image(Configuration.RESOURCES_FOLDER+"buttons/up.png"), 
				new Color(0,100,200), 
				30, 30, 50);
		
		bodies.setElements(bodiesData);
		bodies.setX(x);
		bodies.setY(y);

		bodies.setElementRenderer(new Renderable<Body>() {
			@Override
			public void render(Graphics g, int x, int y, int index, Body element){
				int displayedX = x + 15;
				int displayedY = y + 15;
				
				element.drawCentered(displayedX, displayedY);
				
			}
		});
		x+=bodies.getWidth();
		
		bodies.setOnChange(() -> {
			Body body = (Body)bodies.getElement().clone();
			players.get(currentPlayerPosition).setBody((Body)bodies.getElement().clone());
			if(hosting){
				server.broadcast("setpiece position="+currentPlayerPosition+" piece=body id="+body.getId());
			}
			else{
				client.send("setpiece position="+currentPlayerPosition+" piece=body id="+body.getId());
			}
		});
		
		canonsData = PiecesLoader.loader().loadCanons();
		
		canons = new CarouselListGUI<Canon>(new Image(Configuration.RESOURCES_FOLDER+"buttons/down.png"), new Image(Configuration.RESOURCES_FOLDER+"buttons/up.png"), 
				new Color(0,100,200), 
				30, 30, 50);
		
		canons.setElements(canonsData);
		canons.setX(x);
		canons.setY(y);

		canons.setElementRenderer(new Renderable<Canon>() {
			@Override
			public void render(Graphics g, int x, int y, int index, Canon element){
				int displayedX = x + 15;
				int displayedY = y + 15;
				
				element.drawCenter(displayedX, displayedY);
			}
		});
		x+=canons.getWidth();
		
		canons.setOnChange(() -> {
			Canon canon = (Canon)canons.getElement().clone();
			players.get(currentPlayerPosition).setCanon(canon);
			if(hosting){
				server.broadcast("setpiece position="+currentPlayerPosition+" piece=canon id="+canon.getId());
			}
			else{
				client.send("setpiece position="+currentPlayerPosition+" piece=canon id="+canon.getId());
			}
		});
		
		carterpillarsData = PiecesLoader.loader().loadCarterpillars();
		carterpillars = new CarouselListGUI<Carterpillar>(new Image(Configuration.RESOURCES_FOLDER+"buttons/down.png"), new Image(Configuration.RESOURCES_FOLDER+"buttons/up.png"), 
				new Color(0,100,200), 
				30, 30, 50);
		
		carterpillars.setElements(carterpillarsData);
		carterpillars.setX(x);
		carterpillars.setY(y);

		carterpillars.setElementRenderer(new Renderable<Carterpillar>() {
			@Override
			public void render(Graphics g, int x, int y, int index, Carterpillar element){
				int displayedX = x + 15;
				int displayedY = y + 15;
				
				element.render(displayedX, displayedY, false);
			}
		});
		
		carterpillars.setOnChange(() -> {
			Carterpillar cartepillar = (Carterpillar)carterpillars.getElement().clone();
			players.get(currentPlayerPosition).setCartepillar(cartepillar);
			if(hosting){
				server.broadcast("setpiece position="+currentPlayerPosition+" piece=carterpillar id="+cartepillar.getId());
			}
			else{
				client.send("setpiece position="+currentPlayerPosition+" piece=carterpillar id="+cartepillar.getId());
			}
		});
		
		Player player = new Player((Carterpillar)carterpillars.getElement().clone(), (Canon)canons.getElement().clone(), Color.white, (Body)bodies.getElement().clone(), "Unnamed");
		player.setRotation(90);
		players.put(currentPlayerPosition, player);
	}


	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		g.clearWorldClip();
		
		//Draw borders
		g.setColor(borderColor);
		
		g.drawLine(0, headerHeight - 1, Configuration.SCREEN_WIDTH, headerHeight - 1);
		g.drawLine(0, headerHeight, Configuration.SCREEN_WIDTH, headerHeight);
		
		g.drawLine(0, headerHeight - 1, Configuration.SCREEN_WIDTH, headerHeight - 1);
		g.drawLine(0, 32, Configuration.SCREEN_WIDTH, 32);
		
		int boxDimension = (Configuration.SCREEN_HEIGHT - headerHeight) / boxNumberByColumn;
		
		g.drawLine(boxDimension+1, headerHeight, boxDimension+1, Configuration.SCREEN_HEIGHT);
		g.drawLine(boxDimension+2, headerHeight, boxDimension+2, Configuration.SCREEN_HEIGHT);
		
		g.drawLine(Configuration.SCREEN_WIDTH - boxDimension-1, headerHeight, Configuration.SCREEN_WIDTH - boxDimension-1, Configuration.SCREEN_HEIGHT);
		g.drawLine(Configuration.SCREEN_WIDTH -boxDimension-2, headerHeight, Configuration.SCREEN_WIDTH - boxDimension-2, Configuration.SCREEN_HEIGHT);
		
		int boxY = headerHeight;
		for(int i=0; i<boxNumberByColumn; i++){
			boxY+=boxDimension;
			
			g.drawLine(0, boxY+1, boxDimension, boxY+1);
			g.drawLine(0, boxY+2, boxDimension, boxY+2);
			
			g.drawLine(Configuration.SCREEN_WIDTH - boxDimension, boxY+1,Configuration.SCREEN_WIDTH, boxY+1);
			g.drawLine(Configuration.SCREEN_WIDTH - boxDimension, boxY+2, Configuration.SCREEN_WIDTH, boxY+2);
		}
		
		int statY = 200;
		g.drawLine(boxDimension+1, statY, Configuration.SCREEN_WIDTH - boxDimension-1, statY);
		g.drawLine(boxDimension+1, statY+1, Configuration.SCREEN_WIDTH - boxDimension-1, statY+1);
		
		//draw players
		for(Integer position : players.keySet()){
			int playerX = boxDimension / 2 - players.get(position).getWidth() / 2;
			int playerY = headerHeight + boxDimension / 2 - players.get(position).getHeight() / 2;
			
			playerY += (position % boxNumberByColumn) * boxDimension;
			
			
			g.drawString(players.get(position).getRemoteKey()+"", playerX + 20, playerY + 20);
			players.get(position).render(playerX, playerY);
		}
		
		//draw stats
		Player player = players.get(currentPlayerPosition);
		int x = boxDimension + 10;
		int y = statY + 10;
		int stringHeight = g.getFont().getHeight("E");
		
		g.drawString("Body : " + player.getBody(), x, y);
		y+=stringHeight;
		g.drawString("Cartepillar : " + player.getCarterpillar(), x, y);
		y+=stringHeight;
		g.drawString("Canon : " + player.getCanon(), x, y);
		
		y+=stringHeight + 10;
		g.drawString("Armor : " + player.getMaximumHealth(), x, y);
		y+=stringHeight;
		g.drawString("Speed : " + player.getSpeed(), x, y);
		y+=stringHeight;
		g.drawString("Rotation : " + player.getSpeedRotation(), x, y);
		y+=stringHeight;
		g.drawString("Power : " + player.getPower(), x, y);
		
		bodies.render(g);
		canons.render(g);
		carterpillars.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		bodies.update(container, delta);
		canons.update(container, delta);
		carterpillars.update(container, delta);
	}

	@Override
	public int getID() {
		return ID.GAME_ROOM;
	}

	public void configureServer(Server server) {
		this.server = server;
		//Set events
		server.clearEvents();
		
		server.setOnClientQuit(key -> {
			removePlayerFromRemoteKey(key);
		});
		
		//------JOIN---------
		/*
		 * When a clients ask to join the rooms.
		 */
		server.on("join", (e) -> {
			String key = e.getKey();
			
			int position = 0;
			
			//recherche d'un emplacement vide
			boolean adding = true;
			while(players.get(position) != null && position < boxNumberByColumn*2){
				position++;
			}
			
			//On essaye d'ajouter le nouveau joueur
			adding = false;
			if(position < boxNumberByColumn*2){
				try {
					players.put(position, new Player(PiecesLoader.loader().loadCarterpillars().get(0), PiecesLoader.loader().loadCanons().get(0), Color.white, PiecesLoader.loader().loadBodies().get(0), "Client"));
					players.get(position).setRemoteKey(key);
					players.get(position).setRotation(90);
					adding = true;
					
				} catch (Exception ex) {
					adding = false;
				}
			}
			
			if(adding){
				System.out.println(e.getSource());
				//Notification au client qu'il a bien été ajouté au serveur
				if(server.send("joinstatus status=0 message=ok position=" + position + " key=" + e.getKey(), e.getSource())){
					//Envoie des infos des autres joueurs au nouveau client + envoie des infos du nouveau client aux autres clients
					for(Integer pos : players.keySet()){
	
						Player p = players.get(pos);
						HashMap<String, String> parameters = new HashMap<String, String>();
						
						parameters.put("position", String.valueOf(pos));
						parameters.put("carterpillar", p.getCarterpillarId());
						parameters.put("canon", p.getCanonId());
						parameters.put("body", p.getCanonId());
						parameters.put("name", p.getName());
						
						//Si le joueur est le hosteur alors il n'a pas de clé.
						if(pos.equals(currentPlayerPosition)){
							parameters.put("key", "HOSTER");
						}
						else{
							parameters.put("key", p.getRemoteKey());
						}
						
						if(pos != position){ //envoie des infos au nouveau client.
							server.send("addplayer", parameters, e.getSource());
						}
						else{ //envoie des infos du nouveau client aux autres clients
							server.broadcast("addplayer", parameters, e.getSource());
						}
					}
				}
				else{
					players.remove(position);
				}
			}else{
				server.send("joinstatus status=1 message=full", e.getSource());
			}
			
		});
		
		//------SETPIECE---------
		/*
		 * When a client set a piece of his tank
		 */
		server.on("setpiece", e -> {
			String piece = e.getParameter("piece");
			String id = e.getParameter("id");
			int position = e.getIntParameter("position");
			
			if(piece.equals("body")){
				players.get(position).setBody((Body) getBodyFromId(id).clone());
			}
			
			if(piece.equals("carterpillar")){
				players.get(position).setCartepillar((Carterpillar) getCarterpillarFromId(id).clone());
			}
			
			if(piece.equals("canon")){
				players.get(position).setCanon((Canon)getCanonFromId(id).clone());
			}
			
			server.broadcast("setpiece", e.getParameters(), e.getSource());
			
		});
		
		this.hosting = true;
	}
	
	public Body getBodyFromId(String id){
		return (Body)bodiesData.stream().filter(b -> b.getId().equals(id)).toArray()[0];
	}
	
	public Carterpillar getCarterpillarFromId(String id){
		return (Carterpillar)carterpillarsData.stream().filter(c -> c.getId().equals(id)).toArray()[0];
	}
	
	public Canon getCanonFromId(String id){
		return (Canon)canonsData.stream().filter(c -> c.getId().equals(id)).toArray()[0];
	}

	public void configureClient(Client client) {
		this.client = client;
		this.hosting = false;
		client.clearEvents();
		
		client.on("addplayer",e -> {
			String carterpillarId = e.getParameter("carterpillar");
			String bodyId = e.getParameter("body");
			String canonId = e.getParameter("canon");
			String name = e.getParameter("name");
			int position = e.getIntParameter("position");
			String key = e.getParameter("key");
			
			Player addingPlayer = new Player(
					getCarterpillarFromId(carterpillarId), 
					getCanonFromId(canonId), 
					Color.white,
					getBodyFromId(bodyId), 
					name);
			addingPlayer.setRotation(90);
			addingPlayer.setRemoteKey(key);
			
			players.put(position, addingPlayer);
		});
		
		client.on("setpiece", e -> {
			String piece = e.getParameter("piece");
			String id = e.getParameter("id");
			int position = e.getIntParameter("position");
			
			if(piece.equals("body")){
				players.get(position).setBody((Body) getBodyFromId(id).clone());
			}
			
			if(piece.equals("carterpillar")){
				players.get(position).setCartepillar((Carterpillar) getCarterpillarFromId(id).clone());
			}
			
			if(piece.equals("canon")){
				players.get(position).setCanon((Canon)getCanonFromId(id).clone());
			}
			
		});
		
		client.on("quit", e -> {
			String key = e.getParameter("key");
			removePlayerFromRemoteKey(key);
		});
		
	}


	private void removePlayerFromRemoteKey(String key) {
		for(Integer position : players.keySet()){
			
			if(players.get(position).getRemoteKey() != null && players.get(position).getRemoteKey().equals(key)){
				players.remove(position);
				break;
			}
		}
	}


	public void setPosition(int position) {
		Player player = players.remove(currentPlayerPosition);
		currentPlayerPosition = position;
		players.put(currentPlayerPosition, player);
	}


	public void setRemoteKey(String key) {
		players.get(currentPlayerPosition).setRemoteKey(key);
	}
}
