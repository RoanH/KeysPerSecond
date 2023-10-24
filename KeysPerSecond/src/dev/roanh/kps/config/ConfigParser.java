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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConfigParser{
	private String version;
	
	private boolean defaultUsed;
	
	
	
	
	
	public ConfigParser(BufferedReader in) throws IOException{
		
		
		
	}
	
	
	private void parse(BufferedReader in) throws IOException{
		//read version
		String line = in.readLine();
		if(line == null){
			throw new IOException("Empty config file");
		}
		
		if(!line.startsWith("version:")){
			//the last version to not declare a version
			version = "v8.4";
		}else{
			version = line.substring(8).trim();
		}

		//read data
		
		
		
		
	}
	
	
	private Map<String, Setting<?>> settings;
	
	
	
	private static final char[] LIST_ITEM_START = new char[]{' ', ' ', '-', ' '};
	private static final char[] LIST_ITEM_BODY = new char[]{' ', ' ', ' ', ' '};
	
	public static void main(String[] args) throws IOException{
		parseList(Files.newBufferedReader(Paths.get("C:\\Users\\RoanH\\Downloads\\ymltest.txt")), map->{
			
			System.out.println("item: " + map);
			
			return null;
		});
	}
	
	private static <T extends SettingGroup> List<T> parseList(BufferedReader in, Function<Map<String, String>, T> ctor) throws IOException{
		in.mark(1000);
		char[] lead = new char[4];
		List<T> data = new ArrayList<T>();
		
		Map<String, String> item = null;
		while(in.ready()){
			if(in.read(lead, 0, 4) != 4){
				//end of file hit or not enough list data
				in.reset();
				break;
			}
			
			if(Arrays.equals(lead, LIST_ITEM_START)){
				if(item != null){
					data.add(ctor.apply(item));
				}
				
				item = new HashMap<String, String>();
			}else if(!Arrays.equals(lead, LIST_ITEM_BODY)){
				//end of list
				in.reset();
				break;
			}
			
			String line = in.readLine();
			if(line == null){
				//end of file
				in.reset();
				break;
			}
			
			int mark = line.indexOf(':');
			if(mark == -1){
				//assume leading whitespace on the next line
				in.reset();
				break;
			}
			
			item.put(line.substring(0, mark).trim(), line.substring(mark + 1, line.length()).trim());
		}
		
		//end last item
		if(item != null){
			data.add(ctor.apply(item));
		}
		
		return data;
	}
	
	
	
//	private boolean parseSettings()
	
	private boolean parseSetting(String key, String data){
		Setting<?> setting = settings.get(key);
		if(setting == null){
			//TODO unknown key silently ignore or report -- probably legacy actually
		}
		
		return setting.parse(data);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
