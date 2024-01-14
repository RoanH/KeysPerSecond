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
package dev.roanh.kps.config;

import java.awt.Color;
import java.util.Locale;
import java.util.Objects;

/**
 * Class representing a theme color.
 * @author Roan
 */
public class ThemeColor{
	/**
	 * The RBG component of this color.
	 */
	private final Color color;
	/**
	 * The alpha component of this color.
	 */
	private final float alpha;
	
	/**
	 * Constructs a new theme color.
	 * @param rgb The RGB component of this color.
	 * @param alpha The alpha component of this color (0 - 255).
	 */
	public ThemeColor(int rgb, int alpha){
		this(rgb, alpha / 255.0F);
	}
	
	/**
	 * Constructs a new theme color.
	 * @param rgb The RGB component of this color.
	 * @param alpha The alpha component of this color (0 - 1).
	 */
	public ThemeColor(int rgb, float alpha){
		color = new Color(rgb);
		this.alpha = alpha;
	}
	
	/**
	 * Constructs a new theme color.
	 * @param r The red component of this color (0 - 255).
	 * @param g The green component of this color (0 - 255).
	 * @param b The blue component of this color (0 - 255).
	 * @param alpha The alpha component of this color (0 - 255).
	 */
	public ThemeColor(int r, int g, int b, int alpha){
		this(r, g, b, alpha / 255.0F);
	}
	
	/**
	 * Constructs a new theme color.
	 * @param r The red component of this color (0 - 255).
	 * @param g The green component of this color (0 - 255).
	 * @param b The blue component of this color (0 - 255).
	 * @param alpha The alpha component of this color (0 - 1).
	 */
	public ThemeColor(int r, int g, int b, float alpha){
		color = new Color(r, g, b);
		this.alpha = alpha;
	}
	
	/**
	 * Gets the RGB component of this color.
	 * @return The RGB component of this color.
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * Gets the alpha component of this color
	 * @return The alpha component of this color.
	 */
	public float getAlpha(){
		return alpha;
	}
	
	/**
	 * Gets the RGB component of this color.
	 * @return The RGB component of this color.
	 */
	public int getRGB(){
		return color.getRGB() & 0xFFFFFF;
	}
	
	/**
	 * Converts this color to a HEX string in the format
	 * RRGGBBAA, where the alpha part will be omitted if
	 * the alpha value is equal to 255 (max opacity).
	 * @return A HEX string for this color.
	 */
	public String toHex(){
		int alpha = Math.round(this.alpha * 255.0F);
		String rgb = String.format("%6s", Integer.toHexString(getRGB())).replace(' ', '0').toUpperCase(Locale.ROOT);
		
		if(alpha == 0xFF){
			return rgb;
		}else if(alpha < 16){
			return rgb + "0" + Integer.toHexString(alpha).toUpperCase(Locale.ROOT);
		}else{
			return rgb + Integer.toHexString(alpha).toUpperCase(Locale.ROOT);
		}
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof ThemeColor){
			ThemeColor other = (ThemeColor)obj;
			return other.color.equals(color) && Float.compare(alpha, other.alpha) == 0;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(color, alpha);
	}
	
	@Override
	public String toString(){
		return "#" + toHex();
	}
}
