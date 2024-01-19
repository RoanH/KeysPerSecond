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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedList;

import javax.swing.JPanel;

import dev.roanh.kps.config.group.MouseGraphSettings;
import dev.roanh.kps.config.group.PanelSettings;
import dev.roanh.kps.event.listener.MouseMoveListener;
import dev.roanh.kps.layout.LayoutPosition;

public class MouseGraphPanel extends BasePanel implements MouseMoveListener{//TODO remove listener
	public MouseGraphPanel(MouseGraphSettings config){
		super(config);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2604642433575841219L;
	public static final LinkedList<TimePoint> path = new LinkedList<TimePoint>();
	private volatile long lastPaint;

	@Override
	public void paintComponent(Graphics g){
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.CYAN);
		System.out.println("repaint");
		for(int i = 0; i < path.size() - 2; i++){
			//TODO use path2d
			g.drawLine(path.get(i).x / 10, path.get(i).y / 10, path.get(i + 1).x / 10, path.get(i + 1).y / 10);
			
			//TODO that div 10 is a nope
		}
	}
	
	//TODO private
	public void addPoint(int x, int y){
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

	@Override
	protected void render(Graphics2D g){
		// TODO Auto-generated method stub
		
	}
}
