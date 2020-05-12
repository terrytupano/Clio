/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import java.io.*;
import com.ae.core.*;

/** Representa un campo o variable dentro de un registro. <code>Field</code>
 * es la unidad minima de informacion dentro de un registro y uno o mas de 
 * estos conforman toda la informacion contenida dentro de un registro
 */
public class Field implements Serializable {
	private String id;
	private String shortName;
	private int length;
	private Object value;

	/** Nuevo campo.
	 * 
	 * @param id - identificador interno
	 * @param snam - descripcion corta. generalmente usada como descripcion de
	 * columna dentro de una tabla o lista. puede ser null
	 * @param lnam - descripcion larga. Generalmete usada como descripcion de 
	 * campo de entrada
	 * @param len longitud maxima de los datos de entrada
	 * @param val contenido
	 */	
	public Field(String id, String snam, int len, Object val) {
		this.id = id;
		this.shortName = snam;
		this.length = len;
		this.value = val;
	}
	/** retorn identificador de campo
	 * 
	 * @return identificador
	 */
	public String getID() {
		return id;
	}
	/** retorna nombre corto
	 * 
	 * @return nombre corto
	 */
	public String getName() {
		return shortName == null ? "" : DIMain.bundle.getString(shortName);		
	}
	/** retora valor.
	 * 
	 * @return valor
	 */
	public Object getValue() {
		return value;		
	}
	/** retorn longitud maxima de entrada
	 * 
	 * @return longitud
	 */
	public int getLength() {
		return length;
	}
	/** establece nuevo valor para este campo
	 * 
	 * @param val nuevo valor
	 */
	public void setValue(Object val) {
		value = val;
	}
}