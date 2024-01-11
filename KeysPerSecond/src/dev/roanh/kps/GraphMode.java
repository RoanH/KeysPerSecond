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

/**
 * Enum specifying all the different
 * layout positions for the graph
 * @author Roan
 */
@Deprecated
public enum GraphMode{
	/**
	 * Indicates that the graph is placed
	 * in the same window as the other tiles
	 */
	INLINE("Inline"),
	/**
	 * Indicates that the graph is placed
	 * in it's own window
	 */
	DETACHED("Detached");

	/**
	 * The display name of this mode
	 */
	private String name;

	/**
	 * Constructs a new GraphMode with
	 * the given display name 
	 * @param name The display name for this mode
	 */
	private GraphMode(String name){
		this.name = name;
	}

	@Override
	public String toString(){
		return name;
	}
}
