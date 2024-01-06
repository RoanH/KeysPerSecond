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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class ConfigParserTest{
	
	@Test
	public void fullTest() throws IOException{
//		Configuration config = new Configuration(Paths.get("test/main.kps"));
		//TODO currently without calling load this never parses anything using the old logic
		
		
		Configuration config = Configuration.newLoadTemporary(Paths.get("test/main.kps"));
		
		//TODO assert config version
		
//		max
//		avg
//		cur
//		total
//		keys
		assertFalse(config.isOverlayMode());
//		allkeys
//		allbuttons
		assertEquals(100, config.getUpdateRateMs());
//		percision
		assertFalse(config.isKeyModifierTrackingEnabled());
		
//		graph enable
//		graph backlog
//		graph avg
		
//		custom colour
//		foreground col
//		background col
//		foreground op
//		background op
		
//		cmd reset stats
//		cmd exit
//		cmd reset totals
//		cmd hide
//		cmd pause
//		cmd reload
		
		//TODO layout
		
		//TODO auto save
		
		//TODO keys
		
	}
	
}
