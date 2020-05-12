/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.ae.gui.*;
import com.ae.resource.*;
import com.ae.action.*;
import com.ae.core.*;

/** Lista de contribuyentes
 * 
 */
public class TP_Table extends AbstractTableDataInput implements ActionListener, Keeper {
	
	private JComboBox jcbox;
	private String tip_per;
	
	/** Constructor sin argumentos
	 *
	 */
	public TP_Table() {
		super("TaxPayer", new TaxPayerRecord());
		this.jcbox = new JComboBox(AppConstants.getConstantsOfType("person"));
		jcbox.setMaximumSize(new Dimension(jcbox.getPreferredSize().width, 100));
		jcbox.addActionListener(this);
		jcbox.setToolTipText(
			((String) AppConstants.getConstant("tpSelector").getValue()));
		super.setToolBar(new Component[] {
			jcbox,
			new JButton(new NewRecordAction(this)),
//			new JButton(new PrintAction(this)),
			new JButton(new EditRecordAction(this)),
			new JButton(new DeleteRecordAction(this)),
			new JButton(new AllocateFormAction(this))
		});
		
		this.tip_per = (String)
			DIMain.preferenceRecord.getFieldValue("prTipPer");
			if (tip_per.equals("ju")) {
				jcbox.setSelectedIndex(1);
			}
		selectRecord();
	}
	/*
	 *  (non-Javadoc)
	 * @see com.ae.businesso.BusinessObject#getRightPanel(javax.swing.AbstractAction)
	 */
	public RightPanel getRightPanel(AbstractAction aa) {
		RightPanel rp = null;
		
		if (aa instanceof NewRecordAction) {
			TaxPayerRecord tpr = new TaxPayerRecord();
			tpr.setFieldValue(5, tip_per);
			rp = new TP_Record(tpr, (ConfirmationAction) aa, true);
		}
		if (aa instanceof EditRecordAction) {
			rp = new TP_Record(getRecord(), (ConfirmationAction) aa, false);
		}
		return rp;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		this.tip_per =
			(String) ((AppEntry) jcbox.getSelectedItem()).getKey();
		selectRecord();  
	}
	
	/* selecciona sub lista
	 * 
	 */
	private void selectRecord() {
		getTableModel().selectRecord("'{tpTipPer}' == '" + tip_per + "'");
	}
}