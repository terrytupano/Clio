/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package butil;

import javax.swing.*;

/** Protocolo de comunicaciones entre la ventana de utilidad y las acciones
 * desarrolladas para realizar acciones utilitarias para la aplicacion.
 * 
 */
public interface Actions {
	
	/** Este metodo es invocado cuado se necesita el componente visual diceñado
	 * por la accion para la interaccion con la accion. 
	 * 
	 * @return - componente para entrada
	 */
	public JComponent getComponent();

	/** invocado cuando es usuario nesecita que esta accion porsece los datos
	 * de entrada. 
	 *
	 */
	public void proceed();
	

}
