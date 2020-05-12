/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.*;
import com.ae.gui.wizard.*;
import com.ae.input.*;
import com.ae.resource.*;

/** Construye e inicia la secuencia paneles que conforman el asistente para 
 * importar movimientos contables. este estara activo solo si se detecta
 * origenes de importacion en la tabla de entradas contables.
 * 
 */
public class ImportVouchersAction extends AppAbstractAction implements ConfirmationAction {
	
	private JDialog dialog;
	private WizardContainer wizardC;
	private TaxPayerSelector tp_selector;
	private String orgDta;
	
	/** nueva accion
	 * 
	 */
	public ImportVouchersAction(TaxPayerSelector tps) {
		super("i01", "Import", AppAbstractAction.NO_SCOPE, "tt11");
		this.tp_selector = tps;
		this.dialog = null;
		this.orgDta = getOrgApp();
		setEnabled(!orgDta.equals(""));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		this.wizardC = new WizardContainer(this);
		wizardC.addPanel(new ImportVoucher_WP1()); 
		wizardC.addPanel(new ImportVoucher_WP3(orgDta)); 
		wizardC.next();
				
		this.dialog = getDialog(wizardC, null, "i03");
		dialog.setSize(500, 380);
		dialog.setLocationRelativeTo(null);
		dialog.show();
	}
	
	/** retorna la aplicacion usada para importar cuentas contables
	 * para este contribuyente. este metodo buscara hasta que encuentre 
	 * al menos una entrada que haya sido importada. 
	 * 
	 * @return - id de aplicacion.
	 */
	private String getOrgApp() {
		TaxPayerRecord tpr = tp_selector.getSelectedRecord();
		AccountRecord accr = new AccountRecord();
		
		// si no existe ningun contribuyente
		if (tpr != null) {
			AppTableModel atm = PrevalentSystemManager.getTableModel(accr);
			atm.selectRecord("'{acRifTP}' == '" + tpr.getFieldValue(0) + "'");
			for (int a = 0; a < atm.getRowCount(); a++) {
				accr = (AccountRecord) atm.getRecordAt(a);
				if (!accr.getFieldValue("acIApp").equals("")) {
					break;
				}
			}
		}
		return (String) accr.getFieldValue("acIApp");
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

		if (aa instanceof NextAction) {
			ProgressPanel pp = new ProgressPanel(ProgressPanel.VOUCHER);
			dialog.setVisible(false);
			dialog = getDialog(pp, null, "i03");
			Thread t = new Thread(new ImportVouchers(pp, dialog, tp_selector));
			t.start();
			dialog.setVisible(true);
		}
	}
}
