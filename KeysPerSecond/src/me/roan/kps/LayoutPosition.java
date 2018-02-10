package me.roan.kps;

/**
 * //TODO
 * @author Roan
 */
public abstract interface LayoutPosition {
	/**
	 * Returns the layout index of the component
	 * @return The layout index
	 */
	@Deprecated
	public abstract int getIndex();
	
	public abstract int getX();
	
	public abstract int getY();
	
	public abstract int getWidth();
	
	public abstract int getHeight();
}
