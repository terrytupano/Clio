/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import java.awt.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import com.ae.core.*;
import com.ae.form.*;
import com.ae.resource.*;

/** Permite una entrada sencilla para un seccion dentro de la forma. Esta clase
 * busca el atributo <code>sectionValue</code> para determinar la clase del objeto
 * que representa el valor para esta seccion; ademas, crea el componente de entrada
 * correspondiente.
 * 
 * si <code>sectionValue="Double"</code> la seccion sumariza los montos y
 * presenta el total.
 * 
 * si <code>sectionValue="Integer"</code> la seccion cuenta en nro de
 * transacciones y presenta la cuenta
 * 
 * durante <code>setInitialValues(TaxPayerRecord)</code> esta clase:
 * - obtiene una lista de <code>Account_ClioObjectRecord</code> para el id
 * se seccion al cual esta instancia esta asociada.
 * 
 * durante <code>recalc()</code>
 * - se obtienen todas las transacciones que se encuentren en el rango de fecha
 * de la declaracion, (*) se sumarizan o se cuentan y se presentan como valor 
 * dentro del componente de entrada.
 * 
 * Si una instancia de esta clase puede modificar el valor de una seccion 
 * a travez de transacciones , se adiciona un <code>JCheckBox</code>
 * que permite al usuario decidir entre si usar el valor sumarizado de contablidad
 * o uno que se coloque manualmente.
 * 
 * (*) el rango de la declaracion esta determinado por los atributos globales
 * <code>declarationDateStart y declarationDateEnd</code>
 */
public class SimpleInput extends AbstractSection {
	
	private JComponent iComponent;
	private InputRequestPanel inputRP;
	private JCheckBox jcb_totVou;
	private Object oldVal;
	private String s_value;
	private String tp_ID, fo_ID, se_ID;
	private Vector vec_acc_clio, vec_voucher;
	private AccountRecord acc_rcd;
	
