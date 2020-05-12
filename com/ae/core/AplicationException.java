/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;
import java.util.logging.*;
import java.awt.*;
import java.lang.reflect.*;
import javax.swing.*;
import com.ae.resource.*;

/** AplicationExection es la super clase de la cual todas las execpciones de 
 * la aplicacion se extienden. estas puedes ser arrojadas durante la ejecucion 
 * de la aplicacion y no deberian denetern la misma. cada tipo de expecion 
 * tiene un tiempo de presentacion por omision. usar el metodo setMiliSeconds
 * para alterar el tiempo de presentacion. 
 * 
 * 
 */
public abstract class AplicationException extends RuntimeException implements Cloneable {
	public static String ACTION = "action";
	public static String CRITICAL = "critical";
	public static String INFORMATION = "information";
	public static String WARNING = "warning";
	public static int SHORT = 4000;
	public static int A_WHILE = 10000;
	public static int FOR_EVER = 999000;
	
	private Color exceptionColor;
	private int milis;
	private ImageIcon exceptionIcon;
	private String message;
	private String eType;
	

	/** nueva instancia
	 * 
	 * @param msg - id de texto de excepcion
	 * @param et - tipo 
	 * @param t - tiempo medido en segundos 
	 */
	public AplicationException(String msg, String et) {
		super();
		this.message = msg;
		this.eType = et;
		this.exceptionIcon = ResourceUtilities.getSmallIcon(eType);
		this.exceptionColor = new Color(230,230,255);
		this.milis = SHORT;
		if (eType.equals(WARNING)) { 
			exceptionColor = new Color(255,255,220);
			this.milis = A_WHILE;
		}
		if (eType.equals(ACTION)) { 
			exceptionColor = new Color(255,220,230);
			this.milis = FOR_EVER;
		}
		if (eType.equals(CRITICAL)) { 
			exceptionColor = new Color(255,190,230);
			this.milis = FOR_EVER;
		}
	}
	
	/** Retorna el color asociado al tipo de excepcion
	 * 
	 * @return color
	 */
	public Color getExceptionColor() {
		return exceptionColor;
	}
	
	/** retorna icono para esta excepcion 
	 * 
	 * @return icono
	 */
	public ImageIcon getExceptionIcon() {
		return exceptionIcon;
	}

	/*
	 *  (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */	
	public String getMessage() {
		return message;
	}
	
	/** Establece un nuevo mensaje de texto para esta exception
	 * 
	 * @param msg - mensaje
	 */
	public void setMessage(String msg) {
		this.message = msg;
	}
	
	/** establece el tiempo de duracion para esta exepcion.
	 * 
	 * @param nt - nuevo timempo.
	 */
	public void setMiliSeconds(int nt) {
		this.milis = nt;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Class cls = getClass();
		Object obj = null;
		try {
			Constructor con = cls.getConstructor(new Class[] {String.class});
			obj = con.newInstance(new Object[] {eType});
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
		return obj;	
	}

	/** retorna el tiempo que se espera se muestre esta exepcion.
	 * 
	 * @return tiempo en seg.
	 */	
	public int getMiliSeconds() {
		return milis;
	}
}