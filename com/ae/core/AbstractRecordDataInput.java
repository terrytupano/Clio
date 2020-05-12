/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

import java.awt.event.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import com.ae.gui.*;
import com.ae.input.*;
import com.ae.resource.*;

/** Provee funcionalidad basica a todos los paneles de entrada o modificacion de
 * datos de un registro. Sub clases deben implementar los metodos abstractos para
 * dar soporte personalizado. 
 */
public abstract class AbstractRecordDataInput extends RightPanel implements 
	ActionListener, DocumentListener, FocusListener {
		
	private JButton d_button;
	private boolean newR;
	private Record rcdModel;
	protected AppTableModel tableModel;
	private Vector v_component;

	/** nueva sub-clase.
	 * 
	 * @param dnam - Nombre del documento a mostrar en la barra de informacion
	 * @param rcd - model
	 * @param nr - <code>true</code> si es un nuevo registro, <code>false</code>
	 * si es edicion.
	 */
	public AbstractRecordDataInput(String dnam, Record rcd, boolean nr) {
		super(dnam);
		this.newR = nr;
		this.v_component = new Vector();
		this.d_button = null;
		this.rcdModel = rcd;
		this.tableModel = PrevalentSystemManager.getTableModel(rcdModel, false);
		
	}
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		preValidate(); 
	}
	/** ejecuta <code>addInputComponent(cmp, false)</code>
	 * 
	 * @param cmp - componente a añadir
	 */
	public void addInputComponent(JComponent cmp) {
		addInputComponent(cmp, false);
	}
	
	/** Adiciona el componente de entrada a la lista de componentes.  si
	 * el componente es instancia de <code>JTextField</code>, establece
	 * los verificadores de entrada y los listener correspondientes.
	 * 
	 * -------------
	 * NOTA: existen momentos donde se disparan 3 o mas eventos debido a
	 * que existen varios tipos de eventos asociados a un componente.
	 * ----------- 
	 * 
	 * @param cmp componente de entrada
	 * @param req = true, el componente es de entrada obligatoria, = false, 
	 * no lo es.
	 */
	public void addInputComponent(JComponent cmp, boolean req) {
		v_component.add(cmp);
		if ((cmp instanceof JTextField && req == true) ||
			cmp instanceof JFormattedTextField) {
			JTextComponent jtec = (JTextComponent) cmp;
			jtec.setInputVerifier(new TextFieldVerifier(this, req));
			jtec.addFocusListener(this);
			jtec.getDocument().addDocumentListener(this);
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent de) {
	//	validateRecord(); 
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent fe) {
		
	}
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent fe) {
		preValidate(); 
	}
	/** Puente para facil acceso a metodo equivalente en <code>AppConstants</code>
	 * 
	 * @param key codigo de la constante
	 * @return valor
	 */
	public AppEntry getConstant(String key) {
		return AppConstants.getConstant(key);
	}
	/** Puente para facil acceso a metodo equivalente en <code>AppConstants</code>
	 * 
	 * @param typ - tipo de constantes 
	 * @return lista de constantes dentro del systema
	 */
	public AppEntry[] getConstantsOfType(String typ) {
		return AppConstants.getConstantsOfType(typ);
	}

	/** Retorna <code>JButton</code> que debe ser establecido como boton por defecto para 
	 * 
	 * @return botnn por omision 
	 */	
	public JButton getDefautlButton() {
		return d_button;
	}

	/** Retorna Registro interno de componentes de entrada. 
	 * Este arreglo de componentes es creado por el metodo addInputComponent que es invocado por
	 * subclases para añadir los componentes de entrada de datos. Estos deben estar
	 * en el mismo orden de los campos de <code>Record</code> que representan o a los que le da
	 * entrada.
	 * 
	 * @return lista de componentes de entrada
	 */
	public JComponent[] getInputComponents() {
		return (JComponent[]) v_component.toArray(new JComponent[0]);
	}

	/** retorna nueva instancia del registro que se esta manteniendo. Esta es
	 * implementacion basica y retorna el tipo de objeto segun el componente
	 * que se destino para recibir la entrada (ej: 
	 * <code>JTextField -> String</code>, <code>JCheckBox -> Boolean</code> 
	 * Etc.
	 * 
	 * @return Registro nuevo o modificado
	 */	
	public Record getRecord() {
		Record r = null;
		try {
			r = (Record) ((Class) rcdModel.getClass()).newInstance();
		} catch(Exception e) {
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
		for (int c = 0; c < v_component.size(); c++) {
			JComponent jcmp = (JComponent) v_component.elementAt(c);
			if (jcmp instanceof JFormattedTextField) {
				Object oval = ((JFormattedTextField) jcmp).getValue();
				if (oval instanceof Number) {
					Number n = (Number) oval;
					if (r.getFieldValue(c) instanceof Integer) {
						r.setFieldValue(c, new Integer(n.intValue()));
					}
					if (r.getFieldValue(c) instanceof Double) {
						r.setFieldValue(c, new Double(n.doubleValue()));		
					}
				}
				if (oval instanceof java.util.Date) {
					AppDate ad = new AppDate();
					ad.setTime(((Date) oval).getTime()); 
					r.setFieldValue(c, ad);		
				}
				// porque tambien es instancia de JTextfield
				continue;
			}
			if (jcmp instanceof JTextComponent) {
				// si es addressrecord lo salto.
				if (!(r.getFieldClass(c) == AddressRecord.class)) {
					r.setFieldValue(c, ((JTextComponent) jcmp).getText());
				}
			}
			if (jcmp instanceof JCheckBox) {
				r.setFieldValue(c, new Boolean(((JCheckBox) jcmp).isSelected()));
			}
			if (jcmp instanceof JComboBox) {
				AppEntry ae = (AppEntry) (((JComboBox) jcmp).getSelectedItem());
				r.setFieldValue(c, ae.getKey());
			}
		}
		return r;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent de) {
		preValidate(); 
	}
	
	/** Metodo auxiliar que inicia la validacion de datos.
	 * Comienza validando que todos los campos de entrada obligatoria contengan
	 * algun valor. Luego llama a ValidateFields() para que cada sub clase
	 * continua con las validaciones propias
	 */
	protected void preValidate() {
		showMessage(null);
		
		// verificacion de campos obligatorios
		boolean all = true;
		for(int c = 0; c < v_component.size(); c++) {
			JComponent icmp = (JComponent) v_component.elementAt(c);

			// ver addInputComponent()
			if (icmp instanceof AddressEditor) {
				AddressRecord dr = (AddressRecord) ((AddressEditor) icmp).getRecord();
				if (dr.getFieldValue("adUrbani").equals("")) {
					all = false;
					break;
				}				 
			}
			if (icmp.getInputVerifier() != null) {
				TextFieldVerifier ver = 
					(TextFieldVerifier) icmp.getInputVerifier();
				if (!ver.verify(icmp)) {
					all = false;
					break;
				}
			}
		}

		// si es un nuevo registro, trata de determinar se ya existe dentro
		// del modelo
		if (newR) {
			rcdModel = getRecord();
			if (tableModel.getRecord(rcdModel.getKey()) != null) {
				showMessage("msg88");
			}
		}
		if (!isShowingError()) {
			validateFields();
		} 
		d_button.setEnabled(!isShowingError() && all);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */	
	public void removeUpdate(DocumentEvent de){
		preValidate(); 
	}

	/** establece el boton que sera usado por <code>JRootPane</code> como DefautlButton.
	 * Este, generalmente, deberia ser un boton creado apartir de una instacia de
	 * <code>AceptAction</code> 
	 * 
	 * @param a boton por omision 
	 */
	public void setDefaultButton(JButton a) {
		this.d_button = a;
	}
	
	/** Validacion de campos de entrada de registro. 
	 * Sub clases deben implementar este metodo para validar la situacion de los
	 * componentes de entrada y su relacion, prsentar los mensajes pertinentes y 
	 * realizar los cambios dentro del GUI para asegurar que el usuario coloque los 
	 * valores conrrectos en los componentes correctos.
	 *  
	 * la secuencia estandar de eventos de validacion es:
	 * 
	 * 1- se verifican que los campos marcados como de entrada obligatoria
	 * contengan algun valor.
	 * 2- si se esta adicionando un nuevo registro, se verifica que no se este
	 * intentando adicionar un registro existente.
	 * 3. si hasta ahora no se ha detectado errores, se ejecuta este evento.  
	 * 4. despues de la ejecucion de este metodo, esta clase determina si se debe
	 * o no habilitar boton designado como boton por omision evaluando si 
	 * se esta presentando algun mensaje de error Y el valor retornado por este
	 * metodo es true
	 * 
	 * <b>Nota sobre implementacion:</b> validar los campos en orden inverso; es decir,
	 * validar primero los valores de los componentes que se encuentran mas abajo
	 * de la ventana continuando hacia los de arriba. Esto es para que se
	 * presente el mensaje de error del componente que se encuentre mas arriva
	 * 
	 * @return true si todo esta ok, false de lo contrario
	 */
	public abstract boolean validateFields();
	
	/** obtiene <code>String</code> desde <code>ResourceBundle</code>
	 * 
	 * @param arg - key
	 * @return <code>String</code> 
	 */
	public String getString(String arg) {
		return DIMain.bundle.getString(arg);
	}

}
