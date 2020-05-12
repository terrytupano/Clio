
package com.ae.form.sections;

import java.awt.*;
import javax.swing.*;

import com.ae.resource.*;
import com.ae.form.*;

/** Esta clase especificamente sobreescribe el metodo <code>getSectionValue()</code>
 * para convertir la salida de la seccion en una salida tipo booleana. En otras palabras, 
 * durante la inicializacion, evalua <code>outputIf</code> y establece 
 * <code>true</code> o <code>false</code> para  esta seccion. si el area 
 * ocupada por esta seccion es menor 100 coloca el fondo opaco para mejor visual 
 *  
 */
public class BooleanOutput extends AbstractSection {
	
	private Color backC;
	
	/** nueva salida tipo boolean
	 *
	 */
	public BooleanOutput() {
		setSectionValue(new Boolean(false));
		this.backC = null;
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
		Boolean val = new Boolean(inoutIf(getAttribute("outputIf")));
		setSectionValue(val);
		// fondo opaco
		Dimension d = getSize();
		if ((d.width * d.height) < 100 && val.booleanValue()) {
			getOutputComponent().setBackground(backC);
			getOutputComponent().setOpaque(true);
		}
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		getOutputComponent().setHorizontalAlignment(SwingConstants.CENTER);
		this.backC = (Color) form.getAttribute("panel.foreground");
	}
}
