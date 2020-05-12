/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.resource.*;


/** Clase raiz de todas las acciones dentro de la aplicacion
 * 
 */
public abstract class AppAbstractAction extends AbstractAction {
	
	public static int NO_SCOPE = 0;
	public static int TABLE_SCOPE = 1;	
	public static int RECORD_SCOPE = 2;
	
	private int scope;
	
	/** nueva accion
	 * 
	 * @param tex Texto (puede ser null)
	 * @param inam Nombre del icono
	 * @param sco alcance
	 */
	public AppAbstractAction(String tex, String inam, int sco, String tid) {
		putValue(SMALL_ICON, ResourceUtilities.getSmallIcon(inam));
		
		// porque tex puede ser null
		if (tex != null) {
			putValue(NAME, DIMain.bundle.getString(tex));
		}
		this.scope = sco;
		if (scope == RECORD_SCOPE) {
			setEnabled(false);
		}
		if (tid != null) {
			AppEntry ap = AppConstants.getConstant(tid);
			if (ap != null) {
				String sd = "<html>" + CoreUtilities.getInsertedBR(
					(String) ap.getValue(), 80) + "</html>";
				putValue(SHORT_DESCRIPTION, sd);
			}
		}
	}
	/** retorna alcance de esta accion. 
	 * 
	 * @return alcance
	 */
	public int getScope() {
		return scope;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public abstract void actionPerformed(ActionEvent arg0); 

	/** Retorna un nuevo dialogo de entrada con valores por defecto.  
	 * 
	 * @param pane - panel con componentes
	 * @param dft - pulsador por defecto
	 * @param tit - id de texto para la barra de titulo
	 * 
	 * @return dialogo
	 */
	public JDialog getDialog(JComponent pane, JButton dft, String tit) {
		JDialog dialog = new JDialog(DIMain.frame, true);	
		dialog.setContentPane(pane);
		dialog.getRootPane().setDefaultButton(dft);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		dialog.setTitle(getString(tit));
		return dialog;
	}
	
	/** similar a getDialgo(JComponent, JButton, String) pero con alguans
	 * caracteristicas especificas para el editor de formas.
	 * 
	 * @param pane - componente
	 * @param dft - boton por defecto
	 * @param tit - titulo
	 * @return - dialogo
	 */
	public JDialog getDialogForEditor(JComponent pane, JButton dft, String tit) {
		
		// algunos modelos apple tienen varios monitores uno junto a otro
		Rectangle bound = new Rectangle();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		for (int j = 0; j < gs.length; j++) { 
			GraphicsConfiguration[] gc = gs[j].getConfigurations();
			for (int i=0; i < gc.length; i++) {
				bound = bound.union(gc[i].getBounds());
			}
		} 		
		JDialog dialog = getDialog(pane, dft, tit);
		dialog.setResizable(true);
		Rectangle dlgrec = dialog.getBounds();
		bound.x = dlgrec.x < 0 ? 0 : dlgrec.x;  
		bound.y = dlgrec.y < 0 ? 0 : dlgrec.y;
		bound.width = dlgrec.width < bound.width ? dlgrec.width : bound.width;
		bound.height = dlgrec.height < bound.height ? dlgrec.height : bound.height;
		dialog.setBounds(bound);    
		return dialog;
	}
	
	/** obtiene <code>String</code> desde <code>ResourceBundle</code>
	 * 
	 * @param arg - key
	 * @return <code>String</code> 
	 */
	public String getString(String arg) {
		return DIMain.bundle.getString(arg);
	}
}
