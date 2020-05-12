/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import com.ae.gui.*;

/** Presenta la ventana de "Tip del dia"
 * 
 */
public class TipAction extends AppAbstractAction implements ConfirmationAction {
	
	private JDialog dialog;
	private Random random;
	
	/** nueva accion
	 * 
	 */
	public TipAction() {
		super("t03", "TipsIcon", AppAbstractAction.NO_SCOPE, "tt20");
		this.dialog = null;
		this.random = new Random();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		
		TipsPanel tp = new TipsPanel();
		
		// acciones
		JButton okjb = new JButton(new OkAction(this)); 
		Box b1 = Box.createHorizontalBox();
		b1.add(Box.createHorizontalGlue());
		b1.add(new JButton(new PreviousAction(tp)));
		b1.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
		JButton jb = new JButton(new NextAction(tp));
		jb.setHorizontalTextPosition(JLabel.LEFT);
		b1.add(jb);

		b1.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
		b1.add(okjb);
		
		JPanel jp1 = new JPanel(new BorderLayout());
		jp1.add(tp, BorderLayout.NORTH);
		jp1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP), BorderLayout.CENTER);
		jp1.add(new JLabel(getString("meb")), BorderLayout.SOUTH);
		
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(jp1, BorderLayout.NORTH);
		jp.add(GUIUtilities.getBoxForButtons(b1, false), BorderLayout.SOUTH);
		GUIUtilities.setEmptyBorder(jp);
		dialog = getDialog(jp, okjb, "t04");
		
		tp.showTip(random.nextInt(16));
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
