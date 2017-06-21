package me.roan.kps;

import java.awt.Font;
import java.awt.Graphics2D;

public enum RenderingMode {

	HORIZONTAL {
		@Override
		public void render(Graphics2D g, Font textFont, Font numberFont, String text, Number n) {
			// TODO Auto-generated method stub
			
		}
	},
	
	VERTICAL {
		@Override
		public void render(Graphics2D g, Font textFont, Font numberFont, String text, Number n) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public abstract void render(Graphics2D g, Font textFont, Font numberFont, String text, Number n);
}
