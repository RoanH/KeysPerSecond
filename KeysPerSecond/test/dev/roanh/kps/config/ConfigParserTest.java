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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import dev.roanh.kps.CommandKeys;
import dev.roanh.kps.GraphMode;
import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.group.AveragePanelSettings;
import dev.roanh.kps.config.group.CommandSettings;
import dev.roanh.kps.config.group.CurrentPanelSettings;
import dev.roanh.kps.config.group.GraphSettings;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.config.group.MaxPanelSettings;
import dev.roanh.kps.config.group.SpecialPanelSettings;
import dev.roanh.kps.config.group.StatsSavingSettings;
import dev.roanh.kps.config.group.TotalPanelSettings;
import dev.roanh.kps.config.setting.CommandKeySetting;

public class ConfigParserTest{
	//note: in various places we assume the iterator order is consistent with the config but this isn't strictly required and may change in the future
	
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
		assertFalse(config.isKeyModifierTrackingEnabled());
		
//		graph enable -- this one is not handled yet
		
//		custom colour
//		foreground col
//		background col
//		foreground op
//		background op
		
//		//commands
//		CommandKeys.isCtrlDown = true;
//		CommandSettings commands = config.getCommands();
//		
//		CommandKeySetting cmd = commands.getCommandResetStats();
//		assertEquals(1114137, cmd.getValue());
//		assertTrue(cmd.matches(25));
//		assertEquals("Ctrl + P", cmd.toDisplayString());
		
		//TODO probably test new format first
		
//		cmd reset stats
//		cmd exit
//		cmd reset totals
//		cmd hide
//		cmd pause
//		cmd reload
		
		
		//TODO layout (just borderoffset and cell size I guess now?)

		//commands
		CommandKeys.isCtrlDown = true;
		CommandSettings commands = config.getCommands();

		CommandKeySetting cmd = commands.getCommandResetStats();
		assertEquals(1114137, cmd.getValue());
		assertTrue(cmd.matches(25));
		assertEquals("Ctrl + P", cmd.toDisplayString());

		cmd = commands.getCommandExit();
		assertEquals(1114134, cmd.getValue());
		assertTrue(cmd.matches(22));
		assertEquals("Ctrl + U", cmd.toDisplayString());

		cmd = commands.getCommandResetTotals();
		assertEquals(1114135, cmd.getValue());
		assertTrue(cmd.matches(23));
		assertEquals("Ctrl + I", cmd.toDisplayString());
		
		cmd = commands.getCommandHide();
		assertEquals(1114133, cmd.getValue());
		assertTrue(cmd.matches(21));
		assertEquals("Ctrl + Y", cmd.toDisplayString());
		
		cmd = commands.getCommandPause();
		assertEquals(1114132, cmd.getValue());
		assertTrue(cmd.matches(20));
		assertEquals("Ctrl + T", cmd.toDisplayString());
		
		cmd = commands.getCommandReload();
		assertEquals(1114131, cmd.getValue());
		assertTrue(cmd.matches(19));
		assertEquals("Ctrl + R", cmd.toDisplayString());
		
		//stats
		StatsSavingSettings stats = config.getStatsSavingSettings();
		assertFalse(stats.isAutoSaveEnabled());
		assertEquals("C:\\Users\\RoanH", stats.getAutoSaveDestination());
		assertEquals("'kps stats' yyyy-MM-dd HH.mm.ss'.kpsstats'", stats.getAutoSaveFormat());
		assertEquals(600000, stats.getAutoSaveInterval());
		assertFalse(stats.isSaveOnExitEnabled());
		assertFalse(stats.isLoadOnLaunchEnabled());
		
		//graph
		GraphSettings graph = config.getGraphSettings();
		assertEquals(0, graph.getX());
		assertEquals(-1, graph.getY());
		assertEquals(40, graph.getWidth());
		assertEquals(7, graph.getHeight());
		assertEquals(GraphMode.INLINE, graph.getGraphMode());
		assertTrue(graph.isAverageVisible());
		assertEquals(1800, graph.getBacklog());
		
		//special panels
		Iterator<SpecialPanelSettings> panels = config.getPanels().iterator();
		
		MaxPanelSettings maxSettings = assertInstanceOf(MaxPanelSettings.class, panels.next());
		assertEquals(23, maxSettings.getX());
		assertEquals(0, maxSettings.getY());
		assertEquals(2, maxSettings.getWidth());
		assertEquals(3, maxSettings.getHeight());
		assertEquals(RenderingMode.VERTICAL, maxSettings.getRenderingMode());
		assertEquals("MAX", maxSettings.getName());
		
		AveragePanelSettings avgSettings = assertInstanceOf(AveragePanelSettings.class, panels.next());
		assertEquals(21, avgSettings.getX());
		assertEquals(0, avgSettings.getY());
		assertEquals(2, avgSettings.getWidth());
		assertEquals(3, avgSettings.getHeight());
		assertEquals(RenderingMode.VERTICAL, avgSettings.getRenderingMode());
		assertEquals("AVG", avgSettings.getName());
		assertEquals(0, avgSettings.getPrecision());
		
		CurrentPanelSettings curSettings = assertInstanceOf(CurrentPanelSettings.class, panels.next());
		assertEquals(25, curSettings.getX());
		assertEquals(0, curSettings.getY());
		assertEquals(2, curSettings.getWidth());
		assertEquals(3, curSettings.getHeight());
		assertEquals(RenderingMode.VERTICAL, curSettings.getRenderingMode());
		assertEquals("KPS", curSettings.getName());//technically 'CUR' historically
		
