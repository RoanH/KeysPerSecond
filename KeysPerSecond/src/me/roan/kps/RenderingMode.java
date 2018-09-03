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
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(SizeManager.insideOffset + 1, SizeManager.insideOffset + (getPanelInsideHeight(panel) + getHeight(g, font)) / 2);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(panel.getWidth() - SizeManager.insideOffset - 1 - metrics.stringWidth(value), SizeManager.insideOffset + (getPanelInsideHeight(panel) + getHeight(g, font)) / 2);
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			return (getPanelInsideHeight(panel) * 5) / 8;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			return getPanelInsideWidth(panel) / 2;
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			return getPanelInsideHeight(panel) / 2;
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
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(panel.getWidth() - SizeManager.insideOffset - 1 - metrics.stringWidth(title), SizeManager.insideOffset + (getPanelInsideHeight(panel) + getHeight(g, font)) / 2);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(SizeManager.insideOffset + 1, SizeManager.insideOffset + (getPanelInsideHeight(panel) + getHeight(g, font)) / 2);
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			return (getPanelInsideHeight(panel) * 5) / 8;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			return getPanelInsideWidth(panel) / 2;
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			return getPanelInsideHeight(panel) / 2;
		}

		@Override
		protected int getEffectiveValueWidth(BasePanel panel){
			return getPanelInsideWidth(panel) / 2;
		}
	},
	/**
	 * DIAGONAL text rendering
	 */
	DIAGONAL1("Text diagonally left above value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(SizeManager.sideTextOffset + getPanelInsideWidth(panel) - 1 - metrics.stringWidth(title), SizeManager.sideTextOffset + 1 + getHeight(g, font));
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(SizeManager.sideTextOffset + 1, SizeManager.sideTextOffset + getPanelInsideHeight(panel) - 1);
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			return getPanelInsideHeight(panel) / 2 - 2;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			return getPanelInsideWidth(panel) - 2;
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			return getPanelInsideHeight(panel) / 2 - 2;
		}

		@Override
		protected int getEffectiveValueWidth(BasePanel panel){
			return getPanelInsideWidth(panel) - 2;
		}
	},
	/**
	 * DIAGONAL text rendering
	 */
	DIAGONAL2("Text diagonally right under value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(SizeManager.sideTextOffset, metrics.getAscent() + 1);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
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
	 * DIAGONAL text rendering
	 */
	DIAGONAL3("Text diagonally right above value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(SizeManager.sideTextOffset, metrics.getAscent() + 1);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
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
	 * DIAGONAL text rendering
	 */
	DIAGONAL4("Text diagonally left under value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(SizeManager.sideTextOffset, metrics.getAscent() + 1);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
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
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(SizeManager.insideOffset + (int)Math.round((double)(getPanelInsideWidth(panel) - metrics.stringWidth(title)) / 2.0D), SizeManager.insideOffset + (getPanelInsideHeight(panel) * 3) / 8 + 1);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(SizeManager.insideOffset + (int)Math.round((double)(getPanelInsideWidth(panel) - metrics.stringWidth(value)) / 2.0D), SizeManager.insideOffset + (getPanelInsideHeight(panel) * 7) / 8 + 1);
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			return (getPanelInsideHeight(panel) * 11) / 32;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			return getPanelInsideWidth(panel);
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			return (getPanelInsideHeight(panel) * 9) / 32;
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
	private static final char[] ref = new char[]{'R'};

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
	protected abstract void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title);

	/**
	 * Mode specific logic for the value position
	 * @param metrics FontMetrics that will be used to draw the value
	 * @param baseline Baseline that will be used to draw the value
	 * @param width Panel width
	 * @param height Panel height
	 * @param value The value that will be drawn
	 * @return The location at which the value should be drawn
	 */
	protected abstract void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value);

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
	protected Point getTitleDrawPosition(Graphics2D g, BasePanel panel, String title, Font font){
		setTitleDrawPositionImpl(g.getFontMetrics(font), g, font, panel, title);
		return point;
	}
	
	private static final int getHeight(Graphics2D g, Font font){
		return font.createGlyphVector(g.getFontRenderContext(), ref).getPixelBounds(null, 0.0F, 0.0F).height;
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
	protected Point getValueDrawPosition(Graphics2D g, BasePanel panel, String value, Font font){
		setValueDrawPositionImpl(g.getFontMetrics(font), g, font, panel, value);
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

	protected Font getTitleFont(String text, Graphics2D g, BasePanel panel, Font currentFont){
		return resolveFont(text, g, getEffectiveTitleWidth(panel), getEffectiveTitleHeight(panel), Font.BOLD, currentFont);
	}

	protected Font getValueFont(String text, Graphics2D g, BasePanel panel, Font currentFont){
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
			if(getHeight(g, currentFont) <= maxHeight && stringWidth(text, fm) <= maxWidth){
				return currentFont;
			}
		}

		int size = (int)(maxHeight * (Toolkit.getDefaultToolkit().getScreenResolution() / 72.0));
		Font font;
		do{
			font = new Font("Dialog", properties, size);
			fm = g.getFontMetrics(font);
			size--;
		}while(!(getHeight(g, font) <= maxHeight && stringWidth(text, fm) <= maxWidth));

		return font;
	}
	
	private static final int stringWidth(String text, FontMetrics fm){
		return fm.charWidth(ref[0]) * text.length();
	}

	@Override
	public String toString(){
		return name;
	}
	
	public static class RenderCache{
		
		private RenderingMode mode;
		private Point valuePos;
		private Point titlePos;
		private Font valueFont;
		private Font titleFont;
		private int titleLen;
		private int valueLen;
		
		public final void init(RenderingMode mode){
			this.mode = mode;
			valuePos = null;
			titlePos = null;
			valueFont = null;
			titleFont = null;
			titleLen = -1;
			valueLen = -1;
		}
		
		public final void renderTitle(String title, Graphics2D g, BasePanel panel){
			if(titleLen != title.length()){
				titleLen = title.length();
				titleFont = mode.getTitleFont(title, g, panel, titleFont);
				titlePos = mode.getTitleDrawPosition(g, panel, title, titleFont).getLocation();
			}
			
			g.setFont(titleFont);
			g.drawString(title, titlePos.x, titlePos.y);
		}
		
		public final void renderValue(String value, Graphics2D g, BasePanel panel){
			if(valueLen != value.length()){
				valueLen = value.length();
				valueFont = mode.getValueFont(value, g, panel, valueFont);
				valuePos = mode.getValueDrawPosition(g, panel, value, valueFont).getLocation();
			}
			
			g.setFont(valueFont);
			g.drawString(value, valuePos.x, valuePos.y);
		}
	}
}
