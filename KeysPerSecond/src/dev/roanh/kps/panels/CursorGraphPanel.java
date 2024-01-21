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
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.group.CursorGraphSettings;
import dev.roanh.kps.event.listener.MouseMoveListener;

public class CursorGraphPanel extends BasePanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2604642433575841219L;
	private ConcurrentLinkedDeque<TimePoint> path = new ConcurrentLinkedDeque<TimePoint>();
	private CursorGraphSettings config;

	private volatile long lastPaint;
	
	private Rectangle display;
	
	public CursorGraphPanel(CursorGraphSettings config){
		super(config);
		this.config = config;
		display = config.getDisplay().getDefaultConfiguration().getBounds();
	}
	
	//TODO private
	public void addPoint(int x, int y){
		if(!display.contains(x, y)){
			return;
		}
		
		long time = System.currentTimeMillis();//TODO sys time may not be accurate, but it is fast
		path.addFirst(new TimePoint(x, y, time));
		while(time - path.getLast().time > config.getBacklog()){//todo config
			path.removeLast();
		}
		
//		if(time - lastPaint > 20){
//			lastPaint = time;
//			this.repaint();//TODO
//		}
	}
	
	@Override
	protected void render(Graphics2D g){
		if(display == null){
			//configured display was not found
			return;
		}
		
		
		
		int left = RenderingMode.insideOffset + borderOffset;
		int right = this.getWidth() - RenderingMode.insideOffset - borderOffset - 1;
		int top = RenderingMode.insideOffset + borderOffset;
		int bottom = this.getHeight() - RenderingMode.insideOffset - borderOffset;
		
		g.setClip(left, top, right - left + 1, bottom - top + 1);
		g.translate(left, top);
		
		
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
		
		//TODO remove
		g.setColor(foreground.getColor());
		g.drawRect(0, 0, (int)Math.round(display.width * f), (int)Math.round(display.height * f));
		
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
	}
	
	//TODO private?
	public static class TimePoint {
		//TODO private
		protected int x;
		protected int y;
		public long time;

		//TODO private
		public TimePoint(int x, int y, long time){
			this.x = x;
			this.y = y;
			this.time = time;
		}
	}
}
