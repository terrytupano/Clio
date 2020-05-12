/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.*;

/** panel de pestañas con listas de variables del sistema.
 * 
 */
public class SysVar_T extends RightPanel implements Keeper {

	/** Constructor sin argumentos
	 *
	 */
	public SysVar_T() {
		super("SysVar");
		JTabbedPane jtabp = new JTabbedPane();

		TributaryUnit_T tut = new TributaryUnit_T();
		GUIUtilities.setEmptyBorder(tut);
		jtabp.add(DIMain.bundle.getString("u03"), tut);

		TaxKeep_T tk = new TaxKeep_T();
		GUIUtilities.setEmptyBorder(tk);
		jtabp.add(DIMain.bundle.getString("r06"), tk);

		Fare_T fa = new Fare_T();
		GUIUtilities.setEmptyBorder(fa);
		jtabp.add(DIMain.bundle.getString("t15"), fa);

		add(jtabp);
	}	
}