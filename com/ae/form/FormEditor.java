/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form;

import java.awt.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.jdom.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.gui.*;
import com.ae.resource.*;

/** presenta las operaciones que el usuario puede efectura a la clase 
 * <code>Form</code> tratandola como unidad. (guardarla, imprimirla etc.)
 *  
 */
public class FormEditor extends RightPanel {
	
	private Form form;
	private String f_name;
	private FormSavedRecord fs_record;
	private AppTableModel tableM;
	private Hashtable propertyList;
	private AbstractCommand command;
	private boolean newrcd;

	/** nuevo editor de formas.
	 * 
	 * @param fn - nombre de la forma
	 * @param tpr - contribuyente al cual se le asignara/editara esta forma
	 * @param rcd - registro seleccionada para edicion o <code>null</code> si
	 * es una nueva asignacion
	 * 
	 */	
	public FormEditor(String f, TaxPayerRecord tpr, Record rcd) {
		super("FormEditor");
		installProgressBar(ProgressPanel.FORM);
		this.f_name = f;
		this.newrcd = false;
		if (rcd == null) {
			this.fs_record = new FormSavedRecord();
			this.newrcd = true;
			fs_record.setFieldValue("fsForma", f_name);
			fs_record.setFieldValue("fsRif", tpr.getFieldValue("tpRif"));
			// la fecha es la de hoy
			fs_record.setFieldValue("fsNombre", (String) tpr.getFieldValue("tpNombre"));
			// estado: activo
			// consecutivo: mili del dia de hoy
			this.form =  new Form(f_name, tpr, this, null, true);
			this.command = new NewRecordCommand(fs_record);
		} else {
			this.fs_record = (FormSavedRecord) 
				PrevalentSystemManager.getRecordFrom(new FormSavedRecord(), rcd.getKey());
			Hashtable h = (Hashtable) fs_record.getFieldValue("fsForSec");
			Boolean bs = (Boolean) fs_record.getFieldValue("fsStatus");
			this.form = new Form(f_name, tpr, this, h, bs.booleanValue());
			this.command = new EditRecordCommand(fs_record); 
		}
		
		// se intenta dar un valor estimadoa para el tamaño del editor basa
		// basado en el tamaño de la imagen.
		Hashtable h = PrevalentSystemManager.getFormElements(f);
		Element e = (Element) h.get("overse.image");
		ImageIcon ii = new ImageIcon(
			ResourceUtilities.getPluginsPath() + e.getAttributeValue("name") + ".gif");
		setPreferredSize(new Dimension(ii.getIconWidth(), ii.getIconHeight()));
	}
	
	/** ejecutado invocado por las acciones para iniciar el proceso 
	 * de creacion o edicion de la forma.
	 * correccion de error en equipos rapidos.
	 *
	 */
	public void init() {
		Thread t = new Thread(form);
		t.start();
	}
	
	
	/** Retorna la forma que este editor esta editando.
	 * 
	 * @return forma en edicion
	 */
	public Form getForm() {
		return form;
	}
	
	/** retorna el nombre de la forma en edicion
	 * 
	 * @return nombre de la planilla
	 */
	public String getFormName() {
		return f_name;
	}
	
	/** actualiza/adiciona los cambios efectuados a la forma que se esta 
	 * editando. 
	 *
	 */
	public void executeCommand() {
		Hashtable h = form.getSectionsIntervalValue();
		fs_record.setFieldValue("fsForSec", h);
		PrevalentSystemManager.executeCommand(command);
	}
	
	/** completa la configuracion del editor, actualiza el archivo de formas
	 * y descuenta la forma asociada 
	 * 
	 * ejecutado desde Form.run()
	 */
	public void complete() {
		try {
			
			// nuevo registro? descuenta
			if (newrcd) {
				StockRecord str = (StockRecord) PrevalentSystemManager.getRecordFrom(
					new StockRecord(), f_name);
				int c = ((Integer) str.getFieldValue("sCantida")).intValue(); 
				str.setFieldValue("sCantida", new Integer(c - 1)); 
				PrevalentSystemManager.getPrevayler(str).executeCommand(
					new EditRecordCommand(str));			
				executeCommand();
			}
			setVisible(false);
			Boolean b = (Boolean) fs_record.getFieldValue("fsStatus");
			super.setToolBar(new Component[] {
				new JButton(new SaveFormAction(this, b.booleanValue())),
				new JButton(new PrintFormAction(this)),
				new JButton(new TurnPageAction(this)),
			});
			add(new JScrollPane(form));
			setVisible(true);
		} catch(Exception e) {
			// no deberia haber ninguna
			Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
		}
	}
}
