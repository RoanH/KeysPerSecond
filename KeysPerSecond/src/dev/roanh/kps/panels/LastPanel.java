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

public class LastPanel extends BasePanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -2570027494924539862L;
	private static final Resolution[] magnitudes = new Resolution[]{Resolution.HOUR, Resolution.MINUTE, Resolution.SECOND, Resolution.MILLIS};
	private int lastResolution = 0;
	private LastPanelSettings config;

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
		
		String value = "";
		long diff = (System.nanoTime() - Main.lastHitTime) / 1000000;
		for(int i = 0; i < magnitudes.length; i++){
			if(diff >= magnitudes[i].millis){
				for(int j = magnitudes.length - 1; j > i; j--){
					Resolution res = magnitudes[j];
					if(j - i < config.getUnitCount() && (res != Resolution.MILLIS || config.showMillis())){
						value = " " + (diff % res.factor) + res.suffix + value;
					}
					diff /= res.factor;
				}
				
				if(lastResolution < i){
					lastResolution = i;
					cache.invalidateValueCache();
				}
				
				return diff + magnitudes[i].suffix + value;
			}
		}
		
		//this is impossible unless the time difference somehow goes negative
		return "?";
	}
	
	private static enum Resolution{
		HOUR(24, 60 * 60 * 1000, "h"),
		MINUTE(60, 60 * 1000, "m"),
		SECOND(60, 1000, "s"),
		MILLIS(1000, 0, "ms");
		
		private final int factor;
		private final long millis;
		private final String suffix;
		
		private Resolution(int factor, long millis, String suffix){
			this.factor = factor;
			this.millis = millis;
			this.suffix = suffix;
		}
	}
}
