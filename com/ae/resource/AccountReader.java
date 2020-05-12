/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import java.io.*;
import java.nio.*;
import java.text.*;
import java.util.*;

import com.ae.core.*;

/** Clase donde se encapsula toda la logica para la lectura de datos contables
 * de las aplicaciones soportadas. esta clase se encarga de interpretar los 
 * formatos de las distintas aplicaciones y retornarlos en un formato único
 *   
 */
public class AccountReader {
	
	// como 731581 representa el 01/01/2004 entonces, a la cantidad 
	// leida le adiciono la diferencia entre cantidad - 731581 que 
	// representa el numero de dias desde el 01/01/2004 para obtener la
	// fecha verdadera
	private static int A2_FIRST_DAY = 731581; 

	private static TreeMap a2cont160Accounts(String f, String patt)	throws IOException {
		char[] rcd = new char[658];
		TreeMap tm = new TreeMap();
		FileReader fr = new FileReader(f);
		// encabezado descartable
		fr.read(new char[46985]);
		while (fr.read(rcd) != -1) {
			String srcd = new String(rcd);
			String nam = srcd.substring(32, 79).trim();
			String acc = srcd.substring(0, 31).trim();
			acc = CoreUtilities.mask(patt, acc);
			tm.put(acc, nam);
		}
		return tm;
	}
	
	/** debido a que en esta aplicacion cada compañia tiene su propia mascara,
	 * se separa en nombre de la ultima carpeta del resto, se localiza el 
	 * archivo con informacion de mascara para compañias y se usa la mascara
	 * que se encuentre en el mismo registro que contenga el nombre de la ultima 
	 * carpeta. esto es porque los datos pueden estar almacenados en carpetas
	 * con nombres distinto al codigo de la compañia.
	 * 
	 * @param dir - directorio origen de datos
	 * @return - mascara
	 * @throws IOException
	 */
	private static String a2cont160Mask(String dir) throws IOException {
		int lfs = dir.lastIndexOf(File.separator) + 1;
		String cod = dir.substring(lfs, dir.length() - 1);
		cod = cod.toLowerCase();
		String dir1 =  dir.substring(0, lfs);
		String mask = "";

		char[] rcd = new char[4465];
		FileReader fr = new FileReader(dir1 + "DataSistema/a2CEmpresas.DAT");
		// basura
		fr.read(new char[83081]);
		while (fr.read(rcd) > -1) {
			String rc = (new String(rcd, 1086, 60)).trim();
			rc = rc.toLowerCase();
//			String rc = (new String(rcd, 0, 30)).trim();
			if (rc.startsWith(cod)) {
				mask = (new String(rcd, 776, 53)).trim();
			}
		}
		return mask.replace('9', '#');
	}
	
