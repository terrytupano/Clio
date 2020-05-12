/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.input;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import com.ae.gui.*;
import com.ae.resource.*;
import com.ae.action.*;
import com.ae.core.*;

/** UI para edicion de datos de los contribuyentes. Esta clase evalue el campo
 * <code>tpTipPer</code> y para determinar las pestañas que deben presentarse y asi
 * dar una correcta edicion del registro.
 *  
 */
public class TP_Record extends AbstractRecordDataInput {

	private JButton aceptar;
	private AddressEditor dir_1, dir_2;
	private JFormattedTextField jftf_cced, jftf_rced;
	private JTextField jtf_cnom, jtf_ccpc, jtf_rrif, jtf_rnom, jtf_rdir;
	private boolean natural;
	private Record taxPayer;
	
	/** nuevo editor para contribuyente
	 * 
	 * @param rcd - registro a editar
	 * @param ca - accion a la que hay que reportar
	 * @param newR - =true nuevo registro, =false edicion
	 */
	public TP_Record(Record rcd, ConfirmationAction ca, boolean newR) {
		super(((newR) ? "NewRecord" : "EditRecord"), new TaxPayerRecord(), newR);
		this.natural = ((String) rcd.getFieldValue(5)).equals("na");
		this.taxPayer = rcd;

		//RIF
		JTextField jtf = GUIUtilities.getJTextField(rcd, 0);
		addInputComponent(jtf, true);

		//NIT
		jtf = GUIUtilities.getJTextField(rcd, 1);
		addInputComponent(jtf);

		// razon social
		jtf = GUIUtilities.getJTextField(rcd, 2);
		addInputComponent(jtf, true);		

		//inicio
		JComboBox jcbox = GUIUtilities.getJComboBox(getConstantsOfType("month"), rcd, 3, false);
		addInputComponent(jcbox);
		
		//direccion: no se presenta
		JLabel jl = new JLabel(rcd.getFieldName(4));
		jl.setFont(jl.getFont().deriveFont(Font.BOLD));
		jtf = new JTextField();
		this.dir_1 = new AddressEditor(jl, (AddressRecord) rcd.getFieldValue(4), jtf);
		addInputComponent(jtf, true);

		//tipo de persona. registro componente ficticio para no tener que 
		//implementar getRecord() El valor de este es el pasado como argumento
		jtf = new JTextField((String) rcd.getFieldValue(5));
		addInputComponent(jtf);

		//Nacionalidad
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("nation"), rcd, 6, false);
		addInputComponent(jcbox);

		//siglas
		jtf = GUIUtilities.getJTextField(rcd, 7);
		addInputComponent(jtf);

