/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.wizard.*;

/** panel con instrucciones para importar Cuentas
 * 
 */
public class ImportAccount_WP0 extends WizardPanel {

	/** nueva instancia
	 *
	 */
	public ImportAccount_WP0() {
		super(WizardPanel.WELLCOME);
		setText(DIMain.bundle.getString("m04"));
		setInputComponent(Box.createGlue());
		
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
