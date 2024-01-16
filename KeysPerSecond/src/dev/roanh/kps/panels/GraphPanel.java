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
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.JPanel;

import dev.roanh.kps.ColorManager;
import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.ThemeColor;
import dev.roanh.kps.config.group.GraphSettings;
import dev.roanh.kps.layout.LayoutPosition;
import dev.roanh.kps.ui.editor.EditorProvider;

/**
 * Panel to draw continuous graphs.
 * @author Roan
 */
public class GraphPanel extends JPanel implements LayoutPosition, EditorProvider{
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1805853149241193714L;
	/**
	 * Stroke used to draw the graph.
	 */
	private static final Stroke line = new BasicStroke(2.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0F, null, 0);
	/**
	 * Stroke used to draw average line.
	 */
	private static final Stroke avgstroke = new BasicStroke(1.0F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0F, null, 0);
	/**
	 * Data points.
	 */
	private ConcurrentLinkedDeque<Integer> values = new ConcurrentLinkedDeque<Integer>();
	/**
	 * Highest encountered value used as the upper bound of the graph.
	 */
	private int maxval = 1;
	/**
	 * The configuration for this graph panel.
	 */
	private GraphSettings config;
	
	/**
	 * Constructs a new graph panel with the given configuration.
	 * @param config The configuration for this panel.
	 */
	public GraphPanel(GraphSettings config){
		this.config = config;
	}

	/**
	 * Resets the graph data.
	 */
	public final void reset(){
		values.clear();
		maxval = 1;
	}

	@Override
	public void paintComponent(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
		int borderOffset = Main.config.getBorderOffset();

		//background
		ThemeColor background = Main.config.getTheme().getBackground();
		if(ColorManager.transparency){
			g.setColor(ColorManager.transparent);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, background.getAlpha()));
			g.setColor(background.getColor());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, background.getAlpha()));
		}else{
			g.setColor(background.getColor());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
		}

		//graph computation
		Polygon poly = new Polygon();
		final int oy = this.getHeight() - borderOffset - RenderingMode.insideOffset - 1;
		final int ox = this.getWidth() - borderOffset - RenderingMode.insideOffset - 2;
		final double insideHeight = this.getHeight() - (borderOffset + RenderingMode.insideOffset) * 2;
		final double insideWidth = (this.getWidth() - (borderOffset + RenderingMode.insideOffset) * 2 - 2);
		final double segment = insideWidth / (config.getBacklog() - 1);

		double px = ox;
		poly.addPoint(ox, oy);
		for(int val : values){
			poly.addPoint((int)px, (int)(oy - ((insideHeight * val) / maxval)));
			px -= segment;
		}
		poly.addPoint((int)Math.min(ox, px + segment), oy);

		//average line
		ThemeColor foreground = Main.config.getTheme().getForeground();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, foreground.getAlpha()));
		if(config.isAverageVisible() && Main.avg <= config.getMaxValue()){
			int y = (int)(oy + 1 - ((insideHeight * Main.avg) / maxval));
			g.setColor(foreground.getColor().darker());
			g.setStroke(avgstroke);
			g.drawLine(borderOffset + RenderingMode.insideOffset, y, ox, y);
		}

		//graph drawing
		g.setStroke(line);
		g.setColor(ColorManager.alphaAqua);
		g.fillPolygon(poly);
		g.setColor(foreground.getColor());
		g.drawPolygon(poly);

		//border
		g.drawImage(ColorManager.graph_upper_left,   borderOffset, borderOffset, borderOffset + BasePanel.imageSize, borderOffset + BasePanel.imageSize, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_lower_left,   borderOffset, this.getHeight() - borderOffset - 1 - BasePanel.imageSize, borderOffset + BasePanel.imageSize, this.getHeight() - 1 - borderOffset, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_upper_right,  this.getWidth() - 1 - borderOffset - BasePanel.imageSize, borderOffset, this.getWidth() - borderOffset - 1, borderOffset + BasePanel.imageSize, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_lower_right,  this.getWidth() - 1 - borderOffset - BasePanel.imageSize, this.getHeight() - 1 - borderOffset - BasePanel.imageSize, this.getWidth() - 1 - borderOffset, this.getHeight() - 1 - borderOffset, 0, 0, 4, 4, this);
		g.drawImage(ColorManager.graph_side_left,    borderOffset, borderOffset + BasePanel.imageSize, borderOffset + BasePanel.imageSize, this.getHeight() - 1 - borderOffset - BasePanel.imageSize, 0, 0, 4, 56, this);
		g.drawImage(ColorManager.graph_upper_middle, borderOffset + BasePanel.imageSize, borderOffset, this.getWidth() - 1 - borderOffset - BasePanel.imageSize, borderOffset + BasePanel.imageSize, 0, 0, 46, 4, this);
		g.drawImage(ColorManager.graph_lower_middle, borderOffset + BasePanel.imageSize, this.getHeight() - 1 - borderOffset - BasePanel.imageSize, this.getWidth() - 1 - borderOffset - BasePanel.imageSize, this.getHeight() - 1 - borderOffset, 0, 0, 46, 4, this);
		g.drawImage(ColorManager.graph_side_right,   this.getWidth() - 1 - borderOffset - BasePanel.imageSize, borderOffset + BasePanel.imageSize, this.getWidth() - 1 - borderOffset, this.getHeight() - 1 - borderOffset - BasePanel.imageSize, 0, 0, 4, 56, this);
	}

	/**
	 * Adds a new point to the end of this graph.
	 * @param value The new point to add.
	 */
	public void addPoint(int value){
		value = Math.min(value, config.getMaxValue());
		
		if(value > maxval){
			maxval = value;
		}

		values.addFirst(value);
		while(values.size() > config.getBacklog()){
			values.removeLast();
		}
	}

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

	@Override
	public void showEditor(boolean live){
		config.showEditor(live);
	}
}
