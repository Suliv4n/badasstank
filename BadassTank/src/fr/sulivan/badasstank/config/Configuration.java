package fr.sulivan.badasstank.config;

import org.newdawn.slick.Color;

public class Configuration {
	public static final int SCREEN_WIDTH = 640;
	public static final int SCREEN_HEIGHT = 480;
	public static final int FPS = 60;
	public static final Color HEALTH_BAR_COLOR = new Color(180,240,255);
	public static final Color BORDER_COLOR = new Color(150,150,150);
	public static final String RESOURCES_FOLDER = "resources/";
	public static final String PIECES_FILE = RESOURCES_FOLDER + "pieces.xml";
	public static final int DEFAULT_PORT = 1337;
	public static final int MAXIMUM_TIME_BEFORE_RESPAWN = 10_000;
	public static final int MINIMUM_TIME_BEFORE_RESPAWN = 10_000;
}
