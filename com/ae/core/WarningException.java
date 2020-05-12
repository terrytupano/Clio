/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

/** Exception arrojada por la aplicacion cuando se desea que el usuario este consiente
 * de que esta ocurriendo algun error pero que puede continuar.
 */
public class WarningException extends AplicationException  implements Cloneable {
	
	/** Crea nueva excepcion
	 * 
	 * @param msg mensaje
	 */
	public WarningException(String msg) {
		super(msg, WARNING);
	}
}