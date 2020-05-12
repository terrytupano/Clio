/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import com.ae.core.*;
import com.ae.resource.*;


/** Da valor al atributo <code>variable</code> y lo distriye entre el numero de columnas. 
 * El Nro de columnas es calculado:
 *  - explicitamente expresando el atributo <code>columns</code> 
 *  - si el atributo no esta presenta, de la longitud del campo dentro de
 * base de datos.
 *  - 10
 * el atributo <code>variable</code> puede referirse tanto a una seccion como a un
 * campo de db 
 * el atributo <code>mask</code> puede estar presente. La longitud de 
 * la nueva secuencia se modifica. 
 * 
 * nota: error cuando caracter de sustitucion es un espacio en blanco.
 * 
 */
public class SpacedOutput extends SpacedString {
	
	/** constructor
	 *
	 */
	public SpacedOutput() {
		setSectionValue("");
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		int col = 0;
		String fldn = getAttribute("variable");
		String svalue = form.getVariableValue(fldn).toString();
		
		// columns
		String cols = getAttribute("columns");
		if (cols != null) {
			col = Integer.parseInt(cols);
		}
		
		// longitud de campo de db
		if (col == 0) {
			col = tpr.getFieldLength(fldn);
		} 

		// 10		
		if (col == 0) {
			col = 10;
		}
		
		// mask
		String ms =	getAttribute("mask");
		if (ms != null) {
			svalue = CoreUtilities.mask(ms, svalue);
			col = svalue.length();
		}
 		
		setColumns(col);
		setSectionValue(svalue);
	}
}
