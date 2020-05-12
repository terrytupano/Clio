/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.awt.dnd.*;
import javax.swing.*;

import com.ae.gui.*;
import com.ae.core.*;
import com.ae.resource.*;
import com.ae.action.*;

/** Presenta una lista de las entradas contables asignadas al un determinado 
 * contribuyente. El contribuyente lo determina el usuario a travez de una
 * interface diceñada para ello y por la cual, esta clase conoce. 
 * 
 */
public class Account_Table extends AbstractTableDataInput implements DragGestureListener {
	
	private Account_ClioObject acc_clio;

	/** Unico constructor
	 * 
	 * @param rp - ver la documentacion de <code>Account_clioObject</code>
	 */	
	public Account_Table(Account_ClioObject rp) {
		super(null, new AccountRecord());
		this.acc_clio = rp;
		setToolBar(new Component[] {
			new JButton(new NewRecordAction(this)),
			new JButton(new ImportAccountsAction(acc_clio)),
			new JButton(new EditRecordAction(this)),
			new JButton(new DeleteRecordAction(this))
		});
		DragSource ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(getTable(), DnDConstants.ACTION_COPY, this);
		getTable().setDragEnabled(true);
		showLikeList(new TableLikeListCellRenderer(new AccountRecord(), null));
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent dge) {
		 acc_clio.dragGestureRecognized(getRecord());
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.datainput.AbstractTableDataInput#getRightPanel(javax.swing.AbstractAction)
	 */		
	public RightPanel getRightPanel(AbstractAction aa) {
		RightPanel rp = null;
		if (aa instanceof NewRecordAction) {
			AccountRecord ar = new AccountRecord();
			TaxPayerRecord tpr = acc_clio.getTaxPayerSelector().getSelectedRecord();
			ar.setFieldValue(0, tpr.getFieldValue(0));
			rp = new Account_Record(ar, (ConfirmationAction) aa, true);
		}
		if (aa instanceof EditRecordAction) {
			rp = new Account_Record(getRecord(), (ConfirmationAction) aa, false);
		}
		return rp;
	}		
}	