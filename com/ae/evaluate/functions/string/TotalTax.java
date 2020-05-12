/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.evaluate.functions.string;

import com.ae.evaluate.*;
import com.ae.core.*;

/** Funcion que retorna el total del impuesto para el año grabable expresado en
 * unidades tributarias. Los argumentos de esta funcion son:
 * - la seccion que contiene el valor la renta neta para determinar el porcentaje 
 * y sustraendo expresado en unidades tributarias
 * - la tarifa. Este es un entero que indica cual tarifa sera aplicada. Los
 * valores son 1 para tarifas articulo 50 y 2 para tarifas del articulo 52
 * - 0 para indicar que no se deseal al calculo con el sustraendo aplicado. 1
 * indica que se aplica el sustraendo.
 * 
 * 
 */
public class TotalTax implements Function {

	private AppEntry[] article5x;

	/** nueva instancia 
	 * 
	 */
	public TotalTax() {
		this.article5x = null;	
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#getName()
	 */
	public String getName() {
		return "totalTax";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#execute(com.ae.evaluate.Evaluator, java.lang.String)
	 */
	public String execute(Evaluator evaluator, String s) throws FunctionException {
		String arg[] = s.split(",");
		int flg = Integer.parseInt(arg[2].trim());
		double apraisal = Double.parseDouble(arg[0]);
		
		// retorna cero
		if (apraisal == 0) {
			return "0.0";
		}
		
		if (arg[1].trim().equals("1")) {
			this.article5x = AppConstants.getConstantsOfType("article50Table");	
		} else {
			this.article5x = AppConstants.getConstantsOfType("article52Table");	
		}
		
		int per = 0;
		int sus = 0;
		
		// busca rango 
		for (int c = 0; c < article5x.length; c++) {
			String[] frac = ((String) article5x[c].getKey()).split(";");
			int min = Integer.parseInt(frac[0]);
			int max = Integer.parseInt(frac[1]);
			if ((min < apraisal && apraisal <= max) || (c == article5x.length - 1)) {
				String[] ps = ((String) article5x[c].getValue()).split(";");
				per = Integer.parseInt(ps[0]);
				sus = Integer.parseInt(ps[1]);
				break;
			}
		}
		// determina si se aplica el sustraendo
		if (flg == 0) {
			sus = 0;
		}
		
		// se calcula el porcentaje y se le resta el sustraendo
		return String.valueOf((apraisal * per) / 100 - sus);
	}
}