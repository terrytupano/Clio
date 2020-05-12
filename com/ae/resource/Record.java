/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import java.io.*;

/** esta clase represeta un registro de datos para la plicacion. Este es el 
 * origen primario de datos y de estructura de datos. Partiendo de aqui, las 
 * clases que implementan interfaces de modelos de datos tales colo 
 * ListModel o TableModel pueden consultar la informacion encapsulada aqui 
 * para dar forma a los datos presentados en las distintas vistas.
 *  
 */
public abstract class Record implements Serializable {

	private Field[] internalRecord;

	/** Nuevo registro.
	 * 
	 * @param flds - Arreglo de campos que forma el registro.
	 */
	public Record(Field[] flds) {
		this.internalRecord = flds;
	}
	/** Retorna la clase de cada campo de datos
	 *
	 */
	public Class getFieldClass(int fld) {
		return internalRecord[fld].getValue().getClass();
	}

	/** retorna el nombre (identificador) del campo que se encuentra en la
	 * posicion pasada como argumento
	 * 
	 * @param fld - indice de campo
	 * @return - id
	 */
	public String getFieldID(int fld) {
		return internalRecord[fld].getID();
	}

	/** Retorna el nro de columnas del campo
	 * 
	 * @param fld - campo
	 * @return - nro de columnas
	 */
	public int getFieldLength(int fld) {
		return internalRecord[fld].getLength();
	}

	/** retorna el nro de columnas (longitud) del campo
	 * 
	 * @param fld - Nombre del campo
	 * @return longitud
	 */
	public int getFieldLength(String fld) {
		int len = 0;
		for (int k = 0; k < internalRecord.length; k++) {
			if (internalRecord[k].getID().equals(fld)) {
				len = getFieldLength(k);
			}
		}
		return len;
	}

	/** retorna los nombres de los campos
	 *
	 */
	public String getFieldName(int fld) {
		return internalRecord[fld].getName();
	}
	/** retorn Nro de <code>Field</code> que conforman este registro
	 * 
	 * @return Numero de campos
	 */
	public int getFieldsCount() {
		return internalRecord.length;
	}
	/** Retorna el valor del campo que se encuentra en 
	 * la posicion fld dentro del registro interno
	 * 
	 * @param fld - posicion
	 * @return valor
	 */
	public Object getFieldValue(int fld) {
		return internalRecord[fld].getValue();
	}
	/** Retorna el valor del campo del campo cuyo nombre es fn
	 * 
	 * @param fn - Nombre del campo
	 * @return valor
	 */
	public Object getFieldValue(String fn) {
		Object val = null;
		for (int k = 0; k < internalRecord.length; k++) {
			if (internalRecord[k].getID().equals(fn)) {
				val = getFieldValue(k);
			}
		}
		return val;
	}
	/** retorna String con el valor clave del rejistro.
	 * Este metodo convierte los campos clave del registro en String. 
	 * Debe ser implementado por sub-clases para que <code>AppTableModel</code> 
	 * pueda localizar
	 * y ordenar los registro dentro de la tabla
	 * 
	 * @return Clave completa del registro
	 */
	public abstract String getKey();

	/** Metodo utilitiario que retorna la clave de un registro en una estructura
	 * tal que permite el correcto ordenamiento dentro de la tabla. No llame este
	 * metodo directamente. use solo <code>getKey()</code>. Si es necesario, el 
	 * registro se encarga de llamar a este metodo. 
	 * 
	 * @param flds - identificadores de campo que conforma la clave
	 * @return <code>String</code> con estructrura acorde al formato segun la 
	 * clave.
	 */
	protected String makeKey(int[] flds) {
		StringBuffer ksb =
			new StringBuffer(
				"                                                  "
					+ "                                                  "
					+ "                                                  ");
		int kl = 0;
		for (int k = 0; k < flds.length; k++) {
			ksb.insert(kl, getFieldValue(flds[k]).toString());
			kl += internalRecord[k].getLength();
		}
		return ksb.toString().substring(0, kl);
	}

	/** establece el valor <code>val</code> para el campo con id 
	 * <code>fld</code>
	 * 
	 * @param fld - posicion del campo
	 * @param val - valor
	 */
	public void setFieldValue(int fld, Object val) {
		internalRecord[fld].setValue(val);
	}
	/** Establece el valor <code>va</code> para el campo con nombre 
	 * <code>fn</code>
	 * 
	 * @param fn - Nombre del campo
	 * @param va - valor
	 */
	public void setFieldValue(String fn, Object va) {
		for (int k = 0; k < internalRecord.length; k++) {
			if (internalRecord[k].getID().equals(fn)) {
				setFieldValue(k, va);
			}
		}
	}

	/** determina si se desea presentar la columna col.
	 * <code>AppTableModel</code> consulta esta metodo para realizar multiples 
	 * calculos entre ellos para determinar 
	 * que columnas desea este registro que se presenten. Este metodo evita el tener que implementar
	 * la interface TableColumnModel.
	 */
	public abstract boolean showInList(int col);

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getKey();
	}

}