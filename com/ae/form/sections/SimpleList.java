/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.resource.*;
import com.ae.form.*;

/** Seccion de entrada que permite seleccionar un elemento que se presenta en
 * una lista desplrgable. Esta clase busta el atributo <code>listType</code>
 * para identificar el tipo elementos disponibles para seleccion.
 * Estos pueden ser:
 * *year, *month
 * para cualquiera de las listas, el valor seleccionado inicialmente es el
 * año o mes actual
 * 
 */
public class SimpleList extends AbstractSection {
	
	private InputRequestPanel i_panel;
	private JComboBox jcbox;
	private Object oldValue;
	private AppEntry[] aents;
	private int selected;
	
	/** Nuevo año grabable
	 */
	public SimpleList() {
		setSectionValue("");
		this.aents = null;
		this.selected = -1;
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractSection#createInputRequestPanel()
	 */
	public InputRequestPanel createInputRequestPanel() {
		this.i_panel = new InputRequestPanel(form);
		
		this.jcbox = FormUtilities.getJComboBox(form, aents);
		jcbox.setSelectedIndex(selected);
		JPanel pan = FormUtilities.getJPanel(form, true);
		pan.setLayout(new GridLayout(2,1));
		pan.add(FormUtilities.getJLabel(form, getAttribute("label")));
		pan.add(jcbox);
		i_panel.add(pan, jcbox);
		return i_panel;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#getSectionInternalValue()
	 */
	public Object getSectionInternalValue() {
		AppEntry ae = (AppEntry) jcbox.getSelectedItem();

		// indice de elemento seleccionado;valor 
			 
		return String.valueOf(jcbox.getSelectedIndex()) + 
			";" + (String) ae.getKey();
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractSection#inputConclude(boolean)
	 */
	public void inputConclude(int c) {
		if (c == InputRequestPanel.ACEPT) {
			AppEntry ae = (AppEntry) jcbox.getSelectedItem();
			setSectionValue((String) ae.getKey());
		} else {
			setSectionInternalValue(oldValue);
		}
		form.inputConclude();	
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractSection#inputRequest(java.awt.Point)
	 */
	public void inputRequest(Point p) {
		oldValue = getSectionInternalValue();
		i_panel.inputRequest(this, p);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.sections.AbstractSection#recalc()
	 */
	public void recalc() {

	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.sections.AbstractSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		
		AppDate dat = new AppDate();
		// *year
		if (getAttribute("listType").equals("*year")) {
			aents = AppConstants.getYears(10);
			selected = aents.length - 1;
		}
		
		// * month
		if (getAttribute("listType").equals("*month")) {
			aents = AppConstants.getConstantsOfType("month");
			selected = dat.getFromCalendar(GregorianCalendar.MONTH);
		}
		setSectionValue((String) aents[selected].getKey());
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#setSectionInternalValue(java.util.Vector)
	 */
	public void setSectionInternalValue(Object v) {
		String[] val = ((String) v).split(";");
		jcbox.setSelectedIndex(Integer.parseInt(val[0]));
		setSectionValue(val[1]);
	}
}
