/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;

import javax.swing.border.*;

/** Border sombreado
 */
public class ShadowedBorder implements Border {
	
	private Insets insets;
	private Color[] colors;
	
	public ShadowedBorder() {
		this.insets = new Insets(1, 1, 4, 4);
		this.colors = new Color[] 
			{Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY};
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
		g2d.setColor(c.getParent().getBackground());
		g2d.fill(new Rectangle(x, y, width, height));
		g2d.setColor(colors[0]);
		g2d.drawRect(x, y, width - insets.right, height - insets.bottom);
		
		for (int b = 1; b < colors.length; b++) {
			g2d.setColor(colors[b]);
			g2d.drawLine(
				width - insets.right + b, 
				y + b, 
				width - insets.right + b, 
				height - insets.bottom + b);
			g2d.drawLine(
				width - insets.right + b, 
				height - insets.bottom + b, 
				x + b, height - insets.bottom + b); 
		}
/*		
		for (int b = colors.length; b > 0 ; b--) {
			g2d.setColor(colors[b - 1]);
			g2d.drawRect(x, y, width - insets.right + b, height - insets.right + b);
		}
		*/
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
	 */
	public Insets getBorderInsets(Component c) {
		return insets;
	}

}
