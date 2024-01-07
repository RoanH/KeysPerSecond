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
import dev.roanh.kps.KeyInformation;
import dev.roanh.kps.RenderingMode;

/**
 * Panel to display the number
 * of times a certain key has 
 * been pressed
 * @author Roan
 */
public final class KeyPanel extends BasePanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;
	/**
	 * The key object associated with this panel<br>
	 * This key object keep track of the amount of this
	 * the assigned key has been hit
	 */
	private Key key;
	/**
	 * The key information object
	 * for this key
	 */
	protected KeyInformation info;

	/**
	 * Constructs a new KeyPanel
	 * with the given key object
	 * @param key The key object to
	 *        associate this panel with
	 * @param i The key information object
	 *        for the key for this panel
	 * @see Key
	 * @see #key
	 */
	public KeyPanel(Key key, KeyInformation i){
		this.key = key;
		info = i;
	}

	@Override
	protected boolean isActive(){
		return key.down;
	}

	@Override
	public String getTitle(){
		return key.name;
	}

	@Override
	protected String getValue(){
		return String.valueOf(key.count);
	}

	@Override
	public int getLayoutX(){
		return info.getX();
	}

	@Override
	public int getLayoutY(){
		return info.getY();
	}

	@Override
	public int getLayoutWidth(){
		return info.getWidth();
	}

	@Override
	public int getLayoutHeight(){
		return info.getHeight();
	}

	@Override
	public RenderingMode getRenderingMode(){
		return info.getRenderingMode();
	}
}