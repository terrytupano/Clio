/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;

import com.ae.core.*;
import com.ae.gui.*;

/** redacta y envia correo 
 * 
 */
public class SendEmail implements Runnable {
	
	private ProgressPanel progressP;
	private RunnableMonitor runM;
	private Vector msgdta;
	
	/** nueva instancia
	 * 
	 * @param pb - instancia de barra de progreso
	 * @param d - dialogo (para JDialog.dispose())
	 * @param dta - vector con contenido para redactar correo. ver
	 * sendRequet_wp3
	 */
	public SendEmail(ProgressPanel pb, Vector dta) {
		this.progressP = pb; 
		this.msgdta = dta;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			this.runM = new RunnableMonitor(progressP);
			progressP.offsetMaximum(3);
			progressP.increment(DIMain.bundle.getString("r04"));
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp"); 
			Session session = Session.getDefaultInstance(props, null);
//			session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(
				(String) DIMain.preferenceRecord.getFieldValue("prEmail")));
			message.addRecipient(Message.RecipientType.TO, 
				new InternetAddress(DIMain.bundle.getString("c17")));
			message.setSubject(DIMain.bundle.getString("r19"));
			message.setSentDate(new Date());
			
			MimeMultipart mmp = new MimeMultipart();
			// mensaje
			MimeBodyPart mbp = new MimeBodyPart();
			DataSource msgtxt = new FileDataSource((File) msgdta.elementAt(1));
			mbp.setDataHandler(new DataHandler(msgtxt));
			mmp.addBodyPart(mbp);

			// .jar
			mbp = new MimeBodyPart();
			File fi = (File) msgdta.elementAt(2);
			DataSource jar = new FileDataSource(fi);
			mbp.setDataHandler(new DataHandler(jar));
			mbp.setFileName(fi.getName());
			mmp.addBodyPart(mbp);
			
			message.setContent(mmp);
			message.saveChanges();
			progressP.increment(DIMain.bundle.getString("e17"));

			Transport tra = session.getTransport();
			tra.connect(DIMain.bundle.getString("h02"), 26, "", "");
			tra.sendMessage(message, message.getAllRecipients());
			tra.close();
			
			progressP.increment(DIMain.bundle.getString("s14"));
			Thread.sleep(4000);				

		} catch (Exception e) {
			String msg = e.getMessage();
			Throwable th = e.getCause();
			while(th != null) {
				msg += th.getMessage() + "\n"; 
				th = e.getCause();
			}
			String endmsg = DIMain.bundle.getString("e19") + "\n" + msg;
			JOptionPane.showMessageDialog(null, endmsg, DIMain.bundle.getString("e16"), 
				JOptionPane.ERROR_MESSAGE);
			Logger.getLogger("").logp(Level.WARNING, null, null, e.getMessage(), e);
		}
		runM.dialog.dispose();
	}
}