		// contibuyente
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("tpType"), rcd, 8, false);
		addInputComponent(jcbox);

		//cedula rep.
		this.jftf_rced = GUIUtilities.getJFormattedTextField(rcd, 9);
		jftf_rced.addFocusListener(this);
		jftf_rced.getDocument().addDocumentListener(this);
		addInputComponent(jftf_rced);

		//Nacionalidad rep.
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("nation"), rcd, 10, false);
		addInputComponent(jcbox);

		//RIF rep.
		this.jtf_rrif = GUIUtilities.getJTextField(rcd, 11);
		jtf_rrif.addFocusListener(this);
		jtf_rrif.getDocument().addDocumentListener(this);
		addInputComponent(jtf_rrif);

		//NIT rep.
		jtf = GUIUtilities.getJTextField(rcd, 12);
		addInputComponent(jtf);

		// nombre rep.
		this.jtf_rnom = GUIUtilities.getJTextField(rcd, 13);
		jtf_rnom.addFocusListener(this);
		jtf_rnom.getDocument().addDocumentListener(this);
		addInputComponent(jtf_rnom);		

		//direccion (no se presenta)
		this.jtf_rdir = new JTextField();
		jl = new JLabel(rcd.getFieldName(14));
		this.dir_2 = 
			new AddressEditor(jl, (AddressRecord) rcd.getFieldValue(14), jtf_rdir);
		jtf_rdir.addFocusListener(this);
		jtf_rdir.getDocument().addDocumentListener(this);
		addInputComponent(jtf_rdir);

		// tipo de sociedad
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("corporation"), rcd, 15, false);
		addInputComponent(jcbox);

		// actividad
		String act = (natural) ? "naActivity" : "activity";
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType(act), rcd, 16, false);
		
		// esto es porque las personas naturales pueden dedicarsa a estas 
		// actividades
		if (natural) {
			jcbox.addItem(AppConstants.getConstant("ak"));
			jcbox.addItem(AppConstants.getConstant("am"));
			jcbox.addItem(AppConstants.getConstant("an"));
		}
		addInputComponent(jcbox);

		//domiciliado
		JCheckBox jch = GUIUtilities.getJCheckBox(rcd.getFieldName(17), rcd, 17);
		addInputComponent(jch);

		//mayorista
		jch = GUIUtilities.getJCheckBox(rcd.getFieldName(18), rcd, 18);
		addInputComponent(jch);

		//importador
		jch = GUIUtilities.getJCheckBox(rcd.getFieldName(19), rcd, 19);
		addInputComponent(jch);

		//retencion
		jch = GUIUtilities.getJCheckBox(rcd.getFieldName(20), rcd, 20);
		addInputComponent(jch);

		//receptor
		jch = GUIUtilities.getJCheckBox(rcd.getFieldName(21), rcd, 21);
		addInputComponent(jch);

		//rentas exeptas
		jch = GUIUtilities.getJCheckBox(rcd.getFieldName(22), rcd, 22);
		addInputComponent(jch);

		//lucro
		jch = GUIUtilities.getJCheckBox(rcd.getFieldName(23), rcd, 23);
		addInputComponent(jch);

		// contador
		this.jtf_cnom = GUIUtilities.getJTextField(rcd, 24);
		jtf_cnom.addFocusListener(this);
		jtf_cnom.getDocument().addDocumentListener(this);
		addInputComponent(jtf_cnom);		

		//cedula
		this.jftf_cced = GUIUtilities.getJFormattedTextField(rcd, 25);
		jftf_cced.addFocusListener(this);
		jftf_cced.getDocument().addDocumentListener(this);
		addInputComponent(jftf_cced);

		// cpc
		this.jtf_ccpc = GUIUtilities.getJTextField(rcd, 26);
		jtf_ccpc.addFocusListener(this);
		jtf_ccpc.getDocument().addDocumentListener(this);
		addInputComponent(jtf_ccpc);

		//cedula persona natural
		JFormattedTextField jftf = GUIUtilities.getJFormattedTextField(rcd, 27);
		addInputComponent(jftf, natural);

		//fecha nacimiento persona natural
		jftf = GUIUtilities.getJFormattedTextField(rcd, 28);
		addInputComponent(jftf);

		//Persona natural: residenciado 
		jch = GUIUtilities.getJCheckBox(rcd.getFieldName(29), rcd, 29);
		if (newR) {
			jch.setSelected(true);
		}
		addInputComponent(jch);

		//Persona natural: edo civil
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("status"), rcd, 30, false);
		addInputComponent(jcbox);

		//Persona natural: conyuge declara aparte 
		jch = GUIUtilities.getJCheckBox(rcd.getFieldName(31), rcd, 31);
		addInputComponent(jch);

		//Persona natural: Separado de bienes 
		jch = GUIUtilities.getJCheckBox(rcd.getFieldName(32), rcd, 32);
		addInputComponent(jch);
		
		//actividad economica principal
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("ciiu"), rcd, 33, false);
		addInputComponent(jcbox);
		
		Dimension d = jcbox.getPreferredSize();
		d.width = 250;
		jcbox.setPreferredSize(d);

		//actividad economica secundaria
		jcbox = GUIUtilities.getJComboBox(getConstantsOfType("ciiu"), rcd, 34, false);
		addInputComponent(jcbox);

		jcbox.setPreferredSize(d);

		JComponent[] jcmps = getInputComponents();
		
		//-------------------------------------------
		// informacion general
		//-------------------------------------------
		// rif y nit
		JPanel panel5 = new JPanel(new GridLayout(1, 2, 
			GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP));
		panel5.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(0), jcmps[0], true, newR));
		panel5.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(1), jcmps[1]));
		
		// contador
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		JPanel panel1 = new JPanel(new GridLayout(1, 2, 
			GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP));
		panel2.setBorder(new TitledBorder(DIMain.bundle.getString("c18")));
		panel1.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(25), jcmps[25]));
		panel1.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(26), jcmps[26]));
		panel2.add(panel1);
		panel2.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		panel2.add(GUIUtilities.getCoupleInBox1(rcd.getFieldName(24), jcmps[24]));
		
		// mes de cierre, nacionalidad, y tipo de contribuyente
		JPanel panel3 = new JPanel(new GridLayout(2, 2, 
			GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP));
		panel3.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(3), jcmps[3]));
		panel3.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(6), jcmps[6]));
		panel3.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(8), jcmps[8]));
		
		// direccion (obligatoria) 
