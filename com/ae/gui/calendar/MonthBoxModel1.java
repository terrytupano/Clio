/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */
 
package com.ae.gui.calendar;

import java.text.*;
import java.util.*;

import javax.swing.table.*;

/** Clase que sirve de modelo para la <code>JTable</code>. Esta, modelo  
 * los datos del mes de calendario dentro de la clase <code>MonthBox1</code>
 *  
 */
public class MonthBoxModel1 extends AbstractTableModel {//implements TableModel {

	private DateFormatSymbols dateFormat;
	private String[][] days_date;
	private int[] today;
	
	/** Nuevo modelo 
	 * 
	 * @param cal - Calendario
	 */
	public MonthBoxModel1(Calendar cal) {
		this.dateFormat = new DateFormatSymbols();
		this.today = new int[] {
			cal.get(GregorianCalendar.YEAR), 
			cal.get(GregorianCalendar.MONTH)
		};
		
		// inicializa dias de la semana
		this.days_date = new String[6][7];
		for (int x = 0; x < days_date.length; x++) {
			for (int y = 0; y < days_date[x].length; y++) {
				days_date[x][y] = "";
			}
		}
		
		// 
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int c = cal.get(Calendar.DAY_OF_WEEK);
		int r = 1;
		int day = 1;
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		while (day <= lastDay) {
			days_date[r - 1][c - 1] = Integer.toString(day++);
			if (++c > 7) {
				c = 1;
				r++;
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 7;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return 6;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return days_date[rowIndex][columnIndex];
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
		return dateFormat.getShortWeekdays()[col + 1].substring(0, 1).toUpperCase(); 
	}
	/** Retorna el nombre del mes para este modelo
	 * 
	 * @return nombre del mes
	 */
	public String getMonthName() {
		String t = dateFormat.getMonths()[today[1]];
		String b = t.substring(1, t.length());
		String f = t.substring(0, 1).toUpperCase(); 
		return f + b; 
	}
	/** retorna el mes del año 
	 * 
	 * @return mes del año
	 */
	public int getMonth() {
		return today[1];
	}
	/** retorna true si este es el modelo para un <code>MontBox1</code> que 
	 * representa el mes y el año pasados como argumento de entrada.
	 * 
	 * @param date [0]=año [1]=mes
	 * @return true si el año/mes coinciden
	 */
	public boolean isCurrentYearAndMonth(int[] date) {
		return (date[0] == today[0]) && (date[1] == today[1]);
	}
}
