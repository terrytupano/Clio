/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.*;
import com.ae.gui.wizard.*;
import com.ae.resource.*;

/** panel de entrada que permite completar la solicitud de clave de adquisicion.
 * el texto de cierre de almacena el el registro de preferencias.
 * 
 */
public class SendRequest_WP1 extends WizardPanel {

	private JLabel doc_jl;
	private JComboBox bank_jcb;
	private JTextField depN_jtf, etxt_jtf, email_jtf;
	
	/** nueva instancia
	 *
	 */
	public SendRequest_WP1() {
		super(WizardPanel.NORMAL);
		this.depN_jtf = new JTextField();
		GUIUtilities.setDimensionForTextComponent(depN_jtf, 20);
		this.etxt_jtf = new JTextField((String) 
			DIMain.preferenceRecord.getFieldValue("prEndTxt"));
		GUIUtilities.setDimensionForTextComponent(etxt_jtf, 20);
		this.email_jtf = new JTextField((String) 
			DIMain.preferenceRecord.getFieldValue("prEmail"));
		GUIUtilities.setDimensionForTextComponent(email_jtf, 20);
		this.bank_jcb = new JComboBox(AppConstants.getConstantsOfType("bank"));
		JPanel jp1 = new JPanel();
		jp1.setLayout(new BoxLayout(jp1, BoxLayout.Y_AXIS));
		jp1.add(GUIUtilities.getCoupleInBox(DIMain.bundle.getString("b04"), bank_jcb));
		jp1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		jp1.add(GUIUtilities.getCoupleInBox(DIMain.bundle.getString("n04"), depN_jtf));
		jp1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		jp1.add(GUIUtilities.getCoupleInBox(DIMain.bundle.getString("n08"), etxt_jtf)); 
		jp1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		jp1.add(GUIUtilities.getCoupleInBox(DIMain.bundle.getString("d06"), email_jtf)); 
		jp1.add(new JPanel(new BorderLayout()));
		setText(DIMain.bundle.getString("m17"));
		setInputComponent(jp1);
	}
	
	/** retorna <code>Hashtable</code> con variables, valor de los datos
	 * obtenidos. los datos grabados en el registro de preferencias pueden
	 * ser usados directamente dentro del asistente
	 * 
	 * @return - lista de variable, valor
	 *  
	 * @see com.ae.gui.wizard.WizardPanel#getData()
	 */
	public Object getData() {
		DIMain.preferenceRecord.setFieldValue("prEndTxt", etxt_jtf.getText());
		DIMain.preferenceRecord.setFieldValue("prEmail", email_jtf.getText());
		PrevalentSystemManager.executeCommand(new EditRecordCommand(DIMain.preferenceRecord));
		Hashtable ht = new Hashtable();
		AppEntry b = (AppEntry) bank_jcb.getSelectedItem();

		String[] c_b_n = ((String) b.getValue()).split(",");

		ht.put("<accountNumber>", c_b_n[1]);
		ht.put("<bank>", c_b_n[0]);
		ht.put("<accountOwner>", c_b_n[2]);
		ht.put("<depNumber>", depN_jtf.getText());
		ht.put("<name>", etxt_jtf.getText());
		return ht;
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
		return (depN_jtf.getText().trim().equals("") ||
			etxt_jtf.getText().trim().equals("") || 
			email_jtf.getText().trim().equals("")) ? 
			(AplicationException)
			PrevalentSystemManager.exceptions.get("msg92") : null;
	}
}
