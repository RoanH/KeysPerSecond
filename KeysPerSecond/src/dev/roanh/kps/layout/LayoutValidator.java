/*
 * KeysPerSecond: An open source input statistics displayer.
 * Copyright (C) 2017  Roan Hofland (roan@roanh.dev).  All rights reserved.
 * GitHub Repository: https://github.com/RoanH/KeysPerSecond
 *
 * KeysPerSecond is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeysPerSecond is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.roanh.kps.layout;

import dev.roanh.kps.ui.model.SpecialNumberModel;

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
	 * Constructs and initialises a new LayoutValidator.
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
		 * Prevent outside initialisation
		 */
		private FieldListener(){
		}

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
		
		/**
		 * Gets the model for this field listener.
		 * @return The model for this field listener.
		 */
		public final SpecialNumberModel getModel(){
			return model;
		}
	}
}
