package me.roan.kps;

import java.awt.*;
import java.awt.image.BufferedImage;

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
	 * Font 1 used to draw the title of the panel
	 */
	protected static Font font1;
	/**
	 * Font 2 used to display the value of this panel
	 */
	protected static Font font2;
	/**
	 * Font 2 but smaller
	 */
	protected static Font font2small;
	/**
	 * Font 2 small but smaller
	 */
	protected static Font font2smallest;
	
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

		BufferedImage colorImage = isActive() ? ColorManager.pressed : ColorManager.unpressed;
		g.drawImage(colorImage, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, colorImage.getWidth(), colorImage.getHeight(), this);

		Color drawColor = isActive() ? Main.config.getBackgroundColor() : Main.config.getForegroundColor();
		g.setColor(drawColor);

		String titleString = getTitle();
		Font titleFont = getTitleFont(titleString);
		Point namePos = Main.config.mode.getTitleDrawPosition(g, this, titleString, titleFont);
		g.setFont(titleFont);
		g.drawString(titleString, namePos.x, namePos.y);

		String valueString = getValue();
		Font valueFont = getValueFont(valueString);
		Point keyCountPos = Main.config.mode.getValueDrawPosition(g, this, valueString, valueFont);
		g.setFont(valueFont);
		g.drawString(valueString, keyCountPos.x, keyCountPos.y);
	}

	/**
	 * @param value The value to be drawn
	 * @return the font to use for value text
	 */
	protected Font getValueFont(String value) {
		return value.length() >= 5 ? font2smallest : (value.length() >= 4 ? font2small : font2);
	}
	
	/**
	 * @param title The title to be drawn
	 * @return The font to use for title text
	 */
	protected Font getTitleFont(String title) {
		return font1;
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
