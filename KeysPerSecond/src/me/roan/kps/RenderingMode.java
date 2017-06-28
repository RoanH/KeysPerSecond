package me.roan.kps;

/**
 * An enum specifying the different
 * text rendering modes
 * @author Roan
 */
public enum RenderingMode {
	/**
	 * HORIZONTAL text rendering
	 */
	HORIZONTAL_TN("Horizontal (text - value)"),
	/**
	 * HORIZONTAL text rendering
	 */
	HORIZONTAL_NT("Horizontal (value - text)"),
	/**
	 * VERTICAL text rendering
	 */
	VERTICAL("Vertical");
	
	/**
	 * The display name of the
	 * enum constant, used in dialogs
	 */
	private String name;
	
	/**
	 * Constructs a new RenderingMode
	 * with the given name
	 * @param n The display name of the mode
	 */
	private RenderingMode(String n){
		name = n;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
