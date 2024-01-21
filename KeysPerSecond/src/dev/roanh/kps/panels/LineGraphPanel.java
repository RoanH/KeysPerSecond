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
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.util.concurrent.ConcurrentLinkedDeque;

import dev.roanh.kps.ColorManager;
import dev.roanh.kps.Main;
import dev.roanh.kps.RenderingMode;
import dev.roanh.kps.config.group.LineGraphSettings;

/**
 * Panel to draw a line graph.
 * @author Roan
 * @see LineGraphSettings
 */
public class LineGraphPanel extends GraphPanel{
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
	private final ConcurrentLinkedDeque<Integer> values = new ConcurrentLinkedDeque<Integer>();
	/**
	 * The configuration for this graph panel.
	 */
	private final LineGraphSettings config;
	/**
	 * Highest encountered value used as the upper bound of the graph.
	 */
	private int maxval = 1;
	
	/**
	 * Constructs a new graph panel with the given configuration.
	 * @param config The configuration for this panel.
	 */
	public LineGraphPanel(LineGraphSettings config){
		super(config);
		this.config = config;
	}

	@Override
	public final void reset(){
		values.clear();
		maxval = 1;
		repaint();
	}

	@Override
	protected void render(Graphics2D g){
		//graph computation
		Polygon poly = new Polygon();
		final int oy = this.getHeight() - borderOffset - RenderingMode.insideOffset;
		final int ox = this.getWidth() - borderOffset - RenderingMode.insideOffset - 1;
		final double insideHeight = this.getHeight() - (borderOffset + RenderingMode.insideOffset) * 2;
		final double insideWidth = this.getWidth() - (borderOffset + RenderingMode.insideOffset) * 2 - 1;
		final int frames = (config.getBacklog() / Main.config.getUpdateRateMs()) - 1;
		
		//average line
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, foreground.getAlpha()));
		if(config.isAverageVisible() && Main.avg <= config.getMaxValue()){
			int y = (int)(oy - ((insideHeight * Main.avg) / maxval));
			g.setColor(foreground.getColor().darker());
			g.setStroke(avgstroke);
			g.drawLine(borderOffset + RenderingMode.insideOffset, y, ox, y);
		}

		//graph drawing
		if(frames > 0){
			final double segment = insideWidth / frames;

			double px = ox;
			poly.addPoint(ox, oy);
			for(int val : values){
				poly.addPoint((int)px, (int)(oy - ((insideHeight * val) / maxval)));
				px -= segment;
			}
			poly.addPoint((int)Math.min(ox, px + segment), oy);

			g.setStroke(line);
			g.setColor(ColorManager.alphaAqua);
			g.fillPolygon(poly);
			g.setColor(foreground.getColor());
			g.drawPolygon(poly);
		}
	}
	
	@Override
	public void update(){
		addPoint(Main.prev);
	}
	
	/**
	 * Adds a new point to the end of this graph.
	 * @param value The new point to add.
	 */
	private void addPoint(int value){
		value = Math.min(value, config.getMaxValue());
		
		if(value > maxval){
			maxval = value;
		}

		values.addFirst(value);
		while(values.size() > config.getBacklog() / Main.config.getUpdateRateMs()){
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
