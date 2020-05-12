/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.util.*;

import javax.swing.table.*;

/** Clase destinada solo para controlar el retorno correcto de las 
 * clases segun la columna.
 * 
 */
public class AppDefaultTableModel extends DefaultTableModel {
		
	private Vector rcd;
		
	/** nueva instancia
	 * 
	 * @param vr - vector de registros. Esta clase espera que cada 
	 * elemento sea un vector
	 * @param vc - nombre de columnas
	 */
	public AppDefaultTableModel(Vector vr, Vector vc) {
		super(vr, vc);
		this.rcd = (Vector) vr.elementAt(0);
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int ci) {
		return rcd.elementAt(ci).getClass(); 
	}
}