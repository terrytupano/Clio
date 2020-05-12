
package com.ae.action;

import java.awt.event.*;
import javax.swing.*;

import com.ae.input.*;

/** Presenta el editor de direcciones dentro de un dialogo de entrada
 * 
 */
public class AddressEditAction extends AppAbstractAction implements ConfirmationAction {
	
	private AddressEditor editor;
	private JDialog dialog;

	/** nueva accion de edicion.
	 * 
	 * @param ae - editor
	 * @param nr - =true si es u registro nuevo. Usado para determinar el comando
	 * que alterara la informacion de la tabla
	 */	
	public AddressEditAction(AddressEditor ae) {
		super(null, "Address", AppAbstractAction.NO_SCOPE, "tt02");
		this.editor = ae;
		this.dialog = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		dialog = getDialog(editor, editor.getDefautlButton(), "e02");
		dialog.show();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */
	public void actionPerformed2(AbstractAction aa) {
		if (!(aa instanceof CancelAction)) {
			editor.done(aa);
		}
		dialog.dispose();
	}
}
