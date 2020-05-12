/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.util.*;

/** Especializacion de <code>Date</code> con la finalidad de alterar 
 * <code>toString</code> 
 * 
 */
public class AppDate extends Date {
	
	private transient GregorianCalendar calendar;
	
	/** Construtor sin argumentos
	 * 
	 */
	public AppDate() {
		super();
		this.calendar = calendar();
		clearTime();
	}
	
	/** nueva instancia
	 * 
	 * @param t - tiempo
	 */
	public AppDate(Date t) {
		this.calendar = calendar();
		setTime(t.getTime());
		clearTime();
	}
	
	/** crea nueva instancia.
	 * 
	 * @param y - año en formato yyyy
	 * @param m - mes 
	 * @param d - dia
	 */
	public AppDate(String y, String m, String d) {
		this.calendar = calendar();
		calendar().set(GregorianCalendar.YEAR, Integer.parseInt(y));
		calendar().set(GregorianCalendar.MONTH, Integer.parseInt(m) - 1);
		calendar().set(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(d));
		setTime(calendar.getTime().getTime());
		clearTime();
	}
	
	/** nueva instancia
	 * 
	 * @param ymd - en formato yyyymmdd
	 */
	public AppDate(String ymd) {
		this(ymd.substring(0, 4),  ymd.substring(4, 6), ymd.substring(6, 8));
	}
	
	/** metodo auxiliar que establece la hora a 00:00:00
	 *
	 */
	private void clearTime() {
		calendar().setTime(this);
		calendar().set(GregorianCalendar.HOUR_OF_DAY, 0);
		calendar().set(GregorianCalendar.MINUTE, 0);
		calendar().set(GregorianCalendar.SECOND, 0);
		setTime(calendar().getTimeInMillis());
	}

	/** Alteracion de <code>Date.toString()</code> para siempre retornar
	 * esta fecha en formato yyyymmdd
	 * 
	 */
	public String toString() {
		calendar().setTime(this);
		int dd = calendar().get(GregorianCalendar.DAY_OF_MONTH);
		String dia = dd < 10 ? "0" + dd : "" + dd; 
		int mm = calendar().get(GregorianCalendar.MONTH) + 1;
		String mes = mm < 10 ? "0" + mm : "" + mm; 
		return 
		calendar().get(GregorianCalendar.YEAR) + 
		mes + dia;
	}
	
	/** Retorna el valor solicitados desde el calendario interno de esta clase
	 * 
	 * @param fld - alguna de las contantes de <code>GregorianCalendar</code>
	 * 
	 * @return - valor solicitado
	 */
	public int getFromCalendar(int fld) {
		calendar().setTime(this);
		return calendar().get(fld);
	}
	
	
	/** metodo para obtener <code>GrecorianCalendar</code>
	 * 
	 * @return - <code>GregorianCalendar</code>
	 */
	private GregorianCalendar calendar() {
		if (calendar == null) {
			this.calendar = new GregorianCalendar();
		}
		return calendar;
	}
}
