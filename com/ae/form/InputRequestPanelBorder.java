/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;

/** Border para InputRequestPanel. El color del borde el el descrito en 
 * <code>panel.background</code>. Obtener las caracteristicas de la etiqueta que
 * funge como titulo para el <code>InputRequestPanel</code> tiene la intencion
 * de evitar que se presente un cuadro de otro color a su alrededor.
 */
public class InputRequestPanelBorder implements Border {

	private Insets insets;
	private Color panelColor, borderColor, titleColor; 
	private int direction;
	private GeneralPath generalP, rect;
	private int titleHeight;
	
	public InputRequestPanelBorder(Form f) {
		this.insets = new Insets(2, 15, 2, 15);
		this.panelColor = (Color) f.getAttribute("panel.background");
		this.borderColor = (Color) f.getAttribute("panel.foreground");
		this.direction = SwingConstants.WEST;
		this.rect = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		
		// atributos del titulo
		this.titleColor = (Color) f.getAttribute("title.background");
		JLabel jl = FormUtilities.getTitleLabel(f);
		jl.setText("X");
		this.titleHeight = (int) jl.getPreferredSize().getHeight();		
	}

	/* (non-Javadoc)
	 * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
	 */
	public Insets getBorderInsets(Component arg0) {
		return insets;
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
	public void paintBorder(Component arg0, Graphics arg1, int x, int y,
		int width, int height) {
		Graphics2D g2 = (Graphics2D) arg1;
		
		// hacia la derecha o la izquierda
		int c = height / 2;
		GeneralPath generalP = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		if (direction == SwingConstants.WEST) {
			int o = width - (insets.right);
			generalP.moveTo(o, c);						
			generalP.lineTo(width, c + insets.right - 5);
			generalP.lineTo(o, c + insets.left - 5);
			generalP.closePath();
		} else {
			generalP.moveTo(insets.left, c);						
			generalP.lineTo(0, c + insets.left - 5);
			generalP.lineTo(insets.left, c + insets.left - 5);
			generalP.closePath();
		}
		
		int cor = 3;
		rect.moveTo((insets.left - 2) + cor, 0);			
		//	/
		rect.lineTo((insets.left - 2), cor);					
		//	|
		rect.lineTo((insets.left - 2), (height - 1) - cor);
		// \		
		rect.lineTo((insets.left - 2) + cor, (height - 1));
		// -		
		rect.lineTo((width - insets.right + 1) - cor, (height - 1));
		// /
		rect.lineTo((width - insets.right + 1), (height - 1) - cor);
		// |
		rect.lineTo((width - insets.right + 1), cor);
		// \
		rect.lineTo((width - insets.right + 1) - cor, 0);
		// -
		rect.closePath();

		Area area = new Area(rect);
		area.add(new Area(generalP));
		
		g2.setClip(area);
		g2.setColor(panelColor);
		g2.fill(area);
		g2.setColor(titleColor);
		g2.fillRect(0, 1, width, titleHeight + 1);
		g2.setClip(0, 0, width, height);
		g2.setColor(borderColor);
		g2.draw(area);
	}
	
	/** Establece la direccion a la que apunta la flecha.
	 * 
	 * @param dir <code>SwingConstants.WEST o SwingConstant.EAST</code>
	 */	
	public void setArrow(int dir) {
		this.direction = dir;
	}

}
