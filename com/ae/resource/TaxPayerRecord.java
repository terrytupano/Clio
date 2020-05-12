/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import com.ae.core.*;

public class TaxPayerRecord extends Record {

	public TaxPayerRecord() {	
		super(new Field[] {
			new Field("tpRif", "r12", 10, ""),
			new Field("tpNit", "n06", 10, ""),
			new Field("tpNombre", "r13", 30, ""),
			new Field("tpMesCie", "m21", 10, ""),
			new Field("tpDirecc", "d04", 10, new AddressRecord()),
			new Field("tpTipPer", "p07", 10, ""),
			new Field("tpNacion", "n07", 10, ""),
			
			// suplantar cuando se pueda
			new Field("tpSiglas", null, 10, ""),
			
			new Field("tpTipCon", "c23", 10, ""),
			new Field("tpCedRep", "c24", 10, new Integer(0)),
			new Field("tpNacRep", "n07", 2, ""),
			new Field("tpRifRep", "r12", 10, ""),
			new Field("tpNitRep", "n06", 10, ""),
			new Field("tpNomRep", "n01", 30, ""),
			new Field("tpDirRep", "d04", 40, new AddressRecord()),
			new Field("tpSocied", "t12", 2, ""),
			new Field("tpActEco", "a30", 5, ""),
			new Field("tpDomici", "d05", 1, new Boolean(true)),
			new Field("tpMayori", "m22", 1, new Boolean(false)),
			new Field("tpImport", "i16", 1, new Boolean(false)),
			new Field("tpRetenc", "r14", 1, new Boolean(false)),
			new Field("tpRecept", "r15", 1, new Boolean(false)),
			new Field("tpRenExe", "r16", 1, new Boolean(false)),
			new Field("tpLucro", "l08", 1, new Boolean(true)),
			new Field("tpNomCon", "n01", 30, ""),
			new Field("tpCedCon", "c24", 10, new Integer(0)),
			new Field("tpCpcCon", "c25", 10, ""),
			new Field("tpCedula", "c24", 10, new Integer(0)),
			new Field("tpFecNac", "f11", 10, new AppDate()),
			new Field("tpReside", "r17", 1, new Boolean(false)),
			new Field("tpECivil", "e14", 7, ""),
			new Field("tpConyDA", "c26", 1, new Boolean(true)),
			new Field("tpSepBie", "s12", 1, new Boolean(false)),
			new Field("tpActEcP", "a31", 10, ""),
			new Field("tpActEcS", "a32", 10, ""),
			
		});
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.businesso.Record#showInList(int)
	 */
	public boolean showInList(int col) {
		return (col < 3);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.businesso.Record#getKey()
	 */
	public String getKey() {
		return (String) getFieldValue(0);
	}
}