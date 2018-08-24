package me.roan.kps.layout;

public abstract interface Positionable{

	public abstract void setX(int x);
	
	public abstract void setY(int y);
	
	public abstract void setWidth(int w);
	
	public abstract void setHeight(int h);
	
	public abstract String getName();
	
	public abstract int getX();
	
	public abstract int getY();
	
	public abstract int getWidth();
	
	public abstract int getHeight();
}
