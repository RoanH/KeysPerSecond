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
package dev.roanh.kps.ui.model;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Custom comparable to use as a variable
 * bound on a JSpinner
 * @author Roan
 * @see Number
 * @see Comparable
 */
public class DynamicInteger extends Number implements Comparable<Integer>{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 7783953216941858736L;
	/**
	 * The value of this variable
	 */
	private Supplier<Integer> bound;
	
	/**
	 * Constructs a new DynamicInteger
	 * with the given variable bound
	 * @param bound The variable value
	 *        for this DynamicInteger
	 */
	public DynamicInteger(Supplier<Integer> bound){
		this.bound = bound;
	}
	
	@Override
	public int compareTo(Integer o){
		return bound.get().compareTo(o);
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Number){
			return bound.get() == ((Number)obj).intValue();
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(bound.get());
	}
	
	@Override
	public int intValue(){
		return bound.get();
	}

	@Override
	public long longValue(){
		return bound.get();
	}

	@Override
	public float floatValue(){
		return bound.get();
	}

	@Override
	public double doubleValue(){
		return bound.get();
	}
}
