package me.roan.kps.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

import me.roan.kps.Main;

/**
 * Layout manager that handles the layout
 * of all the panels in the program
 * @author Roan
 */
public class Layout implements LayoutManager2{
	/**
	 * The maximum width of the layout in cells
	 */
	private int maxw;
	/**
	 * The maximum height of the layout in cells
	 */
	private int maxh;
	/**
	 * Extra width for the layout in cells.
	 * Extra width comes from panels that are
	 * rendered in the end position as those
	 * do not count towards {@link #maxw}
	 */
	private int extraWidth = 0;
	/**
	 * Extra height for the layout in cells.
	 * Extra height comes from panels that are
	 * rendered in the end position as those
	 * do not count towards {@link #maxh}
	 */
	private int extraHeight = 0;
	/**
	 * The container this is the
	 * LayoutManager for
	 */
	private final Container parent;

	/**
	 * Constructs a new Layout for
	 * the given container
	 * @param parent The container this
	 *        will be the layout for
	 */
	public Layout(Container parent){
		this.parent = parent;
		parent.setLayout(this);
	}

	/**
	 * Removes all the components that
	 * were added to this layout
	 */
	public void removeAll(){
		parent.removeAll();
		extraWidth = 0;
		extraHeight = 0;
	}

	/**
	 * Gets the width in pixels of this layout
	 * @return The width in pixels of this layout
	 */
	public int getWidth(){
		return Main.config.cellSize * (maxw + extraWidth);
	}

	/**
	 * Gets the height in pixels of this layout
	 * @return The height in pixels of this layout
	 */
	public int getHeight(){
		return Main.config.cellSize * (maxh + extraHeight);
	}

	/**
	 * Adds the given component to this layout
	 * @param comp The component to add
	 */
	public void add(Component comp){
		LayoutPosition lp = (LayoutPosition)comp;
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

	/**
	 * Recomputes the size of the layout
	 */
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
	public void addLayoutComponent(Component comp, Object constraints){
		add(comp);
	}

	@Override
	public void addLayoutComponent(String name, Component comp){
		add(comp);
	}

	@Override
	public Dimension maximumLayoutSize(Container target){
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public float getLayoutAlignmentX(Container target){
		return 0.5F;
	}

	@Override
	public float getLayoutAlignmentY(Container target){
		return 0.5F;
	}

	@Override
	public void invalidateLayout(Container target){
	}

	@Override
	public void removeLayoutComponent(Component comp){
		recomputeGrid();
	}

	@Override
	public Dimension preferredLayoutSize(Container parent){
		return new Dimension(0, 0);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent){
		return new Dimension(0, 0);
	}

	@Override
	public void layoutContainer(Container parent){
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
						                    (int)Math.ceil(dy * (lp.getLayoutHeight() == -1 ? (maxh + extraHeight) : lp.getLayoutHeight())));
						width += lp.getLayoutWidth();
					}
				}else{
					if(lp.getLayoutY() == -1){
						component.setBounds((int)Math.floor((lp.getLayoutWidth() == -1 ? 0 : dx) * lp.getLayoutX()), 
						                    (int)Math.floor(dy * height), 
						                    (int)Math.ceil(dx * (lp.getLayoutWidth() == -1 ? (maxw + extraWidth) : lp.getLayoutWidth())), 
						                    (int)Math.ceil(dy * lp.getLayoutHeight()));
						height += lp.getLayoutHeight();
					}else{
						component.setBounds((int)Math.floor((lp.getLayoutWidth() == -1 ? 0 : dx) * lp.getLayoutX()), 
						                    (int)Math.floor((lp.getLayoutHeight() == -1 ? 0 : dy) * (maxh - lp.getLayoutY() - lp.getLayoutHeight())), 
						                    (int)Math.ceil(dx * (lp.getLayoutWidth() == -1 ? (maxw + extraWidth) : lp.getLayoutWidth())), 
						                    (int)Math.ceil(dy * (lp.getLayoutHeight() == -1 ? (maxh + extraHeight) : lp.getLayoutHeight())));
					}
				}
			}
		}
	}
}
