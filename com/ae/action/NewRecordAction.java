/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;
import javax.swing.*;

import com.ae.core.*;
import com.ae.resource.*;

/** accion estandar para crear un nuevo registro
 * 
 */
public class NewRecordAction extends AppAbstractAction implements ConfirmationAction {
	
	private AbstractRecordDataInput rdInput;
	private AbstractTableDataInput atable;
	private JDialog dialog;
	
	/** nueva accion
	 * 
	 * @param at - Tabla a la que notifica
	 */
	public NewRecordAction(AbstractTableDataInput at) {
		super("n03", "New", AppAbstractAction.TABLE_SCOPE, "tt13");
		this.rdInput = null;
		this.atable = at;
		this.dialog = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		rdInput = (AbstractRecordDataInput) atable.getRightPanel(this);
		dialog = getDialog(rdInput, rdInput.getDefautlButton(), "a10");
		dialog.show();
	}
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */
	public void actionPerformed2(AbstractAction aa) {
		if (aa instanceof AceptAction) {
			PrevalentSystemManager.executeCommand(new NewRecordCommand(rdInput.getRecord()));
		}
		dialog.dispose();
	}
}
