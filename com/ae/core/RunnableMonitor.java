/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/** Esta clase auxiliar es puede ser usada para monitorear las acciones 
 * del usuario sobre un dialogo de entrada que sea actualizado por un 
 * proceso separado. Si se cierra el dialogo la variable<code>stop</code> 
 * es acutalizada y debe se moditoreada por en Thread.run() para determinar 
 * finalizar la funcion 
 * 
 */
public class RunnableMonitor {
	
	public boolean stop;
	public JDialog dialog;
	
	/** nueva instancia
	 * 
	 * @param cmp - componente que usara esta clase para determinar 
	 * el la instancia de JDialog que lo contiene.
	 */
	public RunnableMonitor(Component cmp) {
		this.stop = false;
		if (cmp instanceof JDialog) {
			this.dialog = (JDialog) cmp; 
		} else {
			while(!(cmp instanceof JDialog)) {
				cmp = cmp.getParent();
			}
			this.dialog = (JDialog) cmp; 
		}
		dialog.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			public void windowClosing(WindowEvent e) {
				stop = true;
			}

		});
	}
}
