/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import com.ae.core.*;

/** registro ficticio que sirve como formato de salida para presentar en forma
 * de tabla, el contenido del cifrado.  
 * 
 */
public class StockRecord extends Record {

	public StockRecord() {	
		super(new Field[] {
			new Field("sForm", null, 10, ""),
			new Field("sAppId", null, 4, ""),
			new Field("sFecha", null, 10, new AppDate(DIMain.bundle.getString("mea"))),
			new Field("sCantida", null, 2, new Integer(0))					
		});
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.businesso.Record#showInList(int)
	 */
	public boolean showInList(int col) {
		return false;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.businesso.Record#getKey()
	 */
	public String getKey() {
		return (String) getFieldValue(0);
	}
}