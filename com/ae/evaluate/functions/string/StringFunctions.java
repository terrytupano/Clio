

package com.ae.evaluate.functions.string;

import com.ae.evaluate.*;
import java.util.*;

public class StringFunctions
	implements FunctionGroup {

	public StringFunctions() {
		_fldif = new ArrayList();
		_fldif.add(new LastTributaryUnit());
		_fldif.add(new TotalTax());
		_fldif.add(new TotalTax3());
		_fldif.add(new TaxKeeping());
		_fldif.add(new Aliquot());
		_fldif.add(new FromSection());
	}

	public String getName() {
		return "stringFunctions";
	}

	public List getFunctions() {
		return _fldif;
	}

	public void load(Evaluator evaluator) {
		for(Iterator iterator = _fldif.iterator(); iterator.hasNext(); evaluator.putFunction((Function)iterator.next()));
	}

	public void unload(Evaluator evaluator) {
		for(Iterator iterator = _fldif.iterator(); iterator.hasNext(); evaluator.removeFunction(((Function)iterator.next()).getName()));
	}

	private List _fldif;
}