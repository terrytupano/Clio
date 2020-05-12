/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.gui.wizard.*;

/** consulta sobre el tipo de procedimiento a usar en la importacion de cuentas
 * manual o automatico. Esta clase puede comportarse diferente segun el valor
 * del argumento de entrada en el constructor. 
 * - si <code>"", ""</code> es la primera vez que se importan datos desde una
 * aplicacion contables para este contribuyente y se debe obtener informacion
 * de la aplicacion y el directorio orige de lo paneles anteriores.
 * - de lo contrario, ya se ha importado y los datos origen deben ser importados
 * desde esta aplicacion y desde este directorio origen.
 * 
 */
public class ImportAccount_WP3 extends WizardPanel implements ActionListener {

	private String app_name, app_dir;
	private boolean app_set;
	private WizardContainer wizardC;
	private JRadioButton jrb_aut, jrb_man;

	/** nueva instancia.
	 * 
	 * @param ap - aplicacion origen
	 * @param di - directorio origen
	 * 
	 */		
	public ImportAccount_WP3(String ap, String di) {
		super(WizardPanel.NORMAL);
		this.app_name = ap;
		app_set = ap.equals("") ? false : true;
		this.app_dir = di;
		this.jrb_aut = new JRadioButton(DIMain.bundle.getString("m26"));
		jrb_aut.addActionListener(this);
		this.jrb_man = new JRadioButton(DIMain.bundle.getString("m27"), true);
		jrb_man.addActionListener(this);
		ButtonGroup bg = new ButtonGroup();
		bg.add(jrb_aut);
		bg.add(jrb_man);
		JPanel jp3 = new JPanel(new BorderLayout(4, 4));
		jp3.add(jrb_aut, BorderLayout.NORTH);
		jp3.add(jrb_man, BorderLayout.SOUTH);
		jp3.setBorder(new LineBorder(Color.GRAY));
		JPanel jp2 = new JPanel(new BorderLayout());
		jp2.add(jp3, BorderLayout.NORTH);

		setText(DIMain.bundle.getString("m28"));
		setInputComponent(jp2);
		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#getData()
	 */
	public Object getData() {
		// vector con:
		// Boolean(true) si es automatico.
		// String con id de aplicacion
		// String con directorio origen de importacion.
		Vector v = new Vector(3);
		v.add(new Boolean(jrb_aut.isSelected()));
		v.add(app_name);
		v.add(app_dir);
		return v;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#initializePanel(com.ae.gui.wizard.WizardContainer)
	 */
	public void initializePanel(WizardContainer arg0) {
		this.wizardC = arg0;
		// aplicacion ya viene establecida desde contrustor.
		if (!app_set) {
			AppEntry ae = (AppEntry) arg0.getPanel(1).getData();
			this.app_name = (String) ae.getKey(); 
			this.app_dir = (String) arg0.getPanel(2).getData(); 
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#validateFields()
	 */
	public AplicationException validateFields() {
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		wizardC.changeStatus(NextAction.FINALIZE);
		// altera el estado de la accion next
		if (!jrb_aut.isSelected()) {
				wizardC.changeStatus(NextAction.NEXT);
		}
	}
}
