/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

/**
 * @author Arnaldo Fuentes
 * 
 * Exception arrojada por la aplicacion cuando quiere informar algo sin nesecidad
 * de que se tomen acciones.
 */
public class InformationException extends AplicationException {
	
	/** Crea nueva excepcion
	 * 
	 * @param msg mensaje
	 */
	public InformationException(String msg) {
		super(msg, AplicationException.INFORMATION);
	}
}