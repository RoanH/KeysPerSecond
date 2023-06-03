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
package dev.roanh.kps.config;

/**
 * Enum of valid update rates for the program. These
 * values are all positive and a clean divisor of 1000.
 * The update rate only affects constantly updating components
 * such as statistic panels and graphs.
 * @author Roan
 */
public enum UpdateRate{
	MS_1000(1000),
	MS_500(500),
	MS_250(250),
	MS_200(200),
	MS_125(125),
	MS_100(100),
	MS_50(50),
	MS_25(25),
	MS_20(20),
	MS_10(10),
	MS_5(5),
	MS_1(1);
	
	private final int ms;
	
	private UpdateRate(int ms){
		this.ms = ms;
	}
	
	public int getRate(){
		return ms;
	}
	
	@Override
	public String toString(){
		return ms + "ms";
	}
	
	public static UpdateRate fromMs(int ms){
		for(UpdateRate rate : values()){
			if(rate.ms == ms){
				return rate;
			}
		}
		
		throw new IllegalArgumentException("Invalid update rate.");
	}
}
