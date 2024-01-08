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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.config.group.StatsSavingSettings;

public class ConfigParserTest{
	
	@Test
	public void fullTest1() throws IOException{
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
		
		//stats
		StatsSavingSettings stats = config.getStatsSavingSettings();
		assertFalse(stats.isAutoSaveEnabled());
		assertEquals("C:\\Users\\RoanH", stats.getAutoSaveDestination());
		assertEquals("'kps stats' yyyy-MM-dd HH.mm.ss'.kpsstats'", stats.getAutoSaveFormat());
		assertEquals(600000, stats.getAutoSaveInterval());
		assertFalse(stats.isSaveOnExitEnabled());
		assertFalse(stats.isLoadOnLaunchEnabled());
		
		//TODO keys
		
	}
	
	@Test
	public void fullTest2() throws IOException{
		Configuration config = Configuration.newLoadTemporary(Paths.get("test/config88.kps"));

		
		
		
		//note: we assume the order is consistent with the config but this isn't strictly required and may change in the future
		SettingList<KeyPanelSettings> keys = config.getKeySettings();
		assertEquals(2, keys.size());
		
		KeyPanelSettings key1 = keys.get(0);
		assertEquals(1048606, key1.getKeyCode());
		assertEquals(13, key1.getX());
		assertEquals(0, key1.getY());
		assertEquals(2, key1.getWidth());
		assertEquals(3, key1.getHeight());
		assertEquals(RenderingMode.VALUE_ONLY, key1.getRenderingMode());
		assertTrue(key1.isVisible());
		assertEquals("A", key1.getName());
		
		KeyPanelSettings key2 = keys.get(1);
		assertEquals(1048607, key2.getKeyCode());
		assertEquals(12, key2.getX());
		assertEquals(9, key2.getY());
		assertEquals(6, key2.getWidth());
		assertEquals(7, key2.getHeight());
		assertEquals(RenderingMode.VALUE_ONLY, key2.getRenderingMode());
		assertFalse(key2.isVisible());
		assertEquals("B", key2.getName());
		
		//stats
		StatsSavingSettings stats = config.getStatsSavingSettings();
		assertFalse(stats.isAutoSaveEnabled());
		assertEquals("C:\\Users\\RoanH", stats.getAutoSaveDestination());
		assertEquals("'kps stats' yyyy-MM-dd HH.mm.ss'.kpsstats'", stats.getAutoSaveFormat());
		assertEquals(600000, stats.getAutoSaveInterval());
		assertFalse(stats.isSaveOnExitEnabled());
		assertFalse(stats.isLoadOnLaunchEnabled());
		assertEquals("C:\\Users\\RoanH\\stats.kpsstats", stats.getSaveFile());
	}
}
