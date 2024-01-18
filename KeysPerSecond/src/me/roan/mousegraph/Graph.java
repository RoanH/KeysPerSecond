package me.roan.mousegraph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class Graph extends JPanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2604642433575841219L;

	@Override
	public void paintComponent(Graphics g){
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.CYAN);
		System.out.println("repaint");
		for(int i = 0; i < Main.path.size() - 2; i++){
			g.drawLine(Main.path.get(i).x / 10, Main.path.get(i).y / 10, Main.path.get(i + 1).x / 10, Main.path.get(i + 1).y / 10);
		}
	}
}
