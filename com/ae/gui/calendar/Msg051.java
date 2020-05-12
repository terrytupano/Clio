/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.calendar;

import java.util.*;

import com.ae.core.*;

/** clase que determina si estan dados los elementos necesarios para que
 * <code>DutyCalendar</code> presente el mensaje msg051
 * 
 */
public class Msg051 implements Runnable {
	
	private DutyCalendar dutyC;
	private Hashtable docEntries;
	
	/** nueva instancia
	 * 
	 * @param dt - instancia de <code>DutyCalendar</code>
	 * @param de - documento .xml en formato apropidado
	 */
	public Msg051(DutyCalendar dt, Hashtable de) {
		this.dutyC = dt;
		this.docEntries = de;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Iterator it = docEntries.keySet().iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			String[] md = key.split(";");
			if (md.length > 1) {
				// hoy
				GregorianCalendar calendar = new GregorianCalendar();
				AppDate td = new AppDate(calendar.getTime());

				// fecha tope
				calendar.set(GregorianCalendar.MONTH, Integer.parseInt(md[0]) - 1);
				calendar.set(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(md[1]));
				AppDate ed = new AppDate(calendar.getTime());

				// fecha inicio
				calendar.add(GregorianCalendar.DAY_OF_MONTH, -DutyCalendar.LAPSE);
				AppDate sd = new AppDate(calendar.getTime());

				// hoy esta entre fecha inicio y fecha tope?
				if ((sd.getTime() <= td.getTime()) && (td.getTime() <= ed.getTime())) {
					Object[] d51 = new Object[2];
					String did = (String) docEntries.get(key);
					AppDate t = new AppDate(new Date(ed.getTime() - td.getTime()));
					d51[0] = new Integer(Integer.parseInt(t.toString().substring(7)) - 1);
					d51[1] = docEntries.get("name." + did);
					dutyC.addMessage("msg51", d51);
				}

				/*
				GregorianCalendar calendar = new GregorianCalendar();
				AppDate td = new AppDate(calendar.getTime());

				int tdd = calendar.get(GregorianCalendar.DAY_OF_MONTH);
				int day = Integer.parseInt(dm[0]);
				calendar.set(GregorianCalendar.MONTH, Integer.parseInt(dm[1]) - 1);
				calendar.set(GregorianCalendar.DAY_OF_MONTH, day);
				AppDate ed = new AppDate(calendar.getTime());

				int edd = calendar.get(GregorianCalendar.DAY_OF_MONTH);
				calendar.set(GregorianCalendar.DAY_OF_MONTH, day - DutyCalendar.LAPSE);
				AppDate sd = new AppDate(calendar.getTime());

				if ((sd.getTime() <= td.getTime()) && (td.getTime() <= ed.getTime())) {
					Object[] d51 = new Object[2];
					String did = (String) docEntries.get(key);
					d51[0] = new Integer(edd - tdd);
					d51[1] = docEntries.get("name." + did);
					dutyC.addMessage("msg51", d51);
				}
				*/
			}
		}
	}
}
