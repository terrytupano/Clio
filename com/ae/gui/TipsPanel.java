/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import com.ae.core.*;
import com.ae.gui.wizard.*;
import com.ae.resource.*;

/** Panel encargado de localizar, dar formato y presentar los "datos" del 
 * dia
 */
public class TipsPanel extends JPanel implements Navigator {
	
	private Color bg_color = Color.WHITE;
	private JEditorPane tipPane;
	private int tipID;

	public TipsPanel() {
		super(new BorderLayout());
		setBackground(bg_color);
		this.tipPane = new JEditorPane();
		this.tipID = 0;
		tipPane.setEditable(false);
		JLabel jl = new JLabel(DIMain.bundle.getString("s08"), 
			ResourceUtilities.getIcon("TipsIcon32"), JLabel.LEFT);
		Font f = new Font("Times New Roman", Font.BOLD, 20);
		jl.setFont(f);
		jl.setBorder(new EmptyBorder(5, 5 ,5 ,5));
		
		add(jl, BorderLayout.NORTH);
		JScrollPane jsp = new JScrollPane(tipPane);
		jsp.setBorder(null); 
		add(jsp, BorderLayout.CENTER);
//		add(tipPane, BorderLayout.CENTER);
		setBorder(new LineBorder(Color.gray));
		
		Dimension dim = new Dimension(400, 150);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
	}
	
	/** presenta el documento que contiene la informacion o el dato del dia
	 * 
	 * @param ti - identificador de dato
	 */
	public void showTip(int ti) {
		try {
			tipPane.setPage(ResourceUtilities.getURL("Tip" + ti));
			tipID = ti;
		} catch(Exception e) {
			// no importa
		}
	}
	
	/** retorna el identificador del "dato" que se esta presentando
	 * 
	 * @return id de "dato"
	 */
	private int getTipID() {
		return tipID;
	}

	/* (non-Javadoc)
	 * @see com.ae.gui.wizard.Navigator#hasNext()
	 */
	public boolean hasNext() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.ae.gui.wizard.Navigator#isFirst()
	 */
	public boolean isFirst() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.ae.gui.wizard.Navigator#next()
	 */
	public void next() {
		int ti = getTipID();
		showTip(++ti);
	}

	/* (non-Javadoc)
	 * @see com.ae.gui.wizard.Navigator#previous()
	 */
	public void previous() {
		int ti = getTipID();
		showTip(--ti);
	}

}