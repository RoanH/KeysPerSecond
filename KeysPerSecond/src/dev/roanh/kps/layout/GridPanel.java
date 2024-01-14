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
package dev.roanh.kps.layout;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import dev.roanh.kps.ColorManager;
import dev.roanh.kps.Main;
import dev.roanh.kps.config.ThemeColor;

/**
 * Simple panel that draws a spaced grid
 * @author Roan
 */
public class GridPanel extends JPanel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5878892347456014988L;
	/**
	 * Whether or not the grid is currently displayed
	 */
	private boolean showGrid = false;

	@Override
	public void paintComponent(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		Composite comp = g.getComposite();
		ThemeColor background = Main.config.getTheme().getBackground();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, background.getAlpha()));
		g.setColor(background.getColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setComposite(comp);
		
		if(showGrid){
			g.setColor(ColorManager.alphaAqua);
			int cellSize = Main.config.getCellSize();
			
			for(int i = cellSize; i < this.getWidth(); i += cellSize){
				g.drawLine(i, 0, i, this.getHeight());
				g.drawLine(i - 1, 0, i - 1, this.getHeight());
			}
			
			for(int i = cellSize; i < this.getHeight(); i += cellSize){
				g.drawLine(0, i, this.getWidth(), i);
				g.drawLine(0, i - 1, this.getWidth(), i - 1);
			}
		}
	}

	/**
	 * Turns on grid rendering
	 */
	public void showGrid(){
		showGrid = true;
	}

	/**
	 * Turns off grid rendering
	 */
	public void hideGrid(){
		showGrid = false;
	}
}
