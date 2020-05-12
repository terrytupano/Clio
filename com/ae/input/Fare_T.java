/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.*;

/** tabla practica de las tarifas del impuesto sobre las rentas
 * 
 */
public class Fare_T extends JPanel {

	/** Constructor sin argumentos
	 *
	 */
	public Fare_T() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(getTable("article50Table", "Tarifa Nro. 1"));
		add(getTable("article52Table", "Tarifa Nro. 2"));
		
		// tarifa 3
		Vector vc = new Vector();
		vc.add(DIMain.bundle.getString("d02"));
		Vector vr = new Vector(); 
		Vector r = new Vector();
		AppEntry[] fare = AppConstants.getConstantsOfType("article53Table");

		String[] de = DIMain.bundle.getString("m30").split(";");
		String va = ((String) fare[0].getValue());
		r.add(de[0] + va + de[1]);
		vr.add(r);

		r = new Vector();
		de = DIMain.bundle.getString("m31").split(";");
		va = ((String) fare[1].getValue());
		r.add(de[0] + va + de[1]);
		vr.add(r);

		JTable jt = new JTable(new AppDefaultTableModel(vr, vc));
		jt.setEnabled(false);
		JPanel jp3 = new JPanel(new BorderLayout());
		jp3.add(new JLabel("Tarifa Nro. 3"), BorderLayout.NORTH);
		jp3.add(new JScrollPane(jt), BorderLayout.CENTER);
		
		add(jp3);
		
	}
	
	/** retorna panel con tabla de la tarifa pasada como argumento
	 * 
	 * @param far - tarifa 
	 * @param tit - titulo de grupo
	 * @return panel
	 */
	private JPanel getTable(String far, String tit) {
		Vector vc = new Vector();
		vc.add(DIMain.bundle.getString("d02"));
		vc.add("%");
		vc.add(DIMain.bundle.getString("s13"));
		
		Vector vr = new Vector();
		AppEntry[] fare = AppConstants.getConstantsOfType(far);
		for (int e = 0; e < fare.length; e++) {
			Vector r = new Vector();
			String[] de = DIMain.bundle.getString("m29").split(";");
			String[] dh = ((String) fare[e].getKey()).split(";");
			String[] ps = ((String) fare[e].getValue()).split(";");
			r.add(de[0] + dh[0] + de[1] + dh[1] + de[2]);
			r.add(ps[0]);
			r.add(ps[1]);
			vr.add(r);
		}
		JTable jt = new JTable(new AppDefaultTableModel(vr, vc));
		GUIUtilities.fixTableColumn(jt, new int[] {330, 10, 20});
		jt.setEnabled(false);
		JPanel jp1 = new JPanel(new BorderLayout(4, 0));
		jp1.add(new JLabel(tit), BorderLayout.NORTH);
		jp1.add(new JScrollPane(jt), BorderLayout.CENTER);
		jp1.add(Box.createVerticalStrut(4), BorderLayout.SOUTH);
		return jp1;
	}
}