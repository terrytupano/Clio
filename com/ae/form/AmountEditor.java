
package com.ae.form;

import java.awt.*;
import javax.swing.*;

import com.ae.core.*;


/** Proporciona interfaz estandar para la estandas de cantidades o montos dentro
 * de una forma. Cuando aqui se refiera a frecuencia, indica la cantidad de dias.
 * Este editor de cantidades puede tener como origen de datos, otro editor.
 * si esto es cierto, este editor funciona para expresar el nro de veces por el
 * que se debe expresar el monto colacado en el editor origen. Ej:
 * 
 * editor origen: 100 mensual
 * este editor:     1 dia
 * 
 * cuando la seccion invoque alguno de los metodos <code>getAmount() o getAmount(int)</code>
 * "este editor" extraera una frecuencia del monto en "editor origen" y retornara
 * esta como resultado
 *  
 */
public class AmountEditor extends JPanel {
	private JComboBox jcbox_freq;
	
	private JFormattedTextField jftf_cant;
	private AmountEditor srcCmp;
	private int timeScope;
	
	/** nuevo editor de cantidades 
	 * 
	 * @param f - forma
	 */
	public AmountEditor(Form f) {
		this.srcCmp = null;
		createAmountEditor(f, true);
	}
	/** nuevo editor de cantidades. Este contruscor crea una nueva instancia de
	 * esta clase pero aqui se expresaran unidades y frecuencias de  
	 * otro editor de montos.
	 * 
	 * @param f - Forma
	 * @param src - Editor origen de montos
	 */
	public AmountEditor(Form f, AmountEditor src) {
		this.srcCmp = src; ;
		createAmountEditor(f, false);
	}
	/* crea los componentes que componen este editor de montos. 
	 * 
	 * @param f - Forma
	 * @param af - si =true, el <code>JComboBox</code> de las fecuencias estara
	 * todas las frecuencias disponibles; de lo contrario, "anual" no estara dispobible 
	 */
	private void createAmountEditor(Form f, boolean af) {
		this.jftf_cant = FormUtilities.getJFormattedTextField(f, new Double(0.0));

		if (af) {
			this.jcbox_freq = FormUtilities.getJComboBox(f, 
				AppConstants.getConstantsOfType("mountFrequency"));
		} else {
			this.jcbox_freq = FormUtilities.getJComboBox(f, 
				AppConstants.getConstantsOfType("unitFrequency"));
		}
		
		// ancho fijo
		Dimension d = jcbox_freq.getPreferredSize();
		d.width = 80;
		jcbox_freq.setMinimumSize(d);
		jcbox_freq.setPreferredSize(d);
		jcbox_freq.setMaximumSize(d);
		
		jcbox_freq.setSelectedIndex(1);
		setBackground((Color) f.getAttribute("panel.background"));
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		add(jftf_cant);
		add(Box.createHorizontalStrut(FormUtilities.HORIZONTAL_GAP));
		add(jcbox_freq);
		setMaximumSize(getPreferredSize());
		this.timeScope = Integer.parseInt((String) f.getAttribute("gv.timeScope"));		
	}
	/** Retorna el monto contenido en este editor en la frecuencia
	 * expresada en la variable global <code>gv.timeScope</code>
	 * 
	 * @return monto
	 */
	public double getAmount() {
		return getAmount(timeScope);
	}
	/** Retorna el monto contenido en este editor en la frecuencia 
	 * pasada como argumento.  
	 * 
	 * @param freq - frequencia
	 * @return valor 
	 */
	public double getAmount(int freq) {
		
		// frecuencia
		AppEntry ae = (AppEntry) jcbox_freq.getSelectedItem();
		int div = Integer.parseInt((String) ae.getKey());
		Number n = (Number) jftf_cant.getValue();
		
		// monto o cantidad
		double dval = 0.0;
		dval = n.doubleValue();

		if (srcCmp != null) {
			// obtengo el monto en mi misma frecuencia y lo multiplico por
			// la cantidad
			double dvalsrc = srcCmp.getAmount(div);
			dval = dvalsrc * dval;
		} else {
			// convierto a una frecuencia y calculo la solicitada
			dval = dval / div;
			dval = dval * freq;			
		}
		
		return dval;		
	}
	/* retorna la frecuencia el la que esta expresada el monto o unidad 
	 * @return frecuencia
	 */
	private int getFrequency() {
		AppEntry ae = (AppEntry) jcbox_freq.getSelectedItem();
		return Integer.parseInt((String) ae.getKey());
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.Component#requestFocus()
	 */
	public void requestFocus() {
		jftf_cant.requestFocus();
	}
	
	/** retorna el valor interno de este componente.
	 * 
	 * @return <code>AppEntry</code> con valor para este componente
	 */
	public AppEntry getInternalValue() {
		Number n = (Number) jftf_cant.getValue();
		Integer k = new Integer(jcbox_freq.getSelectedIndex());
		Double v = new Double(n.doubleValue());
		return new AppEntry(k, v);
	}
	
	/** establece el valor interno para este componente.
	 * 
	 * @return <code>AppEntry</code> con valor para este componente
	 */
	public void setInternalValue(AppEntry ae) {
		Integer k = (Integer) ae.getKey();
		jcbox_freq.setSelectedIndex(k.intValue());
		jftf_cant.setValue((Double) ae.getValue());
	}
}
