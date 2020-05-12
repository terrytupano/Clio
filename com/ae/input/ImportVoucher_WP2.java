/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.util.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.*;
import com.ae.gui.wizard.*;
import com.ae.resource.*;

/** presenta componentes que permiten la seleccion de la aplicacion que 
 * desde las cuales se desea importar movimientos. 
 * 
 */
public class ImportVoucher_WP2 extends WizardPanel {
	
	private JCheckBox[] app_jcb;
	private JPanel app_jp;
	private TaxPayerSelector tpSel;
	private AplicationException ndtaE;

	/** nueva instancia
	 * 
	 * @param tp - selector de contribuyente
	 */
	public ImportVoucher_WP2(TaxPayerSelector tp) {
		super(WizardPanel.NORMAL);
		this.app_jp = new JPanel();
		this.tpSel = tp;
		app_jp.setLayout(new BoxLayout(app_jp, BoxLayout.Y_AXIS));
		
		setText(DIMain.bundle.getString("m40"));
		setInputComponent(app_jp);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#getData()
	 */
	public Object getData() {
		
		// hashtable cuya clave es el codigo de app y valor null
		Hashtable h = new Hashtable();
		for (int s = 0; s < app_jcb.length; s++) {
			if (app_jcb[s].isSelected()) {
				h.put(app_jcb[s].getName(), "");
			}
		}
		return h;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#initializePanel(com.ae.gui.wizard.WizardContainer)
	 */
	public void initializePanel(WizardContainer arg0) {
		app_jp.removeAll();
		this.ndtaE = (AplicationException) PrevalentSystemManager.exceptions.get("msg100");
		
		// explora las entradas contables de un contribuyente en busca de
		// s aplicaciones que las originaron.
		AppTableModel atm = 
			PrevalentSystemManager.getTableModel(new AccountRecord());
		//hashtable para que sobreescriba los repetidos
		Hashtable h = new Hashtable();
		Record tpr = tpSel.getSelectedRecord();
		for (int t = 0; t < atm.getRowCount(); t++) {
			Record ar = atm.getRecordAt(t);
			String apn = (String) ar.getFieldValue("acIApp");
			if (ar.getFieldValue("acRifTP").equals(tpr.getFieldValue("tpRif")))
			h.put(apn, AppConstants.getConstant(apn));
		}
		
		// jcheckBox para cada una
		if (h.size() > 0) {
			ndtaE = null;
			int l = h.size();
			app_jcb = new JCheckBox[l];
			AppEntry[] as = (AppEntry[]) h.values().toArray(new AppEntry[l]);	
			for (int s = 0; s < as.length; s++) {
				app_jcb[s] = new JCheckBox((String) as[s].getValue(), true);
				app_jcb[s].setName((String) as[s].getKey()); 
				app_jp.add(app_jcb[s]);
			}
		} else {
			arg0.showExceptionMessage(ndtaE);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#validateFields()
	 */
	public AplicationException validateFields() {
		// ninguna dato de importacion.
		AplicationException ae = ndtaE; 
		if (ndtaE == null) {
			// si ninguno esta seleccionado
			ae = (AplicationException) PrevalentSystemManager.exceptions.get("msg99");
			for (int s = 0; s < app_jcb.length; s++) {
				ae = (app_jcb[s].isSelected() && ae != null) ? null : ae;
			}
		}
		return ae;
	}
}
