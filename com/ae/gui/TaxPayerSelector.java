/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.ae.core.*;
import com.ae.resource.*;

/** Componente encargado de desplegar los contribuyentes para que el usuario
 * pueda seleccionar el contribuyente al cual, se cambiaran archivos 
 * asociados. Este envia <code>itemSelected(TaxPayerRecord)</code>
 * al componente derecho que se encuentra activo para notificar que la seleccion
 * a cambiado.
 */
public class TaxPayerSelector extends JPanel implements ActionListener {

	private AppTableModel at_model;
	private JComboBox jcbox_tpr;
	private TaxPayerListener tp_listener;
	
	/** Constructor
	 * 
	 * @param at - manejador de eventos de seleccion de contribuyentes
	 */
	public TaxPayerSelector(TaxPayerListener at) {
		super(new BorderLayout());
		setOpaque(false);
		
		this.at_model = PrevalentSystemManager.getTableModel(
			new TaxPayerRecord());
		this.jcbox_tpr = new JComboBox();
		jcbox_tpr.addActionListener(this);
		jcbox_tpr.setToolTipText(
			((String) AppConstants.getConstant("taxPSelector").getValue()));
		fillJComboBox();
		this.tp_listener = at;
		
		add(new JLabel(DIMain.bundle.getString("c08") + "  "), BorderLayout.WEST);
		add(jcbox_tpr, BorderLayout.CENTER);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */	
	public void actionPerformed(ActionEvent ae) {
		TaxPayerRecord tpr = getSelectedRecord();
		if (tpr != null) {
			tp_listener.itemSelected(tpr);
		}
	}
	
	/** contruye un nuevo <code>ComboBoxModel</code> el cual contiene 
	 * una lista de todos los contribuyentes.
	 * 
	 */
	private void fillJComboBox() {
		TaxPayerRecord tpr = new TaxPayerRecord();
		at_model.selectRecord(null);
		
		DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
		int rc = at_model.getRowCount();
		for (int r = 0; r < rc; r++) {
			tpr = (TaxPayerRecord) at_model.getRecordAt(r);
//			if (tpr.getFieldValue("tpTipPer").equals("ju")) {
				dcbm.addElement(new AppEntry(tpr, (String) tpr.getFieldValue("tpNombre")));
//			}
		}
		jcbox_tpr.setModel(dcbm);
	}
	
	/** retorn el elemento que se ecuentra seleccionado.
	 * 
	 * @return Registro actual
	 */
	public TaxPayerRecord getSelectedRecord() {
		AppEntry ape = (AppEntry) jcbox_tpr.getSelectedItem();
		if (ape != null) {
			return (TaxPayerRecord) ape.getKey();
		} else {
			return null;
		}
	}
}
