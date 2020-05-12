/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import com.ae.gui.*;
import com.ae.core.*;
import com.ae.resource.*;

/** Asigna un contribuyente para comenzar la edicion de una nueva forma. 
 * 
 */
public class AllocateTaxPayerAction extends AbstractNewFormAction implements ConfirmationAction {
	
	private AbstractTableDataInput atable;
	private AppPopupMenu popup;
	private AppEntry f_dta;
	private TaxPayerRecord taxPayer;
	
	public AllocateTaxPayerAction(AbstractTableDataInput at) {
		super("a07", "ATaxPayer", AppAbstractAction.TABLE_SCOPE, "tt04");
		this.atable = at;
		this.f_dta = null;
		this.taxPayer = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		this.f_dta = 
			DIMain.leftComponentManager.getSelectedForm();
		
		AppTableModel atm = 
			PrevalentSystemManager.getTableModel(new TaxPayerRecord());
		int tpc = atm.getRowCount();
		Vector vec = new Vector();
		for (int r = 0; r < tpc; r++) {
			TaxPayerRecord tpr = (TaxPayerRecord) atm.getRecordAt(r);
			if (checkConditions(f_dta, tpr)) {
				vec.add(new AppEntry(tpr, (String) tpr.getFieldValue("tpNombre")));
			}
		}
		popup = new AppPopupMenu(
		(AppEntry[]) vec.toArray(new AppEntry[vec.size()]),
			"TaxPayer", this);
		JButton jb = (JButton) arg0.getSource();
		Rectangle r = jb.getBounds();
		popup.show(jb, 0, r.height);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */
	public void actionPerformed2(AbstractAction aa) {
		AppEntry ae = popup.getSelectedItem();
		taxPayer = (TaxPayerRecord) ae.getKey();
		confirm(f_dta, taxPayer);
	}
}

