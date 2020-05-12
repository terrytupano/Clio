/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import com.ae.resource.*;


/** Interface que permite la comunicacion entre <code>TaxPayerSelector</code>
 * y un componente interezado en el evento
 * 
 */
public interface TaxPayerListener {
	
	/** invocado por el <code>TaxPayerSelector</code> para indicar que un nuvo
	 * contribuyente ha sido seleccionado. 
	 * 
	 * @param rcd - nueva seleccion
	 */
	public void itemSelected(Record rcd);

}
