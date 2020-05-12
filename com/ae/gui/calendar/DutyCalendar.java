/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.calendar;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import javax.swing.border.*;

import com.ae.core.*;
import com.ae.gui.*;
import com.ae.resource.*;

import org.jdom.*;

/** El calendario de obligaciones controla la relacion entre el tiempo y el
 * inventario de formas fiscales controlando el descuento automatico de 
 * formas a la fecha de vencimiento y a su vez comportandoce como un 
 * asistente, proporcionando informacion al usuario sobre las acciones e 
 * informacion relacionada. Los mensajes enviados por esta clase se presentan
 * en la barra de mensajes. Si son varios, se presenta una, dura algunos seguendos
 * y luego se presenta el siguiente, asi de forma recurrente.
 * 
 */
public class DutyCalendar extends RightPanel implements ActionListener, Keeper {

	public static int LAPSE = 5;

	private MonthBox1[] mb = new MonthBox1[12];
	private Hashtable docEntries;
	private Vector toShowE; 
	private String[] unlook_arr;
	private javax.swing.Timer timer;
	private int lastShow;


	/** Contruye nuevo panel
	 * 
	 * @param year - año
	 */
	public DutyCalendar() {
		super("DutyCalendar");
		GregorianCalendar cal = new GregorianCalendar();
		
		setDocumentEntries();

		JPanel jp1 = new JPanel(new BorderLayout());
		jp1.add(getLegentComponent(), BorderLayout.SOUTH);
		jp1.add(new JScrollPane(buildYear(cal.get(GregorianCalendar.YEAR))), BorderLayout.CENTER);		
		add(jp1);
		
		this.lastShow = -1;
		this.toShowE = new Vector();
		
		// mensajes 
		Thread m051 = new Thread(new Msg051(this, docEntries), "msg51");
		m051.start();
		Thread m052 = new Thread(new Msg052(this, docEntries), "msg52");
		m052.start();
		Thread m053 = new Thread(new Msg053(this, docEntries), "msg53");
		m053.start();
		Thread m054 = new Thread(new Msg054(this, docEntries), "msg54");
		m054.start();
		
		this.timer = new javax.swing.Timer(AplicationException.SHORT, this);
		timer.setRepeats(true);
		timer.start();
	}
	
	/** adiciona una nueva excepcion a la lista de mesajes a presentar.
	 * 
	 * @param mid - id de mensajes
	 * @param dta - datos de sustitucion
	 */
	public void addMessage(String mid, Object[] dta) {
		AplicationException ape = (AplicationException) 
			PrevalentSystemManager.exceptions.get(mid);
		AplicationException ape1 = (AplicationException) ape.clone(); 
		ape1.setMessage(MessageFormat.format(ape.getMessage(), dta));
		toShowE.add(ape1);
	}

	/** Interpreta el documento que contiene las fechas de decharacion
	 * colocando sus elementos en forma:
	 * para elementos <declaration>
	 * put("name." + valor atributo id, nombre)
	 * put("color." + valor atributo id, color)
	 * para elementos <ddate>
	 * put(valor atributo dm, valor atributo did) 
	 */
	private void setDocumentEntries() {
		Document doc = PrevalentSystemManager.getXMLDocument(
			ResourceUtilities.getPrevaylerPath() + "/DeclarationDate.xml");
		java.util.List l = doc.getRootElement().getChildren();
		this.docEntries = new Hashtable();
		for (int c = 0; c < l.size(); c++) {
			Element e = (Element) l.get(c);

			if (e.getName().equals("declaration")) {
				String did = e.getAttributeValue("id");
				docEntries.put("name." + did, e.getAttributeValue("name"));
				String[] rgb = e.getAttributeValue("color").split(";");
				docEntries.put("color." + did, 
					new Color(Integer.parseInt(rgb[0]), 
					Integer.parseInt(rgb[1]), 
					Integer.parseInt(rgb[2]))
				);
			}
			
			if (e.getName().equals("ddate")) {
				docEntries.put(e.getAttributeValue("md"), e.getAttributeValue("did"));
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */	
	public void actionPerformed(ActionEvent ae) {
		if (toShowE.size() != 0) {
			lastShow = (lastShow == toShowE.size() - 1) ? 0 : lastShow + 1;
			showExceptionMessage((AplicationException) toShowE.elementAt(lastShow));
		}
	}

	/** Construye y retorna un calendario.
	 * 
	 * @param year - año
	 * @return contenedor con meses
	 */
	private JPanel buildYear(int year) {
		JPanel jp = new JPanel(new GridLayout(3, 4, 4, 4));
		jp.setBackground(Color.WHITE);
		Dimension d = new Dimension(140, 130);
		for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
			mb[month] =	new MonthBox1(year, month, docEntries);
			mb[month].setMinimumSize(d);
			mb[month].setPreferredSize(d);
			mb[month].setMaximumSize(d);
			
			jp.add(mb[month]);
		}
		return jp;
	}
	
	/** crea y retorna un componente que contiene la leyenda de los colores
	 * usados dentro del calendario
	 * 
	 * @return leyenda de colores
	 */
	private JPanel getLegentComponent() {
		JPanel jp = new JPanel(new GridLayout(2, 0, 
			GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP));
		GUIUtilities.setEmptyBorder(jp);
		jp.setBorder(new TitledBorder(DIMain.bundle.getString("l06")));
//		jp.setPreferredSize(new Dimension(0, 64));
		Iterator it = docEntries.keySet().iterator();
		while(it.hasNext()) {
			String next = (String) it.next();
			if (next.startsWith("color.")) {
				String did = next.substring(6);
				
				Color cl = (Color) docEntries.get("color." + did);
				JLabel flag = new JLabel(" ");
				flag.setOpaque(true);
				flag.setBorder(new LineBorder(cl.darker()));
				flag.setPreferredSize(new Dimension(26, 16));
				flag.setBackground(cl); 
				JLabel nam = new JLabel((String) docEntries.get("name." + did));
				Box b1 = Box.createHorizontalBox();
				b1.add(flag);
				b1.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
				b1.add(nam);
				b1.add(Box.createHorizontalGlue());
				jp.add(b1);
			}
		}
		return jp;
	}
}