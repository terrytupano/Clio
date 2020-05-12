/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;
import java.awt.print.*;

//import javax.print.attribute.*;

	/** Presenta dialgo de seleccion de impresora segun atributos del
	 * trabajo
	 * 
	 */
	public class PrintDialogAction extends AppAbstractAction {
		
		private PrinterJob pjob;

		/** nueva instancia
		 * 
		 * @param pj - trabajo de impresion
		 */				
		public PrintDialogAction(PrinterJob pj) {
			super("i18", "Print", AppAbstractAction.NO_SCOPE, "tt24");
			this.pjob = pj;
		}
		
		/* (non-Javadoc)
		 * @see com.ae.core.AppAbstractAction#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {

//	cross plataform dialog			
//			PrintRequestAttributeSet as = new HashPrintRequestAttributeSet();
//			pjob.printDialog(as);
			pjob.printDialog();

		}
	}
