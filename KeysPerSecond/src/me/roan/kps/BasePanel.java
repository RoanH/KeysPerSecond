package me.roan.kps;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Abstract base class for the 
 * panels that display the
 * average, maximum and current
 * keys per second
 * @author Roan
 */
public abstract class BasePanel extends JPanel {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;
	/**
	 * Font 1 used to draw the title of the panel
	 */
	protected static final Font font1 = new Font("Dialog", Font.BOLD, 15);
	/**
	 * Font 2 used to draw the value of the panel
	 */
	private static final Font font2 = new Font("Dialog", Font.PLAIN, 18);

	@Override
	public void paintComponent(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setColor(ColorManager.background);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(ColorManager.unpressed, 2, 2, null);
		g.setColor(ColorManager.foreground);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(font1);
		g.drawString(getTitle(), (this.getWidth() - g.getFontMetrics().stringWidth(getTitle())) / 2, 30);
		g.setFont(font2);
		String str = getValue();
		g.drawString(str, (this.getWidth() - g.getFontMetrics().stringWidth(str)) / 2, 55);
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
