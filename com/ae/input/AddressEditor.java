
package com.ae.input;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import com.ae.gui.*;
import com.ae.action.*;
import com.ae.core.*;
import com.ae.resource.*;

/** Panel de edicion de direcciones. Clases que editan registro con direccines en
 * debe usar esta clase para editar y presentar las direcciones en forma uniforme.
 * 
 */
public class AddressEditor extends AbstractRecordDataInput {

	private JButton aceptar;
	private Record model;
	private AddressEditAction editAction;
	private JTextArea jta_adr;
	private JLabel jl_lab;
	private JTextField textf;

	/** nueva instancia del editor de direcciones
	 * 
	 * @param lab - componente con texto que sera colocado como encabezado
	 * cuando se solicite el ticket
	 * @param idcmp - componente que sera actualizado con el id de direcccion
	 */	
	public AddressEditor(JLabel lab, AddressRecord adr, JTextField jtf1) {
		super("AddressInfo", adr, false);
		this.model = adr;
		this.jl_lab = lab;
		this.textf = jtf1;
		
		// estado
		JTextField jtf = GUIUtilities.getJTextField(model, 0);
		addInputComponent(jtf, true);

		//ciudad
		jtf = GUIUtilities.getJTextField(model, 1);
		addInputComponent(jtf, true);

		// Municipio
		jtf = GUIUtilities.getJTextField(model, 2);
		addInputComponent(jtf);		

		// Parroquia
		jtf = GUIUtilities.getJTextField(model, 3);
		addInputComponent(jtf);		

		// Urbanizacion
		jtf = GUIUtilities.getJTextField(model, 4);
		addInputComponent(jtf, true);		
		textf.setText(jtf.getText()); 

		// calle
		jtf = GUIUtilities.getJTextField(model, 5);
		addInputComponent(jtf, true);		

		// edf
		jtf = GUIUtilities.getJTextField(model, 6);
		addInputComponent(jtf, true);		

		// Cod geografico
		jtf = GUIUtilities.getJTextField(model, 7);
		addInputComponent(jtf);		

		// Zona postal
		jtf = GUIUtilities.getJTextField(model, 8);
		addInputComponent(jtf);		

		// tlf1
		jtf = GUIUtilities.getJTextField(model, 9);
		addInputComponent(jtf, true);		

		// tlf2
		jtf = GUIUtilities.getJTextField(model, 10);
		addInputComponent(jtf);		

		// fax
		jtf = GUIUtilities.getJTextField(model, 11);
		addInputComponent(jtf);		

		// local
		jtf = GUIUtilities.getJTextField(model, 12);
		addInputComponent(jtf);		

		JComponent[] jcmps = getInputComponents();
		
		//-------------------------------------------
		// panel de entrada
		//-------------------------------------------
		JPanel panel = new JPanel(new GridLayout(0, 2, 
			GUIUtilities.HORIZONTAL_GAP, GUIUtilities.VERTICAL_GAP));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(0), jcmps[0], true, true));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(1), jcmps[1], true, true));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(2), jcmps[2]));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(3), jcmps[3]));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(4), jcmps[4], true, true));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(5), jcmps[5], true, true));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(6), jcmps[6], true, true));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(12), jcmps[12]));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(7), jcmps[7]));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(8), jcmps[8]));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(9), jcmps[9], true, true));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(10), jcmps[10]));
		panel.add(GUIUtilities.getCoupleInBox(model.getFieldName(11), jcmps[11]));

		// Acciones
		this.editAction = new AddressEditAction(this);
		AbstractAction[] bts = new AbstractAction[] {
			new ClearAddress(editAction), new AceptAction(editAction), new CancelAction(editAction)};
		this.aceptar = setActionBar(bts);
		setDefaultButton(aceptar);
		add(panel);
		this.jta_adr = GUIUtilities.getJTextArea(40); 
		jta_adr.setText(CoreUtilities.formatAddress((AddressRecord) getRecord()));
		jta_adr.setToolTipText(
			((String) AppConstants.getConstant("addrTicket").getValue()));	
		preValidate();
	}
	/* (non-Javadoc)
	 * @see com.ae.datainput.AbstractRecordDataInput#validateFields()
	 */
	public boolean validateFields() {
		return true;
	}
	
	/** Invocado por <code>AddressEditAction</code> cuando el usuario presiona
	 * algun boton dentro del dialogo de entrada
	 * 
	 * @param aaa - accion presionada
	 */
	public void done(AbstractAction aaa) {
		JComponent[] jc = getInputComponents();
		if (aaa instanceof ClearAddress) {
			for (int i = 0; i < jc.length; i++) {
				((JTextField) jc[i]).setText("");
			}
		}
		jta_adr.setText(CoreUtilities.formatAddress((AddressRecord) getRecord()));
		textf.setText(((JTextField) jc[4]).getText()); 
	}
	/** Retorna la interfaz de entrada/salida de direccion. Esta permite a las
	 * clases con campos de direccion editar direcciones a travez de este panel de
	 * entrada y a su vez, presenta la direccion redactada de manera uniforme
	 * 
	 * @return panel con componentes de entrada/salida de direcciones.
	 */
	public JPanel getTicket() {
		JPanel jp = new JPanel(new BorderLayout());
		JButton jb = new JButton(editAction);
		Dimension d = new Dimension(32,32);
		jb.setMinimumSize(d);
		jb.setPreferredSize(d);
		jb.setMaximumSize(d);
		Box b1 = Box.createHorizontalBox();
		b1.add(jb);
		b1.add(jta_adr);
		Border b = jb.getBorder();
		jb.setBorder(null);
		jb.setBorder(new MatteBorder(0, 1, 0, 0, Color.GRAY));
		b1.setBorder(new LineBorder(Color.GRAY, 1));
		b1.add(jb);
		Box b2 = Box.createHorizontalBox();
		b2.add(jl_lab);
		b2.add(Box.createHorizontalGlue());
		jp.add(b2, BorderLayout.NORTH);
		jp.add(b1, BorderLayout.CENTER);
		
		return jp;
	}	
}