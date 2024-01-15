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
package dev.roanh.kps;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import dev.roanh.kps.config.group.ThemeSettings;

/**
 * Central class for managing colours and resources.
 * @author Roan
 */
public class ColorManager{
	/**
	 * Graph image segment 'lower right'
	 */
	public static Image graph_lower_right;
	/**
	 * Graph image segment 'lower left'
	 */
	public static Image graph_lower_left;
	/**
	 * Graph image segment 'lower middle'
	 */
	public static Image graph_lower_middle;
	/**
	 * Graph image segment 'side left'
	 */
	public static Image graph_side_left;
	/**
	 * Graph image segment 'side right'
	 */
	public static Image graph_side_right;
	/**
	 * Graph image segment 'upper right'
	 */
	public static Image graph_upper_right;
	/**
	 * Graph image segment 'upper left'
	 */
	public static Image graph_upper_left;
	/**
	 * Graph image segment 'upper middle'
	 */
	public static Image graph_upper_middle;
	/**
	 * Color used to fill the area underneath the graph
	 */
	public static Color alphaAqua;
	/**
	 * Whether or not transparency is enabled
	 */
	public static boolean transparency = false;
	/**
	 * Transparent color
	 */
	public static final Color transparent = new Color(0.0F, 0.0F, 0.0F, 0.0F);
	/**
	 * Checkmark icon
	 */
	protected static Image checkmark;
	/**
	 * Arrow icon
	 */
	protected static Image arrow;
	/**
	 * Active panel color
	 */
	public static Color activeColor;
	
	/**
	 * Prepares the colours and images used by the program.
	 * @throws IOException When an IOException occurs
	 */
	public static final void prepareImages() throws IOException{
		ThemeSettings config = Main.config.getTheme();
		Color foreground = config.getForeground().getColor();
		transparency = config.isTransparencyRequired();

		if(config.hasCustomColors()){
			alphaAqua = new Color(foreground.getRed(), foreground.getGreen(), foreground.getBlue(), 51);
			graph_lower_right  = dye(ImageIO.read(ClassLoader.getSystemResource("graph_lower_right.png")), foreground);
			graph_lower_left   = dye(ImageIO.read(ClassLoader.getSystemResource("graph_lower_left.png")), foreground);
			graph_lower_middle = dye(ImageIO.read(ClassLoader.getSystemResource("graph_lower_middle.png")), foreground);
			graph_side_left    = dye(ImageIO.read(ClassLoader.getSystemResource("graph_side_left.png")), foreground);
			graph_side_right   = dye(ImageIO.read(ClassLoader.getSystemResource("graph_side_right.png")), foreground);
			graph_upper_right  = dye(ImageIO.read(ClassLoader.getSystemResource("graph_upper_right.png")), foreground);
			graph_upper_left   = dye(ImageIO.read(ClassLoader.getSystemResource("graph_upper_left.png")), foreground);
			graph_upper_middle = dye(ImageIO.read(ClassLoader.getSystemResource("graph_upper_middle.png")), foreground);
			activeColor = new Color(repaintPixel(new Color(221, 255, 255).getRGB(), foreground));
		}else{
			alphaAqua = new Color(0.0F, 1.0F, 1.0F, 0.2F);
			graph_lower_right  = ImageIO.read(ClassLoader.getSystemResource("graph_lower_right.png"));
			graph_lower_left   = ImageIO.read(ClassLoader.getSystemResource("graph_lower_left.png"));
			graph_lower_middle = ImageIO.read(ClassLoader.getSystemResource("graph_lower_middle.png"));
			graph_side_left    = ImageIO.read(ClassLoader.getSystemResource("graph_side_left.png"));
			graph_side_right   = ImageIO.read(ClassLoader.getSystemResource("graph_side_right.png"));
			graph_upper_right  = ImageIO.read(ClassLoader.getSystemResource("graph_upper_right.png"));
			graph_upper_left   = ImageIO.read(ClassLoader.getSystemResource("graph_upper_left.png"));
			graph_upper_middle = ImageIO.read(ClassLoader.getSystemResource("graph_upper_middle.png"));
			activeColor = new Color(221, 255, 255);
		}
		
		checkmark = dye(ImageIO.read(ClassLoader.getSystemResource("checkmark.png")), foreground);
		arrow = dye(ImageIO.read(ClassLoader.getSystemResource("arrow.png")), foreground);
	}
	
	/**
	 * Parses the given image in order to change its colours to the config foreground color.
	 * @param img The image to parse.
	 * @param foreground The configured foreground color to dye with.
	 * @return The recoloured image.
	 */
	private static final BufferedImage dye(BufferedImage img, Color foreground){
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				img.setRGB(x, y, repaintPixel(img.getRGB(x, y), foreground));
			}
		}
		return img;
	}
	
	/**
	 * Mixes the two given colours where the new color is made
	 * twice as prominent as the  original color while the
	 * alpha value is copied from the original color.
	 * @param original The original color.
	 * @param newcolor The new color.
	 * @return The two colours mixed.
	 */
	private static final int repaintPixel(int original, Color newcolor){
		return (((original >> 24) & 0x000000FF) << 24) | (((((original >> 16) & 0x000000FF) + 2 * newcolor.getRed()) / 3) << 16) | (((((original >> 8) & 0x000000FF) + 2 * newcolor.getGreen()) / 3) << 8) | (((original & 0x000000FF) + 2 * newcolor.getBlue()) / 3);
	}
}
