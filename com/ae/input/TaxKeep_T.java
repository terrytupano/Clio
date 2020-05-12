/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.jdom.*;

import com.ae.core.*;
import com.ae.evaluate.functions.string.*;
import com.ae.gui.*;
import com.ae.resource.*;

/** Presenta una lista de las retenciones. Esta clase presenta un componente
 * con el tipo de a los cuales es aplicable las retenciones segun el decreto.
 * Tambien un componente con los años de los cuales se conoce el monto de las unidades tributarias
 * para permitir a la funcion <code>TaxKeeping</code> calcular el 
 * "monto desde" y el "sustraendo" segun aplique.
 * 
 */
public class TaxKeep_T extends JPanel implements ActionListener {

	private JComboBox jcbYear, jcbBene;
	private Vector treeCol, fourCol;
	private List docChild;
	private JTable table; 
	private TaxKeeping taxkeeping;
	private NumberFormat numberf;
	private boolean fourCols;
	
	/** Constructor sin argumentos
	 *
	 */
	public TaxKeep_T() {
		super(new BorderLayout());
		this.taxkeeping = new TaxKeeping();
		this.treeCol = new Vector();
		treeCol.add(DIMain.bundle.getString("d02"));
		treeCol.add(DIMain.bundle.getString("b03"));
		treeCol.add(DIMain.bundle.getString("r07"));
		this.fourCol = new Vector(treeCol);
		fourCol.add(DIMain.bundle.getString("d03"));
		this.fourCols = false;
		this.numberf = NumberFormat.getInstance();

		this.jcbBene = new JComboBox(AppConstants.getConstantsOfType("keepTo"));
		jcbBene.setToolTipText(
			(String) AppConstants.getConstant("keepTo").getValue());
		jcbBene.addActionListener(this);
		this.jcbYear = new JComboBox(AppConstants.getYears(10));
		jcbYear.setToolTipText(
			(String) AppConstants.getConstant("year").getValue());
		jcbYear.addActionListener(this);
		JToolBar jtb = new JToolBar();
		jtb.setFloatable(false);
		jtb.add(new JLabel(DIMain.bundle.getString("r08") + "  "));
		jtb.add(jcbBene);
		jtb.addSeparator();
		jtb.add(new JLabel(DIMain.bundle.getString("a24") + "  ")); 
		jtb.add(jcbYear);

		Document d =
			PrevalentSystemManager.getXMLDocument(
				ResourceUtilities.getPrevaylerPath() + "1808List.xml");
		Element ro = d.getRootElement();
		this.docChild = ro.getChildren();
		
		this.table = new JTable();
		table.setEnabled(false);
		JScrollPane jsp = new JScrollPane(table);

		add(jtb, BorderLayout.NORTH);
		add(jsp, BorderLayout.CENTER);

		jcbYear.setSelectedIndex(jcbYear.getItemCount() - 1);
	}
	
	private void updateTable() {
		String fid = (String) ((AppEntry) jcbBene.getSelectedItem()).getKey();
		String y = (String) ((AppEntry) jcbYear.getSelectedItem()).getKey();
		
		fourCols = fid.equals("pn-r") || fid.equals("pj-d");
		
		Vector vr = new Vector();
		for (int e = 0; e < docChild.size(); e++) {
			Element el = (Element) docChild.get(e);
			String cod = el.getAttributeValue("code"); 
			if (cod.startsWith(fid)) {
				String anl = cod.substring(fid.length());
				Vector r = new Vector();
				r.add(el.getAttributeValue("label"));
				
				String[] bp = el.getAttributeValue("text").split(";");
				// base imponible
				r.add(bp[0] + "%");
				
				// % ret - sustraendo y monto base
				String re = DIMain.bundle.getString("t09");
				double[] sb = new double[] {0, 0};
				if (!bp[1].equals("T2")) {
					try {
						sb = taxkeeping.getSubstractAndBase(fid, y + "1231", Double.parseDouble(bp[1]));
						re = bp[1] + "%";
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				r.add((sb[0] > 0) ? re + " - " + numberf.format(sb[0]) : re);
				r.add(new Double(sb[1]));
				vr.add(r);
			}
		}
		table.setModel(new AppDefaultTableModel(vr, 
			fourCols ? fourCol : treeCol));
		GUIUtilities.fixTableColumn(table, 
			fourCols ? new int[] {300, 40, 130} : new int[] {400});
	}
	
	public void actionPerformed(ActionEvent ae) {
		updateTable();
	}

}