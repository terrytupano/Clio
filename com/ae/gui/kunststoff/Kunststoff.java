/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.kunststoff;

import java.util.logging.*;

import javax.swing.*;

import com.ae.core.*;
import com.incors.plaf.kunststoff.*;

/** extencion de <code>KunststoffLookAndFeel</code> para la aplicacion
 * 
 */
public class Kunststoff extends KunststoffLookAndFeel implements Keeper {

	/** nueva instancia
	 */
	public Kunststoff() {
		super();
	}

	/** Metodo modificado. Ejecuta <code>super()</code> y adiciona entrada
	 * para <code>FormattedTextField</code>
	 * 
	 * @param table - table con valores por omision
	 */
	protected void initClassDefaults(UIDefaults table) {
		super.initClassDefaults(table);

		table.put(
			"FormattedTextFieldUI",
			"com.incors.plaf.kunststoff.KunststoffTextFieldUI");
		table.put(
			"CheckBoxUI",
			"com.ae.gui.kunststoff.KunststoffCheckBoxUI");
		try {
		  String className = "com.ae.gui.kunststoff.KunststoffCheckBoxIcon";
		  table.put("CheckBox.icon", className);
		} catch (Exception ex) {
			Logger l = Logger.getLogger("");
			l.logp(Level.WARNING, null, null, "Look and feel error.");
		}
		
		/*
		table.put("LeftComponentTreeUI", 
			"com.incors.plaf.kunststoff.KunststoffTreeUI");
		*/
	}
}