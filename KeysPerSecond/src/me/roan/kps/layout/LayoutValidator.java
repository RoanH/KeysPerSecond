package me.roan.kps.layout;

import me.roan.kps.ui.model.SpecialNumberModel;

/**
 * Validator that ensures that only
 * valid panel configuration get added
 * to the layout
 * @author Roan
 * @see Layout
 * @see LayoutPosition
 */
public class LayoutValidator{
	/**
	 * Change listener for the x position editor
	 */
	private FieldListener x = new FieldListener();
	/**
	 * Change listener for the y position editor
	 */
	private FieldListener y = new FieldListener();
	/**
	 * Change listener for the width editor
	 */
	private FieldListener width = new FieldListener();
	/**
	 * Change listener for the height editor
	 */
	private FieldListener height = new FieldListener();

	/**
	 * Constructs and initialises a new LayoutValidator
	 */
	public LayoutValidator(){
		x.incompatible = width;
		width.incompatible = x;
		y.incompatible = height;
		height.incompatible = y;
	}

	/**
	 * Gets the field listener that should be
	 * registered to the x position editor
	 * @return The listener for the x position editor
	 */
	public final FieldListener getXField(){
		return x;
	}

	/**
	 * Gets the field listener that should be
	 * registered to the y position editor
	 * @return The listener for the y position editor
	 */
	public final FieldListener getYField(){
		return y;
	}

	/**
	 * Gets the field listener that should be
	 * registered to the width editor
	 * @return The listener for the width editor
	 */
	public final FieldListener getWidthField(){
		return width;
	}

	/**
	 * Gets the field listener that should be
	 * registered to the height editor
	 * @return The listener for the height editor
	 */
	public final FieldListener getHeightField(){
		return height;
	}

	/**
	 * Listener that listens for and validates
	 * changes for the editor this listener is
	 * associated with
	 * @author Roan
	 */
	public static final class FieldListener{
		/**
		 * The listener attached to the model
		 * that the model for this listener is
		 * incompatible with
		 */
		private FieldListener incompatible;
		/**
		 * The model this listener is listening on
		 */
		private SpecialNumberModel model;

		/**
		 * Checks to see if it is valid for
		 * the model this listener is attached
		 * to to select the special value. The 
		 * special value is either the <i>end</i>
		 * or <i>max</i> value and these values
		 * are only valid if the model that the
		 * model of this listener is incompatible
		 * with does not have its special value
		 * selected.
		 * @return Whether or not the selecting the
		 *         special value is valid at this time
		 */
		public final boolean specialValid(){
			return !incompatible.model.isSpecialValueSelected();
		}

		/**
		 * Sets the model this listener is
		 * listening for changes on
		 * @param model The model this
		 *        listener is listening on
		 */
		public final void setModel(SpecialNumberModel model){
			this.model = model;
		}
	}
}
