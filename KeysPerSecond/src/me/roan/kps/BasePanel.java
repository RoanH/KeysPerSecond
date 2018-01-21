package me.roan.kps;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

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
		
		//RoundRectangle2D rect = new RoundRectangle2D.Double(2 + SizeManager.graphImageSize - 2, 
		//		                                            2 + SizeManager.graphImageSize - 2, 
		//		                                            this.getWidth() - 5 - SizeManager.graphImageSize, 
		//		                                            this.getHeight() - 5 - SizeManager.graphImageSize, 
		//		                                            5, 5);
		
		int[] x = new int[]{
			2 + SizeManager.graphImageSize/4*3 + SizeManager.graphImageSize/4,
			this.getWidth() - SizeManager.graphImageSize/2 - 2 - 1 - SizeManager.graphImageSize/4,
			this.getWidth() - 2 - 1 - SizeManager.graphImageSize/2,
			this.getWidth() - 2 - 1 -SizeManager.graphImageSize/2,
			this.getWidth() - SizeManager.graphImageSize/2 - 2 - 1 - SizeManager.graphImageSize/4,
			2 + SizeManager.graphImageSize/4*3 + SizeManager.graphImageSize/4,
			2 + SizeManager.graphImageSize/4*3,
			2 + SizeManager.graphImageSize/4*3
		};
		int[] y = new int[]{
			2 + SizeManager.graphImageSize/4*3,
			2 + SizeManager.graphImageSize/4*3,
			2 + SizeManager.graphImageSize/4*3 + SizeManager.graphImageSize/4,
			this.getHeight() - SizeManager.graphImageSize/2 - 2 - 1 - SizeManager.graphImageSize/4,
			this.getHeight() - 2 - 1 - SizeManager.graphImageSize/2,
			this.getHeight() - 2 - 1 - SizeManager.graphImageSize/2,
			this.getHeight() - SizeManager.graphImageSize/2 - 2 - 1 - SizeManager.graphImageSize/4,
			2 + SizeManager.graphImageSize/4*3 + SizeManager.graphImageSize/4
		};

		//BufferedImage colorImage = isActive() ? ColorManager.pressed : ColorManager.unpressed;
		//g.drawImage(colorImage, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, colorImage.getWidth(), colorImage.getHeight(), this);

		if(isActive()){
			g.setColor(new Color(221, 255, 255));
			//g.fill(new Polygon(x, y, 8));
			g.fillRect(2 + SizeManager.graphImageSize/4*3, 
					2 + SizeManager.graphImageSize/4*3, 
					this.getWidth() - 2 - 1 - SizeManager.graphImageSize/2 - (2 + SizeManager.graphImageSize/4*3), 
					this.getHeight() - 2 - 1 - SizeManager.graphImageSize/2 - (2 + SizeManager.graphImageSize/4*3));
		}
		
		g.drawImage(ColorManager.graph_upper_left,   2, 2, 2 + SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_lower_left,   2, this.getHeight() - 3 - SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, this.getHeight() - 3, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_upper_right,  this.getWidth() - 3 - SizeManager.graphImageSize, 2, this.getWidth() - 3, 2 + SizeManager.graphImageSize, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_lower_right,  this.getWidth() - 3 - SizeManager.graphImageSize, this.getHeight() - 3 - SizeManager.graphImageSize, this.getWidth() - 3, this.getHeight() - 3, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_side_left,    2, 2 + SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, this.getHeight() - 3 - SizeManager.graphImageSize, 0, 0, 4, 56, this);
		g.drawImage(ColorManager.graph_upper_middle, 2 + SizeManager.graphImageSize, 2, this.getWidth() - 3 - SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, 0, 0, 46, 4, this);
		g.drawImage(ColorManager.graph_lower_middle, 2 + SizeManager.graphImageSize, this.getHeight() - 3 - SizeManager.graphImageSize, this.getWidth() - 3 - SizeManager.graphImageSize, this.getHeight() - 3, 0, 0, 46, 4, this);
		g.drawImage(ColorManager.graph_side_right,   this.getWidth() - 3 - SizeManager.graphImageSize, 2 + SizeManager.graphImageSize, this.getWidth() - 3, this.getHeight() - 3 - SizeManager.graphImageSize, 0, 0, 4, 56, this);
		
		g.setColor(isActive() ? Main.config.getBackgroundColor() : Main.config.getForegroundColor());

		String titleString = getTitle();
		Font titleFont = Main.config.mode.getTitleFont(titleString);
		Point namePos = Main.config.mode.getTitleDrawPosition(g, this, titleString, titleFont);
		g.setFont(titleFont);
		g.drawString(titleString, namePos.x, namePos.y);

		String valueString = getValue();
		Font valueFont = Main.config.mode.getValueFont(valueString);
		Point keyCountPos = Main.config.mode.getValueDrawPosition(g, this, valueString, valueFont);
		g.setFont(valueFont);
		g.drawString(valueString, keyCountPos.x, keyCountPos.y);
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
