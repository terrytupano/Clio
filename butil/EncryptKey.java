/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package butil;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.jar.*;
import java.util.zip.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.ae.core.*;
import com.ae.gui.*;
import com.ae.input.*;

/** Utilitario que recibe la fecha, id de aplicacion y formas y con estos datos
 * genera crea el archivo producer.jar con el archivo producer que contiene
 * las formas solicitadas. Luego de esto, Esta clase ejecuta 
 * <code>TemporalFixGenerator</code> con el archivo jar creado aqui para que
 * esta adicione posibles arregos temporales.
 * 
 */
public class EncryptKey implements ActionListener, TableModelListener, DocumentListener {
	
	private JTextField jtf_appid, jtf_date, jtf_in, jtf_out;
	private PurchaseTable purchaseT;
	
	/** nueva instancia 
	 *
	 */
	public EncryptKey() {
		this.jtf_date = new JTextField(new AppDate().toString().substring(2)); 
		jtf_date.getDocument().addDocumentListener(this);
		GUIUtilities.setDimensionForTextComponent(jtf_date, 8);
		this.jtf_appid = new JTextField();
		GUIUtilities.setDimensionForTextComponent(jtf_appid, 5);
		jtf_appid.getDocument().addDocumentListener(this);
		this.jtf_in = new JTextField();
		jtf_in.getDocument().addDocumentListener(this);
		GUIUtilities.setDimensionForTextComponent(jtf_in, 30);
		this.jtf_out = new JTextField();
		GUIUtilities.setDimensionForTextComponent(jtf_out, 30);
		this.purchaseT = new PurchaseTable(true);
		purchaseT.getTable().getModel().addTableModelListener(this);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.butil.Actions#getComponent()
	 */	
	public JComponent getComponent() {
		JButton jb = new JButton("Empacar");
		jb.addActionListener(this);

		int h = jb.getPreferredSize().height; //30;
		jtf_appid.setMaximumSize(new Dimension(jtf_appid.getPreferredSize().width, h));
		jtf_date.setMaximumSize(new Dimension(jtf_date.getPreferredSize().width, h));
		
		Box bn1 = Box.createHorizontalBox();
		bn1.add(jtf_appid);
		bn1.add(jtf_date);
		bn1.add(jtf_in);
		bn1.add(jb);
		
		Box bn = Box.createVerticalBox();
		bn.add(Box.createHorizontalStrut(4));
		bn.add(GUIUtilities.getCoupleInBox2("Codificación para el cifrado", bn1));
		bn.add(Box.createHorizontalStrut(4));
		bn.add(GUIUtilities.getCoupleInBox2("Clave de adquisición", jtf_out));
		
		JPanel jp = new JPanel(new BorderLayout(4, 4));
		jp.add(bn, BorderLayout.NORTH);
		jp.add(new JScrollPane(purchaseT.getTable()), BorderLayout.CENTER);
		jp.add(purchaseT.getTotalPanel(), BorderLayout.SOUTH);
		return jp;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
		TableModel tm = purchaseT.getTable().getModel();
		if (e.getType() == TableModelEvent.UPDATE) {
			String so = "";
			for (int r = 0; r < tm.getRowCount(); r++) {
				int c = ((Integer) tm.getValueAt(r, 1)).intValue();
				if (c > 0) {
					so += tm.getValueAt(r, 3).toString() + c;
				}
			}
			jtf_in.setText(so);
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		try {
			File tf = new File(Util1.DISTRIBUTION_PATH + "producer.jar");
			if (tf.exists()) {
				tf.delete();
			}
			tf.createNewFile();
			FileOutputStream fos = new FileOutputStream(tf);
			JarOutputStream jos = new JarOutputStream(fos);
			jos.putNextEntry(new ZipEntry("producer"));
			jos.write(jtf_out.getText().getBytes());
			
			TemporalFixGenerator tfg = new TemporalFixGenerator(jos);
			tfg.actionPerformed(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void translate() {
		Translator.initialize(Translator.ENCRYPT);
		String k = 
			jtf_date.getText() + 
			jtf_appid.getText() + 
			jtf_in.getText();
		jtf_out.setText(Translator.translateKey(k));
		/*
		Translator.initialize(Translator.ENCRYPT);
		String k = jtf_date.getText() + jtf_in.getText();
		String tt = Translator.translateKey(k);
		Translator.initialize(Translator.DECRYPT);
		jtf_out.setText(tt + " > " + Translator.translateKey(tt));
		*/
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent e) {
		translate();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent e) {
		translate();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	public void removeUpdate(DocumentEvent e) {
		translate();
	}

}
