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
	
	protected abstract Point getTitleDrawPositionImpl(FontMetrics metrics, int baseline, int width, String title);
	
	protected abstract Point getValueDrawPositionImpl(FontMetrics metrics, int baseline, int width, String value);
	
	public Point getTitleDrawPosition(Graphics2D g, BasePanel panel, String title, Font font){
		FontMetrics metrics = g.getFontMetrics(font);
		int baseline = getHorizontalBaseline(g, panel, font);
		return getTitleDrawPositionImpl(metrics, baseline, panel.getWidth(), title);
	}
	
	public Point getValueDrawPosition(Graphics2D g, BasePanel panel, String value, Font font){
		FontMetrics metrics = g.getFontMetrics(font);
		int baseline = getHorizontalBaseline(g, panel, font);
		return getValueDrawPositionImpl(metrics, baseline, panel.getWidth(), value);
	}
	
	private int getHorizontalBaseline(Graphics2D g, BasePanel panel, Font f) {
		FontMetrics metrics = g.getFontMetrics(f);
		return (panel.getHeight() + metrics.getAscent() - metrics.getDescent()) / 2;
	}
}
