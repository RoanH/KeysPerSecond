package me.roan.kps;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.LinkedList;

import javax.swing.JPanel;

/**
 * Panel to draw continuous graphs
 * @author Roan
 */
public class GraphPanel extends JPanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1805853149241193714L;
	/**
	 * Data points
	 */
	private LinkedList<Integer> values = new LinkedList<Integer>();
	/**
	 * Number of points the graph consists of
	 */
	protected static int MAX = 30;
	/**
	 * Highest encountered value used as the
	 * upper bound of the graph
	 */
	private int maxval = 1;
	/**
	 * Draw the horizontal average line
	 */
	protected static boolean showAverage = true;
	/**
	 * Stroke used to draw the graph
	 */
	private static final Stroke line = new BasicStroke(2.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0F, null, 0);
	/**
	 * Stroke used to draw average line
	 */
	private static final Stroke avgstroke = new BasicStroke(1.0F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0F, null, 0);
	/**
	 * Whether or not the graph is active and plotting
	 */
	protected boolean enabled = true;
	/**
	 * Number of frames, this is used to determine
	 * the width of the graph and it equal
	 * to the number of keys being tracked +
	 * the number of informative keys being
	 * displayed (avg, max, cur)
	 */
	protected static int frames = 0;
	
	@Override
	public void paintComponent(Graphics g1){
		if(enabled){
			Graphics2D g = (Graphics2D)g1;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(ColorManager.background);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			Polygon poly = new Polygon();
			poly.addPoint(this.getWidth() - 6, this.getHeight() - 5);
			for(int i = 1; i <= values.size(); i++){
				int px = (int) (6 + ((double)(this.getWidth() - 12) / (double)(MAX - 1)) * (MAX - i));
				int py = (int) (this.getHeight() - 5 - ((float)(this.getHeight() - 11) * ((float)values.get(i - 1) / (float)maxval)));
				poly.addPoint(px, py);
				if(i == values.size()){
					poly.addPoint(px, this.getHeight() - 5);
				}
			}
			if(showAverage){
				int y = (int) (this.getHeight() - 5 - ((float)(this.getHeight() - 11) * (Main.avg / (float)maxval)));
				g.setColor(ColorManager.foreground.darker());
				g.setStroke(avgstroke);
				g.drawLine(5, y, this.getWidth() - 5, y);
			}
			g.setStroke(line);
			g.setColor(ColorManager.alphaAqua);
			g.fillPolygon(poly);
			g.setColor(ColorManager.foreground);
			g.drawPolygon(poly);
			g.drawImage(ColorManager.gleft, 3, 2, null);
			g.drawImage(ColorManager.gmid, 44 + 1, 2, (44 + 2) * (frames - 2), 64, null);
			g.drawImage(ColorManager.gright, 44 + 1 + (44 + 2) * (frames - 2), 2, null);
		}
	}
	
	/**
	 * Adds a new point to the end of this graph
	 * @param value The new point to add
	 */
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
