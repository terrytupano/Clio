/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.logging.*;
import java.util.zip.*;

import javax.swing.*;

import com.ae.core.*;

/** esta clase centraliza la instalacion o desinstalacion de arreglos temporales
 * de programa. 
 * 
 */
public class PTFManager {
	
	/** instalacion de ptf. este metodo determina si se encuentran archivos para ptf (todo 
	 * archivo que no sea producer) si es asi, respalda los archivo viejo y respalda
	 * 
	 * @param qjn - nombre completo del paquete que contiene los arregos
	 */
	public static void installPTF(String qjn) {
		Vector v = new Vector();
		try {
			JarFile jf = new JarFile(qjn);
			Enumeration e =jf.entries();
			while(e.hasMoreElements()) {
				ZipEntry ze = (ZipEntry) e.nextElement();
				String zefn = ze.getName();
				if (!zefn.equals("producer")) {
					v.addElement(ze);
				}
			}
			if (v.size() > 0) {
				Object[] options = {DIMain.bundle.getString("c27")};
				int o = JOptionPane.showOptionDialog(null, DIMain.bundle.getString("m24"),
				DIMain.bundle.getString("c12"),  
				JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]);

				backup(v);
				install(jf, v);

				Object[] opt1 = {DIMain.bundle.getString("r18")};
				o = JOptionPane.showOptionDialog(null, DIMain.bundle.getString("m25"),
				DIMain.bundle.getString("c12"),  
				JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, opt1, opt1[0]);
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
	}
	
	/** reviza la lista pasada como argumento de entrada y si existe un archivo
	 * que sera suplantado por uno nuevo dentro del paquete, lo respalda 
	 * copiandolo dentro de un archivo ptf####.zip donde # es la fecha de hoy 
	 * (en mili)
	 * 
	 * @param vec - vector con elementos encontrados dentro del paquete
	 * @throws IOException
	 */
	private static void backup(Vector vec) throws IOException {
		
		File jarf = new File(System.getProperty("user.dir") + 
			"/ptf" + (new Date()).getTime() + ".zip");
		jarf.createNewFile();
		FileOutputStream fos = new FileOutputStream(jarf);
		JarOutputStream jos = new JarOutputStream(fos);
		
		for (int v = 0; v < vec.size(); v++) {
			ZipEntry ze = (ZipEntry) vec.elementAt(v);
			String zefn =  ze.getName();
			File fn = new File(System.getProperty("user.dir") + "/" + zefn);
			
			// si no existe, debe ser un archivo nuevo.
			if (fn.exists()) {
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fn));
				jos.putNextEntry(new ZipEntry(zefn));
				byte[] b = new byte[(int) fn.length()];
				for (int x = 0; x < b.length; x++) {
					b[x] = (byte) bis.read();
				}				
				jos.write(b);
			}
		}
		jos.close();
	}
	
	/** para cada elemento de la lista, extrae los datos y los coloca en sus 
	 * ubicaciones 
	 * 
	 * @param jf - paquete origen de datos
	 * @param v - lista de elementos a instalar
	 */
	private static void install(JarFile jf, Vector v) throws IOException {
		for (int l = 0; l < v.size(); l++) {
			ZipEntry ze = (ZipEntry) v.elementAt(l);
			InputStream is = jf.getInputStream(ze);
			byte[] b = new byte[(int) ze.getSize()];
			for (int x = 0; x < b.length; x++) {
				b[x] = (byte) is.read();
			}
			String sfn = System.getProperty("user.dir") + "/" + ze.getName();
			File fn = new File(sfn);
			if (fn.exists()) {
				fn.delete();
				fn = new File(sfn);
			} else {
//				fn.createNewFile();
			} 

			BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(fn));
			bos.write(b);
			bos.close();
			is.close();
		}
	}	
}
