/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.prevayler.implementation.*;

import com.ae.evaluate.*;
import com.ae.resource.*;

/** Modelo standar para todas las <code>JTables</code> usadas en la aplicacion. 
 * AppTableModel trabaja en conjunto con <code>Record</code> el cual sirve a su ves como
 * modelo para implementar los metodos faltantes de <code>TableModel</code>.
 * Una instancia de esta clase representa una fucion similar a la de un 
 * archivo de base de datos. Esto es gracias a que es una especializacion
 * de <code>AbstractPrevalnetSystem</code> debido a ello, cualquier clase
 * que necesite informacion de base de datos debe tener una compia de una 
 * instancia de esta clase.
 * Esta clase no debe ser creada directamente. para ello ejecute
 * <code>PrevalentSystemManager.getTableModel(Record) o 
 * PrevalentSystemManager.getTableModel(Record, boolean)</code> el sistema
 * prebalente se encargara de realizar todas las gestiones necesarias
 * para retornar la instancia de datos correspondiente.
 * 
 */
public class AppTableModel extends AbstractPrevalentSystem implements TableModel {

	private transient Evaluator evaluator;
	protected transient EventListenerList listenerList;

	private Record record;
	private TreeMap treeMap;
	private transient Vector view;
	private String sel_exp;

	/** Nuevo modelo.
	 * 
	 * @param rcd - registro 
	 */	
	public AppTableModel(Record rcd) {
		super();
		this.record = rcd;
		this.treeMap = new TreeMap();
		this.listenerList = eventListeners();
		evaluator();
		this.view = view();
		this.sel_exp = "1.0";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
	 */
	public void addTableModelListener(TableModelListener ltn) {
		eventListeners().add(TableModelListener.class, ltn);	
	}
	
	/*
	 * metodo auxiliar para miembros de datos tipo transient
	 */
	private Evaluator evaluator() {
		if (evaluator == null) {
			this.evaluator = new Evaluator();
			selectRecord("1.0");
		}
		return evaluator;
	}
	
	/*
	 * metodo auxiliar para miembros de datos tipo transient
	 */
	private EventListenerList eventListeners() {
		if (listenerList == null) {
			this.listenerList = new EventListenerList();
		}
		return listenerList;
	}

	/** Alteracion del contenido dentro de esta modelo. Cualquier operacion que
	 * altere el contenido dentro de este modelo debe invocar este metodo
	 * 
	 * @param acmd - Tipo de alteracion
	 */	
	public void executeCommand(AbstractCommand acmd) {
		
		/* **************************************************
		 * pendiente validacion si registro existe y es new_record o 
		 * si no existe y es editRecord
		 * 
		 * **************************************************
		 * averiguar como hacer fireTableRowsxxxxx con el nro de registro correcto
		 * 
		 ***************************************************/
		Record rcd = acmd.getData();
		
		if (acmd instanceof NewRecordCommand) {
			treeMap.put(rcd.getKey(), rcd);
			fireTableChanged(new TableModelEvent(this, 0, 0,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
		}
		if (acmd instanceof EditRecordCommand) {
			treeMap.put(rcd.getKey(), rcd);
			fireTableChanged(new TableModelEvent(this, 0, 0,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
		}
		if (acmd instanceof DeleteRecordCommand) {
			treeMap.remove(rcd.getKey());
			fireTableChanged(new TableModelEvent(this, 0, 0,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
		}
	}

	/** Notificacion a todos los <code>TableModelListener</code> que un 
	 * cambio en este modelo a ocurrido. Antes de notificar, realiza una 
	 * nueva seleccion para mantener actualizado el subconjunto de registros
	 * 
	 * @param e - modificacion
	 */
	private void fireTableChanged(TableModelEvent e) {
		updateView();
		Object[] listeners = eventListeners().getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TableModelListener.class) {
				((TableModelListener)listeners[i+1]).tableChanged(e);
			}
		}
	}
	
 	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int col) {
		return record.getFieldClass(col);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return record.getFieldsCount();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
		return record.getFieldName(col);
	}
	
	/** Retorna <code>Record</code> asociado con la clave solicitada. Este metodo
	 * retorna <code>null</code> si no existe ningun registro con dicha clave 
	 * 
	 * @param key - Clave
	 * @return Registro
	 */
	public Record getRecord(String key){
		return (Record) treeMap.get(key);
	}
	
	/** retorna un vector con los registro contenidos dentro de este "archivo"
	 * 
	 * @param all - indica si se retornan todos los registros o
	 * solo los que cumplen con el criterio de seleccion.
	 * 
	 * @return <code>Vector</code> con lista de registros
	 */
	public Vector getRecords(boolean all) {
		Vector vr = new Vector();
		if (all) {
			vr = new Vector(treeMap.values());
		} else {
			for (int oc = 0; oc < getRowCount(); oc++) {
				vr.add(getRecordAt(oc));
			}
		}
		return vr;
	}
	
	/** retorna <code>Record</code> que se encuentra en la fila <code>row</code>
	 *
	 */
	public Record getRecordAt(int row) {
		// error que ocurre cuando se actualiza tabla a travez de proceso
		// separado 
		//
		//PARCHE. averiguar mejor forma
		
		Record rc = null;
		if (row < view.size()) {
			rc = (Record) view().elementAt(row) ;//getInternalRecordAt(row);
		}	 
		return rc; 
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return view().size();
	}
	
	/** retorna JTable con este AppTableModel como JTableModel
	 * este metodo consulta a Record para calcular el ancho de las columnas y 
	 * eliminar las columans que segun Record no deben ser presentadas por este
	 * JTable
	 * 
	 * @return JTable Con formato correcto
	 */
	public JTable getTable(ListSelectionListener at) {
		JTable t = new JTable(this);
//		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel cm = t.getColumnModel();
		ListSelectionModel lsm = t.getSelectionModel();
		lsm.addListSelectionListener(at);
    		
		//***************
		// temporal: averiguar como obtemer la metrica del font actual para asi
		// calcular correctamente el anco de la columna
		//****************
		// ancho de las columnas
		for (int c = 0; c < getColumnCount(); c++) {
			TableColumn tc = cm.getColumn(c);
			tc.setPreferredWidth(record.getFieldLength(c) * 8);
		}

		//crea nuevo modelo de columnas para eliminar las que no se desea que se
		//presenten
		DefaultTableColumnModel dtcm = new DefaultTableColumnModel(); 
		for (int c = 0; c < getColumnCount(); c++) {
			if (record.showInList(c)) {
				dtcm.addColumn(cm.getColumn(c));
			}
		}
		t.setColumnModel(dtcm);
//		updateView();
		return t;
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		// error que ocurre cuando se actualiza tabla a travez de proceso
		// separado 
		//
		//PARCHE. averiguar mejor forma
		
		Object ob = null;
		if (row < view.size()) {
			ob = ((Record) view().elementAt(row)).getFieldValue(col); // (getInternalRecordAt(row)).getFieldValue(col); 
		}
		return ob; 
	}

	/** Retorna <code>SortedMap</code> con una porcion de los registro contenidos
	 * dentro de este modelo. Si;
	 * 
	 * <code>from == null && to != null</code> la lista retornada es de los 
	 * registro con clave estrictamente menor a la clave del argumento de entrada
	 * 
	 * <code>from != null && to == null</code> la lista es de los registro 
	 * con clave igual o mayor a la clave del argumento de entrada
	 * 
	 * <code>from != null && to != null</code> retorna una lista de registros
	 * cuya clave este dentro del rango de from.getKey() >= x > to.getKey().
	 * si las claves de from y to son iguales la lista estara vacia   
	 * 
	 * @param from - desde 
	 * @param from - hasta
	 *  
	 * @return porcion del contenido de este modelo segun argumentos
	 */	
	public SortedMap getSortedMap(Record from, Record to) {
		SortedMap sm = null;
		if (from == null && to != null) {
			sm = treeMap.headMap(to.getKey()); 
		}
		if (from != null && to == null) {
			sm = treeMap.tailMap(from.getKey()); 
		}
		if (from != null && to != null) {
			sm = treeMap.subMap(from.getKey(), to.getKey()); 
		}
		return sm;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/*
	 * @see Serializable
	 */
	private void readObject(java.io.ObjectInputStream in)
		 throws IOException, ClassNotFoundException {
		 	in.defaultReadObject();
		 	updateView();
		 }
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
	 */
	public void removeTableModelListener(TableModelListener l) {
		eventListeners().remove(TableModelListener.class, l);
	}
	
	/** indic a este modelo que establezca la de registro. La seleccion 
	 * solo permite que se presenten los registro que cumplan con la expresion
	 * pasada como argumento. Esta seleccion solo afecta a los metodos de
	 * <code>TableModel</code>. otros, como <code>getRecordAt(String)</code>
	 * se aplican al contenido completo, independientemente de la seleccion. Si el
	 * argumento  es <code>null</code> no se establece seleccion.
	 * 
	 * @param exp - expresion logica
	 */
	public void selectRecord(String exp) {
		this.sel_exp = (exp == null) ? "1.0" : exp;
		try {
			evaluator().parse(sel_exp);
			fireTableChanged(new TableModelEvent(this));
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage() + "( " + exp + ")", e);
		}
	}
	
	/** retorna la expresion por la cual este <code>AppTableModel</code> esta
	 * seleccionando los registros
	 * 
	 * @return expresion 
	 */
	public String getSelectionExpresion() {
		return sel_exp;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object val, int row, int col) {
		Record rcd = (Record) view.elementAt(row);
		rcd.setFieldValue(col, val);
	}
	
	/** Actualiza el vector que contiene el conjunto de registros que cuplen con
	 * el requisito de seleccion. 
	 *
	 */	
	private void updateView() {
		Set tmpset = treeMap.entrySet();
		Object[] oss = tmpset.toArray();
		view().removeAllElements();
		for (int oc = 0; oc < oss.length; oc++) {
			Map.Entry me = (Map.Entry) oss[oc];
			Record rc = (Record) me.getValue();
			try {
				String[] vars = evaluator().getVariables();
				for (int vc = 0; vc < vars.length; vc++) {
					evaluator().putVariable(
						vars[vc], rc.getFieldValue(vars[vc]).toString());
				}
				if (evaluator().evaluate().equals("1.0")) {
					view().add(me.getValue());
				}
			} catch(Exception e) {
				String exp = e.getMessage() + "(" + getSelectionExpresion() + ")";
				Logger.getLogger("").logp(Level.SEVERE, null, null, exp, e);
			}
		}
	}
	
	/*
	 * metodo auxiliar para miembros de datos tipo transient
	 */
	private Vector view() {
		if (view == null) {
			this.view = new Vector();
		}
		return view;
	}
}