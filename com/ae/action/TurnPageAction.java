/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;
import javax.swing.*;

import com.ae.form.*;
import com.ae.resource.*;

/** indica a <code>FormEditor</code> que de vuelta a la pagina. 
 */
public class TurnPageAction extends AppAbstractAction {
		
	private ImageIcon[] actIcon;
	private FormEditor form_editor;
		
	public TurnPageAction (FormEditor fe) {
		super("h04", "toReversePage", AppAbstractAction.NO_SCOPE, "tt22");
		this.actIcon = new ImageIcon[2];
		this.actIcon[Form.OVERSE] = ResourceUtilities.getSmallIcon("toOversePage");
		this.actIcon[Form.REVERSE] = ResourceUtilities.getSmallIcon("toReversePage");
		this.form_editor = fe;
		setEnabled(form_editor.getForm().isReverseAvailable());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		int p = (form_editor.getForm().getVisibleFace() == Form.REVERSE) ?
			Form.OVERSE : Form.REVERSE;
		putValue(SMALL_ICON, actIcon[p]);  
		form_editor.getForm().turnPage(p);
	}
}
