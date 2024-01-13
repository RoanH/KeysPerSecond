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

import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;

/**
 * Formatter factory that creates a formatter that rejects
 * content containing characters that are illegal in file paths.
 * @author Roan
 */
public class FilePathFormatterFactory extends AbstractFormatterFactory{
	/**
	 * List of characters that are invalid in file paths.
	 */
	private static final char[] INVALID_CHARS = new char[]{'/', '\\', '?', '%', '*', ':', '|', '"', '<', '>'};

	@Override
	public AbstractFormatter getFormatter(JFormattedTextField tf){
		return new AbstractFormatter(){
			/**
			 * Serial ID
			 */
			private static final long serialVersionUID = 5956641218097576666L;

			@Override
			public Object stringToValue(String text) throws ParseException{
				int errorPos = computeErrorPos(text);
				if(errorPos == -1){
					return text;
				}else{
					throw new ParseException("Invalid character found", errorPos);
				}
			}

			@Override
			public String valueToString(Object value) throws ParseException{
				return value instanceof String ? (String)value : null;
			}
		};
	}
	
	/**
	 * Finds the first position in the given path string that
	 * contains an invalid character for a file path.
	 * @param path The path to check.
	 * @return The position of the first invalid character or
	 *         -1 if there are no invalid characters in the path.
	 */
	private static final int computeErrorPos(String path){
		for(char ch : INVALID_CHARS){
			int index = path.indexOf(ch);
			if(index != -1){
				return index;
			}
		}
		
		return -1;
	}
	
	/**
	 * Tests if there are any invalid characters in the given file path.
	 * @param path The file path to check.
	 * @return True if the file path does not contain any invalid characters.
	 */
	public static final boolean isValid(String path){
		return computeErrorPos(path) == -1;
	}
}
