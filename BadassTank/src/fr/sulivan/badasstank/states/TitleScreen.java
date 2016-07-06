package fr.sulivan.badasstank.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.util.gui.TexturedButtonGUI;

public class TitleScreen extends BasicGameState{
	
	private TexturedButtonGUI buttonJoin;
	private TexturedButtonGUI buttonHost;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		buttonJoin = new TexturedButtonGUI(new Image(Configuration.RESOURCES_FOLDER+"textures/yandb.png"), 200, 30, "Join");
		buttonJoin.setTextureMouseOver(new Image(Configuration.RESOURCES_FOLDER+"textures/yandb_mouseover.png"));
		buttonJoin.setBorder(2, Color.white);
		buttonJoin.setX(Configuration.SCREEN_WIDTH / 2 - buttonJoin.getWidth() / 2);
		buttonJoin.setY(100);
		
		buttonHost = new TexturedButtonGUI(new Image(Configuration.RESOURCES_FOLDER+"textures/yandb.png"), 200, 30, "Héberger");
		buttonHost.setTextureMouseOver(new Image(Configuration.RESOURCES_FOLDER+"textures/yandb_mouseover.png"));
		buttonHost.setBorder(2, Color.white);
		buttonHost.setX(Configuration.SCREEN_WIDTH / 2 - buttonHost.getWidth() / 2);
		buttonHost.setY(150);
		
		buttonHost.setOnClick(() -> {
			game.enterState(ID.SERVER_CONFIGURATION);
		});

	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game){
		container.getInput().clearMousePressedRecord();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		buttonJoin.render(g);
		buttonHost.render(g);
		
	}
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		buttonJoin.update(container);
		buttonHost.update(container);
	}
	@Override
	public int getID() {
		return ID.TITLE_SCREEN;
	}
}
