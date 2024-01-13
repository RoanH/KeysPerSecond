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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigParser{
	private static final int MARK_LIMIT = 10000;
	private static final char[] LIST_ITEM_START = new char[]{' ', ' ', '-', ' '};
	private static final char[] LIST_ITEM_BODY = new char[]{' ', ' ', ' ', ' '};
	private static final char[] GROUP_BODY = new char[]{' ', ' '};
	private Map<String, Setting<?>> settings = new HashMap<String, Setting<?>>();
	private Map<String, SettingGroup> groups = new HashMap<String, SettingGroup>();
	private Map<String, SettingList<? extends SettingGroup>> lists = new HashMap<String, SettingList<? extends SettingGroup>>();
	private Version version;
	private boolean defaultUsed;//overriden, default used when not specified doesn't count, this is when given values are replaced with safe values deliberately
	private Configuration config;
	
	private ConfigParser(Path path){
		config = new Configuration(path);
	}
	
	public static Configuration read(Path path) throws IOException{
		return parse(path).getConfig();
	}
	
	public static ConfigParser parse(Path path) throws IOException{
		ConfigParser parser = new ConfigParser(path);
		parser.parse();
		return parser;
	}
	
	public Version getVersion(){
		return version;
	}
	
	public Configuration getConfig(){
		return config;
	}
	
	public boolean wasDefaultUsed(){
		return defaultUsed;
	}
	
	private void parse() throws IOException{
		try(BufferedReader in = Files.newBufferedReader(config.getPath())){
			//read version
			readVersion(in);

			//read data
			prepareMaps(version);
			readData(in);
		}
	}
	
	private void readVersion(BufferedReader in) throws IOException{
		in.mark(MARK_LIMIT);
		String line = in.readLine();
		if(line == null){
			throw new IOException("Empty config file");
		}
		
		if(line.startsWith("version:")){
			version = Version.parse(line.substring(8));
		}else{
			in.reset();
			version = Version.UNKNOWN;
		}
	}
	
	private void prepareMaps(Version version){
		//map settings to parse
		for(Setting<?> setting : config.getSettings()){
			settings.put(setting.getKey(), setting);
		}

		for(SettingGroup group : config.getSettingGroups()){
			groups.put(group.getKey(), group);
		}

		for(SettingList<? extends SettingGroup> list : config.getSettingLists()){
			lists.put(list.getKey(), list);
		}
		
		//legacy compatibility
		for(Setting<?> setting : config.getLegacySettings(version)){
			settings.put(setting.getKey(), setting);
		}
	}
	
	private void readData(BufferedReader in) throws IOException{
		String line;
		while((line = in.readLine()) != null){
			line = line.trim();
			if(line.startsWith("#") || line.isEmpty()){
				continue;
			}

			int mark = line.indexOf(':');
			if(mark != -1){
				String key = line.substring(0, mark).trim();

				//direct settings
				Setting<?> setting = settings.get(key);
				if(setting != null){
					defaultUsed |= setting.parse(line.substring(mark + 1, line.length()).trim());
					continue;
				}

				//setting groups
				SettingGroup group = groups.get(key);
				if(group != null){
					defaultUsed |= parseGroup(in, group);
					continue;
				}

				//setting lists
				SettingList<? extends SettingGroup> list = lists.get(key);
				if(list != null){
					defaultUsed |= parseList(in, list);
					continue;
				}
			}

			//unknown / invalid settings just get ignored but do generate a default used warning
			System.out.println("unknown: " + line);
			defaultUsed = true;
		}
	}
	
	private static boolean parseGroup(BufferedReader in, SettingGroup target) throws IOException{
		char[] lead = new char[2];
		
		Map<String, String> item = new HashMap<String, String>();
		while(in.ready()){
			in.mark(MARK_LIMIT);
			if(in.read(lead, 0, 2) != 2){
				//end of file hit or not enough group data
				in.reset();
				break;
			}
			
			if(!Arrays.equals(lead, GROUP_BODY)){
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
		
		return target.parse(item);
	}
	
	private static <T extends SettingGroup> boolean parseList(BufferedReader in, SettingList<T> list) throws IOException{
		char[] lead = new char[4];
		
		boolean defaultUsed = false;
		List<String> item = null;
		while(in.ready()){
			in.mark(MARK_LIMIT);
			if(in.read(lead, 0, 4) != 4){
				//end of file hit or not enough list data
				in.reset();
				break;
			}
			
			if(Arrays.equals(lead, LIST_ITEM_START)){
				if(item != null){
					defaultUsed |= list.add(item);
				}
				
				item = new ArrayList<String>();
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
			
			item.add(line.trim());
		}
		
		//end last item
		if(item != null){
			defaultUsed |= list.add(item);
		}
		
		return defaultUsed;
	}
}
