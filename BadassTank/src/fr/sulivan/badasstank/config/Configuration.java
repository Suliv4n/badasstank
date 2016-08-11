package fr.sulivan.badasstank.config;

import org.newdawn.slick.Color;

public class Configuration {
	public static final int SCREEN_WIDTH = 640;
	public static final int SCREEN_HEIGHT = 480;
	public static final int FPS = 60;
	public static final Color HEALTH_BAR_COLOR = new Color(180,240,255);
	public static final Color COOLDOWN_BAR_COLOR = new Color(255,240,180);
	public static final Color COOLDOWN_FULL_BAR_COLOR = new Color(180,255,180);	
	public static final Color BORDER_COLOR = new Color(150,150,150);
	public static final Color BACK_BUTTON_COLOR = new Color(100,0,0);
	public static final String RESOURCES_FOLDER = "resources/";
	public static final String PIECES_FILE = RESOURCES_FOLDER + "pieces.xml";
	public static final int DEFAULT_PORT = 1337;
	public static final int MAXIMUM_TIME_BEFORE_RESPAWN = 10_000;
	public static final int MINIMUM_TIME_BEFORE_RESPAWN = 10_000;
	public static final int TIME_MAXIMUM_IN_GAME_ROOM = 5 * 60 * 1000;
	
}
