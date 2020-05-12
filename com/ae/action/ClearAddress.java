
package com.ae.action;

import java.awt.event.*;

/** accion especial para limpiar direcciones.
 * 
 */
public class ClearAddress extends AppAbstractAction {
	
	private ConfirmationAction c_action;

	/** nueva instancia
	 * 
	 * @param ae - editor
	 */	
	public ClearAddress(ConfirmationAction ca) {
		super("l03", "Clear", AppAbstractAction.NO_SCOPE, null);
		this.c_action = ca;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		c_action.actionPerformed2(this);
	}
}
