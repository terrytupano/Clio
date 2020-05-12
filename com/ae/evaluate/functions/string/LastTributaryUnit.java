/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.evaluate.functions.string;


import java.util.*;

import com.ae.core.*;
import com.ae.evaluate.*;

/** funcion encargada de retornar la ultima unidad tributaria (dentro del archivo
 * de unidades tributarias) para el año grabable. se esperan 2 argumentos
 * de entrada: El primero se refiere a una fecha. La funcion determina el 
 * tipo de argumento (si es un entero que representa el año o 
 * una instancia de <code>AppDate</code> cuyo valor viene en formato
 * AppDate.toString(). Si al argumento de entrada es -1, se asume 
 * este año.
 * el segundo argumento indica si hay que aplicar el articulo 3 del cot.
 * si el valor es 0 no se aplica, si es 1 se aplica (unidad tributaria vijente
 * al menos por 183 dias)
 * 
 */
public class LastTributaryUnit implements Function {

	private String year;
	private String date;
	private AppEntry[] tuList;
	private GregorianCalendar calendar;
	private AppDate appDate;

	/** nueva instancia 
	 * 
	 */
	public LastTributaryUnit() {
		this.year = new AppDate().toString().substring(0, 4);
		this.appDate = new AppDate(year, "12", "31");
		this.calendar = new GregorianCalendar();
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
	public String execute(Evaluator evaluator, String s1) throws FunctionException {
		String arg[] = s1.split(",");
		this.date = appDate.toString();
		this.tuList = AppConstants.getConstantsOfType("tu");
		
		// si no es -1 debe ser 'yyyy' o AppDate.toString()
		if (!arg[0].equals("-1")) {
			if (arg[0].length() == 8) {
				date = arg[0];
			} else {
				date = date.replaceAll(year, arg[0].substring(1, 5));
//				year = arg[0].substring(b, 4 + b);
			}
		}
		
		// if this is a declaration of anual type, sub 183 days
		if (arg[1].trim().equals("1")) {
			calendar.setTime(new AppDate(date));
			calendar.add(GregorianCalendar.DAY_OF_YEAR, -183);
			date = new AppDate(calendar.getTime()).toString();
		}

		double tv = 0.0;
		int d = Integer.parseInt(date);
		for (int i = 0; i < tuList.length; i++) {
			tv = Integer.parseInt(((String) tuList[i].getKey())) < d ?
				Double.parseDouble((String) tuList[i].getValue()) : tv;
		}
		return String.valueOf(tv);
	}
}