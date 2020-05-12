/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import java.awt.*;
import javax.swing.*;

import com.ae.resource.*;
import com.ae.form.*;

/** Permite la entrada de una secuenca de caracteres que luego presenta en
 * forma distribuida a lo largo de la seccion. Esta clase busca el atributo 
 * <code>columns</code> para determinar entra cuantas columnas se desea dividir
 * la seccion
 * 
 */
public class SpacedInput extends SpacedString {
	
	private int columns;
	private InputRequestPanel inputRP;
	private JTextField jtField;
	private Object oldVal;
	private JLabel[] outlabels;
	
	/** Constructor
	 */
	public SpacedInput() {
		setSectionValue("");
		this.columns = 1;
		this.oldVal = null;
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#createInputRequestPanel()
	 */
	public InputRequestPanel createInputRequestPanel() {
		this.inputRP = new InputRequestPanel(form);
		
		// intenta localizar el atributo label, si no existe coloca el titulo
		String lab = getAttribute("label");
		lab = (lab == null) ? getAttribute("title") : lab;
		
		JPanel jp = FormUtilities.getJPanel(form, false);
		jp.setLayout(new GridLayout(2, 1));
		jp.add(FormUtilities.getJLabel(form, lab));
		this.jtField = FormUtilities.getJTextField(form, columns);
		jp.add(jtField);
		
		inputRP.add(jp, jtField);
		return inputRP;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#getSectionInternalValue()
	 */
	public Object getSectionInternalValue() {
		return jtField.getText();
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputConclude(boolean)
	 */
	public void inputConclude(int c) {
		if (c == InputRequestPanel.ACEPT) {
			setSectionValue(jtField.getText());
		} else {
			setSectionInternalValue(oldVal);
		}
		form.inputConclude();
	}
	
	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputRequest(java.awt.Point)
	 */
	public void inputRequest(Point p) {
		oldVal = getSectionInternalValue();
		inputRP.inputRequest(this, p);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#recalc()
	 */
	public void recalc() {
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		this.columns = Integer.parseInt(getAttribute("columns"));
		setColumns(columns);
	}

	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#setSectionInternalValue(java.util.Vector)
	 */	
	public void setSectionInternalValue(Object c) {
		jtField.setText((String) c);
		setSectionValue(c);
	}
}
