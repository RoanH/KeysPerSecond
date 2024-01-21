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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import dev.roanh.kps.CommandKeys;
import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.group.AveragePanelSettings;
import dev.roanh.kps.config.group.CommandSettings;
import dev.roanh.kps.config.group.CurrentPanelSettings;
import dev.roanh.kps.config.group.CursorGraphSettings;
import dev.roanh.kps.config.group.GraphPanelSettings;
import dev.roanh.kps.config.group.LineGraphSettings;
import dev.roanh.kps.config.group.KeyPanelSettings;
import dev.roanh.kps.config.group.LastPanelSettings;
import dev.roanh.kps.config.group.MaxPanelSettings;
import dev.roanh.kps.config.group.PositionSettings;
import dev.roanh.kps.config.group.SpecialPanelSettings;
import dev.roanh.kps.config.group.StatsSavingSettings;
import dev.roanh.kps.config.group.ThemeSettings;
import dev.roanh.kps.config.group.TotalPanelSettings;
import dev.roanh.kps.config.setting.CommandKeySetting;

public class ConfigParserTest{
	//note: in various places we assume the iterator order is consistent with the config but this isn't strictly required and may change in the future
	
	@Test
	public void fullTest1() throws IOException{
		ConfigParser parser = ConfigParser.parse(Paths.get("test/legacy.kps"));
		assertFalse(parser.wasDefaultUsed());
		assertEquals(Version.UNKNOWN, parser.getVersion());
		
		Configuration config = parser.getConfig();

		//general
		assertFalse(config.isOverlayMode());
		assertFalse(config.isTrackAllKeys());
		assertFalse(config.isTrackAllButtons());
		assertEquals(100, config.getUpdateRateMs());
		assertFalse(config.isKeyModifierTrackingEnabled());
		assertFalse(config.isWindowedMode());
		
		//position
		PositionSettings pos = config.getFramePosition();
		assertTrue(pos.hasPosition());
		assertEquals(new Point(100, 90), pos.getLocation());
		
		//theme
		ThemeSettings theme = config.getTheme();
		assertFalse(theme.hasCustomColors());
		theme.setCustomColorsEnabled(true);
		assertEquals(new ThemeColor(0, 255, 255, 255), theme.getForeground());
		assertEquals(new ThemeColor(0, 0, 0, 255), theme.getBackground());
		
		//commands
		CommandKeys.isAltDown = false;
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
		
		//layout
		assertEquals(22, config.getCellSize());
		assertEquals(2, config.getBorderOffset());
		
		//stats
		StatsSavingSettings stats = config.getStatsSavingSettings();
		assertFalse(stats.isAutoSaveEnabled());
		assertEquals("C:\\Users\\RoanH", stats.getAutoSaveDestination());
		assertEquals("'kps stats' yyyy-MM-dd HH.mm.ss'.kpsstats'", stats.getAutoSaveFormat());
		assertEquals(600000, stats.getAutoSaveInterval());
		assertFalse(stats.isSaveOnExitEnabled());
		assertFalse(stats.isLoadOnLaunchEnabled());
		
		//graph
		assertEquals(1, config.getGraphs().size());
		LineGraphSettings graph = assertInstanceOf(LineGraphSettings.class, config.getGraphs().get(0));
		assertEquals(0, graph.getLayoutX());
		assertEquals(-1, graph.getLayoutY());
		assertEquals(40, graph.getLayoutWidth());
		assertEquals(7, graph.getLayoutHeight());
		assertTrue(graph.isAverageVisible());
		assertEquals(180000, graph.getBacklog());
		assertEquals(Integer.MAX_VALUE, graph.getMaxValue());
		
		//special panels
		Iterator<SpecialPanelSettings> panels = config.getPanels().iterator();
		
		MaxPanelSettings maxSettings = assertInstanceOf(MaxPanelSettings.class, panels.next());
		assertEquals(23, maxSettings.getLayoutX());
		assertEquals(0, maxSettings.getLayoutY());
		assertEquals(2, maxSettings.getLayoutWidth());
		assertEquals(3, maxSettings.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, maxSettings.getRenderingMode());
		assertEquals("MAX", maxSettings.getName());
		
		AveragePanelSettings avgSettings = assertInstanceOf(AveragePanelSettings.class, panels.next());
		assertEquals(21, avgSettings.getLayoutX());
		assertEquals(0, avgSettings.getLayoutY());
		assertEquals(2, avgSettings.getLayoutWidth());
		assertEquals(3, avgSettings.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, avgSettings.getRenderingMode());
		assertEquals("AVG", avgSettings.getName());
		assertEquals(0, avgSettings.getPrecision());
		
		CurrentPanelSettings curSettings = assertInstanceOf(CurrentPanelSettings.class, panels.next());
		assertEquals(25, curSettings.getLayoutX());
		assertEquals(0, curSettings.getLayoutY());
		assertEquals(2, curSettings.getLayoutWidth());
		assertEquals(3, curSettings.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, curSettings.getRenderingMode());
		assertEquals("KPS", curSettings.getName());//technically 'CUR' historically
		
		TotalPanelSettings totSettings = assertInstanceOf(TotalPanelSettings.class, panels.next());
		assertEquals(25, totSettings.getLayoutX());
		assertEquals(0, totSettings.getLayoutY());
		assertEquals(2, totSettings.getLayoutWidth());
		assertEquals(3, totSettings.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, totSettings.getRenderingMode());
		assertEquals("TOT", totSettings.getName());
		
		assertFalse(panels.hasNext());
		
		//keys
		SettingList<KeyPanelSettings> keys = config.getKeys();
		assertEquals(6, keys.size());
		
		KeyPanelSettings key1 = keys.get(0);
		assertEquals(1048606, key1.getKeyCode());
		assertEquals(13, key1.getLayoutX());
		assertEquals(0, key1.getLayoutY());
		assertEquals(2, key1.getLayoutWidth());
		assertEquals(3, key1.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, key1.getRenderingMode());
		assertTrue(key1.isVisible());
		assertEquals("A", key1.getName());
		
		KeyPanelSettings key2 = keys.get(1);
		assertEquals(1048607, key2.getKeyCode());
		assertEquals(15, key2.getLayoutX());
		assertEquals(0, key2.getLayoutY());
		assertEquals(2, key2.getLayoutWidth());
		assertEquals(3, key2.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, key2.getRenderingMode());
		assertTrue(key2.isVisible());
		assertEquals("S", key2.getName());
		
		KeyPanelSettings key3 = keys.get(2);
		assertEquals(1048615, key3.getKeyCode());
		assertEquals(17, key3.getLayoutX());
		assertEquals(0, key3.getLayoutY());
		assertEquals(2, key3.getLayoutWidth());
		assertEquals(3, key3.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, key3.getRenderingMode());
		assertTrue(key3.isVisible());
		assertEquals(";", key3.getName());
		
		KeyPanelSettings key4 = keys.get(3);
		assertEquals(1048616, key4.getKeyCode());
		assertEquals(19, key4.getLayoutX());
		assertEquals(0, key4.getLayoutY());
		assertEquals(2, key4.getLayoutWidth());
		assertEquals(3, key4.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, key4.getRenderingMode());
		assertTrue(key4.isVisible());
		assertEquals("\"", key4.getName());
		
		KeyPanelSettings key5 = keys.get(4);
		assertEquals(1048593, key5.getKeyCode());
		assertEquals(11, key5.getLayoutX());
		assertEquals(0, key5.getLayoutY());
		assertEquals(2, key5.getLayoutWidth());
		assertEquals(3, key5.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, key5.getRenderingMode());
		assertTrue(key5.isVisible());
		assertEquals("W", key5.getName());
		
		KeyPanelSettings key6 = keys.get(5);
		assertEquals(1048594, key6.getKeyCode());
		assertEquals(9, key6.getLayoutX());
		assertEquals(0, key6.getLayoutY());
		assertEquals(2, key6.getLayoutWidth());
		assertEquals(3, key6.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, key6.getRenderingMode());
		assertTrue(key6.isVisible());
		assertEquals("E", key6.getName());
	}
	
