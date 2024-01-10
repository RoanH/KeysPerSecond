/*
 * KeysPerSecond: An open source input statistics displayer.
 * Copyright (C) 2017  Roan Hofland (roan@roanh.dev).  All rights reserved.
 * GitHub Repository: https://github.com/RoanH/KeysPerSecond
 *
 * KeysPerSecond is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeysPerSecond is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.roanh.kps;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;

import dev.roanh.kps.panels.BasePanel;

/**
 * An enum specifying the different
 * text rendering modes
 * @author Roan
 */
public enum RenderingMode{
	/**
	 * HORIZONTAL text rendering
	 * The text is followed by the value
	 */
	HORIZONTAL_TN("Text - value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(
				Main.config.getBorderOffset() + insideOffset + 1,
				Main.config.getBorderOffset() + insideOffset + (getPanelInsideHeight(panel) + getHeight(g, font)) / 2
			);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(
				panel.getWidth() - Main.config.getBorderOffset() - insideOffset - 1 - metrics.stringWidth(value),
				Main.config.getBorderOffset() + insideOffset + (getPanelInsideHeight(panel) + getHeight(g, font)) / 2
			);
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
	 * The value is followed by the text
	 */
	HORIZONTAL_NT("Value - text"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(
				panel.getWidth() - Main.config.getBorderOffset() - insideOffset - 1 - metrics.stringWidth(title),
				Main.config.getBorderOffset() + insideOffset + (getPanelInsideHeight(panel) + getHeight(g, font)) / 2
			);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(
				Main.config.getBorderOffset() + insideOffset + 1,
				Main.config.getBorderOffset() + insideOffset + (getPanelInsideHeight(panel) + getHeight(g, font)) / 2
			);
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
	 * The text is diagonally right above the value
	 */
	DIAGONAL1("Text diagonally right above value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(
				Main.config.getBorderOffset() + insideOffset + getPanelInsideWidth(panel) - 1 - metrics.stringWidth(title),
				Main.config.getBorderOffset() + insideOffset + 1 + getHeight(g, font)
			);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(
				Main.config.getBorderOffset() + insideOffset + 1,
				Main.config.getBorderOffset() + insideOffset + getPanelInsideHeight(panel) - 1
			);
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
	 * The text is diagonally left under the value
	 */
	DIAGONAL2("Text diagonally left under value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(
				Main.config.getBorderOffset() + insideOffset + 1,
				Main.config.getBorderOffset() + insideOffset + getPanelInsideHeight(panel) - 1
			);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(
				Main.config.getBorderOffset() + insideOffset + getPanelInsideWidth(panel) - 1 - metrics.stringWidth(value),
				Main.config.getBorderOffset() + insideOffset + 1 + getHeight(g, font)
			);
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
	 * The text is diagonally left above the value
	 */
	DIAGONAL3("Text diagonally left above value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(
				Main.config.getBorderOffset() + insideOffset + 1,
				Main.config.getBorderOffset() + insideOffset + 1 + getHeight(g, font)
			);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(
				Main.config.getBorderOffset() + insideOffset + getPanelInsideWidth(panel) - 1 - metrics.stringWidth(value),
				Main.config.getBorderOffset() + insideOffset + getPanelInsideHeight(panel) - 1
			);
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
	 * The text is diagonally right under the value
	 */
	DIAGONAL4("Text diagonally right under value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(
				Main.config.getBorderOffset() + insideOffset + getPanelInsideWidth(panel) - 1 - metrics.stringWidth(title),
				Main.config.getBorderOffset() + insideOffset + getPanelInsideHeight(panel) - 1
			);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(
				Main.config.getBorderOffset() + insideOffset + 1,
				Main.config.getBorderOffset() + insideOffset + 1 + getHeight(g, font)
			);
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
	 * VERTICAL text rendering
	 * The text is above the value
	 */
	VERTICAL("Text above value"){
		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(
				Main.config.getBorderOffset() + insideOffset + (int)Math.round((getPanelInsideWidth(panel) - metrics.stringWidth(title)) / 2.0D),
				Main.config.getBorderOffset() + insideOffset + (getPanelInsideHeight(panel) * 3) / 8 + 1
			);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(
				Main.config.getBorderOffset() + insideOffset + (int)Math.round((getPanelInsideWidth(panel) - metrics.stringWidth(value)) / 2.0D),
				Main.config.getBorderOffset() + insideOffset + (getPanelInsideHeight(panel) * 7) / 8 + 1
			);
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
	},
	/**
	 * Rendering mode where only the panel title
	 * is displayed in the center.
	 */
	TEXT_ONLY("Text only", true, false){

		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
			point.move(
				Main.config.getBorderOffset() + insideOffset + (int)Math.round((getPanelInsideWidth(panel) - metrics.stringWidth(title)) / 2.0D),
				Main.config.getBorderOffset() + insideOffset + (getPanelInsideHeight(panel) + getHeight(g, font)) / 2
			);
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			return getPanelInsideHeight(panel) - 4;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			return getPanelInsideWidth(panel) - 4;
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			return 0;
		}

		@Override
		protected int getEffectiveValueWidth(BasePanel panel){
			return 0;
		}
	},
	/**
	 * Rendering mode where only the panel value
	 * is displayed in the center.
	 */
	VALUE_ONLY("Value only", false, true){

		@Override
		protected void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title){
		}

