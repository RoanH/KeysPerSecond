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
package dev.roanh.kps.panels;

import dev.roanh.kps.config.group.GraphPanelSettings;

/**
 * Base class for graph implementations.
 * @author Roan
 */
public abstract class GraphPanel extends BasePanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 393397627762727201L;

	/**
	 * Constructs a new graph panel.
	 * @param config The graph settings.
	 */
	protected GraphPanel(GraphPanelSettings config){
		super(config);
	}

	/**
	 * Called when the graph should update its data. This
	 * method is called from the main update loop thread
	 * and it is thus guaranteed that derived statistics
	 * do not update during this method.
	 */
	public abstract void update();
	
	/**
	 * Resets any data collected by this graph to defaults.
	 */
	public abstract void reset();
}
