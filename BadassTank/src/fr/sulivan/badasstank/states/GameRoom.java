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
import fr.sulivan.badasstank.main.BadassTank;
import fr.sulivan.badasstank.mob.player.Player;
import fr.sulivan.badasstank.mob.player.PlayersSet;
import fr.sulivan.badasstank.mob.tank.Body;
import fr.sulivan.badasstank.mob.tank.Canon;
import fr.sulivan.badasstank.mob.tank.Carterpillar;
import fr.sulivan.badasstank.mob.tank.TankPiece;
import fr.sulivan.badasstank.network.Client;
import fr.sulivan.badasstank.network.Server;
import fr.sulivan.badasstank.util.gui.CarouselListGUI;
import fr.sulivan.badasstank.util.gui.ColorPickerGUI;
import fr.sulivan.badasstank.util.gui.ColoredBullButton;
import fr.sulivan.badasstank.util.gui.ElementRenderer;
import fr.sulivan.badasstank.util.gui.TexturedButtonGUI;

public class GameRoom extends BasicGameState{

	private final Color borderColor = Color.white;
	private  PlayersSet players;
	
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
	
	private ColorPickerGUI carterpillarColor;
	private ColorPickerGUI bodyColor;
	private ColorPickerGUI canonColor;
	
	private ColoredBullButton changeCarterpillarColor;
	private ColoredBullButton changeBodyColor;
	private ColoredBullButton changeCanonColor;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		players = new PlayersSet(boxNumberByColumn * 2);
		
		int x=150;
		int y=60;
		
		bodiesData = PiecesLoader.loader().loadBodies();
		
		bodies = new CarouselListGUI<Body>(new Image(Configuration.RESOURCES_FOLDER+"buttons/down.png"), new Image(Configuration.RESOURCES_FOLDER+"buttons/up.png"), 
				new Color(0,100,200), 
				30, 30, 50);
		
		bodies.setElements(bodiesData);
		bodies.setX(x);
		bodies.setY(y);

