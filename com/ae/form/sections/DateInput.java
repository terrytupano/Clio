
package com.ae.form.sections;

import java.awt.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.form.*;
import com.ae.resource.*;

/** Presenta un unico campo de entrada donde se permite la entrada de fechas.
 * el atributo <code>init</code> indica una expresion que sera evaluada para determinar
 * la fecha inicial a colocar como valor de salida y dentro del componente de
 * entrada. para este tipo de seccion se recomienda usar la funcion DoubleDate()
 * 
 */
public class DateInput extends DateSection {
	
	private JFormattedTextField i_jftf;
	private InputRequestPanel inputRP;
	private Object oldVal;
	private AppDate cerod;
	
	/** Constructor
	 * 
	 */
	public DateInput() {
		super();
		this.oldVal = null;
		this.cerod = new AppDate();
		cerod.setTime(0);
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#createInputRequestPanel()
	 */
	public InputRequestPanel createInputRequestPanel() {
		this.inputRP = new InputRequestPanel(form);
		JPanel jp = FormUtilities.getJPanel(form, true);
		jp.setLayout(new GridLayout(2, 1));
		
		// intenta localizar el atributo label, si no existe coloca el titulo
		String lab = getAttribute("label");
		lab = (lab == null) ? getAttribute("title") : lab;
		jp.add(FormUtilities.getJLabel(form, lab));
		
		jp.add(i_jftf);
		inputRP.add(jp, i_jftf);
		return inputRP;
	}
	
	/** retorna instancia <code>AppDate</code> correspondiente a <code>Date</code>
	 * retornado por componente de entrada 
	 * 
	 * @return AppDate
	 */
	private AppDate getAppDate() {
		Object obj = i_jftf.getValue();
		AppDate apd = new AppDate();
		if (obj instanceof Date) {
			apd.setTime(((Date) obj).getTime());
		}
		return apd;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#getSectionInternalValue()
	 */
	public Object getSectionInternalValue() {
		return getAppDate();
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputConclude(boolean)
	 */
	public void inputConclude(int c) {
		if (c == InputRequestPanel.ACEPT) {
			setSectionValue(getAppDate());
		}
		if (c == InputRequestPanel.CANCEL) {
			setSectionInternalValue(oldVal);
		}
		if (c == InputRequestPanel.CLEAR_DATE) {
			setSectionInternalValue(cerod);
		}
		form.inputConclude();
	}
	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputRequest(java.awt.Point)
	 */
	public void inputRequest(Point p) {
		oldVal = getSectionInternalValue();
		if (((AppDate) getSectionValue()).getTime() == 0) {
			i_jftf.setValue(new AppDate());
		}
		inputRP.inputRequest(this, p);
	}
	

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		this.i_jftf = FormUtilities.getJFormattedTextField(form, cerod);
		String ini = getAttribute("init");
		if (ini != null) {
			try {
				form.evaluator.parse(ini);
				Number d = new Double(form.evaluator.evaluate(ini));
				AppDate apd = new AppDate(String.valueOf(d.intValue()));
				setSectionValue(apd);
				i_jftf.setValue(apd);
			} catch (Exception e) {
				String sid = e.getMessage() + "(" + ini + ") (" + getAttribute("id") + ")";
				Logger.getLogger("").logp(Level.WARNING, null, null, sid, e);
			}
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#setSectionInternalValue(java.util.Vector)
	 */	
	public void setSectionInternalValue(Object c) {
		i_jftf.setValue(c);
		setSectionValue(c);
	}
}
