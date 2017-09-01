package me.roan.kps;

import java.awt.*;

import javax.swing.JPanel;

import me.roan.kps.Main.Key;
import me.roan.kps.Main.KeyInformation;

/**
 * Panel to display the number
 * of times a certain key has 
 * been pressed
 * @author Roan
 */
public final class KeyPanel extends JPanel implements LayoutPosition{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 8816524158873355997L;
	/**
	 * The key object associated with this panel<br>
	 * This key object keep track of the amount of this
	 * the assigned key has been hit
	 */
	private Key key;
	/**
	 * Font 1 used to display the title of the panel
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
	 * The key information object
	 * for this key
	 */
	protected KeyInformation info;

	/**
	 * Constructs a new KeyPanel
	 * with the given key object
	 * @param key The key object to
	 *        associate this panel with
	 * @see Key
	 * @see #key
	 */
	protected KeyPanel(Key key, KeyInformation i) {
		this.key = key;
		info = i;
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

		Image colorImage = key.down ? ColorManager.pressed : ColorManager.unpressed;
		Point imageSize = Main.config.mode == RenderingMode.VERTICAL ? new Point(40, 64) : new Point(64, 40);
		g.drawImage(colorImage, 2, 2, this.getWidth() - 2, this.getHeight() - 2, 0, 0, imageSize.x, imageSize.y, this);

		Color drawColor = key.down ? Main.config.getBackgroundColor() : Main.config.getForegroundColor();
		g.setColor(drawColor);

		Point namePos = getNameDrawPosition(g, Main.config.mode);
		g.setFont(getNameFont());
		g.drawString(key.name, namePos.x, namePos.y);

		String keyCountString = String.valueOf(key.count);
		Point keyCountPos = getKeyCountDrawPosition(g, Main.config.mode, keyCountString);
		g.setFont(getKeyCountFont());
		g.drawString(keyCountString, keyCountPos.x, keyCountPos.y);
	}

	private Point getNameDrawPosition(Graphics2D g, RenderingMode renderingMode) {
		FontMetrics metrics = g.getFontMetrics(getNameFont());
		int baseline = getHorizontalBaseline(g);

		switch (renderingMode) {
			case HORIZONTAL_NT:
				return new Point(this.getWidth() - SizeManager.horizontalTextOffset - metrics.stringWidth(key.name), baseline);
			case HORIZONTAL_TN:
				return new Point(SizeManager.horizontalTextOffset, baseline);
			default:
				return new Point((this.getWidth() - metrics.stringWidth(key.name)) / 2, SizeManager.keyTitleTextOffset);
		}
	}

	private Point getKeyCountDrawPosition(Graphics2D g, RenderingMode renderingMode, String keyCountString) {
		FontMetrics metrics = g.getFontMetrics(getKeyCountFont());
		int baseline = getHorizontalBaseline(g);

		switch (renderingMode) {
			case HORIZONTAL_NT:
				return new Point(SizeManager.horizontalTextOffset, baseline);
			case HORIZONTAL_TN:
				return new Point(this.getWidth() - SizeManager.horizontalTextOffset - metrics.stringWidth(keyCountString), baseline);
			default:
				return new Point((this.getWidth() - metrics.stringWidth(keyCountString)) / 2, SizeManager.keyDataTextOffset);
		}
	}

	private int getHorizontalBaseline(Graphics2D g) {
		FontMetrics metrics = g.getFontMetrics(getNameFont());
		return (this.getHeight() + metrics.getAscent() - metrics.getDescent()) / 2;
	}

	private Font getKeyCountFont() {
		return key.count >= 10000 ? font2smallest : (key.count >= 1000 ? font2small : font2);
	}

	private Font getNameFont() {
		return key.name.length() == 1 ? font1 : BasePanel.font1;
	}

	@Override
	public int getIndex() {
		return info.index;
	}
}