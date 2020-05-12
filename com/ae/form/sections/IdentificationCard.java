/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import javax.swing.*;

import com.ae.resource.*;

/** Presenta el documento de identidad (cedula). El valor del atributo <code>variable</code>
 * indica el documento de identidad que esta clase presentara como valor de salida.
 * Adicionalmente, el atributo <code>nationalityField</code> contiene el nombre 
 * del campo que indica la nacionalidad que sera tomada en cuenta por esta clase
 * para el formato de salida del documento de identidad
 *  
 * 
 */
public class IdentificationCard extends SpacedString {
	
	/** constructor
	 *
	 */
	public IdentificationCard() {
		setSectionValue(new Integer(0));
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		String fld = getAttribute("variable");
		String nac = getAttribute("nationalityField");
		setSectionValue((Integer) tpr.getFieldValue(fld));
		setColumns(8);
		String nation = (String) tpr.getFieldValue(nac);
		setVerticalAlignment(SwingConstants.BOTTOM);
		if (nation.equals("ve")) {
			setVerticalAlignment(SwingConstants.TOP);
		}
	}
}
