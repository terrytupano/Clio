/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;

/** Accion que presenta el dialogo de seleccion de archivos y ejecuta
 * la carga de arreglos temporales
 * 
 */
public class LoadTemporalFix extends AppAbstractAction {
	
	private ConfirmationAction c_action;
	
	/** nueva accion
	 * 
	 * @param ca accion
	 */
	public LoadTemporalFix(ConfirmationAction ca) {
		super("ACargar", "Load", AppAbstractAction.NO_SCOPE, null);
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
