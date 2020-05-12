/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.gui.*;
import com.ae.resource.*;

/** editor de comprobante contable
 * 
 */
public class Voucher_Record extends AbstractRecordDataInput {

	private JButton aceptar;
	private AppTableModel atm_account;
	private AccountRecord rcd_account;
	private boolean newR;

	/** nueva instancia
	 * 
	 * @param rcd - registro
	 * @param ca - ConfirmationAction
	 * @param nr - true si es nuevo registro
	 */	
	public Voucher_Record(Record rcd, ConfirmationAction ca, boolean nr) {
		super(((nr) ? "NewRecord" : "EditRecord"), rcd, nr);

		this.newR = nr;
		this.rcd_account = new AccountRecord();
		rcd_account.setFieldValue(0, rcd.getFieldValue(0));
		this.atm_account = PrevalentSystemManager.getTableModel(rcd_account);
			
		//contribuyente 
		JTextField jtf = GUIUtilities.getJTextField(rcd, 0);
		addInputComponent(jtf);

		//entrada contables
		jtf = GUIUtilities.getJTextField(rcd, 1);
		addInputComponent(jtf, true);

		//Fecha
		JFormattedTextField jftf = GUIUtilities.getJFormattedTextField(rcd, 2);
		addInputComponent(jftf, true);
		
		// consecutivo. ver getRecord()
		jtf = GUIUtilities.getJFormattedTextField(rcd, 3);
		addInputComponent(jtf);
		
		//monto
		jftf = GUIUtilities.getJFormattedTextField(rcd, 4);
		addInputComponent(jftf, true);

		//texto descriptivo
		jtf = GUIUtilities.getJTextField(rcd, 5);
		addInputComponent(jtf);

		JComponent[] jcmps = getInputComponents();

		JPanel main_panel = new JPanel(
			new GridLayout(3,1,GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP)
		);
		JPanel p1 = new JPanel(
			new GridLayout(1,2,GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP)
		);
		p1.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(2), jcmps[2], true, newR));
		p1.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(4), jcmps[4], true, true));
		
		main_panel.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(1), jcmps[1], true, newR));
		main_panel.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(5), jcmps[5]));
		main_panel.add(p1);

		// Acciones
		AbstractAction[] bts = new AbstractAction[] {
			new AceptAction(ca), new CancelAction(ca)};
		this.aceptar = setActionBar(bts);
		setDefaultButton(aceptar);

		GUIUtilities.setEmptyBorder(main_panel);
		add(main_panel);

		preValidate();
	}
	
	/* (non-Javadoc)
	 * @see com.ae.datainput.AbstractRecordDataInput#getRecord()
	 */
	public Record getRecord() {
		Record rcd = super.getRecord();
		
		if (newR) {
			// actualiza campo de consecutivo. localiza el ultimo registro
			// que tenga la cuenta y la fecha iguales, extrae el consecutivo y 
			// le adiciona 1 para este registro
			rcd.setFieldValue("avConsec", new Integer(999)); 
			SortedMap sm = tableModel.getSortedMap(null, rcd);
			if (!sm.isEmpty()) {
				VoucherRecord vr = (VoucherRecord) 
					tableModel.getRecord((String) sm.lastKey());
				int con = ((Integer) vr.getFieldValue("avConsec")).intValue();
				rcd.setFieldValue("avConsec", new Integer(++con));
			}
		}
		return rcd;
	}

	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.RightPanel#validateField(int, com.ae.businesso.Record)
	 */	
	public boolean validateFields() {
		JComponent[] jcmps = getInputComponents();
		boolean ena = true;
		
		// entrada contables
		if(newR) {
			String ac = ((JTextField) jcmps[1]).getText();;
			rcd_account.setFieldValue("acCode", ac);
			if (!(ac.trim().equals("")) && 
				atm_account.getRecord(rcd_account.getKey()) == null) {
				showMessage("msg94");
			}
		}
		return ena;
	}
}