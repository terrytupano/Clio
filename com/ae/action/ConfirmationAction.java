/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import javax.swing.*;

/** Interface que debe ser implementados por acciones que solicitan un respuesta.
 * es decir, una accion que necesita que otra le notifique cuando se ha disparado
 * el metodo <code>ActionPermormed(ActionEvent)</code>
 */
public interface ConfirmationAction {
	
	/** Notificacion emitida por acciones. Este metodo generalmete es invocado 
	 * por acciones como <code>AceptAction</code> cuando el usuario disparo en 
	 * ella <code>actionPerforme(ActionEvent)</code>
	 * 
	 * @param aa Accion
	 */
	public void actionPerformed2(AbstractAction aa);

}
