/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.evaluate.functions.string;


import com.ae.evaluate.*;
import com.ae.core.*;

/** funcion encargada de retornar la ultima unidad tributaria (dentro del archivo
 * de unidades tributarias) para el año grabable. La funcion determina el 
 * tipo de argumento de entrada (si es un entero que representa el año o 
 * una instancia de <code>AppDate</code> cuyo valor viene en formato
 * AppDate.toString(). Si al argumento de entrada es -1, se asume 
 * este año.
 * 
 */
public class LastTributaryUnit1 implements Function {

	private String year;
	private String date;
	private AppEntry[] tuList;

	/** nueva instancia 
	 * 
	 */
	public LastTributaryUnit1() {
		this.year = new AppDate().toString().substring(0, 4);
		this.date = (new AppDate(year, "12", "31")).toString();
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#getName()
	 */
	public String getName() {
		return "lastTributaryUnit";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#execute(com.ae.evaluate.Evaluator, java.lang.String)
	 */
	public String execute(Evaluator evaluator, String s) throws FunctionException {
		
		this.tuList = AppConstants.getConstantsOfType("tu");
		// si no es -1 debe ser 'yyyy' o AppDate.toString()
		if (!s.equals("-1")) {
			int b = (s.substring(0, 1).equals("'")) ? 1 : 0;
			date = date.replaceAll(year, s.substring(b, 4 + b));
			year = s.substring(b, 4 + b);
		}
		double tv = 0.0;
		
		// if this is a declaration of anual type, sub 183 days
		
		
		int d = Integer.parseInt(date);
		for (int i = 0; i < tuList.length; i++) {
			tv = Integer.parseInt(((String) tuList[i].getKey())) < d ?
				Double.parseDouble((String) tuList[i].getValue()) : tv;
		}
		return String.valueOf(tv);
	}
}