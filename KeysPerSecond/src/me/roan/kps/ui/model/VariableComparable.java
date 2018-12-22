package me.roan.kps.ui.model;

import java.util.function.Supplier;

public class VariableComparable extends Number implements Comparable<Integer>{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 7783953216941858736L;
	private Supplier<Integer> bound;
	
	public VariableComparable(Supplier<Integer> bound){
		this.bound = bound;
	}

	@Override
	public int compareTo(Integer o){
		return bound.get().compareTo(o);
	}

	@Override
	public int intValue(){
		return bound.get();
	}

	@Override
	public long longValue(){
		return bound.get();
	}

	@Override
	public float floatValue(){
		return bound.get();
	}

	@Override
	public double doubleValue(){
		return bound.get();
	}
}
