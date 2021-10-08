package dev.roanh.kps.layout;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import dev.roanh.kps.ColorManager;
import dev.roanh.kps.Main;

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
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, Main.config.getBackgroundOpacity()));
		g.setColor(Main.config.getBackgroundColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setComposite(comp);
		if(showGrid){
			g.setColor(ColorManager.alphaAqua);
			for(int i = Main.config.cellSize; i < this.getWidth(); i += Main.config.cellSize){
				g.drawLine(i, 0, i, this.getHeight());
				g.drawLine(i - 1, 0, i - 1, this.getHeight());
			}
			for(int i = Main.config.cellSize; i < this.getHeight(); i += Main.config.cellSize){
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