	private static TreeMap a2cont160Voucher(String f, String msk, String ac) throws 
		IOException, ParseException {
		byte[] rcd = new byte[484];
		TreeMap tm = new TreeMap();
		int cnt = 1;
		FileInputStream fis = new FileInputStream(f);
		fis.skip(25481);
		GregorianCalendar gc = new GregorianCalendar();
		while (fis.read(rcd) == rcd.length) {
			String srcd = new String(rcd);
			String codcta = srcd.substring(53, 75).trim();
			codcta = CoreUtilities.mask(msk, codcta);
			if (codcta.equals(ac)) {
				Object[] fdm = new Object[3];
				// fecha. ver comentario en constante A2_FIRST_DAY
				ByteBuffer bb = ByteBuffer.wrap(rcd, 32, 36);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				gc.set(GregorianCalendar.YEAR, 2004);
				gc.set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY);
				gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
				gc.add(GregorianCalendar.DAY_OF_YEAR, bb.getInt() - A2_FIRST_DAY);
				fdm[0] = new AppDate(gc.getTime());
				//descripcion
				fdm[1] = srcd.substring(213, 273).trim();
				//monto
				ByteBuffer bbd = ByteBuffer.wrap(rcd, 274, 8);
				bbd.order(ByteOrder.LITTLE_ENDIAN);
				double mto = bbd.getDouble(); 
				if (mto == 0) {
					ByteBuffer bbh = ByteBuffer.wrap(rcd, 283, 8);
					bbh.order(ByteOrder.LITTLE_ENDIAN);
					mto = bbh.getDouble(); 
				}
				fdm[2] = new Double(mto);
				tm.put(new Integer(cnt++), fdm); 
			}
		}
		return tm;
	}	
	
	/** retorna lista de directorio de los periodos. creado para infocont 5.46
	 * que crea un directorio por periodo llamado ai-af donde ai = año inicial
	 * y af año final
	 * 
	 * @param dir directorio a buscar
	 * @return treemap cuya clave y valor son ai-af 
	 * 
	 */
	private static TreeMap getDirs(String dir) {
		File diro = new File(dir);
		TreeMap t = new TreeMap();
		String[] sdir = diro.list();
		for (int f = 0; f < sdir.length; f++) {
			File fi = new File(sdir[f]);
			String sfi = fi.getName();
			if (sfi.charAt(2) == '-') {
				t.put(sfi, sfi);
			}
		}
		return t;
	}
	
	/** lectura de catalogo de cuenta de infocont. dado que existen distintos
	 * directorios por periodos se localiza el ultimo y se extraen de alli
	 * las cuentas. 
	 * 
	 * @param dir - directorio raiz
	 * @param msk - mascara
	 * @return - cuentas contables
	 * @throws IOException
	 */
	private static TreeMap infocont546Accounts(String dir) throws IOException {
		String accf = dir + ((String) getDirs(dir).lastKey()) + 
			"/DICTACON/Cuentaco.db";
		FileReader fr = new FileReader(accf);

		// mascara y longitud de cuenta
		Object[] l_m = infocont546Mask(dir);
		int accl = ((Integer) l_m[0]).intValue();
		String msk = (String) l_m[1];
		
		// cada 11 registro de longitud 166 + accl esta un registro ???
		// por ella, se lee la longitud de ese registro y se este no termina
		// en siertos caracteres => que es un registro a tomar en cuenta
		
//		char[] rcd = new char[166 + accl];
		char[] rcd = new char[101];
		int dif = (166 + accl) - 101;
		TreeMap tm = new TreeMap();
		fr.skip(2054);
		while (fr.read(rcd) != -1) {
			if (!(rcd[100] == 6 || rcd[100] == 3)) {
				String srcd = new String(rcd);
				String acc = srcd.substring(0, accl).trim();
				String nam = srcd.substring(accl, accl + 40).trim();
				acc = CoreUtilities.mask(msk, acc);
				tm.put(acc, nam);
				fr.skip(dif);
			}
		}
		return tm;
	}
	
	/** localiza los niveles de las cuentas en el archivo que describe el 
	 * catalogo. con los niveles y sus longitudes se contruye la mascara
	 * para las cuentas. diceñado para infocontab 5.46
	 * 
	 * @param dir - directorio donde se encuentra la empresa
	 * @return - arreglo de objetos donde: Object[0] = Integer con logitud del
	 * codigo de cuenta contables Object[1] = mascara
	 * @throws IOException
	 */
	private static Object[] infocont546Mask(String dir) throws IOException {
		FileReader fr = new FileReader(dir + "dianocon/anoconta.db");
		char[] rcd = new char[10];
		fr.skip(4629);
		fr.read(rcd);
		String p = "##########";
		String m = "";
		int len = 0;
		for (int r = 0; r < 10; r++) {
			int val = (int) rcd[r];
			if (val > 0) {
				len += val;
				m += p.substring(0, val) + ".";
			}
		}
		m = m.substring(0, m.length() - 1);
		return new Object[] {new Integer(len), m};
	}
	private static TreeMap infocont546Voucher(String dir, String ac) throws IOException {
		TreeMap tm = new TreeMap();
		TreeMap tmd = getDirs(dir);
		// mascara y longitud de cuenta
		Object[] l_m = infocont546Mask(dir);
		int accl = ((Integer) l_m[0]).intValue();
		String msk = (String) l_m[1];

		// cada 11 registro de longitud 166 + accl esta un registro de 97 chr
		// por ella, se lee la longitud de ese registro y se este no termina
		// en siertos caracteres => que es un registro a tomar en cuenta
		
//		char[] rcd = new char[166 + accl];
		byte[] rcd1 = new byte[88]; 
		byte[] rcd2 = new byte[245 - 88];
		Vector vec = new Vector(tmd.values());
		for (int v = 0; v < vec.size(); v++) {
			FileInputStream fis = new FileInputStream(dir + 
				(String) vec.elementAt(v) + "/DICOMCON/asientoc.db");
			int cnt = 1;
			fis.skip(2054);
			while (fis.read(rcd1) > 0) {
				if (rcd1[0] == 0) {
					continue;
				}
				fis.read(rcd2);
				String srcd1 = new String(rcd1);
				String srcd2 = new String(rcd2);
				String codcta = CoreUtilities.mask(msk, srcd1.substring(17, 17 + accl).trim()); 
				if (codcta.equals(ac)) {
					Object[] fdm = new Object[3];
					// fecha. si len = 0 no ha sido contabilizado				
					String f = srcd2.substring(22 + accl, 22 + accl + 8).trim();
					if (f.length() == 0) {
						continue;
					}
					fdm[0] = new AppDate(f);
					//descripcion
					fdm[1] = srcd1.substring(47 + accl).trim();
					System.out.println(srcd1.substring(47 + accl).trim());
					//monto
					ByteBuffer bb = ByteBuffer.wrap(rcd1, 18 + accl, 8);
					fdm[2] = new Double(Math.abs(bb.getDouble()));
					tm.put(new Integer(cnt++), fdm); 
				}
			}
		}
		return tm;
	}
	
	/** Remueve todo caracter que no sea digito o letra de derecha a
	 * izquierda 
	 * 
	 * @param s - secuencia 
	 */
	private static String onlyLetterOrDigit(String s) {
		char[] ch = s.toCharArray();
		for (int x = ch.length; !Character.isLetterOrDigit(ch[x - 1]); x--) {
			ch[x - 1] =  ' '; 
		}
		return (new String(ch)).trim();
	}	
	
	private static TreeMap psoft50Accounts(String f, String patt)	throws IOException {
		char[] rcd = new char[295];
		TreeMap tm = new TreeMap();
		FileReader fr = new FileReader(f);
		// encabezado descartable
		fr.read(new char[967]);
		while (fr.read(rcd) != -1) {
			String srcd = new String(rcd);
			String acc = onlyLetterOrDigit(srcd.substring(0, 19).trim());
			String nam = srcd.substring(20, 79).trim();
			tm.put(acc, nam);
		}
		return tm;
	}

	private static TreeMap psoft50Voucher(String f, String ac) throws 
		IOException, ParseException {
		char[] rcd = new char[235];
		TreeMap tm = new TreeMap();
		int cnt = 1;
		FileReader fr = new FileReader(f);
		fr.read(new char[552]);
		while (fr.read(rcd) == rcd.length) {
			String srcd = new String(rcd);
			String codcta = onlyLetterOrDigit(srcd.substring(21, 40).trim());
			if (codcta.equals(ac)) {
				Object[] fdm = new Object[3];
				// fecha					
				fdm[0] = new AppDate(srcd.substring(159, 163), 
					srcd.substring(163, 165), srcd.substring(165, 167));
				//descripcion
				fdm[1] = srcd.substring(41, 71).trim();
				//monto
				String md = srcd.substring(121, 140).trim();
				String mh = srcd.substring(140, 159).trim();
				Number mnd = NumberFormat.getInstance().parse(md);
				Number mnh = NumberFormat.getInstance().parse(mh);
				Double dob = new Double(mnd.doubleValue());
				if (mnh.doubleValue() != 0) {
					dob = new Double(mnh.doubleValue() * -1);
				}
				fdm[2] = dob;
				tm.put(new Integer(cnt++), fdm); 
			}
		}
		return tm;
	}
	
	/** retornal la mascara o plantilla por la cual, la aplicacion contable
	 * da formato a sus cuentas. La mascara retornada esta lista para su 
	 * uso mediante el metodo <code>FormUtilities.mask(String, String)</code> 
	 * 
	 * @param at - id de aplicacion
	 * @param dir - directorio 
	 * @return mascara
	 */
	public static String readAccountMask(String ida, String dir) throws IOException {
		String tm = "";
		int rl = 0, sp = 0, ep = 0;
		String fn = "";
		char fc = 'X';
		boolean flag = true;
		if (ida.equals("saint45")) {
			rl = 678;
			sp = 34;
			ep = 22;
			fn = dir + "scconf.dat";
			fc = 'X';
		}
		if (ida.equals("psoft50")) {
			rl = 1114;
			sp = 842;
			ep = 19;
			fn = dir + "ctacar.dbf";
			fc = '9';
		}
		if (ida.equals("a2cont160")) {
			tm = a2cont160Mask(dir);
			flag = false;
		}
		if (ida.equals("infocont546")) {
			tm = (String) infocont546Mask(dir)[1];
			flag = false;
		}
		
		if (flag) {
			char[] rcd = new char[rl];
			FileReader fr = new FileReader(fn);
			fr.read(rcd);
			tm = (new String(rcd, sp, ep)).trim();
			tm = tm.replace(fc, '#');
		}
		return tm;
	}
	
	/** retorna <code>Vector</code> con las cuentas contables extradidas del
	 * archivo de catalogo de cuentas de la aplicacion solicitada. La lista
	 * esta ordenada por codigo de cuenta contable. La estructura de este vector
	 * esta dispuesta para ser usada como el primer argumento del constructor
	 * <code>JTable(Vector, Vector)</code> 
	 * 
	 * @param at - id de la plicacion
	 * @param dir - directorio donde se encuentran los datos a importar.
	 * @return arreglo de vectores. Vector[0] datos, Vector[1] nombre de columnas
	 */
	public static Vector[] readAccounts(String at, String dir) throws IOException {
		TreeMap tm = null;
		Vector cn = new Vector(2);
		cn.add(DIMain.bundle.getString("c19"));
		cn.add(DIMain.bundle.getString("d02"));
		if (at.equals("saint45")) {
			tm = saint45Accounts(dir + "sccta.dat", readAccountMask(at, dir));
		}
		if (at.equals("psoft50")) {
			tm = psoft50Accounts(dir + "ctacont.dbf", readAccountMask(at, dir));
		}
		if (at.equals("a2cont160")) {
			tm = a2cont160Accounts(dir + "data/a2CCuentas.DAT", readAccountMask(at, dir));
		}
		if (at.equals("infocont546")) {
			tm = infocont546Accounts(dir);
		}
		
		// de tremap a vector
		Vector rd = new Vector();
		Iterator it = tm.keySet().iterator();
		while(it.hasNext()) {
			Vector rd_x = new Vector();
			Object k = it.next();
			rd_x.add(k);
			rd_x.add(tm.get(k));
			rd.add(rd_x);
		}
		return new Vector[] {rd, cn};
	}
	
	/** Este metodo retorna un <code>TreeMap</code> con los movimientos contables
	 * encontrados para la cuenta pasada como argumento. La clave es una
	 * instancia de <code>Integer</code> con el nro consecutivo de entrada. 
	 * el valor es un arreglo de tres objetos donde:
	 * - obj[0] - fecha de la entrada - instancia de <code>AppDate</code>
	 * - obj[1] - descripcion del movimiento
	 * - obj[2] - monto. instancia de <code>Double</code>
	 * 
	 * @param at - identificador de aplicacion 
	 * @param dir - directorio donde se encuentran los daton
	 * @param idi - directorio desde donde se importo la cuenta contables
	 * @param ac - cuenta contable para la cual buscar mov.  
	 * @return - lista de mov.
	 */
	public static TreeMap readVouchers(String at, String dir, String ac)
		throws IOException, ParseException {
		TreeMap tm = null;
		String msk = readAccountMask(at, dir);
		if (at.equals("saint45")) {
			tm = saint45Voucher("c:/o01/Scmov.txt", msk, ac);
		}
		if (at.equals("psoft50")) {
			tm = psoft50Voucher(dir + "compmov.dbf", ac);
		}
		if (at.equals("a2cont160")) {
			tm = a2cont160Voucher(dir + "data/a2CCompDiarioMov.DAT", msk, ac);
		}
		if (at.equals("infocont546")) {
			tm = infocont546Voucher(dir, ac);
		}
		return tm;
	}
		
	private static TreeMap saint45Accounts(String f, String patt)	throws IOException {
		char[] rcd = new char[1107];
		TreeMap tm = new TreeMap();
		FileReader fr = new FileReader(f);
		// encabezado descartable
		fr.read(new char[1112]);
		while (fr.read(rcd) != -1) {
			String srcd = new String(rcd);
			String acc = srcd.substring(0, 22).trim();
			String nam = srcd.substring(23, 70).trim();
			acc = CoreUtilities.mask(patt, acc);
			tm.put(acc, nam);
		}
		return tm;
	}

	/** reemplaza los caracteres de control por espacios en blanco.
	 * posibles problemas al adicionar datos con estos caracteresn dentro
	 * de objetos
	 * 
	 * @param rcd registro
	 * @return rcd con caracteres reemplazados
	private static char[] replaceControlCharacter(char[] rc) {
		for (int x = 0; x < rc.length; x++) {
			rc[x] = (!Character.isJavaIdentifierPart(rc[x])) ? 
				(char) Character.DIRECTIONALITY_WHITESPACE : 
				rc[x];
		}
		return rc;
	}
	 */

	/** lee y retorna movimientos de archivo exportado mediante la 
	 * opcion de conversion "archivos a formato ascii" del menu varios.
	 * 
	 * @param f - nombre calificado del archivo exportado
	 * @param msk - mascara para cuenta contables
	 * @param ac - cuenta contable de la que se extraen movimientos
	 * @return lista con movimientos
	 * @throws IOException
	 * @throws ParseException
	 *   
	 */
	private static TreeMap saint45Voucher(String f, String msk, String ac) throws 
		IOException, ParseException {
		NumberFormat numf = NumberFormat.getInstance(new Locale("es", "VE"));	
//		char[] rcd = new char[241];
		char[] rcd = new char[196];
		TreeMap tm = new TreeMap();
		int cnt = 1;
		FileReader fr = new FileReader(f);
//		fr.read(new char[245]);
		while (fr.read(rcd) != -1) {
			String srcd = new String(rcd);
			String codcta = srcd.substring(9, 30).trim();
			codcta = CoreUtilities.mask(msk, codcta);
			if (codcta.equals(ac)) {
				Object[] fdm = new Object[3];
				// fecha
				fdm[0] = new AppDate(srcd.substring(36, 40), 
					srcd.substring(34, 36), srcd.substring(32, 34)); 
				//descripcion
				fdm[1] = srcd.substring(52, 92).trim();
					 
				//monto
				Number num = numf.parse(srcd.substring(170, 189).trim());
				double d = num.doubleValue();
				d = (d < 0) ? d * -1 : d; 
				fdm[2] = new Double(d);
				tm.put(new Integer(cnt++), fdm);
				
				// por si algun dia lo descubro :(
				/*
			String codcta = srcd.substring(1, 23).trim();
			codcta = CoreUtilities.mask(msk, codcta);
			if (codcta.equals(ac) && rcd[240] != 256) {
				Object[] fdm = new Object[3];
				// fecha					
				fdm[0] = new AppDate(srcd.substring(171, 175), 
					srcd.substring(169, 171), srcd.substring(167, 169)); 
				//descripcion
				int len = rcd[43];
				fdm[1] = srcd.substring(44, 44 + len);
					 
				//monto
				String md1 = srcd.substring(180, 188);
				String md2 = srcd.substring(181, 189);

				ByteBuffer bb = ByteBuffer.wrap(md2.getBytes());
				bb.order(ByteOrder.LITTLE_ENDIAN);
				System.out.println(fdm[1] + md2);

				fdm[2] = new Double(bb.getDouble());
				tm.put(new Integer(cnt++), fdm);
				*/
			}
		}
		return tm;
	}
}
