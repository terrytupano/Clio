/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package butil;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;

import com.ae.core.*;

/** envio masivo de correo electronico
 * 
 */
public class SendEmail implements ActionListener {
	
	private JList jl_out;
	private DefaultListModel dlModel;
	private static String dta_fn = Util1.UTIL_PATH + "emaillist.txt";
	private static String patt_fn = Util1.UTIL_PATH + "cliostar.htm";
	private String[] s_n_e;
	private String pattern;
	private JLabel stbar;

	/** nueva instancia
	 * 
	 * 
	 */	
	public SendEmail() {
		this.dlModel = new DefaultListModel();
		this.stbar = new JLabel(" ");
		
		try {
			
			// datos
			File f = new File(dta_fn);
			byte[] dta = new byte[(int) f.length()];
			FileInputStream fis = new FileInputStream(f);
			fis.read(dta);
			String s = new String(dta);
			this.s_n_e = s.split(";");
			
			// plantilla
			f = new File(patt_fn);
			dta = new byte[(int) f.length()];
			fis = fis = new FileInputStream(f);;
			fis.read(dta);
			this.pattern = new String(dta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see com.ae.butil.Actions#getComponent()
	 */	
	public JComponent getComponent() {
		JButton jb = new JButton("Enviar correos electronicos");
		jb.addActionListener(this);

		JPanel jp = new JPanel(new BorderLayout(4, 4));
		jp.add(jb, BorderLayout.NORTH);
		jp.add(new JScrollPane(new JList(dlModel)), BorderLayout.CENTER);
		jp.add(stbar, BorderLayout.SOUTH);
		return jp;
	}
	
	/*  
	 * 
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		Hashtable ht = new Hashtable();
		Date da = new Date();
		for (int d = 1; d < s_n_e.length; d += 3) {
			ht.clear();
			ht.put("<date>", da);
			if (s_n_e[d].equals("m")) {
				ht.put("<sex>", "Estimado Señor .-");	
			} else {
				ht.put("<sex>", "Estimada Señora .-");	
			}
			String na = s_n_e[d + 1].trim();
			ht.put("<name>", na);
			String em = s_n_e[d + 2].trim();
			ht.put("<email>", em);
			String doc_rmp = CoreUtilities.replaceHTMLVariables1(pattern, ht);
			if (!na.startsWith("***")) {
				File tmpf = createFile(doc_rmp, s_n_e[d + 1]);
				System.out.println("Mensaje enviado a >>" + em + "<< ...");
//				sendEmail(tmpf, em);
			} else {
				System.out.println(na);
			}
		}
	}
	
	private File createFile(String dr, String na) {
		File htmf = null; 
		try {
			htmf = File.createTempFile(na, ".html", new File(Util1.UTIL_PATH));			
			htmf.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(htmf));
			bos.write(dr.getBytes());
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmf;
	}
	
	private void sendEmail(File fi, String email) {
		try {
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp"); 
			Session session = Session.getDefaultInstance(props, null);
//			session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress("clio@clio-ve.com", "Arnaldo Fuentes"));
			message.addRecipient(Message.RecipientType.TO,
				new InternetAddress(email));
			message.setSubject("Clio");
			message.setSentDate(new Date());
			
			MimeMultipart mmp = new MimeMultipart();
			// mensaje
			MimeBodyPart mbp = new MimeBodyPart();
			DataSource msgtxt = new FileDataSource(fi);
			mbp.setDataHandler(new DataHandler(msgtxt));
			mmp.addBodyPart(mbp);

			message.setContent(mmp);
			message.saveChanges();

			Transport tra = session.getTransport();
//			tra.connect("mail.clio-ve.com", 26, "", "");
			tra.connect("mail.cantv.net", 25, "", "");
			tra.sendMessage(message, message.getAllRecipients());
			tra.close();
//			fi.delete();
			Thread.sleep(8000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}