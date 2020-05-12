/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.jdom.*;

import com.ae.core.*;
import com.ae.gui.*;
import com.ae.resource.*;

/** presenta una lista de las unidades tributarias dentro de la aplicacion
 * 
 */
public class TributaryUnit_T extends JPanel {

	/** Constructor sin argumentos
	 *
	 */
	public TributaryUnit_T() {
		super(new BorderLayout());
		Vector vc = new Vector();
		vc.add(DIMain.bundle.getString("a24"));
		vc.add(DIMain.bundle.getString("g02"));
		vc.add(DIMain.bundle.getString("f06"));
		vc.add(DIMain.bundle.getString("v01"));
		Vector vr = new Vector();
		Document d =
			PrevalentSystemManager.getXMLDocument(
				ResourceUtilities.getPrevaylerPath() + "TUList.xml");
		Element ro = d.getRootElement();
		List l = ro.getChildren();
		for (int e = 0; e < l.size(); e++) {
			Vector r = new Vector();
			Element el = (Element) l.get(e);
			r.add(el.getAttributeValue("year"));
			r.add(el.getAttributeValue("gazzete"));
			r.add(new AppDate(el.getAttributeValue("code")));
			r.add(new Double(el.getAttributeValue("text")));
			vr.add(r);
		}
		JTable jt = new JTable(new AppDefaultTableModel(vr, vc));
		GUIUtilities.fixTableColumn(jt, new int[] {50, 330, 110});
		jt.setEnabled(false);
		add(new JScrollPane(jt), BorderLayout.CENTER);
		
	}
}