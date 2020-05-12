/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */
 
package com.ae.core;

import com.ae.resource.*;

/** comando que indica que este registro ha sido suprimido. 
 * 
 */
public class DeleteRecordCommand extends AbstractCommand {
	
	/** nuevo comando
	 * 
	 * @param rcd - registro a suprimir
	 */
	public DeleteRecordCommand(Record rcd) {
		super(rcd); 
	}
}