	/** Constructor
	 * 
	 */
	public SimpleInput() {
		setSectionValue("");
		this.oldVal = null;
		this.vec_acc_clio = new Vector(0);
		this.vec_voucher = new Vector(0);
		this.tp_ID = ""; fo_ID = ""; se_ID = "";
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#createInputRequestPanel()
	 */
	public InputRequestPanel createInputRequestPanel() {
		this.inputRP = new InputRequestPanel(form);
		
		// transacciones asociadas?
		this.jcb_totVou = FormUtilities.getJCheckBox(form, "u02");
		jcb_totVou.setEnabled(false);
		if (vec_acc_clio.size() > 0) {
			jcb_totVou.setSelected(true);
			jcb_totVou.setEnabled(true);
		}
		
		Box b1 = Box.createVerticalBox();
		b1.add(Box.createHorizontalStrut(FormUtilities.VERTICAL_GAP));
		b1.add(jcb_totVou);

		// intenta localizar el atributo label, si no existe coloca el titulo
		String lab = getAttribute("label");
		lab = (lab == null) ? getAttribute("title") : lab;
		
		JPanel jp = FormUtilities.getJPanel(form, false);
		
		jp.setLayout(new BorderLayout());
//		jp.setLayout(new GridLayout(2, 1));
		jp.add(FormUtilities.getJLabel(form, lab), BorderLayout.NORTH);
		jp.add(iComponent, BorderLayout.SOUTH);
		
		JPanel jp1 = FormUtilities.getJPanel(form, true);
		jp1.setLayout(new BorderLayout());
		jp1.add(jp, BorderLayout.NORTH);
		
		if (s_value.equals("Double") || s_value.equals("Integer")) {
			jp1.add(b1, BorderLayout.SOUTH);
		}
		inputRP.add(jp1, iComponent);
		return inputRP;
	}
	
	/** determina el tipo de instancia del la seccion seccion mencionada 
	 * en <code>gv.declarationDate???</code> y crea <code>AppDate</code> 
	 * segun el caso. Si la fecha no puede deterninarse, se retorna la fecha
	 * de hoy
	 * 
	 * @param sid - seccion
	 * @return <code>Appdate</code>
	 */
	private AppDate getAppDate(String sid) {
		AppDate rv = null;
		Object unk = form.getVariableValue(sid);
		if (unk instanceof AppDate) {
			rv = (AppDate) unk;
		}
		if (unk instanceof Double) {
			int dv = (int) ((Double) unk).doubleValue();
			String s = (dv == 0) ? 
				new AppDate().toString() : String.valueOf(dv);
			rv = new AppDate(s.substring(0, 4), s.substring(4, 6), s.substring(6, 8));

/*			
			int y = Integer.parseInt(s.substring(0, 4));
			int m = Integer.parseInt(s.substring(4, 6));
			int d = Integer.parseInt(s.substring(6, 8));
			GregorianCalendar gc = new GregorianCalendar(y, m - 1, d);
			rv = new AppDate(gc.getTime());
			*/
		}
		return rv;
	}

	/** retorna el valor del componente de entrada.
	 * 
	 * @return valor del componente de entrada
	 */	
	private Object getComponentValue() {
		Object val = null;
		//cantidades editadas
		if(s_value.equals("Amount")){
			AmountEditor ae = (AmountEditor) iComponent;
			val = new Double(ae.getAmount());
		}
				
		// entradas a travez de componentes de texto
		if (s_value.equals("String")) {
			JTextField jtf =(JTextField) iComponent;
			val = jtf.getText();
		}

		// numeros 
		if (s_value.equals("Integer")) {
			JFormattedTextField jftf = (JFormattedTextField) iComponent;
			Number num = (Number) jftf.getValue();
			val = new Integer(num.intValue());
		}
		if (s_value.equals("Double")) {
			JFormattedTextField jftf = (JFormattedTextField) iComponent;
			Number num = (Number) jftf.getValue();
			val = new Double(num.doubleValue());
		}
		return val;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#getSectionInternalValue()
	 */
	public Object getSectionInternalValue() {
		Object val = null;
		if (s_value.equals("Amount")) {
			AmountEditor ae = (AmountEditor) iComponent;
			val = ae.getInternalValue();
		} else {
			val = getComponentValue();
		}
		return val;
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputConclude(boolean)
	 */
	public void inputConclude(int c) {
		if (c == InputRequestPanel.ACEPT) {
			setSectionValue(getComponentValue());
		} else {
			setSectionInternalValue(oldVal);
		}
		form.inputConclude();
	}
	
	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#inputRequest(java.awt.Point)
	 */
	public void inputRequest(Point p) {
		oldVal = getSectionInternalValue();
		inputRP.inputRequest(this, p);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#recalc()
	 */
	public void recalc() {

		// solo recalculo si hay datos y si estado es activo
		if (!form.getStatus() || vec_acc_clio.size() < 1){
			return;
		}			
		
		// mejora de rendimiento si en la forma antes del calculo 
		// se seleccionan los moviminetos segun el rango. asi cada 
		// instancia de esta clase solo buscara en la seleccion hecha 
		// una vez por la forma y no en toda la lista.
		
		// secciones con la informacion sobre el rango de declaracion
		AppDate ads = getAppDate(
			(String) form.getAttribute("gv.declarationDateStart"));
		AppDate ade = getAppDate(
			(String) form.getAttribute("gv.declarationDateEnd"));

		// sumarizo solo los de igual entrada y dentro del rango
		double totalVoucher = 0.0;
		int countVoucher = 0;
		for (int vo = 0; vo < vec_acc_clio.size(); vo++) {
			Record acr = (Record) vec_acc_clio.elementAt(vo);
			for (int vi = 0; vi < vec_voucher.size(); vi++) {
				Record vor = (Record) vec_voucher.elementAt(vi);
				AppDate dat = (AppDate) vor.getFieldValue("avFecha");
				if (acr.getFieldValue("coAcCode").equals(vor.getFieldValue("avAccAC"))
					&& (ads.getTime() <= dat.getTime() && dat.getTime() <= ade.getTime())) {
					totalVoucher += preCalculus(acr, vor);							
//					totalVoucher += ((Double) vor.getFieldValue("avMonto")).doubleValue();
					countVoucher += 1;
				}
			}
		}	  
		if (jcb_totVou.isSelected()) {
			Object val = null;
			if (s_value.equals("Integer")) {
				val = new Integer(countVoucher);
			} else {
				val = new Double(totalVoucher);
			}
			setSectionValue(val);
			((JFormattedTextField) iComponent).setValue(val);
		}
	}
	
	/** evalua la expresion descrita en el atributo precal dentro de una 
	 * cuanta contable. Si no hay ningun atributo este metodo retorna el
	 * monto sin alterar 
	 * 
	 * @param acr - registro de AccountClioObjectRecord
	 * @param vor - registro de VoucherRecord
	 * @return valor con o sin precalculo
	 */
	private double preCalculus(Record acr, Record vor) {
		acc_rcd.setFieldValue("acCode", acr.getFieldValue("coAcCode"));
		double d = ((Double) vor.getFieldValue("avMonto")).doubleValue();
		String fu = (String) 
			PrevalentSystemManager.getRecordFrom(acc_rcd, acc_rcd.getKey()).getFieldValue("acPreC"); 
		if (!fu.equals("")) {
			try {
				form.evaluator.putVariable("var", vor.getFieldValue("avMonto").toString());
				form.evaluator.parse(fu);
				d = (Double.valueOf(form.evaluator.evaluate(fu))).doubleValue();
			} catch (Exception e) {
				Logger.getLogger("").logp(Level.WARNING, null, null, 
					"(" + fu + ")", e);
			}
		}
		return d;
	}

	/* (non-Javadoc)
	 * @see com.ae.form.AbstractFormSection#setInitialValues(com.ae.uitax.TaxPayerRecord)
	 */
	public void setInitialValues(TaxPayerRecord tpr) {
		this.s_value = getAttribute("sectionValue");
		s_value = (s_value == null) ? "String" : s_value;

		// establece el tipo de componente de entrada y el valor inicial de
		// la seccion para que otras secciones puedan acceder al valor correcto
		if (s_value.equals("String")) {
			setSectionValue("");
			this.iComponent = FormUtilities.getJTextField(form, 20);
		}
		if (s_value.equals("Integer")) {
			setSectionValue(new Integer(0));
			this.iComponent = FormUtilities.getJFormattedTextField(form, 
				new Integer(0));
		}
		if (s_value.equals("Double")) {
			setSectionValue(new Double(0.0));
			this.iComponent = FormUtilities.getJFormattedTextField(form,
				new Double(0.0));
		}
		if (s_value.equals("Amount")) {
			setSectionValue(new Double(0.0));
			this.iComponent = new AmountEditor(form);
		}

		// informacion contable
		if (s_value.equals("Double") || s_value.equals("Integer")) {
			
			// lista de Account_ClioObjectRecord
			this.tp_ID = (String) tpr.getFieldValue("tpRif");
			this.fo_ID = form.getFormID();
			this.se_ID = getAttribute("id");
			AppTableModel tmodel = 
				PrevalentSystemManager.getTableModel(new Account_ClioObjectRecord());
			tmodel.selectRecord(
				"'{coRifTP}' == '" + tp_ID +
				"' && '{coObjID}' == '" + fo_ID +
				"' && '{coObject}' == '" + se_ID + 
				"' && '{coAcCode}' != ''"
				);
			this.vec_acc_clio = tmodel.getRecords(false);

			// registro cuenta contables
			this.acc_rcd = new AccountRecord();
			acc_rcd.setFieldValue("acRifTP", tp_ID);
			
			// lista de comprobantes 
			tmodel = 
				PrevalentSystemManager.getTableModel(new VoucherRecord());
			tmodel.selectRecord("'{avRifTP}' == '" + tp_ID + "'");
			this.vec_voucher = tmodel.getRecords(false);
		}
	
		setHorizontalAlignment();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.form.AbstractSection#setSectionInternalValue(java.util.Vector)
	 */	
	public void setSectionInternalValue(Object c) {
		if (iComponent instanceof AmountEditor) {
			AmountEditor a = (AmountEditor) iComponent;
			AppEntry ae = (AppEntry) c;
			a.setInternalValue(ae);
			setSectionValue((Double) ae.getValue());
			return;
		}
		 
		if (iComponent instanceof JFormattedTextField) {
			JFormattedTextField f = (JFormattedTextField) iComponent;
			f.setValue(c);
			setSectionValue(c);
			return;
		}

		if (iComponent instanceof JTextField) {
			JTextField t = (JTextField) iComponent;
			t.setText((String) c);
			setSectionValue((String) c);
		}
	}
}
