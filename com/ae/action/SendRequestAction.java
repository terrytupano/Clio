/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.ae.gui.*;
import com.ae.gui.wizard.*;
import com.ae.input.*;

/** Configura e incia la secuencia de componentes que conforman el asistete
 * para el envio se solicitud de clave de adquisicion.
 * 
 */
public class SendRequestAction extends AppAbstractAction implements ConfirmationAction {
	
	private JDialog dialog;
	private WizardPanel lastPanel;
	private PurchasePanel purchaseP;
	
	/** nueva accion
	 * 
	 */
	public SendRequestAction(PurchasePanel pp) {
		super("s05", "SendRequest", AppAbstractAction.TABLE_SCOPE, "tt18");
		this.purchaseP = pp;
		this.dialog = null;
		this.lastPanel = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		WizardContainer wizardC = new WizardContainer(this);
		wizardC.addPanel(new SendRequest_WP0()); 
		wizardC.addPanel(new SendRequest_WP1()); 
		wizardC.addPanel(new SendRequest_WP2()); 
		this.lastPanel = new SendRequest_WP3(purchaseP);
		wizardC.addPanel(lastPanel); 
		wizardC.next();
				
		this.dialog = getDialog(wizardC, null, "s06");
		dialog.setSize(500, 380);
		dialog.setLocationRelativeTo(null);
		dialog.show();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */
	public void actionPerformed2(AbstractAction aa) {
		
		//cancelar
		if (aa instanceof CancelAction) {
			dialog.dispose();
		}
		
		// Finalizar 
		if (aa instanceof NextAction) {
			Vector dta = (Vector) lastPanel.getData();
			// si el primer elemento del vector de datos es null indica que
			// se desea enviar por correo. de lo contrario, no se hace nada
			// porque los archivos ya se enuentran almacendados donde se
			// desea
			if (dta.elementAt(0) == null) {
				send(dta);
			}
		}
	}

	/** somete envio de solicitud via correo electronico
	 * 
	 * @param dta - datos del mensaje
	 */
	private void send(Vector dta) {
		ProgressPanel pp = new ProgressPanel(ProgressPanel.EMAIL);
		dialog.setVisible(false);
		dialog = getDialog(pp, null, "e16");
		Thread t = new Thread(new SendEmail(pp, dta));
		t.start();
		dialog.setVisible(true);
	}
}
