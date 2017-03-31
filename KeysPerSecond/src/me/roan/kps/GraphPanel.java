package me.roan.kps;

import java.awt.AlphaComposite;
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
	
	/**
	 * Constructs a new GraphPanel
	 */
	protected GraphPanel(){
		this.addMouseListener(Listener.INSTANCE);
		this.addMouseMotionListener(Listener.INSTANCE);
	}
	
	@Override
	public void paintComponent(Graphics g1){
		if(enabled){
			Graphics2D g = (Graphics2D)g1;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
			if(ColorManager.transparency){
				g.setColor(ColorManager.transparent);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ColorManager.opacitybg));
				g.setColor(ColorManager.background);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ColorManager.opacityfg));
			}else{
				g.setColor(ColorManager.background);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
			}
			Polygon poly = new Polygon();
			poly.addPoint(this.getWidth() - SizeManager.graphOffset - 2, this.getHeight() - SizeManager.graphOffset);
			for(int i = 1; i <= values.size(); i++){
				int px = (int) (SizeManager.graphOffset + 2 + ((double)(this.getWidth() - SizeManager.graphOffset * 2 - 4) / (double)(MAX - 1)) * (MAX - i));
				int py = (int) (this.getHeight() - SizeManager.graphOffset - ((float)(this.getHeight() - SizeManager.graphOffset * 2) * ((float)values.get(i - 1) / (float)maxval)));
				poly.addPoint(px, py);
				if(i == values.size()){
					poly.addPoint(px, this.getHeight() - SizeManager.graphOffset);
				}
			}
			if(showAverage){
				int y = (int) (this.getHeight() - SizeManager.graphOffset - ((float)(this.getHeight() - SizeManager.graphOffset * 2) * (Main.avg / (float)maxval)));
				g.setColor(ColorManager.foreground.darker());
				g.setStroke(avgstroke);
				g.drawLine(SizeManager.graphOffset + 2, y, this.getWidth() - SizeManager.graphOffset - 2, y);
			}
			g.setStroke(line);
			g.setColor(ColorManager.alphaAqua);
			g.fillPolygon(poly);
			g.setColor(ColorManager.foreground);
			g.drawPolygon(poly);
			if(frames > 1){
				g.drawImage(ColorManager.gleft, 3, 2, 2 + SizeManager.graphImageLeftRightWidth, this.getHeight() - 2, 0, 0, 42, 64, null);
				g.drawImage(ColorManager.gmid, SizeManager.graphImageLeftRightWidth + 2, 2, SizeManager.graphImageLeftRightWidth + 2 + SizeManager.graphImageMiddleWidth * (frames - 2), this.getHeight() - 2, 0, 0, 46, 64, null);
				g.drawImage(ColorManager.gright, SizeManager.graphImageLeftRightWidth + 2 + SizeManager.graphImageMiddleWidth * (frames - 2),2, this.getWidth() - 4, this.getHeight() - 2, 0, 0, 42, 64, null);
			}else{
				g.drawImage(ColorManager.unpressed, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, 40, 64, null);
			}
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
