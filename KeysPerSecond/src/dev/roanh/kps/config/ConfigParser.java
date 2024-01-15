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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.roanh.kps.Main;
import dev.roanh.kps.Statistics;
import dev.roanh.util.Dialog;
import dev.roanh.util.FileSelector;
import dev.roanh.util.FileSelector.FileExtension;

/**
 * Class used to parse configuration files.
 * @author Roan
 * @see Configuration
 */
public class ConfigParser{
	/**
	 * Extension filter for the current KeysPerSecond configuration file format.
	 */
	public static final FileExtension KPS_NEW_EXT = FileSelector.registerFileExtension("KeysPerSecond config", "kps");
	/**
	 * Extension filter for all KeysPerSecond configuration files.
	 */
	private static final FileExtension KPS_ALL_EXT = FileSelector.registerFileExtension("KeysPerSecond config", "kps", "kpsconf", "kpsconf2", "kpsconf3");
	/**
	 * Extension filter for legacy KeysPerSecond configuration file formats.
	 */
	private static final FileExtension KPS_LEGACY_EXT = FileSelector.registerFileExtension("Legacy KeysPerSecond config", "kpsconf", "kpsconf2", "kpsconf3");
	/**
	 * The read ahead limit mark used when parsing.
	 */
	private static final int MARK_LIMIT = 10000;
	/**
	 * Start of a new list item prefix.
	 */
	private static final char[] LIST_ITEM_START = new char[]{' ', ' ', '-', ' '};
	/**
	 * Continuation of a list item body prefix.
	 */
	private static final char[] LIST_ITEM_BODY = new char[]{' ', ' ', ' ', ' '};
	/**
	 * Group item body prefix (indent).
	 */
	private static final char[] GROUP_BODY = new char[]{' ', ' '};
	/**
	 * A key index map of settings to parse.
	 */
	private Map<String, Setting<?>> settings = new HashMap<String, Setting<?>>();
	/**
	 * A key index map of setting groups to parse.
	 */
	private Map<String, SettingGroup> groups = new HashMap<String, SettingGroup>();
	/**
	 * A key index map of setting lists to parse.
	 */
	private Map<String, SettingList<? extends SettingGroup>> lists = new HashMap<String, SettingList<? extends SettingGroup>>();
	/**
	 * The version of the configuration being parsed.
	 */
	private Version version;
	/**
	 * Set to true when a setting could not be parsed and had to be reset to default values.
	 */
	private boolean defaultUsed;
	/**
	 * The configuration being parsed.
	 */
	private Configuration config;
	
	/**
	 * Constructs a new configuration parser.
	 * @param path The path to configuration file to read.
	 */
	private ConfigParser(Path path){
		config = new Configuration(path);
	}
	
	/**
	 * Loads a configuration file (with GUI).
	 * @return Whether or not the config was loaded successfully.
	 */
	public static final boolean loadConfiguration(){
		Path saveloc = Dialog.showFileOpenDialog(KPS_ALL_EXT, KPS_NEW_EXT, KPS_LEGACY_EXT);
		if(saveloc == null){
			return false;
		}else if(saveloc.getFileName().toString().endsWith("kpsconf") || saveloc.getFileName().toString().endsWith("kpsconf2")){
			Dialog.showMessageDialog(
				"You are trying to load a legacy configuration file.\n"
				+ "This is no longer possible with this version of the program.\n"
				+ "You should convert your configuration file first using version 8.4."
			);
			return false;
		}
		
		try{
			ConfigParser parser = ConfigParser.parse(saveloc);
			Main.config = parser.getConfig();

			if(parser.wasDefaultUsed()){
				Dialog.showMessageDialog("Configuration loaded succesfully but some default values were used.");
			}else{
				Dialog.showMessageDialog("Configuration loaded succesfully.");
			}
			
			if(Main.config.getFramePosition().hasPosition()){
				Main.frame.setLocation(Main.config.getFramePosition().getLocation());
			}
			
			if(Main.config.getStatsSavingSettings().isLoadOnLaunchEnabled()){
				try{
					Statistics.loadStats(Paths.get(Main.config.getStatsSavingSettings().getSaveFile()));
				}catch(Exception e){
					e.printStackTrace();
					Dialog.showMessageDialog("Failed to load statistics on launch.\nCause: " + e.getMessage());
				}
			}
			
			return true;
		}catch(IOException e){
			e.printStackTrace();
			Dialog.showErrorDialog("Failed to read the requested configuration, cause: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Reads the configuration at the given path.
	 * @param path The path to the configuration to parse.
	 * @return The parsed configuration file.
	 * @throws IOException When an IOException occurs.
	 */
	public static Configuration read(Path path) throws IOException{
		return parse(path).getConfig();
	}
	
	/**
	 * Reads and parses the configuration at the given path.
	 * @param path The path to configuration to parse.
	 * @return The config parser containing the parsed configuration.
	 * @throws IOException When an IOException occurs.
	 */
	public static ConfigParser parse(Path path) throws IOException{
		ConfigParser parser = new ConfigParser(path);
		parser.parse();
		return parser;
	}
	
	/**
	 * Gets the version of the parsed configuration file.
	 * This indicates the KeysPerSecond version that wrote it.
	 * @return The version for the parsed configuration file.
	 */
	public Version getVersion(){
		return version;
	}
	
	/**
	 * Gets the parsed configuration file.
	 * @return The parsed configuration file.
	 */
	public Configuration getConfig(){
		return config;
	}
	
	/**
	 * Tests if any settings were reset to their default value because parsing them failed.
	 * @return True if any default values were used.
	 */
	public boolean wasDefaultUsed(){
		return defaultUsed;
	}
	
	/**
	 * Parses the set configuration file.
	 * @throws IOException When an IOException occurs.
	 */
	private void parse() throws IOException{
		try(BufferedReader in = Files.newBufferedReader(config.getPath())){
			//read version
			readVersion(in);

			//read data
			prepareMaps(version);
			readData(in);
		}
	}
	
	/**
	 * Reads the configuration version from the file being parsed.
	 * @param in The reader to the configuration file.
	 * @throws IOException When an IOException occurs.
	 */
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
	
	/**
	 * Prepares key-setting maps for all the settings to parse.
	 * @param version The version of the configuration being read
	 *        to use to determine which legacy compatibility settings to include.
	 */
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
	
	/**
	 * Reads all settings from the configuration being read.
	 * @param in The reader to the configuration file.
	 * @throws IOException When an IOException occurs.
	 */
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
					parseGroup(in, group);
					continue;
				}

				//setting lists
				SettingList<? extends SettingGroup> list = lists.get(key);
				if(list != null){
					parseList(in, list);
					continue;
				}
			}

			//unknown / invalid settings just get ignored but do generate a default used warning
			defaultUsed = true;
		}
	}
	
	/**
	 * Parses a setting group.
	 * @param in The reader to read from.
	 * @param target The group to store the parsed data in.
	 * @throws IOException When an IOException occurs.
	 */
	private void parseGroup(BufferedReader in, SettingGroup target) throws IOException{
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
		
		defaultUsed |= target.parse(item);
	}
	
	/**
	 * Parses a setting list.
	 * @param <T> The list data type.
	 * @param in The reader to read from.
	 * @param list The list to store the parsed data in.
	 * @throws IOException When an IOException occurs.
	 */
	private <T extends SettingGroup> void parseList(BufferedReader in, SettingList<T> list) throws IOException{
		char[] lead = new char[4];
		
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
	}
}