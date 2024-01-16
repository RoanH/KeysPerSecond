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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import dev.roanh.kps.Main;
import dev.roanh.kps.Statistics;
import dev.roanh.util.Dialog;
import dev.roanh.util.FileSelector;
import dev.roanh.util.FileSelector.FileExtension;

public class ConfigLoader{
	/**
	 * Extension filter for all KeysPerSecond configuration files.
	 */
	private static final FileExtension KPS_ALL_EXT = FileSelector.registerFileExtension("KeysPerSecond config", "kps", "kpsconf", "kpsconf2", "kpsconf3");
	/**
	 * Extension filter for legacy KeysPerSecond configuration file formats.
	 */
	private static final FileExtension KPS_LEGACY_EXT = FileSelector.registerFileExtension("Legacy KeysPerSecond config", "kpsconf", "kpsconf2", "kpsconf3");
	private static final Preferences prefs = Preferences.userRoot().node("dev.roanh.kps");
	
	
	
	
	
	
	/**
	 * Loads a configuration file (with GUI).
	 * @return Whether or not the config was loaded successfully.
	 */
	public static final boolean loadConfiguration(){
		Path saveloc = Dialog.showFileOpenDialog(KPS_ALL_EXT, Configuration.KPS_NEW_EXT, KPS_LEGACY_EXT);
		if(saveloc == null){
			return false;
		}else if(Objects.toString(saveloc.getFileName()).endsWith("kpsconf") || Objects.toString(saveloc.getFileName()).endsWith("kpsconf2")){
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
				}catch(IOException | UnsupportedOperationException | IllegalArgumentException e){
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
	
	public static final Path getDefaultConfig(){
		String path = prefs.get("defaultConfig", null);
		return path == null ? null : Paths.get(path);
	}
	
	public static final void setDefaultConfig(Path config) throws BackingStoreException{
		prefs.put("defaultConfig", config.toAbsolutePath().toString());
		prefs.flush();
	}
}
