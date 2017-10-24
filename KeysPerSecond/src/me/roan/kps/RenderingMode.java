package me.roan.kps;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * An enum specifying the different
 * text rendering modes
 * @author Roan
 */
public enum RenderingMode {
	/**
	 * HORIZONTAL text rendering
	 */
	HORIZONTAL_TN("Horizontal (text - value)") {
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, int baseline, int width, String title) {
			point.move(SizeManager.horizontalTextOffset, baseline);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, int baseline, int width, String value) {
			point.move(width - SizeManager.horizontalTextOffset - metrics.stringWidth(value), baseline);
		}
	},
	/**
	 * HORIZONTAL text rendering
	 */
	HORIZONTAL_NT("Horizontal (value - text)") {
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, int baseline, int width, String title) {
			point.move(width - SizeManager.horizontalTextOffset - metrics.stringWidth(title), baseline);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, int baseline, int width, String value) {
			point.move(SizeManager.horizontalTextOffset, baseline);
		}
	},
	/**
	 * VERTICAL text rendering
	 */
	VERTICAL("Vertical") {
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, int baseline, int width, String title) {
			point.move((width - metrics.stringWidth(title)) / 2, SizeManager.keyTitleTextOffset);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, int baseline, int width, String value) {
			point.move((width - metrics.stringWidth(value)) / 2, SizeManager.keyDataTextOffset);
		}
	};
	
	/**
	 * The display name of the
	 * enum constant, used in dialogs
	 */
	private String name;
	/**
	 * Cache point that is constantly being reused and
	 * returned by the methods from this enum. This prevents
	 * the creation of a lot of extra objects. But prevents
	 * multiple points from this class existing at the same time.
	 */
	private static final Point point = new Point();
	
	/**
	 * Constructs a new RenderingMode
	 * with the given name
	 * @param n The display name of the mode
	 */
	private RenderingMode(String n){
		name = n;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	/**
	 * Mode specific logic for the title position
	 * @param metrics FontMetrics that will be used to draw the title
	 * @param baseline Baseline that will be used to draw the title
	 * @param width Panel width
	 * @param title The title that will be drawn
	 * @return The location at which the title should be drawn
	 */
	protected abstract void setTitleDrawPositionImpl(FontMetrics metrics, int baseline, int width, String title);
	
	/**
	 * Mode specific logic for the value position
	 * @param metrics FontMetrics that will be used to draw the value
	 * @param baseline Baseline that will be used to draw the value
	 * @param width Panel width
	 * @param value The value that will be drawn
	 * @return The location at which the value should be drawn
	 */
	protected abstract void setValueDrawPositionImpl(FontMetrics metrics, int baseline, int width, String value);
	
	/**
	 * Gets the location at which the panel title should be drawn
	 * Points returned by this class are not static
	 * @param g The graphics used for drawing
	 * @param panel The panel
	 * @param title The title
	 * @param font The font with which the title is going to be drawn
	 * @return The location at which the title should be drawn
	 */
	public Point getTitleDrawPosition(Graphics2D g, BasePanel panel, String title, Font font){
		setTitleDrawPositionImpl(g.getFontMetrics(font), getHorizontalBaseline(g, panel, font), panel.getWidth(), title);
		return point;
	}
	
	/**
	 * Gets the location at which the panel value should be drawn
	 * Points returned by this class are not static
	 * @param g The graphics used for drawing
	 * @param panel The panel
	 * @param title The value
	 * @param font The font with which the value is going to be drawn
	 * @return The location at which the value should be drawn
	 */
	public Point getValueDrawPosition(Graphics2D g, BasePanel panel, String value, Font font){
		setValueDrawPositionImpl(g.getFontMetrics(font), getHorizontalBaseline(g, panel, font), panel.getWidth(), value);
		return point;
	}
	
	/**
	 * Gets the baseline to draw text in the panel
	 * @param g The graphics used for drawing
	 * @param panel The panel
	 * @param font The font used for drawing
	 * @return The horizontal baseline to use for drawing
	 */
	private int getHorizontalBaseline(Graphics2D g, BasePanel panel, Font font) {
		FontMetrics metrics = g.getFontMetrics(font);
		return (panel.getHeight() + metrics.getAscent() - metrics.getDescent()) / 2;
	}
}
