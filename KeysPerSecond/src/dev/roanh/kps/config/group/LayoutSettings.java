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
import dev.roanh.kps.config.legacy.LegacyProxyStore;
import dev.roanh.kps.config.legacy.ProxySetting;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.panels.BasePanel;

/**
 * Settings group with all general layout related settings.
 * @author Roan
 */
public class LayoutSettings extends SettingGroup implements LegacyProxyStore{
	/**
	 * The offset from the border of a panel to the actual panel content.
	 */
	private final IntSetting borderOffset = new IntSetting("borderOffset", 0, Integer.MAX_VALUE, 2);
	/**
	 * The pixel size of one grid cell in the program.
	 */
	private final IntSetting cellSize = new IntSetting("cellSize", BasePanel.imageSize, Integer.MAX_VALUE, 22);

	/**
	 * Constructs new layout settings.
	 */
	public LayoutSettings(){
		super("layout");
	}
	
	/**
	 * Gets the size in pixels of all the layout grid cells.
	 * @return The grid cell size in pixels.
	 */
	public int getCellSize(){
		return cellSize.getValue();
	}
	
	/**
	 * Gets the offset from the border of a panel to the actual content.
	 * @return The distance in pixels from panel border to panel content.
	 */
	public int getBorderOffset(){
		return borderOffset.getValue();
	}

	/**
	 * Sets the offset from the panel border to the panel content.
	 * The value will be adjusted if invalid.
	 * @param offset The offset in pixels.
	 * @see #validate()
	 */
	public void setBorderOffset(int offset){
		borderOffset.update(offset);
		validate();
	}
	
	/**
	 * Sets the size of the layout cells. The border offset may
	 * be adjusted if required for the settings to be valid.
	 * @param size The cell size in pixels.
	 * @see #validate()
	 */
	public void setCellSize(int size){
		cellSize.update(size);
		validate();
	}
	
	/**
	 * Validates that the configured layout options are valid and adjusts
	 * them if necessary. This check ensures that the border offset always
	 * leaves an interior for a cell.
	 * @return True if values had to be adjusted to obtain valid settings,
	 *         false if no values were adjusted.
	 */
	private boolean validate(){
		if(borderOffset.getValue() > cellSize.getValue() - BasePanel.imageSize){
			borderOffset.update(cellSize.getValue() - BasePanel.imageSize);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean parse(Map<String, String> data){
		boolean defaultUsed = findAndParse(data, borderOffset, cellSize);
		return validate() || defaultUsed;
	}

	@Override
	public void writeItems(IndentWriter out){
		cellSize.write(out);
		borderOffset.write(out);
	}
	
	@Override
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(ProxySetting.of("borderOffset", borderOffset));
		proxyList.add(ProxySetting.of("cellSize", cellSize));
	}
}
