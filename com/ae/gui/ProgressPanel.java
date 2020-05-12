/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;

import javax.swing.*;

/** Conjunto de componentes que presentan informacion visual y escrita de
 * las operaciones que estan teniendo lugar en un <code>thread</code> aparte
 * es argumento de entrada esperado por el contrusctor se refiere a un 
 * combo de operaciones perviamente establecidas. 
 * 
 */
public class ProgressPanel extends JPanel {

	public static int FORM = 0;
	public static int ACCOUNT = 1;
	public static int EMAIL = 2;
	public static int VOUCHER = 3;
	
	private JLabel progressLabel;
	private JProgressBar progressBar;
	private String text;

	/** nueva barra de progreso. 
	 * 
	 * @param com - indica el combo que se desea presentar. 
	 */
	public ProgressPanel(int com) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.progressLabel = new JLabel(" ");
		this.progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setMinimumSize(new Dimension(400, 0));
		progressLabel.setLabelFor(progressBar);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		progressBar.setMaximum(0);

		JPanel jp = new JPanel(new BorderLayout());
		jp.add(progressLabel, BorderLayout.NORTH);
		jp.add(progressBar, BorderLayout.SOUTH);
		GUIUtilities.setEmptyBorder(jp);
		Dimension d = new Dimension(400, jp.getPreferredSize().height);
		jp.setMinimumSize(d);
		jp.setPreferredSize(d);
		jp.setMaximumSize(d);
		
		add(new JPanel(new BorderLayout()));
		add(jp);
		add(new JPanel(new BorderLayout()));		
	}
		
	/** incrementa la barra de progreso y presenta el argumento de entrada
	 * 
	 * @param act - txto (no id)
	 */
	public void increment(String ida) {
		progressLabel.setText(ida);
		progressBar.setValue(progressBar.getValue() + 1);		
	}	
	
	/** desplaza el maximo del la barra de progreso para que refleje 
	 * el porcentaje correcto de tarea terminada. 
	 * 
	 * @param l - longitud a adicionar al maximo
	 */
	public void offsetMaximum(int l) {
		progressBar.setMaximum(progressBar.getMaximum() + l);
	}
}
