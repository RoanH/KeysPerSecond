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

import dev.roanh.kps.Key;
import dev.roanh.kps.config.group.KeyPanelSettings;

/**
 * Panel to display the number
 * of times a certain key has 
 * been pressed
 * @author Roan
 */
public final class KeyPanel extends DataPanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;
	/**
	 * The key object associated with this panel. This key object
	 * keeps track of the number of times the assigned key has been hit.
	 */
	private Key key;

	/**
	 * Constructs a new KeyPanel with the given key object.
	 * @param key The key object to associate this panel with.
	 * @param settings The key settings object for this panel.
	 * @see Key
	 * @see KeyPanelSettings
	 */
	public KeyPanel(Key key, KeyPanelSettings settings){
		super(settings);
		this.key = key;
		key.setPanel(this);
	}

	@Override
	protected boolean isActive(){
		return key.isDown();
	}

	@Override
	protected String getValue(){
		return String.valueOf(key.getCount());
	}
}