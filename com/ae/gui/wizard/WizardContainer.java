/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.wizard;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.ae.action.*;
import com.ae.gui.*;

/** Un <code>WizardContainer</code> contiene y controla una lista de componentes
 * tipo <code>WizardPanel</code> en forma secuencial para asi presentar
 * una secuencia de paneles de entrada al estilo "asistente para..." esta
 * clase controla la secuencia de presentacion y ejecuta 
 * <code>ConfirmationAction.actionPerformed2(AppAbstractAction)</code> cuando
 * se alcanza el final de la secuencia de entrada. 
 *   
 * 
 */
public class WizardContainer extends RightPanel implements Navigator {

	public static int PREVIOUS = -1;

	private Vector panels = new Vector();
	private int p_counter;
	private JPanel container;
	private AppAbstractAction prevAct, cancelAct;
	private NextAction nextAct;
	private ConfirmationAction c_action;

	/** nueva instancia. 
	 * 
	 * @param doc - nombre del documento que sera presentado en area informa
	 * tiva (por ser esta clase extencion de <code>RightPanel</code>) 
	 * @param ca - instancia de <code>ConfirmationAction</code> 
	 */
	public WizardContainer(ConfirmationAction ca) {
		super(null);
		this.c_action = ca;
		this.container = new JPanel(new BorderLayout());
		this.p_counter = -1;
		this.prevAct = new PreviousAction(this);
		this.nextAct = new NextAction(this);
		this.cancelAct = new CancelAction(c_action);
		addWithoutBorder(container);
		setActionBar(new AbstractAction[] {prevAct, nextAct, cancelAct});
	}

	/** adiciona elementos a la lista de paneles a presentar. Los elementos
	 * debes ser añadidos en el orden que se desean presentar.
	 * 
	 * @param j - panel.
	 */
	public void addPanel(WizardPanel j) {
		panels.addElement(j);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.Navigator#hasNext()
	 */
	public boolean hasNext() {
		return !((p_counter + 1) == panels.size());
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.Navigator#isFirst()
	 */
	public boolean isFirst() {
		return p_counter == 0;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.Navigator#next()
	 */
	public void next() {
		boolean err = false;
		
		if (p_counter > -1) {
			WizardPanel wp = (WizardPanel) panels.elementAt(p_counter);
			showExceptionMessage(wp.validateFields());
			err = isShowingError();
		}
		
		// si no se estan presentando errores
		if (!err) {
			if (nextAct.getStatus() == NextAction.FINALIZE) {
				c_action.actionPerformed2(nextAct);
				c_action.actionPerformed2(cancelAct);
			} else if (hasNext()) {
				update(p_counter, ++p_counter);
			}
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.Navigator#previous()
	 */
	public void previous() {
		showMessage(null);
		if (!isFirst()) {
			update(p_counter, --p_counter);
			prevAct.setEnabled(!isFirst());
		}
	}
	
	/** retorna instancia del panel que se encuentra en la posision solicitada.
	 * puede ser una constante como PRUEVIOUS
	 * 
	 * @param pos - posicion 
	 * @return panel
	 */
	public WizardPanel getPanel(int pos) {
		if (pos == PREVIOUS) {
			pos = p_counter - 1;
		}
		return (WizardPanel) panels.elementAt(pos);
	}
	
	/** hacia adelante o atras segun la combinacion de argumento. este metodo
	 * elimina panel, inicializa panel a presentar, adiciona y presenta
	 * 
	 * @param ol - posicion de panel a remover
	 * @param ne - posicion de panel a presentar 
	 */
	private void update(int ol, int ne) {
		setVisible(false);
		if (ol > -1) {
			container.remove((WizardPanel) panels.elementAt(ol));
		}
		WizardPanel wp = (WizardPanel) panels.elementAt(ne);
		wp.initializePanel(this);
		
		// panel de bienvenida
		if (ne > 0) {
			GUIUtilities.setEmptyBorder(wp);
			setDocPanel("ImportInfo");
		} else {
			setDocPanel(null);
		}
		container.add(wp, BorderLayout.CENTER);
		prevAct.setEnabled(!isFirst());
		if (hasNext()) {
			changeStatus(NextAction.NEXT);
		} else {
			changeStatus(NextAction.FINALIZE);
		}
		setVisible(true);
	}

	/** puente para el cambio de situacion de la accion <code>NextAction</code>.
	 * este metodo permite controlar un final anticipado de la secuencia 
	 * de paneles.
	 * 
	 * @param m - modalidad (ver NextAction)
	 */	
	public void changeStatus(int m) {
		nextAct.changeStatus(m);
		
	}
}