package me.roan.kps.layout;

import java.awt.Graphics;

import javax.swing.JPanel;

import me.roan.kps.ColorManager;
import me.roan.kps.Main;
import me.roan.kps.SizeManager;

/**
 * Simple panel that draws a grid spaced
 * using the {@link SizeManager#cellSize}
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
	public void paintComponent(Graphics g){
		g.setColor(Main.config.getBackgroundColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if(showGrid){
			g.setColor(ColorManager.alphaAqua);
			for(int i = SizeManager.cellSize; i < this.getWidth(); i += SizeManager.cellSize){
				g.drawLine(i, 0, i, this.getHeight());
				g.drawLine(i - 1, 0, i - 1, this.getHeight());
			}
			for(int i = SizeManager.cellSize; i < this.getHeight(); i += SizeManager.cellSize){
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
