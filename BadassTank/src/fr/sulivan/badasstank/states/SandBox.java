package fr.sulivan.badasstank.states;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.plaf.SliderUI;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import fr.sulivan.badasstank.config.Configuration;
import fr.sulivan.badasstank.hud.HUD;
import fr.sulivan.badasstank.loader.PiecesLoader;
import fr.sulivan.badasstank.main.BadassTank;
import fr.sulivan.badasstank.map.Map;
import fr.sulivan.badasstank.mob.displayer.Displayer;
import fr.sulivan.badasstank.mob.player.Player;
import fr.sulivan.badasstank.mob.tank.Body;
import fr.sulivan.badasstank.mob.tank.Canon;
import fr.sulivan.badasstank.mob.tank.Carterpillar;
import fr.sulivan.badasstank.util.gui.CarouselListGUI;
import fr.sulivan.badasstank.util.gui.ColorPickerGUI;
import fr.sulivan.badasstank.util.gui.ColoredButtonGUI;
import fr.sulivan.badasstank.util.gui.ElementRenderer;
import fr.sulivan.badasstank.util.gui.InputGUI;
import fr.sulivan.badasstank.util.gui.PopupGUI;
import fr.sulivan.badasstank.util.gui.RangeGUI;

public class SandBox extends BasicGameState{

	
	private Player player1;
	private Player player2;
	
	private ArrayList<Displayer> displayers;

	private Map map;
	
	private int mapX;
	private int mapY;
	
	private int cursorX;
	private int cursorY;
	
	private InputGUI input;
	
	private HUD hud;
	
	//private ColoredButtonGUI button;
	private CarouselListGUI<Body> bodiesList;
	private RangeGUI slider;
	
	private ColorPickerGUI popup;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		input = new InputGUI(container, 60, 20);
		input.setLocation(400, 210);
		input.setBorder(2, Color.blue);
		
		slider = new RangeGUI(Color.white, 200, 0, 255);
		
		slider.setX(200);
		slider.setY(200);
		
		input.bind(slider);
		
		//popup = new PopupGUI("Test", 300, 100, );
		popup = new ColorPickerGUI(container, "TEST color picker", new Image(Configuration.RESOURCES_FOLDER+"buttons/close.png", new Color(255,0,255)),
				new Image(Configuration.RESOURCES_FOLDER+"textures/yandb.png"),
				new Image(Configuration.RESOURCES_FOLDER+"textures/yandb_mouseover.png") );
		
		popup.setLocation(10, 10);
		popup.setBorder(2, new Color(150,150,150));
		popup.setBackgroundColor( new Color(30,30,30));
		popup.open();
		
		bodiesList = new CarouselListGUI<Body>(
				new Image(Configuration.RESOURCES_FOLDER+"buttons/down.png"), new Image(Configuration.RESOURCES_FOLDER+"buttons/up.png"), 
				new Color(0,100,200), 
				30, 30, 50);
		
		bodiesList.setElements(PiecesLoader.loader().loadBodies());
		
		bodiesList.setX(200);
		bodiesList.setY(10);
		
		bodiesList.setElementRenderer(new ElementRenderer<Body>() {
			@Override
			public void render(Graphics g, int x, int y, int index, Body element){
				int displayedX = x + 15;
				int displayedY = y + 15;
				
				element.drawCentered(displayedX, displayedY);
				
			}
		});
		
		/*
		SpriteSheet sprites1 = new SpriteSheet("resources/spritesheets/carterpillars.png", 7 ,23 ,new Color(255,0,255));
		Animation animation1 = new Animation(sprites1, 0, 0, 2, 0,true, 100, true);
		Carterpillar carterpillar1 = new Carterpillar(animation1, 1.5,3);
		Body body1 = new Body(new Image("resources/spritesheets/bodies.png").getSubImage(0, 0, 16, 18));
		Canon canon1 = new Canon(new Image("resources/spritesheets/canons.png", new Color(255,0,255)).getSubImage(0, 0, 11, 17), new Image("resources/spritesheets/bullet.png", new Color(255,0,255)).getSubImage(0, 0, 8, 9), null, 3f, 100, 500);
		*/
		Carterpillar carterpillar1 = PiecesLoader.loader().loadCarterpillars().get(0);
		Canon canon1 = PiecesLoader.loader().loadCanons().get(0);
		Body body1 = PiecesLoader.loader().loadBodies().get(0);
		player1 = new Player(carterpillar1, canon1, new Color(20,150,20), body1, "Sulivan");
		
		
		/*
		SpriteSheet sprites2 = new SpriteSheet("resources/spritesheets/carterpillars.png", 7 ,23 ,new Color(255,0,255));
		Animation animation2 = new Animation(sprites2, 0, 0, 2, 0,true, 100, true);
		Carterpillar carterpillar2 = new Carterpillar(animation2, 1.5,3);
		Body body2 = new Body(new Image("resources/spritesheets/bodies.png").getSubImage(0, 0, 16, 18));
		Canon canon2 = new Canon(new Image("resources/spritesheets/canons.png", new Color(255,0,255)).getSubImage(0, 0, 11, 17), new Image("resources/spritesheets/bullet.png", new Color(255,0,255)).getSubImage(0, 0, 8, 9), null, 3f, 100, 500);
		*/
		Carterpillar carterpillar2 = PiecesLoader.loader().loadCarterpillars().get(0);
		Canon canon2 = PiecesLoader.loader().loadCanons().get(0);
		Body body2 = PiecesLoader.loader().loadBodies().get(0);
		player1 = new Player(carterpillar1, canon1, new Color(20,150,20), body1, "Sulivan");
		player2 = new Player(carterpillar2, canon2, new Color(20,20,150), body2, "Joueur 2");
		
