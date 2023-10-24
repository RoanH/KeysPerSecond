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

import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.group.AveragePanelSettings;

/**
 * Panel used to display the
 * average keys pressed per second
 * @author Roan
 */
public final class AvgPanel extends BasePanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5628759695450014071L;
	
	private AveragePanelSettings settings;

	/**
	 * Constructs a new average panel
	 */
	public AvgPanel(AveragePanelSettings settings){
		this.settings = settings;
		//TODO add listener for rendering mode changes
		sizeChanged();
	}

	@Override
	protected String getTitle(){
		return settings.getName();
	}

	@Override
	protected String getValue(){
		return settings.formatAvg(Main.avg);
	}

	@Override
	public int getLayoutX(){
		return settings.getX();
	}

	@Override
	public int getLayoutY(){
		return settings.getY();
	}

	@Override
	public int getLayoutWidth(){
		return settings.getWidth();
	}

	@Override
	public int getLayoutHeight(){
		return settings.getHeight();
	}

	@Override
	protected RenderingMode getRenderingMode(){
		return settings.getRenderingMode();
	}
}