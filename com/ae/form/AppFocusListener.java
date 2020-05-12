/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/** Extiende <code>FocusAdeapter</code> solo para seleccionar todo el contenido
 * del campo de entrada. Con ello, se facilita la entrada de datos
 * 
 * nota: no sirve. borrar cuando sea definitivo
 * 
 */
public class AppFocusListener extends FocusAdapter implements CaretListener {
	
	public void focusGained(FocusEvent fe) {
		JTextField jtf = (JTextField) fe.getSource();
		int len = jtf.getText().length();
		if (fe.getSource() instanceof JFormattedTextField) {
			JFormattedTextField jftf = (JFormattedTextField) fe.getSource(); 
	//		jtf.selectAll();
			jftf.setCaretPosition(0);
			jftf.moveCaretPosition(len);
		} else {
			jtf.select(0, len);
		}
	}
	public void caretUpdate(CaretEvent ce) {
		System.out.println(ce.getDot());
		System.out.println(ce.getMark());
	}
}
