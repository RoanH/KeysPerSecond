package dev.roanh.kps.layout;

import dev.roanh.kps.RenderingMode;

/**
 * Configuration interface for panels
 * that can be added to the layout
 * @author Roan
 */
public abstract interface Positionable{
	/**
	 * Sets the x position for this panel
	 * @param x The new x position
	 */
	public abstract void setX(int x);

	/**
	 * Sets the y position for htis panel
	 * @param y The new y position
	 */
	public abstract void setY(int y);

	/**
	 * Sets the width for this panel
	 * @param w The new width
	 */
	public abstract void setWidth(int w);

	/**
	 * Sets the height for this panel
	 * @param h The new height
	 */
	public abstract void setHeight(int h);

	/**
	 * Gets the display name for this panel
	 * @return The display name for this panel
	 */
	public abstract String getName();

	/**
	 * Gets the x position for this panel
	 * @return The x position for this panel
	 */
	public abstract int getX();

	/**
	 * Gets the y position for this panel
	 * @return The y position for this panel
	 */
	public abstract int getY();

	/**
	 * Gets the width for this panel
	 * @return The width for this panel
	 */
	public abstract int getWidth();

	/**
	 * Gets the height for this panel
	 * @return The height for this panel
	 */
	public abstract int getHeight();

	/**
	 * Gets the rendering mode for this panel
	 * @return The rendering mode for this panel
	 */
	public abstract RenderingMode getRenderingMode();

	/**
	 * Sets the rendering mode for this panel
	 * @param mode The new rendering mode
	 */
	public abstract void setRenderingMode(RenderingMode mode);
}
