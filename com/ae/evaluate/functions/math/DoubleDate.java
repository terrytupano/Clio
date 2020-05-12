/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.evaluate.functions.math;

import java.util.*;
import java.text.*;
import com.ae.evaluate.*;
import com.ae.core.*;

/** Esta funcion espera tres argumentos de entrada:
 * - el año (numero o *year, *year- para indicar este año o el año pasado)
 * - el mes (numero o *month para indicar este mes)
 * - 0, 1 o 2
 * Segun estos parametros, esta fucion retorna el primer dia del año/mes (si 
 * el tercer parametro es 0, el ultimo dia del año/mes (si el tercer parametro
 * es 1) o el ultimo dia del año/mes - 1 si el tercer parametro es 2. 
 * todos, en formato yyyymmdd
 * 
 * 
 */
public class DoubleDate implements Function {

	private GregorianCalendar calendar;
	private AppDate date;

	/** nueva instancia 
	 * 
	 */
	public DoubleDate() {
		this.calendar = new GregorianCalendar();
		this.date = new AppDate();
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#getName()
	 */
	public String getName() {
		return "doubleDate";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#execute(com.ae.evaluate.Evaluator, java.lang.String)
	 */
	public String execute(Evaluator evaluator, String s) throws FunctionException {
		String[] ymx = s.split(",");
		
		// valore especiales
		if (ymx[0].equals("*year")) {
			ymx[0] = date.toString().substring(0, 4);
		}
		if (ymx[0].equals("*year-")) {
			int y = Integer.parseInt(date.toString().substring(0, 4));
			ymx[0] = String.valueOf(--y);
		}
		if (ymx[1].equals("*month")) {
			ymx[1] = date.toString().substring(4, 6);
		}

		NumberFormat nf = NumberFormat.getInstance(new Locale(""));
		int y, m, x = 0;
		try {
			y = nf.parse(ymx[0].trim()).intValue();
			m = nf.parse(ymx[1].trim()).intValue();
			x = nf.parse(ymx[2].trim()).intValue();
		} 
		catch(Exception e) {
			throw new FunctionException("Parse Problemns");
		}
		
		GregorianCalendar gc = new GregorianCalendar(y, m - 1, 1);

		// resta 1 mes		
		if (x == 2) {
			gc.add(GregorianCalendar.MONTH, -1);
			x = 1;
		}
		// ultimo dia del mes
		if (x == 1) {
			gc.set(GregorianCalendar.DAY_OF_MONTH, 
				gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		}

		return new AppDate(gc.getTime()).toString();
		
	}
}