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
package dev.roanh.kps.panels;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import dev.roanh.kps.ColorManager;
import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode.RenderCache;
import dev.roanh.kps.config.ThemeColor;
import dev.roanh.kps.config.group.DataPanelSettings;
import dev.roanh.kps.layout.LayoutPosition;

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
	 * Smallest size of graph images
	 */
	public static final int imageSize = 4;
	/**
	 * RenderCache for this panel
	 */
	private RenderCache cache = new RenderCache();
	/**
	 * Settings for this panel.
	 */
	private DataPanelSettings config;
	
	/**
	 * Constructs a new base panel with the given settings.
	 * @param config The panel settings.
	 */
	public BasePanel(DataPanelSettings config){
		this.config = config;
		sizeChanged();
	}

	/**
	 * Signals this panel that its size
	 * or properties changed and that thus
	 * the render cache should be invalidated
	 */
	public void sizeChanged(){
		cache.init(config.getRenderingMode());
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g1){
		Graphics2D g = (Graphics2D)g1;

		ThemeColor background = Main.config.getTheme().getBackground();
		ThemeColor foreground = Main.config.getTheme().getForeground();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, background.getAlpha()));
		g.setColor(background.getColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, foreground.getAlpha()));
		g.addRenderingHints(Main.desktopHints);

		int borderOffset = Main.config.getBorderOffset();
		g.drawImage(ColorManager.graph_upper_left,   borderOffset, borderOffset, borderOffset + imageSize, borderOffset + imageSize, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_lower_left,   borderOffset, this.getHeight() - borderOffset - imageSize, borderOffset + imageSize, this.getHeight() - borderOffset, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_upper_right,  this.getWidth() - borderOffset - imageSize, borderOffset, this.getWidth() - borderOffset, borderOffset + imageSize, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_lower_right,  this.getWidth() - borderOffset - imageSize, this.getHeight() - borderOffset - imageSize, this.getWidth() - borderOffset, this.getHeight() - borderOffset, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_side_left,    borderOffset, borderOffset + imageSize, borderOffset + imageSize, this.getHeight() - borderOffset - imageSize, 0, 0, 4, 56, this);
		g.drawImage(ColorManager.graph_upper_middle, borderOffset + imageSize, borderOffset, this.getWidth() - borderOffset - imageSize, borderOffset + imageSize, 0, 0, 46, 4, this);
		g.drawImage(ColorManager.graph_lower_middle, borderOffset + imageSize, this.getHeight() - borderOffset - imageSize, this.getWidth() - borderOffset - imageSize, this.getHeight() - borderOffset, 0, 0, 46, 4, this);
		g.drawImage(ColorManager.graph_side_right,   this.getWidth() - borderOffset - imageSize, borderOffset + imageSize, this.getWidth() - borderOffset, this.getHeight() - borderOffset - imageSize, 0, 0, 4, 56, this);
		
		if(isActive()){
			g.setColor(ColorManager.activeColor);
			g.fillRect(
				borderOffset + (imageSize / 4) * 3,
				borderOffset + (imageSize / 4) * 2,
				this.getWidth() - 2 * borderOffset - (imageSize / 4) * 6,
				this.getHeight() - 2 * borderOffset - (imageSize / 4) * 4
			);
			g.setColor(background.getColor());
		}else{
			g.setColor(foreground.getColor());
		}

		cache.renderTitle(config.getName(), g, this);

		cache.renderValue(getValue(), g, this);
	}

	/**
	 * Gets whether or not this panel should be highlighted.
	 * @return Whether the panel is "active" or not.
	 */
	protected boolean isActive(){
		return false;
	}

	/**
	 * Gets the value for this panel.
	 * @return The value for this panel.
	 */
	protected abstract String getValue();

	@Override
	public int getLayoutX(){
		return config.getLayoutX();
	}

	@Override
	public int getLayoutY(){
		return config.getLayoutY();
	}

	@Override
	public int getLayoutWidth(){
		return config.getLayoutWidth();
	}

	@Override
	public int getLayoutHeight(){
		return config.getLayoutHeight();
	}
}
