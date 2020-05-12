/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package butil;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

import com.ae.core.*;
import com.ae.gui.*;

/** utilitario para la encriptacion de archivos. el directorio por omision para
 * ejecutar cifado es <code>Util1.DISTRIBUTION_DIR</code>
 * 
 */
public class EncryptFiles implements ActionListener {
	
	private JTextField jtf_orgd;
	private JList jl_out;
	private DefaultListModel dlModel;
	

	/** nueva instancia.
	 * 
	 * @param dir - nombre de directorio. Si es null, el directorio sera 
	 * <code>Util1.DISTRIBUTION_DIR</code>
	 */	
	public EncryptFiles(String dir) {
		this.jtf_orgd = new JTextField(Util1.DISTRIBUTION_DIR);
		if (dir != null) {
			jtf_orgd.setText(dir);
		}
		GUIUtilities.setDimensionForTextComponent(jtf_orgd, 30);
		this.dlModel = new DefaultListModel();
		this.jl_out = new JList(dlModel);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.butil.Actions#getComponent()
	 */	
	public JComponent getComponent() {
		JButton jb = new JButton("Encriptar");
		jb.addActionListener(this);

		Box bn1 = Box.createHorizontalBox();
		bn1.add(jtf_orgd);
		bn1.add(jb);
		
		Box bn = Box.createVerticalBox();
		bn.add(Box.createHorizontalStrut(4));
		bn.add(GUIUtilities.getCoupleInBox2("Directorio donde se encuentral los archivos", bn1));
		
		JPanel jp = new JPanel(new BorderLayout(4, 4));
		jp.add(bn, BorderLayout.NORTH);
		jp.add(new JScrollPane(jl_out), BorderLayout.CENTER);
//		jp.add(GUIUtilities.getCoupleInBox2("Archivos encriptados", 
//		new JScrollPane(jl_out)), BorderLayout.CENTER);
		return jp;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		File mdir = new File(jtf_orgd.getText());
		addFiles(mdir);
		cipher(Translator.ENCRYPT);
	}
	
	/** adiciona los archivos contenidos en el en el directorio. Si 
	 * el directorio contiene otros sub-directorios, se ejecuta el metodo
	 * en forma recursiva. Solo:
	 * 
	 * - se adicionan archivos (no directorios) de los directorios 
	 * prevalenceBase, y forms
	 * - los archivos .class de dentro del directorio com.ae son renombrados 
	 * a .class1
	 * 
	 * @param dir - directorio 
	 */
	private void addFiles(File dir) {
		File[] fl = dir.listFiles();
		for(int j = 0; j < fl.length; j++) {
			if (fl[j].isDirectory()) {
				addFiles(fl[j]);
			} else {
				String nam = fl[j].getAbsolutePath();
				if ((nam.endsWith(".xml") || nam.endsWith(".properties")) && 
					(nam.indexOf("prevalenceBase") != -1 || 
					nam.indexOf("forms") != -1)) {
					dlModel.addElement(fl[j]);
				}
				
				/*
				if (nam.endsWith(".class") && 
					!nam.endsWith("EncryptClassLoader.class")) {
					String nfn = fl[j].toString() + "1";
					fl[j].renameTo(new File(nfn));
					dlModel.addElement(fl[j]);
				}
				*/
			}
		}
	}
	
	/** ejecuta el cifrado/decifrado de los archivos dentro de la lista 
	 * y que son validos para esta accion. para otras clases, este metodo
	 * debe llamarse solo para el decifrado y asi revertir el proceso
	 * y dejar los archivos del directorio destino disponibles para otra 
	 * operacion
	 * 
	 * @param mode - modalidad cifrado o decifrado
	 */
	public void cipher(int mode) {
		for (int m = 0; m < dlModel.getSize(); m++) {
			Translator.initialize(mode);
			try {
				File f = (File) dlModel.get(m);
				FileReader fr = new FileReader(f);
				char[] ch_f = new char[(int) f.length()];
				fr.read(ch_f);
				
				// para archivos xml y de propiedadas
				if (f.getName().endsWith(".xml") || f.getName().endsWith(".properties")) {
					for (int i = 0; i < ch_f.length; i++) {
						ch_f[i] = Translator.translate(ch_f[i]);
					}
				}
				
				fr.close();
				FileWriter fw = new FileWriter(f);
				fw.write(ch_f);
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
