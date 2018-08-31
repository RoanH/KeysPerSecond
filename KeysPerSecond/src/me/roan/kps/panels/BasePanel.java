package me.roan.kps.panels;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.JPanel;

import me.roan.kps.ColorManager;
import me.roan.kps.Main;
import me.roan.kps.RenderingMode;
import me.roan.kps.SizeManager;
import me.roan.kps.layout.LayoutPosition;
import me.roan.kps.RenderingMode.RenderCache;

/**
 * Abstract base class for the 
 * panels that display the
 * average, maximum and current
 * keys per second
 * @author Roan
 */
public abstract class BasePanel extends JPanel implements LayoutPosition{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;
	
	private RenderCache cache = new RenderCache();
	
	public void sizeChanged(){
		cache.init(getRenderingMode());
	}
	
	/**
	 * Constructs a new BasePanel
	 */
	protected BasePanel(){
		this.setOpaque(!ColorManager.transparency);
	}

	@Override
	public void paintComponent(Graphics g1){
		Graphics2D g = (Graphics2D) g1;

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, Main.config.getBackgroundOpacity()));
		g.setColor(Main.config.getBackgroundColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Main.config.getForegroundOpacity()));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g.drawImage(ColorManager.graph_upper_left,   2, 2, 2 + SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_lower_left,   2, this.getHeight() - 2 - SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, this.getHeight() - 2, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_upper_right,  this.getWidth() - 2 - SizeManager.graphImageSize, 2, this.getWidth() - 2, 2 + SizeManager.graphImageSize, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_lower_right,  this.getWidth() - 2 - SizeManager.graphImageSize, this.getHeight() - 2 - SizeManager.graphImageSize, this.getWidth() - 2, this.getHeight() - 2, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_side_left,    2, 2 + SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, this.getHeight() - 2 - SizeManager.graphImageSize, 0, 0, 4, 56, this);
		g.drawImage(ColorManager.graph_upper_middle, 2 + SizeManager.graphImageSize, 2, this.getWidth() - 2 - SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, 0, 0, 46, 4, this);
		g.drawImage(ColorManager.graph_lower_middle, 2 + SizeManager.graphImageSize, this.getHeight() - 2 - SizeManager.graphImageSize, this.getWidth() - 2 - SizeManager.graphImageSize, this.getHeight() - 2, 0, 0, 46, 4, this);
		g.drawImage(ColorManager.graph_side_right,   this.getWidth() - 2 - SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, this.getWidth() - 2, this.getHeight() - 2 - SizeManager.graphImageSize, 0, 0, 4, 56, this);
		
		if(isActive()){
			g.setColor(ColorManager.activeColor);
			g.fillRect(2 + (SizeManager.graphImageSize / 4) * 3, 
					   2 + (SizeManager.graphImageSize / 4) * 2, 
					   this.getWidth() - 5 - (SizeManager.graphImageSize / 4) * 5, 
					   this.getHeight() - 4 - (SizeManager.graphImageSize / 4) * 4);
			g.setColor(Main.config.getBackgroundColor());
		}else{
			g.setColor(Main.config.getForegroundColor());
		}
				
		cache.renderTitle(getTitle(), g, this);
		
		cache.renderValue(getValue(), g, this);
		
		//TODO debug only
		int xs = SizeManager.borderSize() - 1;
		int xe = this.getWidth() - SizeManager.borderSize();
		int ys = SizeManager.borderSize() - 1;
		int ye = this.getHeight() - SizeManager.borderSize();
		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g.setColor(new Color(0, 255, 255, 100));
		g.drawLine(0, ys, this.getWidth(), ys);
		g.drawLine(xs, 0, xs, this.getHeight());
		g.drawLine(0, ye, this.getWidth(), ye);
		g.drawLine(xe, 0, xe, this.getHeight());
		
		//System.out.println("ys | ye | ys + ye: " + ys + " | " + ye + " | " + (ye - ys));
		//System.out.println("xs | xe | xs + xe: " + xs + " | " + xe + " | " + (xe - xs));
		
		g.drawLine(0, (ys + ye) / 2, this.getWidth(), (ys + ye) / 2);
		g.drawLine(0, (ys + ye) / 2 + 1, this.getWidth(), (ys + ye) / 2 + 1);

		g.drawLine((xs + xe) / 2, 0, (xs + xe) / 2, this.getHeight());
		g.drawLine((xs + xe) / 2 + 1, 0, (xs + xe) / 2 + 1, this.getHeight());

		g.drawLine(xs + (xe - xs) / 4, 0, xs + (xe - xs) / 4, this.getHeight());
		g.drawLine(xs + ((xe - xs) * 3) / 4 + 1, 0, xs + ((xe - xs) * 3) / 4 + 1, this.getHeight());

		g.drawLine(0, ys + (ye - ys) / 4, this.getWidth(), ys + (ye - ys) / 4);
		g.drawLine(0, ys + (ye - ys) / 4 + 1, this.getWidth(), ys + (ye - ys) / 4 + 1);

		g.drawLine(0, ys + ((ye - ys) * 3) / 4, this.getWidth(), ys + ((ye - ys) * 3) / 4);
		g.drawLine(0, ys + ((ye - ys) * 3) / 4 + 1, this.getWidth(), ys + ((ye - ys) * 3) / 4 + 1);

		g.drawLine(0, ys + ((ye - ys) * 3) / 8, this.getWidth(), ys + ((ye - ys) * 3) / 8);
		g.drawLine(0, ys + ((ye - ys) * 3) / 8 + 1, this.getWidth(), ys + ((ye - ys) * 3) / 8 + 1);

		g.drawLine(0, ys + ((ye - ys) * 7) / 8, this.getWidth(), ys + ((ye - ys) * 7) / 8);
		g.drawLine(0, ys + ((ye - ys) * 7) / 8 + 1, this.getWidth(), ys + ((ye - ys) * 7) / 8 + 1);
	}
	
	/**
	 * Gets whether or not this panel
	 * should be highlighted
	 * @return Whether the panel is "active" or not
	 */
	protected boolean isActive(){
		return false;
	}
	
	/**
	 * Gets the title for this panel
	 * @return The title for this panel
	 */
	protected abstract String getTitle();
	
	/**
	 * Gets the value for this panel
	 * @return The value for this panel
	 */
	protected abstract String getValue();
	
	/**
	 * Gets the rendering mode for this panel
	 * @return The rendering mode for this panel
	 */
	protected abstract RenderingMode getRenderingMode();
}
