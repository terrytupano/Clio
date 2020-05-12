/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;

/** Cargar. Esta accion comienza estando inhabilitada.
 * 
 */
public class Load extends AppAbstractAction {
	
	private ConfirmationAction c_action;
	
	/** nueva accion
	 * 
	 * @param ca accion
	 */
	public Load(ConfirmationAction ca) {
		super("c04", "Load", AppAbstractAction.NO_SCOPE, "tt12");
		this.c_action = ca;
		setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		c_action.actionPerformed2(this);
	}
}
