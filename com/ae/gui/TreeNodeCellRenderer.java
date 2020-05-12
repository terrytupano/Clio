/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;
import javax.swing.*;
//import javax.swing.border.*;
import javax.swing.tree.*;

import com.ae.core.*;

/** Especializacion de <code>DefaultTreeCellRenderer</code> para los 
 * elementos del navegador
 * 
 */
public class TreeNodeCellRenderer extends DefaultTreeCellRenderer {
	
	public static Color back_color = new Color(240, 240, 240);
	public static Dimension cell_dim = new Dimension(200, 0);
	
	private Color back_sel_color = new Color(152, 169, 201);
	private Color back_cell_color = new Color(202,211, 227);
	private Color back_title_color = new Color(79, 166, 225);
	private Color fore_title_color = Color.WHITE;
	private Dimension elemt_dim;
	private DefaultMutableTreeNode ancestor;

	
	public TreeNodeCellRenderer() {
		super();
		setOpaque(true);
		int dif = cell_dim.width - 12;
		this.elemt_dim = new Dimension(dif, 18);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
		boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
			hasFocus);
		DefaultMutableTreeNode tnod = (DefaultMutableTreeNode) value;
		NodeUserObjectInfo ninf = (NodeUserObjectInfo) tnod.getUserObject();
		Font f = getFont();
		setText(ninf.getName());
		setFont(f.deriveFont(Font.PLAIN));
		setIcon(ninf.getIcon());
		
		setMinimumSize(elemt_dim);
		setPreferredSize(elemt_dim);
		setMaximumSize(elemt_dim);
	
		// si es titulo de grupo
		if (!leaf) {
			ancestor = tnod;
			setFont(f.deriveFont(Font.BOLD));
			setForeground(fore_title_color);
			setBackground(back_title_color);
			setBorder(new TreeNodeBorder(1, 1, 1, 1, back_title_color.darker()));
//			setBorder(new MatteBorder(1, 1, 1, 1, back_title_color.darker()));
		} 

		// si es opcion
		if (leaf) {
			setForeground(Color.BLACK);
			setBackground(back_color);
			
			// no es espacio
			if (!(ninf.getName().equals(" "))) {
				setBackground(back_cell_color);
				if (tnod == ancestor.getLastLeaf()) {
					setBorder(new TreeNodeBorder(0, 1, 1, 1, back_title_color.darker()));
//					setBorder(new MatteBorder(0, 1, 1, 1, back_title_color.darker()));
				} else {
					setBorder(new TreeNodeBorder(0, 1, 0, 1, back_title_color.darker()));
//					setBorder(new MatteBorder(0, 1, 0, 1, back_title_color.darker()));
				}
				if (sel) {
					setBackground(back_sel_color);
				}
			} else {
				setBorder(null);
			}
		}
		return this;
	}
}