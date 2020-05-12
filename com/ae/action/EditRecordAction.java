/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import com.ae.core.*;
import com.ae.resource.*;

/** accon estandar para editar registros
 */
public class EditRecordAction extends AppAbstractAction implements ConfirmationAction {
	
	private AbstractRecordDataInput rdInput;
	private AbstractTableDataInput aTable;
	private JDialog dialog;
	
	/** nueva accion de edicion.
	 * 
	 * @param at - tabla a la que notifico
	 */
	public EditRecordAction(AbstractTableDataInput at) {
		super("e03", "Edit", AppAbstractAction.RECORD_SCOPE, "tt07");		
		this.rdInput = null;
		this.aTable = at;
		this.dialog = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		rdInput = (AbstractRecordDataInput) aTable.getRightPanel(this);
		dialog = getDialog(rdInput, rdInput.getDefautlButton(), "e05");
		dialog.show();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */
	public void actionPerformed2(AbstractAction aa) {
		if (aa instanceof AceptAction) {
			PrevalentSystemManager.executeCommand(
				new EditRecordCommand(rdInput.getRecord()));
		}
		dialog.dispose();
	}
}