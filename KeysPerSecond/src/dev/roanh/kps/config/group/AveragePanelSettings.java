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
import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.legacy.LegacyProxyStore;
import dev.roanh.kps.config.legacy.ProxySetting;
import dev.roanh.kps.config.setting.PrecisionSetting;
import dev.roanh.kps.panels.AvgPanel;
import dev.roanh.kps.ui.editor.AvgPanelEditor;
import dev.roanh.kps.ui.editor.Editor;

/**
 * Configuration for the average panel.
 * @author Roan
 * @see AvgPanel
 */
public class AveragePanelSettings extends SpecialPanelSettings implements LegacyProxyStore{
	/**
	 * Setting for the display precision of the panel (decimal digits).
	 */
	private final PrecisionSetting precision = new PrecisionSetting("precision", 0, 3, 0);
	
	/**
	 * Constructs new average panel settings.
	 */
	public AveragePanelSettings(){
		super(PanelType.AVG, "AVG");
	}
	
	/**
	 * Sets the precision for the panel.
	 * @param value The number of decimal digits to display.
	 */
	public void setPrecision(int value){
		precision.update(value);
	}
	
	/**
	 * Gets the precision for the panel.
	 * @return The number of decimal digits to display.
	 */
	public int getPrecision(){
		return precision.getValue();
	}
	
	/**
	 * Formats the given number according to the precision
	 * configured for this average panel.
	 * @param value The value to format.
	 * @return The formatted value.
	 */
	public String formatAvg(double value){
		return precision.format(value);
	}
	
	@Override
	public AvgPanel createPanel(){
		return new AvgPanel(this);
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, precision);
	}
	
	@Override
	public void writeItems(IndentWriter out){
		super.writeItems(out);
		precision.write(out);
	}
	
	@Override
	public void showEditor(boolean live){
		Editor.showEditor(new AvgPanelEditor(this, live));
	}
	
	@Override
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(ProxySetting.of("avgX", x));
		proxyList.add(ProxySetting.of("avgY", y));
		proxyList.add(ProxySetting.of("avgWidth", width));
		proxyList.add(ProxySetting.of("avgHeight", height));
		proxyList.add(ProxySetting.of("avgMode", mode));
		proxyList.add(ProxySetting.of("precision", precision));
	}
}
