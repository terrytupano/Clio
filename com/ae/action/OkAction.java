/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;

/** Accion estandar para solo cerrar el cuadro de dialogo
 * 
 */
public class OkAction extends AppAbstractAction {
	
	private ConfirmationAction c_action;
	
	/** nueva accion
	 * 
	 * @param ca accion
	 */
	public OkAction(ConfirmationAction ca) {
		super("o01", "ok", AppAbstractAction.NO_SCOPE, null);
		this.c_action = ca;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		c_action.actionPerformed2(this);
	}
}
