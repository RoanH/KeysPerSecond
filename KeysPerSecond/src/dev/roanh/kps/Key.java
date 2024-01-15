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
package dev.roanh.kps;

import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.panels.KeyPanel;

/**
 * This class is used to keep track of how
 * many times a specific key is pressed.
 * @author Roan
 */
public class Key{
	/**
	 * Whether or not this key is currently pressed
	 */
	private boolean down = false;
	/**
	 * The total number of times this key has been pressed
	 */
	private int count = 0;
	/**
	 * The graphical display for this key, used
	 * for instant rendering updates.
	 */
	private KeyPanel panel = null;
	/**
	 * Whether or not alt has to be down
	 */
	private boolean alt;
	/**
	 * Whether or not ctrl has to be down
	 */
	private boolean ctrl;
	/**
	 * Whether or not shift has to be down
	 */
	private boolean shift;
	
	/**
	 * Constructs a new key object with the given hit count and modifier keys.
	 * @param count The number of times this key was hit so far.
	 * @param alt Whether alt has to be down for this key.
	 * @param ctrl Whether ctrl has to be down for this key.
	 * @param shift Whether shift has to be down for this key.
	 */
	public Key(int count, boolean alt, boolean ctrl, boolean shift){
		this.count = count;
		this.alt = alt;
		this.ctrl = ctrl;
		this.shift = shift;
	}

	/**
	 * Constructs a key with 0 hits that does not require modifiers.
	 */
	public Key(){
		this(0, false, false, false);
	}
	
	/**
	 * Constructs a key with 0 hits and with the modifiers from the given key code.
	 * @param extendedCode The key code to extract required modifiers from.
	 */
	public Key(int extendedCode){
		this(0, CommandKeys.hasAlt(extendedCode), CommandKeys.hasCtrl(extendedCode), CommandKeys.hasShift(extendedCode));
	}

	/**
	 * Constructs a key with 0 hits and settings from the given settings.
	 * @param info The settings to get the key modifiers from.
	 */
	public Key(KeyPanelSettings info){
		this(info.getKeyCode());
	}
	
	/**
	 * Sets the panel associated with this key (used for real time GUI updates).
	 * @param panel The panel associated with this key.
	 */
	public void setPanel(KeyPanel panel){
		this.panel = panel;
	}
	
	/**
	 * Gets the hit count for this key.
	 * @return The hit count for this key.
	 */
	public int getCount(){
		return count;
	}
	
	/**
	 * Checks if this key requires shift to be pressed to trigger.
	 * @return True if this key requires shift.
	 */
	public boolean hasShift(){
		return shift;
	}
	
	/**
	 * Checks if this key requires ctrl to be pressed to trigger.
	 * @return True if this key requires ctrl.
	 */
	public boolean hasCtrl(){
		return ctrl;
	}
	
	/**
	 * Checks if this key requires alt to be pressed to trigger.
	 * @return True if this key requires alt.
	 */
	public boolean hasAlt(){
		return alt;
	}
	
	/**
	 * Checks if this key is currently pressed.
	 * @return True if this key is pressed.
	 */
	public boolean isDown(){
		return down;
	}
	
	/**
	 * Sets the hit count for this key.
	 * @param count The new hit count.
	 */
	public void setCount(int count){
		this.count = count;
	}

	/**
	 * Called when this key is pressed
	 */
	public void keyPressed(){
		if(!down){
			count++;
			down = true;
			Main.tmp.incrementAndGet();
			Main.lastHitTime = System.nanoTime();
			if(panel != null){
				panel.repaint();
			}
		}
	}

	/**
	 * Called when this key is released
	 */
	public void keyReleased(){
		if(down){
			down = false;
			if(panel != null){
				panel.repaint();
			}
		}
	}
}
