/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.plaf.metal.*;

import com.ae.action.*;
import com.ae.gui.kunststoff.*;
import com.ae.resource.*;
import com.incors.plaf.kunststoff.*;

/** punto de entrada a la aplicacion
 * 
 */
public class DIMain {

	public static JFrame frame;
	public static LeftComponentManager leftComponentManager;
	public static PreferenceRecord preferenceRecord;
	public static JToolBar toolBar;
	public static ResourceBundle bundle;
	
	private static JPanel mainContainer;
	private static JWindow jw_intro;
	private static JButton jb_tips, jb_news;
	private static JLabel jlabel;
	
	/** Arranque de aplicacion
	 * 
	 * @param arg - argumento de entrada
	 */
	public static void main(String arg[]) {
		
		Locale.setDefault(new Locale("es", "VE"));
		// si el arg[0] = "true" se esta en desarrollo
//		if (arg.length > 0 && arg[0].equals("true")) {
			CoreUtilities.development = true;
//		}
		// ResourceBundle y registro de anotaciones
		try {
			bundle = ResourceUtilities.getBundle();
			FileHandler fh = new FileHandler(ResourceUtilities.getLogFileName(), true);
			fh.setFormatter(new SimpleFormatter());
			fh.setLevel(Level.WARNING);
			ConsoleHandler ch = new ConsoleHandler();
			ch.setLevel(Level.WARNING);
			Logger logger = Logger.getLogger("");
			Handler[] hs = logger.getHandlers();
			for (int x = 0; x < hs.length; x++) {
				logger.removeHandler(hs[x]);
			}
			logger.addHandler(fh);
			logger.addHandler(ch);
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, bundle.getString("d02"), e.getMessage(), e);
			System.exit(-1);
		}

		// intro
		ImageIcon fb = ResourceUtilities.getIcon("clio.intro");
		jw_intro = new JWindow();
		jw_intro.setSize(fb.getIconWidth(), fb.getIconHeight());
		jw_intro.setLocationRelativeTo(null);
		jw_intro.getContentPane().add(new JLabel(fb));
		jw_intro.setVisible(true);

		PrevalentSystemManager prevalentSystemManager = new PrevalentSystemManager();
		AppConstants ap = new AppConstants();
		
		// preferencias
		PreferenceRecord pr = new PreferenceRecord();
		preferenceRecord = null;
//		preferenceRecord = (PreferenceRecord) 
//			PrevalentSystemManager.getRecordFrom(pr, pr.getKey());
		if (preferenceRecord == null) {
			preferenceRecord = pr;
	//		PrevalentSystemManager.executeCommand(new NewRecordCommand(preferenceRecord));
		}
		
		//l&f y tema
		KunststoffLookAndFeel klf = new Kunststoff();
		DefaultMetalTheme th = null;
		try {
			Class cls = Class.forName(
				(String) preferenceRecord.getFieldValue("prTema")
			);
			th = (DefaultMetalTheme) cls.newInstance();
		//	KunststoffLookAndFeel.setCurrentTheme(new KunststoffDesktopTheme());
		//	KunststoffLookAndFeel.setCurrentGradientTheme(new KunststoffGradientTheme());
			KunststoffLookAndFeel.setCurrentTheme(th);
			UIManager.setLookAndFeel(klf);
		} catch (Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
			System.exit(-1);
		}
		
		DIMain app = new DIMain();
		frame.setVisible(true);
		jw_intro.dispose();

		// Noticias
		if (jb_news.isEnabled()) { 
			jb_news.doClick();
		}
		
		// tips
		if (((Boolean) 
			preferenceRecord.getFieldValue("prTipsAS")).booleanValue()) {
			jb_tips.doClick();
		}
	}

	/** Nueva aplicacion
	 */
	public DIMain() {
		frame = new JFrame(bundle.getString("t02"));
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				PrevalentSystemManager.appEnd();
				System.exit(0);
			}
		});
		frame.setBounds(1, 50, 799, 500);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setIconImage(ResourceUtilities.getIcon("Clio32").getImage());

		int leftC = LeftComponentManager.DUTY_CALENDAR;		
		// inventario. Intenta con el ar-i ya que este es gratis y deberia
		// siempre existir en inventario.
		StockRecord sr = (StockRecord) 
			PrevalentSystemManager.getRecordFrom(new StockRecord(), bundle.getString("a01"));
		if (sr == null) {
			com.ae.gui.calendar.Msg053.resetStock();
			leftC = LeftComponentManager.WELLCOME;
		}

		// componente que contiene a componente izquierdo y derecho
		mainContainer = new JPanel(new BorderLayout());
		leftComponentManager = new LeftComponentManager(mainContainer);
		JComponent ljc = leftComponentManager.getLeftComponent();
		mainContainer.add(ljc, BorderLayout.WEST);
		leftComponentManager.setRightComponent(leftC);
		
		// barra de herramientas
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.add(new JButton(new PreferenceAction()));
		toolBar.add(new JButton(new StockAction()));
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(new JButton(new WellcomeAction()));
		
		// para novedades
		jb_news = new JButton(new NewsAction());
		toolBar.add(jb_news);

		// para tips
		jb_tips = new JButton(new TipAction());
		toolBar.add(jb_tips);
		
		toolBar.add(new JButton(new HelpAction("a02", "Help", "ClioHelp")));
		toolBar.add(new JButton(new HelpAction("l01", "Law", "Law")));
		toolBar.add(new JButton(new AboutAction()));
		Component[] jc = toolBar.getComponents();
		for(int c = 0; c < jc.length; c++) {
			jc[c].setFocusable(false);
		} 
		
		// adiciona componentes a la ventana principal
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		frame.getContentPane().add(mainContainer, BorderLayout.CENTER);

		jlabel = new JLabel(bundle.getString("c01"));
		jlabel.setHorizontalAlignment(JLabel.LEFT);
		frame.getContentPane().add(jlabel, BorderLayout.SOUTH);		
	}

}