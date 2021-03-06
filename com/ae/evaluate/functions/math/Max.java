
package com.ae.evaluate.functions.math;

import com.ae.evaluate.*;
import java.util.ArrayList;


public class Max
	implements Function {


	public Max() {
	}
	

	public String getName() {
		return "max";
	}
	

	public String execute(Evaluator evaluator, String s) throws FunctionException {
		Double double1 = null;
		ArrayList arraylist = FunctionHelper.getDoubles(s, ',');
		if(arraylist.size() != 2)
			throw new FunctionException("Two numeric arguments are required.");
		try {
			double d = ((Double)arraylist.get(0)).doubleValue();
			double d1 = ((Double)arraylist.get(1)).doubleValue();
			double1 = new Double(Math.max(d, d1));
		}
		catch(Exception exception) {
			throw new FunctionException("Two numeric arguments are required.");
		}
		return double1.toString();
	}
}