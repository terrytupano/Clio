/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.ae.gui.*;

/** Presenta la ventana de con noticias desde w3.clio-ve.com
 * 
 */
public class NewsAction extends AppAbstractAction implements ConfirmationAction {
	
	private JDialog dialog;
	private NewsPanel newsP;
	
	/** nueva accion
	 * 
	 */
	public NewsAction() {
		super("n11", "NewsIcon", AppAbstractAction.NO_SCOPE, "tt21");
		this.dialog = null;
		this.newsP = new NewsPanel();
		setEnabled(newsP.isConnected());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		// acciones
		JButton okjb = new JButton(new OkAction(this)); 
		Box b1 = Box.createHorizontalBox();
		b1.add(Box.createHorizontalGlue());
		b1.add(okjb);
		
		JPanel jp1 = new JPanel(new BorderLayout());
		jp1.add(newsP, BorderLayout.NORTH);
		jp1.add(b1, BorderLayout.SOUTH);
		
//		jp1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP), BorderLayout.CENTER);
//		jp1.add(new JLabel(getString("meb")), BorderLayout.SOUTH);
		
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(jp1, BorderLayout.NORTH);
		jp.add(GUIUtilities.getBoxForButtons(b1, false), BorderLayout.SOUTH);
		GUIUtilities.setEmptyBorder(jp);
		dialog = getDialog(jp, okjb, "n12");
		
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
