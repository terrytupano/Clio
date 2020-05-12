
package com.ae.evaluate.functions.math;

import com.ae.evaluate.*;
import java.util.*;

public class MathFunctions
	implements FunctionGroup {

	public MathFunctions() {
		a = new ArrayList();
		a.add(new Max());
		a.add(new Min());
		a.add(new DoubleDate());
		a.add(new DateDistance());
		a.add(new ISign());
	}

	public String getName() {
		return "numberFunctions";
	}

	public List getFunctions() {
		return a;
	}

	public void load(Evaluator evaluator) {
		for(Iterator iterator = a.iterator(); iterator.hasNext(); evaluator.putFunction((Function)iterator.next()));
	}

	public void unload(Evaluator evaluator) {
		for(Iterator iterator = a.iterator(); iterator.hasNext(); evaluator.removeFunction(((Function)iterator.next()).getName()));
	}

	private List a;
}