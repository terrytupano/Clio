/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;

import javax.swing.border.*;

/** Borde usado por <code>JTree</code> dentro del <code>LeftComponentPanel</code>
 * este esta diceñado para dibujar una linea solo en donde se indique conservando
 * el espaciado.
 */
public class TreeNodeBorder implements Border {
	
	private Insets insets;
	private Color color;
	private int[] tlbr;

	/** nueva instancia
	 * 
	 * @param t - dibujar linea superior
	 * @param l - dibujar linea izquierda
	 * @param b - dibujar linea inferior
	 * @param r - dibujar linea derecha
	 * @param c - color
	 */
	public TreeNodeBorder(int t, int l, int b, int r, Color c) {
		this.insets = new Insets(1, 1, 1, 1);
		this.color = c;
		this.tlbr = new int[] {t, l, b, r};
	}
	/* (non-Javadoc)
	 * @see javax.swing.border.Border#isBorderOpaque()
	 */
	public boolean isBorderOpaque() {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.Border#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
		int height) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		// top
		if (tlbr[0] == 1) {
			g2d.drawLine(x, y, x + width, y); 
		}
		// left 
		if (tlbr[1] == 1) {
			g2d.drawLine(x, y, x, y + height); 
		}
		// bottom 
		if (tlbr[2] == 1) {
			g2d.drawLine(x, y + height - 1, x + width, y + height - 1); 
		}
		// right 
		if (tlbr[3] == 1) {
			g2d.drawLine(x + width - 1, y, x + width - 1, y + height - 1); 
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
	 */
	public Insets getBorderInsets(Component c) {
		return insets;
	}

}
