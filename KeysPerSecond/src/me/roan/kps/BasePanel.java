package me.roan.kps;

import java.awt.*;

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

		Image colorImage = isActive() ? ColorManager.pressed : ColorManager.unpressed;
		Point imageSize = Main.config.mode == RenderingMode.VERTICAL ? new Point(40, 64) : new Point(64, 40);
		g.drawImage(colorImage, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, imageSize.x, imageSize.y, this);

		Color drawColor = isActive() ? Main.config.getBackgroundColor() : Main.config.getForegroundColor();
		g.setColor(drawColor);

		String titleString = getTitle();
		Point namePos = getTitleDrawPosition(g, Main.config.mode, titleString);
		g.setFont(getTitleFont(titleString));
		g.drawString(titleString, namePos.x, namePos.y);

		String valueString = getValue();
		Point keyCountPos = getValueDrawPosition(g, Main.config.mode, valueString);
		g.setFont(getValueFont(valueString));
		g.drawString(valueString, keyCountPos.x, keyCountPos.y);
	}

	private Point getTitleDrawPosition(Graphics2D g, RenderingMode renderingMode, String titleString) {
		FontMetrics metrics = g.getFontMetrics(getTitleFont(titleString));
		int baseline = getHorizontalBaseline(g);

		switch (renderingMode) {
			case HORIZONTAL_NT:
				return new Point(this.getWidth() - SizeManager.horizontalTextOffset - metrics.stringWidth(titleString), baseline);
			case HORIZONTAL_TN:
				return new Point(SizeManager.horizontalTextOffset, baseline);
			default:
				return new Point((this.getWidth() - metrics.stringWidth(titleString)) / 2, SizeManager.keyTitleTextOffset);
		}
	}

	private Point getValueDrawPosition(Graphics2D g, RenderingMode renderingMode, String valueString) {
		FontMetrics metrics = g.getFontMetrics(getValueFont(valueString));
		int baseline = getHorizontalBaseline(g);

		switch (renderingMode) {
			case HORIZONTAL_NT:
				return new Point(SizeManager.horizontalTextOffset, baseline);
			case HORIZONTAL_TN:
				return new Point(this.getWidth() - SizeManager.horizontalTextOffset - metrics.stringWidth(valueString), baseline);
			default:
				return new Point((this.getWidth() - metrics.stringWidth(valueString)) / 2, SizeManager.keyDataTextOffset);
		}
	}

	private int getHorizontalBaseline(Graphics2D g) {
		FontMetrics metrics = g.getFontMetrics(getTitleFont(getTitle()));
		return (this.getHeight() + metrics.getAscent() - metrics.getDescent()) / 2;
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
