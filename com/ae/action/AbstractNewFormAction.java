/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action; 

import java.util.*;

import javax.swing.*;

import com.ae.resource.*;
import com.ae.core.*;
import com.ae.form.*;

/** Proporciona soporte para sub-clases. 
 * Esta accion reviza el estado de cuenta y verifica que existan 
 * los creditos (la forma adecuada) para asignar la planilla fiscal al contribuyente
 * seleccionado 
 * 
 */
public abstract class AbstractNewFormAction extends AppAbstractAction {
	
	private TaxPayerRecord taxPayer;
	
	/** nueva Accion. 
	 * 
	 * @param lab - texto de la accion
	 * @param ico - icono
	 * @param sco - alcance
	 */
	public AbstractNewFormAction(String lab, String ico, int sco, String ttid) {
		super(lab, ico, sco, ttid);
		this.taxPayer = null;
	}

	/** Verifica el inventario de formas retornando Si no hay formas
	 * disponibles, present un mensaje indicandolo. Este metodo modifica
	 * los campos del registro para que la forma sea descontada por el 
	 * editor de formas despues que se a completado la asociacion
	 * 
	 * @param fn identificador de forma
	 * @return =true si hay rial, =false si no
	 */	
	private boolean checkAccount(String fn) {
		StockRecord sr = (StockRecord) PrevalentSystemManager.getRecordFrom(
		new StockRecord(), fn);
		// si no esta dentro del archivo, el usuario no a adquirido formas
		boolean flag = (sr != null);
		if (!flag) {
			Object[] options = {"Ok"};
			int o = JOptionPane.showOptionDialog(null, getString("m01"), 
				getString("e01"), JOptionPane.OK_OPTION , JOptionPane.ERROR_MESSAGE,
			null, options, options[0]);
		}
		return flag;
	}
	
	/** Verifica si las condiciones entre las forma y el contribuyente 
	 * estan dadas para que esta puedan asociarse (asignar forma). para que
	 * pueda darse la asociacion debe complirse que:
	 * - no exista una asociacion anterior de menos de un dia
	 * - que la paremetrizacion del contribuyente coincidan con los requisitos
	 * minimos solicitados por la forma
	 * 
	 * @param fdta - <code>AppEntry</code> con id de forma
	 * @param tpr - contribuyente
	 * @return <code>true</code> si es factible la asignacion
	 * 
	 */
	public boolean checkConditions(AppEntry fdta, TaxPayerRecord tpr) {
		// existe una forma asignada hoy a este contribuyente?
		FormSavedRecord fsr = new FormSavedRecord();
		fsr.setFieldValue("fsForma", (String) fdta.getKey());
		fsr.setFieldValue("fsRif", (String) tpr.getKey());
		Record rcd = PrevalentSystemManager.getRecordFrom(fsr, fsr.getKey());

		// es valida la asociacion
		return (rcd == null) && CoreUtilities.isSuitable(fdta, tpr);
	}

	/** Presenta dialogo que permite al usuario obtener un resumen de la operacion
	 * que esta por comenzar. Si el usuario acepta los cambios, se decuenta
	 * una forma del inventario y se da inicio a la edicion de la forma. 
	 * 
	 * @param fdta - par id, nombre de forma
	 * @param tpr - contribuyente
	 */	
	public void confirm(AppEntry fdta, TaxPayerRecord tpr) {
		String fna = (String) fdta.getValue();
		String fke = (String) fdta.getKey();
		boolean flag = checkAccount(fke);
		if (!flag) {
			return; 
		}

		Hashtable da = new Hashtable();
		da.put("<v1>", (String) tpr.getFieldValue("tpNombre"));
		da.put("<v2>",fna);
		String msg = CoreUtilities.replaceHTMLVariables1(getString("m02"), da);
		String[] jb = new String[] {getString("a05"), getString("c05")};
		String tit = (this instanceof AllocateFormAction) ? 
			getString("a06") : getString("a07");

		int result = JOptionPane.showOptionDialog(
			null, msg, tit, JOptionPane.DEFAULT_OPTION,
			JOptionPane.WARNING_MESSAGE, null, jb,
			jb[1]);
			
		if (result == 0) {
			FormEditor fedt = new FormEditor((String) fdta.getKey(), tpr, null); 
			JDialog dlg = getDialogForEditor(fedt, null, "a08");
			fedt.init();
			dlg.show();
		}
	}
}

