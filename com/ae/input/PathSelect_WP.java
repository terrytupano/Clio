/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.io.*;
import javax.swing.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.gui.*;
import com.ae.gui.wizard.*;
import com.ae.resource.*;

/** panel de entrada del directorio donde se encuentran los datos de la
 * aplicacion contables
 * 
 */
public class PathSelect_WP extends WizardPanel implements ConfirmationAction {

	private JTextField dir_jtf;
	private OpenFileChooser openFC;
		
	public PathSelect_WP() {
		super(WizardPanel.NORMAL);
		this.dir_jtf = new JTextField();
		this.openFC = new OpenFileChooser(this, JFileChooser.DIRECTORIES_ONLY); 
		Box b1 = Box.createHorizontalBox();
		b1.add(dir_jtf);
		b1.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
		b1.add(new JButton(openFC));
		JPanel jp2 = new JPanel(new BorderLayout());
		jp2.add(b1, BorderLayout.NORTH);
		
		setText(DIMain.bundle.getString("m08"));
		setInputComponent(jp2);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#getData()
	 */
	public Object getData() {
		String dir = dir_jtf.getText(); 
		return dir.endsWith("/") ? dir : dir + "/";
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
		File f = new File(dir_jtf.getText());
		return f.isDirectory() ? null: 
			(AplicationException) PrevalentSystemManager.exceptions.get("msg83");
	}

	/* (non-Javadoc)
	 * @see com.ae.action.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */
	public void actionPerformed2(AbstractAction aa) {
		dir_jtf.setText(openFC.getSelectedFile());
	}
}
