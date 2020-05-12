/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import com.ae.resource.*;

/** Comando que indica que algunos datos de este registro han sido modificados
 * 
 */
public class EditRecordCommand extends AbstractCommand {
	
	/** nuevo comando
	 * 
	 * @param rcd - datos modificado
	 */
	public EditRecordCommand(Record rcd) {
		super(rcd); 
	}
}
