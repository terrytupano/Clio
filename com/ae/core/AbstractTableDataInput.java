/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.ae.gui.*;
import com.ae.action.*;
import com.ae.resource.*;

/** Centraliza el manejo de operaciones comunes a todas las subclases que 
 * tienen como funcion presentar una lista (o conjuto) de registros para que
 * el usuario pueda realizar operaciones con ellos.
 * 
 */
public abstract class AbstractTableDataInput extends RightPanel 
	implements ListSelectionListener, TaxPayerListener {
	private JScrollPane js_pane;
	//, TableModelListener { // , FocusListener {
	private Record model;

	private JPanel panel;
	private JTable table;
	private AppTableModel tModel;
	private TaxPayerSelector tp_selector;

	/** nueva table
	 * 
	 * @param dName - Nombre del documento
	 * @param atmm - Modelo para <code>AppTableModel</code>
	 */
	public AbstractTableDataInput(String dName, Record atmm) {
		super(dName);
		this.panel = new JPanel(new BorderLayout());
		this.model = atmm;
		this.tModel = PrevalentSystemManager.getTableModel(model);
		this.tp_selector = new TaxPayerSelector(this);
		table = tModel.getTable(this);
		this.js_pane = new JScrollPane(table);
		panel.add(js_pane);
		add(panel);
	}

	/** Habilita/desabilita todas las acciones disponibles dentro de la barra
	 * de herramientas segun el tipo de accion.
	 * 
	 * @param sco - tipo de accion que se desea inhabilitar. este puede
	 * ser alguno de los tipos desctito en <code>AppAbstractAction</code>
	 * @param ena  - =true, habilitar, = false, deshabilitar
	 */
	public void enableActions(int sco, boolean ena) {
		JButton[] btns = getToolBarButtons();
		for (int j = 0; j < btns.length; j++) {
			AppAbstractAction ac = (AppAbstractAction) btns[j].getAction();
			if (ac.getScope() == sco) {
				btns[j].setEnabled(ena);
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent fe) {
		if (tModel.getRowCount() != 0) {
			table.setRowSelectionInterval(0, 0);
		} else {
			enableActions(AppAbstractAction.RECORD_SCOPE, false);
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent fe) {

	}

	/** Retorna registro que se actualmente se encuentra seleccionado.
	 * 
	 * @return registro seleccionado.
	 */
	public Record getRecord() {
		return tModel.getRecordAt(table.getSelectedRow());
	}
	
	public Record[] getRecords() {
		int[] ridx = table.getSelectedRows();
		Record[] rcds = new Record[ridx.length];
		for (int rc = 0; rc < ridx.length; rc++) {
			rcds[rc] = tModel.getRecordAt(ridx[rc]);
		}
		return rcds;
	}

	/** Retorna <code>RightPanel</code> diceñado para la accion solicitada.
	 * Sub.clases implementan este medoto para retornar los paneles segun la
	 * <code>AbstractAction</code> que se para como argumento.
	 * 
	 * @param aa - accion solicitante
	 * @return el <code>RightPanel</code> diceñado para la accion
	 */
	public abstract RightPanel getRightPanel(AbstractAction aa);

	/** retorna la table encargada de presentar la lista de registros.
	 * 
	 * @return
	 */
	public JTable getTable() {
		return table;
	}

	/** Retorna <code>AppTableModel</code> modelo del <code>JTable</code> que
	 * presenta la lista de registros. 
	 * 
	 * @return - modelo
	 */
	public AppTableModel getTableModel() {
		return tModel;
	}

	/** Retorna el selector de contribuyentes asociado a esta tabla.
	 * 
	 * @return selector de contribuyentes
	 */
	public TaxPayerSelector getTaxPayerSelector() {
		return tp_selector;
	}

	/** implementacion * omision. Este metodo evita que las clases que no 
	 * usan un selector de contribuyentes, tenga que implementar este metodo y
	 * permite que las las que lo necesiten puedan sobreescribirlo.
	 */
	public void itemSelected(Record rcd) {
	}

	/** Cambia <code>JTable</code> de forma que tenga la apariencia de 
	 * <code>JList</code>. Dentro de los cambios, se encuentra la eliminacion
	 * de todas mes una de las columnas de la table. Esta es elegida arbitraria
	 * mente ya que se espera que el argumento de entrada se encarge de
	 * presentar la informacion correctamente. 
	 * 
	 * @param tcr - asigna un nuevo <code>TableCellRenderer</code> para que
	 * sea asignado a esta table. Este componente se encargara de presentar
	 * la informacion para que la transformacion sea completa.
	 */
	public void showLikeList(TableLikeListCellRenderer tcr) {
		table.setTableHeader(null);
		table.setShowGrid(false);
		DefaultTableColumnModel dtcm = new DefaultTableColumnModel();
		dtcm.addColumn(new TableColumn(0, 1, tcr, null));
		table.setColumnModel(dtcm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		// mamarrachada temporal
		table.setRowHeight(tcr.getPreferredSize().height);

		JViewport jvp = js_pane.getViewport();
		jvp.setBackground(table.getBackground());

	}

	/** Muestra el selector de contribuyente
	 *
	 */
	public void showTaxPayerSelector() {
		Box b1 = Box.createVerticalBox();
		b1.add(tp_selector);
		b1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		panel.add(b1, BorderLayout.NORTH);
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		boolean enable = true;
		if (e.getValueIsAdjusting())
			return;
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty()) {
			enable = false;
		} else {
			enable = true;
		}
		enableActions(AppAbstractAction.RECORD_SCOPE, enable);
	}
}
