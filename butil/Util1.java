/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package butil;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.metal.*;

import com.ae.core.*;
import com.ae.gui.kunststoff.*;
import com.ae.resource.*;
import com.incors.plaf.kunststoff.*;

/**
 * @author Arnaldo Fuentes
 *
 */
public class Util1 {
	
	private JFrame frame;
	private JPanel frameP;
	public static PrevalentSystemManager prevalentSystemManager;
	
	public static String DISTRIBUTION_DIR = "d:/di.distribution/";
	public static String DISTRIBUTION_PATH = "d:/di.distribution.path/";
	public static String UTIL_PATH = "D:/di/butil/data/";

	/** main
	 * 
	 * @param arg - argumentos
	 */	
	public static void main(String[] arg) {

		CoreUtilities.development = true;
		try {
			DIMain.bundle = ResourceUtilities.getBundle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		prevalentSystemManager = new PrevalentSystemManager();

		// l&f
		KunststoffLookAndFeel klf = new Kunststoff();
		DefaultMetalTheme th = null;
		try {
			KunststoffLookAndFeel.setCurrentTheme(new KunststoffDesktopTheme());
			UIManager.setLookAndFeel(klf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// no desde argumentos de entrada
		Util1 app = new Util1();
	}
	
	/** nueva instancia
	 *
	 */
	public Util1() {
		frame = new JFrame("Util1");
		frame.setBounds(200, 100, 400, 400);
		frame.setIconImage(ResourceUtilities.getIcon("Clio32").getImage());
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		
		this.frameP = new JPanel(new BorderLayout());
		JTabbedPane jtp = new JTabbedPane();
		
		// encriptar clave de adquisicion
		EncryptKey ea = new EncryptKey();
		jtp.add("Clave", ea.getComponent());

		// encriptar documentos xml
		EncryptFiles ef = new EncryptFiles(null);
		jtp.add("Encriptar archivos", ef.getComponent());

		// generar ptf
		TemporalFixGenerator tfg = new TemporalFixGenerator(null);
		jtp.add("generador de PTF", tfg.getComponent());

		// envio de email
		SendEmail sm = new SendEmail();
		jtp.add("Enviar correo", sm.getComponent());
		 
		frameP.add(jtp, BorderLayout.CENTER);
		frame.setContentPane(frameP);
		frame.setVisible(true);
	}
}
