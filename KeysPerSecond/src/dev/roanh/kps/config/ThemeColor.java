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

public class ThemeColor{
	private final Color color;
	private final float alpha;
	
	public ThemeColor(int rgb, int alpha){
		this(rgb, alpha / 255.0F);
	}
	
	public ThemeColor(int rgb, float alpha){
		color = new Color(rgb);
		this.alpha = alpha;
	}
	
	public ThemeColor(int r, int g, int b, int alpha){
		this(r, g, b, alpha / 255.0F);
	}
	
	public ThemeColor(int r, int g, int b, float alpha){
		color = new Color(r, g, b);
		this.alpha = alpha;
	}
	
	public Color getColor(){
		return color;
	}
	
	public float getAlpha(){
		return alpha;
	}
	
	public int getRGB(){
		return color.getRGB() & 0xFFFFFF;
	}
	
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
