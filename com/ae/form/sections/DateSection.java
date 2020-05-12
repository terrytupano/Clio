/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import java.awt.*;

import javax.swing.*;

import com.ae.core.*;

/** Soporte base para las secciones de entrada/salida de fechas. 
 * Esta clase sobreescribe los metodos correspondientes de <code>AbstractSection</code>
 * para presentar la fecha en el formato correcto. El componente de salida
 * son tres <code>JLabel</code> dentro de un panel con <code>BorderLayout</code>
 * de esta forma, para que los tres elementos quede alineados (dd mm aa) las
 * dimensiones de la seccion deben calcularce desde el centro de hacia la
 * izquierda y luego usar la misma distancia hacia la derecha. 
 * 
 * El atributo <code>chrSep</code> indica, si esta presente, que esta seccion
 * debe mostrar un separador de fechas. para ello, el componente que se coloca
 * en el centro (generalmente el mes) ahora es una caja orizontal con "/ mm /"
 * 
 */
public abstract class DateSection extends AbstractSection {
	
	private JLabel[] outL;
	private String chrSep;
	
	/** Constructor
	 */
	public DateSection() {
		AppDate d = new AppDate();
		d.setTime(0);
		setSectionValue(d);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#createOutputComponent()
	 */
	public void createOutputComponent() {
		String g = getAttribute("eastGap");
		g = (g == null) ? "5" : g;
		int eg = Integer.parseInt(g);
		g = getAttribute("westGap");
		g = (g == null) ? "5" : g;
		int wg = Integer.parseInt(g);
		setLayout(new BorderLayout());
		add(Box.createHorizontalStrut(wg), BorderLayout.WEST);
		add(Box.createHorizontalStrut(eg), BorderLayout.EAST);
		
		this.outL = new JLabel[5];
		for (int cc = 0; cc < outL.length; cc++) {
			outL[cc] = new JLabel();
			outL[cc].setFont((Font) form.getAttribute("panel.sectionFont"));
			outL[cc].setHorizontalAlignment(SwingConstants.CENTER);
			outL[cc].setVerticalAlignment(SwingConstants.CENTER);

//			outL[cc].setOpaque(true);
//			outL[cc].setBackground(Color.LIGHT_GRAY);
			
		}
		JPanel jp = new JPanel(new GridLayout(1, 3, 0, 0));
		jp.setOpaque(false);
		jp.add(outL[0]);
		
		this.chrSep = getAttribute("chrSep");
		// caracter separador (outL[1] y 3)
		if (chrSep != null) {
			outL[1].setText(chrSep);
			outL[3].setText(chrSep);
			
			JPanel jpc = new JPanel(new BorderLayout());
			jpc.setOpaque(false);
			jpc.add(outL[1], BorderLayout.WEST);
			jpc.add(outL[2], BorderLayout.CENTER);
			jpc.add(outL[3], BorderLayout.EAST);
			jp.add(jpc);
		} else {
			jp.add(outL[2]);
		}
		jp.add(outL[4]);
		add(jp, BorderLayout.CENTER);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#formatOutput()
	 */
	public void formatOutput() {
		AppDate dat = (AppDate) getSectionValue();
		String ts = "        ";
		String cs ="";
//		System.out.println(getAttribute("id"));
		if (dat.getTime() != 0) {
			ts = dat.toString();
			cs = chrSep;
		}
		outL[0].setText(ts.substring(6));
		outL[1].setText(cs);
		outL[2].setText(ts.substring(4, 6));
		outL[3].setText(cs);
		outL[4].setText(ts.substring(2, 4));
		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#recalc()
	 */
	public void recalc() {
	}
}
