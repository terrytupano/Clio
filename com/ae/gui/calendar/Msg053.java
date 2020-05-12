/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.calendar;

import java.util.*;

import com.ae.core.*;
import com.ae.resource.*;

/** clase que determina si estan dados los elementos necesarios para que
 * <code>DutyCalendar</code> presente el mensaje msg053. Adicionalmente
 * esta clase restablece el inventario si el año de la fecha de hoy
 * es mayor al año de la ultima compra dentro del inventario.
 * 
 */
public class Msg053 implements Runnable {
	
	private static int LAPSE = 28; // siempre <= 28
	private Hashtable docEntries;
	
	private DutyCalendar dutyC;
	
	/** nueva instancia
	 * 
	 * @param dt - instancia de <code>DutyCalendar</code>
	 * @param de - documento .xml en formato apropidado
	 */
	public Msg053(DutyCalendar dt, Hashtable de) {
		this.dutyC = dt;
		this.docEntries = de;
	}

	
	/** metodo que centraliza el inicio o reinicio del registro
	 * de inventario. Este metodo tambien asigna un nuevo identificador de 
	 * aplicacion. Este es almacenado en el campo sAppId del registro 
	 * "ar-i". El id de aplicacion esta compuesto por 4 caracteres en 
	 * minuscula.
	 * 
	 * @param sr - Instancia de <code>StrockRecord</code>
	 */
	public static void resetStock() {
		AppEntry[] apes = ResourceUtilities.getInstalledForms();
		for(int c = 0; c < apes.length; c++) {
			String fna = (String) apes[c].getKey(); 
			if (fna.equals("ar-i")) {
				
				Random rnd = new Random();
				StringBuffer sai = new StringBuffer(4);
				while (sai.length() < 4) {
					char ch = (char) (rnd.nextInt(26) + 97);
					if (Character.isLowerCase(ch)) {
						sai.append(ch);
					}
				}
				
				StockRecord sr = new StockRecord();
				sr.setFieldValue("sForm", fna);
				sr.setFieldValue("sAppId", sai.toString());
				sr.setFieldValue("sCantida", new Integer(1000));
				sr.setFieldValue("sFecha", new AppDate(DIMain.bundle.getString("mea")));
				PrevalentSystemManager.executeCommand(new NewRecordCommand(sr));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		GregorianCalendar cal = new GregorianCalendar();
		int td = cal.get(GregorianCalendar.DAY_OF_MONTH);
		int tm = cal.get(GregorianCalendar.MONTH);
		int ty = cal.get(GregorianCalendar.YEAR);
		cal.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
		int ld = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		int lm = cal.get(GregorianCalendar.MONTH);
		int res = ld - td;
		if (res > 0 && tm == lm) {
			Object[] exd = new Object[] {new Integer(res)};
			dutyC.addMessage("msg53", exd);
		}
		
		// reestablecer inventario?
		/*
		StockRecord Srcd = DIMain.stockRecord;
		String en = (String) Srcd.getFieldValue(0);
		String[] arr = UnlookAction.cipher(UnlookAction.DECRYPT, en).split(";");
		int ly = Integer.parseInt(arr[0].substring(0, 4));
		if (ty > ly) {
			resetStock(Srcd);
		}
		*/
		
	}
}