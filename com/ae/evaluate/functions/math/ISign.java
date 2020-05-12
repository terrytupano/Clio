/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.evaluate.functions.math;

import com.ae.evaluate.*;

/** funcion usada por precalculo.
 * 
 * invierte el signo del monto. ver SimpleInput
 * 
 * este tipo de funcion tambien debe estar dentro de AppConstants
 * 
 */
public class ISign implements Function {

	/** nueva instancia 
	 * 
	 */
	public ISign() {
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#getName()
	 */
	public String getName() {
		return "ISign";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#execute(com.ae.evaluate.Evaluator, java.lang.String)
	 */
	public String execute(Evaluator evaluator, String s) throws FunctionException {
		double d = Double.valueOf(s).doubleValue();
		d = d * -1;		
		return (new Double(d)).toString();
	}
}