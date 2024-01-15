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

import dev.roanh.kps.config.IndentWriter;
import dev.roanh.kps.config.Setting;

/**
 * Small legacy setting that ensures parsing of the legacy
 * graphMode setting does not throw a warning that a default
 * was used as long as the configured value was set to the
 * only currently supported option of inline. In addition this
 * setting silently discards the legacy graphPosition setting
 * that accompanied the detached graph mode setting.
 * @author Roan
 */
public class LegacyGraphSetting extends Setting<String>{

	/**
	 * Constructs a new legacy graph mode setting.
	 * @param key The setting key, either graphMode or graphPosition.
	 * @param required The only accepted value to not trigger a
	 *        warning that a default value was used. If null all
	 *        values are accepted without generating a warning.
	 */
	public LegacyGraphSetting(String key, String required){
		super(key, required);
	}

	@Override
	public boolean parse(String data){
		return getDefaultValue() != null && !data.equalsIgnoreCase(getDefaultValue());
	}

	@Override
	public void write(IndentWriter out){
		throw new IllegalStateException("Legacy proxy settings should never be written.");
	}
}
