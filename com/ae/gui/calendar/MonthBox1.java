/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.calendar;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

/** un mes del calendario
 * 
 */
public class MonthBox1 extends JPanel {

	/** nuevo mes de calendario
	 * 
	 * @param y - año
	 * @param m - mes
	 * @param de - datos de declaraciones. (ver DutyCalendar)
	 */
	public MonthBox1(int y, int m, Hashtable de) {
		super(new BorderLayout());
		setBackground(Color.WHITE);
		
		// tabla & modelo
		MonthBoxModel1 mbm = new MonthBoxModel1(new GregorianCalendar(y, m, 1));
		JTable jt = new JTable(mbm);
		jt.setEnabled(false);
		jt.setBorder(new MatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));
		jt.setCellSelectionEnabled(false);
		jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jt.getTableHeader().setReorderingAllowed(false);
		jt.setShowGrid(false);
		
		// cellrenderer
		MonthBoxCellRenderer renderer = new MonthBoxCellRenderer(mbm, de);
		for (int col = 0; col < mbm.getColumnCount(); col++) {
			jt.getColumnModel().getColumn(col).setCellRenderer(renderer);
		}
		
		//etiqueta
		JLabel jlm = new JLabel(mbm.getMonthName());
		jlm.setFont(jlm.getFont().deriveFont(Font.BOLD));
		//new Font("Verdana", Font.BOLD, 11));
		jlm.setOpaque(true);
		jlm.setForeground(Color.WHITE);
		jlm.setBackground(new Color(0, 107, 159));
		jlm.setBorder(new EmptyBorder(2, 2, 2, 2));

		//panel
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(jlm, BorderLayout.NORTH);
		jp.add(jt.getTableHeader(), BorderLayout.SOUTH);
		add(jp, BorderLayout.NORTH);
		Box b1 = Box.createVerticalBox();
		b1.add(jt);
		b1.add(Box.createVerticalGlue());
		add(jp, BorderLayout.NORTH);
		add(b1, BorderLayout.CENTER);
		
	}
}