package com.ae.resource;

import java.util.*;
import com.ae.core.*;

/** encapsula la informacion relacionada con la edicion de una forma para un 
 * contribuyente en un momento determinado. Este registro basicamente representa
 * una planilla fiscal asignada al contribuyente durante el proceso de completar
 * la informacion contenida dentro de ella, Es decir, durante el llenado.
 * 
 */
public class FormSavedRecord extends Record {

	public FormSavedRecord() {
		super(new Field[] {
			new Field("fsForma", "f09", 10, ""),
			new Field("fsRif", "r12", 10, ""),
			new Field("fsNombre", "n01", 30, ""),
			new Field("fsFecha", "a27", 10, new AppDate()),
			new Field("fsForSec", null, 10, new Hashtable()),
			new Field("fsStatus", "a28", 10, new Boolean(true))
		});	
		
	}

	/* (non-Javadoc)
	 * @see com.ae.core.Record#getKey()
	 */
	public String getKey() {
		return makeKey(new int[] {0, 1, 3});
	}

	/* (non-Javadoc)
	 * @see com.ae.core.Record#showInList(int)
	 */
	public boolean showInList(int col) {
		return !(col == 0 || col == 4);
	}

}