		TotalPanelSettings totSettings = assertInstanceOf(TotalPanelSettings.class, panels.next());
		assertEquals(25, totSettings.getX());
		assertEquals(0, totSettings.getY());
		assertEquals(2, totSettings.getWidth());
		assertEquals(3, totSettings.getHeight());
		assertEquals(RenderingMode.VERTICAL, totSettings.getRenderingMode());
		assertEquals("TOT", totSettings.getName());
		
		assertFalse(panels.hasNext());
		
		//keys
		SettingList<KeyPanelSettings> keys = config.getKeySettings();
		assertEquals(6, keys.size());
		
		KeyPanelSettings key1 = keys.get(0);
		assertEquals(1048606, key1.getKeyCode());
		assertEquals(13, key1.getX());
		assertEquals(0, key1.getY());
		assertEquals(2, key1.getWidth());
		assertEquals(3, key1.getHeight());
		assertEquals(RenderingMode.VERTICAL, key1.getRenderingMode());
		assertTrue(key1.isVisible());
		assertEquals("A", key1.getName());
		
		KeyPanelSettings key2 = keys.get(1);
		assertEquals(1048607, key2.getKeyCode());
		assertEquals(15, key2.getX());
		assertEquals(0, key2.getY());
		assertEquals(2, key2.getWidth());
		assertEquals(3, key2.getHeight());
		assertEquals(RenderingMode.VERTICAL, key2.getRenderingMode());
		assertTrue(key2.isVisible());
		assertEquals("S", key2.getName());
		
		KeyPanelSettings key3 = keys.get(2);
		assertEquals(1048615, key3.getKeyCode());
		assertEquals(17, key3.getX());
		assertEquals(0, key3.getY());
		assertEquals(2, key3.getWidth());
		assertEquals(3, key3.getHeight());
		assertEquals(RenderingMode.VERTICAL, key3.getRenderingMode());
		assertTrue(key3.isVisible());
		assertEquals(";", key3.getName());
		
		KeyPanelSettings key4 = keys.get(3);
		assertEquals(1048616, key4.getKeyCode());
		assertEquals(19, key4.getX());
		assertEquals(0, key4.getY());
		assertEquals(2, key4.getWidth());
		assertEquals(3, key4.getHeight());
		assertEquals(RenderingMode.VERTICAL, key4.getRenderingMode());
		assertTrue(key4.isVisible());
		assertEquals("\"", key4.getName());
		
		KeyPanelSettings key5 = keys.get(4);
		assertEquals(1048593, key5.getKeyCode());
		assertEquals(11, key5.getX());
		assertEquals(0, key5.getY());
		assertEquals(2, key5.getWidth());
		assertEquals(3, key5.getHeight());
		assertEquals(RenderingMode.VERTICAL, key5.getRenderingMode());
		assertTrue(key5.isVisible());
		assertEquals("W", key5.getName());
		
		KeyPanelSettings key6 = keys.get(5);
		assertEquals(1048594, key6.getKeyCode());
		assertEquals(9, key6.getX());
		assertEquals(0, key6.getY());
		assertEquals(2, key6.getWidth());
		assertEquals(3, key6.getHeight());
		assertEquals(RenderingMode.VERTICAL, key6.getRenderingMode());
		assertTrue(key6.isVisible());
		assertEquals("E", key6.getName());
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
		
		//graphs
		GraphSettings graph = config.getGraphSettings();
		assertEquals(1, graph.getX());
		assertEquals(2, graph.getY());
		assertEquals(5, graph.getWidth());
		assertEquals(8, graph.getHeight());
		assertEquals(GraphMode.DETACHED, graph.getGraphMode());
		assertFalse(graph.isAverageVisible());
		assertEquals(45, graph.getBacklog());
		
		//special panels
		Iterator<SpecialPanelSettings> panels = config.getPanels().iterator();
		
		MaxPanelSettings maxSettings = assertInstanceOf(MaxPanelSettings.class, panels.next());
		assertEquals(6, maxSettings.getX());
		assertEquals(3, maxSettings.getY());
		assertEquals(5, maxSettings.getWidth());
		assertEquals(1, maxSettings.getHeight());
		assertEquals(RenderingMode.VALUE_ONLY, maxSettings.getRenderingMode());
		assertEquals("NMAX", maxSettings.getName());
		
		AveragePanelSettings avgSettings = assertInstanceOf(AveragePanelSettings.class, panels.next());
		assertEquals(9, avgSettings.getX());
		assertEquals(8, avgSettings.getY());
		assertEquals(7, avgSettings.getWidth());
		assertEquals(6, avgSettings.getHeight());
		assertEquals(RenderingMode.VALUE_ONLY, avgSettings.getRenderingMode());
		assertEquals("average", avgSettings.getName());
		assertEquals(2, avgSettings.getPrecision());
		
		CurrentPanelSettings curSettings = assertInstanceOf(CurrentPanelSettings.class, panels.next());
		assertEquals(0, curSettings.getX());
		assertEquals(9, curSettings.getY());
		assertEquals(4, curSettings.getWidth());
		assertEquals(5, curSettings.getHeight());
		assertEquals(RenderingMode.VALUE_ONLY, curSettings.getRenderingMode());
		assertEquals("kps", curSettings.getName());
		
		TotalPanelSettings totSettings = assertInstanceOf(TotalPanelSettings.class, panels.next());
		assertEquals(1, totSettings.getX());
		assertEquals(1, totSettings.getY());
		assertEquals(2, totSettings.getWidth());
		assertEquals(2, totSettings.getHeight());
		assertEquals(RenderingMode.VALUE_ONLY, totSettings.getRenderingMode());
		assertEquals("Total Panel", totSettings.getName());
		
		assertFalse(panels.hasNext());
	}
}
