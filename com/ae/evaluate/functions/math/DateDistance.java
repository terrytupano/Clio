/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.evaluate.functions.math;

import java.util.*;
import com.ae.evaluate.*;
import com.ae.core.*;

/** Esta funcion espera dos argumentos:
 * - fecha desde 
 * - fecha hasta
 * 
 * con estas dos fechas, resta al segundo argumento el primero y retorna la
 * diferencia en dias 
 * 
 */
public class DateDistance implements Function {

	private GregorianCalendar calendar;

	/** nueva instancia 
	 * 
	 */
	public DateDistance() {
		this.calendar = new GregorianCalendar();
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#getName()
	 */
	public String getName() {
		return "dateDistance";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#execute(com.ae.evaluate.Evaluator, java.lang.String)
	 */
	public String execute(Evaluator evaluator, String s) throws FunctionException {
		String[] dd_dh = s.split(",");
		calendar.setTime(new Date(
			(new AppDate(dd_dh[1].trim())).getTime() - 
			(new AppDate(dd_dh[0].trim())).getTime()));
			int d = calendar.get(GregorianCalendar.DAY_OF_YEAR);
		return String.valueOf(d);
		
	}
}