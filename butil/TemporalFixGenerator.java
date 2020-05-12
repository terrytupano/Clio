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

import com.ae.core.*;
import com.ae.gui.*;

/** Verifica el contenido del directorio<code>DISTRIBUTION_PATH</code>
 * y se almacena su contenido dentro del archivo .jar con nombre 
 * indicado en el campo de entrada
 * 
 */
public class TemporalFixGenerator implements ActionListener {
	
	private JTextField jtf_name;
	private JList jl_out;
	private DefaultListModel dlModel;
	private JarOutputStream jarOutput;

	/** nueva instancia
	 * 
	 * @param jo - <code>JarOputputStream</code> si es null, se creara un
	 * nuevo archivo en el directorio <code>Util1.DISTRIBUTION_PATH</code>
	 * con nombre por omision. de lo contrario, archivos seran añadidos a 
	 * este <code>JarOputputStream</code>
	 */	
	public TemporalFixGenerator(JarOutputStream jo) {
		this.jtf_name = new JTextField("PTF" + (new AppDate().toString()));
		GUIUtilities.setDimensionForTextComponent(jtf_name, 30);
		this.dlModel = new DefaultListModel();
		this.jl_out = new JList(dlModel);
		this.jarOutput = jo;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.butil.Actions#getComponent()
	 */	
	public JComponent getComponent() {
		JButton jb = new JButton("Empaquetar PTF");
		jb.addActionListener(this);

		Box bn1 = Box.createHorizontalBox();
		bn1.add(jtf_name);
		bn1.add(jb);
		
		Box bn = Box.createVerticalBox();
		bn.add(Box.createHorizontalStrut(4));
		bn.add(GUIUtilities.getCoupleInBox2("Nombre de archivo", bn1));
		
		JPanel jp = new JPanel(new BorderLayout(4, 4));
		jp.add(bn, BorderLayout.NORTH);
		jp.add(new JScrollPane(jl_out), BorderLayout.CENTER);
		return jp;
	}
	
	/*  
	 * 
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		try {
			EncryptFiles ef = new EncryptFiles(Util1.DISTRIBUTION_PATH);
			ef.actionPerformed(null);
			addFiles(new File(Util1.DISTRIBUTION_PATH));

			if (jarOutput == null) {
				File tf = new File(
					Util1.DISTRIBUTION_PATH + jtf_name.getText() + ".jar");
				tf.createNewFile();
				FileOutputStream fos = new FileOutputStream(tf);
				jarOutput = new JarOutputStream(fos);
			}
			int ilen = Util1.DISTRIBUTION_PATH.length();
			for (int m = 0; m < dlModel.getSize(); m++) {
				File f = (File)dlModel.elementAt(m);
				BufferedInputStream fbus = new BufferedInputStream(
					new FileInputStream(f));
				byte[] b = new byte[(int) f.length()];
				fbus.read(b);
				jarOutput.putNextEntry(new ZipEntry(f.getPath().substring(ilen)));
				jarOutput.write(b);
			}
			jarOutput.close();
			ef.cipher(Translator.DECRYPT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** adiciona archvivos a lista de archivos dentro del directorio.
	 * directorio inicial: <code>DISTRIBUTION_PATH</code> 
	 * los archivos que esten en el directorio
	 * raiz de <code>DISTRIBUTION_PATH</code> (no se encuentra en una carpeta
	 * dentro de este) no se adicionan a la lista.
	 * 
	 * @param dir - directorio 
	 */
	private void addFiles(File dir) {
		File[] fl = dir.listFiles();
		for(int j = 0; j < fl.length; j++) {
			if (fl[j].isDirectory()) {
				addFiles(fl[j]);
			} else {
				String fp = fl[j].getParent().replace(File.separatorChar, '/');
				if (!Util1.DISTRIBUTION_PATH.equals(fp + "/")) {
					dlModel.addElement(fl[j]);
				}
			}
		}
	}
}