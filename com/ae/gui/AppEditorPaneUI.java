/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

import com.incors.plaf.kunststoff.*;

/** Altera <code>paintBackground(Graphics)</code> para <code>JEditorPane</code>. Esta clase solo 
 * es instalada para el componente informativo dentro el <code>RightPanel</code>
 * 
 */
public class AppEditorPaneUI extends BasicEditorPaneUI {

	protected JComponent myComponent;
	private Color leftColor, rightColor;
	private JPanel container;

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new AppEditorPaneUI(c);
	}
	
	/** para que solo sea usado desde esta misma clase
	 * 
	 *
	 */
	private AppEditorPaneUI() {
		super();
		this.leftColor = Color.WHITE;
		this.rightColor = new Color(230, 230, 200);
	}

	protected AppEditorPaneUI(JComponent c) {
		this();
		myComponent = c;
	}
	
	/** usar este constructor 
	 * 
	 * @param ep - instancia de <code>JEditorPane</code> 
	 * @param con - instancia de <code>JPanel</code> de donde se obtiene el 
	 * color a degradar 
	 * 
	 */
	public AppEditorPaneUI(JComponent ep, JPanel con) {
		this();
		this.myComponent = ep;
		this.container = con;
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicTextUI#paintBackground(java.awt.Graphics)
	 */
	protected void paintBackground(Graphics g) {
		super.paintBackground(g);
		leftColor = myComponent.getBackground();
		rightColor = container.getBackground();
		Rectangle rect =
			new Rectangle(myComponent.getWidth() / 3, 0, myComponent.getWidth(), myComponent.getHeight());
		KunststoffUtilities.drawGradient(g, leftColor, rightColor, rect, false);
	}
}