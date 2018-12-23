package me.roan.kps.ui.model;

import java.util.function.Supplier;

/**
 * Custom comparable to use as a variable
 * bound on a JSpinner
 * @author Roan
 * @see Number
 * @see Comparable
 */
public class DynamicInteger extends Number implements Comparable<Integer>{
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 7783953216941858736L;
	/**
	 * The value of this variable
	 */
	private Supplier<Integer> bound;
	
	/**
	 * Constructs a new DynamicInteger
	 * with the given variable bound
	 * @param bound The variable value
	 *        for this DynamicInteger
	 */
	public DynamicInteger(Supplier<Integer> bound){
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