	@Test
	public void fullTest2() throws IOException{
		ConfigParser parser = ConfigParser.parse(Paths.get("test/config88.kps"));
		assertFalse(parser.wasDefaultUsed());
		assertEquals(new Version(8, 8), parser.getVersion());
		
		Configuration config = parser.getConfig();
		
		//general
		assertFalse(config.isOverlayMode());
		assertFalse(config.isTrackAllKeys());
		assertFalse(config.isTrackAllButtons());
		assertEquals(100, config.getUpdateRateMs());
		assertFalse(config.isKeyModifierTrackingEnabled());
		assertFalse(config.isWindowedMode());
		
		//position
		PositionSettings pos = config.getFramePosition();
		assertTrue(pos.hasPosition());
		assertEquals(new Point(100, 90), pos.getLocation());
		
		//theme
		ThemeSettings theme = config.getTheme();
		assertFalse(theme.hasCustomColors());
		theme.setCustomColorsEnabled(true);
		assertEquals(new ThemeColor(0, 255, 255, 255), theme.getForeground());
		assertEquals(new ThemeColor(0, 0, 0, 255), theme.getBackground());

		//commands
		CommandKeys.isAltDown = false;
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
		
		//layout
		assertEquals(22, config.getCellSize());
		assertEquals(2, config.getBorderOffset());

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
		assertEquals(1, config.getGraphs().size());
		LineGraphSettings graph = assertInstanceOf(LineGraphSettings.class, config.getGraphs().get(0));
		assertEquals(1, graph.getLayoutX());
		assertEquals(2, graph.getLayoutY());
		assertEquals(5, graph.getLayoutWidth());
		assertEquals(8, graph.getLayoutHeight());
		assertFalse(graph.isAverageVisible());
		assertEquals(4500, graph.getBacklog());
		assertEquals(Integer.MAX_VALUE, graph.getMaxValue());
		
		//special panels
		Iterator<SpecialPanelSettings> panels = config.getPanels().iterator();
		
		MaxPanelSettings maxSettings = assertInstanceOf(MaxPanelSettings.class, panels.next());
		assertEquals(6, maxSettings.getLayoutX());
		assertEquals(3, maxSettings.getLayoutY());
		assertEquals(5, maxSettings.getLayoutWidth());
		assertEquals(1, maxSettings.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, maxSettings.getRenderingMode());
		assertEquals("NMAX", maxSettings.getName());
		
		AveragePanelSettings avgSettings = assertInstanceOf(AveragePanelSettings.class, panels.next());
		assertEquals(9, avgSettings.getLayoutX());
		assertEquals(8, avgSettings.getLayoutY());
		assertEquals(7, avgSettings.getLayoutWidth());
		assertEquals(6, avgSettings.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, avgSettings.getRenderingMode());
		assertEquals("average", avgSettings.getName());
		assertEquals(2, avgSettings.getPrecision());
		
		CurrentPanelSettings curSettings = assertInstanceOf(CurrentPanelSettings.class, panels.next());
		assertEquals(0, curSettings.getLayoutX());
		assertEquals(9, curSettings.getLayoutY());
		assertEquals(4, curSettings.getLayoutWidth());
		assertEquals(5, curSettings.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, curSettings.getRenderingMode());
		assertEquals("kps", curSettings.getName());
		
		TotalPanelSettings totSettings = assertInstanceOf(TotalPanelSettings.class, panels.next());
		assertEquals(1, totSettings.getLayoutX());
		assertEquals(1, totSettings.getLayoutY());
		assertEquals(2, totSettings.getLayoutWidth());
		assertEquals(2, totSettings.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, totSettings.getRenderingMode());
		assertEquals("Total Panel", totSettings.getName());
		
		assertFalse(panels.hasNext());
		
		//keys
		SettingList<KeyPanelSettings> keys = config.getKeys();
		assertEquals(2, keys.size());
		
		KeyPanelSettings key1 = keys.get(0);
		assertEquals(1048606, key1.getKeyCode());
		assertEquals(13, key1.getLayoutX());
		assertEquals(0, key1.getLayoutY());
		assertEquals(2, key1.getLayoutWidth());
		assertEquals(3, key1.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, key1.getRenderingMode());
		assertTrue(key1.isVisible());
		assertEquals("A", key1.getName());
		
		KeyPanelSettings key2 = keys.get(1);
		assertEquals(1048607, key2.getKeyCode());
		assertEquals(12, key2.getLayoutX());
		assertEquals(9, key2.getLayoutY());
		assertEquals(6, key2.getLayoutWidth());
		assertEquals(7, key2.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, key2.getRenderingMode());
		assertFalse(key2.isVisible());
		assertEquals("B", key2.getName());
	}
	
