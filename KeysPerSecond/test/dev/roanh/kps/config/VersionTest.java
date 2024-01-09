package dev.roanh.kps.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class VersionTest{

	@Test
	public void beforeDirect(){
		Version base = new Version(4, 6);
		
		assertTrue(base.isBefore(5, 6));
		assertFalse(base.isBefore(4, 5));
		assertFalse(base.isBefore(4, 6));
		assertTrue(base.isBefore(6, 0));
		assertTrue(base.isBefore(4, 7));
		assertFalse(base.isBefore(3, 7));
	}
	
	@Test
	public void beforeCompare(){
		Version base = new Version(4, 6);
		
		assertTrue(base.isBefore(new Version(5, 6)));
		assertFalse(base.isBefore(new Version(4, 5)));
		assertFalse(base.isBefore(new Version(4, 6)));
		assertTrue(base.isBefore(new Version(6, 0)));
		assertTrue(base.isBefore(new Version(4, 7)));
		assertFalse(base.isBefore(new Version(3, 7)));
	}
	
	@Test
	public void stringTest(){
		assertEquals("v8.4", new Version(8, 4).toString());
		assertEquals("unknown", Version.UNKNOWN.toString());
	}
	
	@Test
	public void parseTest(){
		assertEquals(new Version(4, 5), Version.parse("4.5"));
		assertEquals(new Version(5, 9), Version.parse("v5.9"));
		assertNotEquals(new Version(4, 5), Version.parse("3.5"));
		assertThrows(IllegalArgumentException.class, ()->Version.parse("ver6.7"));
	}
}
