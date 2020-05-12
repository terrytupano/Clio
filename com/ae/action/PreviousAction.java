/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;

import com.ae.gui.wizard.*;

	/** <<< anterior
	 * 
	 */
	public class PreviousAction extends AppAbstractAction {
		
		private Navigator nav;

		/** nueva instancia
		 */		
		public PreviousAction(Navigator nav) {
			super("a12", "Previous", AppAbstractAction.NO_SCOPE, null);
			this.nav = nav;
		}
		
		/* (non-Javadoc)
		 * @see com.ae.core.AppAbstractAction#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			nav.previous();
		}
	}
