package dev.roanh.kps.config.group;

import java.awt.Color;
import java.util.Map;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.ColorSetting;

public class ThemeSettings extends SettingGroup{
	private final BooleanSetting customColors = new BooleanSetting("customColors", false);
	private final ColorSetting foreground = new ColorSetting("foreground", Color.CYAN);
	private final ColorSetting background = new ColorSetting("background", Color.WHITE);

	public ThemeSettings(){
		super("theme");
	}
	
	/**
	 * Enables or disables custom colours.
	 * @param enabled True to enable custom colours.
	 */
	public void setCustomColorsEnabled(boolean enabled){
		customColors.update(enabled);
	}
	
	/**
	 * Checks if custom colours are configured for the application.
	 * @return True if custom colours are configured.
	 */
	public boolean hasCustomColors(){
		return customColors.getValue();
	}
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public boolean parse(Map<String, String> data){
		return findAndParse(data, customColors, foreground, background);
	}

	@Override
	public void writeItems(IndentWriter out){
		customColors.write(out);
		foreground.write(out);
		background.write(out);
	}
	
	//TODO legacy proxies
}
