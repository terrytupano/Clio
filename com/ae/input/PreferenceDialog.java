/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import javax.swing.*;

import com.ae.gui.*;
import com.ae.action.*;
import com.ae.core.*;
import com.ae.resource.*;

/** dialogo de entrada de datos para la edicion de las preferencias de la 
 * aplicacion
 * 
 */
public class PreferenceDialog extends AbstractRecordDataInput {

	private JButton aceptar;
	private Record rcd;
	
	public PreferenceDialog(ConfirmationAction ca) {
		super("Preference", DIMain.preferenceRecord, false);
		this.rcd = DIMain.preferenceRecord;

		//Tipo de persona
		JComboBox jcbox = GUIUtilities.getJComboBox(getConstantsOfType("person"), rcd, 0, false);
		addInputComponent(jcbox);

		//tema
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("theme"), rcd, 1, false);
		addInputComponent(jcbox);

		// tiempo de espera para guardardo automatico
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("savForm"), rcd, 2, false);
		addInputComponent(jcbox);

		// fuente
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("Font"), rcd, 3, false);
		addInputComponent(jcbox);
		
		// tips al entrar
		JCheckBox jcb = GUIUtilities.getJCheckBox(DIMain.bundle.getString("m09"), rcd, 4);
		addInputComponent(jcb);

		// recordatorio de adquisicion
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("remaind"), rcd, 5, false);
		addInputComponent(jcbox);
		
		JComponent[] jcmps = getInputComponents();
		
		// general
		JPanel jp_g = new JPanel();
		jp_g.setLayout(new GridLayout(0, 1));
		jp_g.add(decorate("m10", "f07", "Favorite32", jcmps[0]));
		jp_g.add(decorate("m11", "t06", "theme32", jcmps[1]));
		jp_g.add(decorate("m12", "t03", "TipsIcon32", jcmps[4]));

		// formas
		JPanel jp_f = new JPanel();
		jp_f.setLayout(new GridLayout(0, 1));
		jp_f.add(decorate("m13", "g03", "clock32", jcmps[2]));
		jp_f.add(decorate("m14", "t07", "SectionFont32", jcmps[3]));
		jp_f.add(decorate("m15", "r05", "Remaind32", jcmps[5]));
		
		//-------------------------------------------
		// pestañas
		//-------------------------------------------
		JTabbedPane jtPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		jtPane.addTab(DIMain.bundle.getString("g04"), jp_g);
		jtPane.addTab(DIMain.bundle.getString("f02"), jp_f);

		// Acciones
		AbstractAction[] bts = new AbstractAction[] {new AceptAction(ca), new CancelAction(ca)};
		this.aceptar = setActionBar(bts);
		setDefaultButton(aceptar);
		add(jtPane);
	}
	/* (non-Javadoc)
	 * @see com.ae.datainput.AbstractRecordDataInput#validateFields()
	 */
	public boolean validateFields() {
		return true;
	}
	
	/** adiciona una serie de componente que explican y adornan al componente
	 * encargado de la entrada de los datos. 
	 * 
	 * @param tid - id de descripcion 
	 * @param titd - id de titulo
	 * @param in - nombre del icon
	 * @param cmp - componente de entrada
	 * @return componente a presentar
	 */
	private JPanel decorate(String tid, String titd, String in, JComponent cmp) {
		JPanel jp = new JPanel(new BorderLayout());
		
		//titulo
		Box tb1 = Box.createVerticalBox();
		tb1.add(Box.createVerticalGlue());
		tb1.add(new JSeparator());
		Box tb = Box.createHorizontalBox();
		tb.add(new JLabel(DIMain.bundle.getString(titd)));
		tb.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
		tb.add(tb1);
		jp.add(tb, BorderLayout.NORTH);

		JPanel jp1 = new JPanel(new BorderLayout(8, 0));
		jp1.add(new JLabel(ResourceUtilities.getIcon(in)), BorderLayout.WEST);
		jp1.add(GUIUtilities.getCoupleInBox2(DIMain.bundle.getString(tid), cmp), BorderLayout.CENTER);
		
		jp.add(jp1, BorderLayout.CENTER);
		GUIUtilities.setEmptyBorder(jp);
/*		
		//icono & desc
		JLabel jl = new JLabel(txt);
		jl.setIcon(ResourceUtilities.getIcon(in));
		jl.setIconTextGap(10);
		jp.add(jl, BorderLayout.CENTER);
		
		// componente
		jp.add(cmp, BorderLayout.EAST);
		GUIUtilities.setEmptyBorder(jp);
		*/
		return jp;
	}

}