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
 * values are all positive and a clean divisors of 1000.
 * The update rate only affects constantly updating components
 * such as statistic panels and graphs.
 * @author Roan
 */
public enum UpdateRate{
	/**
	 * Update rate of 1000 milliseconds.
	 */
	MS_1000(1000),
	/**
	 * Update rate of 500 milliseconds.
	 */
	MS_500(500),
	/**
	 * Update rate of 250 milliseconds.
	 */
	MS_250(250),
	/**
	 * Update rate of 200 milliseconds.
	 */
	MS_200(200),
	/**
	 * Update rate of 125 milliseconds.
	 */
	MS_125(125),
	/**
	 * Update rate of 100 milliseconds.
	 */
	MS_100(100),
	/**
	 * Update rate of 50 milliseconds.
	 */
	MS_50(50),
	/**
	 * Update rate of 25 milliseconds.
	 */
	MS_25(25),
	/**
	 * Update rate of 20 milliseconds.
	 */
	MS_20(20),
	/**
	 * Update rate of 10 milliseconds.
	 */
	MS_10(10),
	/**
	 * Update rate of 5 milliseconds.
	 */
	MS_5(5),
	/**
	 * Update rate of 1 millisecond.
	 */
	MS_1(1);
	
	/**
	 * The update rate in milliseconds.
	 */
	private final int ms;
	
	/**
	 * Constructs a new update rate with the given update
	 * rate time frame in milliseconds.
	 * @param ms The update rate in milliseconds, has to
	 *        be a positive clean divisor of 1000.
	 */
	private UpdateRate(int ms){
		this.ms = ms;
	}
	
	/**
	 * Gets the update rate in milliseconds.
	 * @return The update rate in milliseconds.
	 */
	public int getRate(){
		return ms;
	}
	
	/**
	 * Checks if this update is considered high, this
	 * classification applies to update rates below 100ms.
	 * @return True if this update rate is high.
	 */
	public boolean isHigh(){
		return ms < 100;
	}
	
	/**
	 * Checks if this update is considered very high, this
	 * classification applies to update rates below 10ms.
	 * Update rates this low are likely to cause performance issues.
	 * @return True if this update rate is very high.
	 */
	public boolean isVeryHigh(){
		return ms < 10;
	}
	
	@Override
	public String toString(){
		return ms + "ms";
	}
	
	/**
	 * Resolves an update rate from its millisecond rate.
	 * @param ms The update rate in milliseconds.
	 * @return The update rate instance.
	 * @throws IllegalArgumentException When the given rate
	 *         does not correspond to a valid update rate.
	 */
	public static UpdateRate fromMs(int ms) throws IllegalArgumentException{
		for(UpdateRate rate : values()){
			if(rate.ms == ms){
				return rate;
			}
		}
		
		throw new IllegalArgumentException("Invalid update rate.");
	}
}
