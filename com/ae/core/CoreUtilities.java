/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.logging.*;
import java.util.zip.*;
import java.text.*;

import javax.swing.*;

import org.jdom.*;

import com.ae.evaluate.*;
import com.ae.form.*;
import com.ae.resource.*;

/** Clase con metodos utilitarios de proposito general
 * 
 */
public abstract class CoreUtilities {

	// actualizadas desde main y util1
	public static boolean development = false; 


	/** determina si una determinada forma es adecuada para un deternimado 
	 * contribuyente.
	 * 
	 * @param fdta - Entrada que contiene la informacion de la forma.
	 * @param tpr - contribuyente
	 * @return true si la forma es valida para el contribuyente. 
	 */
	public static boolean isSuitable(AppEntry fdta, TaxPayerRecord tpr) {
		String fn = (String) fdta.getKey();
		Hashtable ht = PrevalentSystemManager.getFormElements(fn);
		Element el = (Element) ht.get("gv.taxPayerConditions");
		boolean flag = false;
		
		Evaluator evaluator = new Evaluator();
		
		try {
			//evaluator.clearVariables();
			evaluator.parse(el.getAttributeValue("value"));
			String[] vars = evaluator.getVariables();
			for (int v = 0; v < vars.length; v++) {
				evaluator.putVariable(
					vars[v],
					FormUtilities.stringFormat(
						tpr.getFieldValue(vars[v]),
						false));
			}
			flag = evaluator.evaluate().equals("1.0");
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
		return flag;
	}

	/** retorna la cadena de caracteres origen con espacios o caracteres 
	 * insertados segun la plantilla pasada como argumento. Todo caracter
	 * distinto a '#' es insertado en la posicion dentro del texto. Ej.:
	 * <code>org="n110430236"</code>
	 * <code>ms="#-########-#"</code>
	 * el resultado final es 'n-11043023-6'. Si la longitud de la mascara 
	 * es menor al valor del campo origen, es posible que el valor con 
	 * el formato aparezca truncado.
	 * 
	 * @param ms - plantilla o mascara
	 * @param org - secuencia a formatear
	 * @return cadena de caracteres con formato
	 */
	public static String mask(String ms, String org) {
		StringBuffer sbval = new StringBuffer(org);
		sbval.append("");
		for (int x = 0; x < ms.length() && x < sbval.length(); x++) {
			char c = ms.charAt(x);
			if (c != '#') {
				sbval.insert(x, c);
			}
		}
		return sbval.toString();
	}
	
	/** reemplaza las variables contenidas dentro del documento cuyo nombre
	 * es pasado como argumento. para ello, ejecuta <code>replaceHTMLVariables1</code>
	 * 
	 * @param dn - nombre del docuemto. 
	 * @param dta - <code>Hashtable</code> cuya clave es el nombre de variable
	 * 
	 * @return Secuencia con variables sustituidas.
	 */
	public static String replaceHTMLVariables(String dn, Hashtable dta) {
		String sdta = null;
		try {			
			URL url = ResourceUtilities.getURL(dn);
			BufferedInputStream bis = (BufferedInputStream) url.getContent();
			byte[] bdta = new byte[bis.available()];
			bis.read(bdta);
			sdta = replaceHTMLVariables1(new String(bdta), dta);
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
		return sdta;
	}
	
	/** reemplaza las variables contenidas dentro de un documento html con 
	 * los datos pasados como argumento. Las variables solo son elementos
	 * (tag) que coinciden con un nombre dado. <date>, <total> etc. Este
	 * metodo explora los datos y sustituye el nombre de variables por su valor
	 * correspondiente.
	 * 
	 * @param doc - cadena de caracteres con el contenido del documento.
	 * @param dta - <code>Hashtable</code> cuya clave es el nombre de variable
	 * 
	 * @return Secuencia con variables sustituidas.
	 */
	public static String replaceHTMLVariables1(String doc, Hashtable htd) {
		DateFormat dFormat = DateFormat.getDateInstance(DateFormat.LONG);
		String sdta = doc.toString();
		Enumeration en = htd.keys();
		while(en.hasMoreElements()) {
			String vn = (String) en.nextElement();
			Object va = htd.get(vn);
			if (vn.equals("<date>")) {
				sdta = sdta.replaceAll("<date>", dFormat.format((Date) va));
			} else {
				sdta = sdta.replaceAll(vn, va.toString());
			}
		}
		return sdta;
	}
	
	/** Retorna formato estandar para las direcciones largas. Si el campo
	 * obligatorio <code>adUrbani</code> esta vacio, retorna ""
	 * 
	 * @param adr - Registro con direccion a dar formato
	 * @return direccion
	 */
	public static String formatAddress(AddressRecord adr) {
		String dir = 
			"Urb. " + adr.getFieldValue("adUrbani") + " " +
			"calle " + adr.getFieldValue("adCalle") + " " +
			"Edf. " + adr.getFieldValue("adEdf") + ". " +
			adr.getFieldValue("adCiudad") + ", Edo. " + 
			adr.getFieldValue("adEstado") + ".";

		// verifica solo uno de los campos oblicatorios.
		if (((String) adr.getFieldValue("adUrbani")).equals("")) {
			dir = "";
		}
		return dir;			
	}
	
	/** retorna el identificador de aplicacion.
	 * 
	 * @return id de aplicacion.
	 */
	public static String getApplicationId() {
		Record rcd = 
			PrevalentSystemManager.getRecordFrom(new StockRecord(), "ar-i");
		return (String) rcd.getFieldValue("sAppId");
	}
	
	/** Desencripta el archivo pasado como argumento (generalmente un 
	 * documento XML) solo si no se esta en modalidad de desarrollo 
	 * 
	 * @param f - ubicacion del archivo
	 * @return - <code>Reader</code>
	 * @throws IOException
	 */
	public static char[] translate(File f) throws IOException {
		FileReader fr = new FileReader(f);
		int len = (int) f.length();
		char[] fch = new char[len];

		Translator.initialize(Translator.DECRYPT);
		for (int t = 0; t < len; t++) {
			fch[t] = development ? (char) fr.read() : 
				Translator.translate((char) fr.read());
		}
//		Logger.getLogger("").severe(new String(fch));
		return fch;
	}
	
	/** Crea la solicitud con extencion .htm y el .jar que sera 
	 * enviado para la solicitud de la clave de adquisicion. 
	 * El nombre ansoluto del archivo puede ser null. Si esto es cierto, se 
	 * crean archivos temporales. 
	 * 
	 * el .jar esta compuesto por los archivos
	 * - consumer: unico registro con: identificador de app; 
	 * fecha de solicitud; contenido del argumento cin; monto deposito; cuenta
	 * - clio.log
	 * 
	 * nota: se espera la variable <criptoid#> dentro de la lista. 
	 * 
	 * @param jn - nombre calificado del archivo. o <code>null</code>
	 * @param dta - datos de sustitucion para orden de compra.
	 * 
	 * @return arreglo de archivos {.html, .jar}
	 * 
	 */
	public static File[] makeConsumerFiles(String qfn, Hashtable dta) throws IOException {
		// temporales?
		if (qfn == null) {
			qfn = System.getProperty("java.io.tmpdir") + "request";
		}
		
		// orden de compra
		String sf = CoreUtilities.replaceHTMLVariables("RequestOrder.htm", dta);
		File htmf = new File(qfn + ".htm");
		htmf.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(
			new FileOutputStream(htmf));
		bos.write(sf.getBytes());
		bos.close();
		
		// archivo jar
		File jarf = new File(qfn + ".jar");
		jarf.createNewFile();
		String tfr =
			new AppDate().toString().substring(2) + ";" + 
			getApplicationId() + ";" + 
			(String) dta.get("<criptoid#>") + ";" + 
			(String) dta.get("<total>");
		FileOutputStream fos = new FileOutputStream(jarf);
		
		JarOutputStream jos = new JarOutputStream(fos);
		jos.putNextEntry(new ZipEntry("consumer.txt"));
		jos.write(tfr.getBytes());
		
		// propiedades
		Properties prp = System.getProperties();
		Enumeration e = prp.propertyNames();
		String pe = "";
		while (e.hasMoreElements()) {
			String k = (String) e.nextElement();
			pe += k + ": " + prp.getProperty(k) + "\n";
		}
		jos.putNextEntry(new ZipEntry("properties.txt"));
		jos.write(pe.getBytes());
		
		// log
		File fc = new File(ResourceUtilities.getLogFileName());
		FileInputStream fis = new FileInputStream(fc);
		jos.putNextEntry(new ZipEntry("clio.log"));
		byte[] b = new byte[(int) fc.length()];
		fis.read(b);
		jos.write(b);
		jos.close();
		
		return new File[] {htmf, jarf};
		
		/* 
			private File toTmpFile(String dta) throws IOException {
				String qfn = System.getProperty("java.io.tmpdir") + "request";
				File fil = null;
				fil = File.createTempFile("tuList", null);
				BufferedOutputStream bos =
					new BufferedOutputStream(new FileOutputStream(fil));
				bos.write(dta.getBytes());
				bos.close();
				return fil;
			}
			*/

		
	}
	
	/** extrae la clave de adquisiscion del archivo .jar enviado para tal fin
	 * 
	 * @param qjn - nombre absoluto del archivo
	 * @return - secuencia 
	 * 
	 * @throws IOException
	 */
	public static String getPurchaseKey(String qjn) throws IOException {
		JarFile jf = new JarFile(qjn);
		ZipEntry ze = jf.getEntry("producer");
		InputStream is = jf.getInputStream(ze);
		byte[] b = new byte[(int) ze.getSize()];
		is.read(b);
		is.close();
		return new String(b);
	}
	
	/** metodo encargado de verificar e instalar posibles ptf. toda entrada
	 * exepto "producer" sera considerada como archivo de ptf
	 * 
	 * Si este metodo detecta ptf, envia un mensaje
	 * indicando que se estan instalando. y luego finaliza la aplicacion.
	 * 
	 * @param qjn - nombre calificado del archivo .jar.
	 * @throws IOException
	 */
	public static void installPTF(String qjn) {
		try {
			JarFile jf = new JarFile(qjn);
			Enumeration e =jf.entries();
			boolean fl = false;
			while(e.hasMoreElements()) {
				ZipEntry ze = (ZipEntry) e.nextElement();
				String zefn = ze.getName();
				if (!zefn.equals("producer")) {
					if (!fl) {
						fl = true;
						Object[] options = {DIMain.bundle.getString("c27")};
						int o = JOptionPane.showOptionDialog(null, DIMain.bundle.getString("m24"),
						DIMain.bundle.getString("c12"),  
						JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,
						null, options, options[0]);
					}
					// instalacion
					InputStream is = jf.getInputStream(ze);
					byte[] b = new byte[(int) ze.getSize()];
					for (int x = 0; x < b.length; x++) {
						b[x] = (byte) is.read();
					}
					String sfn = System.getProperty("user.dir") + "/" + zefn;
					File fn = new File(sfn);
					if (fn.exists()) {
						fn.delete();
					}
					// crea nuevo por error cuando existe y se borra
					BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(new File(sfn)));
					bos.write(b);
					bos.close();
					is.close();
				}
			}
			if (fl) {
				Object[] options = {DIMain.bundle.getString("r18")};
				int o = JOptionPane.showOptionDialog(null, DIMain.bundle.getString("m25"),
				DIMain.bundle.getString("c12"),  
				JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]);
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
	}
	
	/** inserta el atributo<br> dentro de una secuencia de caracteres cada
	 * vez que se consiga un espacio en blanco despues de una longitud determinada
	 * Ej.: getInsertedBR("Arnaldo Fuentes", 3) retornara
	 * "Arnaldo<br> Fuentes"
	 * 
	 * @param str - secuancia 
	 * @param len - longitud
	 * @return secoencia con atributo insertado
	 */
	public static String getInsertedBR(String str, int len) {
		StringBuffer stt1 = new StringBuffer(str);
		int c = 0;
		boolean f = false;
		for (int x = 0; x < stt1.length(); x++) {
			f = (c > len && stt1.charAt(x) == ' ');
			if (f) {
				stt1.insert(x, "<br>");
				c = 0;
			} else {
				c++;
			}
		}
		return stt1.toString();
	}
	
	/** obtiene <code>String</code> desde <code>ResourceBundle</code>
	 * 
	 * @param arg - key
	 * @return <code>String</code> 
	 */
	public String getString(String arg) {
		return DIMain.bundle.getString(arg);
	}
	
	/** este metodo filtra los elementos de las secciones que comporenden
	 * una forma separando aquellas a las que se le puede asociar una entrada
	 * contable. los requisitos son:  
	 * - secciones cuyo atributo "class" sea "SimpleInput" y 
	 * "SectionValue" sea "Double" (para total) o Integer" (para cuenta).
	 * - que no contenga el atributo query="*no"
	 * 
	 * @param coid - identificador de forma
	 * @return <code>Vector</code>donde cada elemento es una instancacina
	 * de AppEntry(nombre de seccion, elemento)
	 * 
	 */
	public static Vector getAssociableSections(String coid) {
		Vector v = new Vector();
		Hashtable fe = PrevalentSystemManager.getFormElements(coid);
		Enumeration en = fe.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement(); 
			Element el = (Element) fe.get(key);
			String cls = el.getAttributeValue("class");
			String sev = el.getAttributeValue("sectionValue");
			String qry = el.getAttributeValue("query");
			if ((cls != null && cls.equals("SimpleInput")) &&
				(qry == null || !qry.equals("*no")) &&
				((sev != null && sev.equals("Double")) ||
				(sev != null && sev.equals("Integer")))) {
				v.add(new AppEntry(key, el));
			}
		}
		return v;
	}
	
	/**Retorna lista de las formas instaladas. 
	 * Para estos, se evalua si el contribuyente
	 * cumple con los requisitos. Si una forma es apropiada para este contribuyente,
	 * se incluye dentro de la lista
	 * 
	 * @param tpid - Identificador de contribuyente
	 * @return arreglo con los objetos de la apliacaion.
	 * 
	 */	
	public static AppEntry[] getFormsFor(String tpid) {
		Vector tmp = new Vector();
		TaxPayerRecord tpr = (TaxPayerRecord) 
		PrevalentSystemManager.getRecordFrom(new TaxPayerRecord(), tpid);
		AppEntry[] aif = ResourceUtilities.getInstalledForms();
		for (int fc = 0; fc < aif.length; fc++) {
			if (isSuitable(aif[fc], tpr)) {
				tmp.add(aif[fc]);
			}
		}
		return (AppEntry[]) tmp.toArray(new AppEntry[0]);
	}
	
	
}
