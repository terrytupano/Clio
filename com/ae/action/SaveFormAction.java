
package com.ae.action;

import java.awt.event.*;
import javax.swing.*;

import com.ae.core.*;
import com.ae.form.*;

/** Guardar forma. Esta accion implementa el parametro "autoguardado
 * de la forma" que se encuentra en las preferencias de la aplicacion.
 * 
 */
public class SaveFormAction extends AppAbstractAction {
	
	private FormEditor feditor;
	private Timer timer;
	private boolean act;
	
	/** nueva accion
	 * 
	 * @param ca accion
	 */
	public SaveFormAction(FormEditor e, boolean act) {
		super("g01", "Save", AppAbstractAction.NO_SCOPE, "tt17");
		this.feditor = e;
		this.act = act;
		setEnabled(act);
		Integer dly = (Integer) 
			DIMain.preferenceRecord.getFieldValue("prGAutom");
		this.timer = new Timer(dly.intValue() * 60000, this);
		if (act) {
			timer.start();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == timer) {
			feditor.showMessage("msg82");
		}
		feditor.executeCommand();
	}
}
