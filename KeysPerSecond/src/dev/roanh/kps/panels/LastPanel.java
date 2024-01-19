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
import dev.roanh.kps.config.group.LastPanelSettings;

/**
 * Panel showing the time since the last input.
 * @author Roan
 */
public class LastPanel extends DataPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -2570027494924539862L;
	/**
	 * Array of time resolutions to use when formatting times.
	 */
	private static final Resolution[] magnitudes = new Resolution[]{Resolution.HOUR, Resolution.MINUTE, Resolution.SECOND, Resolution.MILLIS};
	/**
	 * Last used resolution, used to invalidate cached rendering
	 * info when the resolution magnitude changes.
	 */
	private int lastResolution = 0;
	/**
	 * The settings for this panel.
	 */
	private LastPanelSettings config;

	/**
	 * Constructs a new last panel.
	 * @param config The panel configuration.
	 */
	public LastPanel(LastPanelSettings config){
		super(config);
		this.config = config;
	}

	@Override
	protected String getValue(){
		if(Main.lastHitTime == -1){
			lastResolution = -1;
			return "-";
		}
		
		boolean millis = config.showMillis();
		long diff = (System.nanoTime() - Main.lastHitTime) / 1000000;
		for(int i = 0; i < magnitudes.length; i++){
			if(diff >= magnitudes[i].millis || (magnitudes[i] == Resolution.SECOND && !millis)){
				String value = "";
				
				for(int j = magnitudes.length - 1; j > i; j--){
					Resolution res = magnitudes[j];
					if(j - i < config.getUnitCount() && (res != Resolution.MILLIS || millis)){
						value = " " + (diff % res.factor) + res.suffix + value;
					}
					diff /= res.factor;
				}
				
				if(lastResolution != i){
					lastResolution = i;
					cache.invalidateValueCache();
				}
				
				return diff + magnitudes[i].suffix + value;
			}
		}
		
		//this is impossible unless the time difference somehow goes negative
		return "?";
	}
	
	/**
	 * Helper class with time unit properties.
	 * @author Roan
	 */
	private static enum Resolution{
		/**
		 * Hour resolution.
		 */
		HOUR(24, 60 * 60 * 1000, "h"),
		/**
		 * Minute resolution.
		 */
		MINUTE(60, 60 * 1000, "m"),
		/**
		 * Second resolution.
		 */
		SECOND(60, 1000, "s"),
		/**
		 * Millisecond resolution.
		 */
		MILLIS(1000, 0, "ms");
		
		/**
		 * The factor get from a single unit of this resolution
		 * to a single unit of one resolution larger.
		 */
		private final int factor;
		/**
		 * The total number of milliseconds in a single unit of this resolution.
		 */
		private final long millis;
		/**
		 * The suffix to use for this resolution.
		 */
		private final String suffix;
		
		/**
		 * Constructs a new resolution.
		 * @param factor The factor up to the next resolution.
		 * @param millis The total number of milliseconds in one unit.
		 * @param suffix The suffix for this resolution.
		 */
		private Resolution(int factor, long millis, String suffix){
			this.factor = factor;
			this.millis = millis;
			this.suffix = suffix;
		}
	}
}
