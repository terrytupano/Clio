
package com.ae.evaluate;

import com.ae.core.*;


public class EvaluateException extends Exception {

	public EvaluateException(String s) {
		super(DIMain.bundle.getString(s));
	}
}