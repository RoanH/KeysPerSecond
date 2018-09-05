package me.roan.kps;

/**
 * Simple class holding all the size
 * constants for the program
 * @author Roan
 */
public class SizeManager {
	//TODO
	public final static int cellSize = 22;
	protected final static int insideOffset = 5;
	
	//BasePanel & KeyPanel data
	/**
	 * Height of the title in a key panel
	 */
	protected final static int keyTitleTextOffset = 30;
	/**
	 * Height of the data / count in a key panel
	 */
	protected final static int keyDataTextOffset = 55;
	/**
	 * HORIZONTAL offset for the title text from the right side
	 */
	protected final static int sideTextOffset = 6;
	
	//Graph
	/**
	 * Smallest size of graph images
	 */
	public static int graphImageSize = 4;
	/**
	 * Graph offset from the sides of the panel
	 */
	public static int graphOffset = 5;
	
	//TODO javadoc
	public static final int borderSize(){
		return 2 + graphImageSize;
	}
}
