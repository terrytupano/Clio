/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;

import com.ae.gui.wizard.*;
import com.ae.resource.*;

	/** Siguiente >>
	 * 
	 */
	public class NextAction extends AppAbstractAction {
		
		public static int NEXT = 0; 
		public static int FINALIZE = 1;
		private int status;
		private String txt, imagen;
		private Navigator nav;

		/** Crea nuava instancia de esta accion.
		 * 
		 * @param n - navegador 
		 */		
		public NextAction(Navigator n) {
			super("s04", "Next", AppAbstractAction.NO_SCOPE, null);
			this.nav = n;
			this.status = NEXT;
		}
		
		/* (non-Javadoc)
		 * @see com.ae.core.AppAbstractAction#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			nav.next();
		}
		
		/** retorna el estado actual de esta accion.
		 * 
		 * @return <code>NEXT o FINALIZE</code>
		 */
		public int getStatus() {
			return status;
		}
		
		/** cambia el aspecto visual de esta accion hacia la accion descrita 
		 * como argumento.
		 * 
		 * @param m - nueva estado
		 */
		public void changeStatus(int m) {
			this.status = NEXT;
			this.txt = getString("s04");
			this.imagen = "Next";
			if (m == FINALIZE) {
				this.txt = getString("f01");
				this.imagen = "Ok";
				this.status = FINALIZE;
			}
			putValue(SMALL_ICON, ResourceUtilities.getSmallIcon(imagen));
			putValue(NAME, txt);
		}
	}	
