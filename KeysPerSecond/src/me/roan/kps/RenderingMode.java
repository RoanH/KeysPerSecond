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
		protected Point getTitleDrawPositionImpl(FontMetrics metrics, int baseline, int width, String title) {
			return new Point(SizeManager.horizontalTextOffset, baseline);
		}

		@Override
		protected Point getValueDrawPositionImpl(FontMetrics metrics, int baseline, int width, String value) {
			return new Point(width - SizeManager.horizontalTextOffset - metrics.stringWidth(value), baseline);
		}
	},
	/**
	 * HORIZONTAL text rendering
	 */
	HORIZONTAL_NT("Horizontal (value - text)") {
		@Override
		protected Point getTitleDrawPositionImpl(FontMetrics metrics, int baseline, int width, String title) {
			return new Point(width - SizeManager.horizontalTextOffset - metrics.stringWidth(title), baseline);
		}

		@Override
		protected Point getValueDrawPositionImpl(FontMetrics metrics, int baseline, int width, String value) {
			return new Point(SizeManager.horizontalTextOffset, baseline);
		}
	},
	/**
	 * VERTICAL text rendering
	 */
	VERTICAL("Vertical") {
		@Override
		protected Point getTitleDrawPositionImpl(FontMetrics metrics, int baseline, int width, String title) {
			return new Point((width - metrics.stringWidth(title)) / 2, SizeManager.keyTitleTextOffset);
		}

		@Override
		protected Point getValueDrawPositionImpl(FontMetrics metrics, int baseline, int width, String value) {
			return new Point((width - metrics.stringWidth(value)) / 2, SizeManager.keyDataTextOffset);
		}
	};
	
	/**
	 * The display name of the
	 * enum constant, used in dialogs
	 */
	private String name;
	
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
	protected abstract Point getTitleDrawPositionImpl(FontMetrics metrics, int baseline, int width, String title);
	
	/**
	 * Mode specific logic for the value position
	 * @param metrics FontMetrics that will be used to draw the value
	 * @param baseline Baseline that will be used to draw the value
	 * @param width Panel width
	 * @param value The value that will be drawn
	 * @return The location at which the value should be drawn
	 */
	protected abstract Point getValueDrawPositionImpl(FontMetrics metrics, int baseline, int width, String value);
	
	/**
	 * Gets the location at which the panel title should be drawn
	 * @param g The graphics used for drawing
	 * @param panel The panel
	 * @param title The title
	 * @param font The font with which the title is going to be drawn
	 * @return The location at which the title should be drawn
	 */
	public Point getTitleDrawPosition(Graphics2D g, BasePanel panel, String title, Font font){
		return getTitleDrawPositionImpl(g.getFontMetrics(font), getHorizontalBaseline(g, panel, font), panel.getWidth(), title);
	}
	
	/**
	 * Gets the location at which the panel value should be drawn
	 * @param g The graphics used for drawing
	 * @param panel The panel
	 * @param title The value
	 * @param font The font with which the value is going to be drawn
	 * @return The location at which the value should be drawn
	 */
	public Point getValueDrawPosition(Graphics2D g, BasePanel panel, String value, Font font){
		return getValueDrawPositionImpl(g.getFontMetrics(font), getHorizontalBaseline(g, panel, font), panel.getWidth(), value);
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
