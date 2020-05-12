/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form.sections;

import java.awt.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import com.ae.form.*;
import com.ae.core.*;
import com.ae.resource.*;

/** Provee implementacion basica para todas las secciones. Usa seccion es un rectangulo
 * dentro de una planilla y que representa un concepto que puede parametrizarce a travez
 * del archivo de configuracion de la forma. Sub clases deben umplementar un conjunto de metodos 
 * los cuales forman parte del
 * protocolo de comunicaciones con la forma que los contiene. Aunque todas las
 * secciones contienen un componente de salida que da formato y presenta el 
 * valor de la seccion dentro de la forma, basicamente, una seccion puede ser:
 * - salida solo presenta el valor de la seccion el cual fue obtenido mediante la 
 * evaluacion de una expresion o presenta un campo de base de datos.
 * - de entrada: solicita datos de entrada (generalmente mendiante una instancia de
 * <code>InputRequestPanel</code>
 * 
 * - se debe almacenar el valor interno de esta seccion.
 */
public abstract class AbstractSection extends JPanel implements Keeper {

	private Hashtable attributes;
	private JLabel jl_out;
	private int mode;
	private Object sectionValue;

	protected Form form;

	/** Constructor por omision 
	 *
	 */
	public AbstractSection() {
		super();
	}
	
	

	/** Establece o crea instancia de <code>InputRequestPanel</code>. 
	 * Sub-clases deben implementar 
	 * este metodo si desean crear una instacia de <code>InputRequestPanel</code>.
	 * El valor de retorno es usado para establecer el entorno funcional de la forma 
	 * durante la ejecucion de <code>registreSections()</code>.
	 * 
	 * @param <code>InputRequetPanel</code> asignado a esta seccion.
	 */
	public abstract InputRequestPanel createInputRequestPanel();
	
