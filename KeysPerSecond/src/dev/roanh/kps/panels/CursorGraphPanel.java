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
package dev.roanh.kps.panels;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.group.CursorGraphSettings;

/**
 * Graph showing the cursor movement.
 * @author Roan
 * @see CursorGraphSettings
 */
public class CursorGraphPanel extends GraphPanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -2604642433575841219L;
	/**
	 * List of recorded cursor snapshots.
	 */
	private final ConcurrentLinkedDeque<TimePoint> path = new ConcurrentLinkedDeque<TimePoint>();
	/**
	 * The graph configuration.
	 */
	private final CursorGraphSettings config;
	/**
	 * Rectangle with the dimensions and location of the tracked display.
	 */
	private final Rectangle display;
	
	/**
	 * Constructs a new cursor graph with the given settings.
	 * @param config The graph configuration.
	 */
	public CursorGraphPanel(CursorGraphSettings config){
		super(config);
		this.config = config;
		
		GraphicsDevice device = config.getDisplay();
		display = device == null ? null : device.getDefaultConfiguration().getBounds();
	}
	
	/**
	 * Adds a new cursor snapshot to the graph.
	 * @param x The cursor x coordinate.
	 * @param y The cursor y coordinate.
	 * @param time The snapshot time.
	 */
	private void addPoint(int x, int y, long time){
		if(display.contains(x, y)){
			path.addFirst(new TimePoint(x, y, time));
		}
	}
	
	/**
	 * Removes all cursor snapshots older than the configured backlog size.
	 * @param time The current time.
	 */
	private void removeExpired(long time){
		while(!path.isEmpty() && time - path.peekLast().time > config.getBacklog()){
			path.removeLast();
		}
	}
	
	@Override
	public void update(){
		long time = System.nanoTime() / 1000000;
		addPoint(Main.mouseLoc.x, Main.mouseLoc.y, time);
		removeExpired(time);
	}
	
	@Override
	public void reset(){
		path.clear();
		repaint();
	}
	
	@Override
	protected void render(Graphics2D g){
		if(display == null){
			//configured display was not found
			return;
		}

		//compute canvas dimensions
		final int left = RenderingMode.insideOffset + borderOffset;
		final int right = this.getWidth() - RenderingMode.insideOffset - borderOffset - 1;
		final int top = RenderingMode.insideOffset + borderOffset;
		final int bottom = this.getHeight() - RenderingMode.insideOffset - borderOffset;
		
		//prepare for drawing
		AffineTransform transform = g.getTransform();
		g.setClip(left, top, right - left + 1, bottom - top + 1);
		g.translate(left, top);
		
		//center the display on the canvas
		double fx = (right - left) / display.getWidth();
		double fy = (bottom - top) / display.getHeight();
		double f;
		if(fx < fy){
			f = fx;
			g.translate(0.0D, (bottom - top - display.getHeight() * f) / 2.0D);
		}else{
			f = fy;
			g.translate((right - left - display.getWidth() * f) / 2.0D, 0.0D);
		}
		
		//draw the cursor path
		Iterator<TimePoint> iter = path.iterator();
		if(iter.hasNext()){
			Path2D line = new Path2D.Double();
			
			TimePoint p = iter.next();
			g.translate(-display.x * f, -display.y * f);
			line.moveTo(p.x * f, p.y * f);
			while(iter.hasNext()){
				p = iter.next();
				line.lineTo(p.x * f, p.y * f);
			}
			
			g.setColor(foreground.getColor());
			g.draw(line);
		}
		
		//restore original state
		g.setClip(null);
		g.setTransform(transform);
	}
	
	/**
	 * Captured time point with a cursor snapshot.
	 * @author Roan
	 */
	private static class TimePoint{
		/**
		 * The cursor x coordinate.
		 */
		private final int x;
		/**
		 * The cursor y coordinate.
		 */
		private final int y;
		/**
		 * The time the snapshot was taken.
		 */
		private final long time;

		/**
		 * Constructs a new time point.
		 * @param x The cursor x coordinate.
		 * @param y The cursor y coordinate.
		 * @param time The snapshot time.
		 */
		private TimePoint(int x, int y, long time){
			this.x = x;
			this.y = y;
			this.time = time;
		}
	}
}
