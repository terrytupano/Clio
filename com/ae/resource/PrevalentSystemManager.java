/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */
 
package com.ae.resource;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.jdom.*;
import org.jdom.input.*;
import org.prevayler.implementation.*;

import com.ae.core.*;


/** Centro de control para todo lo referente al sistema prevalente de datos.
 * Esta clase se encarga de dar todo el soporte necesario para el acceso y control
 * de los datos almacenados dentro de la aplicacion. Cualquier dato controlado por
 * esta clase, es guardado para mejorar el rendimiento para futuros acceso.
 * Para cualquier instancia de <code>SnapshotPrevayler</code> es ejecutado
 * un <code>SnapshotPrevayler.takeSnapshot()</code> cada 30 minutos para todas
 * las entradas guardadas
 *
 */
public class PrevalentSystemManager {
	
	private static TreeMap treeMap;
	
	public static Hashtable exceptions;
	
	/** Nueva instancia de esta clase
	 */
	public PrevalentSystemManager() {
		treeMap = new TreeMap();
//		Thread t = new Thread(this, "TableModelManager");
//		t.start();
		
		exceptions = new Hashtable();
		LoadExceptions.run();
		
//		t = new Thread(new LoadExceptions(), "Load exceptions");
//		t.start();
		
	}
		
	/** Retorna <code>SnapshotPrevayler</code>
	 * 
	 * @param mod
	 * @return
	 */
	public static SnapshotPrevayler getPrevayler(Record mod) {
		String key = loadPrevayler(mod);
		return (SnapshotPrevayler) treeMap.get(key);
	}
	
	/** ejecuta el comando pasado como argumento. El model del archivo al 
	 * hacial el cual este comando hace referencia es determinado usando
	 * <code>cmd.getDate()</code> 
	 * 
	 * @param cmd - comando
	 */
	public static void executeCommand(AbstractCommand cmd) {
		try {
			getPrevayler(cmd.getData()).executeCommand(cmd);
		} catch (Exception e) {
			String sc = e.getMessage() + "(" + cmd.getClass().getName() + ")";
			Logger.getLogger("").logp(Level.SEVERE, null, null, sc, e);
		}
	}
	
	/** Del archivo cuyo modelo es <code>rcd</code>, retorna el registro cuya 
	 * clave sea <code>key</code>
	 * 
	 * @param rcd - modelo de archivo
	 * @param key - clave
	 * @return <code>Record</code> con clave solicitada
	 */
	public static Record getRecordFrom(Record rcd, String key) {
		AppTableModel atm = getTableModel(rcd, false);
		return atm.getRecord(key);
	}
	/** invoca <code>getTableModel(model, true)</code> 
	 * 
	 * @param model - model para appTableModel
	 * @return appTableModel 
	 */
	public static AppTableModel getTableModel(Record model) {
		return getTableModel(model, true);
	}
	
	/** retorn <code>AppTableModel</code> cuyo modelo es el argumento de entrada.
	 * 
	 * @param model - model para appTableModel
	 * @param clrsel - indica si se elimina cualquier seleccion previa para la
	 * AppTableModel. Esto es debido a que la aplicacion entera funciona solo
	 * con una instancia de AppTableModel para cada registro modelo. Por ello,
	 * diferentes objetos aceden a la misma instancia alterando la seleccion
	 * modifiando la vista de otros objetos aun visibles.
	 * 
	 * @return appTableModel si seleccion alterada 
	 */
	public static AppTableModel getTableModel(Record model, boolean clrsel) {
		String key = loadPrevayler(model);
		SnapshotPrevayler prev = (SnapshotPrevayler) treeMap.get(key);
		AppTableModel atm = (AppTableModel) prev.system();
		if (clrsel) {
			atm.selectRecord(null);
		}
		return atm;
	}

