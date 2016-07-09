package fr.sulivan.badasstank.main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.network.NetworkException;
import fr.sulivan.badasstank.network.Server;
import fr.sulivan.badasstank.states.GameRoom;
import fr.sulivan.badasstank.states.ID;
import fr.sulivan.badasstank.states.SandBox;
import fr.sulivan.badasstank.states.ServerConfiguration;
import fr.sulivan.badasstank.states.TankBuilding;
import fr.sulivan.badasstank.states.TitleScreen;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class BadassTank extends StateBasedGame 
{
	
	 /**//**//**//**//**//**//**//**//**//**//**/
	/**/private static AppGameContainer app;/**/
   /**//**//**//**//**//**//**//**//**//**//**/

	private SandBox sandbox;
	private TankBuilding tankBuilding;
	private TitleScreen titleScreen;
	private ServerConfiguration serverConfiguration;
	private GameRoom gameRoom;
	
	private static boolean fullScreen = false;
	private static BadassTank game;

	private BadassTank(String name) {
		super(name);
	}
	
	public static BadassTank game() {
		return game;
	}
	
	public void launchGame(){
		
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		container.setMouseCursor(new Image(Configuration.RESOURCES_FOLDER+"mouse/cursors.png", new Color(255,0,255)).getSubImage(0,0,13,13), 7, 7);
		
		sandbox = new SandBox();
		tankBuilding = new TankBuilding();
		titleScreen = new TitleScreen();
		serverConfiguration = new ServerConfiguration();
		gameRoom = new GameRoom();
		addState(titleScreen);
		addState(sandbox);
		addState(titleScreen);
		addState(sandbox);
		addState(tankBuilding);
		addState(serverConfiguration);
		addState(gameRoom);
	}
	
	public static void toggleFullScreen() throws SlickException{
		fullScreen = !fullScreen;
		app.setFullscreen(fullScreen);
	}
	
	public static void main(String[] args){
		try {
			game = new BadassTank("Super Badass Tank");
			app = new AppGameContainer(game);
			app.setDisplayMode(Configuration.SCREEN_WIDTH, Configuration.SCREEN_HEIGHT, false);
			app.setTargetFrameRate(Configuration.FPS);
			app.setShowFPS(false);
			app.setFullscreen(fullScreen);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void host(String host) throws SlickException {
		String hostname = host;
		int port = Configuration.DEFAULT_PORT;
		
		if(hostname.matches("^[^:]+:[0-9]+$")){
			String[] parts = hostname.split(":");
			hostname = parts[0];
			port = Integer.parseInt(parts[1]);
		}
		InetSocketAddress address = new InetSocketAddress(hostname, port);
		try {
			Server server = new Server(address.getAddress(), address.getPort());
			gameRoom.setServer(server);
			game.enterState(ID.GAME_ROOM);
			server.start();
		} catch (IOException | NetworkException e) {
			throw new SlickException(e.getMessage(), e);
		}
	}


}