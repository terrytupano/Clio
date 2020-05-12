/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import com.ae.core.*;

public class VoucherRecord extends Record {

	public VoucherRecord() {	
		super(new Field[] {
			new Field("avRifTP", "r12", 10, ""),
			new Field("avAccAC", "e15", 20, ""),
			new Field("avFecha", "f12", 10, new AppDate()),
			new Field("avConsec", null, 3, new Integer(0)),
			new Field("avMonto", "m23", 15, new Double(0.0)),
			new Field("avTexto", "d02", 30, "")
		});
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.businesso.Record#showInList(int)
	 */
	public boolean showInList(int col) {
		return !(col == 0 || col == 3);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.businesso.Record#getKey()
	 */
	public String getKey() {
		return makeKey(new int[] {0, 1, 2, 3});
	}
}