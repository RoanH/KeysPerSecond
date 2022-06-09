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

/**
 * Panel used to display the
 * total number of keys pressed
 * @author Roan
 */
public final class TotPanel extends BasePanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5628759695450014071L;
	/**
	 * Total number of hits
	 */
	public static int hits;
	/**
	 * Static instance of this panel that is reused all the time
	 */
	public static final TotPanel INSTANCE = new TotPanel();

	/**
	 * Constructs a new total panel
	 */
	private TotPanel(){
		sizeChanged();
	}

	@Override
	protected String getTitle(){
		return "TOT";
	}

	@Override
	protected String getValue(){
		return String.valueOf(hits);
	}

	@Override
	public int getLayoutX(){
		return Main.config.totPanel.getX();
	}

	@Override
	public int getLayoutY(){
		return Main.config.totPanel.getY();
	}

	@Override
	public int getLayoutWidth(){
		return Main.config.totPanel.getWidth();
	}

	@Override
	public int getLayoutHeight(){
		return Main.config.totPanel.getHeight();
	}

	@Override
	protected RenderingMode getRenderingMode(){
		return Main.config.totPanel.getRenderingMode();
	}
}