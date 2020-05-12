/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;

import com.ae.gui.*;
import com.ae.resource.*;
import com.ae.action.*;
import com.ae.core.*;

/** presenta una lista de los elementos de cada objeto clio presentes dentro
 * de la aplicacion. La inseccion de elementos dentro del modelo de esta table
 * se realiza de dos formas:
 * - durante la seleccion de objetos, se crea un nuevo registro por cada registro
 * del objeto que no este presente dentro de esta tabla. 
 * - durante el evento xxxx se adicionan nuevas entradas.
 * 
 * En palabras sencillas, el modelo que mantiene esta table es una relacion entre
 * las entradas contables representadas por <code>AccountRecord</code> y el
 * resto de los datos presentes  dentro de la aplicacion. 
 * 
 * 
 */
public class ClioObject_Table extends AbstractTableDataInput 
	implements ActionListener, DropTargetListener, ListSelectionListener {
	
	private Account_ClioObject acc_clio;
	private DropTarget dropt;
	private JComboBox objSelector;
	private boolean selection;
	private String tp_ID, fo_ID;

	/** nueva table de objetos. 
	 * 
	 * @param ac - instancia de <code>Account_ClioObjet</code> 
	 * para comunicacion
	 */		
	public ClioObject_Table(Account_ClioObject ac) {
		super(null, new Account_ClioObjectRecord());
		this.acc_clio = ac;
		this.selection = false;
		this.objSelector = new JComboBox();
		objSelector.setToolTipText(
			(String) AppConstants.getConstant("formSelector").getValue());
		objSelector.addActionListener(this);
		objSelector.setMaximumSize(
			new Dimension(200, 100)
		);
		setToolBar(new Component[] {
			objSelector,
			new JButton(new DeleteRecordAction(this))
		});
		
		this.dropt = new DropTarget(getTable(), this);
		showLikeList(new TableLikeListCellRenderer(
			new Account_ClioObjectRecord(), objSelector));
			
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */		
	public void actionPerformed(ActionEvent ae){
		selection = true;
		selectRecord(tp_ID);  
		selection = false;
	}
	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dte) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dtde) {
		int r = getTable().rowAtPoint(dtde.getLocation());
		getTable().setRowSelectionInterval(r, r);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dtde) {
		Record rcd = acc_clio.getDnDData();
		int r = getTable().rowAtPoint(dtde.getLocation());
		Account_ClioObjectRecord cor = 
			(Account_ClioObjectRecord) getTableModel().getRecordAt(r);
		insertIfNotExist(fo_ID, cor.getFieldValue("coObject"), 
			(String) rcd.getFieldValue("acCode"), rcd.getFieldValue("acText")
		);
		dtde.dropComplete(true);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dtde) {

	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.businesso.BusinessObject#getRightPanel(javax.swing.AbstractAction)
	 */
	public RightPanel getRightPanel(AbstractAction aa) {
		return null;
	}
	
	/** este metodo inserta un nuevo registro dentro de la tabla 
	 * (que relaciona las secciones con las entradas contables) si
	 * se determina que no exite un registro igual para los argumentos
	 * de entrada. is el arguemto <code>t != null</code> se registra
	 * la operacion en clio.log para asi, mantener un rastreo y mejorar
	 * la kb.
	 * 
	 * @param fid - identificador de forma
	 * @param obj - identificador de seccion
	 * @param ac - entrada contables
	 * @param t - = texto de la cuenta que se desea asociar
	 * 
	 */
	public void insertIfNotExist(Object fid, Object obj, String ac, 
		Object t) {
		Account_ClioObjectRecord cor = new Account_ClioObjectRecord();
		cor.setFieldValue("coRifTP", tp_ID);
		cor.setFieldValue("coObjID", fid);
		cor.setFieldValue("coObject", obj);
		if (ac != null) {
			cor.setFieldValue("coAcCode", ac);
		}
		if (getTableModel().getRecord(cor.getKey()) == null) {
			PrevalentSystemManager.executeCommand(new NewRecordCommand(cor));
		}
		if (t != null) {
			Logger.getLogger("").logp(Level.WARNING, null, null, 
				t.toString() + " ---> " + fid + "(" + obj.toString() + ")"
			);
		}
	}
		
	/** se modifica la lista de formas disponibles segun el contribuyente
	 * dentro del selector de objetos. adicinalmente. Adicionalmente, 
	 * para la primera forma seleccionada (la primera que se inserto)
	 * este metodo inserta las secciones que no esten
	 * y dentro de esta tabla para hacerlas disponibles para la asociacion 
	 * 
	 * @param tID - Identificador de contribuyente.
	 */
	public void selectRecord(String tID) {
		this.tp_ID = tID;
		
		// formas segun tipo de contribuyente
		// para evitar que el selector de objetos se llene durante la seleccion
		if (!selection) {
			// porque removeItem dispara actionPerformed
			objSelector.removeActionListener(this);
			objSelector.setModel(
				new DefaultComboBoxModel(CoreUtilities.getFormsFor(tp_ID)));
			objSelector.addActionListener(this);
		}
		this.fo_ID =
			(String) ((AppEntry) objSelector.getSelectedItem()).getKey();
		Vector vec = CoreUtilities.getAssociableSections(fo_ID);

		// inicia la insercion
		for (int r = 0; r < vec.size(); r++) {
			AppEntry ae = (AppEntry) vec.elementAt(r);
			insertIfNotExist(fo_ID, ae.getKey(), null, null);
		}
		
		getTableModel().selectRecord(
			"'{coRifTP}' == '" + tp_ID + "' && '{coObjID}' == '" + fo_ID + "'"
		);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent lse) {
		super.valueChanged(lse);

		// existe un estado indeterminado durante la actualizacion del model
		// de la table donde se dispara este metodo. no se ha podido controlar
		// de otra forma.
		try {
			Record r = getRecord();
			if (((String) r.getFieldValue("coAcCode")).equals("")) {
				enableActions(AppAbstractAction.RECORD_SCOPE, false);
			}
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			// 
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null,	e.getMessage(), e);
		}
	} 
}