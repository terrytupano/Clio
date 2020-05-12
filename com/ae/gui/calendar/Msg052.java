/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.calendar;

import java.util.*;

import com.ae.core.*;
import com.ae.resource.*;

/** clase que determina si estan dados los elementos necesarios para que
 * <code>DutyCalendar</code> presente el mensaje msg052
 * 
 */
public class Msg052 implements Runnable {
	
	private DutyCalendar dutyC;
	private Hashtable docEntries;
	private AppTableModel tableM;
	
	/** nueva instancia
	 * 
	 * @param dt - instancia de <code>DutyCalendar</code>
	 * @param de - documento .xml en formato apropidado
	 */
	public Msg052(DutyCalendar dt, Hashtable de) {
		this.dutyC = dt;
		this.docEntries = de;
		this.tableM = PrevalentSystemManager.getTableModel(new StockRecord());
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		AppEntry[] ifo = ResourceUtilities.getInstalledForms();
		Object[] exd = new Object[2];
		for (int x = 0; x < ifo.length; x++) {
			String fid = (String)ifo[x].getKey();
			Record rcd = tableM.getRecord(fid);
			int rem = ((Integer) 
				DIMain.preferenceRecord.getFieldValue("prAdquiF")).intValue();
			if (rcd != null) {
				int can = ((Integer) rcd.getFieldValue("sCantida")).intValue();
				if (can <= rem && can > 0) {
					exd[0] = new Integer(can);
					exd[1] = fid;
					dutyC.addMessage("msg52", exd);
				}
			}
		}
	}
}
