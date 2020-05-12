/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;
import javax.swing.*;

import com.ae.core.*;
import com.ae.input.*;
import com.ae.resource.*;

/** presenta la ventana de preferencias de la aplicacion
 * 
 */
public class PreferenceAction extends AppAbstractAction implements ConfirmationAction {
	
	private JDialog dialog;
	private PreferenceDialog pDlg;
	
	/** nueva accion
	 * 
	 */
	public PreferenceAction() {
		super("p02", "Preference", AppAbstractAction.NO_SCOPE, "tt15");
		this.dialog = null;
		this.pDlg = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		pDlg = new PreferenceDialog(this);
		dialog = getDialog(pDlg, pDlg.getDefautlButton(), "p02");
		dialog.show();
	}
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */
	public void actionPerformed2(AbstractAction aa) {
		if (aa instanceof AceptAction) {
			DIMain.preferenceRecord = (PreferenceRecord) pDlg.getRecord();
			PrevalentSystemManager.executeCommand(new EditRecordCommand(pDlg.getRecord()));
		}
		dialog.dispose();
	}
}
