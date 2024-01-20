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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.group.CursorGraphSettings;
import dev.roanh.kps.event.listener.MouseMoveListener;

public class CursorGraphPanel extends BasePanel implements MouseMoveListener{//TODO remove listener
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2604642433575841219L;
	private ConcurrentLinkedDeque<TimePoint> path = new ConcurrentLinkedDeque<TimePoint>();

	private volatile long lastPaint;
	
	private Rectangle display;
	
	//TODO remove
	private Rectangle display1;
	private Rectangle display2;
	private Rectangle display3;

	public CursorGraphPanel(CursorGraphSettings config){
		super(config);
		// TODO Auto-generated constructor stub
		
		
		GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		
		
		for(GraphicsDevice screen : screens){
			System.out.println("screen: " + screen.getIDstring() + " | " + screen.getDisplayMode().getWidth() + "x" + screen.getDisplayMode().getHeight());
		}
		
		
		display1 = screens[0].getDefaultConfiguration().getBounds();
		display2 = screens[1].getDefaultConfiguration().getBounds();
		display3 = screens[2].getDefaultConfiguration().getBounds();
		
		
		display = config.getDisplay().getDefaultConfiguration().getBounds();
	}
	
	//TODO private
	public void addPoint(int x, int y){
		if(!display.contains(x, y)){
			return;
		}
		
		long time = System.currentTimeMillis();//TODO sys time may not be accurate, but it is fast
		path.addFirst(new TimePoint(x, y, time));
		while(time - path.getLast().time > 1000){//todo config
			path.removeLast();
		}
		
		if(time - lastPaint > 20){
			lastPaint = time;
			this.repaint();//TODO
		}
	}
	
	@Override
	protected void render(Graphics2D g){
		if(display == null){
			//configured display was not found
			return;
		}
		
//		System.out.println("repaint");
		display = display1;

		
		
		//TODO clip account for out of bounds or just exclude these points on add later probably just clip them inside maybe?
		
		
		int left = RenderingMode.insideOffset + borderOffset;
		int right = this.getWidth() - RenderingMode.insideOffset - borderOffset - 1;
		int top = RenderingMode.insideOffset + borderOffset;
		int bottom = this.getHeight() - RenderingMode.insideOffset - borderOffset;
		
		g.setColor(Color.RED);
		g.drawLine(left, 25, right, 25);
		g.drawLine(10, top, 10, bottom);
		g.drawRect(left, top, right - left, bottom - top);
		
		g.setClip(left, top, right - left + 1, bottom - top + 1);
		g.translate(left, top);
		
		
		double fx = (right - left) / display.getWidth();
		double fy = (bottom - top) / display.getHeight();
		
//		System.out.println(fx + " and " + fy);
		
		double f;
		if(fx < fy){
			f = fx;
			g.translate(0.0D, (bottom - top - display.getHeight() * f) / 2.0D);
		}else{
			f = fy;
			g.translate((right - left - display.getWidth() * f) / 2.0D, 0.0D);
		}
		
		
		
		
		
		g.setColor(Color.GREEN);
		g.drawRect(0, 0, (int)(display.width * f), (int)(display.height * f));
		
//		g.translate(10, 10);
		
		
//		g.setClip(display);
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
		
		
		
//		for(int i = 0; i < path.size() - 2; i++){
//			//TODO use path2d
//			g.drawLine(path.get(i).x / 10, path.get(i).y / 10, path.get(i + 1).x / 10, path.get(i + 1).y / 10);
//			
//			//TODO that div 10 is a nope
//		}
	}

//	@Override
	public void onMouseMove(int x, int y){
//		addPoint(x, y);
	}

	@Override
	public int getLayoutX(){
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLayoutY(){
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int getLayoutWidth(){
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int getLayoutHeight(){
		// TODO Auto-generated method stub
		return 5;
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
