/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

public class AccountRecord extends Record {

	public AccountRecord() {	
		super(new Field[] {
			new Field("acRifTP", "r11", 10, ""),
			new Field("acCode", "c19", 20, ""),
			new Field("acText", "d02", 30, ""),
			new Field("acIApp", "i14", 10, ""),
			new Field("acIDir", "i15", 30, ""),
			new Field("acPreC", "p08", 30, ""),
		});
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.businesso.Record#showInList(int)
	 */
	public boolean showInList(int col) {
		return (col > 0);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.businesso.Record#getKey()
	 */
	public String getKey() {
		return (String) getFieldValue(0) + getFieldValue(1);	}
}