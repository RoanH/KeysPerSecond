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
package dev.roanh.kps.config.group;

import java.util.List;
import java.util.Map;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.SettingGroup;
import dev.roanh.kps.config.legacy.LegacyProxyStore;
import dev.roanh.kps.config.legacy.ProxySetting;
import dev.roanh.kps.config.setting.CommandKeySetting;

/**
 * Setting for all the configurable command keys.
 * @author Roan
 */
public class CommandSettings extends SettingGroup implements LegacyProxyStore{
	/**
	 * Reset stats command key.
	 */
	private final CommandKeySetting resetStats = new CommandKeySetting("resetStats");
	/**
	 * Exit command key.
	 */
	private final CommandKeySetting exit = new CommandKeySetting("exit", NativeKeyEvent.VC_U, false, true);
	/**
	 * Reset totals command key.
	 */
	private final CommandKeySetting resetTotals = new CommandKeySetting("resetTotals");
	/**
	 * Hide/show command key.
	 */
	private final CommandKeySetting hide = new CommandKeySetting("hide", NativeKeyEvent.VC_Y, false, true);
	/**
	 * Pause command key.
	 */
	private final CommandKeySetting pause = new CommandKeySetting("pause", NativeKeyEvent.VC_T, false, true);
	/**
	 * Reload configuration command key.
	 */
	private final CommandKeySetting reload = new CommandKeySetting("reload");

	/**
	 * Constructs new command key settings.
	 */
	public CommandSettings(){
		super("commands");
	}
	
	/**
	 * Gets the command for resetting stats.
	 * @return The command for resetting stats.
	 */
	public CommandKeySetting getCommandResetStats(){
		return resetStats;
	}
	
	/**
	 * Gets the command for resetting totals.
	 * @return The command for resetting totals.
	 */
	public CommandKeySetting getCommandResetTotals(){
		return resetTotals;
	}

	/**
	 * Gets the command for hiding the window.
	 * @return The command for hiding the window.
	 */
	public CommandKeySetting getCommandHide(){
		return hide;
	}
	
	/**
	 * Gets the command for pausing updates.
	 * @return The command for pausing updates.
	 */
	public CommandKeySetting getCommandPause(){
		return pause;
	}
	
	/**
	 * Gets the command for reloading the configuration.
	 * @return The command for reloading the configuration.
	 */
	public CommandKeySetting getCommandReload(){
		return reload;
	}
	
	/**
	 * Gets the command for exiting the application.
	 * @return The command for exiting the application.
	 */
	public CommandKeySetting getCommandExit(){
		return exit;
	}
	
	@Override
	public boolean parse(Map<String, String> data){
		return findAndParse(data, resetStats, exit, resetTotals, hide, pause, reload);
	}

	@Override
	public void writeItems(IndentWriter out){
		resetStats.write(out);
		exit.write(out);
		resetTotals.write(out);
		hide.write(out);
		pause.write(out);
		reload.write(out);
	}

	@Override
	public void collectLegacyProxies(List<Setting<?>> proxyList){
		proxyList.add(ProxySetting.of("keyResetStats", resetStats));
		proxyList.add(ProxySetting.of("keyExit", exit));
		proxyList.add(ProxySetting.of("keyResetTotals", resetTotals));
		proxyList.add(ProxySetting.of("keyHide", hide));
		proxyList.add(ProxySetting.of("keyPause", pause));
		proxyList.add(ProxySetting.of("keyReload", reload));
	}
}
