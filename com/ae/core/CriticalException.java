/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

/** Exception arrojada por la aplicacion cuado exite una condicion que no permite que
 * la aplicacion continue hasta que se corrija dicha condicion 
 */
public class CriticalException extends AplicationException {
	/** Crea nueva excepcion
	 * 
	 * @param msg mensaje
	 */
	public CriticalException(String msg) {
		super(msg, AplicationException.CRITICAL);
	}
}