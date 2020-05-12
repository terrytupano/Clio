/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */
package com.ae.form.sections;

import java.awt.*;

import com.ae.resource.*;
import com.ae.form.*;
import com.ae.core.*;

/** Procesa todas las peticiones de salida referidas a valores constantes de la 
 * aplicacion. Esta clase usa el valor del atributo <code>variable</code> para 
 * determinar el campo de base de datos que contiene el valor de la constante
 * que se desea procesar. El atributo <code>constantElement</code> identifica cual de los
 * elemento de la constante sera usado como salida. Los valores posibles para
 * este atributo son: 
 *  - <code>*key</code> usa la clave de la constante como salida
 *  - <code>*value</code> usa el valor de la constante como salida
 *  
 */
public class ConstantOutput extends AbstractSection {

	/** Nuevo ConstantOutput
	 */
	public ConstantOutput() {
		setSectionValue("");
	}
	/* (non-Javadoc)
	 * @see com.ae.form.AbstractSection#createInputRequestPanel()
	 */
	public InputRequestPanel createInputRequestPanel() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractSection#inputConclude(boolean)
	 */
	public void inputConclude(int c) {

	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractSection#inputRequest(java.awt.Point)
	 */
	public void inputRequest(Point p) {

	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractSection#recalc()
	 */
	public void recalc() {

	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		
		// constante
		String fld = getAttribute("variable");
		AppEntry ape 
			= AppConstants.getConstant((String) tpr.getFieldValue(fld));
		
		// elemento de la constante
		String ce = getAttribute("constantElement");
		if (ce.equals("*key")) {
			setSectionValue((String) ape.getKey()); 
		}
		if (ce.equals("*value")) {
			setSectionValue((String) ape.getValue()); 
		}
	}
}
