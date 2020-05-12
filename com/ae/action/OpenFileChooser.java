/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.event.*;
import java.io.*;

import javax.swing.*;

/** accion que abre <code>JFileChooser</code> para permitir al usuari
 * seleccionar un archovo o directorio. La seleccion hecha en es notificada
 * al interezado usando <code>ConfirmationAction.actionPerformed2(AbstractAction)</code>
 * y puede obtener el nombre absoluto usando <code>getSelectedFile()</code>
 * 
 */
public class OpenFileChooser extends AppAbstractAction {
	private ConfirmationAction cAction;
	private File file;

	private int mode;

	/** nueva accion.
	 *  
	 * @param ca - interesado en la seleccion.
	 * @param m - modo de seleccion. Esta puede ser cualquiera de
	 * las modalidades de selecion descritas en 
	 * <code>JFileChooser.setFileSelectionMode(int)</code>
	 */
	public OpenFileChooser(ConfirmationAction ca, int m) {
		super("a11", "Open", AppAbstractAction.NO_SCOPE, "tt14");
		this.cAction = ca;
		this.mode = m;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(mode);
		int x = jfc.showOpenDialog(null);
		if (x == JFileChooser.APPROVE_OPTION) {
			this.file = jfc.getSelectedFile();
			cAction.actionPerformed2(this);
		}
	}

	/** retorna el nombre absoluto (calificado) del archivo/directorio s
	 * seleccionado. 
	 * 
	 * @return - nombre absoluto
	 */
	public String getSelectedFile() {
		return file.getAbsolutePath();
	}
}
