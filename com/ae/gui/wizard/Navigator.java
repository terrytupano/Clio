/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.wizard;

/** interface que deben implementar las clases que desean usar las acciones
 * <code>PreviousAction y NextAction</code>
 * 
 */
public interface Navigator {
	
	/** anterior
	 *
	 */
	public void previous();

	/** siguiente
	 *
	 */	
	public void next();

	/** retorna <code>true</code> si existen mas elementos hacia la derecha
	 * (abajo) de la lista
	 * 
	 * @return <code>true o false</code>
	 */	
	public boolean hasNext();
	
	/** retorna <code>true</code> no hay mas elementos hacia la izquierda
	 * (arriba) dentro de la lista 
	 * 
	 * @return <code>true o false</code>
	 */
	public boolean isFirst();

}
