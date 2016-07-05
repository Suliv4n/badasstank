package fr.sulivan.badasstank.main;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.states.SandBox;
import fr.sulivan.badasstank.states.TankBuilding;
import fr.sulivan.badasstank.states.TitleScreen;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class BadassTank extends StateBasedGame 
{
	
	 /**//**//**//**//**//**//**//**//**//**//**/
	/**/private static AppGameContainer app;/**/
   /**//**//**//**//**//**//**//**//**//**//**/

	private SandBox game;
	private TankBuilding tankBuilding;
	private TitleScreen titleScreen;
	
	private static boolean fullScreen = false;

	public BadassTank(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		game = new SandBox();
		tankBuilding = new TankBuilding();
		titleScreen = new TitleScreen();
		
		titleScreen.init(container, this);
		addState(titleScreen);
		addState(game);
		addState(tankBuilding);
		
	}
	
	public static void toggleFullScreen() throws SlickException{
		fullScreen = !fullScreen;
		app.setFullscreen(fullScreen);
	}
	
	public static void main(String[] args){
		try {
			app = new AppGameContainer(new BadassTank("Super Badass Tank"));
			app.setDisplayMode(Configuration.SCREEN_WIDTH, Configuration.SCREEN_HEIGHT, false);
			app.setTargetFrameRate(Configuration.FPS);
			app.setShowFPS(false);
			app.setFullscreen(fullScreen);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}


}