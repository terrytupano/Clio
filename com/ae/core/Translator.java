/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

/** Clase encargada del cifrado/decifrado de una secuencia de caracteres. El
 * objetivo general es solo "revolver" los caracteres para evitar intromision
 * en el codigo y datos de la aplicaciom. Para ello dos secuencias con los
 * caracteres que se desean transladar (numeros y letras) se colocan uno 
 * bajo otro. y para cada caracter encontrado en la superior es traducida por
 * el caracter que se encuentre en la inferior. luego de esto, la secuencia 
 * inferior se desplaza una posicion ha cia la derecha o izquierda (segun la 
 * modalidad). Esto debe ser suficiente para desalentar a los intrusos
 * 
 */
public abstract class Translator {
	
	private static StringBuffer ucSup, ucInf, lcSup, lcInf, secInf, secSup, secBus;
	private static int mode;
	
	public static int ENCRYPT = 0;
	public static int DECRYPT = 35; 

	/** inicializa la modalidad de cifrado. Este metodo debe llamarse
	 * solo una vez antes de comenzar el proceso de traduccion.
	 * 
	 * @param m
	 */	
	public static void initialize(int m) {
		mode = m;
		//ojo con ultimas 6 letras (ver translateKey())
		ucSup = new StringBuffer("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZÑÁÉÍÓÚ");
		ucInf = new StringBuffer("ABCDEFGHIJKLMNOP0Q1R2S3T4U5V6W7X8Y9ZÑÁÉÍÓÚ");
		lcSup = new StringBuffer("abcdefghijklmñnopqrstuvwxyzáéíóú");  
		lcInf = new StringBuffer("qwertyuiopasdfghjklñzxcvbnmáéíóú");
		secSup = secInf = secBus = null;
	}
	
	/** retorna la traduccion del caracter pasado como argumento
	 * 
	 * @param ch - caracter
	 * @return - traduccion
	 */
	public static char translate(char ch) {
		char rch = ch;
		if (Character.isLetterOrDigit(ch)) {
			secInf = (Character.isLowerCase(ch)) ? lcInf : ucInf;
			secSup = (Character.isLowerCase(ch)) ? lcSup : ucSup;
			
			// Ajuste de secuencia inferior
			char s = secInf.charAt(0);
			secInf.append(s);
			secInf.deleteCharAt(0);
			
			// intercambia el origen > destino de traduccion
			if (mode == DECRYPT) {
				StringBuffer sb = secSup;
				secSup = secInf;
				secInf = sb;
			}
			
			// busqueda de posicion. 
			int idx = 0;
			for (int p = 0; p < secSup.length(); p++) {
				if (secSup.charAt(p) == ch) {
					idx = p;
					break;
				}
			}
			rch = secInf.charAt(idx);
		}
		 return rch;
	}

	/** traduce una secuencia de caracteres.
	 * 
	 * @param src - origen de traduccion.
	 * @return secuencia traducida
	 */
	public static String translate(String src) {
		char[] tch = src.toCharArray();
		for (int x = 0; x < src.length(); x++) {
			tch[x] = translate(tch[x]);
		}
		return new String(tch);
	}
	
	/** traduce clave de adquisicion. Este metodo solo debe ser usuado para
	 * la clave de adquisicion ya que:
	 * - convierte la secuencia a mayscula cuando encripta y minuscula cuando desencripta
	 * - elimina las ultimas 6 letras de las secuencias mayusculas/numeros para evitar encriptado
	 * con acentos ni Ñ
	 * (modificar para que adicione espacios en blanco cada 4 caracteres)
	 * 
	 * @param key
	 * @return
	 */
	public static String translateKey(String key) {
		ucSup.delete(ucSup.length() - 6, ucSup.length());		
		ucInf.delete(ucInf.length() - 6, ucInf.length());		
		key = key.toUpperCase();
		return (mode == ENCRYPT) ? translate(key) : translate(key).toLowerCase() ;
	}
}
