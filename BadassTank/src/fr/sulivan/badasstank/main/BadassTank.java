package fr.sulivan.badasstank.main;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.states.Game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class BadassTank extends StateBasedGame 
{
	
	 /**//**//**//**//**//**//**//**//**//**//**/
	/**/private static AppGameContainer app;/**/
   /**//**//**//**//**//**//**//**//**//**//**/

	private Game game;
	private static boolean fullScreen = true;

	public BadassTank(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		game = new Game();
		addState(game);
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
			app.setFullscreen(false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}


}