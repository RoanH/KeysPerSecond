package me.roan.kps.layout;

import me.roan.kps.ui.model.SpecialNumberModel;

public class LayoutValidator{

	private FieldListener x = new FieldListener();
	private FieldListener y = new FieldListener();
	private FieldListener width = new FieldListener();
	private FieldListener height = new FieldListener();
	
	public LayoutValidator(){
		x.incompatible = width;
		width.incompatible = x;
		y.incompatible = height;
		height.incompatible = y;
	}
	
	public final FieldListener getXField(){
		return x;
	}
	
	public final FieldListener getYField(){
		return y;
	}
	
	public final FieldListener getWidthField(){
		return width;
	}
	
	public final FieldListener getHeightField(){
		return height;
	}
	
	public static final class FieldListener{
		private FieldListener incompatible;
		private SpecialNumberModel model;
		
		public final boolean specialValid(){
			return !incompatible.model.isSpecialValueSelected();
		}
		
		public final void setModel(SpecialNumberModel model){
			this.model = model;
		}
	}
}
