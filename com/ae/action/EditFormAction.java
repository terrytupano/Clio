
package com.ae.action;

import java.awt.event.ActionEvent;
import javax.swing.*;

import com.ae.form.*;

/** Editar una forma salvada
 */
public class EditFormAction extends AppAbstractAction {
	
	private FS_Table savedF;
	private JDialog dialog;
	
	public EditFormAction(FS_Table sv) {
		super("e03", "Edit", AppAbstractAction.RECORD_SCOPE, "tt06");
		this.savedF = sv;		
		this.dialog = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		FormEditor fe = (FormEditor) savedF.getRightPanel(this);
		dialog = getDialogForEditor(fe, null, "e04");
		fe.init();
		dialog.show();
	}
}