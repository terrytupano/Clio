/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.*;
import com.ae.gui.wizard.*;
import com.ae.input.*;
import com.ae.resource.*;

/** contruye e inicia la secuencia de paneles del asistente para importar
 * cuentas contables
 * 
 */
public class ImportAccountsAction extends AppAbstractAction implements ConfirmationAction {
	
	private JDialog dialog;
	private WizardContainer wizardC;
	private WizardPanel wp3;
	private ImportAccount_WP4 wp4;
	private Account_ClioObject acc_clo;
	
	/** nueva accion
	 * 
	 */
	public ImportAccountsAction(Account_ClioObject aco) {
		super("i01", "Import", AppAbstractAction.TABLE_SCOPE, "tt10");
		this.acc_clo = aco;
		this.dialog = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		this.wizardC = new WizardContainer(this);
		wizardC.addPanel(new ImportAccount_WP0());
		String[] a_d = getAppAndDir();
		if (a_d[0].equals("")) {
			wizardC.addPanel(new ImportAccount_WP1()); 
			wizardC.addPanel(new PathSelect_WP());
		}
		this.wp3 = new ImportAccount_WP3(a_d[0], a_d[1]);
		wizardC.addPanel(wp3);
		this.wp4 = new ImportAccount_WP4();
		wizardC.addPanel(wp4); 
		wizardC.next();
				
		this.dialog = getDialog(wizardC, null, "i02");
		dialog.setSize(450, 380);
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
			proceed();
		}
	}

	/** inicia preparativos y somete la importacion y/o asocicacion de cuentas
	 * contables
	 * 
	 */	
	private void proceed() {
		ProgressPanel pp = new ProgressPanel(ProgressPanel.ACCOUNT);
		dialog.setVisible(false);
		dialog = getDialog(pp, null, "i04");
		Vector v = (Vector) wp3.getData();
		boolean auto = ((Boolean) v.elementAt(0)).booleanValue();
		
		// automatico o manual
		if (auto) {
			wp4.selectAll(wizardC);
		}
		Vector dta = (Vector) wp4.getData();
		Thread t = new Thread(new ImportAndAssociate(acc_clo, dta, pp, auto));
		t.start();
		dialog.setVisible(true);
	}
	
	/** determina la aplicacion y directorio origen para las cuentas 
	 * importadas. 
	 * 
	 * @return {id de aplicacion, directorio origen}
	 */
	private String[] getAppAndDir() {
		AccountRecord accr = new AccountRecord();
		AppTableModel atm = PrevalentSystemManager.getTableModel(accr, false);
		String[] ap_di = new String[] {"", ""};
		for (int a = 0; a < atm.getRowCount(); a++) {
			AccountRecord r = (AccountRecord) atm.getRecordAt(a);
			if (!r.getFieldValue("acIApp").equals("")) {
				ap_di[0] = (String) r.getFieldValue("acIApp");
				ap_di[1] = (String) r.getFieldValue("acIDir");
				break;
			}
		}
		return ap_di;
	}
}