//		JLabel jl = new JLabel(rcd.getFieldName(4));
//		jl.setFont(jl.getFont().deriveFont(Font.BOLD));
//		AddressEditor dir_1 = new 
//			AddressEditor(jl, (AddressRecord) rcd.getFieldValue(4));

		JPanel ptab1 = new JPanel();
		ptab1.setLayout(new BoxLayout(ptab1, BoxLayout.Y_AXIS));
		GUIUtilities.setEmptyBorder(ptab1);
		
		ptab1.add(panel5);
		ptab1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		String nom = rcd.getFieldName(2);
		if (natural) {
			nom = DIMain.bundle.getString("a26");
		}
		ptab1.add(GUIUtilities.getCoupleInBox(nom, jcmps[2], true, true));
		ptab1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		ptab1.add(dir_1.getTicket());
		ptab1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		ptab1.add(panel3);
		ptab1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		ptab1.add(panel2);
		
		//-------------------------------------------
		// representante legal
		//-------------------------------------------
		// rif y nit
		JPanel panel6 = new JPanel(new GridLayout(2, 2, 
			GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP));
		panel6.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(11), jcmps[11]));
		panel6.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(12), jcmps[12]));
		
		//nombre, direccion
		JPanel panel7 = new JPanel(new BorderLayout());
		Box b1 = Box.createVerticalBox();
		b1.add(GUIUtilities.getCoupleInBox1(rcd.getFieldName(13), jcmps[13]));
		b1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		b1.add(dir_2.getTicket());
		panel7.add(b1, BorderLayout.NORTH);
		
		// cedula y nacionalidad
		panel6.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(9), jcmps[9]));
		panel6.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(10), jcmps[10]));
		
		JPanel ptab2 = new JPanel();
		ptab2.setLayout(new BoxLayout(ptab2, BoxLayout.Y_AXIS));
		GUIUtilities.setEmptyBorder(ptab2);
		ptab2.add(panel6);
		ptab2.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		ptab2.add(panel7);
		
		//-------------------------------------------
		// registro
		//-------------------------------------------

		//jcheckbox
		JPanel panel13 = new JPanel(new GridLayout(0, 2, 
			GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP));
		panel13.add(jcmps[17]);
		panel13.add(jcmps[18]);
		panel13.add(jcmps[19]);
		panel13.add(jcmps[20]);
		panel13.add(jcmps[21]);
		panel13.add(jcmps[22]);
		panel13.add(jcmps[23]);
		
		// los dos anteriores
		JPanel panel11 = new JPanel(new GridLayout(0, 2, 
			GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP));
		panel11.add(panel13);
		
		// todo en un Box
		Box b6 = Box.createVerticalBox();
		b6.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(15), jcmps[15]));
		b6.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		
		// Actividad economica
		Box b16 = GUIUtilities.getCoupleInBox(rcd.getFieldName(16), jcmps[16]);
		
		b6.add(b16);
		b6.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		b6.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(33), jcmps[33]));
		b6.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		b6.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(34), jcmps[34]));
		b6.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		
		b6.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		b6.add(panel11);
		
		JPanel ptab3 = new JPanel(new BorderLayout());
		GUIUtilities.setEmptyBorder(ptab3);
		ptab3.add(b6, BorderLayout.NORTH);
		
		// si es persona natural, para evitar confucion inhabilito todos los
		//componentes - actividad comercial y retencion
		if (natural) {
	//		GUIUtilities.setEnabled(new JComponent[] {ptab3}, false);
	//		GUIUtilities.setEnabled(new JComponent[] {b16}, true);
	//		GUIUtilities.setEnabled(new JComponent[] {jcmps[20]}, true);
		}

		
		//-------------------------------------------
		// persona natural
		//-------------------------------------------
		JPanel panel10 = new JPanel(new GridLayout(2, 3, 
			GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP));
		panel10.add(GUIUtilities.getCoupleInBox(
			rcd.getFieldName(27), jcmps[27], natural, true));
