package me.roan.kps.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;

import me.roan.kps.SizeManager;

/**
 * 
 * @author Roan
 */
public class Layout implements LayoutManager, LayoutManager2{
	
	private int maxw;
	private int maxh;
	private int extraWidth = 0;
	private int extraHeight = 0;
	private final Container parent;
	
	public Layout(Container parent){
		this.parent = parent;
		parent.setLayout(this);
	}
	
	public void removeAll(){
		parent.removeAll();
		extraWidth = 0;
		extraHeight = 0;
	}
	
	public int getLayoutWidth(){
		return maxw;
	}
	
	public int getWidth(){
		return SizeManager.cellSize * (maxw + extraWidth);
	}
	
	public int getHeight(){
		return SizeManager.cellSize * (maxh + extraHeight);
	}
	
	public void add(Component comp) throws InvalidLayoutException{
		LayoutPosition lp = (LayoutPosition)comp;
		System.out.println("Adding component: " + lp.getLayoutLocation());
		//validate(lp.getLayoutX(), lp.getLayoutY(), lp.getLayoutWidth(), lp.getLayoutHeight(), lp);
		if(lp.getLayoutX() != -1){
			maxw = Math.max(maxw, lp.getLayoutX() + lp.getLayoutWidth());
		}else{
			extraWidth += lp.getLayoutWidth();
		}
		if(lp.getLayoutY() != -1){
			maxh = Math.max(maxh, lp.getLayoutY() + lp.getLayoutHeight());
		}else{
			extraHeight += lp.getLayoutHeight();
		}
		if(comp.getParent() == null){
			parent.add(comp);
		}
	}
	
	private void recomputeGrid(){
		LayoutPosition lp;
		maxw = 0;
		maxh = 0;
		for(Component component : parent.getComponents()){
			lp = (LayoutPosition)component;
			maxw = Math.max(maxw, lp.getLayoutX() + lp.getLayoutWidth());
			maxh = Math.max(maxh, lp.getLayoutY() + lp.getLayoutHeight());
		}
	}
	
	@Override
	public String toString(){
		return "Layout[components=" + parent.getComponentCount() + ",maxw=" + maxw + ",maxh=" + maxh + "]";
	}
	
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		try {
			add(comp);
		} catch (InvalidLayoutException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		try {
			add(comp);
		} catch (InvalidLayoutException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.5F;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.5F;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		recomputeGrid();
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(0, 0);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(0, 0);
	}

	@Override
	public void layoutContainer(Container parent) {
		if(!(maxw == 0 && extraWidth == 0) && !(maxh == 0 && extraHeight == 0)){
			double dx = parent.getWidth() / (maxw + extraWidth);
			double dy = parent.getHeight() / (maxh + extraHeight);
			LayoutPosition lp;
			int width = maxw;
			int height = maxh;
			for(Component component : parent.getComponents()){
				lp = (LayoutPosition)component;
				if(lp.getLayoutX() == -1){
					if(lp.getLayoutY() == -1){
						component.setBounds((int)Math.floor(dx * width), 
			                                (int)Math.floor(dy * height), 
			                                (int)Math.ceil(dx * lp.getLayoutWidth()), 
			                                (int)Math.ceil(dy * lp.getLayoutHeight()));
						width += lp.getLayoutWidth();
						height += lp.getLayoutHeight();
					}else{
						component.setBounds((int)Math.floor(dx * width), 
						                    (int)Math.floor((lp.getLayoutHeight() == -1 ? 0 : dy) * (maxh - lp.getLayoutY() - lp.getLayoutHeight())), 
						                    (int)Math.ceil(dx * lp.getLayoutWidth()), 
						                    (int)Math.ceil(dy * (lp.getLayoutHeight() == -1 ? height : lp.getLayoutHeight())));
						width += lp.getLayoutWidth();
					}
				}else{
					if(lp.getLayoutY() == -1){
						component.setBounds((int)Math.floor((lp.getLayoutWidth() == -1 ? 0 : dx) * lp.getLayoutX()), 
						                    (int)Math.floor(dy * height), 
						                    (int)Math.ceil(dx * (lp.getLayoutWidth() == -1 ? width : lp.getLayoutWidth())), 
						                    (int)Math.ceil(dy * lp.getLayoutHeight()));
						height += lp.getLayoutHeight();
					}else{
						component.setBounds((int)Math.floor((lp.getLayoutWidth() == -1 ? 0 : dx) * lp.getLayoutX()), 
						                    (int)Math.floor((lp.getLayoutHeight() == -1 ? 0 : dy) * (maxh - lp.getLayoutY() - lp.getLayoutHeight())), 
						                    (int)Math.ceil(dx * (lp.getLayoutWidth() == -1 ? width : lp.getLayoutWidth())), 
						                    (int)Math.ceil(dy * (lp.getLayoutHeight() == -1 ? height : lp.getLayoutHeight())));
					}
				}
			}
		}
	}
	
	public static class InvalidLayoutException extends Exception{
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = -6137260732635701944L;	
	}
	
	@Deprecated
	public static final class LayoutOverlapException extends InvalidLayoutException{
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = -4501441399333364320L;
	}
	
	@Deprecated
	public static final class NoAreaException extends InvalidLayoutException{
		/**
		 * Serial ID
		 */
		private static final long serialVersionUID = 4640131353893912934L;
	}
}
