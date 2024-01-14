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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.roanh.kps.config.ListItemConstructor;
import dev.roanh.kps.config.group.KeyPanelSettings;

/**
 * Key panel settings parser that handles the legacy key
 * format in addition to the new format introduced in v8.8.
 * @author Roan
 */
public class LegacyCompatibleKeyConstructor implements ListItemConstructor<KeyPanelSettings>{
	/**
	 * Regex used to parse the legacy key format used from v8.0 to v8.7.<br>
	 * Group 1: key keycode.<br>
	 * Group 2: panel x location.<br>
	 * Group 3: panel y location.<br>
	 * Group 4: panel width.<br>
	 * Group 5: panel height.<br>
	 * Group 6: panel rendering mode.<br>
	 * Group 7: true/false if the panel is visible or not.<br>
	 * Group 8: display name of the panel.
	 */
	private static final Pattern LEGEACY_KEY_REGEX = Pattern.compile("^\\[keycode=(\\d+),x=(-?\\d+),y=(-?\\d+),width=(-?\\d+),height=(-?\\d+),mode=(HORIZONTAL_TN|HORIZONTAL_NT|DIAGONAL1|DIAGONAL2|DIAGONAL3|DIAGONAL4|VERTICAL|TEXT_ONLY|VALUE_ONLY),visible=(true|false),name=\\\"(.*)\\\"]$");

	@Override
	public ParsedItem<KeyPanelSettings> construct(List<String> data){
		Map<String, String> info = new HashMap<String, String>();
		if(data.size() == 1){
			Matcher m = LEGEACY_KEY_REGEX.matcher(data.get(0));
			if(m.matches()){
				info.put("keycode", m.group(1));
				info.put("x", m.group(2));
				info.put("y", m.group(3));
				info.put("width", m.group(4));
				info.put("height", m.group(5));
				info.put("mode", m.group(6));
				info.put("visible", m.group(7));
				info.put("name", m.group(8));
				return ListItemConstructor.constructThenParse(KeyPanelSettings::new, info);
			}
		}
		
		info = ListItemConstructor.buildMap(data);
		return info == null ? null : ListItemConstructor.constructThenParse(KeyPanelSettings::new, info);
	}
}
