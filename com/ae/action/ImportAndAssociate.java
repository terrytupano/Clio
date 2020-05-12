/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;
import org.jdom.*;

import com.ae.core.*;
import com.ae.gui.*;
import com.ae.gui.wizard.*;
import com.ae.input.*;
import com.ae.resource.*;

/** esta clase realiza la importacion de las cuentas contables seleccionadas
 * en el ultimo paso de importar cuentas contables y asocia dichas cuentas
 * a las secciones segun el contenido de la kb. 
 * 
 */
public class ImportAndAssociate implements Runnable {
	
	private WizardPanel lastPanel;
	private ProgressPanel progressP;
	private AppTableModel tmodel;
	private Vector accVect;
	private boolean auto;
	private Account_ClioObject acc_clo;
	private TaxPayerRecord taxP_rcd;
	private AppEntry[] a_forms;
	private RunnableMonitor rMon;
	private String luceneDir; 
	
	/** nueva instancia
	 * 
	 * @param sel - instancia de <code>Account_ClioObject</code>
	 * @param ad - registros leidos desde el catalogo de cuentas (todos o 
	 * seleccionado)
	 * @param pb - instancia de barra de progreso
	 * @param a - si es automatico o manual.
	 */
	public ImportAndAssociate(Account_ClioObject aco, Vector ad, 
		ProgressPanel pb, boolean a) {
		this.acc_clo = aco;
		this.accVect = ad;
		this.progressP = pb; 
		this.rMon = new RunnableMonitor(progressP);
		this.auto = a;
		this.luceneDir = System.getProperty("java.io.tmpdir") + "iw" + Math.random();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		progressP.increment(DIMain.bundle.getString("i12"));
		this.taxP_rcd = acc_clo.getTaxPayerSelector().getSelectedRecord();
		this.a_forms = CoreUtilities.getFormsFor(taxP_rcd.getKey());
		
		// para que el resto de la aplicacion espabile
		try {
			Thread.sleep(1000);
		}catch (Exception e) {
			//
		}
		
		// si el proceso es manual solo importa. 
		if (!auto) {
			progressP.offsetMaximum(accVect.size());
			for (int x = 0; x < accVect.size() && !rMon.stop; x++) {
				AccountRecord acr = (AccountRecord) accVect.elementAt(x);
				progressP.increment(DIMain.bundle.getString("a04") + " " + 
					acr.getFieldValue("acText"));
				importAccount(acr);
			}
		} else {
			progressP.increment(DIMain.bundle.getString("p03")); 
			Object hasvec[] = selectAccounts(); 
			progressP.offsetMaximum(a_forms.length);
			try {
				associateAccounts(hasvec);
			} catch (Exception e) {
				Logger.getLogger("").log(Level.SEVERE, e.getMessage(), e);
			}
		}
		rMon.dialog.dispose();
	}

	/** este metodo recibe un registros de cuentas contables donde el campo 
	 * acRifTP contiene una constate arbiraria, se reemplaza esa constante 
	 * por el valor correcto segun el contribuyente seleccionado, y se 
	 * adicionan los registro (si no existen) o se actulizan los datos de
	 * importacion si existe
	 * 
	 * @param rcdsrc - vector con cuentas contables a importar
	 */
	private void importAccount(AccountRecord acr) {
		this.tmodel = PrevalentSystemManager.getTableModel(acr, false);
		acr.setFieldValue("acRifTP", taxP_rcd.getKey());
		AccountRecord rcd = (AccountRecord) tmodel.getRecord(acr.getKey());
		if (rcd == null) {
			rcd = acr;
			PrevalentSystemManager.executeCommand(new NewRecordCommand(rcd));
		} else {
			rcd.setFieldValue("acIApp", acr.getFieldValue("acIApp"));
			rcd.setFieldValue("acIDir", acr.getFieldValue("acIDir"));
			PrevalentSystemManager.executeCommand(new EditRecordCommand(rcd));
		}
	}
	
