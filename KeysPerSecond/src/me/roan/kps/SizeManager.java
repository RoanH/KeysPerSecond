package me.roan.kps;

import java.awt.Font;

/**
 * Simple class holding all the size
 * constants for the program
 * @author Roan
 */
public class SizeManager {
	//JFrame initialisation
	/**
	 * Height of the graph or a key panel
	 */
	protected static int subComponentHeight = 68;
	/**
	 * Width of a key panel
	 */
	protected static int keyPanelWidth = 44;
	/**
	 * Width of the graph when there are no
	 * panels configured (only a graph)
	 */
	protected static int defaultGraphWidth = 228;
	
	//BasePanel & KeyPanel data
	/**
	 * Height of the title in a key panel
	 */
	protected static int keyTitleTextOffset = 30;
	/**
	 * Height of the data / count in a key panel
	 */
	protected static int keyDataTextOffset = 55;
	/**
	 * HORIZONTAL offset for the title text from the right side
	 */
	protected static int horizontalTextOffset = 6;
	
	//Fonts
	/**
	 * Font size of the title for the stats
	 */
	protected static int basePanelTitleTextSize = 15;
	/**
	 * Font size for a key name
	 */
	protected static int keyPanelFont1Size = 24;
	/**
	 * Font size for a small key count value (n < 100)
	 */
	protected static int keyPanelFont2Size = 18;
	/**
	 * Font size for a medium key count value (n >= 100 && n < 1000)
	 */
	protected static int keyPanelFont2smallSize = 13;
	/**
	 * Font size for a huge key count value (n >= 1000)
	 */
	protected static int keyPanelFont2smallestSize = 10;
	
	//Graph
	/**
	 * Smallest size of graph images
	 */
	protected static int graphImageSize = 4;
	/**
	 * Graph offset from the sides of the panel
	 */
	protected static int graphOffset = 5;
	
	/**
	 * Multiplier all constants by the given factor
	 * @param factor The factor
	 */
	protected static final void scale(double factor){
		subComponentHeight        = (int)Math.ceil(factor * subComponentHeight);
		keyPanelWidth             = (int)Math.ceil(factor * keyPanelWidth);
		defaultGraphWidth         = (int)Math.ceil(factor * defaultGraphWidth);
		keyTitleTextOffset        = (int)Math.ceil(factor * keyTitleTextOffset);
		keyDataTextOffset         = (int)Math.ceil(factor * keyDataTextOffset);
		basePanelTitleTextSize    = (int)Math.floor(factor * basePanelTitleTextSize);
		keyPanelFont1Size         = (int)Math.floor(factor * keyPanelFont1Size);
		keyPanelFont2Size         = (int)Math.floor(factor * keyPanelFont2Size);
		keyPanelFont2smallSize    = (int)Math.floor(factor * keyPanelFont2smallSize);
		keyPanelFont2smallestSize = (int)Math.floor(factor * keyPanelFont2smallestSize);
		graphImageSize            = (int)Math.ceil(factor * graphImageSize);
		graphOffset               = (int)Math.ceil(factor * graphOffset);
		horizontalTextOffset      = (int)Math.ceil(factor * horizontalTextOffset);
		setFonts();
	}
	
	/**
	 * Sets the fonts
	 */
	private static final void setFonts(){
		BasePanel.font1 = new Font("Dialog", Font.BOLD, SizeManager.basePanelTitleTextSize);
		KeyPanel.font1 = new Font("Dialog", Font.BOLD, SizeManager.keyPanelFont1Size);                          		                                                                                         
		BasePanel.font2 = new Font("Dialog", Font.PLAIN, SizeManager.keyPanelFont2Size);
		BasePanel.font2small = new Font("Dialog", Font.PLAIN, SizeManager.keyPanelFont2smallSize);
		BasePanel.font2smallest = new Font("Dialog", Font.PLAIN, SizeManager.keyPanelFont2smallestSize);
	}
	
	/**
	 * Changes the component width & height values depending
	 * on the rendering mode
	 * @param oldMode The old rendering mode
	 * @param newMode The new rendering mode
	 * @see RenderingMode
	 */
	protected static final void setLayoutMode(RenderingMode oldMode, RenderingMode newMode){
		if(oldMode == newMode || (oldMode != RenderingMode.VERTICAL && newMode != RenderingMode.VERTICAL)){
			return;
		}else{
			int tmp = subComponentHeight;
			subComponentHeight = keyPanelWidth;
			keyPanelWidth = tmp;
		}
	}
}
