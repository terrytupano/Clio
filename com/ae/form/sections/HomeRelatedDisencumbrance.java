/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import com.ae.core.*;
import com.ae.resource.*;
import com.ae.form.*;

/** Desgrabamenes  por intereses en la adquisicion de vivienda principal o por
 * el alquiler pagado por la vivienda sirve de residencia permanente. Esta clase
 * utilza el atributo <code>variable</code> para determinar cual seccion contiene el valor de
 * la unidad tributaria
 * 
 */ 
public class HomeRelatedDisencumbrance extends AbstractSection {
	private AmountEditor[] amountI;
	private int h_loan, h_rent;
	private InputRequestPanel inputRP;
	
	private String[] labelList;
	private JPanel panel;
	private String tu_section;
	private Vector oldVal;
	
	/** nueva estimacion de remuneracion
	 * 
	 */
	public HomeRelatedDisencumbrance() {
		setSectionValue(new Double(0.0));
		this.labelList = new String[] {DIMain.bundle.getString("a23"), 
			DIMain.bundle.getString("i11")
		};
		this.amountI = new AmountEditor[labelList.length];
		this.oldVal = null;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInputRequestPanel()
	 */
	public InputRequestPanel createInputRequestPanel() {
		this.inputRP = new InputRequestPanel(form);
		JPanel panel = FormUtilities.getJPanel(form, true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		for (int l = 0; l < labelList.length; l++) {
			amountI[l] = new AmountEditor(form);
			panel.add(FormUtilities.getCoupleInBox(form, 
				labelList[l], amountI[l], true));
			panel.add(Box.createVerticalStrut(FormUtilities.VERTICAL_GAP));
		}
		inputRP.add(panel, amountI[0]);
		return inputRP;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#getSectionInternalValue()
	 */
	public Object getSectionInternalValue() {
		Vector v = new Vector();
		for (int a = 0; a < amountI.length; a++) {
			v.add(amountI[a].getInternalValue());
		}
		return v;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputConclude(boolean)
	 */
	public void inputConclude(int a) {
		if (a == InputRequestPanel.ACEPT) {
			recalc();
		} else {
			setSectionInternalValue(oldVal);
		}
		form.inputConclude();		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputRequest(java.awt.Point)
	 */
	public void inputRequest(Point p) {
		oldVal = (Vector) getSectionInternalValue();
		inputRP.inputRequest(this, p);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#recalc()
	 */	
	public void recalc() {
		
		double val = 0;
		
		//unidades tributarias
		double tuval = 
			((Double) form.getVariableValue(tu_section)).doubleValue();
		if (tuval != 0 ) {
			//datos de entrada
			double rent = amountI[0].getAmount();
			double loan = amountI[1].getAmount();
				
			//calculo:
			// alquiler / valor de u.t > a u.t maximas de alquiler
			// =true coloco maximo u.t * valor de u.t
			// = false coloco alquiler
			rent = ((rent / tuval) > h_rent) ? h_rent * tuval : rent;
			
			// calculo:
			// intereses / valor de u.t > a u.t. maximas de intereses?
			// =true coloco maximo u.t * valor de u.t
			// =false coloco intereses
			loan = ((loan / tuval) > h_loan) ? h_loan * tuval : loan;
			
			//como el valor puede ser uno, no ambos coloco que que tenga datos
			val = (rent > 0) ? rent : loan; 
		}			  
		setSectionValue(new Double(val));
	
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr){
		this.tu_section = getAttribute("variable");
		AppEntry ae= AppConstants.getConstant("tuForHomeRent");
		String aes = ((String) ae.getValue());
		this.h_rent = Integer.parseInt(aes);
		ae =  AppConstants.getConstant("tuForHomeLoan");
		aes = (String) ae.getValue();
		this.h_loan = Integer.parseInt(aes); 
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#setSectionInternalValue(java.util.Vector)
	 */
	public void setSectionInternalValue(Object v) {
		Vector v1 = (Vector) v;
		for (int a = 0; a < amountI.length; a++) {
			amountI[a].setInternalValue((AppEntry) v1.elementAt(a));
		}
	}
}