		@Override
		protected void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value){
			point.move(
				Main.config.getBorderOffset() + insideOffset + (int)Math.round((getPanelInsideWidth(panel) - metrics.stringWidth(value)) / 2.0D),
				Main.config.getBorderOffset() + insideOffset + (getPanelInsideHeight(panel) + getHeight(g, font)) / 2
			);
		}

		@Override
		protected int getEffectiveTitleHeight(BasePanel panel){
			return 0;
		}

		@Override
		protected int getEffectiveTitleWidth(BasePanel panel){
			return 0;
		}

		@Override
		protected int getEffectiveValueHeight(BasePanel panel){
			return getPanelInsideHeight(panel) - 4;
		}

		@Override
		protected int getEffectiveValueWidth(BasePanel panel){
			return getPanelInsideWidth(panel) - 4;
		}
	};

	/**
	 * The display name of the
	 * enum constant, used in dialogs
	 */
	private final String name;
	/**
	 * Whether this rendering mode renders a panel title.
	 */
	private final boolean text;
	/**
	 * Whether this rendering mode renders a panel value.
	 */
	private final boolean value;
	/**
	 * Cache point that is constantly being reused and
	 * returned by the methods from this enum. This prevents
	 * the creation of a lot of extra objects. But prevents
	 * multiple points from this enum from existing at the same time.
	 */
	private static final Point point = new Point();
	/**
	 * Reference character for string width and height measurements.
	 * This is used to give strings with the same number of characters
	 * the same appearance.
	 */
	private static final char[] ref = new char[]{'R'};
	/**
	 * Offset from the panel border offset to the inside
	 * of the panel (inside the image).
	 */
	public static final int insideOffset = 3;
	
	/**
	 * Constructs a new RenderingMode
	 * with the given name.
	 * @param name The display name of the mode.
	 */
	private RenderingMode(String name){
		this(name, true, true);
	}

	/**
	 * Constructs a new RenderingMode
	 * with the given name and display settings.
	 * @param name The display name of the mode.
	 * @param text Whether to render a panel title or not.
	 * @param value Whether to render a panel value or not.
	 */
	private RenderingMode(String name, boolean text, boolean value){
		this.name = name;
		this.text = text;
		this.value = value;
	}

	/**
	 * Mode specific logic for the title position
	 * @param metrics FontMetrics that will be used to draw the title
	 * @param g The graphics used to render the title
	 * @param font The font used to draw the title
	 * @param panel The panel the title will be drawn on
	 * @param title The title that will be drawn
	 */
	protected abstract void setTitleDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String title);

	/**
	 * Mode specific logic for the value position
	 * @param metrics FontMetrics that will be used to draw the value
	 * @param g The graphics used to render the value
	 * @param font The font used to draw the value
	 * @param panel The panel the value will be drawn on
	 * @param value The value that will be drawn
	 */
	protected abstract void setValueDrawPositionImpl(FontMetrics metrics, Graphics2D g, Font font, BasePanel panel, String value);

	/**
	 * Gets the effective height available to draw the title
	 * @param panel The panel to get the effective height for
	 * @return The effective height available to draw the title
	 */
	protected abstract int getEffectiveTitleHeight(BasePanel panel);

	/**
	 * Gets the effective width available to draw the title
	 * @param panel The panel to get the effective width for
	 * @return The effective width available to draw the title
	 */
	protected abstract int getEffectiveTitleWidth(BasePanel panel);

	/**
	 * Gets the effective height available to draw the value
	 * @param panel The panel to get the effective height for
	 * @return The effective height available to draw the value
	 */
	protected abstract int getEffectiveValueHeight(BasePanel panel);

	/**
	 * Gets the effective width available to draw the value
	 * @param panel The panel to get the effective width for
	 * @return The effective width available to draw the value
	 */
	protected abstract int getEffectiveValueWidth(BasePanel panel);

	/**
	 * Gets the location at which the panel title should be drawn
	 * Points returned by this class are dynamic and change on a call
	 * to either {@link #getTitleDrawPosition(Graphics2D, BasePanel, String, Font)}
	 * or {@link #getValueDrawPosition(Graphics2D, BasePanel, String, Font)}.
	 * @param g The graphics used to draw the title.
	 * @param panel The panel the title will be drawn on.
	 * @param title The title that is going to be drawn.
	 * @param font The font with which the title is going to be drawn.
	 * @return The location at which the title should be drawn.
	 */
	protected Point getTitleDrawPosition(Graphics2D g, BasePanel panel, String title, Font font){
		setTitleDrawPositionImpl(g.getFontMetrics(font), g, font, panel, title);
		return point;
	}

	/**
	 * Gets the location at which the panel title should be drawn
	 * Points returned by this class are dynamic and change on a call
	 * to either {@link #getTitleDrawPosition(Graphics2D, BasePanel, String, Font)}
	 * or {@link #getValueDrawPosition(Graphics2D, BasePanel, String, Font)}.
	 * @param g The graphics used to draw the value.
	 * @param panel The panel the value will be drawn on.
	 * @param value The value that is going to be drawn.
	 * @param font The font with which the value is going to be drawn.
	 * @return The location at which the value should be drawn.
	 */
	protected Point getValueDrawPosition(Graphics2D g, BasePanel panel, String value, Font font){
		setValueDrawPositionImpl(g.getFontMetrics(font), g, font, panel, value);
		return point;
	}

	/**
	 * Gets the title font that should be used to draw the
	 * panel title
	 * @param text The title that is going to be drawn
	 * @param g The graphics that will be used to draw the title
	 * @param panel The panel the title will be drawn on
	 * @param currentFont The font last used to draw this title
	 * @return The font to use this time to draw the title
	 */
	protected Font getTitleFont(String text, Graphics2D g, BasePanel panel, Font currentFont){
		return resolveFont(text, g, getEffectiveTitleWidth(panel), getEffectiveTitleHeight(panel), Font.BOLD, currentFont);
	}

	/**
	 * Gets the value font that should be used to draw the
	 * panel value
	 * @param text The value that is going to be drawn
	 * @param g The graphics that will be used to draw the value
	 * @param panel The panel the value will be drawn on
	 * @param currentFont The font last used to draw this value
	 * @return The font to use this time to draw the value
	 */
	protected Font getValueFont(String text, Graphics2D g, BasePanel panel, Font currentFont){
		return resolveFont(text, g, getEffectiveValueWidth(panel), getEffectiveValueHeight(panel), Font.PLAIN, currentFont);
	}

	/**
	 * Gets the inside height in pixel of the
	 * given panel. The inside height is the
	 * height inside of the border image
	 * @param panel The panel to get the inside height for
	 * @return The inside height of the panel
	 */
	private static final int getPanelInsideHeight(BasePanel panel){
		return panel.getHeight() - (Main.config.getBorderOffset() + insideOffset) * 2;
	}

	/**
	 * Gets the inside width in pixel of the
	 * given panel. The inside width is the
	 * width inside of the border image
	 * @param panel The panel to get the inside width for
	 * @return The inside width of the panel
	 */
	private static final int getPanelInsideWidth(BasePanel panel){
		return panel.getWidth() - (Main.config.getBorderOffset() + insideOffset) * 2;
	}

	/**
	 * Computes the font that should be used to
	 * draw the given text based on the given
	 * constraints
	 * @param text The text that is going to be drawn
	 * @param g The graphics that are going to be used to draw the text
	 * @param maxWidth The maximum width the drawn text may be
	 * @param maxHeight The maximum height the drawn text may be
	 * @param properties The flags to pass on to the Font
	 * @param currentFont The font currently being used by the panel
	 * @return The new font to use to draw the text for the panel
	 */
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

	/**
	 * Gets the height of drawn text in pixels
	 * for the given font
	 * @param g The graphics that will be used for drawing
	 * @param font The font to check
	 * @return The height of drawn text in the given font
	 */
	private static final int getHeight(Graphics2D g, Font font){
		return font.createGlyphVector(g.getFontRenderContext(), ref).getPixelBounds(null, 0.0F, 0.0F).height;
	}

	/**
	 * Gets the width of the given string
	 * using the given font metrics
	 * @param text The text to get the width for
	 * @param fm The font metrics to used to find this width
	 * @return The width of the given string
	 */
	private static final int stringWidth(String text, FontMetrics fm){
		return Math.max(fm.charWidth(ref[0]) * text.length(), fm.stringWidth(text));
	}

	@Override
	public String toString(){
		return name;
	}

	/**
	 * Simple cache that caches rendering information
	 * so that it does not have to be recomputed for
	 * every repaint event
	 * @author Roan
	 */
	public static class RenderCache{
		/**
		 * The rendering mode this cache is for
		 */
		private RenderingMode mode;
		/**
		 * The last valid draw position for the value string
		 */
		private Point valuePos;
		/**
		 * The last valid draw position for the title string
		 */
		private Point titlePos;
		/**
		 * The last valid font for the value string
		 */
		private Font valueFont;
		/**
		 * The last valid font for the title string
		 */
		private Font titleFont;
		/**
		 * The last known length of the title string
		 */
		private int titleLen;
		/**
		 * The last known length of the value string
		 */
		private int valueLen;

		/**
		 * Initialises and resets this cache
		 * for the given rendering mode
		 * @param mode The rendering mode to
		 *        initialise this cache for
		 */
		public final void init(RenderingMode mode){
			this.mode = mode;
			valuePos = null;
			titlePos = null;
			valueFont = null;
			titleFont = null;
			titleLen = -1;
			valueLen = -1;
		}

		/**
		 * Renders the title string on the given panel with
		 * the given graphics according to the cached information
		 * @param title The title to render
		 * @param g The graphics to use to draw this title
		 * @param panel The panel to draw this title on
		 */
		public final void renderTitle(String title, Graphics2D g, BasePanel panel){
			if(mode.text){
				if(titleLen != title.length()){
					titleLen = title.length();
					titleFont = mode.getTitleFont(title, g, panel, titleFont);
					titlePos = mode.getTitleDrawPosition(g, panel, title, titleFont).getLocation();
				}

				g.setFont(titleFont);
				g.drawString(title, titlePos.x, titlePos.y);
			}
		}

		/**
		 * Renders the value string on the given panel with
		 * the given graphics according to the cached information
		 * @param value The value to render
		 * @param g The graphics to use to draw this value
		 * @param panel The panel to draw this value on
		 */
		public final void renderValue(String value, Graphics2D g, BasePanel panel){
			if(mode.value){
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
}
