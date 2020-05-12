/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.evaluate.functions.string;


import java.util.*;

import com.ae.evaluate.*;
import com.ae.resource.*;
import com.ae.core.*;

/** localiza y retorna el valor de alguna seccion dentro de una forma para 
 * un contribuyente especificado. Los valores para esta funcion son:
 * - rif del contribuyente
 * - id de forma
 * - id de seccion
 * - id seccion con numero.
 * - numero o fecha de declaracion. Si este valor es 0 se localiza la ultima 
 * declaracion inactiva, existente de la forma solicitada. de lo contrario, 
 * se localiza la delaracion que el contenga este numero (o fecha) en la seccion 
 * identificada en el 4to parametro. 
 *
 * para todos los casos, la forma debe poseer estado inactivo. la decision del colocar
 * el numero o la fecha de la declaracios sustituida depende de la forma. en 
 * lineas generales, es mejor colocar la fecha porque esta siempre esta 
 * presente mientras que el numero de la planilla no. 
 *  
 */
public class FromSection implements Function {

	private FormSavedRecord model;
	private Vector rcdList;
	/** nueva instancia 
	 * 
	 */
	public FromSection() {
		this.model = new FormSavedRecord();
		this.rcdList = null;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#getName()
	 */
	public String getName() {
		return "fromSection";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.evaluate.Function#execute(com.ae.evaluate.Evaluator, java.lang.String)
	 */
	public String execute(Evaluator evaluator, String s) throws FunctionException {
		String arg[] = s.split(",");
		String fid = arg[1].trim();
		String rif = arg[0].trim();
		String sna = arg[2].trim();
		String snum = (arg[3].replaceAll("'", "")).trim();
		String num = arg[4].trim();
		String secval = "0";
		AppTableModel t = PrevalentSystemManager.getTableModel(model, false);
		Vector rcdl = t.getRecords(true);
		Object oval = null;
		for (int r = 0; r < rcdl.size(); r++) {
			model = (FormSavedRecord) rcdl.elementAt(r);
			if (!(model.getFieldValue("fsForma").equals(fid) &&
				model.getFieldValue("fsRif").equals(rif) &&
				((Boolean) model.getFieldValue("fsStatus")).booleanValue() == false)) {
				continue;
			}
			// de una declaracion en especifico
			if (!num.equals("0")) {
				Object o = ((Hashtable) model.getFieldValue("fsForSec")).get(snum);
				if (o != null && o.toString().equals(num)) {
					secval = ((Hashtable) model.getFieldValue("fsForSec")).get(sna).toString();
				}
			// de la ultima. no interrumpe el bucle porque 
			// el orden es de menor fecha a mayor.
			} else {
				secval = ((Hashtable) model.getFieldValue("fsForSec")).get(sna).toString();
			}
		}
		return secval;
	}
}