	/** retorna un arrego de objetos {Hashtable, Vector} con datos pertenecientes
	 * a los registro de solo movimientos. 
	 * la logica es:
	 * - se determina la longitud mas "larga" del codigo de cuenta.
	 * - se separan las cuentas de movimientos de las de grupo.
	 * - con los datos de movimiento se crean dos objetos: una Hashtable con las
	 * cuentas que sera usado para grabar en la tabla de cuentas y un vector 
	 * que sera usado para crear el indice de busqueda
	 * - para cada documento dentro del vector, se concatenan el texto de aquellas
	 * cuentas de grupo cuyo codigo sea el primcipio de una cuenta de movimiento.    
	 * 
	 * @return - arreglo de objetos donde el primero es para importar y el
	 * segundo es para el indice.
	 */
	private Object[] selectAccounts() {
		// longitud de cuenta de mov
		int movlen = 0;
		for (int r = 0; r < accVect.size() && !rMon.stop; r++) {
			AccountRecord acr = (AccountRecord) accVect.elementAt(r);
			String acc = (String) acr.getFieldValue("acCode");
			movlen = (movlen > acc.length()) ? movlen : acc.length(); 
		}

		// se separan las cuentas de movimientos de las cuentas de grupo
		// y se crea hashtable con registros a importar
		Vector vmov = new Vector();
		Hashtable hmov = new Hashtable();
		Vector vgrp = new Vector();
		for (int r = 0; r < accVect.size() && !rMon.stop; r++) {
			AccountRecord acr = (AccountRecord) accVect.elementAt(r);
			String acc = (String) acr.getFieldValue("acCode");
			String atx = (String) acr.getFieldValue("acText");
			if (acc.length() == movlen) {
				AccountRecord arc1 = new AccountRecord();
				for (int c = 0; c < arc1.getFieldsCount(); c++) {
					arc1.setFieldValue(c, acr.getFieldValue(c));
				}
				vmov.add(arc1);
				hmov.put(acc, acr);
			} else {
				vgrp.add(acr);
			}
		}

		// se concatena texto de cuentas de grupo en cuentas de movimiento
		for (int r = 0; r < vgrp.size() && !rMon.stop; r++) {
			AccountRecord acr = (AccountRecord) vgrp.elementAt(r);
			String gcod = (String) acr.getFieldValue("acCode");
			String gtxt = (String) acr.getFieldValue("acText");
			gtxt = gtxt.toLowerCase();
			for (int ri = 0; ri < vmov.size(); ri++) {
				acr = (AccountRecord) vmov.elementAt(ri);
				String mcod = (String) acr.getFieldValue("acCode");
				String mtxt = (String) acr.getFieldValue("acText");
				mtxt = mtxt.toLowerCase();
				if (mcod.startsWith(gcod)) {
					acr.setFieldValue("acText", mtxt + " " + gtxt);
				}
			}
		}
		
		// para las cuentas a importar, se eliminan las palabras repetidas
		// esto es para disminuir el Hits.score()
		//
		// nota: puede que no haga falta verificar
		for (int r = 0; r < vmov.size() && !rMon.stop; r++) {
			AccountRecord acr = (AccountRecord) vmov.elementAt(r);
			String stx1 = ((String) acr.getFieldValue("acText")).replace(' ', ';');
			String[] stx = stx1.split(";");
			String newt = "";
			for (int s = 0; s < stx.length; s++) {
				for (int s1 = s + 1; s1 < stx.length; s1++) {
					if (stx[s].equals(stx[s1])) {
						stx[s1] = "";
					}
				}
				newt += stx[s] + " ";
			}
			acr.setFieldValue("acText", newt);
		}
		
		return new Object[] {hmov, vmov};
	}
	
