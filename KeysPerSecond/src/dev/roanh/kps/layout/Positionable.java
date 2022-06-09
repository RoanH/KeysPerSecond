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
package dev.roanh.kps.layout;

import dev.roanh.kps.RenderingMode;

/**
 * Configuration for panels that can 
 * be added to the layout.
 * @author Roan
 */
public abstract class Positionable{
	/**
	 * The x position of the panel.
	 */
	private int x;
	/**
	 * The y position of the panel.
	 */
	private int y;
	/**
	 * The width of the panel.
	 */
	private int width;
	/**
	 * The height of the panel.
	 */
	private int height;
	/**
	 * The text rendering mode of the panel.
	 */
	private RenderingMode mode;
	
	/**
	 * Constructs a new positionable with the given
	 * position, size and rendering mode.
	 * @param x The x position.
	 * @param y The y position.
	 * @param width The panel width.
	 * @param height The panel height.
	 * @param mode The panel rendering mode.
	 */
	public Positionable(int x, int y, int width, int height, RenderingMode mode){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.mode = mode;
	}
	
	/**
	 * Sets the x position for this panel
	 * @param x The new x position
	 */
	public void setX(int x){
		this.x = x;
	}

	/**
	 * Sets the y position for this panel
	 * @param y The new y position
	 */
	public void setY(int y){
		this.y = y;
	}

	/**
	 * Sets the width for this panel
	 * @param w The new width
	 */
	public void setWidth(int w){
		width = w;
	}

	/**
	 * Sets the height for this panel
	 * @param h The new height
	 */
	public void setHeight(int h){
		height = h;
	}

	/**
	 * Gets the display name for this panel
	 * @return The display name for this panel
	 */
	public abstract String getName();//TODO keep this abstract or not? -- could reuse this base class for graphs but those do not have a title currently

	/**
	 * Gets the x position for this panel
	 * @return The x position for this panel
	 */
	public int getX(){
		return x;
	}

	/**
	 * Gets the y position for this panel
	 * @return The y position for this panel
	 */
	public int getY(){
		return y;
	}

	/**
	 * Gets the width for this panel
	 * @return The width for this panel
	 */
	public int getWidth(){
		return width;
	}

	/**
	 * Gets the height for this panel
	 * @return The height for this panel
	 */
	public int getHeight(){
		return height;
	}

	/**
	 * Gets the rendering mode for this panel
	 * @return The rendering mode for this panel
	 */
	public RenderingMode getRenderingMode(){
		return mode;
	}

	/**
	 * Sets the rendering mode for this panel
	 * @param mode The new rendering mode
	 */
	public void setRenderingMode(RenderingMode mode){
		this.mode = mode;
	}
}
