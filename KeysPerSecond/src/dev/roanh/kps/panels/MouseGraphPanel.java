package dev.roanh.kps.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedList;

import javax.swing.JPanel;

public class MouseGraphPanel extends JPanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2604642433575841219L;
	public static final LinkedList<TimePoint> path = new LinkedList<TimePoint>();

	@Override
	public void paintComponent(Graphics g){
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.CYAN);
		System.out.println("repaint");
		for(int i = 0; i < path.size() - 2; i++){
			g.drawLine(path.get(i).x / 10, path.get(i).y / 10, path.get(i + 1).x / 10, path.get(i + 1).y / 10);
			
			//TODO that div 10 is a nope
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
