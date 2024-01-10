package dev.roanh.kps.config.setting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

public class ColorSettingTest{

	@Test
	public void writeTest(){
		ColorSetting color = new ColorSetting("test", Color.BLACK);
		
		assertEquals("Setting[test: #000000]", color.toString());
		
		color.update(new Color(15, 255, 255, 3));
		assertEquals("Setting[test: #0FFFFF03]", color.toString());
		
		color.update(new Color(128, 255, 16, 3));
		assertEquals("Setting[test: #80FF1003]", color.toString());
		
		color.update(new Color(128, 255, 16, 255));
		assertEquals("Setting[test: #80FF10]", color.toString());
		
		color.update(new Color(255, 255, 16, 128));
		assertEquals("Setting[test: #FFFF1080]", color.toString());
	}
	
	@Test
	public void parseTest(){
		ColorSetting color = new ColorSetting("test", Color.BLACK);

		assertFalse(color.parse("#FFFFFF"));
		assertEquals(Color.WHITE, color.getValue());

		assertFalse(color.parse("#FFFF1080"));
		assertEquals(new Color(255, 255, 16, 128), color.getValue());

		assertFalse(color.parse("[r=0,g=255,b=255]"));
		assertEquals(new Color(0, 255, 255, 255), color.getValue());
		
		assertTrue(color.parse("[r=0,g=300,b=255]"));
		assertEquals(Color.BLACK, color.getValue());
		
		assertTrue(color.parse("[r=300,g=0,b=255]"));
		assertEquals(Color.BLACK, color.getValue());
		
		assertTrue(color.parse("[r=40,g=0,b=300]"));
		assertEquals(Color.BLACK, color.getValue());
		
		assertTrue(color.parse("something"));
		assertEquals(Color.BLACK, color.getValue());
	}
}
