package fr.sulivan.badasstank.states;

import java.util.HashMap;

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
import fr.sulivan.badasstank.network.Client;
import fr.sulivan.badasstank.network.Server;
import fr.sulivan.badasstank.util.gui.CarouselListGUI;
import fr.sulivan.badasstank.util.gui.Renderable;

public class GameRoom extends BasicGameState{

	private final Color borderColor = Color.white;
	private  HashMap<Integer, Player> players;
	
	private final int headerHeight = 31;
	private int boxNumberByColumn = 4;
	
	private int currentPlayerPosition = 0;
	
	private CarouselListGUI<Body> bodies;
	private CarouselListGUI<Carterpillar> carterpillars;
	private CarouselListGUI<Canon> canons;
	
	private Server server;
	private Client client;
	
	private boolean hosting = false;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		players = new HashMap<Integer, Player>();
		
		int x=150;
		int y=60;
		
		bodies = new CarouselListGUI<Body>(new Image(Configuration.RESOURCES_FOLDER+"buttons/down.png"), new Image(Configuration.RESOURCES_FOLDER+"buttons/up.png"), 
				new Color(0,100,200), 
				30, 30, 50);
		
		bodies.setElements(PiecesLoader.loader().loadBodies());
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
			players.get(currentPlayerPosition).setBody((Body)bodies.getElement().clone());
		});
		
		canons = new CarouselListGUI<Canon>(new Image(Configuration.RESOURCES_FOLDER+"buttons/down.png"), new Image(Configuration.RESOURCES_FOLDER+"buttons/up.png"), 
				new Color(0,100,200), 
				30, 30, 50);
		
		canons.setElements(PiecesLoader.loader().loadCanons());
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
			players.get(currentPlayerPosition).setCanon((Canon)canons.getElement().clone());
		});
		
		carterpillars = new CarouselListGUI<Carterpillar>(new Image(Configuration.RESOURCES_FOLDER+"buttons/down.png"), new Image(Configuration.RESOURCES_FOLDER+"buttons/up.png"), 
				new Color(0,100,200), 
				30, 30, 50);
		
		carterpillars.setElements(PiecesLoader.loader().loadCarterpillars());
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
			players.get(currentPlayerPosition).setCartepillar((Carterpillar)carterpillars.getElement().clone());
		});
		
		Player player = new Player((Carterpillar)carterpillars.getElement().clone(), (Canon)canons.getElement().clone(), Color.white, (Body)bodies.getElement().clone(), "Unnamed");
		player.setRotation(90);
		players.put(currentPlayerPosition, player);
	}


	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.clearWorldClip();
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
		
		int y = headerHeight;
		for(int i=0; i<boxNumberByColumn; i++){
			y+=boxDimension;
			
			g.drawLine(0, y+1, boxDimension, y+1);
			g.drawLine(0, y+2, boxDimension, y+2);
			
			g.drawLine(Configuration.SCREEN_WIDTH - boxDimension, y+1,Configuration.SCREEN_WIDTH, y+1);
			g.drawLine(Configuration.SCREEN_WIDTH - boxDimension, y+2, Configuration.SCREEN_WIDTH, y+2);
		}
		
		int statY = 200;
		g.drawLine(boxDimension+1, statY, Configuration.SCREEN_WIDTH - boxDimension-1, statY);
		g.drawLine(boxDimension+1, statY+1, Configuration.SCREEN_WIDTH - boxDimension-1, statY+1);
		
		for(Integer position : players.keySet()){
			int playerX = boxDimension / 2 - players.get(position).getWidth() / 2;
			int playerY = headerHeight + boxDimension / 2 - players.get(position).getHeight() / 2;
			
			playerY += (position % boxNumberByColumn) * boxDimension;
			
			players.get(position).render(playerX, playerY);
		}
		
		
		
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

	public void setServer(Server server) {
		this.server = server;
		
		//Set events
		
		//------JOIN---------
		/*
		 * When a clients ask to join the rooms.
		 */
		server.on("join", (e) -> {
			int position = 0;
			
			boolean adding = true;
			while(players.get(position) != null && position < boxNumberByColumn*2){
				position++;
			}
			
			System.out.println(position);
			
			adding = false;
			if(position < boxNumberByColumn*2){
				try {
					players.put(position, new Player(PiecesLoader.loader().loadCarterpillars().get(0), PiecesLoader.loader().loadCanons().get(0), Color.white, PiecesLoader.loader().loadBodies().get(0), "Client"));
					players.get(position).setRotation(90);
					adding = true;
					
				} catch (Exception ex) {
					adding = false;
				}
			}
			

			if(adding){
				System.out.println(e.getSource());
				if(!server.send("joinstatus status=0 message=ok position="+position, e.getSource())){
					players.remove(position);
					return false;
				}
				return true;
			}else{
				return server.send("joinstatus status=1 message=full", e.getSource());
			}
			
		});
		
		
		
		this.hosting = true;
	}


	public void setClient(Client client) {
		this.client = client;
		this.hosting = false;
	}


	public void setPosition(int position) {
		Player player = players.remove(currentPlayerPosition);
		currentPlayerPosition = position;
		players.put(currentPlayerPosition, player);
	}

}
