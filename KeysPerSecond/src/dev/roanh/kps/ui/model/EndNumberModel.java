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
package dev.roanh.kps.ui.model;

import dev.roanh.kps.layout.LayoutValidator.FieldListener;

/**
 * SpecialNumberModel with <i>end</i>
 * as the special value
 * @author Roan
 * @see SpecialNumberModel
 */
public class EndNumberModel extends SpecialNumberModel{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2971851618376128616L;

	/**
	 * Constructs a new EndNumberModel with the
	 * given initial value, validator and listener
	 * @param value The initial model value
	 * @param validator The validator to use to validate value changes
	 * @param listener The listener to inform of value changes
	 */
	public EndNumberModel(int value, FieldListener validator, ValueChangeListener listener){
		super(value, "end", validator, listener);
	}
}
