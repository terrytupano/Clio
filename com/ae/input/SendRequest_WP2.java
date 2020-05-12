/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.gui.*;
import com.ae.gui.wizard.*;
import com.ae.resource.*;

/** panel de entrada para determinar el tipo de envio. Guardar o enviar por 
 * correo.
 * 
 */
public class SendRequest_WP2 extends WizardPanel implements ActionListener {

	private JTextField file_jtf;
	private JRadioButton email_rb, file_rb;
	private Box fileBox;

	/** nueva instancia
	 *
	 */
	public SendRequest_WP2() {
		super(WizardPanel.NORMAL);
		this.email_rb = new JRadioButton(
			DIMain.bundle.getString("e11") + " " + DIMain.bundle.getString("c17"));
		email_rb.addActionListener(this);
		this.file_rb = new JRadioButton(DIMain.bundle.getString("g01"));
		file_rb.addActionListener(this);
		ButtonGroup bg = new ButtonGroup();
		bg.add(email_rb);
		bg.add(file_rb);
		this.file_jtf = new JTextField();
		this.fileBox = Box.createHorizontalBox();
		fileBox.add(file_rb);
		fileBox.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
		fileBox.add(file_jtf);
		fileBox.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
		fileBox.add(new JButton(new SavePurchase()));
		JPanel jp2 = new JPanel(new BorderLayout());
		JPanel jp3 = new JPanel(new BorderLayout(4, 4));
		jp3.add(email_rb, BorderLayout.NORTH);
		jp3.add(fileBox, BorderLayout.SOUTH);
		jp3.setBorder(BorderFactory.createTitledBorder(DIMain.bundle.getString("f08")));
		jp2.add(jp3, BorderLayout.NORTH);

		email_rb.doClick();
		setText(DIMain.bundle.getString("m18"));
		setInputComponent(jp2);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		boolean en = (e.getSource() == file_rb);
		GUIUtilities.setEnabled(fileBox.getComponents(), en);
		file_rb.setEnabled(true);
	}
	
	/** retorna nombre del archivo donde se desea guardar los archivos
	 * para la solicitud de formas. retorna <code>null</code> si se ha seleccionado
	 * enviar por correo electronico
	 * 
	 * @return - nombre absoluto de archivo
	 *  
	 * @see com.ae.gui.wizard.WizardPanel#getData()
	 */
	public Object getData() {
		return file_jtf.isEnabled() ? file_jtf.getText() : null;
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
		AplicationException ae = null;
		if (file_jtf.getText().trim().equals("") && file_rb.isSelected()) {
			ae = (ActionException) PrevalentSystemManager.exceptions.get("msg93");
		}
		return ae;
	}

	/** accion que abre <code>JFileChooser</code> para permitir al usuario
	 * cuardar la orden de compra
	 * 
	 */
	public class SavePurchase extends AppAbstractAction {

		/** nueva accion
		 * 
		 */
		public SavePurchase() {
			super("g01", "Save", AppAbstractAction.NO_SCOPE, null);
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int x = jfc.showSaveDialog(null);
			if (x == JFileChooser.APPROVE_OPTION) {
				//			String fn = jfc.getSelectedFile().getName();

				// se asume que no se colocara extencion
				//			int ip = fn.lastIndexOf('.');
				//			fn = fn.substring(0, (ip == -1) ? fn.length() : ip);
				file_jtf.setText(jfc.getSelectedFile().getAbsolutePath());
			}
		}
	}
}
