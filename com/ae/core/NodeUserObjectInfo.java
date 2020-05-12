/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.util.logging.*;

import javax.swing.*;

import com.ae.resource.*;

/** Encapsula la informacion  de las clases que son opciones accesibles 
 * para el usuario y que son colocadas como una opcion dentro del arbol controlado
 * por <code>LeftComponentManager</code>
 *  
 */
public class NodeUserObjectInfo {
	
	private String name;
	private Class rightcmp;
	private ImageIcon icon;

	/** Nueva instacia de este objeto cuando el nodo representa un espacio entre
	 * grupo de opciones
	 *
	 */
	public NodeUserObjectInfo() {
		this.name = " ";
		this.rightcmp = null;
		this.icon = null;
	}
	/** Crea nueva instancia. Constructor debe ser usado cuando el node representa 
	 * una agrupacion logica de opciones.
	 * 
	 * @param title
	 */
	public NodeUserObjectInfo(String title) {
		this();
		this.name = title;
	}
	/** crea nueva instancia de este objeto. 
	 * Este constructor debe ser usado cuando el nodo contiene informacion 
	 * sobre una sub-clases de RightPane. 
	 * 
	 * @param nam nombre de la clase
	 * @param rc nombre de clase a instanciar
	 * @param ico nombre del icono
	 */	
	public NodeUserObjectInfo(String nam, String rc, String ico) {
		this();
		this.name = nam;
		try {
			this.rightcmp = (rc == null) ? null : 
//			Class.forName(rc);
				Class.forName(DIMain.bundle.getString(rc));
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
		this.icon = ResourceUtilities.getSmallIcon(ico);
	}
	/** retorna nombre
	 * 
	 * @return nombre
	 */
	public String getName() {
		return name;
	}
	/** retorna clase del componente
	 * 
	 * @return clase
	 */
	public Class getRightComponentClass() {
		return rightcmp;
	}
	/** retorna icon
	 * 
	 * @return icono
	 */
	public Icon getIcon() {
		return icon;
	}
}
