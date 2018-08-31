package me.roan.kps;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;

import me.roan.kps.panels.BasePanel;

/**
 * An enum specifying the different
 * text rendering modes
 * @author Roan
 */
public enum RenderingMode{
	/**
	 * HORIZONTAL text rendering
	 */
	HORIZONTAL_TN("Text - value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, BasePanel panel, String title){
			point.move(SizeManager.insideOffset + 1, SizeManager.insideOffset + (getPanelInsideHeight(panel) + metrics.getAscent()) / 2);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, BasePanel panel, String value){
			point.move(panel.getWidth() - SizeManager.sideTextOffset - metrics.stringWidth(value), getHorizontalBaseline(panel, metrics));
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			return (getPanelInsideHeight(panel) * 6) / 8;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			return getPanelInsideWidth(panel) / 2;
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			return (getPanelInsideHeight(panel) * 6) / 8;
		}

		@Override
		protected int getEffectiveValueWidth(BasePanel panel){
			return getPanelInsideWidth(panel) / 2;
		}
	},
	/**
	 * HORIZONTAL text rendering
	 */
	HORIZONTAL_NT("Value - text"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, BasePanel panel, String title){
			point.move(panel.getWidth() - SizeManager.sideTextOffset - metrics.stringWidth(title), getHorizontalBaseline(panel, metrics));
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, BasePanel panel, String value){
			point.move(SizeManager.sideTextOffset, getHorizontalBaseline(panel, metrics));
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected int getEffectiveValueWidth(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}
	},
	/**
	 * HORIZONTAL text rendering
	 */
	HORIZONTAL_TDAN("Text diagonally above value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, BasePanel panel, String title){
			point.move(panel.getWidth() - SizeManager.sideTextOffset - metrics.stringWidth(title), metrics.getAscent() + 1);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, BasePanel panel, String value){
			point.move(SizeManager.sideTextOffset, panel.getHeight() - SizeManager.sideTextOffset - 1);
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected int getEffectiveValueWidth(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}
	},
	/**
	 * HORIZONTAL text rendering
	 */
	HORIZONTAL_TDAN2("Text diagonally under value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, BasePanel panel, String title){
			point.move(SizeManager.sideTextOffset, metrics.getAscent() + 1);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, BasePanel panel, String value){
			point.move(panel.getWidth() - SizeManager.sideTextOffset - metrics.stringWidth(value), panel.getHeight() - SizeManager.sideTextOffset - 1);
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected int getEffectiveValueWidth(BasePanel panel){
			// TODO Auto-generated method stub
			return 0;
		}
	},
	/**
	 * VERTICAL text rendering
	 */
	VERTICAL("Text above value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, BasePanel panel, String title){
			point.move(SizeManager.insideOffset + (int)Math.round((double)(getPanelInsideWidth(panel) - metrics.stringWidth(title)) / 2.0D), SizeManager.insideOffset + getEffectiveTitleHeight(panel) + 1);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, BasePanel panel, String value){
			point.move(SizeManager.insideOffset + (int)Math.round((double)(getPanelInsideWidth(panel) - metrics.stringWidth(value)) / 2.0D), SizeManager.insideOffset + (getPanelInsideHeight(panel) * 7) / 8 + 1);
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			return (getPanelInsideHeight(panel) * 3) / 8;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			return getPanelInsideWidth(panel);
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			return (getPanelInsideHeight(panel) * 11) / 32;
		}

		@Override
		protected int getEffectiveValueWidth(BasePanel panel){
			return getPanelInsideWidth(panel);
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

	/**
	 * Mode specific logic for the title position
	 * @param metrics FontMetrics that will be used to draw the title
	 * @param baseline Baseline that will be used to draw the title
	 * @param width Panel width
	 * @param height Panel height
	 * @param title The title that will be drawn
	 * @return The location at which the title should be drawn
	 */
	protected abstract void setTitleDrawPositionImpl(FontMetrics metrics, BasePanel panel, String title);

	/**
	 * Mode specific logic for the value position
	 * @param metrics FontMetrics that will be used to draw the value
	 * @param baseline Baseline that will be used to draw the value
	 * @param width Panel width
	 * @param height Panel height
	 * @param value The value that will be drawn
	 * @return The location at which the value should be drawn
	 */
	protected abstract void setValueDrawPositionImpl(FontMetrics metrics, BasePanel panel, String value);

	protected abstract int getEffectiveTitleHeight(BasePanel panel);

	protected abstract int getEffectiveTitleWidth(BasePanel panel);

	protected abstract int getEffectiveValueHeight(BasePanel panel);

	protected abstract int getEffectiveValueWidth(BasePanel panel);

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
		setTitleDrawPositionImpl(g.getFontMetrics(font), panel, title);
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
		setValueDrawPositionImpl(g.getFontMetrics(font), panel, value);
		return point;
	}

	/**
	 * Gets the baseline to draw text in the panel
	 * @param panel The panel
	 * @param font The font used for drawing
	 * @return The horizontal baseline to use for drawing
	 */
	@Deprecated
	private static int getHorizontalBaseline(BasePanel panel, FontMetrics metrics){
		return (panel.getHeight() + metrics.getAscent() - metrics.getDescent()) / 2;
	}

	public Font getTitleFont(String text, Graphics2D g, BasePanel panel, Font currentFont){
		return resolveFont(text, g, getEffectiveTitleWidth(panel), getEffectiveTitleHeight(panel), Font.BOLD, currentFont);
	}

	public Font getValueFont(String text, Graphics2D g, BasePanel panel, Font currentFont, int maxsize){
		return resolveFont(text, g, getEffectiveValueWidth(panel), getEffectiveValueHeight(panel), Font.PLAIN, currentFont);
	}

	private static final int getPanelInsideHeight(BasePanel panel){
		return panel.getHeight() - SizeManager.insideOffset * 2;
	}

	private static final int getPanelInsideWidth(BasePanel panel){
		return panel.getWidth() - SizeManager.insideOffset * 2;
	}

	private static final Font resolveFont(String text, Graphics2D g, int maxWidth, int maxHeight, int properties, Font currentFont){
		FontMetrics fm;
		if(currentFont != null){
			fm = g.getFontMetrics(currentFont);
			if(fm.getMaxAscent() <= maxHeight && stringWidth(text, fm) <= maxWidth){
				return currentFont;
			}
		}

		int size = (int)(maxHeight * (Toolkit.getDefaultToolkit().getScreenResolution() / 72.0));
		Font font;
		do{
			font = new Font("Dialog", properties, size);
			fm = g.getFontMetrics(font);
			size--;
		}while(!(fm.getMaxAscent() <= maxHeight && stringWidth(text, fm) <= maxWidth));

		return font;
	}
	
	private static final int stringWidth(String text, FontMetrics fm){
		return fm.charWidth('R') * text.length();
	}

	@Override
	public String toString(){
		return name;
	}
}
