
package com.ae.action;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import com.ae.gui.*;
import com.ae.resource.*;
import com.ae.core.*;

/** Asigna nueva forma desde la tabla de contribuyentes. 
 * 
 */
public class AllocateFormAction extends AbstractNewFormAction implements ConfirmationAction {
	
	private AbstractTableDataInput atable;
	private AppPopupMenu popup;
	private String f_name;
	private TaxPayerRecord taxPayer;
	
	public AllocateFormAction(AbstractTableDataInput at) {
		super("a06", "AForm", AppAbstractAction.RECORD_SCOPE, "tt03");
		this.atable = at;
		this.f_name = null;
		this.taxPayer = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		taxPayer = (TaxPayerRecord) atable.getRecord();
		Vector vec = new Vector();
		AppEntry[] ape =  ResourceUtilities.getInstalledForms();
		for (int x = 0; x < ape.length; x++) {
			if (checkConditions(ape[x], taxPayer)) {
				vec.add(ape[x]);
			} 
		}
		popup = new AppPopupMenu((AppEntry[]) vec.toArray(new AppEntry[vec.size()]),
			"InstaledFormsDefaultIcon", this);
		JButton jb = (JButton) arg0.getSource();
		Rectangle r = jb.getBounds();
		popup.show(jb, 0, r.height);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */
	public void actionPerformed2(AbstractAction aa) {
		AppEntry ae = popup.getSelectedItem();
		f_name = (String) ae.getKey();
		confirm(ae, taxPayer);
	}
}

