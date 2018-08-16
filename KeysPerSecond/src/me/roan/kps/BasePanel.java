package me.roan.kps;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import javax.swing.JPanel;

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
	
	private Font titleFont;
	private Font valueFont;
	
	protected void sizeChanged(){
		titleFont = null;
		valueFont = null;
	}
	
	/**
	 * Constructs a new BasePanel
	 */
	protected BasePanel(){
		this.setOpaque(!ColorManager.transparency);
	}

	@Override
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, Main.config.getBackgroundOpacity()));
		g.setColor(Main.config.getBackgroundColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Main.config.getForegroundOpacity()));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g.drawImage(ColorManager.graph_upper_left,   2, 2, 2 + SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_lower_left,   2, this.getHeight() - 3 - SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, this.getHeight() - 3, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_upper_right,  this.getWidth() - 3 - SizeManager.graphImageSize, 2, this.getWidth() - 3, 2 + SizeManager.graphImageSize, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_lower_right,  this.getWidth() - 3 - SizeManager.graphImageSize, this.getHeight() - 3 - SizeManager.graphImageSize, this.getWidth() - 3, this.getHeight() - 3, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_side_left,    2, 2 + SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, this.getHeight() - 3 - SizeManager.graphImageSize, 0, 0, 4, 56, this);
		g.drawImage(ColorManager.graph_upper_middle, 2 + SizeManager.graphImageSize, 2, this.getWidth() - 3 - SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, 0, 0, 46, 4, this);
		g.drawImage(ColorManager.graph_lower_middle, 2 + SizeManager.graphImageSize, this.getHeight() - 3 - SizeManager.graphImageSize, this.getWidth() - 3 - SizeManager.graphImageSize, this.getHeight() - 3, 0, 0, 46, 4, this);
		g.drawImage(ColorManager.graph_side_right,   this.getWidth() - 3 - SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, this.getWidth() - 3, this.getHeight() - 3 - SizeManager.graphImageSize, 0, 0, 4, 56, this);
		
		if(isActive()){
			g.setColor(ColorManager.activeColor);
			g.fillRect(2 + (SizeManager.graphImageSize / 4) * 3, 
					   2 + (SizeManager.graphImageSize / 4) * 2, 
					   this.getWidth() - 5 - (SizeManager.graphImageSize / 4) * 5, 
					   this.getHeight() - 5 - (SizeManager.graphImageSize / 4) * 4);
			g.setColor(Main.config.getBackgroundColor());
		}else{
			g.setColor(Main.config.getForegroundColor());
		}
		
		String titleString = getTitle();
		//TODO just for testing if the mode is horizontal max width and height are different.
		titleFont = RenderingMode.resolveFont(titleString, g, this.getWidth() - (2 + SizeManager.graphImageSize) * 2, this.getHeight() / 2 - (3 + SizeManager.graphImageSize), Font.BOLD, titleFont);
		Point namePos = Main.config.mode.getTitleDrawPosition(g, this, titleString, titleFont);
		g.setFont(titleFont);
		g.drawString(titleString, namePos.x, namePos.y);

		String valueString = getValue();
		valueFont = RenderingMode.resolveFont(valueString, g, this.getWidth() - (2 + SizeManager.graphImageSize) * 2, (this.getHeight() * 3) / 8 - (1 + SizeManager.graphImageSize), Font.PLAIN, valueFont);
		Point keyCountPos = Main.config.mode.getValueDrawPosition(g, this, valueString, valueFont);
		g.setFont(valueFont);
		g.drawString(valueString, keyCountPos.x, keyCountPos.y);
		
		//TODO debug only
		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g.setColor(new Color(0, 255, 255, 100));
		g.drawRect((2 + SizeManager.graphImageSize), (2 + SizeManager.graphImageSize), this.getWidth() - 2 * (2 + SizeManager.graphImageSize), this.getHeight() - 2 * (2 + SizeManager.graphImageSize));
		int u = ((this.getHeight() - SizeManager.borderSize()) / 4);
		g.drawLine(0, u, this.getWidth(), u);
		g.drawLine(0, u * 2, this.getWidth(), u * 2);
		g.drawLine(0, u * 3, this.getWidth(), u * 3);
		g.drawLine(0, u * 4, this.getWidth(), u * 4);
	}
	
	/**
	 * @return Whether the panel is "active" or not
	 */
	protected boolean isActive() {
		return false;
	}
	
	/**
	 * @return The title of this panel
	 */
	protected abstract String getTitle();
	
	/**
	 * @return The value for this panel
	 */
	protected abstract String getValue();
}
