/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import java.awt.*;
import java.util.*;
import java.text.*;

import com.ae.resource.*;
import com.ae.form.*;

/** presenta un valor como salida para la forma. Esta clase analiza en contenido
 * del atributo <code>variable</code> para identificar cual campo de base de datos 
 * debe presentar. este atributo tambien puede contener los valores especiales:
 * - *date: dia del mes 
 * - *month: mes del año
 * - *month_ft: mes del año. 
 * - *year: año
 * Puede estar presente el atributo <code>constant</code> que indica
 * a esta clase que presente el valor de dicho atributo como salida
 * 
 * El valor de la seccion es establecido en el siguiente orden 
 * - presencia de  <code>constant</code>
 * - presencia de <code>variable</code> con valores especiales
 * - presencia de <code>variable</code> con id de seccion o campo
 * 
 * si el atributo variable contiene *month, *date o *year la seccion no sera
 * elegible para salvar 
 */
public class SimpleOutput extends AbstractSection {
	
	private boolean eValue;
	
	/** Constructor
	 */
	public SimpleOutput() {
		setSectionValue("");
		this.eValue = false;
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
	 * @see com.ae.form.AbstractFormSection#recalc()
	 */
	public void recalc() {
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#createInputRequestPanel()
	 */
	public InputRequestPanel createInputRequestPanel() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		Object value = null;

		// constant
		String ce = getAttribute("constant");
		if (ce != null) {
			value = ce;
		}
		
		// variable
		String fld = getAttribute("variable");
		if (fld != null) {
			GregorianCalendar gc = new GregorianCalendar();
			
			// valores especiales
			if (fld.equals("*date")) {
				value = String.valueOf(gc.get(GregorianCalendar.DATE));
				this.eValue = true;
			}
			if (fld.equals("*month")) {
				DateFormatSymbols dfs = new DateFormatSymbols();
				value = dfs.getMonths()[gc.get(GregorianCalendar.MONTH)];
				this.eValue = true;
			}
			if (fld.equals("*year")) {
				value = String.valueOf(gc.get(GregorianCalendar.YEAR));
				this.eValue = true;
			}
			
			// nombre de campo
			if (!fld.startsWith("*")) {
				value = tpr.getFieldValue(getAttribute("variable"));	
			}
		}
		setSectionValue(value);
	}
	
	/* (non-Javadoc)
	 * @see com.ae.form.sections.AbstractSection#setSectionInternalValue(java.lang.Object)
	 */
	public void setSectionInternalValue(Object v) {
		// variables especiales no deben presentar valor almacenado. 
		// deben presentar el valor actual a menos que sea anulado por 
		// atributo saveSpecial
		if (!eValue) {
			super.setSectionInternalValue(v);
		}
	}

}
