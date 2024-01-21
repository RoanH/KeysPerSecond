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

import java.awt.Graphics2D;

import dev.roanh.kps.ColorManager;
import dev.roanh.kps.RenderingMode.RenderCache;
import dev.roanh.kps.config.group.DataPanelSettings;

/**
 * Abstract base class for panels that display a title and a value.
 * @author Roan
 */
public abstract class DataPanel extends BasePanel{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 4584033301375308252L;
	/**
	 * RenderCache for this panel
	 */
	protected RenderCache cache = new RenderCache();
	/**
	 * Settings for this panel.
	 */
	private DataPanelSettings config;

	/**
	 * Constructs a new data panel with the given settings.
	 * @param config The panel settings.
	 */
	protected DataPanel(DataPanelSettings config){
		super(config);
		this.config = config;
		sizeChanged();
	}
	
	/**
	 * Signals this panel that its size
	 * or properties changed and that thus
	 * the render cache should be invalidated
	 */
	public final void sizeChanged(){
		cache.init(config.getRenderingMode());
		this.repaint();
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
	protected void render(Graphics2D g){
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
}
