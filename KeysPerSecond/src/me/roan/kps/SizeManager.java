package me.roan.kps;

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
	@Deprecated
	protected static int subComponentHeight = 68;
	/**
	 * Width of the graph when there are no
	 * panels configured (only a graph)
	 */
	@Deprecated
	protected static int defaultGraphWidth = 228;
	//TODO
	public final static int cellSize = 22;
	
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
