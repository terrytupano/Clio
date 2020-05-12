/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import com.ae.core.*;
import com.ae.form.*;
import com.ae.gui.*;

/** interpreta el archivo que contiente la configuracion de las secciones dentro
 * de una forma. a medida que va intermpretando el .xml, crea todos los objetos 
 * java que ellos describen. Al finalizar la exploracion del 
 * documento, establecer todos los atributos de la forma, se crean las instancias
 * de las secciones y se registran.
 * 
 */
public class FormReaderHandler extends DefaultHandler {
	private String fName;

	private Form form;
	private Hashtable o_hashtable, r_hashtable, gv_hashtable;
	private Vector sectionList, attrsList;
	private int stage_counter;
	private ProgressPanel progressP;
	private RunnableMonitor rMon;
	private Font m_font;

	/** Nueva instancia 
	 * 
	 * @param f - Foma
	 * @param fName - Nombre del archivo 
	 * @param pp - barra de progreso
	 * @param rm - monitor de ejecucion separada
	 */
	public FormReaderHandler(Form f, String fName, ProgressPanel pp, RunnableMonitor rm) {
		this.form = f;
		this.fName = fName;
		this.progressP = pp;
		this.rMon = rm;
		this.o_hashtable = new Hashtable();
		this.r_hashtable = new Hashtable();
		this.gv_hashtable = new Hashtable();
		this.sectionList = new Vector();
		this.attrsList = new Vector();
		this.stage_counter = 0;

		// fuente
		try {
			String[] fnr = ((String) 
				DIMain.preferenceRecord.getFieldValue("prFont")).split(";");
			FileInputStream fis = new FileInputStream(
				ResourceUtilities.getDocumentPath() + fnr[0]);
			Font fo = Font.createFont(Font.TRUETYPE_FONT, fis);
			this.m_font = fo.deriveFont(Font.PLAIN, Float.valueOf(fnr[1]).floatValue());
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);		
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() {
		form.registreAttributes(gv_hashtable, o_hashtable, r_hashtable);
		progressP.offsetMaximum(sectionList.size());

		// comienzo el registro por la parte anterior
		// determina si hay componentes en la parte posteior. si es asi,
		// doy vuelta a la pagina 
		// y registro por la parte posterior
		boolean revCmp = false;
		String page = "overse";
		for (int pl = 0; pl < 2 && !rMon.stop; pl++) {
			for (int i = 0; i < sectionList.size(); i++) {
				Hashtable attrs = null;
				Class cf = (Class) sectionList.elementAt(i);
				try {
					attrs = (Hashtable) attrsList.elementAt(i);
					if (attrs.get("layer").equals(page)) {
						com.ae.form.sections.AbstractSection f =
							(com.ae.form.sections.AbstractSection) cf
								.newInstance();
						form.registreSection(f, attrs);
					} else {
						revCmp = true;
					}
				} catch (Exception e) {
					String ssid =
						e.getMessage() + "(" + (String) attrs.get("id") + ")";
					Logger.getLogger("").logp(
						Level.SEVERE,
						null,
						null,
						ssid,
						e);
				}
			}
			// vuelta a la pagina
			if (revCmp) {
				form.turnPage(Form.REVERSE);
				page = "reverse";
			} else {
				break;
			}
		}

		// da vuelta a la pagina si es necesario
		if (form.getAttribute("gv.firstFace").equals("overse")) {
			form.turnPage(Form.OVERSE);
		}
	}
	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(
		String nsURI,
		String ln,
		String qn,
		Attributes attrs)
		throws SAXException {

		if (rMon.stop) {
			return;
		}
		// debugg
		// Logger.getLogger("").logp(Level.SEVERE, null, null, "decoding " + qn + " ...");
		//	System.out.println("decoding " + qn + " ...");

		// valores globales de la forma
		if (qn.startsWith("gv.")) {
			gv_hashtable.put(qn, attrs.getValue("value"));

		}

		// color para mouseOver
		if (qn.endsWith("selected")) {
			String[] col = attrs.getValue("back").split(";");
			Color sc =
				new Color(
					Integer.parseInt(col[0]),
					Integer.parseInt(col[1]),
					Integer.parseInt(col[2]));
			putAttribute(qn, "selected.background", sc);
		}

		// escenarios
		if (qn.equals("stage")) {
			gv_hashtable.put(
				"stage" + stage_counter++,
				new AttributesImpl(attrs));
		}

		// secciones
		if (qn.endsWith("section")) {
			String[] sec = qn.split("\\W");
			// classpath en resourcebundle
			String strC =
				DIMain.bundle.getString("p30") + attrs.getValue("class");
			Hashtable ht = new Hashtable(attrs.getLength());
			try {
				Class cls = Class.forName(strC);
				sectionList.add(cls);

				// atributos y destino (ov o rev)
				ht.put("layer", sec[0]);
				for (int a = 0; a < attrs.getLength(); a++) {
					ht.put(attrs.getQName(a), attrs.getValue(a));
				}
				attrsList.add(ht);

			} catch (ClassNotFoundException cnfe) {
				String sid = attrs.getValue("id");
				String sscl = " Class: " + strC;
				SAXException se =
					new SAXException(
						"Handler can´t create instance for section."
							+ sid
							+ sscl);
				throw se;
			}
		}

		// imagenes forma. Solo reverse podria ser "" Este elemento determina
		// si una forma posee secciones al reverso de la hoja
		if (qn.endsWith("image")) {
			String im = attrs.getValue("name");
			if (!im.equals("")) {
				ImageIcon ii = ResourceUtilities.getFormImage(fName, im);
				JLabel jl = new JLabel(ii);
				jl.setBounds(0, 0, ii.getIconWidth(), ii.getIconHeight());
				putAttribute(qn, "form.image", jl);
			}
		}

		// fondo. Si es "" no hay fondo pero igual se crea el componente
		// en blanco
		if (qn.endsWith("background")) {
			String bi = attrs.getValue("name");
			JLabel jl1 = new JLabel();
			if (!bi.equals("")) {
				ImageIcon ii1 = ResourceUtilities.getFormImage(fName, bi);
				jl1.setIcon(ii1);
				jl1.setBounds(
					Integer.parseInt(attrs.getValue("xPos")),
					Integer.parseInt(attrs.getValue("yPos")),
					ii1.getIconWidth(),
					ii1.getIconHeight());
			}
			putAttribute(qn, "form.background", jl1);
		}

		// atributos para para inputRequestPanel	
		if (qn.endsWith("title") || qn.endsWith("panel")) {
			String[] atn = qn.split("\\W");
			String[] col = attrs.getValue("fore").split(";");
			Color fc =
				new Color(
					Integer.parseInt(col[0]),
					Integer.parseInt(col[1]),
					Integer.parseInt(col[2]));
			col = attrs.getValue("back").split(";");
			Color bc =
				new Color(
					Integer.parseInt(col[0]),
					Integer.parseInt(col[1]),
					Integer.parseInt(col[2]));
			int b = Font.PLAIN;
			if ("yes".equals(attrs.getValue("fontB"))) {
				b = Font.BOLD;
			}

			Font f =
				new Font(
					attrs.getValue("fontN"),
					b,
					Integer.parseInt(attrs.getValue("fontF")));

			// para las secciones, se usa el fuente descrito en 
			// xx.panel y se le adiciona el nro de unidades descritas en 
			// la preferencias.
/*
			Font f1 =
				new Font(
					attrs.getValue("fontN"),
					b,
					Integer.parseInt(attrs.getValue("fontF")));
			if (qn.endsWith("panel")) {
				Integer i =
					(Integer) DIMain.preferenceRecord.getFieldValue("prFontSi");
				float s = ((float) f1.getSize()) + i.floatValue();
				f1 = f1.deriveFont(s);
			}
*/
			putAttribute(qn, atn[1] + ".foreground", fc);
			putAttribute(qn, atn[1] + ".background", bc);
			putAttribute(qn, atn[1] + ".font", f);
//			putAttribute(qn, atn[1] + ".sectionFont", f1);
			putAttribute(qn, atn[1] + ".sectionFont", 
				m_font.deriveFont((float) m_font.getSize()));
		}
	}

	/* identifica donde colocar el atributo segun lo que indique <code>nod</code>
	 * luego lo coloca en la tabla correspondiente usando <code>na</code> como
	 * clave 
	 * 
	 */
	private void putAttribute(String nod, String na, Object val) {
		if (nod.startsWith("overse")) {
			o_hashtable.put(na, val);
		} else {
			r_hashtable.put(na, val);
		}
	}
}