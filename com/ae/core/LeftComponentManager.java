/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.awt.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;

import com.ae.gui.*;
import com.ae.gui.calendar.*;
import com.ae.resource.*;

/** Clase encargada de manegar los eventos asociados con los componentes colocados
 * en la barra de acceso de funciones de la aplicacion (situada a la izquierda) 
 * 
 */
public class LeftComponentManager implements TreeSelectionListener  {
	
	// contaier donde se colocan los componentes derechos
	private static JPanel rContainer;
	
	public static int WELLCOME = 0;
	public static int DUTY_CALENDAR = 1;
	// inventario de objetos
	private NodeUserObjectInfo[] all_nodes;
	// ultimo componente 
	private JPanel aPanel;
//	private JComponent aPanel;
	// lista con formas instaladas
	private Hashtable iForms;
	private TreeNodeCellRenderer node_renderer;
	// ultima opcion 
	private NodeUserObjectInfo selectedNode;
	private JTree tree;
	private Cursor waitCursor, defaultCursor;
	private ResourceBundle bundle;
	
	/** Nuevo manejador de componente izquierdo
	 * 
	 * @param jp
	 */
	public LeftComponentManager(JPanel jp) {
		rContainer = jp;
		this.selectedNode = null;
		this.aPanel = null;
		this.waitCursor = new Cursor(Cursor.WAIT_CURSOR);
		this.defaultCursor = Cursor.getDefaultCursor();
		this.bundle = DIMain.bundle;

		DefaultMutableTreeNode root = getNode(new NodeUserObjectInfo("Root"));

		//Configuracion de la aplicacion
		root.add(getNode(new NodeUserObjectInfo()));
		DefaultMutableTreeNode conf = getNode(new NodeUserObjectInfo(
			DIMain.bundle.getString("c07"),
			null, null));
		NodeUserObjectInfo nuoi = new NodeUserObjectInfo(
			DIMain.bundle.getString("v02"), 
			"p20", 
			"SysVar");
		conf.add(getNode(nuoi));
		nuoi = new NodeUserObjectInfo(DIMain.bundle.getString("c08"), 
			"p21",
			"TaxPayer");
		conf.add(getNode(nuoi));
		nuoi = new NodeUserObjectInfo(DIMain.bundle.getString("c09"), 
			"p22",
			"Calendar");
		conf.add(getNode(nuoi));
		
		root.add(conf);
		
		// informacion contable
		root.add(getNode(new NodeUserObjectInfo()));
		DefaultMutableTreeNode accounting = getNode(new NodeUserObjectInfo(
			DIMain.bundle.getString("c10"),
			null, null));
		NodeUserObjectInfo accn = new NodeUserObjectInfo(
			DIMain.bundle.getString("c11"), 
			"p25", 
			"account-clio");
		accounting.add(getNode(accn));
		accn = new NodeUserObjectInfo(DIMain.bundle.getString("t05"), 
			"p26", 
			"Voucher");
		accounting.add(getNode(accn));
		root.add(accounting);
		
		
		// Formas instaladas
		root.add(getNode(new NodeUserObjectInfo()));
		DefaultMutableTreeNode forms = getNode(new NodeUserObjectInfo(
			DIMain.bundle.getString("f02"),
			null, null));
		AppEntry[] fn = ResourceUtilities.getInstalledForms();
		this.iForms = new Hashtable(fn.length);
		for (int k = 0; k < fn.length; k++) {
			NodeUserObjectInfo fmni = new NodeUserObjectInfo(
				(String) fn[k].getValue(), 
				"p29", 
				"FormIcon");
			forms.add(getNode(fmni));
			iForms.put(fmni, fn[k]);
		}
		root.add(forms);
		
		this.tree = new JTree(root);
		BasicTreeUI btu = (BasicTreeUI) tree.getUI();
		btu.setLeftChildIndent(0);
		btu.setRightChildIndent(0);
		
//		tree.setUI(new LeftComponentTreeUI());
		
		tree.setCellRenderer(new TreeNodeCellRenderer());
		tree.setRootVisible(false);
		tree.setEditable(false);
		tree.addTreeSelectionListener(this);
		for (int x = 0; x < tree.getRowCount(); x++) {
			tree.expandRow(x);
		}
		Dimension d = TreeNodeCellRenderer.cell_dim;
		tree.setMinimumSize(d);
		tree.setPreferredSize(d);
		tree.setMaximumSize(d);
		tree.setBorder(new LineBorder(TreeNodeCellRenderer.back_color, 4));
		tree.setBackground(TreeNodeCellRenderer.back_color);
		
		// hace inventario de total de opciones
		int nc = tree.getRowCount();
		this.all_nodes = new NodeUserObjectInfo[nc];
		for (int x = 0; x < nc; x++) {
			TreePath tp = tree.getPathForRow(x);
			DefaultMutableTreeNode tn = (DefaultMutableTreeNode) tp.getLastPathComponent();
			all_nodes[x] = (NodeUserObjectInfo) tn.getUserObject(); 
		}
	}
	
