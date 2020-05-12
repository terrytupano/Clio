/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

/** Exception arrojada por la aplicacion cuando se requiere alguna accion por parte
 * del usuario.
 */
public class ActionException extends AplicationException {
	/** Crea nueva excepcion
	 * 
	 * @param msg - mensaje
	 */
	public ActionException(String msg) {
		super(msg, AplicationException.ACTION);
	}
}