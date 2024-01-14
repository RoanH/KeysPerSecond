package dev.roanh.kps.config.group;

import java.util.Map;

import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.setting.RenderingModeSetting;

/**
 * Settings for a named value display panel.
 * @author Roan
 */
public abstract class DataPanelSettings extends PanelSettings{
	/**
	 * The rendering mode used the panel.
	 */
	protected final RenderingModeSetting mode = new RenderingModeSetting("mode", RenderingMode.VERTICAL);

	/**
	 * Constructs new panel settings.
	 * @param key The settings group key.
	 * @param x The x position of the panel.
	 * @param y The y position of the panel.
	 * @param width The width of the panel.
	 * @param height The height of the panel.
	 * @param defaultName The display name of the panel.
	 */
	protected DataPanelSettings(String key, int x, int y, int width, int height, String defaultName){
		super(key, x, y, width, height, defaultName);
	}
	
	/**
	 * Constructs a new panel with default dimensions.
	 * @param key The settings group key.
	 * @param defaultName The display name of the panel.
	 */
	protected DataPanelSettings(String key, String defaultName){
		super(key, defaultName);
	}
	
	/**
	 * Gets the rendering mode for this panel.
	 * @return The rendering mode for this panel.
	 * @see RenderingMode
	 */
	public RenderingMode getRenderingMode(){
		return mode.getValue();
	}
	
	/**
	 * Sets the rendering mode for the panel.
	 * @param mode The new rendering mode.
	 */
	public void setRenderingMode(RenderingMode mode){
		this.mode.update(mode);
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, mode);
	}
	
	@Override
	public void writeItems(IndentWriter out){
		super.writeItems(out);
		mode.write(out);
	}
}
