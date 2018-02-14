package me.roan.kps;

/**
 * //TODO
 * @author Roan
 */
public abstract interface LayoutPosition {
	///**
	// * Returns the layout index of the component
	// * @return The layout index
	// */
	//@Deprecated
	//public abstract int getIndex();
	
	public abstract int getLayoutX();
	
	public abstract int getLayoutY();
	
	public abstract int getLayoutWidth();
	
	public abstract int getLayoutHeight();
	
	public default int getXWidth(){
		return getLayoutX() + getLayoutWidth();
	}
	
	public default int getYHeight(){
		return getLayoutY() + getLayoutHeight();
	}
	
	public default String getLayoutLocation(){
		return "LayoutPosition[x=" + getLayoutX() + ",y=" + getLayoutY() + ",width=" + getLayoutWidth() + ",height=" + getLayoutHeight() + "]";
	}
}
