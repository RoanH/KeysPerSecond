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
package dev.roanh.kps.config.legacy;

import java.util.List;

import dev.roanh.kps.config.Setting;
import dev.roanh.kps.config.SettingGroup;

/**
 * Interface used by setting groups to indicate that they
 * provide legacy setting mapping proxies.
 * @author Roan
 * @see SettingGroup
 */
public abstract interface LegacyProxyStore{

	/**
	 * Collects mappings of legacy settings for this panel
	 * used to map legacy settings onto the new config system.
	 * @param proxyList The list to add the legacy settings to.
	 * @see ProxySetting
	 */
	public abstract void collectLegacyProxies(List<Setting<?>> proxyList);
}
