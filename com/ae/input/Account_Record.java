/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import javax.swing.*;
import javax.swing.border.*;

import com.ae.gui.*;
import com.ae.resource.*;
import com.ae.action.*;
import com.ae.core.*;

public class Account_Record extends AbstractRecordDataInput {

	private JButton aceptar;
	private JTextField app_jtf, fnc_jtf;
	
	public Account_Record(Record rcd, ConfirmationAction ca, boolean nr) {
		super(((nr) ? "NewRecord" : "EditRecord"), rcd, nr);

		//contribuyente (para no alterar getRecord())
		JTextField jtf = GUIUtilities.getJTextField(rcd, 0);
		addInputComponent(jtf, true);

		//Codigo
		jtf = GUIUtilities.getJTextField(rcd, 1);
		addInputComponent(jtf, true);

		//texto
		jtf = GUIUtilities.getJTextField(rcd, 2);
		addInputComponent(jtf);
		
		// aplicacion origen
		jtf = GUIUtilities.getJTextField(rcd, 3);
		app_jtf = GUIUtilities.getJTextField(rcd, 3);
		addInputComponent(jtf);

		// directorio origen
		jtf = GUIUtilities.getJTextField(rcd, 4);
		addInputComponent(jtf);

		// precalculo
		JComboBox jcbox = GUIUtilities.getJComboBox(
			getConstantsOfType("EFuntion"), rcd, 5, false);
		addInputComponent(jcbox);
		
		JComponent[] jcmps = getInputComponents();

		JPanel main_panel = new JPanel();
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
//		JPanel main_panel = new JPanel(new BorderLayout(
//			GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP));
		GUIUtilities.setEmptyBorder(main_panel);

		// origen
		Box bi = Box.createHorizontalBox();
		bi.add(GUIUtilities.getCoupleInBox2(rcd.getFieldName(3), app_jtf));
		bi.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
		bi.add(GUIUtilities.getCoupleInBox2(rcd.getFieldName(4), jcmps[4]));
		bi.setBorder(new TitledBorder(DIMain.bundle.getString("o06")));

		// para modificar atributos puestos por guiutilities
		if (!nr) {
			AppEntry ape = AppConstants.getConstant((String) rcd.getFieldValue("acIApp"));
			app_jtf.setText((String) ape.getValue()); 
		}
		app_jtf.setEnabled(false);
		GUIUtilities.setDimensionForTextComponent(app_jtf, 30);
		jcmps[4].setEnabled(false);

		// cuenta y descripcion 
		Box ba = Box.createHorizontalBox();		
		ba.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(1), jcmps[1], true, nr));
		ba.add(Box.createHorizontalStrut(GUIUtilities.VERTICAL_GAP));
		ba.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(2), jcmps[2]));
		
		main_panel.add(ba);
		main_panel.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		main_panel.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(5), jcmps[5]));
		main_panel.add(bi);
		main_panel.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));

		// Acciones
		AbstractAction[] bts = new AbstractAction[] {
			new AceptAction(ca), new CancelAction(ca)
		};
		this.aceptar = setActionBar(bts);
		setDefaultButton(aceptar);
		add(main_panel);
		preValidate();
	}
	/* (non-Javadoc)
	 * @see com.ae.datainput.AbstractRecordDataInput#validateFields()
	 */
	public boolean validateFields() {
		return true;
	}

}