	/** Verifica si es necesario cargar en memoria <code>SnapShotPrevayler</code> 
	 * para <code>AppTableModel</code> cuyo modelo es el argumento de entrada. 
	 * 
	 * Si no existe una entrada registrada registrada, se intenta localizar 
	 * el sistema prevalente <code>AppTableModel</code> asociado con el 
	 * modelo pasado como parametro. Se registra la carga
	 * 
	 * Si existe, solo retorna el identificador asociado con el modelo	 *
	 * 
	 * @param model - modelo para <code>AppTableModel</code>
	 * @return id de modelo
	 */	
	private static String loadPrevayler(Record model) {
		String key = model.getClass().getName();
		SnapshotPrevayler prev = null;
		try {
			if (!treeMap.containsKey(key)) {
				prev = new SnapshotPrevayler(new AppTableModel(model), 
					ResourceUtilities.getPrevaylerPath() + key);
				treeMap.put(key, prev);
			}
		} catch (Exception e) {
			String sm = e.getMessage() + "(" + model + ")";
			Logger.getLogger("").logp(Level.SEVERE, null, null, sm, e);
		}
		return key;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Runnable#run()
	public void run() {
		try {
			while (true) {
				Thread.sleep(1000 * 60 * 30);
				Iterator iter = treeMap.keySet().iterator();
				while(iter.hasNext()) {
					String dir = (String) iter.next(); 
					Object obj = treeMap.get(dir);
					if (obj instanceof SnapshotPrevayler) {

						// lista de archivos en directorio antes de snapshot
						File rdir = new File(ResourceUtilities.getPrevaylerPath() + dir);
						File[] files = rdir.listFiles();

						((SnapshotPrevayler) obj).takeSnapshot();

						
						// suprime todos los archivos *.commandLog y *.snapshot
						// de la lista; es decir, todos menos el recien creado
						// si la lista de archivos es mayor a 1. ya que si 
						// solo hay un .snapshot, SnapshotPrevayler.takeSnapShot() no
						// tiene ningun efecto
						if (files.length > 1) {
							for (int f = 0; f < files.length; f++) {
								files[f].delete();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
	}
	 */

	/** ejecutado por la aplicacion al finalizar para mantener la 
	 * base de datos los mas limpia posible.
	 *
	 */	
	public static void appEnd() {
		try {
			Iterator iter = treeMap.keySet().iterator();
			while(iter.hasNext()) {
				String dir = (String) iter.next(); 
				Object obj = treeMap.get(dir);
				if (obj instanceof SnapshotPrevayler) {
					((SnapshotPrevayler) obj).takeSnapshot();
					File rdir = new File(ResourceUtilities.getPrevaylerPath() + dir);
					File[] files = rdir.listFiles();
					
					// ordena por fecha de ultima modificaion y elimina
					// todos menos el ultimo
					TreeMap tm = new TreeMap();
					for (int i = 0; i < files.length; i++) {
						tm.put(new Long(files[i].lastModified()), files[i]);
					}
					Vector v = new Vector(tm.values());
					for (int f = 0; f < v.size() - 1; f++) {
						files[f].delete();
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
	}
	
	/** Retorna una instancia de <code>Document</code> construido a partir del
	 * archivo xml pasado como argumento 
	 * 
	 * @param qf - nombre de archivo (path/nombre.ext)
	 * @return documento
	 */	
	public static Document getXMLDocument(String qn) {
		File f = new File(qn);
		Document doc = null;
		if (!treeMap.containsKey(f.toString())) {
			try {
				SAXBuilder sb = new SAXBuilder();
				doc = sb.build(new CharArrayReader(CoreUtilities.translate(f)));
				treeMap.put(f.toString(), doc);
			} catch(Exception e) {
				String sm = e.getMessage() + "(" + f.getAbsolutePath() + ")";
				Logger.getLogger("").logp(Level.SEVERE, null, null, sm, e);
			}
		} else {
			doc = (Document) treeMap.get(f.toString());
		}
		return doc;
	}
	/** Localiza el archivo de configuracion de la forma y retorna todos los 
	 * elementos e este dentro de un <code>Hashtable</code> cuya clave es el
	 * nombre del elementos y el valor es el elemento en si. 
	 * Para las secciones, la clave es el identificador de seccion. 
	 * Los mensajes no estan incluidos
	 * 
	 * -----
	 * pendiente depuracion. colocar en una tabla de elementos las formas
	 * ya cargadas de manera que no se repita la compialacion cada ves
	 * que se solicite los elementos de una forma ya cargada
	 * -------
	 * 
	 * @param fn - id de la forma
	 * @return - lista con todos los elementos 
	 */
	public static Hashtable getFormElements(String fn) {
		Document doc = getXMLDocument(
			ResourceUtilities.getPluginsPath() + fn + ".xml"
		);
		Element root = doc.getRootElement();
		Hashtable ht = new Hashtable();
		ht.put(root.getName(), root);
		List l = root.getChildren();
		for (int c = 0; c < l.size(); c++) {
			Element e = (Element) l.get(c);
			if (e.getName().endsWith("section")) {
				ht.put(e.getAttributeValue("id"), e);
			} else {
				ht.put(e.getName(), e);
			}
		}
		return ht;
	}
	
	public static void deleteRelatedRecord(Record r) {
		Vector vr = new Vector();

		//contribuyente
		if (r instanceof TaxPayerRecord) {

			/*
			String sel = "'{acRifTP}' == '" + r.getKey() + "'";
			emitDeleteRecord(sel, new PreferenceRecord());
			*/
			String sel = "'{acRifTP}' == '" + r.getKey() + "'";
			emitDeleteRecord(sel, new AccountRecord());

			sel = "'{coRifTP}' == '" + r.getKey() + "'";
			emitDeleteRecord(sel, new Account_ClioObjectRecord());

			sel = "'{avRifTP}' == '" + r.getKey() + "'";
			emitDeleteRecord(sel, new VoucherRecord());

			sel = "'{fsRif}' == '" + r.getKey() + "'";
			emitDeleteRecord(sel, new FormSavedRecord());
		}
		
		// Cuentas contables
		if (r instanceof AccountRecord) {

			String sel = 
				"'{coRifTP}' == '" + (String) r.getFieldValue(0) + "' && " + 
				"'{coAcCode}' == '" + (String) r.getFieldValue(1) + "'";
			emitDeleteRecord(sel, new Account_ClioObjectRecord());

			sel = 
				"'{avRifTP}' == '" + (String) r.getFieldValue(0) + "' && " + 
				"'{avAccAC}' == '" + (String) r.getFieldValue(1) + "'";
			emitDeleteRecord(sel, new VoucherRecord());
		}

	}
	
	/** metodo auxiliar que se encarga de la emision masiva de comandos de 
	 * supresion.
	 * 
	 * @param sel - expresion para la seleccion de aquellos registros que
	 * seran suprimidos. 
	 * @param mod - modelo para <code>AppTableModel</code>
	 */
	private static void emitDeleteRecord(String sel, Record mod) {
		AppTableModel atm = getTableModel(mod, false);
		String se = atm.getSelectionExpresion();
		atm.selectRecord(sel);
		Vector vr = atm.getRecords(false);
		for (int j = 0; j < vr.size(); j++) {
			executeCommand(new DeleteRecordCommand((Record) vr.elementAt(j)));
		}
		atm.selectRecord(se);
	}
	
}
