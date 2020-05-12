/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.jdom.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.gui.*;
import com.ae.resource.*;

/** Este panel contiene los componentes visuales encargados de presentar la
 * lista de formas disponibles dentro de la aplicacicon y permiter la entrada
 * de la clave de adquisicion.  
 * 
 */
public class StockPanel extends JPanel implements ConfirmationAction, DocumentListener {

	private AppEntry[] iForms;
	private JTextField key_jtf;
	private Load load;
	private OpenFileChooser openFC;
	private RightPanel rPanel;
	private Vector sCN;
	private AppTableModel stModel;
	private JTable stockT;

	/** constructor sin argumentos
	 * 
	 *
	 */	
	public StockPanel(RightPanel rp) {
		super();
		this.rPanel = rp;
		this.sCN = new Vector(2);
		sCN.add(DIMain.bundle.getString("f09"));
		sCN.add(DIMain.bundle.getString("c15"));
		this.iForms = ResourceUtilities.getInstalledForms();
		this.stModel = PrevalentSystemManager.getTableModel(new StockRecord());
		
		this.key_jtf = new JTextField();
		GUIUtilities.setDimensionForTextComponent(key_jtf, 30);
		key_jtf.getDocument().addDocumentListener(this);
//		Font f = key_jtf.getFont();
//		key_jtf.setFont(f.deriveFont((float) f.getSize() + 4));

		this.stockT = new JTable();
		stockT.setEnabled(false);
		updateStockTable();
		JScrollPane jsp = new JScrollPane(stockT);
		jsp.setPreferredSize(new Dimension(500, 150));
		this.load = new Load(this);
		this.openFC = new OpenFileChooser(this, JFileChooser.FILES_ONLY);
		Box b1 = Box.createHorizontalBox();
		b1.add(key_jtf);
		b1.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
		b1.add(new JButton(openFC));
		b1.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
		b1.add(new JButton(load));
		GUIUtilities.setEmptyBorder(b1);
		b1.setBorder(new CompoundBorder(
			new TitledBorder(DIMain.bundle.getString("a25")), b1.getBorder()));
				
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(b1);
		add(jsp);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.action.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */	
	public void actionPerformed2(AbstractAction aa) {
		if (aa == openFC) {
			key_jtf.setText(openFC.getSelectedFile());
		}
		if (aa == load) {
			load();
		}
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent e) {
		enableLoad();
	}

	/** habilita/desabilita componente de texto que contiene el nombre del
	 * archivo donde se encuentran las formas enviadas por el probeedor
	 *
	 */	
	private void enableLoad() {
		boolean b = key_jtf.getText().trim().equals("");
		load.setEnabled(!b);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent e) {
		enableLoad();
	}
	
	/** Este metodo desencripta la clave de adquisicion y 
	 * adiciona las cantidades descritas en la secuencia desencriptada
	 * dentro del inventario del almacen. La estructura de la clave es:
	 * yymmddiiiif#.. donde
	 * yymmdd = fecha de emision de contraceña
	 * iiii = identificador de aplicacion
	 * f = cryptoId de forma
	 * # = nro de formas 
	 * 
	 * Para que el contenido de las formas sea adicionado al inventario deben
	 * cumplirse:
	 * - Esta clave es emitida para esta aplicacion.
	 * - formato de fecha es correcto
	 * - fecha de clave no puede ser mayor a la fecha de hoy
	 * - fecha de clave no puede ser <= a alguna de las claves anteriores
	 * 
	 */
	private void load() {
		String qjf = openFC.getSelectedFile();
		String des = "";
		try {
			Translator.initialize(Translator.DECRYPT);
			des = Translator.translateKey(CoreUtilities.getPurchaseKey(qjf));
		} catch (Exception e) {
			rPanel.showMessage("msg84");
			log("msg84", qjf);
			return;
		}
 
 		// determina si esta clave de adquisicion fue emitida para esta 
		// aplicacion. Verificacion de identificador
		String aid = des.substring(6, 10);
		if (!(CoreUtilities.getApplicationId().equals(aid))) {
			rPanel.showMessage("msg85");
			log("msg85", qjf);
			return;
		}

		// la parte correspondiente a la fecha es un numero?		
		int keyd = -1;
		String keyds = "20" + des.substring(0, 6);
		try {
			keyd = Integer.parseInt(keyds);
		} catch (NumberFormatException nfe) {
			rPanel.showMessage("msg86");
			log("msg86", qjf);
			return;
		}
		
		// error si fecha de clave > hoy
		if (keyd > Integer.parseInt(new AppDate().toString())) {
			rPanel.showMessage("msg86");
			log("msg98", key_jtf.getText());
			return;
		}
		
		// investiga si se ha tipeado una clave de fecha igual o mayor 
		// a la de la clave.
		for (int rc = 0; rc < stModel.getRowCount(); rc++) {
			AppDate apd = (AppDate) stModel.getRecordAt(rc).getFieldValue("sFecha"); 
			int rcdd = Integer.parseInt(apd.toString()); 
			if (keyd <= rcdd) {
				rPanel.showMessage("msg87");
				log("msg087", qjf);		
				return;  
			}
		}
		
		// inicia exploracion de formas dentro de clave. cada posible cryptoId 
		// de forma es verificado y se hay alguna se adiciona o actualiza 
		// el registro 
		for (int x = 10; x < des.length(); x++) {
			String cid = des.substring(x, x + 1);
			int can = 0;
//			try {
				can = Integer.parseInt((des.substring(++x, x + 1)));
//			} catch (NumberFormatException nfe) {
//				rPanel.showMessage(new 
//					WarningException("Error encontrado en formato interno."));
//				log("Error in quantity of form. CryptoId: " + cid, cid);
//			}
			for (int k = 0; k < iForms.length; k++) {
				Hashtable ht = PrevalentSystemManager.getFormElements(
					(String) iForms[k].getKey());
				Element e = (Element) ht.get("form");
				if (e.getAttributeValue("cryptoId").equals(cid)) {
					String fid = e.getAttributeValue("id");
					StockRecord sr = (StockRecord) stModel.getRecord(fid);
					AbstractCommand cmd; 
					if (sr == null) {
						sr = new StockRecord();
						sr.setFieldValue("sForm", fid);
						cmd = new NewRecordCommand(sr);
					} else {
						cmd = new EditRecordCommand(sr);
						int cana = ((Integer) sr.getFieldValue("sCantida")).intValue();
						can += cana;
					}
					sr.setFieldValue("sCantida", new Integer(can));
					sr.setFieldValue("sFecha", new AppDate(keyds.substring(0, 4), 
						keyds.substring(4, 6), keyds.substring(6, 8)));
					PrevalentSystemManager.executeCommand(cmd);
				}
			}
		}
		
		// actualiza componentes
		key_jtf.setText("");
		rPanel.showMessage(null);
		updateStockTable();

		// verifica ptf
		PTFManager.installPTF(qjf);
	}
	
	/** entrada en registro. 
	 * 
	 * @param mid - id de mensaje (en qmsgf)
	 */
	private void log(String mid, String dk) {
		Logger.getLogger("").logp(Level.WARNING, null, null, 
			((AplicationException) 
			PrevalentSystemManager.exceptions.get(mid)).getMessage(), dk);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	public void removeUpdate(DocumentEvent e) {
		enableLoad();
	}
	
	/** Actualiza el modelo para la tabla que presenta la lista del inventario
	 * de formas. Las entradas son acutalizadas segun las formas instaladas y 
	 * las cantidades son obtenidas del inventario o 0 si no hay entradas
	 * 
	 */	
	private void updateStockTable() {
		Vector rd = new Vector();
		
		for (int x = 0; x < iForms.length; x++) {
			Vector rd_x = new Vector();
			rd_x.add(iForms[x].getValue());
			String fid = (String) iForms[x].getKey();
			Record rcd = stModel.getRecord(fid);
			rd_x.add((rcd == null) ? 
				new Integer(0) : 
				(Integer) rcd.getFieldValue("sCantida")
			);
			rd.add(rd_x);
		}
		
		stockT.setModel(new DefaultTableModel(rd, sCN));
		TableColumnModel cm = stockT.getColumnModel();
		TableColumn tc = cm.getColumn(0);
		tc.setPreferredWidth(450);
	}

}