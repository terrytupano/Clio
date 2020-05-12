
package com.ae.evaluate;

public interface Function {

	public abstract String getName();

	public abstract String execute(Evaluator evaluator, String s) 
		throws FunctionException;
}