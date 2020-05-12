/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import java.awt.*;

import javax.swing.*;

import com.ae.form.*;

/** obtiene el valor de la secion y lo presentan distribuyendolo a todo lo 
 * ancho de la seccion. Sub-clases solo deben implementar los metodos 
 * <code>setInitialValues</code> 
 * 
 * Nota: Tomar en cuenta que el ancho de la secion entre el nro de columnas 
 * usadas para la distribucion debe se un nro entero.  
 */ 
public abstract class SpacedString extends AbstractSection {
	
	private int columns;
	private JLabel[] outlabels;

	/** nueva instancia
	 * 
	 * @param col Nro de columnas a distribuir
	 */	
	public SpacedString() {
		this.columns = 1;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#createOutputComponent()
	 */
	public void createOutputComponent() {
		setLayout(new BorderLayout());
		this.outlabels = new JLabel[columns];
		JPanel jp = new JPanel(new GridLayout(1, columns));
		jp.setOpaque(false);
		for (int cc = 0; cc < columns; cc++) {
			outlabels[cc] = new JLabel();
			outlabels[cc].setFont((Font) form.getAttribute("panel.sectionFont"));
			outlabels[cc].setHorizontalAlignment(SwingConstants.CENTER);
			jp.add(outlabels[cc]);
		}
		add(jp, BorderLayout.CENTER);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#formatOutput()
	 */
	public void formatOutput() {
		String sv = FormUtilities.stringFormat(getSectionValue(), true);
		// si es valor numerico y == 0 presentar blanco
		if (getSectionValue() instanceof Number && 
			((Number) getSectionValue()).doubleValue() == 0) {
			sv = "";
		}
		String stv1 = "              " + sv; 
		String stv = stv1.substring(stv1.length() - columns);
		for (int cc = 0; cc < columns; cc++) {
			outlabels[cc].setText(stv.substring(cc, cc + 1));
		}
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
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#recalc()
	 */
	public void recalc() {
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#createInputRequestPanel()
	 */
	public InputRequestPanel createInputRequestPanel() {
		return null;
	}
	
	/** Establece alineacion vertical para los componentes de salida. Esta puede
	 * se SwingUtilities.TOP, SwingUtilities.CENTER o SwingUtilities.BOTTOM 
	 * 
	 * @param ver Alineacion vertical
	 */
	public void setVerticalAlignment(int ver) {
		for (int cc = 0; cc < outlabels.length; cc++) {
			outlabels[cc].setVerticalAlignment(ver);
		}
	}
	/** Establece el numero de columnas dentro de las cuales se distribuira el
	 * contenido del valor de esta seccion.
	 * 
	 * @param c - Nro de comlumnas
	 */
	public void setColumns(int c) {
		this.columns = c;
		createOutputComponent();
	}
}