//		panel10.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(28), jcmps[28]));
		panel10.add(GUIUtilities.getCoupleInBox(rcd.getFieldName(30), jcmps[30]));
		panel10.add(jcmps[29]);
		panel10.add(jcmps[31]);
		panel10.add(jcmps[32]);
		
		JPanel ptab5 = new JPanel(new BorderLayout());
		GUIUtilities.setEmptyBorder(ptab5);
		ptab5.add(panel10, BorderLayout.NORTH);

		//-------------------------------------------
		// pestañas
		//-------------------------------------------
		JTabbedPane jtPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		jtPane.addTab(DIMain.bundle.getString("i13"), ptab1);
		if (natural) {
			jtPane.addTab(DIMain.bundle.getString("p05"), ptab5);
		}
		jtPane.addTab(DIMain.bundle.getString("r09"), ptab2);
		jtPane.addTab(DIMain.bundle.getString("r10"), ptab3);
		

		// Acciones
		AbstractAction[] bts = new AbstractAction[] {
			new AceptAction(ca), new CancelAction(ca)};
		this.aceptar = setActionBar(bts);
		setDefaultButton(aceptar);
		add(jtPane);

		preValidate();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.AbstractRecordDataInput#getRecord()
	 */
	public Record getRecord() {
		Record rcd = super.getRecord();
		rcd.setFieldValue(4, dir_1.getRecord());
		rcd.setFieldValue(14, dir_2.getRecord());
		return rcd;
	}
	/* (non-Javadoc)
	 * @see com.ae.datainput.AbstractRecordDataInput#validateFields()
	 */
	public boolean validateFields() {
		JComponent[] jcmps = getInputComponents();
		boolean ok = true;
		if (natural) {
			JTextField jtf = (JTextField) jcmps[27]; 
			if (jtf.getText().equals("0")) {
				showMessage("msg95");
			}
		}
		
		// los campos de contador estan todos llenos o todos vacios
		boolean n = !jtf_cnom.getText().trim().equals("");
		boolean cp = !jtf_ccpc.getText().trim().equals("");
		boolean ce = !(((Number) jftf_cced.getValue()).doubleValue() == 0.0);
		if ((n || cp || ce) && !(n && cp && ce)) {
			showMessage("msg96");
		}
		 
		// la pestaña con los datos del representante lega estan todos llenos o todos vacios
		boolean rr = !jtf_rrif.getText().trim().equals("");
		boolean rc = ((Number) jftf_rced.getValue()).intValue() > 0;
		boolean rn = !jtf_rnom.getText().trim().equals("");
		boolean rd = !jtf_rdir.getText().trim().equals("");
		if ((rr || rc || rn || rd) && !(rr && rc && rn && rd)) {
			showMessage("msg97");
		}
		
		return ok; 
	}
}