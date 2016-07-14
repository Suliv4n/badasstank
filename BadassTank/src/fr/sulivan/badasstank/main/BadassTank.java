package fr.sulivan.badasstank.main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.network.Client;
import fr.sulivan.badasstank.network.NetworkException;
import fr.sulivan.badasstank.network.Server;
import fr.sulivan.badasstank.states.GameRoom;
import fr.sulivan.badasstank.states.ID;
import fr.sulivan.badasstank.states.JoinConfiguration;
import fr.sulivan.badasstank.states.SandBox;
import fr.sulivan.badasstank.states.ServerConfiguration;
import fr.sulivan.badasstank.states.TankBuilding;
import fr.sulivan.badasstank.states.TitleScreen;
import fr.sulivan.badasstank.util.Network;

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
	private JoinConfiguration joinConfiguration;
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
		joinConfiguration = new JoinConfiguration();
		gameRoom = new GameRoom();
		
		addState(titleScreen);
		addState(sandbox);
		addState(tankBuilding);
		addState(serverConfiguration);
		addState(joinConfiguration);
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
			app.setAlwaysRender(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void host(String host) throws SlickException {
		try {
			InetSocketAddress address = Network.getSocketAddress(host, Configuration.DEFAULT_PORT);
			Server server = new Server(address.getAddress(), address.getPort());
			server.start();
			gameRoom.configureServer(server);
			game.enterState(ID.GAME_ROOM);
		} catch (IOException | NetworkException e) {
			//TODO envoyer message d'erreur au lieu de throw l'exception
			throw new SlickException(e.getMessage(), e);
		}
	}

	public void join(String host) throws SlickException {
		try {
			InetSocketAddress address = Network.getSocketAddress(host, Configuration.DEFAULT_PORT);
			Client client = new Client(address.getHostName(), address.getPort());
			client.listen();
			client.send("join");
			
			client.on("joinstatus", (e) -> {
				int status = e.getIntParameter("status");
				if(status == 0){
					int position = e.getIntParameter("position");
					String key = e.getParameter("key");
					
					gameRoom.setPosition(position);
					gameRoom.setRemoteKey(key);
					gameRoom.configureClient(client);
					
					game.enterState(ID.GAME_ROOM);
				}
				else{
					client.close();
				}
			});
			

		} catch (IOException e) {
			throw new SlickException(e.getMessage(), e);
		}
	}


}