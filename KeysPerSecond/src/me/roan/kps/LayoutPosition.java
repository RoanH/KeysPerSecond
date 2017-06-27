package me.roan.kps;

/**
 * Interface used to sort both
 * base panels and key panels
 * @author Roan
 */
public abstract interface LayoutPosition {
	/**
	 * Returns the layout index of the component
	 * @return The layout index
	 */
	public abstract int getIndex();
}