		bodies.setElementRenderer(new ElementRenderer<Body>() {
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

		canons.setElementRenderer(new ElementRenderer<Canon>() {
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

		carterpillars.setElementRenderer(new ElementRenderer<Carterpillar>() {
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
		
		go = new TexturedButtonGUI(new Image(Configuration.RESOURCES_FOLDER+"textures/yandb.png"), 100, 30, "Go >>");
		go.setTextureMouseOver(new Image(Configuration.RESOURCES_FOLDER+"textures/yandb_mouseover.png"));
		go.setBorder(2, Color.white);
		go.setX(400);
		go.setY(Configuration.SCREEN_HEIGHT - go.getHeight() - 10);
		go.setOnClick(() -> {
			BadassTank.game().startBattle(server, players, currentPlayerPosition);
		});
		
		Player player = new Player((Carterpillar)carterpillars.getElement().clone(), (Canon)canons.getElement().clone(), Color.white, (Body)bodies.getElement().clone(), "Unnamed");
		player.setRotation(90);
		players.put(currentPlayerPosition, player);
		
		
		carterpillarColor = new ColorPickerGUI(container, "Carterpillar color", 
				new Image(Configuration.RESOURCES_FOLDER+"buttons/close.png", new Color(255,0,255)), 
				new Image(Configuration.RESOURCES_FOLDER+"textures/yandb.png"), 
				new Image(Configuration.RESOURCES_FOLDER+"textures/yandb_mouseover.png"));
		carterpillarColor.setLocation(Configuration.SCREEN_WIDTH/2 - carterpillarColor.getWidth()/2, 200);
		carterpillarColor.setBackgroundColor(new Color(50,50,50));
		carterpillarColor.setBorder(2,new Color(150,150,150));
		carterpillarColor.setOnValidate(() -> {
			Color color = carterpillarColor.getColor();
			player.getCarterpillar().setColor(color.r, color.g, color.b);
			changeCarterpillarColor.setColor(color);
			carterpillarColor.close();
			sendChangeColor("carterpillar", color);
		});
		
		bodyColor = new ColorPickerGUI(container, "Body color", 
				new Image(Configuration.RESOURCES_FOLDER+"buttons/close.png", new Color(255,0,255)), 
				new Image(Configuration.RESOURCES_FOLDER+"textures/yandb.png"), 
				new Image(Configuration.RESOURCES_FOLDER+"textures/yandb_mouseover.png"));
		bodyColor.setLocation(Configuration.SCREEN_WIDTH/2 - carterpillarColor.getWidth()/2, 200);
		bodyColor.setBackgroundColor(new Color(50,50,50));
		bodyColor.setBorder(2,new Color(150,150,150));
		bodyColor.setOnValidate(() -> {
			Color color = bodyColor.getColor();
			player.getBody().setColor(color.r, color.g, color.b);
			changeBodyColor.setColor(color);
			bodyColor.close();
			sendChangeColor("body", color);
		});
		
		canonColor = new ColorPickerGUI(container, "Canon color", 
				new Image(Configuration.RESOURCES_FOLDER+"buttons/close.png", new Color(255,0,255)), 
				new Image(Configuration.RESOURCES_FOLDER+"textures/yandb.png"), 
				new Image(Configuration.RESOURCES_FOLDER+"textures/yandb_mouseover.png"));
		canonColor.setLocation(Configuration.SCREEN_WIDTH/2 - carterpillarColor.getWidth()/2, 200);
		canonColor.setBackgroundColor(new Color(50,50,50));
		canonColor.setBorder(2,new Color(150,150,150));
		canonColor.setOnValidate(() -> {
			Color color = canonColor.getColor();
			player.getCanon().setColor(color.r, color.g, color.b);
			changeCanonColor.setColor(color);
			canonColor.close();
			sendChangeColor("canon", color);
		});
		
		changeCarterpillarColor = new ColoredBullButton(10, player.getCarterpillar().getColor());
		changeBodyColor = new ColoredBullButton(10, player.getBody().getColor());
		changeCanonColor = new ColoredBullButton(10, player.getCanon().getColor());
		
		changeCarterpillarColor.setOnClick(() -> {
			carterpillarColor.open();
			canonColor.close();
			bodyColor.close();
			carterpillarColor.setColor(player.getCarterpillar().getColor());
		});
		
		
		changeBodyColor.setOnClick(() -> {
			carterpillarColor.close();
			canonColor.close();
			bodyColor.open();
			
			bodyColor.setColor(player.getBody().getColor());
		});
		
		
		changeCanonColor.setOnClick(() -> {
			carterpillarColor.close();
			canonColor.open();
			bodyColor.close();
			
			canonColor.setColor(player.getCanon().getColor());
		});
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
			
			
			players.get(position).render(playerX, playerY);
		}
		
		//draw stats
		Player player = players.get(currentPlayerPosition);
		int x = boxDimension + 10;
		int y = statY + 10;
		int stringHeight = g.getFont().getHeight("E");
		
		changeBodyColor.setLocation(x, y + 5);
		g.drawString("Body : " + player.getBody(), x + changeBodyColor.getWidth()+5, y);
		y+=stringHeight;
		changeCarterpillarColor.setLocation(x, y + 5);
		g.drawString("Cartepillar : " + player.getCarterpillar(), x + changeCarterpillarColor.getWidth()+5, y);
		y+=stringHeight;
		changeCanonColor.setLocation(x, y + 5);
		g.drawString("Canon : " + player.getCanon(), x + changeCanonColor.getWidth()+5, y);
		
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
		
		if(hosting){
			go.render(g);
		}
		
		carterpillarColor.render(g);
		bodyColor.render(g);
		canonColor.render(g);
		
		changeCarterpillarColor.render(g);
		changeBodyColor.render(g);
		changeCanonColor.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		bodies.update(container, delta);
		canons.update(container, delta);
		carterpillars.update(container, delta);
		
		if(hosting){
			go.update(container);
		}
		
		carterpillarColor.update(container);
		bodyColor.update(container);
		canonColor.update(container);
		
		changeCarterpillarColor.update(container);
		changeBodyColor.update(container);
		changeCanonColor.update(container);
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
			players.removeFromRemoteKey(key);
		});
		
		//------JOIN---------
		/*
		 * When a clients ask to join the rooms.
		 */
		server.on("join", (e) -> {
			String key = e.getKey();
			
			int position = -1;
			boolean adding;
			try {
				position = players.add(new Player(PiecesLoader.loader().loadCarterpillars().get(0), PiecesLoader.loader().loadCanons().get(0), Color.white, PiecesLoader.loader().loadBodies().get(0), "Client"));
				players.get(position).setRemoteKey(key);
				players.get(position).setRotation(90);
				adding = position != -1;
				
			} catch (Exception ex) {
				adding = false;
			}
			
			if(adding){
				//Notification au client qu'il a bien été ajouté au serveur
				if(server.send("joinstatus status=0 message=ok position=" + position + " key=" + e.getKey(), e.getSource())){
					//Envoie des infos des autres joueurs au nouveau client + envoie des infos du nouveau client aux autres clients
					for(Integer pos : players.keySet()){
	
						Player p = players.get(pos);
						HashMap<String, String> parameters = new HashMap<String, String>();
						Color bodyColor = p.getBody().getColor();
						Color carterpillarColor = p.getCarterpillar().getColor();
						Color canonColor = p.getCanon().getColor();
						
						parameters.put("position", String.valueOf(pos));
						parameters.put("carterpillar", p.getCarterpillarId());
						parameters.put("canon", p.getCanonId());
						parameters.put("body", p.getCanonId());
						parameters.put("name", p.getName());
						parameters.put("bodycolor", String.join(",", new String[]{bodyColor.getRed()+"",bodyColor.getGreen()+"",bodyColor.getBlue()+""}));
						parameters.put("carterpillarcolor", String.join(",", new String[]{carterpillarColor.getRed()+"",carterpillarColor.getGreen()+"",carterpillarColor.getBlue()+""}));
						parameters.put("canoncolor", String.join(",", new String[]{canonColor.getRed()+"",canonColor.getGreen()+"",canonColor.getBlue()+""}));
						
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
		
		//------SETCOLOR---------
		/*
		 * When a player change one og his tank piece color
		 */
		server.on("setcolor", e -> {
			String piece = e.getParameter("piece");
			int position = e.getIntParameter("position");
			int[] colorComponents = e.getArrayIntParameter("color");
			
			setPlayerPieceColor(position, piece, colorComponents);
			
			server.broadcast("setcolor", e.getParameters(), e.getSource());
			
		});
		
		this.hosting = true;
	}
	
	public Body getBodyFromId(String id){
		Body body = (Body)bodiesData.stream().filter(b -> b.getId().equals(id)).toArray()[0];
		return (Body)body.clone();
	}
	
	public Carterpillar getCarterpillarFromId(String id){
		Carterpillar carterpillar = (Carterpillar)carterpillarsData.stream().filter(c -> c.getId().equals(id)).toArray()[0];
		return (Carterpillar)carterpillar.clone();
	}
	
	public Canon getCanonFromId(String id){
		Canon canon = (Canon)canonsData.stream().filter(c -> c.getId().equals(id)).toArray()[0];
		return (Canon)canon.clone();
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
			int[] bodyColor = e.getArrayIntParameter("bodycolor");
			int[] carterpillarColor = e.getArrayIntParameter("carterpillarcolor");
			int[] canonColor = e.getArrayIntParameter("canoncolor");
			

			
			Player addingPlayer = new Player(
					getCarterpillarFromId(carterpillarId), 
					getCanonFromId(canonId), 
					Color.white,
					getBodyFromId(bodyId), 
					name);
			addingPlayer.setRotation(90);
			addingPlayer.setRemoteKey(key);
			
			players.put(position, addingPlayer);
			
			setPlayerPieceColor(position, "body", bodyColor);
			setPlayerPieceColor(position, "carterpillar", carterpillarColor);
			setPlayerPieceColor(position, "canon", canonColor);
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
			players.removeFromRemoteKey(key);
		});
		
		client.on("setcolor", e -> {
			String piece = e.getParameter("piece");
			int position = e.getIntParameter("position");
			int[] colorComponents = e.getArrayIntParameter("color");
			
			setPlayerPieceColor(position,  piece, colorComponents);

		});
	}





	public void setPosition(int position) {
		Player player = players.remove(currentPlayerPosition);
		currentPlayerPosition = position;
		players.put(currentPlayerPosition, player);
	}


	public void setRemoteKey(String key) {
		players.get(currentPlayerPosition).setRemoteKey(key);
	}
	
	private void sendChangeColor(String piece, Color color) {
		String message = "setcolor piece=" + piece + " position=" + currentPlayerPosition + " color=" + color.getRed() + "," + color.getGreen() + "," + color.getBlue();
		
		if(hosting){
			server.broadcast(message);
		}
		else{
			client.send(message);
		}
	}
	
	private void setPlayerPieceColor(int position, String piece, int[] colorComponents){
		Color color = new Color(colorComponents[0], colorComponents[1], colorComponents[2]);
		
		if(piece.equals("body")){
			players.get(position).getBody().setColor(color.r, color.g, color.b);
		}
		else if(piece.equals("carterpillar")){
			players.get(position).getCarterpillar().setColor(color.r, color.g, color.b);
		}
		else if(piece.equals("canon")){
			players.get(position).getCanon().setColor(color.r, color.g, color.b);
		}
	}
}
