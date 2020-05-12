/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.imageio.*;

import org.jdom.*;

/** Determina el espacio ocupado por las secciones dentro de una forma. 
 * Esta busca los atributos 
 * <code>xPos, yPos, width, height o point</code>
 * para asi determinar las dimensiones de la superficie visible de la seccion
 * para <code>xPos, yPos, width, height</code> se retorna el rectangulo 
 * que indican.
 * para <code>point</code> se realiza una exploracion de la imagen para determinar
 * el area. La exploracion traza una cruz buscando un color distinto al 
 * transparente; retornando el resultado de la busqueda en forma de rectangulo.
 * ninguno de los atributos estan presentes, se retorna un rectangulo 
 * ubicado en 0,0 y con dimension 0,0; se presume que es una seccion tipo 
 * variable.
 * para <code>union</code> se realiza la exploracion de la imagen y se computa el
 * rectangulo que une los puntos dados como argumento. 
 * 
 */
public class SectionSurface {
	
	private BufferedImage[] buff_img;
	private Hashtable f_elements;
	
	/** nueva instancia de esta clase
	 * 
	 * @param fn - Nombre de la forma
	 * @param fa - cara de la forma. <code>Form.OVERSE O Form.REVERSE</code> 
	 */
	public SectionSurface(String fn) {
		try {
			this.f_elements = PrevalentSystemManager.getFormElements(fn);
			String[] face = {"overse.image", "reverse.image"};
			this. buff_img = new BufferedImage[2];
			for (int f = 0; f < 2; f++) {
				Element elm = (Element) f_elements.get(face[f]);
				if (elm != null) {
					String gif = elm.getAttributeValue("name");
					File fil = new File(ResourceUtilities.getPluginsPath() + gif + ".gif");
					this.buff_img[f] = ImageIO.read(fil);
				}
			}
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage() + "(" + fn + ")", e);
		}
	}

	/** retorna el rectangulo que debe ocupar la seccion identificada como 
	 * <code>sid</code>. Este metodo examina los atributos 
	 * <code>point, xPos, yPos, union</code> para determina el tipo de calculo
	 * que debe realizarse.
	 * 
	 * @param sid - identificador de seccion
	 * @return rectangulo donde el componenten visible de la secion debe ser 
	 * colocado.
	 */	
	public Rectangle getSectionSurface(String sid) {
		Rectangle rect = new Rectangle();
		Element elm = (Element) f_elements.get(sid);
		int pag = 0;
		if (elm.getName().startsWith("reverse")) {
			pag = 1;
		}
		BufferedImage buff_img1 = buff_img[pag];
		try {
			
			// punto
			String atv = elm.getAttributeValue("point");
			if (atv != null) {
				String[] sp = atv.split(";");
				Point p = new Point(Integer.parseInt(sp[0]), Integer.parseInt(sp[1]));
				rect = getRectangle(buff_img1, p);	
			}
			
			// union
			atv = elm.getAttributeValue("union");
			if (atv != null) {
				String[] sp = atv.split(";");
				Point p1 = new Point(Integer.parseInt(sp[0]), Integer.parseInt(sp[1]));
				Rectangle r1 = getRectangle(buff_img1, p1);	
				Point p2 = new Point(Integer.parseInt(sp[2]), Integer.parseInt(sp[3]));
				Rectangle r2 = getRectangle(buff_img1, p2);
				rect = new Rectangle(r1.x, r1.y, (r2.x + r2.width) - r1.x, (r2.y + r2.height) - r1.y);	
			}
			
			//rectangulo
			if (elm.getAttributeValue("xPos") != null) {
				rect = new Rectangle(
					Integer.parseInt((String) elm.getAttributeValue("xPos")), 	
					Integer.parseInt((String) elm.getAttributeValue("yPos")),	
					Integer.parseInt((String) elm.getAttributeValue("width")), 	
					Integer.parseInt((String) elm.getAttributeValue("height")));
					return rect; 
			} 
			
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage() + "("+ sid + ")", e);
		}
		
		// variable
		return rect;		
	}
	
	/** determina el espacio real que debe ocupar una seccion localizando los
	 * puntos transparentes que se encuentran al este, norte, oeste y sur del
	 * punto pasado como argumento. Si alguno de ellos no es transparente, se
	 * detiene el calculo.
	 * 
	 * @param buff_img1 - imagen
	 * @param p - punto 
	 * @return
	 * @throws Exception - 
	 */
	private Rectangle getRectangle(BufferedImage buff_img1, Point p) throws Exception {
		int x1 = 0, y1 = 0, x2 = 0, y2 = 0, offs = 0;
		while(x1 == 0 || y1 == 0 || x2 == 0 || y2 == 0) {
			offs++;
			if (p.x - offs > 0) {
				x1 = (buff_img1.getRGB(p.x - offs, p.y) != 0 && x1 == 0) ? p.x - offs : x1;
			}
			if (p.y - offs > 0) {
				y1 = (buff_img1.getRGB(p.x, p.y - offs) != 0 && y1 == 0) ? p.y - offs : y1;
			}
			if (p.x + offs < buff_img1.getWidth()) {
				x2 = (buff_img1.getRGB(p.x + offs, p.y) != 0 && x2 == 0) ? p.x + offs : x2;
			}
			if (p.y + offs < buff_img1.getHeight()) {
				y2 = (buff_img1.getRGB(p.x, p.y + offs) != 0 && y2 == 0) ? p.y + offs : y2;
			}
		}
				
		// alteracion
//		x1 -= 1; y1 -= 1; 
//		int w = x2 + 1 - x1, h = y2 + 1 - y1;
		int w = x2 - x1, h = y2 - y1;
		return new Rectangle (x1, y1, w, h);			
	}
}
