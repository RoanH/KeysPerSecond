package me.roan.kps;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.roan.kps.Main.KeyInformation;

public class Configuration {

	//general
	protected boolean showMax = true;
	protected boolean showAvg = true;
	protected boolean showCur = true;
	protected boolean showKeys = true;
	protected boolean showGraph = false;
	/**
	 * Whether of not the frame forces itself to be the top window
	 */
	protected boolean overlay = false;
	protected boolean customColors = false;
	/**
	 * Whether or not to track all key presses
	 */
	protected boolean trackAll = false;
	
	//keys
	/**
	 * Key configuration data, can be serialised
	 */
	protected List<KeyInformation> keyinfo = new ArrayList<KeyInformation>();
	
	//graph
	protected int backlog = 30;
	protected boolean graphAvg = true;
	
	//update rate
	/**
	 * The amount of milliseconds a single time frame takes
	 */
	protected int updateRate = 1000;
	
	//colors
	protected Color fg = Color.CYAN;
	protected Color bg = Color.BLACK;
	protected float opacityFg = 1.0F;
	protected float opacityBg = 1.0F;
	
	//precision
	/**
	 * How many digits to display for avg
	 */
	protected int precision = 0;
	
	//size
	/**
	 * The factor to multiply the frame size with
	 */
	protected double size = 1.0D;
	
	protected static final void loadConfig(){
		
	}
	
	protected static final void saveConfig(){
		
	}
}
