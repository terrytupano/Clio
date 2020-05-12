/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

public class Account_ClioObjectRecord extends Record {

	public Account_ClioObjectRecord() {	
		super(new Field[] {
			new Field("coRifTP", "r11", 10, ""),
			//id de forma
			new Field("coObjID", "f09", 10, ""),
			//id de secciones
			new Field("coObject", "o05", 10, ""),
			// entrada contable
			new Field("coAcCode", "c19", 20, ""),
			
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
		return makeKey(new int[] {0, 1, 2, 3});
	}
}