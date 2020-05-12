/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import javax.swing.*;

import com.ae.gui.*;
import com.ae.resource.*;
import com.ae.action.*;
import com.ae.core.*;

/** Lista de las transacciones realizadas para el contribuyente determinado. 
 * 
 */
public class VoucherTable extends AbstractTableDataInput implements Keeper {
	
	/** Unico constructor
	 * 
	 */	
	public VoucherTable() {
		super("AccountVoucher", new VoucherRecord());
		showTaxPayerSelector();		
		setToolBar(new Component[] {
			new JButton(new NewRecordAction(this)),
			new JButton(new ImportVouchersAction(getTaxPayerSelector())),
//			new JButton(new PrintAction(this)),
			new JButton(new EditRecordAction(this)),
			new JButton(new DeleteRecordAction(this))
		});
		itemSelected(getTaxPayerSelector().getSelectedRecord());
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.datainput.AbstractTableDataInput#getRightPanel(javax.swing.AbstractAction)
	 */		
	public RightPanel getRightPanel(AbstractAction aa) {
		RightPanel rp = null;
		if (aa instanceof NewRecordAction) {
			VoucherRecord av = new VoucherRecord();
			TaxPayerRecord tpr = getTaxPayerSelector().getSelectedRecord();
			av.setFieldValue(0, tpr.getFieldValue(0));
			rp = new Voucher_Record(av, (ConfirmationAction) aa, true);
		}
		if (aa instanceof EditRecordAction) {
			rp = new Voucher_Record(getRecord(), (ConfirmationAction) aa, false);
		}
		return rp;
	}		
	/*
	 *  (non-Javadoc)
	 * @see com.ae.datainput.AbstractTableDataInput#itemSelected(com.ae.core.Record)
	 */
	public void itemSelected(Record rcd) {
		if (rcd != null) {
			getTableModel().selectRecord("'{avRifTP}' == '" + rcd.getFieldValue(0) + "'");
		} else {
			showMessage("msg81");
		}
		enableActions(AppAbstractAction.TABLE_SCOPE, !isShowingError());
	}
}	