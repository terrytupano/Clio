/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form;

import java.util.*;
import javax.swing.*;

import com.ae.gui.*;
import com.ae.action.*;
import com.ae.core.*;
import com.ae.resource.*;

import org.jdom.*;

/** proporciona una interfaz para la gestion de forma almacenadas o en proceso de
 * ser completadas.
 */
public class FS_Table extends AbstractTableDataInput implements Keeper {
	
	private String selected_form; 

	/** Constructor sin argumentos
	 *
	 */
	public FS_Table() {
		super(null, new FormSavedRecord());
		setDocPanel(getInfoDoc());
		super.setToolBar(new JButton[] {
			new JButton(new AllocateTaxPayerAction(this)),
			new JButton(new EditFormAction(this)),
			new JButton(new DeleteRecordAction(this)),
		});
		
		// solo la forma seleccionada
		AppEntry ape = DIMain.leftComponentManager.getSelectedForm();
		this.selected_form = (String) ape.getKey(); 
		getTableModel().selectRecord("'{fsForma}' == '" + selected_form + "'");
	}

	/** metodo conveniente para retornar el nombre del documento (
	 * dentro de la carpeta de documentos) que sera usado como texto
	 * informativo de la forma. Este documento esta descrio en el 
	 * atributo <code>InfoDoc</code>
	 * 
	 * @return Nombre del documento
	 */
	private String getInfoDoc() {
		AppEntry ape = DIMain.leftComponentManager.getSelectedForm();
		Hashtable h = 
			PrevalentSystemManager.getFormElements((String) ape.getKey());
		Element e = (Element) h.get("form");
		return e.getAttributeValue("infoDoc"); 

	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.datainput.AbstractTableDataInput#getRightPanel(javax.swing.AbstractAction)
	 */
	public RightPanel getRightPanel(AbstractAction aa) {
		RightPanel rp = null;
		if (aa instanceof EditFormAction) {
			Record rcd = getRecord();
			TaxPayerRecord tpr = new TaxPayerRecord();
			tpr = (TaxPayerRecord) 
				PrevalentSystemManager.getRecordFrom(tpr, 
				(String) rcd.getFieldValue("fsRif"));
			rp = new FormEditor((String) 
				rcd.getFieldValue("fsForma"), tpr, getRecord());
		}
		return rp;
	}
}