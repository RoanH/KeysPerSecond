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

import java.util.Map;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.config.setting.BooleanSetting;
import dev.roanh.kps.config.setting.IntSetting;
import dev.roanh.kps.panels.LastPanel;
import dev.roanh.kps.ui.editor.Editor;
import dev.roanh.kps.ui.editor.LastPanelEditor;

/**
 * Settings for a panel showing the time since the last input.
 * @author Roan
 * @see LastPanel
 */
public class LastPanelSettings extends SpecialPanelSettings{
	/**
	 * Maximum number of time units to show at the same time.
	 * E.g., two units could mean showing '5m 26s'.
	 */
	private final IntSetting units = new IntSetting("units", 1, 4, 1);
	/**
	 * Whether milliseconds are considered as a valid unit to show or not.
	 * If false milliseconds are never shown.
	 */
	private final BooleanSetting millis = new BooleanSetting("showMillis", false);

	/**
	 * Constructs new last panel settings.
	 */
	public LastPanelSettings(){
		super(PanelType.LAST, "LST");
	}
	
	/**
	 * True if millisecond may be shown as a value for this panel.
	 * @return True if milliseconds are considered a valid unit.
	 */
	public boolean showMillis(){
		return millis.getValue();
	}
	
	/**
	 * Gets the maximum number of time units to string
	 * together to form the time string.
	 * @return The maximum time unit count.
	 */
	public int getUnitCount(){
		return units.getValue();
	}
	
	/**
	 * Sets the maximum number of time units allowed to be used.
	 * @param units The maximum number of time units.
	 */
	public void setUnitCount(int units){
		this.units.update(units);
	}
	
	/**
	 * Sets whether millisecond resolution is allowed as a valid panel value.
	 * @param shown True if milliseconds are allowed to be shown.
	 */
	public void setShowMillis(boolean shown){
		millis.update(shown);
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return super.parse(data) | findAndParse(data, units, millis);
	}
	
	@Override
	public void writeItems(IndentWriter out){
		super.writeItems(out);
		units.write(out);
		millis.write(out);
	}

	@Override
	public void showEditor(boolean live){
		Editor.showEditor(new LastPanelEditor(this, live));
	}

	@Override
	public LastPanel createPanel(){
		return new LastPanel(this);
	}
}
