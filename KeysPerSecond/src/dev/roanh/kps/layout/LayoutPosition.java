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

/**
 * Interface that panels that will be
 * added to a {@link Layout} have to implement
 * @author Roan
 * @see Layout
 */
public abstract interface LayoutPosition{
	/**
	 * Gets the x position of this
	 * panel in the layout
	 * @return The x position of this
	 *         panel in the layout
	 */
	public abstract int getLayoutX();

	/**
	 * Gets the y position of this
	 * panel in the layout
	 * @return The y position of this
	 *         panel in the layout
	 */
	public abstract int getLayoutY();

	/**
	 * Gets the width of this panel
	 * in the layout
	 * @return The width of this panel
	 *         in the layout
	 */
	public abstract int getLayoutWidth();

	/**
	 * Gets the height of this panel
	 * in the layout
	 * @return The height of this panel
	 *         in the layout
	 */
	public abstract int getLayoutHeight();
	
	/**
	 * Gets the position and size given by
	 * this LayoutPosition as a string
	 * @return A string giving the position
	 *         and size of this LayoutPosition
	 */
	public default String getLayoutLocation(){
		return "LayoutPosition[x=" + getLayoutX() + ",y=" + getLayoutY() + ",width=" + getLayoutWidth() + ",height=" + getLayoutHeight() + "]";
	}
}
