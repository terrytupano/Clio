/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import com.ae.core.*;
import com.ae.gui.wizard.*;
import com.ae.resource.*;

/** panel que permite la seleccion de cuentas contables leidas desde el tipo
 * de aplicacion seleccionada en panel 1 con datos almacenados en directorio
 * de panel 2. 
 * 
 * este panel se presenta si se selecciono la opcion manual en en panel 
 * anterior a este en la secuencia.
 * 
 */
public class ImportAccount_WP4 extends WizardPanel {

	private JTable table;
	private String app_name, app_dir;
	private WizardContainer wizardC;
		
	/** contructor si argumentos
	 *
	 */
	public ImportAccount_WP4() {
		super(WizardPanel.NORMAL);
		this.table = new JTable();
		this.app_name = "";
		this.app_dir = "";
		JScrollPane jsp = new JScrollPane(table);
		
		setText(DIMain.bundle.getString("m06"));
		setInputComponent(jsp);
		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#getData()
	 */
	public Object getData() {
		// se retorna un vector donde cada elemento es una instancia de 
		// AccountRecord.
		// con todos, menos la primera, de las variables establecidas
		// a los valores obtenidos de la seccion.
		Vector vec = new Vector();
		int[] rows = table.getSelectedRows();
		TableModel tm = table.getModel();
		for (int r = 0; r < rows.length; r++) {
			AccountRecord ar = new AccountRecord();
			ar.setFieldValue("acRifTP", "rif"); 
			ar.setFieldValue("acCode", (String) tm.getValueAt(rows[r], 0));
			ar.setFieldValue("acText", (String) tm.getValueAt(rows[r], 1));
			ar.setFieldValue("acIApp", app_name);
			ar.setFieldValue("acIDir", app_dir);
			vec.add(ar);
		}
		return vec;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#initializePanel(com.ae.gui.wizard.WizardContainer)
	 */
	public void initializePanel(WizardContainer arg0) {
		this.wizardC = arg0;
		setInit(WizardContainer.PREVIOUS); 
		updateTable();
	}
	
	private void setInit(int f) {
		Vector v = (Vector) wizardC.getPanel(f).getData();
		this.app_name = (String) v.elementAt(1); 
		this.app_dir = (String) v.elementAt(2); 
	}
	
	/** actualiza model de la tabla de cuentas
	 * 
	 * @param an - nombre del tipo de aplicacion
	 * @param dir - directorio de datos
	 */
	private void updateTable(){
		try {
			table.setModel(new DefaultTableModel());
			Vector[] rdcn = AccountReader.readAccounts(app_name, app_dir);
			table.setModel(new DefaultTableModel(rdcn[0], rdcn[1]));
			TableColumnModel cm = table.getColumnModel();
			TableColumn tc = cm.getColumn(1);
//			tc.setMinWidth(260);
			tc.setPreferredWidth(260);
		} catch (Exception e) {
			wizardC.showMessage("msg91");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#validateFields()
	 */
	public AplicationException validateFields() {
		return null;
	}
	
	/** metodo usado para aprovecar la la logica descrita en esta clase. 
	 * ejecuta los pasos necesarios para obtener todos los elementos leidos
	 * de la lista de cuantas de la aplicacion origen. despues de este metodo
	 * debe ejecutarse <code>getData()</code>
	 *  
	 * 
	 * @param wc - instancia del contenedor que contiene a este panel 
	 */
	public void selectAll(WizardContainer wc) {
		// temporal. error cuando es automatico debido a que el panel
		// anterior al que llama este metodo no es el de "tipo de proceso"
		try {
			initializePanel(wc);
		} catch (ClassCastException cce) { 
			setInit(3);
			updateTable();
		}
		table.selectAll();
	}
}
