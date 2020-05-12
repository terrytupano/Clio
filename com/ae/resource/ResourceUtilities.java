/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.jdom.*;

import com.ae.core.*;

/** Utilidad para el acceso uniforme de recursos almacenados para la aplicacion. al
 *  
 */
public class ResourceUtilities {
	
	private static String dir = System.getProperty("user.dir");
	private static String documentPath = dir + "/com/ae/resource/docs/";
	private static String iconPath = dir+"/com/ae/resource/icons/";
	private static String pluginsPath = dir + "/com/ae/resource/forms/";
	private static String prevaylerPath = dir + "/prevalenceBase/";
//	private static String stockPath = System.getProperty("user.home") + "/";
	private static TreeMap treeMap = new TreeMap();
	
	/** retorna origen de datos para los archivos que contienen la 
	 * programacion de la forma. 
	 * 
	 * @param fn - nombre de la forma
	 * @return <code>File</code>
	public static Reader getDescriptorFor(String fn) throws IOException {
		File f = new File(pluginsPath + fn + ".xml");
		return CoreUtilities.translate(f);
	}
	 */
	
	public static ResourceBundle getBundle() throws IOException {
		File f = new File(prevaylerPath + "/StringConstants.properties");
		String tf = new String(CoreUtilities.translate(f));
		ByteArrayInputStream bais = new ByteArrayInputStream(tf.getBytes());
		return new PropertyResourceBundle(bais);
	}
	
	/** retorna el directorio designado para los archivos de anotaciones del
	 * sistema
	 * 
	 * @return directorio
	 */
	public static String getLogFileName() {
		return dir + "/clio.log";
	}
	
	/** Retorna la imagen de la forma o las imagenes relacionadas a esta.
	 * 
	 * @param fn nombre de la forma
	 * @param in nombre de la imagen. 
	 * 
	 * @return Imagen solicitada
	 */
	public static ImageIcon getFormImage(String fn, String in) {
		return new ImageIcon(pluginsPath + in + ".gif"); 
//		return new ImageIcon(pluginsPath + fn + "/" + in + ".gif"); 
	}
	
	/** Retorna la imagen solicitada 
	 * 
	 * @param in - Nombre del la imagen
	 * @return icono
	 */
	public static ImageIcon  getIcon(String in) {
		return new ImageIcon(iconPath + in + ".gif");
	}
	
	/** Retorna lista de formas instaladas.
	 * 
	 * @return arreglo con las formas registradas
	 */
	public static AppEntry[] getInstalledForms() {
		File f = new File(pluginsPath);
		File[] fls = f.listFiles();
		TreeMap tmap = new TreeMap();
		for(int j = 0; j < fls.length; j++) {
			String fn = fls[j].getName();
			if (fn.endsWith(".xml")) {
				fn = fn.substring(0, fn.length() - 4);
				Hashtable h = 
					PrevalentSystemManager.getFormElements(fn);
				Element e = (Element) h.get("form");
				String lab = 
					"<HTML><b>" + e.getAttributeValue("id") + 
					"</b> - " + e.getAttributeValue("name") + "</HTML>";
				tmap.put(e.getAttributeValue("id"), new AppEntry(
					e.getAttributeValue("id"), lab));
			}
		}
		return (AppEntry[]) tmap.values().toArray(new AppEntry[tmap.size()]);
		
		/*
		File f = new File(pluginsPath);
		File[] fls = f.listFiles();
		Vector vec1 = new Vector();
		for(int j = 0; j < fls.length; j++) {
			File f1 = new File(fls[j], "/descriptor.xml");
			if (f1.isFile()) {
				Hashtable h = 
					DIMain.prevalentSystemManager.getFormElements(fls[j].getName());
				Element e = (Element) h.get("form");
				String lab = 
					"<HTML><b>" + e.getAttributeValue("id") + 
					"</b> - " + e.getAttributeValue("name") + "</HTML>";

				vec1.add(new AppEntry(
					e.getAttributeValue("id"), lab));
			}
		}
		return (AppEntry[]) vec1.toArray(new AppEntry[vec1.size()]);  
		 */
		
		  
	}
	/** retorna la ruta hacia la carpeta donde se encuentran las formas 
	 * 
	 * @return ruta
	 */
	public static String getPluginsPath() {
		return pluginsPath;
	}

	/** retorna la ruta hacia la carpeta donde se encuentran 
	 * los documentos  
	 * 
	 * @return ruta
	 */
	public static String getDocumentPath() {
		return documentPath;
	}
	
	/** retorna <code>URL</code> de un archivo que se encuentre 
	 * en la carpeta de docuementos. Si la instancia de <code>new File(qn)</code>
	 * no es un archivo, se adiciona la extencion ".htm" y se intenta de nuevo 
	 * 
	 * @param qn- nombre calificado con extencion o sin ella para qn + ".htm" 
	 * @return URL
	 */
	public static URL getURL(String qn) {
		URL u = null;
		try {
			File f = new File(getDocumentPath() + qn);
			if (!f.isFile()) {
				qn = qn + ".htm";
				f = new File(getDocumentPath() + qn);
			}
			u = f.toURL(); 
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage() + "(" + qn + ")", e);
		}
		return u;
	}
		
	/** retorna la ruta hacia la carpeta donde se almacenan los datos de la 
	 * aplicacion. (todos los datos de la aplicacion exepto los del inventario
	 * 
	 * @return ruta
	 */
	public static String getPrevaylerPath() {
		return prevaylerPath;
	}

	/** retorna ruta donde se almacenan los datos del inventario de 
	 * formas.
	 * 
	 * @return ruta
	public static String getStockPath() {
		return stockPath;
	}
	 */	
	
	/** Retorna el icono pequeño (16*16) 
	 * 
	 * @param in - Nombre del la imagen
	 * @return icono
	 */
	public static ImageIcon  getSmallIcon(String in) {
		return new ImageIcon(iconPath + in + "16.gif");
	}
}
