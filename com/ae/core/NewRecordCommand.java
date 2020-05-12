/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import com.ae.resource.*;

/** Comando que indica la insercion de un nuevo registro
 * 
 */
public class NewRecordCommand extends AbstractCommand {
	
	/** Contructor
	 * 
	 * @param rcd - nuevo registro
	 */
	public NewRecordCommand(Record rcd) {
		super(rcd); 
	}
}
