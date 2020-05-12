/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.util.*;
import java.io.*;

/** Implementacion de <code>Map.Entry</code> para uso de la aplicacion.
 * 
 */
public class AppEntry implements Map.Entry, Serializable {
	
	private Object key;
	private Object value;
	
	/** Nueva entrada.
	 * 
	 * @param k - clave
	 * @param v - valor
	 */
	public AppEntry(Object k, Object v) {
		this.key = k;
		this.value = v;
	}
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Map.Entry))
			return false;
		Map.Entry e = (Map.Entry)o;
		return key.equals(e.getKey()) && value.equals(e.getValue());
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#getKey()
	 */
	public Object getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#getValue()
	 */
	public Object getValue() {
		return value;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int keyHash = (key==null ? 0 : key.hashCode());
		int valueHash = (value==null ? 0 : value.hashCode());
		return keyHash ^ valueHash;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#setValue(java.lang.Object)
	 */
	public Object setValue(Object arg0) {
		Object oldValue = this.value;
		this.value = arg0;
		return oldValue;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return value.toString();
	}
	
}
