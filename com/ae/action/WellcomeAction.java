/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;

import com.ae.core.*;

/** presenta la ventana acerca de la aplicacion
 * 
 */
public class WellcomeAction extends AppAbstractAction {
	
	/** nueva accion
	 * 
	 */
	public WellcomeAction() {
		super("c06", "Wellcome", AppAbstractAction.NO_SCOPE, "tt23");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		DIMain.leftComponentManager.setRightComponent(LeftComponentManager.WELLCOME);
	}	
}
