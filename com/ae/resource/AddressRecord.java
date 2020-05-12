
package com.ae.resource;

/** encapsula los datos necesarios para representar una direccion
 */
public class AddressRecord extends Record {

	/** Nueva direccion
	 */
	public AddressRecord() {	
		super(new Field[] {
			new Field("adEstado", "e12", 15, ""),
			new Field("adCiudad", "c20",  15, ""),
			new Field("adMunici", "m20", 15, ""),
			new Field("adParroq", "p06", 15, ""),
			
			// OJO: usado para validacion
			new Field("adUrbani", "u04", 15, ""),
			
			new Field("adCalle", "c21", 15, ""),
			new Field("adEdf", "e13", 15, ""),
			new Field("adCodGeo", "c22", 10, ""),
			new Field("adZonPos", "z01", 8, ""),
			new Field("adTlf1", "t10", 15, ""),
			new Field("adTlf2", "t10", 15, ""),
			new Field("adFax", "f10", 15, ""),
			new Field("adLocal", "l07", 15, "")
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
		return "Address";
	}
}