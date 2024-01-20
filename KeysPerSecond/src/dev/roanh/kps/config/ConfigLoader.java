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

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import dev.roanh.kps.Main;
import dev.roanh.util.Dialog;
import dev.roanh.util.FileSelector;
import dev.roanh.util.FileSelector.FileExtension;

/**
 * Utility class for loading and locating configuration files.
 * @author Roan
 */
public class ConfigLoader{
	/**
	 * Extension filter for all KeysPerSecond configuration files.
	 */
	private static final FileExtension KPS_ALL_EXT = FileSelector.registerFileExtension("KeysPerSecond config", "kps", "kpsconf", "kpsconf2", "kpsconf3");
	/**
	 * Extension filter for legacy KeysPerSecond configuration file formats.
	 */
	private static final FileExtension KPS_LEGACY_EXT = FileSelector.registerFileExtension("Legacy KeysPerSecond config", "kpsconf", "kpsconf2", "kpsconf3");
	/**
	 * Preferences store for persistent settings.
	 */
	private static final Preferences prefs = Preferences.userRoot().node("dev.roanh.kps");
	
	/**
	 * Loads a configuration file (with GUI).
	 * @return The loaded configuration or null if loading failed or was not possible.
	 */
	public static final Configuration loadConfiguration(){
		Path saveloc = Dialog.showFileOpenDialog(KPS_ALL_EXT, Configuration.KPS_NEW_EXT, KPS_LEGACY_EXT);
		if(saveloc == null){
			return null;
		}else if(Objects.toString(saveloc.getFileName()).endsWith("kpsconf") || Objects.toString(saveloc.getFileName()).endsWith("kpsconf2")){
			Dialog.showMessageDialog(
				"You are trying to load a legacy configuration file.\n"
				+ "This is no longer possible with this version of the program.\n"
				+ "You should convert your configuration file first using version 8.4."
			);
			return null;
		}
		
		try{
			ConfigParser parser = ConfigParser.parse(saveloc);

			if(parser.wasDefaultUsed()){
				Dialog.showMessageDialog("Configuration loaded succesfully but some default values were used.");
			}else{
				Dialog.showMessageDialog("Configuration loaded succesfully.");
			}
			
			return parser.getConfig();
		}catch(IOException e){
			e.printStackTrace();
			Dialog.showErrorDialog("Failed to read the requested configuration, cause: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Reloads the current configuration from file.
	 */
	public static void reloadConfig(){
		if(Main.config.getPath() != null){
			try{
				Main.applyConfig(ConfigParser.read(Main.config.getPath()), true);
			}catch(IOException e){
				e.printStackTrace();
				Dialog.showErrorDialog("Failed to reload the configuration, cause: " + e.getMessage());
			}
		}else{
			Dialog.showMessageDialog("Cannot reload a configuration that was not loaded from a file.");
		}
	}
	
	/**
	 * Gets the path of the configured default config if any.
	 * @return The default config or null if none is set.
	 */
	public static final Path getDefaultConfig(){
		String path = prefs.get("defaultConfig", null);
		return path == null ? null : Paths.get(path);
	}
	
	/**
	 * Sets the default config to open on launch.
	 * @param config The default config to open on launch or
	 *        null to unset the current config if any.
	 * @throws BackingStoreException When saving the setting fails.
	 */
	public static final void setDefaultConfig(Path config) throws BackingStoreException{
		if(config == null){
			prefs.remove("defaultConfig");
		}else{
			prefs.put("defaultConfig", config.toAbsolutePath().toString());
		}
		
		prefs.flush();
	}
	
	/**
	 * Attempts to load a preset configuration for the program if any. This
	 * will first try to load the given CLI passed config. If this config is
	 * not set (null) then the configured default config will be loaded if set.
	 * If neither are set or if loading either config fails then this method
	 * will do nothing. In case of an IO error the user will be informed.
	 * @param cliConfig The passed command line config path or null for none.
	 * @return The loaded configuration or null if loading failed, was not possible, or no config configured.
	 */
	public static final Configuration quickLoadConfiguration(String cliConfig){
		try{
			//prefer the CLI config if one was given
			if(cliConfig != null){
				Configuration config = parseConfiguration(cliConfig);
				if(config != null){
					return config;
				}else{
					//if the user explicitly requested a config via CLI we do not attempt to load the default config
					Dialog.showErrorDialog("Failed to load the requested configuration file.");
					return null;
				}
			}
			
			//see if a default config is set
			Path defaultConfig = getDefaultConfig();
			if(defaultConfig != null && Files.exists(defaultConfig)){
				Configuration config = ConfigParser.read(defaultConfig);
				if(config != null){
					return config;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			Dialog.showErrorDialog("Failed to load the requested configuration file.\nCause: " + e.getMessage());
		}

		//no usable configuration found
		return null;
	}
	
	/**
	 * Parses the given command line argument configuration file by
	 * loading the file from disk while treating unknown characters
	 * as wildcards to deal with Windows argument encoding issues.
	 * @param config The configuration file path.
	 * @return The loaded configuration file or <code>null</code>
	 *         if the file was not found.
	 * @throws IOException When an IOException occurs.
	 */
	private static final Configuration parseConfiguration(String config) throws IOException{
		try{
			Path path = Paths.get(config);
			return Files.exists(path) ? ConfigParser.read(path) : null;
		}catch(InvalidPathException e){
			int index = config.lastIndexOf(File.separatorChar);
			try{
				Path dir = Paths.get(config.substring(0, index));
				final String name = config.substring(index + 1);
				Filter<Path> filter = p->{
					String other = Objects.toString(p.getFileName());
					for(int i = 0; i < name.length(); i++){
						char ch = name.charAt(i);
						if(ch == '?'){
							continue;
						}
						if(i >= other.length() || ch != other.charAt(i)){
							return false;
						}
					}
					return true;
				};
				
				try(DirectoryStream<Path> files = Files.newDirectoryStream(dir, filter)){
					Iterator<Path> iter = files.iterator();
					if(iter.hasNext()){
						return ConfigParser.read(iter.next());
					}
				}
				
				return null;
			}catch(InvalidPathException e2){
				return null;
			}
		}
	}
}
