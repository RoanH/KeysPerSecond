package me.roan.kps;

/**
 * Enum specifying all the different
 * layout positions for the graph
 * @author Roan
 */
public enum GraphMode{
	/**
	 * Indicates that the graph is placed
	 * in the same window as the other tiles
	 */
	INLINE("Inline"),
	/**
	 * Indicates that the graph is placed
	 * in it's own window
	 */
	DETACHED("Detached");

	/**
	 * The display name of this mode
	 */
	private String name;

	/**
	 * Constructs a new GraphMode with
	 * the given display name 
	 * @param name The display name for this mode
	 */
	private GraphMode(String name){
		this.name = name;
	}

	@Override
	public String toString(){
		return name;
	}
}
