/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.resource.*;

/** Especializacion de <code>JPopupMenu</code> usado dentro de la aplicacion.
 * Este mantene una lista con la relacion entre el elemento de menu presentado
 * en la lista y un <code>AppEntry</code>
 *  
 */
public class AppPopupMenu extends JPopupMenu implements ActionListener {
	
	private ConfirmationAction c_action;
	private AppEntry item;
	private Hashtable list;
	private JMenuItem noitem;

	/** nuevo menu desplegable. 
	 * 
	 * @param ae - lista con contenido a mostrar
	 * @param ico - icono para cada elemento de menu
	 * @param ca - accion de confirmacion
	 */	
	public AppPopupMenu(AppEntry[] ae, String ico, ConfirmationAction ca) {
		this.accessibleContext = null;
		this.list = new Hashtable(ae.length);
		this.c_action = ca;
		this.item = null;
		this.noitem = new JMenuItem(DIMain.bundle.getString("n09"));
		noitem.setEnabled(false);
		ImageIcon ii = ResourceUtilities.getSmallIcon(ico);
		if (ae.length == 0) {
			add(noitem);
			noitem.setIcon(ii);
		}
		for (int e = 0; e < ae.length; e++) {
			JMenuItem mi = new JMenuItem((String) ae[e].getValue());
			mi.setIcon(ii);
			mi.addActionListener(this);
			add(mi);
			list.put(mi, ae[e]);
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		this.item = (AppEntry) list.get(ae.getSource());
		c_action.actionPerformed2(null);
	}
	
	/** Retorna <code>AppEntry</code> asociado con el elemento seleccionado
	 * 
	 * @return elemento seleccionado
	 */
	public AppEntry getSelectedItem() {
		return item;
	}

}
