package dev.roanh.kps.config;

import java.io.PrintWriter;

public class IndentWriter{
	private PrintWriter writer;
	
	private int indent;
	private boolean nextIsListItem = false;
	
	
	
	
	
	
	
	public void println(String val){
		if(nextIsListItem){
			writer.print("  - ");
			nextIsListItem = false;
		}else{
			for(int i = 0; i < indent; i++){
				writer.print(' ');
			}
		}
		
		writer.println(val);
	}
}
