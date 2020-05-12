/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;
 
import java.io.*;
import org.prevayler.*;

import com.ae.resource.*;

/** Funcionalida basica para comandos de manejo de datos dentro del sistema
 * prevalente
 * 
 */
public abstract class AbstractCommand implements Command {
	
	private Record dta;
	
	public AbstractCommand(Record dta) {
		this.dta = dta;
	}
	/*
	 *  (non-Javadoc)
	 * @see org.prevayler.Command#execute(org.prevayler.PrevalentSystem)
	 */
	public Serializable execute(PrevalentSystem pv) throws Exception{
		return executeCommand((AppTableModel) pv);
	}
	/** retorna el registro al cual hay que realizar la operacion
	 * 
	 * @return
	 */
	public Record getData() {
		return dta;
	}
	/** ejecuta este comando
	 * 
	 * @param atm
	 */
	public Serializable executeCommand(AppTableModel atm) {
		atm.executeCommand(this);
		return null;		
	}
}
