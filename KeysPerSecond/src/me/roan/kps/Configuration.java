package me.roan.kps;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
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
	/**
	 * Number of points the graph consists of
	 */
	protected int backlog = 30;
	/**
	 * Draw the horizontal average line
	 */
	protected boolean graphAvg = true;
	
	//update rate
	/**
	 * The amount of milliseconds a single time frame takes
	 */
	protected int updateRate = 1000;
	
	//colors
	/**
	 * Foreground color
	 */
	protected Color foreground = Color.CYAN;
	/**
	 * Background color
	 */
	protected Color background = Color.BLACK;
	/**
	 * Foreground opacity in case transparency is enabled
	 */
	protected float opacityfg = 1.0F;
	/**
	 * Background opacity in case transparency is enabled
	 */
	protected float opacitybg = 1.0F;
	
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
	
	protected final boolean loadConfig(File saveloc){
		if(saveloc.getAbsolutePath().endsWith(".kpsconf")){
			return loadLegacyFormat(saveloc);
		}else{
			//TODO new format
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private final boolean loadLegacyFormat(File saveloc){
		try {
			ObjectInputStream objin = new ObjectInputStream(new FileInputStream(saveloc));
			keyinfo = (List<KeyInformation>) objin.readObject();
			showMax = objin.readBoolean();
			showCur = objin.readBoolean();
			showAvg = objin.readBoolean();
			showGraph = objin.readBoolean();
			graphAvg = objin.readBoolean();
			backlog = objin.readInt();
			updateRate = objin.readInt();
			double version = 3.0D;
			if(objin.available() > 0){
				customColors = objin.readBoolean();
				background = (Color)objin.readObject();
				foreground = (Color)objin.readObject();
				if(objin.available() > 0){
					trackAll = objin.readBoolean();
					showKeys = objin.readBoolean();
					if(objin.available() > 0){
						version = objin.readDouble();
					}
				}
			}
			if(version >= 3.9){
				precision = objin.readInt();
			}
			if(version >= 3.10){
				opacitybg = objin.readFloat();
				opacityfg = objin.readFloat();
			}
			if(version >= 4.0D){
				size = objin.readDouble();
			}
			if(version >= 4.2D){
				overlay = objin.readBoolean();
			}
			objin.close();
			for(KeyInformation info : keyinfo){
				if(version < 3.7D){
					info.visible = true;
				}
				if(info.index > KeyInformation.autoIndex){
					KeyInformation.autoIndex = info.index + 1;
				}
			}
			return true;
		} catch (Exception e1) {
			return false;
		}
	}
	
	protected static final void saveConfig(){
		
	}
}
