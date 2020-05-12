/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.calendar;

import java.util.*;

import org.jdom.*;

import com.ae.core.*;
import com.ae.resource.*;

/** clase que determina si estan dados los elementos necesarios para que
 * <code>DutyCalendar</code> presente el mensaje msg054.
 * 
 */
public class Msg054 implements Runnable {
	
	private Hashtable docEntries;
	
	private DutyCalendar dutyC;
	
	/** nueva instancia
	 * 
	 * @param dt - instancia de <code>DutyCalendar</code>
	 * @param de - documento .xml en formato apropidado
	 */
	public Msg054(DutyCalendar dt, Hashtable de) {
		this.dutyC = dt;
		this.docEntries = de;
	}
	
	/** en un lazo que comienza desde la primera fecha contenida dentro del
	 * archivo de declaraciones hasta el dia de hoy, se van evaluando las formas
	 * salvadas con estado activo y se compara cada una 
	 * de ellas con las fechas de declaraciones. si alguna de ellas es menor
	 * (es decir, la fecha tope de entrega mas proxima a ella ya ha 
	 * pasado) se marca el registro como inactivo. 
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		FormSavedRecord fsr = new FormSavedRecord();
		AppTableModel atm_fs = PrevalentSystemManager.getTableModel(fsr, true);
		atm_fs.selectRecord("'{fsStatus}' == 'true'");
		AppDate today = new AppDate();
		String year = today.toString().substring(0, 4);
		Enumeration enume = docEntries.keys();
		while(enume.hasMoreElements()) {
			String key = (String) enume.nextElement();
			String[] md = key.split(";");
			// si no es un elemento de fecha
			if (md.length < 2) {
				continue;
			}
			String decl = (String) docEntries.get(key);
			AppDate dd = new AppDate(year, md[0], md[1]);
			
			// si la fecha del archivo de declaracion es mayor a la de hoy
			if (dd.getTime() > today.getTime()) {
				continue;
			}
			for (int j = 0; j < atm_fs.getRowCount(); j++) {
				fsr = (FormSavedRecord) atm_fs.getRecordAt(j);
				AppDate rd = (AppDate) fsr.getFieldValue("fsFecha");
				String f = (String) fsr.getFieldValue("fsForma");
				Hashtable htf = PrevalentSystemManager.getFormElements(f);
				Element e = (Element) htf.get("form");
				String fde = e.getAttributeValue("declaration");
				if (fde.equals(decl) && (rd.getTime() < dd.getTime())) {
					Object[] o54 = new Object[3];
					fsr.setFieldValue("fsStatus", new Boolean(false));
					PrevalentSystemManager.executeCommand(new EditRecordCommand(fsr));
					o54[0] = fsr.getFieldValue("fsForma");
					o54[1] = fsr.getFieldValue("fsNombre");
					o54[2] = rd;
					dutyC.addMessage("msg54", o54);
				}
			}
		}
	}
}