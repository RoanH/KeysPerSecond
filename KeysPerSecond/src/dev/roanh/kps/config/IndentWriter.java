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
