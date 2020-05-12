/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.wizard.*;

/** panel de entrada de tipo de aplicacion contable
 * 
 */
public class ImportAccount_WP1 extends WizardPanel {

	private JComboBox jcb_app;
	
	/** nueva instancia
	 *
	 */
	public ImportAccount_WP1() {
		super(WizardPanel.NORMAL);
		this.jcb_app = new JComboBox(AppConstants.getConstantsOfType("importApp"));
		JPanel jp1 = new JPanel(new BorderLayout());
		jp1.add(jcb_app, BorderLayout.NORTH);
		setText(DIMain.bundle.getString("m05"));
		setInputComponent(jp1);
		
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#getData()
	 */
	public Object getData() {
		return jcb_app.getSelectedItem();
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
