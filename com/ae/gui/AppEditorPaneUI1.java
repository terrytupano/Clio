/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

import com.ae.resource.*;

/** Altera <code>paintBackground(Graphics)</code> para <code>JEditorPane</code>. 
 * Esta clase solo es instalada para el <code>JEditorPane</code> en <code>WellcomePanel</code>.
 * dibija el logo de la aplicacion en la parte inferior derecha. atras del texto
 * 
 * 
 */
public class AppEditorPaneUI1 extends BasicEditorPaneUI {

	protected JComponent myComponent;
	private ImageIcon image;
	private Color bg_col;

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new AppEditorPaneUI1(c);
	}

	/** nueva instancia.
	 * 
	 * @param c - componente 
	 */
	public AppEditorPaneUI1(JComponent c) {
		super();
		this.image = ResourceUtilities.getIcon("Clio.background");
		this.myComponent = c;
		this.bg_col = new Color(250, 250, 242);
	}
	

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTextUI#paintBackground(java.awt.Graphics)
	 */
	protected void paintBackground(Graphics g) {
		super.paintBackground(g);
		Graphics g2d = (Graphics2D) g;
		int width = myComponent.getWidth();
		int height = myComponent.getHeight();
		int ix = width - image.getIconWidth();
		int iy = height - image.getIconHeight();
//		g2d.setColor(bg_col);
//		g2d.fillRect(0, 0, width, height); 
		g2d.drawImage(image.getImage(), ix, iy, null);
	}
}