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
package dev.roanh.kps.config;

import java.io.PrintWriter;

/**
 * Utility class used to write config files.
 * @author Roan
 */
public class IndentWriter{
	/**
	 * The writer to write output to.
	 */
	private PrintWriter writer;
	/**
	 * The current indent of the writer in spaces.
	 */
	private int indent;
	/**
	 * True if the next println action writes the
	 * start of a new list item.
	 */
	private boolean nextIsListItem = false;
	
	/**
	 * Constructs a new indent writer with the
	 * given writer to forward writes to.
	 * @param writer The underlying writer to output to.
	 */
	public IndentWriter(PrintWriter writer){
		this.writer = writer;
	}
	
	/**
	 * Starts a new list item.
	 * @see #endListItem()
	 */
	public void startListItem(){
		indent += 4;
		nextIsListItem = true;
	}
	
	/**
	 * Ends the current list.
	 * @see #startListItem()
	 */
	public void endListItem(){
		indent -= 4;
		nextIsListItem = false;
	}
	
	/**
	 * Increase the indent of the writer.
	 * @see #decreaseIndent()
	 */
	public void increaseIndent(){
		indent += 2;
	}
	
	/**
	 * Decreases the indent of the writer.
	 * @see #increaseIndent()
	 */
	public void decreaseIndent(){
		indent -= 2;
	}
	
	/**
	 * Prints an empty line.
	 */
	public void println(){
		writer.print('\n');
		writer.flush();
	}
	
	/**
	 * Prints a new line to the configuration subject
	 * to the current indent and list settings.
	 * @param val The value to write.
	 */
	public void println(String val){
		for(int i = 0; i < (nextIsListItem ? (indent - 2) : indent); i++){
			writer.print(' ');
		}
		
		if(nextIsListItem){
			writer.print("- ");
			nextIsListItem = false;
		}
		
		writer.print(val);
		println();
	}
}
