/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;
import java.util.logging.*;

import javax.help.*;

import com.ae.resource.*;

/** Accion destinada a preparar y presentar <code>HelpBroker</code> con el
 * contenido del archivo xxx.hs pasado como argumento.
 * 
 */
public class HelpAction extends AppAbstractAction {

	private String hsFile;

	/** nueva instancia de este objeto
	 * 
	 * @param txt - texto de la accion 
	 * @param in - Nombre del icono para esta accion 
	 * @param hsf - archivo hs (sin extencion) 
	 */
	public HelpAction(String txt, String in, String hsf) {
		super(txt, in, AppAbstractAction.NO_SCOPE, "tt09");
		this.hsFile = hsf + ".hs";
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		try {
			HelpSet hs =
				new HelpSet(null, ResourceUtilities.getURL(hsFile));
			hs.createHelpBroker().setDisplayed(true);
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
	}
}