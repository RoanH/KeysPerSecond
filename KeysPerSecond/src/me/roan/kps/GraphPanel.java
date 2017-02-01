package me.roan.kps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.LinkedList;

import javax.swing.JPanel;

public class GraphPanel extends JPanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1805853149241193714L;
	
	private LinkedList<Integer> values = new LinkedList<Integer>();
	private static final int MAX = 30;
	private int maxval = 1;
	private static final Color alphaAqua = new Color(0.0F, 1.0F, 1.0F, 0.2F);
	private static final Stroke line = new BasicStroke(2.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0F, null, 0);
	protected boolean enabled = true;
	
	@Override
	public void paintComponent(Graphics g1){
		if(enabled){
			Graphics2D g = (Graphics2D)g1;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setStroke(line);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			Polygon poly = new Polygon();
			poly.addPoint(this.getWidth() - 5, this.getHeight() - 5);
			for(int i = 1; i <= values.size(); i++){
				int px = (int) (5 + ((double)(this.getWidth() - 10) / (double)(MAX - 1)) * (MAX - i));
				int py = (int) (this.getHeight() - 5 - ((float)(this.getHeight() - 11) * ((float)values.get(i - 1) / (float)maxval)));
				poly.addPoint(px, py);
				if(i == values.size()){
					poly.addPoint(px, this.getHeight() - 5);
				}
			}
			g.setColor(alphaAqua);
			g.fillPolygon(poly);
			g.setColor(Color.CYAN);
			g.drawPolygon(poly);
			//g.drawImage(Main.unpressed.getScaledInstance(this.getWidth(), this.getHeight(), 0), 0, 0, null);
			
			g.setColor(Color.CYAN);
			g.drawRect(5, 5, this.getWidth() - 10, this.getHeight() - 10);
		}
	}
	
	public void addPoint(int value){
		if(enabled){
			if(value > maxval){
				maxval = value;
			}
			values.addFirst(value);
			if(values.size() > MAX){
				values.removeLast();
			}
		}
	}
}
