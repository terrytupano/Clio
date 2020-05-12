/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.gui.*;
import com.ae.resource.*;

import org.jdom.*;

/** Este panel contiene los componentes visuales necesarios para calcular y
 * emitir una orden o recivo para clave de adquisicion 
 * 
 */
public class PurchasePanel extends JPanel implements TableModelListener {
	
	private NumberFormat nFormat;
	private String pattern, s_record, s_record1;
	private TableModel tableModel;
	private PurchaseTable pTable;
	private AppEntry[] iForms;
	private SendRequestAction sendRequest;

	/** nueva instancia
	 * 
	 */	
	public PurchasePanel() {
		super();
		this.pattern = 	
			"<tr> <td><name></td> <td><quantity></td> <td><price></td> </tr>";
		this.nFormat = NumberFormat.getInstance();
		this.iForms = ResourceUtilities.getInstalledForms();
		this.pTable = new PurchaseTable(false);
		JTable jt = pTable.getTable();
		this.tableModel = jt.getModel();
		tableModel.addTableModelListener(this);
		JScrollPane jsp1 = new JScrollPane(jt);
		jsp1.setPreferredSize(new Dimension(500, 150));
		
		JPanel b2 = pTable.getTotalPanel();
		
//		Box b3 = Box.createHorizontalBox();
//		b3.add(new JButton(new SavePurchase()));
//		b3.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
//		b3.add(new JButton(new EmailAction(this)));
//		b2.add(b3, BorderLayout.EAST);
		this.sendRequest = new SendRequestAction(this);
		sendRequest.setEnabled(false);
		b2.add(new JButton(sendRequest), BorderLayout.EAST);

		GUIUtilities.setEmptyBorder(b2);
		b2.setBorder(new CompoundBorder(
			new TitledBorder(DIMain.bundle.getString("s06")), b2.getBorder()));

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(b2);
		add(jsp1);		
	}

	/** Cuando el tipo de envento es <code>ableModelEvent.UPDATE</code>
	 * actualiza el contenido de los datos de sustitucion de la variable
	 * "record" dentro de la orden de compra. Ademas, mantiene la secuencia 
	 * que sera usada como contenido de el archivo "consumer". este es una 
	 * secuenca criptoId#... 
	 * Este metodo tambien habilita /inhabilita la accion Solicitar.
	 * 
	 * @param tme - <code>TableModelEvent</code>
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */	
	public void tableChanged(TableModelEvent tme) {
		if (tme.getType() == TableModelEvent.UPDATE) {
			s_record = s_record1 = "";
			int cost, quant;
			for (int r = 0; r < tableModel.getRowCount(); r++) {
				cost = ((Integer) tableModel.getValueAt(r, 2)).intValue();
				quant = ((Integer) tableModel.getValueAt(r, 1)).intValue();
				if (quant > 0) {
					String pat1 = pattern.toString();
					pat1 = pat1.replaceAll("<name>", (String) tableModel.getValueAt(r, 0));
					pat1 = pat1.replaceAll("<quantity>", nFormat.format(quant));
					pat1 = pat1.replaceAll("<price>", nFormat.format(cost));
					s_record += pat1;
					
					// porque estan ordenadas 
					Hashtable h = PrevalentSystemManager.getFormElements(
						(String)iForms[r].getKey());
					Element e = (Element) h.get("form");
					s_record1 += e.getAttributeValue("cryptoId") + quant;
				}
			}
		}
		sendRequest.setEnabled(!s_record1.equals(""));
	}

	/** Retorna <code>Hashtable</code> con los datos obtenidos por este panel de
	 * entrada. la clave de cada elemento es el nombre de la variables y el valor
	 * el valores de las variables.
	 * 
	 * nota: ojo con variable <criptoid#>. esta contiene la codificacion de 
	 * la solicitud. 
	 * 
	 * @return - lista de nombre-valor de variables de sustitucion.
	 */	
	public Hashtable getPurchaseData() {
		Hashtable ht = new Hashtable();
		ht.put("<date>", new Date());
		s_record.replaceAll("<HTML>", "");
		s_record.replaceAll("</HTML>", "");
		ht.put("<records>", s_record);
		ht.put("<total>", pTable.getTotalString());
		ht.put("<criptoid#>", s_record1);
		return ht;
	}
	
	/** accion que abre <code>JFileChooser</code> para permitir al usuario
	 * cuardar la orden de compra
	 * 
	 */
	public class SavePurchase extends AppAbstractAction {
	
		/** nueva accion
		 * 
		 */
		public SavePurchase() {
			super("g01", "Save", AppAbstractAction.NO_SCOPE, null);
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int x = jfc.showSaveDialog(null);
			if (x == JFileChooser.APPROVE_OPTION) {
				File df = jfc.getSelectedFile();
//				String fn = jfc.getSelectedFile().getName();
				
				// se asume que no se colocara extencion
//				int ip = fn.lastIndexOf('.');
//				fn = fn.substring(0, (ip == -1) ? fn.length() : ip);
				String qfn = jfc.getSelectedFile().getAbsolutePath();
				try {
//					CoreUtilities.makeConsumerFiles(qfn, s_record1, getPurchaseData());
				} catch (Exception e) {
					Logger.getLogger("").logp(Level.SEVERE, null, null,	e.getMessage(), e);
				}
	
			/*	
			String sf = CoreUtilities.replaceHTMLVariables(
				"RequestOrder.htm", getPurchaseData());
			String fn = jfc.getSelectedFile().getName();
			int ip = fn.indexOf('.');
			fn = fn.substring(0, (ip == -1) ? fn.length() : ip);
			File df = jfc.getSelectedFile();
				try {
					df = new File(df.getParent(), fn + ".htm");
					df.createNewFile();
					BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(df));
					bos.write(sf.getBytes());
					bos.close();
					CoreUtilities.makeConsumerFiles(df.getParent() + fn, s_record1, getPurchaseData());
				} catch (Exception e) {
					String sfn = "File: " + fn;
					Logger.getLogger("").logp(Level.SEVERE, "SavePurchase", 
						"actionPerformed(ActionEvent)",	e.getMessage() + sfn, df);
				}
		*/	
			}
		}
	}
}