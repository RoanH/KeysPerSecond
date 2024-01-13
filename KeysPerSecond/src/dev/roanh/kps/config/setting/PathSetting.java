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
package dev.roanh.kps.config.setting;

import dev.roanh.kps.ui.model.FilePathFormatterFactory;

/**
 * Setting for file paths that performs some extra
 * validation to ensure paths are valid file system paths.
 * @author Roan
 */
public class PathSetting extends StringSetting{

	/**
	 * Constructs a new path setting.
	 * @param key The setting key.
	 * @param defaultValue The default setting value.
	 */
	public PathSetting(String key, String defaultValue){
		super(key, defaultValue);
	}

	@Override
	public boolean parse(String data){
		super.parse(data);

		if(FilePathFormatterFactory.isValidPath(getValue())){
			return false;
		}else{
			reset();
			return true;
		}
	}
}
