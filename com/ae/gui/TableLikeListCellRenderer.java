/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import org.jdom.*;

import com.ae.core.*;
import com.ae.resource.*;

/** especializacion de <code>DefaultTableCellRenderer</code> para usar dentro
 * de una tabla que ha sido cambiada para que se asemeje a <code>JList</code>
 */
public class TableLikeListCellRenderer extends DefaultTableCellRenderer {
	
	private Record model;
	private AppTableModel apptm;
	private ImageIcon totalD, countD;
	private String pattern;
	private JComboBox obj_sel;
	
	/** contruye nueva instancia de esta clase.
	 * 
	 * @param m - modelo usado para determinar el tipo de datos que se presentaran
	 * dentro de la "table pareciad a una lista"
	 *   
	 */
	public TableLikeListCellRenderer(Record m, JComboBox os) {
		super();
		this.model = m;
		this.obj_sel = os;
		this.apptm = PrevalentSystemManager.getTableModel(model);
		this.totalD = ResourceUtilities.getSmallIcon("totalDot");
		this.countD = ResourceUtilities.getSmallIcon("countDot");
		
		// alto de la celda & plantilla
		this.pattern = "<html> <b>;: </b>; </html>";
		setText("X");
		Dimension d = getPreferredSize();
		if (model instanceof AccountRecord) {
			d.height *= 2;
			this.pattern = "<html> <b>;</b> <br>; </html>";
		}
		setPreferredSize(d);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table,value, isSelected, hasFocus, row, column);

		model = apptm.getRecordAt(row);
		String[] patt = pattern.split(";");
		
		// cuando esta clase esta presentando a las entradas contables
		if (model instanceof AccountRecord) {
			setText(
				patt[0] + (String) model.getFieldValue("acCode") +
				patt[1] + (String) model.getFieldValue("acText") + patt[2]
			);
		}
		
		// cuando se presenta la relacion cuentas - secciones 
		if (model instanceof Account_ClioObjectRecord) {
			String coac = (String) model.getFieldValue("coAcCode");
			if (coac.equals("")) {
				AppEntry a = (AppEntry) obj_sel.getSelectedItem();
				Hashtable h = PrevalentSystemManager.getFormElements((String) a.getKey());
				String key = (String) model.getFieldValue("coObject");
				Element e = (Element) h.get(key);
				
				// recupera texto de igual que las secciones
				String txt = e.getAttributeValue("label");
				txt = (txt == null) ? e.getAttributeValue("title") : txt;

				setText(patt[0] + key + patt[1] + txt + patt[2]);
				
				if (e.getAttributeValue("sectionValue").equals("Integer")) {
					setIcon(countD);
				} else {
					setIcon(totalD);
				}
			} else {
				setText("    " + coac);
				setIcon(null);
			}
		}
		return this;
	}
}
