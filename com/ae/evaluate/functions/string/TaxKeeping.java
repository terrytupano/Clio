/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.evaluate.functions.string;

import com.ae.evaluate.*;
import com.ae.core.*;

/** Funcion encargada de calcular el impuesto retenido. Los parametros esperados
 * son:
 * - "xxxxannl" donde 
 *   "xxxx" es el identificador de la forma, 
 *   "a" el articulo, 
 *   "nn" el numeral
 *   "l" el literal   
 * - monto monto a calcula retencion.
 * - fecha 
 * 
 * Segun la combinacion de estos, se obtiene el monto correcto que, segun la
 * ley, debe retenerse para la conbinacion esperada.
 * 
 * Como en la lista de constantes 1808List.xml el atributo code debe estar en
 * el mismo formato que el argumeto de entrada esta funcion determina si es 
 * aplicacble el sustraendo y montos desde determinando si xxxx es igual a 
 * "pn-r". 
 *
 * sustraendo = valor u.t * % * 83.3334
 * monto desde = valor u.t * 83.3334 
 *
 * la constante 83.3334 = 1000 / 12  
 * 
 * para la aplicacion de retenciones en tarifa 2 se procede: se obtiene la 
 * base imponible y a ello se le calcula la tarifa 2. este monto es la 
 * retencion. 
 * 
 * 
 */
public class TaxKeeping implements Function {
	
	private LastTributaryUnit lasttu;
	private TotalTax totalT;
	private static double constante = 83.3334;

	/** nueva instancia 
	 * 
	 */
	public TaxKeeping() {
		this.lasttu = new LastTributaryUnit();
		this.totalT = new TotalTax();
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#getName()
	 */
	public String getName() {
		return "taxKeeping";
	}
	
	/*	
	 * (non-Javadoc)
	 * @see com.ae.evaluate.Function#execute(com.ae.evaluate.Evaluator, java.lang.String)
	 */
	public String execute(Evaluator evaluator, String s) throws FunctionException {
		String[] anlmf = s.split(",");
		String cid = anlmf[0].trim().substring(1, anlmf[0].length() - 1);
		double monto = Double.parseDouble(anlmf[1].trim());
		AppEntry law = AppConstants.getConstant(cid);
		String[] bp = ((String) law.getValue()).split(";");

		double porcent = 0.0;
		double basei = Double.parseDouble(bp[0]);
		double result = 0.0;
		Double d = new Double(anlmf[2]);
		String dat = String.valueOf(d.intValue());
		
		// tarifa 2
		if (bp[1].equals("T2")) {
			basei = monto * basei / 100;
			// en unidades tributarias
			double tu = Double.parseDouble(lasttu.execute(null, dat + ", 0"));
			basei = basei / tu;
			result = Double.parseDouble(
				totalT.execute(null, String.valueOf(basei + ", 2, 0")));
			result = result * tu;  
		} else  {
			porcent = Double.parseDouble(bp[1]);
			basei = monto * basei / 100;
			double[] sb = getSubstractAndBase(cid, dat, porcent);	
			// si monto es mayor a monto base
			if (monto > sb[1]) {	
				result = ((basei * porcent / 100) - sb[0]);
			}
		}
		
		return String.valueOf((result > 0) ? result : 0);
	}
	
	/** metodo que centraliza en esta funcion, el calculo del sustraendo y 
	 * monto base segun el decreto 1808. 
	 * 
	 * @param cid - beneficiario  
	 * @param dat - fecha para obtener la u.t.
	 * @param pr - porcentaje de retencion segun argumento cid.
	 * @return - arreglo de dos elementos. El primero es el sustraendo y 
	 * el segundo el monto base. Todo en bs.
	 * @throws FunctionException
	 */
	public double[] getSubstractAndBase(String cid, String dat, double pr) 
		throws FunctionException {
		double[] su_ba = new double[] {0.0, 0.0};
		if (cid.startsWith("pn-r")) {
			double ut = Double.parseDouble(lasttu.execute(null, dat + ", 0"));
			su_ba[0] = ut * pr / 100 * constante;
			su_ba[1] = ut * constante;
		}
		if (cid.startsWith("pj-d") && (pr == 3 || pr == 5)) {
			double ut = Double.parseDouble(lasttu.execute(null, dat + ", 0"));
			su_ba[1] = 25000;
		}
		return su_ba;
	}
}