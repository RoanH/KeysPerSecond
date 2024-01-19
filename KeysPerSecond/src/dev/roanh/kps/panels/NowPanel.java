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

import dev.roanh.kps.Main;
import dev.roanh.kps.config.group.CurrentPanelSettings;

/**
 * Panel used to display the current keys pressed per second.
 * Note that since the actual 'current' time frame is still on
 * going this actually displays the keys per second from the
 * previous second.
 * @author Roan
 */
public final class NowPanel extends DataPanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;

	/**
	 * Constructs a new current panel
	 * @param settings The panel configuration.
	 */
	public NowPanel(CurrentPanelSettings settings){
		super(settings);
	}

	@Override
	protected String getValue(){
		return String.valueOf(Main.prev);
	}
}