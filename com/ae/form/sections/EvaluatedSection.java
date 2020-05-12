/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import java.awt.*;

import com.ae.form.*;
import com.ae.resource.*;

/** Clase estandar para el calculo de expreciones matematicas descritas en 
 * el atributo "expression". 
 * 
 */ 
public class EvaluatedSection extends AbstractSection {
	
	/** crea nueva instancia
	 * 
	 */
	public EvaluatedSection() {
		setSectionValue(new Double(0.0));
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#createInputRequestPanel()
	 */
	public InputRequestPanel createInputRequestPanel() { 
		return null; 
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputConclude(boolean)
	 */
	public void inputConclude(int b) {}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputRequest(java.awt.Point)
	 */
	public void inputRequest(Point p) {}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#isAnInputSection()
	 */
	public boolean isAnInputSection() { return false;}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#recalc()
	 */
	public void recalc() {
		String exp = getAttribute("expression");
		try {
			form.evaluator.parse(exp);
			Double dval = new Double(form.evaluator.evaluate(exp));
			if (dval.toString().equals("NaN")) {
				dval = new Double(0.0);
			}
			setSectionValue(dval);
		} catch (Exception e) {
			log(exp, e);
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		
	}
	
}