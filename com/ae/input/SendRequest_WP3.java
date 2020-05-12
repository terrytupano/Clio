/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.wizard.*;
import com.ae.resource.*;

/** Panel encargado de presentar un borrador con la solicitud de formas
 * 
 */
public class SendRequest_WP3 extends WizardPanel {

	private File[] htm_jar_f;
	private String absoluteP;
	private PurchasePanel purchaseP;
	private JEditorPane request_jep; 
	
	/** nueva instancia
	 *
	 */
	public SendRequest_WP3(PurchasePanel pp) {
		super(WizardPanel.NORMAL);
		this.purchaseP = pp;
		this.request_jep = new JEditorPane();
		request_jep.setEditable(false);
		setText(DIMain.bundle.getString("m19"));
		setInputComponent(new JScrollPane(request_jep));
		
	}
	
	/** retorna un vector donde:
	 * Vector.elementAt(0) - nombre dado para almacenar los datos (o null)
	 * Vector.elementAt(1) - archivo .htm con solicitud
	 * Vector.elementAt(2) - archivo .jar
	 *
	 * @return datos
	 * 
	 * @see com.ae.gui.wizard.WizardPanel#getData()
	 */
	public Object getData() {
		Vector v = new Vector();
		v.add(absoluteP);
		v.add(htm_jar_f[0]);
		v.add(htm_jar_f[1]);
		return v;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#initializePanel(com.ae.gui.wizard.WizardContainer)
	 */
	public void initializePanel(WizardContainer arg0) {
		Hashtable allData = (Hashtable) arg0.getPanel(1).getData();
		allData.putAll(purchaseP.getPurchaseData());
		this.absoluteP = (String) arg0.getPanel(2).getData();
		try {
			// para cambiar el url
			request_jep.setPage(ResourceUtilities.getURL("RequestOrderError"));
			this.htm_jar_f = CoreUtilities.makeConsumerFiles(absoluteP, allData);
			request_jep.setPage(htm_jar_f[0].toURL());
	
		} catch(Exception e) {
			AplicationException ae = (AplicationException) 
				PrevalentSystemManager.exceptions.get("msg101");
			ae.setMessage(ae.getMessage().replaceAll(";", e.getMessage()));
			arg0.showExceptionMessage(ae);
			Logger.getLogger("").logp(Level.SEVERE, null, null,	e.getMessage(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ae.gui.wizard.WizardPanel#validateFields()
	 */
	public AplicationException validateFields() {
		return null;
	}

}
