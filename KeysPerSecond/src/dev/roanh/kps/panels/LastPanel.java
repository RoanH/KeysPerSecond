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
		long diff = (System.nanoTime() - Main.lastHitTime) / 10000;//00;
		for(int i = 0; i < magnitudes.length; i++){
			if(diff >= magnitudes[i].millis){
				System.out.println("best res: " + magnitudes[i]);
				for(int j = Math.min(magnitudes.length - 1, i + config.getUnitCount()); j > i; j--){
					Resolution res = magnitudes[j];
					System.out.println("append: " + res + " diff " + diff);
					if(res != Resolution.MILLIS || config.showMillis()){
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
		
		return null;
		
		
		
//		
//		
//		for(Resolution res : Resolution.values()){
//			if(diff >= res.millis){
//				while(units > 0){
//					
//					
//					units--;
//				}
//				
//				return value;
//			}
//		}
//		
		
		
		
		
		
		
		
		
//		
//		
//		
//		
//		if(diff < 1000 && config.showMillis()){
//			lastResolution = 1;
//		}
//		
//		
//		
		
		
		
//		
//		if(diff < 1000){
//			lastResolution = 1;
//			return diff + "ms";
//		}
//		
//		long rem = diff % 1000;
//		diff /= 1000;
//		if(diff < 60){
//			if(lastResolution < 2){
//				lastResolution = 2;
//				cache.invalidateValueCache();
//			}
//			
//			return config.showTwoUnits() ? (diff + "s " + rem + "ms") : (diff + "s");
//		}
//		
//		rem = diff % 60;
//		diff /= 60;
//		if(diff < 60){
//			if(lastResolution < 3){
//				lastResolution = 3;
//				cache.invalidateValueCache();
//			}
//			
//			return config.showTwoUnits() ? (diff + "m " + rem + "s") : (diff + "m");
//		}
//
//		if(lastResolution < 3){
//			lastResolution = 3;
//			cache.invalidateValueCache();
//		}
//		
//		rem = diff % 60;
//		diff /= 60;
//		return config.showTwoUnits() ? (diff + "h " + rem + "m") : (diff + "h");
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
