/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import javax.swing.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.gui.*;
import com.ae.resource.*;

/** La funcion principal de este compoente es mostrar los compoenentes
 * <code>Account_Table y ClioObject_Table</code>. Estos se presentan de esta
 * forma porque esta clase:
 * - coordina la comunicacion entre el selector de contribuyentes, la tabla de
 * entradas contables y los distintos objetos presentado la informacion dentro
 * de cada uno de manera cooerente.  
 * - Sirve de puente para la transferencia de datos desde 
 * <code>Account_Table hacia ClioObject_Table</code> a travez de la tecnologia
 * DnD. 
 * 
 */
public class Account_ClioObject extends RightPanel implements TaxPayerListener, Keeper  {
	
	private Account_Table accountT;
	private ClioObject_Table clioObjT;
	private Record dndData;
	private TaxPayerSelector tp_selector;
	
	/** Constructor sin argumentos
	 *
	 */
	public Account_ClioObject() {
		super("Account-Clio");
		this.tp_selector = new TaxPayerSelector(this);
		this.accountT = new Account_Table(this);
		this.clioObjT = new ClioObject_Table(this);
		this.dndData = null;
				
		GUIUtilities.setEmptyBorder(tp_selector);
		Box b1 = Box.createHorizontalBox();
		b1.add(accountT);
		b1.add(Box.createHorizontalStrut(4));
		JPanel panel = new JPanel(new GridLayout(1,2,0,4));
		panel.add(b1);
		panel.add(clioObjT);
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.add(tp_selector, BorderLayout.NORTH);
		panel1.add(panel, BorderLayout.CENTER);
		addWithoutBorder(panel1);
		itemSelected(tp_selector.getSelectedRecord());
	}
	
	/** Establece el elemento que sera sera transferido a travez de DnD. 
	 * 
	 * @param rcd registro marcado para transferencia
	 */
	public void dragGestureRecognized(Record rcd) {
		dndData = rcd;
	}

	/** retorna el registros establecido como dato de transferencia a travez do
	 * DnD 
	 * 
	 * @return datos a transferir
	 */	
	public Record getDnDData() {
		return dndData;
	}
	
	/** invoca el metodo con la misma firma en <code>ClioObject_Table</code>
	 * este puente es usado por la asociacion automatica de cuentas/secciones
	 * 
	 * @param fid - identificador de forma
	 * @param obj - id de seccion
	 * @param ac - cuenta
	 */
	public void insertIfNotExist(Object fid, Object obj, String ac) {
		clioObjT.insertIfNotExist(fid, obj, ac, null);
	}
		
	/** retorna el selector de contribuyentes
	 * 
	 * @return selector de contribuyentes
	 */	
	public TaxPayerSelector getTaxPayerSelector() {
		return tp_selector;
	} 
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.TaxPayerListener#itemSelected(com.ae.core.Record)
	 */
	public void itemSelected(Record rcd) {
		showMessage(null);
		if (rcd != null) {
			accountT.getTableModel().selectRecord("'{acRifTP}' == '" + rcd.getFieldValue(0) + "'");
			clioObjT.selectRecord((String) rcd.getFieldValue(0));
		} else {
			showMessage("msg81");
		}
		accountT.enableActions(AppAbstractAction.TABLE_SCOPE, !isShowingError());
		clioObjT.enableActions(AppAbstractAction.TABLE_SCOPE, !isShowingError());
	}
}