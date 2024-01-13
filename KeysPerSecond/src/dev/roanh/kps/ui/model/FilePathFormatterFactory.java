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

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;

/**
 * Formatter factory that creates a formatter that rejects
 * content containing characters that are illegal in file paths.
 * 
 * @author Roan
 */
public class FilePathFormatterFactory extends AbstractFormatterFactory{
	
	@Override
	public AbstractFormatter getFormatter(JFormattedTextField tf){
		return new AbstractFormatter(){
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = 5956641218097576666L;

			@Override
			public Object stringToValue(String text) throws ParseException{
				if(isValidPath(text)){
					return text;
				}else{
					throw new ParseException("Invalid character found", 0);
				}
			}

			@Override
			public String valueToString(Object value) throws ParseException{
				return value instanceof String ? (String)value : null;
			}
		};
	}
	
	/**
	 * Tests if there are any invalid characters in the given file path.
	 * @param path The file path to check.
	 * @return True if the file path does not contain any invalid characters.
	 */
	public static final boolean isValidPath(String path){
		try{
			Paths.get(path);
			return true;
		}catch(InvalidPathException e){
			return false;
		}
	}
}
