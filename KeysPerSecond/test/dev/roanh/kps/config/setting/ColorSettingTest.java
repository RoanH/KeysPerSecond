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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dev.roanh.kps.config.ThemeColor;

public class ColorSettingTest{

	@Test
	public void writeTest(){
		ColorSetting color = new ColorSetting("test", new ThemeColor(0, 0, 0, 255));
		
		assertEquals("Setting[test: #000000]", color.toString());
		
		color.update(new ThemeColor(15, 255, 255, 3));
		assertEquals("Setting[test: #0FFFFF03]", color.toString());
		
		color.update(new ThemeColor(128, 255, 16, 3));
		assertEquals("Setting[test: #80FF1003]", color.toString());
		
		color.update(new ThemeColor(128, 255, 16, 255));
		assertEquals("Setting[test: #80FF10]", color.toString());
		
		color.update(new ThemeColor(255, 255, 16, 128));
		assertEquals("Setting[test: #FFFF1080]", color.toString());
	}
	
	@Test
	public void parseTest(){
		ColorSetting color = new ColorSetting("test", new ThemeColor(0, 0, 0, 255));

		assertFalse(color.parse("#FFFFFF"));
		assertEquals(new ThemeColor(255, 255, 255, 255), color.getValue());

		assertFalse(color.parse("#FFFF1080"));
		assertEquals(new ThemeColor(255, 255, 16, 128), color.getValue());

		assertFalse(color.parse("[r=0,g=255,b=255]"));
		assertEquals(new ThemeColor(0, 255, 255, 128), color.getValue());
		
		assertTrue(color.parse("[r=0,g=300,b=255]"));
		color.update(new ThemeColor(color.getValue().getRGB(), 255));
		assertEquals(new ThemeColor(0, 0, 0, 255), color.getValue());
		
		assertTrue(color.parse("[r=300,g=0,b=255]"));
		assertEquals(new ThemeColor(0, 0, 0, 255), color.getValue());
		
		assertTrue(color.parse("[r=40,g=0,b=300]"));
		assertEquals(new ThemeColor(0, 0, 0, 255), color.getValue());
		
		assertTrue(color.parse("something"));
		assertEquals(new ThemeColor(0, 0, 0, 255), color.getValue());
	}
}