		player2.setCoordinates(100, 0, true);
		
		
		this.map = new Map(new TiledMap("resources/map/test.tmx"));
		
		map.registerPlayer(player1);
		map.registerPlayer(player2);
		
		ParticleSystem particles = new ParticleSystem("resources/particles/test.png", 1500, new Color(255,0,255));
		
		File xmlFile = new File("resources/particles/test.xml");
		ConfigurableEmitter ce;
		try {
			ce = ParticleIO.loadEmitter(xmlFile);
			particles.addEmitter(ce);
		} catch (IOException e) {
			e.printStackTrace();
		}
		displayers = new ArrayList<Displayer>();
		
		hud = new HUD(this);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		

		map.drawLayer(0);
		player1.render(true, g, map);
		player2.render(false, g, map);
		
		
		
		g.setColor(Color.white);
		g.drawString("Joueur : " + player1.getX() + " ; " + player1.getY() + "  :  " + player1.getRotation() + "° ", 0, 0);
		g.drawString("Curseur : " + cursorX + " ; " + cursorY, 0, 20);
		for(Displayer d : displayers){
			d.render( d.getX() + mapX,  d.getY() + mapY );
			d.getHitbox().draw(new Color(0,0.7f,0, 0.5f), g);;
		}
		
		map.getHitbox().draw(new Color(0.5f, 0.2f, 0.2f, 0.5f), g);
		player1.getHitbox().draw(new Color(0.2f, 0.2f, 0.5f, 0.5f), g);
		player2.getHitbox().draw(new Color(0.2f, 0.2f, 0.5f, 0.5f), g);
		
		hud.render(g);
		
		bodiesList.render(g);
		
		slider.render(g);
		
		input.render(g);
		
		popup.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input in = container.getInput();
		popup.update(container);
		bodiesList.update(container, delta);
		
		mapX = Configuration.SCREEN_WIDTH / 2 - player1.getX();
		mapY = Configuration.SCREEN_HEIGHT / 2 - player1.getY();
		
		map.setDisplayedCoordinate(mapX, mapY);
		
		cursorX = - mapX  + in.getAbsoluteMouseX();
		cursorY = - mapY + in.getAbsoluteMouseY();
		
		if(in.isKeyDown(Input.KEY_Z)){
			player1.setMoving(true, false);
		}
		else if(in.isKeyDown(Input.KEY_S)){
			player1.setMoving(true, true);
		}
		else{
			player1.setMoving(false, false);
		}
		
		if(in.isKeyDown(Input.KEY_Q)){
			//if(!player.getHitbox().copyRotation(player.getRotation() + player.getSpeedRotation()).intersects(map.getHitbox())){
				player1.setRotation(player1.getRotation() + player1.getSpeedRotation());
			//}
		}
		else if(in.isKeyDown(Input.KEY_D)){
			//if(!player.getHitbox().copyRotation(player.getRotation() - player.getSpeedRotation()).intersects(map.getHitbox())){
				player1.setRotation(player1.getRotation() - player1.getSpeedRotation());
			//}
		}
		if(in.isKeyDown(Input.KEY_F1)){
			BadassTank.toggleFullScreen();
		}

		if(in.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			displayers.add(player1.fire(player1.getX(), player1.getY(), cursorX, cursorY));
		}
		
		player1.getCanon().setRotation(Configuration.SCREEN_WIDTH / 2, Configuration.SCREEN_HEIGHT / 2 , in.getAbsoluteMouseX(), in.getAbsoluteMouseY());
		player1.update(map, true);
		
		ArrayList<Displayer> disposed = new ArrayList<Displayer>();
		for(Displayer d : displayers){
			d.update(delta, this);
			if(d.isDisposed()){
				disposed.add(d);
			}
		}
		
		for(Displayer d : disposed){
			displayers.remove(d);
		}
		
		
		hud.update();
		
		slider.update(container);
		
	}

	@Override
	public int getID() {
		return ID.GAME;
	}

	public Player getPlayer() {
		return player1;
	}
	
	public Map getMap(){
		return map;
	}

}