	@Test
	public void fullTest3() throws IOException{
		ConfigParser parser = ConfigParser.parse(Paths.get("test/config87nodefault.kps"));
		assertFalse(parser.wasDefaultUsed());
		assertEquals(new Version(8, 7), parser.getVersion());
		
		Configuration config = parser.getConfig();
		
		//general
		assertTrue(config.isOverlayMode());
		assertTrue(config.isTrackAllKeys());
		assertTrue(config.isTrackAllButtons());
		assertEquals(250, config.getUpdateRateMs());
		assertTrue(config.isKeyModifierTrackingEnabled());
		assertFalse(config.isWindowedMode());
		
		//position
		assertFalse(config.getFramePosition().hasPosition());
		
		//theme
		ThemeSettings theme = config.getTheme();
		assertTrue(theme.hasCustomColors());
		assertEquals(new ThemeColor(1, 2, 3, 0), theme.getForeground());
		assertEquals(new ThemeColor(104, 105, 106, 0.5F), theme.getBackground());

		//commands
		CommandKeys.isAltDown = true;
		CommandKeys.isCtrlDown = false;
		CommandSettings commands = config.getCommands();

		CommandKeySetting cmd = commands.getCommandResetStats();
		assertNull(cmd.getValue());
		assertFalse(cmd.matches(25));
		assertEquals("Unbound", cmd.toDisplayString());

		cmd = commands.getCommandExit();
		assertTrue(cmd.matches(2));
		assertTrue(CommandKeys.hasAlt(cmd.getValue()));
		assertFalse(CommandKeys.hasCtrl(cmd.getValue()));

		cmd = commands.getCommandResetTotals();
		assertTrue(cmd.matches(3));
		assertTrue(CommandKeys.hasAlt(cmd.getValue()));
		assertFalse(CommandKeys.hasCtrl(cmd.getValue()));

		cmd = commands.getCommandHide();
		assertTrue(cmd.matches(4));
		assertTrue(CommandKeys.hasAlt(cmd.getValue()));
		assertFalse(CommandKeys.hasCtrl(cmd.getValue()));

		cmd = commands.getCommandPause();
		assertTrue(cmd.matches(5));
		assertTrue(CommandKeys.hasAlt(cmd.getValue()));
		assertFalse(CommandKeys.hasCtrl(cmd.getValue()));

		cmd = commands.getCommandReload();
		assertTrue(cmd.matches(6));
		assertTrue(CommandKeys.hasAlt(cmd.getValue()));
		assertFalse(CommandKeys.hasCtrl(cmd.getValue()));
		
		//layout
		assertEquals(34, config.getCellSize());
		assertEquals(4, config.getBorderOffset());

		//stats
		StatsSavingSettings stats = config.getStatsSavingSettings();
		assertTrue(stats.isAutoSaveEnabled());
		assertEquals("C:\\Users\\test", stats.getAutoSaveDestination());
		assertEquals("test", stats.getAutoSaveFormat());
		assertEquals(100, stats.getAutoSaveInterval());
		assertTrue(stats.isSaveOnExitEnabled());
		assertTrue(stats.isLoadOnLaunchEnabled());
		assertEquals("C:\\Users\\RoanH\\alsotest", stats.getSaveFile());
		
		//graphs
		assertEquals(0, config.getGraphs().size());
		
		//special panels
		assertEquals(0, config.getPanels().size());
		
		//keys
		SettingList<KeyPanelSettings> keys = config.getKeys();
		assertEquals(2, keys.size());
		
		KeyPanelSettings key1 = keys.get(0);
		assertEquals(1, key1.getKeyCode());
		assertEquals(3, key1.getLayoutX());
		assertEquals(5, key1.getLayoutY());
		assertEquals(7, key1.getLayoutWidth());
		assertEquals(10, key1.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, key1.getRenderingMode());
		assertFalse(key1.isVisible());
		assertEquals("R", key1.getName());
		
		KeyPanelSettings key2 = keys.get(1);
		assertEquals(2, key2.getKeyCode());
		assertEquals(4, key2.getLayoutX());
		assertEquals(6, key2.getLayoutY());
		assertEquals(8, key2.getLayoutWidth());
		assertEquals(11, key2.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, key2.getRenderingMode());
		assertTrue(key2.isVisible());
		assertEquals("S", key2.getName());
	}
	
