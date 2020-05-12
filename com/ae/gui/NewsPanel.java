/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.ae.core.DIMain;
import com.ae.resource.ResourceUtilities;

/** Componente encargado de presentar informacion directamente localizada
 * en w3.clio-ve.com/news.html
 * 
 */
public class NewsPanel extends JPanel {

	private Color bg_color = Color.WHITE;
	private JEditorPane news_jep;

	public NewsPanel() {
		super(new BorderLayout());
		this.news_jep = new JEditorPane();
		setBackground(bg_color);
		JLabel jl = new JLabel(DIMain.bundle.getString("n13"), 
			ResourceUtilities.getIcon("NewsIcon32"), JLabel.LEFT);
		Font f = new Font("Times New Roman", Font.BOLD, 20);
		jl.setFont(f);
		jl.setBorder(new EmptyBorder(5, 5 ,5 ,5));
		
		add(jl, BorderLayout.NORTH);
		JScrollPane jsp = new JScrollPane(news_jep);
		jsp.setBorder(null); 
		add(jsp, BorderLayout.CENTER);
		setBorder(new LineBorder(Color.gray));
		
		Dimension dim = new Dimension(450, 250);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
	}
	
	/** verifica si esta instalcion esta conectada a internet. la
	 * verificacion es solo establecer la pagina predetermimada de 
	 * noticias. si no se establece, se asume que no hay coneccion.
	 * 
	 * @return <code>true</code> si hay coneccion.
	 */
	public boolean isConnected() {
		boolean c = false;
		try {
			news_jep.setEditable(false);
			news_jep.setPage(DIMain.bundle.getString("n10"));
			c = true;
		} catch(UnknownHostException uhe) {
			// no hay coneccion
		} catch(Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
		return c;
	}
}