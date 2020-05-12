/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import java.awt.*;

import com.ae.resource.*;
import com.ae.core.*;
import com.ae.form.*;

/** Seccion de salida para fechas. Esta clase necesita el atributo <code>variable</code>
 * para identificar la seccion del cual tomar la fecha que se presenta aqui.
 * variable puede contener el valor especial *today para indicar que se presentara
 * la fecha de hoy. 
 * 
 */
public class DateOutput extends DateSection {

	private AppDate today;	
	/** Constructor
	 */
	public DateOutput() {
		super();
		this.today = null;
	}
	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#createInputRequestPanel()
	 */
	public InputRequestPanel createInputRequestPanel() {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputConclude(boolean)
	 */
	public void inputConclude(int c) {
	}
	
	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputRequest(java.awt.Point)
	 */
	public void inputRequest(Point p) {
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.sections.AbstractSection#recalc()
	 */
	public void recalc() {
		// no es *today 
		if (today == null) {
			String sec = getAttribute("variable");
			setSectionValue(form.getVariableValue(sec));
		}
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		if (getAttribute("variable").equals("*today")) {
			this.today = new AppDate();
			setSectionValue(today);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ae.form.sections.AbstractSection#setSectionInternalValue(java.lang.Object)
	 */
	public void setSectionInternalValue(Object v) {
		// para que no guarde las fechas de salida. especialmente para 
		// las aquellas con atributo *today
	}

}
