/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.calendar;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import com.ae.gui.*;
/** CellRenderer diceñado para personalizar las celdas de un <code>MonthBox</code>
 * 
 */
public class MonthBoxCellRenderer extends DefaultTableCellRenderer {
	
	private MonthBoxModel1 tablemodel;
	private Color sundayC;	
	private Color tday_bg_col;
	private int[] curYM;
	private int date;
	private Font font;
	
	private Hashtable duty;
	
	public MonthBoxCellRenderer(MonthBoxModel1 mbm, Hashtable de) {
		super();
		setOpaque(true);
		this.tablemodel = mbm;
		this.sundayC = Color.RED;
		this.tday_bg_col = Color.YELLOW.darker();
		GregorianCalendar gc = new GregorianCalendar();
		this.curYM = new int[] {
			gc.get(GregorianCalendar.YEAR),
			gc.get(GregorianCalendar.MONTH),
		};
		this.date = gc.get(GregorianCalendar.DATE);
		setHorizontalAlignment(JLabel.RIGHT);
		this.font = getFont();
//		font = font.deriveFont((float) (font.getSize() - 1));
		 
		// analiza dc para colocar colores degradados donde corresponda
		this.duty = new Hashtable();
		Iterator it = de.keySet().iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			if (key.split(";").length > 1) {
				String did = (String) de.get(key);
				String[] md = key.split(";");
				gradient(Integer.parseInt(md[0]), 
					Integer.parseInt(md[1]), (Color) de.get("color." + did));
			}
		}
	}

	/** Marca el dia indicado con el color de la obligacion correspondiente y 
	 * degrada hacia atras dicho color proporcionando un efecto "de hacercamiento"
	 * 
	 * @param m - mes (1 = enero)
	 * @param d - dia final
	 * @param bc - color base
	 */	
	private void gradient(int m, int d, Color bc) {
		Color nc = bc;
		m -= 1;		
		for (int l = d; l > (d - DutyCalendar.LAPSE); l--) {
			duty.put((l + ";" + m), nc);
			nc = GUIUtilities.brighter(nc);
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(
		JTable table, Object value, boolean isSelected, boolean hasFocus,
		int rowIndex, int colIndex) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
		rowIndex, colIndex);

		setForeground(Color.BLACK);
		setBackground(Color.WHITE);
		setFont(font);

		// para solo evaluar el año/mes que corresponda		
		boolean today = false;
		if (tablemodel.isCurrentYearAndMonth(curYM)) {
			String sv = (String) value;
			sv = (sv.equals("")) ? "0" : sv; 
			int dat = Integer.parseInt(sv);
			today = (date == dat);
		}

		// domingo
		if(colIndex == 0) {
			setForeground(sundayC);
		}

		
		// obligacion
		String d = ((String) value) + ";" + tablemodel.getMonth();
		Color c = (Color) duty.get(d);
		if (c != null) {
			setBackground(c);
		}
		
		// hoy		
		if (today) {
			setBackground(tday_bg_col);
			setForeground(Color.WHITE);
		}
		
		return this;
	}
}