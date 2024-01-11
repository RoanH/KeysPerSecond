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

public class IndentWriter{
	private PrintWriter writer;
	private int indent;
	private boolean nextIsListItem = false;
	
	public IndentWriter(PrintWriter writer){
		this.writer = writer;
	}
	
	public void startListItem(){
		indent += 4;
		nextIsListItem = true;
	}
	
	public void endListItem(){
		indent -= 4;
		nextIsListItem = false;
	}
	
	public void increaseIndent(){
		indent += 2;
	}
	
	public void decreaseIndent(){
		indent -= 2;
	}
	
	public void println(){
		writer.print('\n');
	}
	
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
