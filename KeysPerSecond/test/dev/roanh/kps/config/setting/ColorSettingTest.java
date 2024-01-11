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
