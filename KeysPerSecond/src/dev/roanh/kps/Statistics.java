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
package dev.roanh.kps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.roanh.kps.config.group.StatsSavingSettings;
import dev.roanh.kps.panels.TotPanel;
import dev.roanh.util.Dialog;
import dev.roanh.util.FileSelector;
import dev.roanh.util.FileSelector.FileExtension;

/**
 * Class that handles most of the more complex
 * matters related to statistics.
 * @author Roan
 */
public class Statistics{
	/**
	 * Extension filter for KeysPerSecond statistics files.
	 */
	public static final FileExtension KPS_STATS_EXT = FileSelector.registerFileExtension("KeysPerSecond statistics", "kpsstats");
	/**
	 * Regex used to parse key lines in the statistics save file.
	 */
	private static final Pattern STATS_LINE_REGEX = Pattern.compile("^  - \\[keycode=(-?\\d+),count=(\\d+),alt=(true|false),ctrl=(true|false),shift=(true|false),name=\\\"(.*)\\\"]$");
	/**
	 * Statistics save future
	 */
	protected static ScheduledFuture<?> statsFuture = null;
	/**
	 * Periodic statistics save scheduler
	 */
	protected static ScheduledExecutorService statsScheduler = Executors.newSingleThreadScheduledExecutor();

	/**
	 * Saves the statistics so to the configured
	 * save file if stats saving on exit is enabled.
	 */
	public static void saveStatsOnExit(){
		if(Main.config.getStatsSavingSettings().isSaveOnExitEnabled()){
			try{
				saveStats(Paths.get(Main.config.getStatsSavingSettings().getSaveFile()));
			}catch(IOException e){
				e.printStackTrace();
				if(Dialog.showConfirmDialog("Failed to save statistics on exit.\nCause: " + e.getMessage() + "\nAttempt to save again?")){
					saveStatsOnExit();
				}
			}
		}
	}
	
	/**
	 * Cancels the automatic statistics saving task
	 */
	public static void cancelScheduledTask(){
		if(statsFuture != null){
			statsFuture.cancel(false);
		}
	}
	
	/**
	 * Starts the statistics saving task or cancels the current
	 * one and stars a new one.
	 */
	public static void saveStatsTask(){
		cancelScheduledTask();
		
		StatsSavingSettings config = Main.config.getStatsSavingSettings();
		statsFuture = statsScheduler.scheduleAtFixedRate(()->{
			try{
				Path target = Paths.get(config.getAutoSaveDestination());
				Files.createDirectories(target);
				target.resolve(DateTimeFormatter.ofPattern(config.getAutoSaveFormat()).withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(Instant.now(Clock.systemDefaultZone())));
				saveStats(target);
			}catch(Exception e){
				//Main priority here is to not interrupt whatever the user is doing
				e.printStackTrace();
			}
		}, config.getAutoSaveInterval(), config.getAutoSaveInterval(), TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Saves the statistics logged so far
	 * and asks the user to provide a location
	 * to save to
	 */
	protected static void saveStats(){
		Path file = Dialog.showFileSaveDialog(KPS_STATS_EXT, "stats");
		if(file != null){
			try{
				saveStats(file);
				Dialog.showMessageDialog("Statistics succesfully saved");
			}catch(IOException e){
				e.printStackTrace();
				Dialog.showErrorDialog("Failed to save the statistics!\nCause: " + e.getMessage());
			}
		}
	}

	/**
	 * Saves the statistics logged so far
	 * @param dest The file to save to
	 * @throws IOException When an IOException occurs.
	 */
	private static void saveStats(Path dest) throws IOException{
		try(PrintWriter out = new PrintWriter(Files.newBufferedWriter(dest, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING))){
			out.print("version: ");
			out.println(Main.VERSION);
			out.println();
			out.println("# General");
			out.print("total: ");
			out.println(TotPanel.hits);
			out.print("average: ");
			out.println(Main.avg);
			out.print("maximum: ");
			out.println(Main.max);
			out.print("seconds: ");
			out.println(Main.n);
			out.print("previous: ");
			out.println(Main.prev);
			out.print("current: ");
			out.println(Main.tmp.get());
			out.println();
			out.println("# Keys");
			out.println("keys:");
			for(Entry<Integer, Key> entry : Main.keys.entrySet()){
				Key key = entry.getValue();
				out.print("  - [keycode=");
				out.print(entry.getKey());
				out.print(",count=");
				out.print(key.count);
				out.print(",alt=");
				out.print(key.alt);
				out.print(",ctrl=");
				out.print(key.ctrl);
				out.print(",shift=");
				out.print(key.shift);
//				out.print(",name=\""); -- TODO no longer required going forward
//				out.print(key.name);
				out.println("\"]");
			}
			out.flush();
			out.close();
		}
	}
	
	/**
	 * Loads the statistics from a file, shows
	 * a prompt to the user for the file.
	 */
	protected static void loadStats(){
		Path file = Dialog.showFileOpenDialog(KPS_STATS_EXT);
		if(file == null){
			return;
		}
		
		try{
			loadStats(file);
			Dialog.showMessageDialog("Statistics succesfully loaded");
		}catch(Exception e){
			e.printStackTrace();
			Dialog.showErrorDialog("Failed to load the statistics!\nCause: " + e.getMessage());
		}
	}

	/**
	 * Loads the statistics from a file
	 * @param file The file to load from.
	 * @throws Exception When an Exception occurs.
	 */
	public static void loadStats(Path file) throws Exception{
		try(BufferedReader in = Files.newBufferedReader(file)){
			String line;
			while((line = in.readLine()) != null){
				if(line.startsWith("#") || line.isEmpty()){
					continue;
				}
				
				String[] args = line.split(":", 2);
				String value = args.length > 1 ? args[1].trim() : null;
				switch(args[0]){
				case "version":
					break;
				case "total":
					TotPanel.hits = Integer.parseInt(value);
					break;
				case "average":
					Main.avg = Double.parseDouble(value);
					break;
				case "maximum":
					Main.max = Integer.parseInt(value);
					break;
				case "seconds":
					Main.n = Long.parseLong(value);
					break;
				case "previous":
					Main.prev = Integer.parseInt(value);
					break;
				case "current":
					Main.tmp.set(Integer.parseInt(value));
					break;
				case "keys":
					while(true){
						in.mark(100);
						line = in.readLine();
						if(line == null){
							break;
						}
						
						Matcher m = STATS_LINE_REGEX.matcher(line);
						if(m.matches()){
							int code = Integer.parseInt(m.group(1));
							Key key = Main.keys.get(code);
							if(key == null){
								key = new Key(
//									m.group(6), -- legacy no longer a thing
									Integer.parseInt(m.group(2)),
									Boolean.parseBoolean(m.group(3)),
									Boolean.parseBoolean(m.group(4)),
									Boolean.parseBoolean(m.group(5))
								);
								Main.keys.put(code, key);
							}else{
								key.count = Integer.parseInt(m.group(2));
							}
						}else{
							in.reset();
							break;
						}
					}
					break;
				default:
					throw new IllegalArgumentException("Cannot parse line: " + line);
				}
			}
			in.close();
		}catch(MalformedInputException e){
			throw new UnsupportedOperationException("Loading legacy statistics files is unsupported in this version.", e);
		}catch(Exception e){
			throw e;
		}

		Main.frame.repaint();
	}
}
