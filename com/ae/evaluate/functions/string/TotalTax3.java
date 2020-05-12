/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.evaluate.functions.string;

import com.ae.evaluate.*;
import com.ae.core.*;

/** Funcion que retorna el monto una vez aplicado el porcentaje especificado
 * en la tarifa 3 Los argumentos son
 * - id de seccion con el monto de los ingresos percividos por explotacion
 * petrolera o minera
 * - valor del campo tpActEco que indica la actividad del contribuyente
 * 
 * si la actividad no es ni minera ni de eplotacion petrolera se retorna 
 * el monto de entrada
 * 
 */
public class TotalTax3 implements Function {

	private AppEntry[] article53;

	/** nueva instancia 
	 * 
	 */
	public TotalTax3() {
		
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#getName()
	 */
	public String getName() {
		return "totalTax3";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#execute(com.ae.evaluate.Evaluator, java.lang.String)
	 */
	public String execute(Evaluator evaluator, String s) throws FunctionException {
		this.article53 = AppConstants.getConstantsOfType("article53Table");	
		String arg[] = s.split(",");
		double money = Double.parseDouble(arg[0]);
		int per = 0;
		for (int c = 0; c < article53.length; c++) {
			if (((String) article53[c].getKey()).equals(arg[1].trim() + "_53")) {
				per = Integer.parseInt((String) article53[c].getValue());
			}
		}
		return (per != 0) ? String.valueOf((money * per) / 100): arg[0];
	}
}