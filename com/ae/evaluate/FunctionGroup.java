
package com.ae.evaluate;

import java.util.List;

public interface FunctionGroup {

	public abstract String getName();

	public abstract List getFunctions();

	public abstract void load(Evaluator evaluator);

	public abstract void unload(Evaluator evaluator);
}