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

import dev.roanh.kps.config.GraphType;
import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.panels.GraphPanel;

/**
 * Settings for graph panels.
 * @author Roan
 * @see GraphType
 */
public abstract class GraphPanelSettings extends PanelSettings{
	/**
	 * The type of this graph.
	 */
	private final GraphType type;

	/**
	 * Constructs new graph panel settings.
	 * @param type The type of the graph panel.
	 * @param x The x position of the panel.
	 * @param y The y position of the panel.
	 * @param width The width of the panel.
	 * @param height The height of the panel.
	 * @param defaultName The display name of the panel.
	 */
	protected GraphPanelSettings(GraphType type, int x, int y, int width, int height, String defaultName){
		super("graphs", x, y, width, height, defaultName);
		this.type = type;
	}
	
	/**
	 * Creates a new graph panel with this configuration.
	 * @return The newly created graph panel.
	 */
	public abstract GraphPanel createGraph();

	@Override
	public void writeItems(IndentWriter out){
		out.println("type: " + type.getKey());
		super.writeItems(out);
	}
}
