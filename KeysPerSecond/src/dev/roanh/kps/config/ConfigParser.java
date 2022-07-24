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

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigParser{
	private Map<String, List<ValueData>> data = new HashMap<String, List<ValueData>>();
	private String version;
	
	
	
	
	
	public ConfigParser(BufferedReader in){
		//read version
		String line = in.readLine();
		if(!line.startsWith("version:")){
			//the last version to not declare a version
			version = "v8.4";
		}else{
			version = line.substring(8).trim();
		}
		
		
		
	}
	
	
	
	private static final class ValueData{
		private final String data = null;
		//private final 
	}
	
	private static final class KeyValuePair{
		
	}
}