	/** retorna todos los nodos manejados por esta clase.
	 * 
	 * @return nodos
	 */
	public NodeUserObjectInfo[] getNodes() {
		return all_nodes;
	}
	
	/** crea una nueva instancia del componente asocioado con el nodo y lo coloca
	 * en el contenedor derecho. 
	 * 
	 * @param nuoi - nodo 
	 */
	public void createRightComponent(NodeUserObjectInfo nuoi) {
		try {
			DIMain.frame.setCursor(waitCursor);
			Class cls = nuoi.getRightComponentClass();
			// por click en nodos espacios entre grupos
			if (cls != null) {
				selectedNode = nuoi;
				JPanel rip = null;
				rip = (JPanel) cls.newInstance();
//				JComponent rip = null;
//				rip = (JComponent) cls.newInstance();
				toRight(rip);
/*
				if (aPanel != null) {
					rContainer.remove(aPanel);
				}
				aPanel = rip;
				rip.setBorder(new ShadowedBorder());
				rContainer.add(rip, BorderLayout.CENTER);
				*/
			}
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
		DIMain.frame.setCursor(defaultCursor);
	}
	
	/** Retorna el componente creado por este gestor para presentar las opciones
	 * accecibles al usuario
	 * 
	 * @return componente con opciones
	 */
	public JComponent getLeftComponent() {
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(tree, BorderLayout.CENTER);
		jp.setBorder(new ShadowedBorder());
		return jp;
	}
	
	/* 
	 * crea nodo con valores por omision y conf. cellRender para que todos 
	 * tengan el mismo tamaño que el mayor de todos ellos
	 */	
	private DefaultMutableTreeNode getNode(NodeUserObjectInfo nuoi) {
		DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(nuoi);
		return dmtn;
		
	}
	
	/** Retorna <code>AppEntry</code> con el identificador y el nombre de la forma
	 * seleccionada. Si la seleccion no indica una forma, retorna null. 
	 * 
	 * @return forma que identifica este nodo.
	 */
	public AppEntry getSelectedForm() {
		return (AppEntry) iForms.get(selectedNode);
	}

	/** presenta componentes pre-establecidos dentro del panel derecho.
	 * Cualquier opcion previamente seleccionada es desmarcada.
	 * 
	 * @param cmp - componente 
	 */	
	public void setRightComponent(int cmp) {
		JPanel rip = null;
		if (cmp == WELLCOME) {
			rip = new WellcomePanel();
		}
		if (cmp == DUTY_CALENDAR) {
			rip = new DutyCalendar();
		}
		tree.clearSelection();
		toRight(rip);
	}
	
	private void toRight(JPanel rip) {
		if (aPanel != null) {
			rContainer.remove(aPanel);
		}
		aPanel = rip;
		aPanel.setBorder(new ShadowedBorder());
		rContainer.add(aPanel, BorderLayout.CENTER);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent tse) {
		TreePath tp = tse.getNewLeadSelectionPath();
		// por clearSelection()
		if (tp != null) {
			DefaultMutableTreeNode dmtn = 
				(DefaultMutableTreeNode) tp.getLastPathComponent();
			if (dmtn.isLeaf()) {
				createRightComponent((NodeUserObjectInfo) dmtn.getUserObject());
			} else {
				if (tree.isExpanded(tse.getPath())) {
					tree.collapsePath(tse.getPath());
				} else {
					tree.expandPath(tse.getPath());
				}
			}
		}
	}
}