/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.evaluate.functions.string;


import com.ae.evaluate.*;
import com.ae.core.*;

/** funcion encargada de retornar la ultima alicuota de la lista de 
 * alicuotas del iva disponible para el periodo grabable. El primer 
 * argumento es la fecha en formato estandar y el segundo puede ser una 
 * de los siguientes valores:
 * - 0 para aliguota general
 * - 1 para aliguota general + adicional
 * - 2 para aliguota reducida
 * 
 */
public class Aliquot implements Function {

	private AppEntry[] aliList;

	/** nueva instancia 
	 * 
	 */
	public Aliquot() {

	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#getName()
	 */
	public String getName() {
		return "Aliquot";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#execute(com.ae.evaluate.Evaluator, java.lang.String)
	 */
	public String execute(Evaluator evaluator, String s) throws FunctionException {
		String arg[] = s.split(",");
		this.aliList = AppConstants.getConstantsOfType("ali_iva");
		String iva = "";
		int d = (new Double(arg[0].trim())).intValue();
		for (int i = 0; i < aliList.length; i++) {
			if (Integer.parseInt(((String) aliList[i].getKey())) < d) {
				String[] iv = ((String) aliList[i].getValue()).split(";");
				iva = iv[Integer.parseInt(arg[1].trim())];
			}
		}
		return iva;
	}
}