/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.prevayler.implementation.*;

import com.ae.core.*;
import com.ae.gui.*;
import com.ae.resource.*;

/**  Segun el contribuyente que este seleccionado, selecciona
 * las cuentas contables (AccountRecord) asociadas a este e inicia
 * la lectura de los movimientos para cada una de estas. Si movimientos
 * son encontrados para una cuenta contable, se actualizan los valores
 * de este registro.		
 */	
public class ImportVouchers implements Runnable {
	
	private ProgressPanel progressP;
	private JDialog dialog;
	private TaxPayerSelector tpSel;
	
	/** nueva accion
	 * 
	 */
	public ImportVouchers(ProgressPanel pp, JDialog d, TaxPayerSelector ts) {
		this.dialog = d;
		this.progressP = pp;
		this.tpSel = ts;
	}

	/*
	 *  (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		progressP.increment(DIMain.bundle.getString("i12"));
		
		AccountRecord acr = new AccountRecord();
		VoucherRecord vor = new VoucherRecord();
		String tp_rif = ((TaxPayerRecord) tpSel.getSelectedRecord()).getKey();
		
		AppTableModel atm_acr = PrevalentSystemManager.getTableModel(acr);
		AppTableModel atm_vor = PrevalentSystemManager.getTableModel(vor, false);
		SnapshotPrevayler snaps = PrevalentSystemManager.getPrevayler(vor); 
		atm_acr.selectRecord("'{acRifTP}' == '" + tp_rif + "'");
		int len = atm_acr.getRowCount();
		progressP.offsetMaximum(len);
		
		// cuentas contables
		for (int r = 0; r < len; r++) {
			acr = (AccountRecord) atm_acr.getRecordAt(r);
			String at = (String) acr.getFieldValue("acIApp");
			String dir = (String) acr.getFieldValue("acIDir");
			String ac = (String) acr.getFieldValue("acCode");

			// cuenta creada, no importada
			if (at.equals("")) {
				continue;
			}

			progressP.increment(DIMain.bundle.getString("i17") + ac);
			try {
				// movimientos para esta cuenta contable
				TreeMap tm = AccountReader.readVouchers(at, dir, ac);
				Iterator iter = tm.keySet().iterator();
				while(iter.hasNext()) {
					Object conse = iter.next();
					Object[] fdm = (Object[]) tm.get(conse);
					VoucherRecord vr = new VoucherRecord();
					vr.setFieldValue("avRifTP", tp_rif);  
					vr.setFieldValue("avAccAC", ac);
					vr.setFieldValue("avFecha", (AppDate) fdm[0]);
					vr.setFieldValue("avConsec", (Integer) conse);
					vr.setFieldValue("avMonto", (Double) fdm[2]);  
					vr.setFieldValue("avTexto", fdm[1]);  
					vor = (VoucherRecord) atm_vor.getRecord(vr.getKey());
					
					// insertar si movimiento nuevo, actualizar de lo contrario
					if (vor == null) {
						snaps.executeCommand(new NewRecordCommand(vr));
					} else {
						snaps.executeCommand(new EditRecordCommand(vr));
					}
				}
			} catch (IOException ioe) {
				Logger.getLogger("").logp(Level.SEVERE, null, null, ioe.getMessage(), ioe);
			} catch (Exception e) {
				Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
			}
		}
		dialog.dispose();
	}
}
