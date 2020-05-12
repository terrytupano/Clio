/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

/** registro de preferencias de la aplicacion. Solo puede haber 1. Este registro, al
 * crearse, ya contiene los valores por omision
 * 
 */
public class PreferenceRecord extends Record {

	public PreferenceRecord() {	
		super(new Field[] {
			new Field("prTipPer", "p07", 10, "na"),
			new Field("prTema", "t13", 20, "com.ae.gui.kunststoff.KunststoffDesktopTheme"),
			new Field("prGAutom", "s10", 2, new Integer(3)),
			new Field("prFont", "t07", 2, "ARIAL.TTF;11"),
			new Field("prTipsAS", "t11", 2, new Boolean(true)),
			new Field("prAdquiF", "a29", 2, new Integer(5)),
			new Field("prEndTxt", "t14", 30, ""),
			new Field("prEmail", "e06", 30, "")
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
		return "key";
	}
}