	/** asocia las cuentas contables a las secciones segun las directrices
	 *  
	 * @param arreglo de objetos retornados por la seleccion de cuentas
	 */
	private void associateAccounts(Object[] hasvec) throws IOException, 
		org.apache.lucene.queryParser.ParseException {
		StandardAnalyzer analizer = new StandardAnalyzer();
		Hashtable hmov = (Hashtable) hasvec[0];
		Vector assv = (Vector) hasvec[1];
		
		// con el vector de cuentas de movimientos concatenados, crea el 
		// indice de busqueda. 
		IndexWriter iw = new IndexWriter(luceneDir, analizer, true);
		for (int e = 0; e < assv.size() && !rMon.stop; e++) {
			AccountRecord acr = (AccountRecord) assv.elementAt(e);
			org.apache.lucene.document.Document ld = new org.apache.lucene.document.Document();
			ld.add(org.apache.lucene.document.Field.Text(
				"acCode", (String) acr.getFieldValue("acCode")));
			ld.add(org.apache.lucene.document.Field.Text(
				"acText", (String) acr.getFieldValue("acText")));
			iw.addDocument(ld);
		}
		iw.close();
		iw.optimize();

		// hashtable con consultas, palabras y fucionces de precalculo
		// alamacenadas dentro de kb 
		Hashtable hqryexp = new Hashtable();
		Hashtable hqryprec = new Hashtable();
		Hashtable hword = new Hashtable();
		Vector vword = new Vector();
		org.jdom.Document d = PrevalentSystemManager.getXMLDocument(
			ResourceUtilities.getPrevaylerPath() + "kb.xml");
		Element ro = d.getRootElement();
		List l = ro.getChildren();
		for (int e = 0; e < l.size() && !rMon.stop; e++) {
			Element el = (Element) l.get(e);
			String sid = el.getAttributeValue("id");
			if (el.getName().equals("word")) {
				hword.put(sid, el.getAttributeValue("meaning"));
				vword.add(sid);
			} else {
				hqryexp.put(sid, el.getAttributeValue("expression"));
				String pc = el.getAttributeValue("precal");
				if (pc != null) {
					hqryprec.put(sid, pc);
				}
			}
		}
		
		//prepara el contenido del atributo expresion. para cada elemento
		//query. para ello se reemplaza cada palabra (elemento word) dentro
		// de la expresion expression.
		Enumeration eqry = hqryexp.keys();
		while(eqry.hasMoreElements() && !rMon.stop) {
			String key = (String) eqry.nextElement();
			String exp = (String) hqryexp.get(key);
			for (int wc = 0; wc < vword.size(); wc++) {
				String w = (String) vword.elementAt(wc);
				exp = exp.replaceAll(w, (String) hword.get(w));
			}
			hqryexp.put(key, exp);
		}
		
		// inicio de consultas. para cada seccion con atributo query
		// se evalua para determinar si exiten asiertos dentro del indice
		// de busqueda. si exite(n) se extrae el codigo de cuentay se asocia
		// a dicha seccion
		Searcher searcher = new IndexSearcher(luceneDir);
		for (int foc = 0; foc < a_forms.length && !rMon.stop; foc++) {
			Vector v_sec = CoreUtilities.getAssociableSections((String) a_forms[foc].getKey());
			// usa el </html> del valor de forma registrada
			progressP.increment("<html>" + 
				DIMain.bundle.getString("c28") + " " + (String) a_forms[foc].getValue());
			for (int sco = 0; sco < v_sec.size() && !rMon.stop; sco++) {
				Element el = (Element) ((AppEntry) v_sec.elementAt(sco)).getValue();
				String sqry1 = el.getAttributeValue("query");

				// no hay consulta que hacer
				if (sqry1 == null) {	continue;}
				
				// para varias consultas separadas por ;
				String[] sqry; 
				if (sqry1.indexOf(';') > 0) {
					sqry = sqry1.split(";");
				} else {
					sqry = new String[] {sqry1};
				}
				
				// ejecucion de consultas
				for (int q1 = 0; q1 < sqry.length; q1++) {
					System.out.println(sqry[q1] + " -- " + (String) hqryexp.get(sqry[q1]));
					Hits hit = searcher.search(QueryParser.parse(
						(String) hqryexp.get(sqry[q1]), "acText", analizer));
					for (int hc = 0; hc < hit.length(); hc++) {
						org.apache.lucene.document.Document dc = hit.doc(hc);
						
						System.out.println("   " + hit.score(hc) + " -- " + 
							dc.get("acCode") + " -- " + dc.get("acText"));
						
						String acc = dc.get("acCode");
						AccountRecord ar = (AccountRecord) hmov.get(acc);
						ar.setFieldValue("acPreC", hqryprec.get(sqry[q1])); 
						importAccount(ar);
						acc_clo.insertIfNotExist(a_forms[foc].getKey(), 
							el.getAttributeValue("id"), acc);
					}
				}
			}
		}
	}
}
