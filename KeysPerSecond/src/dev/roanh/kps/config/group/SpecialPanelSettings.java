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

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.PanelType;
import dev.roanh.kps.panels.DataPanel;

/**
 * Settings for special panels that display some sort
 * of derived statistic.
 * @author Roan
 * @see PanelType
 */
public abstract class SpecialPanelSettings extends DataPanelSettings{
	/**
	 * The type of this panel, indicative of the metric displayed.
	 */
	private final PanelType type;
	
	/**
	 * Constructs new special panel settings.
	 * @param type The type of the special panel.
	 * @param defaultName The display name of the panel.
	 */
	protected SpecialPanelSettings(PanelType type, String defaultName){
		super("panels", defaultName);
		this.type = type;
	}

	/**
	 * Creates a new panel based on these settings.
	 * @return The newly created panel.
	 */
	public abstract DataPanel createPanel();
	
	/**
	 * Gets the type of the panel.
	 * @return The type of the panel.
	 */
	public PanelType getType(){
		return type;
	}
	
	@Override
	public void writeItems(IndentWriter out){
		out.println("type: " + type.getKey());
		super.writeItems(out);
	}
}
