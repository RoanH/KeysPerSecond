package me.roan.kps.layout;

/**
 * Interface that panels that will be
 * added to a {@link Layout} have to implement
 * @author Roan
 * @see Layout
 */
public abstract interface LayoutPosition{
	/**
	 * Gets the x position of this
	 * panel in the layout
	 * @return The x position of this
	 *         panel in the layout
	 */
	public abstract int getLayoutX();

	/**
	 * Gets the y position of this
	 * panel in the layout
	 * @return The y position of this
	 *         panel in the layout
	 */
	public abstract int getLayoutY();

	/**
	 * Gets the width of this panel
	 * in the layout
	 * @return The width of this panel
	 *         in the layout
	 */
	public abstract int getLayoutWidth();

	/**
	 * Gets the height of this panel
	 * in the layout
	 * @return The height of this panel
	 *         in the layout
	 */
	public abstract int getLayoutHeight();

	/**
	 * Gets the position and size given by
	 * this LayoutPosition as a string
	 * @return A string giving the position
	 *         and size of this LayoutPosition
	 */
	public default String getLayoutLocation(){
		return "LayoutPosition[x=" + getLayoutX() + ",y=" + getLayoutY() + ",width=" + getLayoutWidth() + ",height=" + getLayoutHeight() + "]";
	}
}
