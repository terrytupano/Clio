/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.util.logging.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.*;
import com.ae.gui.wizard.*;
import com.ae.resource.*;

/** presenta los requisitos provios que el usuario debe cumplir antes de 
 * presionar finalizar. los requisitos estan descritos en documentos cuyo
 * nombre son los nombre de los codigos de aplicacion origen
 * 
 */
public class ImportVoucher_WP3 extends WizardPanel {
	
	/** nueva instancia
	 * 
	 * @param ap - id de aplicacio contable 
	 */
	public ImportVoucher_WP3(String ap) {
		super(WizardPanel.NORMAL);
		JEditorPane jep = new JEditorPane();
		try {
			jep.setEditable(false);
			jep.setPage(ResourceUtilities.getURL(ap));
			GUIUtilities.setEmptyBorder(jep);
		} catch(Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
		setText(DIMain.bundle.getString("m41"));
		setInputComponent(new JScrollPane(jep));
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#getData()
	 */
	public Object getData() {
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#initializePanel(com.ae.gui.wizard.WizardContainer)
	 */
	public void initializePanel(WizardContainer arg0) {
		
	}
	
	/* (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#validateFields()
	 */
	public AplicationException validateFields() {
		return null;
	}
}
