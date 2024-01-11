package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.ThemeColor;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.ColorSetting;

public class ThemeSettings extends SettingGroup{
	/**
	 * Whether or not to use custom colors.
	 */
	private final BooleanSetting customColors = new BooleanSetting("customColors", false);
	/**
	 * Default foreground color and opacity.
	 */
	private final ColorSetting foreground = new ColorSetting("foreground", new ThemeColor(0, 255, 255, 1.0F));
	/**
	 * Default background color and opacity.
	 */
	private final ColorSetting background = new ColorSetting("background", new ThemeColor(0, 0, 0, 1.0F));

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
	
	public ThemeColor getForeground(){
		return customColors.getValue() ? foreground.getValue() : foreground.getDefaultValue();
	}
	
	public ThemeColor getBackground(){
		return customColors.getValue() ? background.getValue() : background.getDefaultValue();
	}
	
	public void setForeground(ThemeColor foreground){
		this.foreground.update(foreground);
	}
	
	public void setBackground(ThemeColor background){
		this.background.update(background);
	}
	
	/**
	 * Tests if transparent components or frames are required to properly
	 * rendering the colours used in this theme.
	 * @return True if transparency support is required.
	 */
	public boolean isTransparencyRequired(){
		return getForeground().getAlpha() != 1.0F || getBackground().getAlpha() != 1.0F;
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
	
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(new LegacyColorProxy("foregroundColor", foreground));
		proxyList.add(new LegacyColorProxy("foregroundOpacity", foreground));
		proxyList.add(new LegacyColorProxy("backgroundColor", background));
		proxyList.add(new LegacyColorProxy("backgroundOpacity", background));
	}
	
	private static final class LegacyColorProxy extends Setting<ThemeColor>{
		private ColorSetting setting;

		protected LegacyColorProxy(String key, ColorSetting setting){
			super(key, setting.getDefaultValue());
			this.setting = setting;
		}

		@Override
		public boolean parse(String data){
			try{
				float alpha = Float.parseFloat(data);
				if(alpha < 0.0F || alpha > 1.0F){
					setting.reset();
					return true;
				}else{
					setting.update(new ThemeColor(setting.getValue().getRGB(), alpha));
					return false;
				}
			}catch(NumberFormatException e){
				return setting.parse(data);
			}
		}

		@Override
		public void write(IndentWriter out){
			throw new IllegalStateException("Legacy proxy settings should never be written.");
		}
	}
}
