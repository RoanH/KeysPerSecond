/*
 * KeysPerSecond: An open source input statistics displayer.
 * Copyright (C) 2017  Roan Hofland (roan@roanh.dev).  All rights reserved.
 * GitHub Repository: https://github.com/RoanH/KeysPerSecond
 *
 * KeysPerSecond is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeysPerSecond is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.ThemeColor;
import dev.roanh.kps.config.legacy.LegacyColorProxy;
import dev.roanh.kps.config.legacy.LegacyProxyStore;
import dev.roanh.kps.config.legacy.ProxySetting;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.ColorSetting;

/**
 * General theme colour settings.
 * @author Roan
 */
public class ThemeSettings extends SettingGroup implements LegacyProxyStore{
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

	/**
	 * Creates new theme settings.
	 */
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
	
	/**
	 * Gets the effective theme foreground color to use.
	 * @return The foreground color.
	 */
	public ThemeColor getForeground(){
		return customColors.getValue() ? foreground.getValue() : foreground.getDefaultValue();
	}
	
	/**
	 * Gets the effective theme background color to use.
	 * @return The background color.
	 */
	public ThemeColor getBackground(){
		return customColors.getValue() ? background.getValue() : background.getDefaultValue();
	}
	
	/**
	 * Gets the current custom foreground color ignoring if
	 * custom colors are enabled or not.
	 * @return The custom foreground color.
	 * @see #getForeground()
	 */
	public ThemeColor getCustomForeground(){
		return foreground.getValue();
	}
	
	/**
	 * Gets the current custom background color ignoring if
	 * custom colors are enabled or not.
	 * @return The custom background color.
	 * @see #getBackground()
	 */
	public ThemeColor getCustomBackground(){
		return background.getValue();
	}
	
	/**
	 * Sets the theme foreground color. Note that
	 * this only takes effect if custom colors are enabled.
	 * @param foreground The new foreground color.
	 */
	public void setForeground(ThemeColor foreground){
		this.foreground.update(foreground);
	}
	
	/**
	 * Sets the theme background color. Note that
	 * this only takes effect if custom colors are enabled.
	 * @param background The new background color.
	 */
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
	
	@Override
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(ProxySetting.of("customColors", customColors));
		proxyList.add(new LegacyColorProxy("foregroundColor", foreground));
		proxyList.add(new LegacyColorProxy("foregroundOpacity", foreground));
		proxyList.add(new LegacyColorProxy("backgroundColor", background));
		proxyList.add(new LegacyColorProxy("backgroundOpacity", background));
	}
}
