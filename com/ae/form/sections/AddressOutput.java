/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */
package com.ae.form.sections;

import java.awt.*;

import com.ae.form.*;
import com.ae.core.*;
import com.ae.resource.*;

/** Localiza la direccion cuyo identificador fue colocado como valor en el atributo
 * <code>variable</code>. Esta clase espera el atributo <code>addrField</code> el cual contiene
 * el campo dentro de <code>AddressRecord</code> que se desea como salida. Si el valor de 
 * <code>addrField = "*complete"</code> se retorna la direccion con formato estandar.   
 */
public class AddressOutput extends AbstractSection {

	public AddressOutput() {
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

		// campo
		String fld = getAttribute("variable");
		AddressRecord arcd = (AddressRecord) tpr.getFieldValue(fld);
		String adr = getAttribute("addrField");
		
		// direccion completa?
		String address = "";
		if (adr.equals("*complete")) {
			address = CoreUtilities.formatAddress(arcd);
		} else {
			address = (String) arcd.getFieldValue(adr);
		}
		setSectionValue(address);
	}
}
