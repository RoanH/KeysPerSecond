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
package dev.roanh.kps.ui.listener;

import java.awt.*;
import java.awt.event.WindowEvent;

import dev.roanh.kps.Main;

import static dev.roanh.kps.Main.config;

/**
 * Listener to be registered to all frames so
 * that they properly clean up resources when closed.
 * @author Roan
 */
public class MainWindowListener implements java.awt.event.WindowListener {

	@Override
	public void windowOpened(WindowEvent e){
		if (config.getFramePosition().hasPosition()) {
			e.getWindow().setLocation(config.getFramePosition().getLocation().x, config.getFramePosition().getLocation().y);
		} else {
			int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
			int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
			int posX = (screenWidth / 2) - (e.getWindow().getWidth() / 2);
			int posY = (screenHeight / 2) - (e.getWindow().getHeight() / 2);
			e.getWindow().setLocation(posX, posY);
		}
	}

	@Override
	public void windowClosing(WindowEvent e){
		Main.exit();
	}

	@Override
	public void windowClosed(WindowEvent e){
	}

	@Override
	public void windowIconified(WindowEvent e){
	}

	@Override
	public void windowDeiconified(WindowEvent e){
	}

	@Override
	public void windowActivated(WindowEvent e){
	}

	@Override
	public void windowDeactivated(WindowEvent e){
	}
}
