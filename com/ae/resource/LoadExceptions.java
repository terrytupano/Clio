/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.resource;

import java.util.*;

import org.jdom.*;

import com.ae.core.*;

/** compila el archivo de exepciones y los carga dentro de la table interna
 * de exepciones de la aplicacion. esta esta en 
 * <code>PrevalentSystemManager</code>
 * 
 * 
 */
public class LoadExceptions { //implements Runnable {

	public static void run() {
		Document doc = PrevalentSystemManager.getXMLDocument(
			ResourceUtilities.getPrevaylerPath() + "/qmsgf.xml");
		List l = doc.getRootElement().getChildren();
		for (int c = 0; c < l.size(); c++) {
			Element e = (Element) l.get(c);
			String msgid = e.getAttributeValue("msgId");
			String mt = e.getAttributeValue("msgType");
			String msgtxt = e.getAttributeValue("msgText");
			if (mt.equals("w")) {
				PrevalentSystemManager.exceptions.put(msgid, new WarningException(msgtxt));
			}
			if (mt.equals("a")) {
				PrevalentSystemManager.exceptions.put(msgid, new ActionException(msgtxt));
			}
			if (mt.equals("c")) {
				PrevalentSystemManager.exceptions.put(msgid, new CriticalException(msgtxt));
			}
			if (mt.equals("i")) {
				PrevalentSystemManager.exceptions.put(msgid, new InformationException(msgtxt));
			}
		}
	}
}