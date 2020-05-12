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

/** Estimacion de las remuneraciones a percibir en el año grabable
 * 
 */ 
public class RemunerationAppraisal extends AbstractSection {
	private AmountEditor[] amountI;
	private InputRequestPanel inputRP;
	
	private String[] labelList;
	private Vector oldValue; 
	
	/** nueva estimacion de remuneracion
	 * 
	 */
	public RemunerationAppraisal() {
		setSectionValue(new Double(0.0));
		this.labelList = new String[] { DIMain.bundle.getString("s07"),
			DIMain.bundle.getString("u01"), DIMain.bundle.getString("v04"),
			DIMain.bundle.getString("p04"), DIMain.bundle.getString("o03"),
			DIMain.bundle.getString("b02"), DIMain.bundle.getString("o04")
		};
		this.amountI = new AmountEditor[labelList.length];
		this.oldValue = null;
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
			
			// utilidades y vacaciones
			if (l == 1 || l == 2 || l == 5) {
				amountI[l] = new AmountEditor(form, amountI[0]);
			} else {
				amountI[l] = new AmountEditor(form);
			}
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
			setSectionInternalValue(oldValue);
		}
		form.inputConclude();		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputRequest(java.awt.Point)
	 */
	public void inputRequest(Point p) {
		oldValue = (Vector) getSectionInternalValue();
		inputRP.inputRequest(this, p);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#recalc()
	 */	
	public void recalc() {
		double val = 0;
		for (int i = 0; i < amountI.length; i++) {
			val += amountI[i].getAmount();
		}
		setSectionValue(new Double(val));
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr){
		
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
		recalc();
	}
	
}