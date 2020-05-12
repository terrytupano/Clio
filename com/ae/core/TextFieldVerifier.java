/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.util.*;
import java.text.*;
import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import com.ae.gui.*;

/** especializacion de <code>InputVerifier</code> para la verificacion de 
 * los datos de entrada. Adicionalmente, esta atento si el componente no
 * puede contener 0 o balco (es requerido). Este verificador emite mensajes
 * directamente al panel que contiene al componente segun algunos de los eventos 
 * que puedan ocurrir.
 */
public class TextFieldVerifier extends InputVerifier {
	
	private boolean required;
	private RightPanel rPanel;
	
	/** nuevo verificador de entrada
	 * 
	 * @param rp - <code>RightPanel</code> donde enviar los mensajes
	 * @param req =true, este componente no puede estar en blanco
	 */
	public TextFieldVerifier(RightPanel rp, boolean req) {
		this.rPanel = rp;
		this.required = req;
	}

	/** determina si hay un valor en un campo requerido. 
	 * Si el campo no es requerido retorna true;
	 * 
	 * @param input - componente de entrada
	 * @return =true si el campo componente de entrada contiene un valor
	 * distinto de 0 o blanco o este no es de entrada obligatoria 
	 */
	private boolean requiredValueSet(JComponent input) {
		boolean req = true;
		String t = (((JTextField) input).getText()).trim();
		if (required) {
			
			// jformattedtextfield para fecha y numeros
			if (input instanceof JFormattedTextField) {
				JFormattedTextField ftf = (JFormattedTextField) input;
				AbstractFormatter formatter = ftf.getFormatter();
				try {
					Object val = formatter.stringToValue(t);
					req = (val instanceof Date);
					if (val instanceof Number) {
						req = (((Number) val).doubleValue() != 0.0);
					}
				} catch (Exception e) {
					req = false;
				}
				
			 // jtextField para texto
			} else {
				req = !(t.trim().equals(""));
			}
		}
		return req;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.InputVerifier#shouldYieldFocus(javax.swing.JComponent)
	 */
	public boolean shouldYieldFocus(JComponent input) {
		boolean yield = verify(input);
		boolean req = requiredValueSet(input);
		if (!req) {
			rPanel.showMessage("msg89");
		}
		if (req && !yield) {
			rPanel.showMessage("msg90");
		} 
		return yield;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
	 */
	public boolean verify(JComponent input) {
		boolean ok = false;
		if (input instanceof JFormattedTextField) {
			JFormattedTextField ftf = (JFormattedTextField) input;
			AbstractFormatter formatter = ftf.getFormatter();
			if (formatter != null) {
				 try {
					String t = ftf.getText();
					formatter.stringToValue(t);
					ok = requiredValueSet(input);
				 } catch (ParseException pe) {
					ok = false;
				 }
			}
		} else {
			ok = requiredValueSet(input);
		}
		return ok;
	}
}