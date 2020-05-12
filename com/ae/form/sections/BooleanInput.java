
package com.ae.form.sections;

import java.awt.*;
import javax.swing.*;

import com.ae.resource.*;
import com.ae.form.*;

/** Intercepta el click del usuario sobre el componente dentro de la forma y le da
 * valor en forma similar a un <code>JRadioButton</code>. Varias secciones pueden
 * ser agrupadas dentro de un grupo (<code>group</code> en da definicion de la
 * seccion) si desea que se comporten de manera similar a un conjunto de botones
 * de seleccion. Si el atributo no existe, esta seccion se marca/o desmarca sin
 * ninguna evaluacion de grupo. si el area 
 * ocupada por esta seccion es menor 100 coloca el fondo gris para mejor visual
 * el atributo <code>init</code> debe contener una expresion logica 
 * para inicializar esta seccion.
 */
public class BooleanInput extends AbstractSection {
	
//	private boolean first;
	
	/** nueva instancia
	 * 
	 */
	public BooleanInput() {
		setSectionValue(new Boolean(false));
//		this.first = true;
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
		BooleanInput[] bi = form.getGroupedValue(getAttribute("group"));
		for(int v = 0; (bi != null && v < bi.length); v++) {
			if (bi[v] != this) {
				bi[v].modify(false);
			}
		}
		Boolean bo = (Boolean) getSectionValue();
		modify(!bo.booleanValue());
		form.inputConclude();

		// fondo gris?
		Dimension d = getSize();
		boolean b = ((Boolean) getSectionValue()).booleanValue();
		if ((d.width * d.height) < 100 && b) {
			getOutputComponent().setBackground(Color.GRAY);
			getOutputComponent().setOpaque(true);
		}
	}
	
	/** Modifica el valor interno para esta seccion. ojo: este metodo es 
	 * llamado desde otra instancia de <code>BooleanInput</code> para modificar
	 * el valor se la seccion.
	 * 
	 * @param nv - nuevo valor
	 */
	public void modify(boolean nv) {
		setSectionValue(new Boolean(nv));
		getOutputComponent().setOpaque(false);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#recalc()
	 */
	public void recalc() {
		/*
		// solo se ejecuta 1 vez
		if (first) {
			first = false;
			String ini = getAttribute("init");
			if (ini != null) {
				setSectionValue(new Boolean(inoutIf(ini)));
			}
		}
*/
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		getOutputComponent().setHorizontalAlignment(SwingConstants.CENTER);
		String ini = getAttribute("init");
		setSectionValue(new Boolean(inoutIf(ini)));
	}
}