	/** Crea componente de salida y lo alinea dentro de este panel segun indicaciones.
	 * Sub clases que necesitan una forma mas avanzada de presentacion deben alterar 
	 * este metodo y en general, tambien <code>formatOuput()</code>
	 *
	 */
	public void createOutputComponent() {
		if (jl_out == null) {
			this.jl_out = new JLabel();
			jl_out.setHorizontalAlignment(JLabel.LEFT);
			jl_out.setFont((Font) form.getAttribute("panel.sectionFont"));
			setHorizontalAlignment();
			String g = getAttribute("eastGap");
			g = (g == null) ? "5" : g;
			int eg = Integer.parseInt(g);
			g = getAttribute("westGap");
			g = (g == null) ? "5" : g;
			int wg = Integer.parseInt(g);
			setLayout(new BorderLayout());
			add(Box.createHorizontalStrut(wg), BorderLayout.WEST);
			add(jl_out, BorderLayout.CENTER);
			add(Box.createHorizontalStrut(eg), BorderLayout.EAST);

			// debugg
			if (form.getAttribute("gv.debugg") != null && 
				form.getAttribute("gv.debugg").equals("yes")) {
					jl_out.setOpaque(true);
					jl_out.setBackground(Color.LIGHT_GRAY);
			}
			makeToolTip();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#createToolTip()
	 */
	public JToolTip createToolTip() {
		JToolTip ttip = super.createToolTip();
		ttip.setBackground((Color) form.getAttribute("panel.background"));
		ttip.setForeground((Color) form.getAttribute("panel.foreground"));
		ttip.setFont((Font) form.getAttribute("panel.font"));
		return ttip;
	}
	
	/** crea texto para tooltips del componente de salida. 
	 * 
	 */
	private void makeToolTip() {
		String l = getAttribute("label");
		String t = getAttribute("title");
		
		// si no tiene ni titulo ni texto
		if (l == null && t == null) {
			return;
		}
		String fstt = "<html> <b>" + t + "</b><p>" + l + "</p></html>";
		// si uno de ellos es nulo
		if (l == null || t == null) {
			fstt = (l == null) ? t : l;
		} 
		
		setToolTipText(fstt);
	}
	
	/** metodo utilitiario que establece la alinecacion indicada en las
	 * preferencias de la aplicacion.
	 * 
	 * comentada, no hace nada
	 * 
	 */
	public void setHorizontalAlignment() {
		/*
		// la alineacion es solo para los numeros
		if (getSectionValue() instanceof Number) {
			Integer ha = 
				(Integer) DIMain.preferenceRecord.getFieldValue("prAliNum");
			jl_out.setHorizontalAlignment(ha.intValue());
		}
		*/
	}
	
	/** Da formato y presenta el valor interno de la seccion. Este metodo es
	 * frecuentemente invocado por <code>Form</code> para descargar en las 
	 * sub-clases de la responsabilidad de presentar sus valores en los formatos
	 * correctos.
	 * 
	 * @return representacion del valor interno
	 *  
	 */
	public void formatOutput() {
		String strVal = FormUtilities.stringFormat(getSectionValue(), true);
		if (strVal.equals("0")) {
			String bl = getAttribute("ifBlank");
			if(bl != null) {
				strVal = bl;
			} else {
				strVal = "";
			} 
		}
		if (getSectionValue() instanceof Boolean) {
			Boolean b = (Boolean) getSectionValue();
			strVal = b.booleanValue() ? "X" : " ";
		}
		
		//debugg
		if (form.getAttribute("gv.debugg") != null && 
			form.getAttribute("gv.debugg").equals("yes")) {
			strVal = getAttribute("id");
		}
		
		jl_out.setText(strVal);
	}
	/** Retorna el valor del atributo aname. 
	 * 
	 * @param aname Nombre del atributo.
	 * @return Valor del atributo aname,
	 */
	public String getAttribute(String aname) {
		return (String) attributes.get(aname);
	}
	
	/** Retorna forma. Este metodo esta colocado aqui para evitar que sub. clases
	 * tengan que guardar una copia de la forma actual. Adicionalmente, form en 
	 * de tipo <code>transient</code> por lo tanto subclases NO DEBEN tener copias
	 * de este objeto. 
	 * 
	 * @return forma 
	public Form form {
		return form;
	}
	 */
	
	/** Retorna componente de salida.
	 * 
	 * @return componente de salida
	 */
	public JLabel getOutputComponent() {
		return jl_out;
	}
	
	/** Este metodo retorna los objetos que conforman el valor interno de esta
	 * seccion. La implementacion por defecto retorna <code>getSectionValue()</code>. 
	 * Este metodo es llamado durante las operaciones de salvar. 
	 * Asi, sub-clases que contenga mas de un objeto como valor deben sobreescribir 
	 * este metodo. 
	 * 
	 * ninguna clase debe retornal <code>null</code> 
	 * 
	 * @return <code>Object</code> que representa el valor de la seccion
	 */
	public Object getSectionInternalValue() {
		return getSectionValue();
	}

	/** utiliza un evaluador interno para evaluar expresiones booleanas
	 * 
	 * @param exp - expresion 
	 * @return true o false
	 */	
	protected boolean inoutIf(String exp) {
		boolean b = false; 
		if (exp != null) {
			try {
				// por ser estatico el evaluador:
				// - no limpiar variables 
				// - usar evaluator.evaluate(expresion) 
				form.evaluator.parse(exp);
				b = form.evaluator.evaluate(exp).equals("1.0") ? true : false;
			} catch (Exception ex) {
				log(exp, ex);
			}
		}
		return b;		
	}

	/** retorna el valor interno de la seccion de esta seccion. 
	 * La implementacion de <code>AbstractSection</code> verifica
	 * el atributo <code>outputIf o inoutIf</code> Si no exite retorna el 
	 * valor de la seccion. Si existe, evalua la expresion. Si esta: 
	 * =true - retorna el valor
	 * =false - retorna la misma instancia del objeto valor pero establecida a
	 * false, 0 o "" segun sea el caso
	 * 
	 * @return Valor - valor de la seccion
	 */
	public Object getSectionValue() {
		boolean b = true;
		String at = getAttribute("outputIf");
		at = (at == null) ? getAttribute("inoutIf") : at;
		if (at != null) {
			b = inoutIf(at);
		}
		Object rv = sectionValue;
		if (!b) {
			if (rv instanceof Double) {
				rv = new Double(0.0);
			}
			if (rv instanceof Boolean) {
				rv = new Boolean(false);
			}
			if (rv instanceof String) {
				rv = "";
			}
			if (rv instanceof Integer) {
				rv = new Integer(0);
			}
			if (rv instanceof AppDate) {
				AppDate rv1 = new AppDate();
				rv1.setTime((long) 0);
				rv = rv1;
			}
		}
		return rv;
	}
	
	/** Entrada de datos culminada. Este metodo es invocado por <code>InputRequestPanel</code>
	 * para indicar que se ha finalizado la entrada de datos.
	 * 
	 * @param c entero que identifica el tipo de finalizacion. Los valores
	 * validos estan en <code>InputRequestPanel</code>. Cada instancia debe
	 * implemetar la logica de manejo para cada uno de ellos
	 * 
	 */
	public abstract void inputConclude(int c) ;

	/** Solicita entrada de usuario.
	 * el sub-sistema inputRequest() es iniciado por una solicitud de entrada 
	 * emitidada por el usuario y se desplaza a travez de todos los componentes
	 * involucrados hasta que alguno procese la solicitud. La instancia que 
	 * solicite los datos (el ultimo objeto en la cadena) debe informar cuando
	 * se finalize la entrada de datos llamando a <code>inputConclude()</code>.
	 * 
	 * @param p Punto dentro de la seccion desde donde se solicito la petición.
	 */
	public abstract void inputRequest(Point p);
	
	/** Retorna <code>true</code> si esta seccion puede permitir la 
	 * entrada de datos. 
	 * Este metodo es invocado por <code>Form</code> para determinar si 
	 * este campo es elegible para <code>InputRequest()</code>. Si lo es, durante
	 * la edicion de la forma, esta seccion reacciona cuando el puntero del raton
	 * se posiciona sobre esta seccion. Si se selecciona , se dispara el metodo 
	 * <code>inputRequets(Point)</code>.
	 * 
	 * este metodo es el encargado de implenentar la funcionalidad para los 
	 * atributos inoutIf o inputIf (en ese orden). Si la expresion retorna 
	 * <code>true</code>, la seccion es una seccion de entrada de datos. Si 
	 * el atributo no se encutra dentro de la definicion de la seccion, se 
	 * asume que es una seccion de salida. 
	 * 
	 * @return true si esta seccion es de entrada
	 */
	public boolean isAnInputSection() {
        String bf = getAttribute("inoutIf");
		bf = (bf == null) ? getAttribute("inputIf") : bf;
		return inoutIf(bf);
	}

	/** invocado por <code>Form</code> para indicar que el puntero del raton se
	 * encuentra sobre el componentes de salida
	 *
	 */
	public void mouseEntered() {
		setVisible(false);
		setOpaque(true);
		setBackground((Color) form.getAttribute("selected.background"));
		setVisible(true);
	}
	/** invocado por <code>Form</code> para indicar que el puntero del raton ya
	 * no se encuetra sobre el componentes de salida
	 *
	 */
	public void mouseExited() {
		setVisible(false);
		setOpaque(false);
		setVisible(true);
	}

	
	/** recalcula el valor interno de esta seccion. Sub-clases deben implementar 
	 * este metodo si el valor retornado por <code>isAnInputSection()</code> retorna
	 * false debido a que <code>Form</code> lo invoca durante las operaciones de recalculo
	 * El resultado de los calculoes realizados dentro de este metodo no deben ser
	 * colocados directamente en el componente de salida  ya que <code>Form</code>
	 * los solicitara mediante el metodo <code>getValue()</code>
	 * 
	 */
	public abstract void recalc() ;
	
	/** Establece atributos para esta seccion. 
	 *  
	 * @param f forma a la que pertenece esta seccion. Este metodo es llamado durante la
	 * creacion de las secciones. Subclases no debe alterar este metodo
	 * el atributo <code>xPos</code> es evaluado para determinar si esta seccion
	 * es una variable. Si lo es, se coloca en un area no visible de la forma 
	 * 
	 * @param att Atributos
	 */
	public void setAttributes(Form f, Hashtable att) {
		this.form = f;
		this.attributes = att;
		String snam = (String) attributes.get("id");
		Rectangle r = form.s_surface.getSectionSurface(snam);
		setBounds(r);
		createOutputComponent();
		mouseExited();

		// debugg
		if (form.getAttribute("gv.debugg") != null && 
			form.getAttribute("gv.debugg").equals("yes")) {
				System.out.println("attributes of " + snam + " set");
			}
		
	}

	/** este metodo es llamado durante <code>registreSections()</code> para que
	 * la seccion establesca sus valores iniciales. Las secciones son responsables
	 * obtener los datos ya sea del arcumento de entrada o de otra fuente. Se 
	 * puede usar este metodo tambien para completar la construcion de objetos 
	 * dentro de la seccion
	 * 
	 * @param tpr - contribuyente al que se esta paremtrizando esta forma 
	 */
	public abstract void setInitialValues(TaxPayerRecord tpr);
	
	/** Establece los valores internos para esta seccion. Este metodo es invocado
	 * durante el registro de las secciones con la intencion de establecer los
	 * valores que fueron almacenados durante una operacion de salvar forma. 
	 * Los vaores que pasados como argumento son los que durante el guardado, 
	 * retorno <code>getSectionInternalValue()</code>
	 * 
	 * @param v - Vector con los valores internos de la seccion.
	 */
	public void setSectionInternalValue(Object v) {
		setSectionValue(v);
	}
	
	/** Establece el valor interno para esta seccion. 
	 * 
	 * @param obj nuevo valor
	 */
	public void setSectionValue(Object obj) {
		this.sectionValue = obj;
	}
	
	/** metodo que permite a las secciones registrar errores. el metodo no
	 * es necesario (por ahora)
	 * 
	 * @param ext - mensaje adicional. este es concatenado con el identificador
	 * de seccion.
	 * @param e - exepcion
	 */
	protected void log(String ext, Throwable e) {
		Logger.getLogger("").logp(Level.WARNING, null, null, 
			e.getMessage() + " (" + ext + " - " + getAttribute("id") + ")", e);
	}
	

}