/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;
import javax.swing.*;

import com.ae.core.*;
import com.ae.resource.*;

/** accion estandar para suprimir registro. Esta accion desencadena una 
 * secuencia de comandos de supresion para los registro que esten 
 * relacionados con el comando <code>DeleteRecordCommand</code>.
 */
public class DeleteRecordAction extends AppAbstractAction {
	
	private AbstractTableDataInput aTable;
	
	/** nueva accion
	 * 
	 * @param at - a quien se notifica
	 */
	public DeleteRecordAction(AbstractTableDataInput at) {
		super("s01", "Delete", AppAbstractAction.RECORD_SCOPE, "tt05");
		this.aTable = at;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		Object[] options = {getString("s02"), getString("n02")};
		int o = JOptionPane.showOptionDialog(null, getString("m03"), getString("s03"), 
 			JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
 			null, options, options[1]);
		if (o == JOptionPane.YES_OPTION) {
			
			/*
			Record rcd = aTable.getRecord();
			PrevalentSystemManager.deleteRelatedRecord(rcd);
			PrevalentSystemManager.executeCommand(new DeleteRecordCommand(rcd));
			*/
			
			Record[] rcds = aTable.getRecords();
			for (int rc = 0; rc < rcds.length; rc++) {
				PrevalentSystemManager.deleteRelatedRecord(rcds[rc]);
				PrevalentSystemManager.executeCommand(new DeleteRecordCommand(rcds[rc]));
				
			}
		}
	}
}
