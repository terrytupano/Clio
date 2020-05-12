/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;
import java.util.logging.*;
import com.ae.gui.*;
import com.ae.resource.*;

/** presenta la ventana acerca de la aplicacion
 * 
 */
public class AboutAction extends AppAbstractAction implements ConfirmationAction {
	
	private JDialog dialog;
	
	/** nueva accion
	 * 
	 */
	public AboutAction() {
		super("a03", "About", AppAbstractAction.NO_SCOPE, "tt01");
		this.dialog = null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		
		JEditorPane jep = new JEditorPane();
		Dimension dim = new Dimension(400, 294);
		jep.setPreferredSize(dim);
		jep.setOpaque(false);
		try {
			jep.setEditable(false);		
			jep.setPage(ResourceUtilities.getURL("About"));
		} catch(Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
		
		// acerca de
		JPanel pa = new JPanel(new BorderLayout());
		pa.setBackground(Color.WHITE);
		pa.add(jep, BorderLayout.CENTER);
		pa.setBorder(new LineBorder(Color.LIGHT_GRAY));

		// propiedades
		Properties prp = System.getProperties();
		Enumeration e = prp.propertyNames();
		Vector vc = new Vector(2);
		vc.add(getString("n01"));
		vc.add(getString("v01"));
		Vector vr = new Vector();
		while (e.hasMoreElements()) {
			Vector rd = new Vector(2);
			String k = (String) e.nextElement();
			rd.add(k);
			rd.add(prp.getProperty(k));
			vr.add(rd);
		}
		JTable jt = new JTable(vr, vc);
		GUIUtilities.fixTableColumn(jt, new int[] {170, 300});
		jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jt.setEnabled(false);
		JScrollPane jsp = new JScrollPane(jt);
		jsp.setPreferredSize(dim);
		JPanel pb = new JPanel(new BorderLayout());
		pb.add(jsp, BorderLayout.CENTER);
		
		JTabbedPane jtp = new JTabbedPane();
		jtp.add(getString("a03"), pa);
		jtp.add(getString("p01"), pb);
		
		// ok
		JButton jb = new JButton(new OkAction(this));
		Box b1 = Box.createHorizontalBox();
		b1.add(Box.createHorizontalGlue());
		b1.add(jb);
		
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(GUIUtilities.getBoxForButtons(b1, false), BorderLayout.SOUTH);
		jp.add(jtp, BorderLayout.CENTER);
		GUIUtilities.setEmptyBorder(jp);
		dialog = getDialog(jp, jb, "a03");
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