	@Test
	public void fullTest4() throws IOException{
		ConfigParser parser = ConfigParser.parse(Paths.get("test/latestnodefault.kps"));
		assertFalse(parser.wasDefaultUsed());
		assertEquals(new Version(8, 9), parser.getVersion());
		
		Configuration config = parser.getConfig();
		
		//general
		assertTrue(config.isOverlayMode());
		assertTrue(config.isTrackAllKeys());
		assertTrue(config.isTrackAllButtons());
		assertEquals(250, config.getUpdateRateMs());
		assertTrue(config.isKeyModifierTrackingEnabled());
		assertTrue(config.isWindowedMode());
		
		//position
		assertFalse(config.getFramePosition().hasPosition());
		
		//theme
		ThemeSettings theme = config.getTheme();
		assertTrue(theme.hasCustomColors());
		assertEquals(new ThemeColor(1, 2, 3, 0), theme.getForeground());
		assertEquals(new ThemeColor(104, 105, 106, 0), theme.getBackground());

		//commands
		CommandKeys.isAltDown = true;
		CommandKeys.isCtrlDown = false;
		CommandSettings commands = config.getCommands();

		CommandKeySetting cmd = commands.getCommandResetStats();
		assertNull(cmd.getValue());
		assertFalse(cmd.matches(25));
		assertEquals("Unbound", cmd.toDisplayString());

		cmd = commands.getCommandExit();
		assertTrue(cmd.matches(2));
		assertTrue(CommandKeys.hasAlt(cmd.getValue()));
		assertFalse(CommandKeys.hasCtrl(cmd.getValue()));

		cmd = commands.getCommandResetTotals();
		assertTrue(cmd.matches(3));
		assertTrue(CommandKeys.hasAlt(cmd.getValue()));
		assertFalse(CommandKeys.hasCtrl(cmd.getValue()));

		cmd = commands.getCommandHide();
		assertTrue(cmd.matches(4));
		assertTrue(CommandKeys.hasAlt(cmd.getValue()));
		assertFalse(CommandKeys.hasCtrl(cmd.getValue()));

		cmd = commands.getCommandPause();
		assertTrue(cmd.matches(5));
		assertTrue(CommandKeys.hasAlt(cmd.getValue()));
		assertFalse(CommandKeys.hasCtrl(cmd.getValue()));

		cmd = commands.getCommandReload();
		assertTrue(cmd.matches(6));
		assertTrue(CommandKeys.hasAlt(cmd.getValue()));
		assertFalse(CommandKeys.hasCtrl(cmd.getValue()));
		
		//layout
		assertEquals(34, config.getCellSize());
		assertEquals(4, config.getBorderOffset());

		//stats
		StatsSavingSettings stats = config.getStatsSavingSettings();
		assertTrue(stats.isAutoSaveEnabled());
		assertEquals("C:\\Users\\test", stats.getAutoSaveDestination());
		assertEquals("test", stats.getAutoSaveFormat());
		assertEquals(100, stats.getAutoSaveInterval());
		assertTrue(stats.isSaveOnExitEnabled());
		assertTrue(stats.isLoadOnLaunchEnabled());
		assertEquals("C:\\Users\\RoanH\\alsotest", stats.getSaveFile());
		
		//graphs
		Iterator<GraphPanelSettings> graphs = config.getGraphs().iterator();
		
		LineGraphSettings lineGraph = assertInstanceOf(LineGraphSettings.class, graphs.next());
		assertEquals(1, lineGraph.getLayoutX());
		assertEquals(2, lineGraph.getLayoutY());
		assertEquals(5, lineGraph.getLayoutWidth());
		assertEquals(8, lineGraph.getLayoutHeight());
		assertFalse(lineGraph.isAverageVisible());
		assertEquals(45, lineGraph.getBacklog());
		assertEquals(20, lineGraph.getMaxValue());
		
		CursorGraphSettings cursorGraph = assertInstanceOf(CursorGraphSettings.class, graphs.next());
		assertEquals(5, cursorGraph.getLayoutX());
		assertEquals(4, cursorGraph.getLayoutY());
		assertEquals(8, cursorGraph.getLayoutWidth());
		assertEquals(7, cursorGraph.getLayoutHeight());
		assertEquals("\\Display1", cursorGraph.getDisplayId());
		assertEquals(1234, cursorGraph.getBacklog());
		
		assertFalse(graphs.hasNext());
		
		//special panels
		Iterator<SpecialPanelSettings> panels = config.getPanels().iterator();
		
		MaxPanelSettings maxSettings = assertInstanceOf(MaxPanelSettings.class, panels.next());
		assertEquals(6, maxSettings.getLayoutX());
		assertEquals(3, maxSettings.getLayoutY());
		assertEquals(5, maxSettings.getLayoutWidth());
		assertEquals(1, maxSettings.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, maxSettings.getRenderingMode());
		assertEquals("NMAX", maxSettings.getName());
		
		AveragePanelSettings avgSettings = assertInstanceOf(AveragePanelSettings.class, panels.next());
		assertEquals(9, avgSettings.getLayoutX());
		assertEquals(8, avgSettings.getLayoutY());
		assertEquals(7, avgSettings.getLayoutWidth());
		assertEquals(6, avgSettings.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, avgSettings.getRenderingMode());
		assertEquals("average", avgSettings.getName());
		assertEquals(2, avgSettings.getPrecision());
		
		CurrentPanelSettings curSettings = assertInstanceOf(CurrentPanelSettings.class, panels.next());
		assertEquals(0, curSettings.getLayoutX());
		assertEquals(9, curSettings.getLayoutY());
		assertEquals(4, curSettings.getLayoutWidth());
		assertEquals(5, curSettings.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, curSettings.getRenderingMode());
		assertEquals("kps", curSettings.getName());//technically 'CUR' historically
		
		TotalPanelSettings totSettings = assertInstanceOf(TotalPanelSettings.class, panels.next());
		assertEquals(1, totSettings.getLayoutX());
		assertEquals(1, totSettings.getLayoutY());
		assertEquals(5, totSettings.getLayoutWidth());
		assertEquals(2, totSettings.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, totSettings.getRenderingMode());
		assertEquals("Total Panel", totSettings.getName());
		
		LastPanelSettings lastSettings = assertInstanceOf(LastPanelSettings.class, panels.next());
		assertEquals(66, lastSettings.getLayoutX());
		assertEquals(77, lastSettings.getLayoutY());
		assertEquals(3, lastSettings.getLayoutWidth());
		assertEquals(6, lastSettings.getLayoutHeight());
		assertEquals(RenderingMode.DIAGONAL1, lastSettings.getRenderingMode());
		assertEquals("Last", lastSettings.getName());
		assertEquals(2, lastSettings.getUnitCount());
		assertTrue(lastSettings.showMillis());
		
		assertFalse(panels.hasNext());
		
		//keys
		SettingList<KeyPanelSettings> keys = config.getKeys();
		assertEquals(2, keys.size());
		
		KeyPanelSettings key1 = keys.get(0);
		assertEquals(1048606, key1.getKeyCode());
		assertEquals(13, key1.getLayoutX());
		assertEquals(0, key1.getLayoutY());
		assertEquals(2, key1.getLayoutWidth());
		assertEquals(3, key1.getLayoutHeight());
		assertEquals(RenderingMode.VALUE_ONLY, key1.getRenderingMode());
		assertTrue(key1.isVisible());
		assertEquals("A", key1.getName());
		
		KeyPanelSettings key2 = keys.get(1);
		assertEquals(1048607, key2.getKeyCode());
		assertEquals(12, key2.getLayoutX());
		assertEquals(9, key2.getLayoutY());
		assertEquals(6, key2.getLayoutWidth());
		assertEquals(7, key2.getLayoutHeight());
		assertEquals(RenderingMode.VERTICAL, key2.getRenderingMode());
		assertFalse(key2.isVisible());
		assertEquals("B", key2.getName());
	}
	
	@Test
	public void readWriteTest() throws IOException{
		ConfigParser parser = ConfigParser.parse(Paths.get("test/latestnodefault.kps"));
		assertFalse(parser.wasDefaultUsed());
		assertEquals(new Version(8, 9), parser.getVersion());
		
		Configuration config = parser.getConfig();
		StringWriter buf = new StringWriter();
		config.write(new IndentWriter(new PrintWriter(buf)), false);
		
		String original = new String(Files.readAllBytes(Paths.get("test/latestnodefault.kps")), StandardCharsets.UTF_8);
		assertEquals("version: " + Main.VERSION + original.substring(13), buf.toString());
	}
}
