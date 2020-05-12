/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.jdom.*;

import com.ae.core.*;
import com.ae.resource.*;

/** Controla la informacion que es presentada al usuario sonbre las 
 * formas dispoblibles para adiquision. Atravez de metodos destinados a
 * ello, clases interesadas obtienen los componentes visuales diceñados
 * para la interacion.
 * 
 */
public class PurchaseTable implements TableModelListener {

	private AppEntry[] iForms;
	private JTable purchaseT;
	private boolean showCrypto;
	private double total;
	private JLabel totalSt;
	private JPanel totalPanel;
	private NumberFormat nFormat;
	private TableModel tableM;

	/** Nueva instancioa
	 * 
	 * @param c - determina si se presenta el atributo <code>cryptoId</code> 
	 */
	public PurchaseTable(boolean sc) {
		this.showCrypto = sc;
		this.iForms = ResourceUtilities.getInstalledForms();
		this.purchaseT = new JTable();
		updatePurchaseTable();
		this.tableM = purchaseT.getModel();
		tableM.addTableModelListener(this);
		this.nFormat = NumberFormat.getInstance();

		Integer[] inte = new Integer[10];
		for (int c = 0; c < inte.length; c++) {
			inte[c] = new Integer(c);
		}
		JComboBox jcb = new JComboBox(inte);
		purchaseT.setDefaultEditor(Integer.class, new DefaultCellEditor(jcb));

		this.totalPanel = new JPanel(new BorderLayout());
		this.totalSt = new JLabel(nFormat.format(0.0));
		totalSt.setFont(totalSt.getFont().deriveFont(Font.BOLD));
		totalPanel.add(new JLabel(DIMain.bundle.getString("t08")), BorderLayout.WEST);
		totalPanel.add(totalSt, BorderLayout.CENTER);
	}
	
	/* 
	 *  (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent tme) {
		// actualiza el total 
		if (tme.getType() == TableModelEvent.UPDATE) {
			int cost, quant;
			total = 0;
			for (int r = 0; r < tableM.getRowCount(); r++) {
				cost = ((Integer) tableM.getValueAt(r, 2)).intValue();
				quant = ((Integer) tableM.getValueAt(r, 1)).intValue();
				total += quant * cost;
			}
			totalSt.setText(nFormat.format(total));
		}
	}
	

	/** retorna <code>JTable</code> con el contenido de las formas
	 * programadas dentro de la aplicacion. La columna cantidad puede
	 * editarse permitiendo colocar el nro de formas a adquirir
	 * 
	 * @return - tabla con formas.
	 */
	public JTable getTable() {
		return purchaseT;
	}
	
	public JPanel getTotalPanel() {
		return totalPanel;
	}
	
	public String getTotalString() {
		return nFormat.format(total);
	}

	/** crea el modelo para la table con las formas, cantidades y precios
	 * dentro del panel para el calculo de precios de adquisicion
	 * 
	 */
	private void updatePurchaseTable() {
		Vector pCN = new Vector(4);
		pCN.add(DIMain.bundle.getString("f09"));
		pCN.add(DIMain.bundle.getString("c15"));
		pCN.add(DIMain.bundle.getString("c16"));
		if (showCrypto) {
			pCN.add("cryptoId");
		}
		Vector rd = new Vector();
		for (int x = 0; x < iForms.length; x++) {
			Vector rd_x = new Vector();
			String fid = (String) iForms[x].getKey();
			rd_x.add(iForms[x].getValue());
			rd_x.add(new Integer(0));
			Hashtable h = PrevalentSystemManager.getFormElements(fid);
			Element e = (Element) h.get("form");
			rd_x.add(Integer.valueOf(e.getAttributeValue("price")));
			
			// cryptoId
			if (showCrypto) {                
				rd_x.add(e.getAttributeValue("cryptoId"));
			}
			
			rd.add(rd_x);
		}

		purchaseT.setModel(new PurchaseTableModel(rd, pCN));
		TableColumnModel cm = purchaseT.getColumnModel();
		TableColumn tc = cm.getColumn(0);
		tc.setPreferredWidth(450);
	}
	
	/** clase que extiende <code>DefaultTableModel</code> par evitar
	 * que las celdas de la primera columna puedan ser editadas y permitir
	 * el editro correcto para la columna cantidad
	 * 
	 */
	public class PurchaseTableModel extends DefaultTableModel {

		public PurchaseTableModel(Vector rd, Vector cn) {
			super(rd, cn);
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		public Class getColumnClass(int col) {
			return (col == 1 || col == 2)
				? Integer.class
				: super.getColumnClass(col);
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int row, int column) {
			return (column == 1);
		}
	}
}
