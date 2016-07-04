package fr.sulivan.badasstank.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.loader.PiecesLoader;
import fr.sulivan.badasstank.mob.tank.Body;
import fr.sulivan.badasstank.mob.tank.Canon;
import fr.sulivan.badasstank.mob.tank.Carterpillar;
import fr.sulivan.badasstank.mob.tank.Tank;
import fr.sulivan.badasstank.util.gui.CarrousselListGUI;
import fr.sulivan.badasstank.util.gui.Renderable;

public class TankBuilding extends BasicGameState{

	private CarrousselListGUI<Body> bodiesList;
	private CarrousselListGUI<Carterpillar> carterpillarsList;
	private CarrousselListGUI<Canon> canonsList;
	
	private Tank tank;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		int x = 20;
		int y = 20;
		bodiesList = new CarrousselListGUI<Body>(
				new Image(Configuration.RESOURCES_FOLDER + "buttons/down.png"), 
				new Image(Configuration.RESOURCES_FOLDER + "buttons/up.png"), 
				new Color(50, 180, 20), 
				30, 30, 
				50);
		bodiesList.setElementRenderer(new Renderable<Body>() {
			@Override
			public void render(Graphics g, int x, int y, int index, Body element){
				int displayedX = x + 15;
				int displayedY = y + 15;
				
				element.drawCentered(displayedX, displayedY);
				
			}
		});
		bodiesList.setX(x);
		bodiesList.setY(y);
		bodiesList.setElements(PiecesLoader.loader().loadBodies());
		
		carterpillarsList = new CarrousselListGUI<Carterpillar>(
				new Image(Configuration.RESOURCES_FOLDER + "buttons/down.png"), 
				new Image(Configuration.RESOURCES_FOLDER + "buttons/up.png"), 
				new Color(50, 180, 20), 
				30, 30, 
				50);
		carterpillarsList.setElementRenderer(new Renderable<Carterpillar>() {
			@Override
			public void render(Graphics g, int x, int y, int index, Carterpillar element){
				int displayedX = x + 15;
				int displayedY = y + 15;
				
				element.render(displayedX, displayedY, false);
				
			}
		});
		x += 30;
		carterpillarsList.setX(x);
		carterpillarsList.setY(y);
		carterpillarsList.setElements(PiecesLoader.loader().loadCarterpillars());
		
		canonsList = new CarrousselListGUI<Canon>(
				new Image(Configuration.RESOURCES_FOLDER + "buttons/down.png"), 
				new Image(Configuration.RESOURCES_FOLDER + "buttons/up.png"), 
				new Color(50, 180, 20), 
				30, 30, 
				50);
		canonsList.setElementRenderer(new Renderable<Canon>() {
			@Override
			public void render(Graphics g, int x, int y, int index, Canon element){
				int displayedX = x + 15;
				int displayedY = y + 15;
				
				element.drawCenter(displayedX, displayedY);
			}
		});
		x += 30;
		canonsList.setX(x);
		canonsList.setY(y);
		canonsList.setElements(PiecesLoader.loader().loadCanons());
		
		buildTank();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		bodiesList.render(g);
		carterpillarsList.render(g);
		canonsList.render(g);
		
		tank.render(150, 150);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		bodiesList.update(container, delta);
		carterpillarsList.update(container, delta);
		canonsList.update(container, delta);
		
		buildTank();
	}
	
	private void buildTank(){
		Carterpillar carterpillar = (Carterpillar)carterpillarsList.getElement().clone();
		Canon canon = (Canon)canonsList.getElement().clone();
		Body body = (Body)bodiesList.getElement().clone();
		
		tank = new Tank(carterpillar, canon, new Color(0, 200, 200), body, "Sulivan");
		tank.setRotation(90);
	}

	@Override
	public int getID() {
		return ID.TANK_BUILDING;
	}

}
