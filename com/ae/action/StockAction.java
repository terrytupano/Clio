/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;

import javax.swing.*;

import com.ae.gui.*;
import com.ae.input.*;

/** presenta la ventana de preferencias de la aplicacion
 * 
 */
public class StockAction extends AppAbstractAction implements ConfirmationAction {
	
	private JDialog dialog;
	
	/** nueva accion
	 * 
	 */
	public StockAction() {
		super("i07", "Stock", AppAbstractAction.NO_SCOPE, "tt19");
		this.dialog = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		RightPanel rp = new RightPanel("Stock");
		JTabbedPane main = new JTabbedPane();
		main.addTab(getString("i07"), new StockPanel(rp));
		main.addTab(getString("o02"), new PurchasePanel());

		// Acciones
		AbstractAction[] bts = new AbstractAction[] {
			new OkAction(this)};
			
		rp.add(main);
		dialog = getDialog(rp, rp.setActionBar(bts), "i07");
		dialog.show();
	}
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */
	public void actionPerformed2(AbstractAction aa) {
		dialog.dispose();
	}
}
