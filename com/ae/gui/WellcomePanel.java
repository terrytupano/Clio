/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;

import com.ae.core.*;
import com.ae.resource.*;

/** Componente designado como de bienvenida a la aplicacion. Este componente 
 * intercepta y analiza los enlaces contenidos dentro de esta pagina. Cuando
 * se diceña la pagina, el atributo <code>href</code> debe ser igual al
 * texto del componente que desea presentar. Estos pueden estar dentro de
 * la barra de herramientas principal o ser un nodo dentro de 
 * <code>LeftComponentManager</code> 
 * 
 */
public class WellcomePanel extends JPanel implements HyperlinkListener{
	
	private Color bg_color = Color.WHITE;
	private ImageIcon image;

	public WellcomePanel() {
		super(new BorderLayout());
		this.image = ResourceUtilities.getIcon("Clio.background");
		
//		setBackground(bg_color);
		JEditorPane jep = new JEditorPane();
		jep.setUI(new AppEditorPaneUI1(jep));
		jep.setEditable(false);
		jep.addHyperlinkListener(this);
		try {
			jep.setPage(ResourceUtilities.getURL("Wellcome"));
		} catch(Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
				
		add(jep, BorderLayout.CENTER);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
	 */	
	public void hyperlinkUpdate(HyperlinkEvent e) {
		
		// este metodo compara la descripcion del atributo HREF dentro del
		// documento comparandola primero con elementos dentro de la barra 
		// de herramientas y luego con elementos dentro del panel izquierdo 
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			String cld = e.getDescription();
			
			// enlace se refiere a una accion dentro de la barra de herramientas.
			Component[] jc = DIMain.toolBar.getComponents();
			for(int c = 0; c < jc.length; c++) {
				if (jc[c] instanceof JButton && 
					((JButton) jc[c]).getText().equals(cld)) {
					((JButton) jc[c]).doClick();
				}
			} 
			
			// enalce se refiere a una clase
			NodeUserObjectInfo[] alln = DIMain.leftComponentManager.getNodes();
			for (int c = 0; c < alln.length; c++) {
				if (cld.equals(alln[c].getName())) {
					DIMain.leftComponentManager.createRightComponent(alln[c]); 
				}
			}
		}
	}	
}