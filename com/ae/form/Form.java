/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import org.xml.sax.*;
import org.jdom.output.*;

import com.ae.core.*;
import com.ae.evaluate.*;
import com.ae.form.sections.*;
import com.ae.resource.*;

/** Esta clase es la interfaz principal entre el usuario u una forma dentro
 * del sistema. Instancias de esta clase presentan una imagen digital del papel
 * que presenta y permite interacturar con las distintas secciones que la 
 * componen interactuando con ellas para en conjunto llenar los datos que la conforman
 * 
 * - es el interlocutor y coordinador de todas las secciones manteneiendo un estado
 * coerente entre ellas 
 * 
 * -proporciona metodos auxiliares a las secciones
 * - representa ambos lados de la hoja; es decir, la forma completa.
 * - la disposicion de las capas son:
 * 
 * bottom [REVERSE] 
 * background [REVERSE]
 * sections [REVERSE]
 * image [REVERSE]
 * 
 * bottom [OVERSE]
 * background [OVERSE]
 * sections [OVERSE]
 * image [OVERSE]
 * 
 * con esta disposicion, dar vuelta a la pagina solo se remite a hacer 
 * visible/invisible la capa OVERSE
 *
 */
public class Form extends JLayeredPane implements MouseListener, Printable, Runnable  {

	private static Integer[] BACKGROUND_LAYER = {
		new Integer(10), new Integer(30)
	};
	private static Integer[] BOTTOM_LAYER = {
		new Integer(5), new Integer(25)
	};
	private static Integer[] IMAGE_LAYER = {
		new Integer(20), new Integer(40)
	};

	public static int OVERSE = 1;
	public static int REVERSE = 0;
	private static Integer[] SECTION_LAYER = {
		new Integer(15), new Integer(35)
	};

	private Hashtable attributes[];
	
	public Evaluator evaluator;
	private String form_name;
	private FormEditor editor;
	private int page;
	private String[] pages;
	private boolean reverseComponents;
	public SectionSurface s_surface;
	private Hashtable savVal, globalValues;
	private AbstractSection sectionInInput;
	private TaxPayerRecord tp_record;
	private boolean inLoad;
	private RunnableMonitor runMon;
	private boolean status;
	private int[] prin_cmp;

	/** nueva forma fn asignada al contribuyente rcd.
	 * 
	 * @param fn - Nombre de la forma (id)
	 * @param rcd - contribuyente 
	 * @param frp - Editor dentro del cual se colocara esta forma
	 * @param sv - Valores internos de las secciones registradas. 
	 * Si !=null, durante el registro de las secciones, se estableceran los valores
	 * alamcenados aqui como valores iniciales. 
	 * @param s - stado activo o incativo. 
	 * 
	 */
	public Form(String fn, TaxPayerRecord rcd, FormEditor frp, Hashtable sv, 
		boolean s) {
		super();
		setOpaque(true);
		this.page = OVERSE;
		this.pages = new String[]{"reverse", "overse"};
		this.reverseComponents = false;
		this.globalValues = new Hashtable();
		this.attributes = new Hashtable[2];
		this.form_name = fn;
		this.tp_record = rcd;
		this.savVal = sv;
		this.sectionInInput = null;
		this.editor = frp;
		this.s_surface = new SectionSurface(form_name);
		this.evaluator = new Evaluator();
		this.inLoad = false;
		this.status = s;
	}
	
	/** retorna estado de la forma que actualmente esta seendo editada.
	 * 
	 * @return - estado: activo o inactivo
	 */
	public boolean getStatus() {
		return status;
	}
	
	/** personalizado para evaluar expresiones booleanas
	 * 
	 * @param be - expresion 
	 * @return - true o false
	 */
	private boolean evaluateBooleanExpression(String be) {
		boolean bol = false;
		try {
			if (be != null) {
				evaluator.parse(be);
				bol = evaluator.evaluate(be).equals("1.0") ? true : false;
			}
		} catch (Exception ex) {
			Logger.getLogger("").logp(Level.WARNING, null, null, 
				ex.getMessage() + "(" + be + ")", ex);
		}
		return bol;
	}
	
	/* retorna todas las secciones registrada en ambos lados de la forma. 
	 * <code>gv.firstFace</code> determina por cual de las paginas (ov o rev) 
	 * se comienzan se comienzan a extraer las secciones. Esto es devido a que
	 * una de las paginas (generalmente rev) describe los detalles de las transaciones 
	 * mientras que la otra, contiene secciones son resumenes de estas. 
	 * 
	 */
	private Component[] getAllSections() {
		int fp = OVERSE;
		int sp = REVERSE;
		if (globalValues.get("gv.firstFace").equals("reverse")) {
			fp = REVERSE;
			sp = OVERSE;
		}
		Component[] c1 = getComponentsInLayer(SECTION_LAYER[fp].intValue());
		Component[] c2 = getComponentsInLayer(SECTION_LAYER[sp].intValue());
		Component[] c3 = new Component[c1.length + c2.length];
		System.arraycopy(c1, 0, c3, 0, c1.length);
		System.arraycopy(c2, 0, c3, c1.length, c2.length);
		return c3;
	}
	
	 /** Retorna objeto que representa el valor del atributo aName. Este Objeto 
	 * puede ser de diferentes clases; por tanto, es responsabilidad del 
	 * que solicita los datos modelar al objeto de clase correspondite con el 
	 * atributo solicitado. Este metodo:
	 * intenta localizar el valor dentro de los valores globales. Si no esta alli
	 * retorna lo que encuentre dentro de los atributos de la pagina
	 * que esta activa en el momento del llamado.
	 * 
	 * @param aName Nombre del atributo
	 * @return Objeto que represeta el valor del atributo.
	 */
	public Object getAttribute(String aName) {
		Object val = globalValues.get(aName);
		if (val == null) {
			val = attributes[page].get(aName);
		}
		return val;
	}

	/** retorna el identificador de esta forma. El identificador el es codigo
	 * por el cual una forma fisica, descrita en el archvio de configuracion
	 * es conocida dentro de la aplicacion.
	 * 
	 * @return id de forma
	 */	
	public String getFormID() {
		return form_name;
	}
	/** Retorna las secciones que pertenecen al grupo pasado como argumento.
	 * 
	 * @param grp - grupo
	 * @return valores de las secciones perteneciones al grupo
	 */
	public BooleanInput[] getGroupedValue(String grp) {
		Vector vec = new Vector();
		vec = new Vector();
		Component[] c = getComponentsInLayer(SECTION_LAYER[page].intValue());
		for (int k = 0; (k < c.length && grp != null); k++) {
			AbstractSection bi = (AbstractSection) c[k];
			String sgp = bi.getAttribute("group"); 
			if (sgp != null && sgp.equals(grp)) {
				vec.add(bi);
			}
		}
		return (BooleanInput[]) vec.toArray(new BooleanInput[vec.size()]);
	}
	
	/** Extrae los valores internos de todas las secciones. Este metodo debe llamarse
	 * antes de las operaciones de guardar para que el objeto retornado contenga
	 * las ultimas modificaciones efectuadas dentro de la forma. 
	 * 
	 * @return <code>Hashtable</code> con los valores de las secciones. la
	 * clave es el nombre de la seccion y el contenido, el valor  
	 * 
	 */
	public Hashtable getSectionsIntervalValue() {
		Hashtable h = new Hashtable();
		Component[] c = getAllSections();
		for (int k = 0; k < c.length; k++) {
			AbstractSection as = (AbstractSection) c[k];
//			if (!(as instanceof EvaluatedSection)) {
				h.put(as.getAttribute("id"), as.getSectionInternalValue());
//			}
		}
		return h;
	}
	
	/** Retorna Valor de la secciones cuyo identificador fue registrado con el 
	 * nombre vv. El identificador vv esta determinado por el valor del
	 * atrubuto id del archivo de configuracion; el cual se uso para identificar 
	 * dicha seccion dentro de la forma
	 * Este medoto es generalmente invocado por las secciones que ejecutan 
	 * formulas de calculo y necesitan el valor de campos relacionados dentro
	 * de Form. Debido a que cada clase tiene un getSectionValue() unico, es 
	 * responsabilidad del la seccion que invoca este metodo modelar la clase original
	 * a la que corresponde el valor retornado por este metodo. 
	 * 
	 * @param vv nombre registrado
	 * @param cv instancia 
	 * @return valor de la seccion registrado con nombre vv
	 */
	public Object getVariableValue(String vv) {
		Component[] cmps = getAllSections();
		Object val = null;
		
		//campo de base de datos
		val = tp_record.getFieldValue(vv);
		
		for (int k = 0; (k < cmps.length) && (val == null); k++) {
			AbstractSection f = (AbstractSection) cmps[k];
			if (f.getAttribute("id").equals(vv)) {
				val = f.getSectionValue();
				break;
			}
		}
		return val;
	}
	
	/** Retorna la cara de la pagina que esta visible en este momento
	 * 
	 * @return <code>OVERSE o REVERSE</code>
	 */
	public int getVisibleFace() {
		return page;
	}
	/** Recibe notifiacion de finalizacion de entrada de datos. Este metodo 
	 * inicia el recalculo general de toda la forma. Para ello, obtiene todas
	 * las secciones registradas (ambas caras) e invoca <code>AbstractSection.recalc()</code>
	 * luego del recalculo, se verfican los esenarios. 
	 * 
	 */
	public void inputConclude() {
		AbstractSection as = null;
		Component[] c = getAllSections();
		
		// actualiza evaluador con datos del componente que llama este metodo
		if (sectionInInput != null) {
			evaluator.putVariable(sectionInInput.getAttribute("id"), 
				FormUtilities.stringFormat(sectionInInput.getSectionValue(), false));
			sectionInInput = null;
		}
		
		// ejecucion de recalculo
		for (int k = 0; k < c.length; k++) {
			as = ((AbstractSection) c[k]);
			String id = as.getAttribute("id");
			/*
			if (id.equals("tributaryUnit")) {
				System.out.println(as.getAttribute("id")); 
			}
			*/
			as.recalc();
			evaluator.putVariable(id, 
				FormUtilities.stringFormat(as.getSectionValue(), false));
			as.formatOutput();
		}
		stages();
		as = null;
	}
	/** Retorna <code>true</code> si esta forma se imprime o tiene secciones
	 * en la parte posterior de la hoja.
	 * 
	 * @return =true si se imprime por detras
	 */
	public boolean isReverseAvailable() {
		return reverseComponents;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent me) {
		if (status) {
			AbstractSection f = (AbstractSection) me.getComponent();
			if (sectionInInput == null && f.isAnInputSection()) {
				Point p = me.getPoint();
				Rectangle r = f.getBounds();
				p.x += r.x;
				p.y += r.y;
				sectionInInput = f;
				f.inputRequest(p);
			}
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent me) {
		if (status == false) {
			return;
		}
		AbstractSection afs = (AbstractSection) me.getComponent();
		if (sectionInInput == null && afs.isAnInputSection()) {
			afs.mouseEntered();
		}
		// mensajes:
		// este atribute puede contener un identificador de mensajes o una
		// expresion de tipo "exp ; msg ; msg". la cual se comporta igual que 
		// exp ? msg : msg
		String mes = afs.getAttribute("mouseEntered");
		if (mes != null) {
			String[] e_mm = mes.split(";");
			if (e_mm.length == 3) {
				mes = evaluateBooleanExpression(e_mm[0]) ?
					e_mm[1].trim() : e_mm[2].trim();
			}
			Object o = PrevalentSystemManager.exceptions.get(mes);
			if (o != null) {
				editor.showExceptionMessage((AplicationException) o);
			}
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent me) {
		((AbstractSection) me.getComponent()).mouseExited();
		stages();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent me) {}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent me) {}
		
	/*
	 *  (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */	
	public int print(Graphics gra, PageFormat pf, int pag) {
		Graphics2D g2d = (Graphics2D) gra;
		g2d.scale(0.75, 0.75);
			
		// aproximacion de la distancia que hay entre el tope del cabezal y la parte
		// superior de la matriz	
		int offset = 38;
		
		// combierte lognitud de pagina expresada en 
		// 1/72 de pulgada (formato de PageFormat) a pixel 
		double phei = pf.getImageableHeight() / 72.0 * 96.0;
		double pwid = pf.getImageableWidth() / 72.0 * 96.0;

		// rotacion
		if (getAttribute("gv.pre-dive").equals("bottom")) {
			g2d.rotate(Math.PI);
			g2d.translate(-getWidth(), -getHeight());
			offset = -offset;
		}
		/*
		for (int c = 0; c < 1000; c = c + 10) {
			String p = String.valueOf(c);
			g2d.drawBytes(p.getBytes(), 0, p.length(), getWidth() - c, getHeight() - c);
		}
		return pag == 0 ? Printable.PAGE_EXISTS : Printable.NO_SUCH_PAGE;
		*/
		
		// determina que componentes iran en cada pagina. la variable pos[x]
		// indicara la pagina del componente x. pos[x]== -1 cuando la parte 
		// superior se encuentra en una pagina y la inferior en otra. este eviata 
		//que solo se imprima la mitad de un componente cuando este se encuentr
		// entre 2 paginas 
		Component[] cmp = getComponentsInLayer(SECTION_LAYER[page].intValue());
		int[] pos = new int[cmp.length];
		boolean l = false;
		for (int v = 0; v < cmp.length; v++) {
			AbstractSection asec = (AbstractSection) cmp[v];
			Rectangle r2 = asec.getBounds();
			r2.y -= offset;
			if (offset < 0) {
				pos[v] = 1;
				if ((getHeight() - r2.y) < phei && 
					(getHeight() - (r2.y + r2.height)) < phei) {
					pos[v] = 0;
				} else {
					if ((getHeight() - r2.y) >= phei && 
						(getHeight() - (r2.y + r2.height)) < phei) {
						pos[v] = -1;
					}
				}
			} else {
				pos[v] = r2.y < phei ? 0 : 1;
			}
		}

		int rv = Printable.NO_SUCH_PAGE;
		for (int c = 0; c < cmp.length; c++) {
			if ((pos[c] == pag || pos[c] < 0) && pag < 2) {
				AbstractSection asec = (AbstractSection) cmp[c];
				Rectangle r2 = asec.getBounds();
				r2.y -= offset;
				double r2y1 = (pag == 0) ? r2.y : (phei * pag) + r2.y;
				g2d.translate(r2.x, r2y1);
				asec.setOpaque(false);				
				asec.print(g2d);
				g2d.translate(-r2.x, -r2y1);
				rv = Printable.PAGE_EXISTS;
			}
		}
		return rv;		
	}
	
	/** Registra atributos para esta forma. Este metodo es invocado por <code>FormReaderHander</code>
	 * para retistrar todos los atributos de la forma encontrados en el archvi .xml
	 * este metodo:
	 * o adiciona <code>overse.image</code>
	 * o determina las dimensiondes para esta forma (solo en overse)
	 * o si existe logo de fondo <code>name != "" en overse.background</code> la añade
	 * o adiciona un lienzo con el color descrito en <code>overse.panel</code>
	 * 
	 * se determina si esta planilla se imprime por ambos lados. si es asi, 
	 * repite los pasos anteriores ahora con reverse.
	 * 
	 * @param gv - valores globales para esta forma.
	 * @param o_attr - atributos de la parte anterior de la hoja
	 * @param r_attr - atributos de la parte posterior
	 */ 
	public void registreAttributes(Hashtable gv, Hashtable o_attr, Hashtable r_attr) {
		editor.getProgressPanel().increment(DIMain.bundle.getString("r01"));
		globalValues = gv;
		attributes[OVERSE] = o_attr;
		attributes[REVERSE] = r_attr;
		
		//--------------------
		//anverso
		//--------------------
		// forma
		JLabel di = (JLabel) attributes[OVERSE].get("form.image"); 
		
		add(di, IMAGE_LAYER[OVERSE]);
		//---------------------------
		setSize(di.getSize());			
		setMinimumSize(di.getSize());	
		setPreferredSize(di.getSize());	
		setMaximumSize(di.getSize());	
		//---------------------------

		// transfondo
		JLabel b_lab = new JLabel();
		b_lab.setOpaque(true);
		b_lab.setBounds(0, 0, getSize().width, getSize().height);
		b_lab.setBackground((Color) attributes[OVERSE].get("panel.background"));
		add(b_lab, BOTTOM_LAYER[OVERSE]);

		// fondo
		b_lab = (JLabel) attributes[OVERSE].get("form.background");
		add(b_lab, BACKGROUND_LAYER[OVERSE]);
		
		//--------------------
		// reverso
		//--------------------
		// forma
		di = (JLabel) attributes[REVERSE].get("form.image");
		this.reverseComponents = false;
		if (di != null) {
			this.reverseComponents = true;
			add(di, IMAGE_LAYER[REVERSE]);

			// transfondo
			b_lab = new JLabel();
			b_lab.setOpaque(true);
			b_lab.setBounds(0, 0, getSize().width, getSize().height);
			b_lab.setBackground((Color) attributes[REVERSE].get("panel.background"));
			add(b_lab, BOTTOM_LAYER[REVERSE]);

			// fondo
			b_lab = (JLabel) attributes[REVERSE].get("form.background");
			if (b_lab != null) {
				add(b_lab, BACKGROUND_LAYER[REVERSE]);
			}
		}
	}
	
	/** invocado por <code>FormReaderHander</code> para registrar todas las 
	 * secciones encontradas dentro del archivo descriptivo de la forma. las
	 * secciones se adicionan a la pagina que esta visible en el momento del 
	 * registro.
	 * 
	 * nota: no modificar secuenca dado que esta secuencia debe ser tomada
	 * en cuenta durante la creacion o modificaion de la logica de las secciones
	 * 
	 * @param f Seccion
	 * @param attrs atributos
	 */
	public void registreSection(AbstractSection f, Hashtable attrs){
		f.setAttributes(this, attrs);
		
		// la seccion ya esta instacionada, ahora se establecen sus valores
		// iniciales
		f.setInitialValues(tp_record);

		editor.getProgressPanel().increment(DIMain.bundle.getString("r02") + attrs.get("id"));
		
		// ya que una seccion de salida puede contener mensajes
		f.addMouseListener(this);
		
		// contiene un panel de entrada
		InputRequestPanel irp = f.createInputRequestPanel();
		if (irp != null) {
			irp.setAttributes(attrs);
		}

		// si es una forma ya guardada.		
		if ((savVal != null) && !(f instanceof EvaluatedSection)){
			String i = (String) attrs.get("id");
			f.setSectionInternalValue(savVal.get(i));
		}

		add(f, SECTION_LAYER[page]);
	}
	
	/** Evaluacion de escenarios. La evaluacion comienza por el primer escenario
	 * y continia con el siguiente solo si la evaluacion resulto false; es decir
	 * que el escenario no se cumplio y no se presento ninguna excepcion.
	 *
	 */
	private void stages() {
		
		// solo se ejecuta cuando no se esta en carga. ver run();
		if (inLoad) {
			return;
		}
		editor.showMessage(null);
		for (int s = 0; ;s++) {
			Attributes attr = (Attributes) globalValues.get("stage" + s);
			if (attr == null) {
				break;
			} else {
				if (attr.getValue("face").equals(pages[page]) && 
					evaluateBooleanExpression(attr.getValue("expression"))) {
					editor.showMessage(attr.getValue("message"));
					break;
				}
			}
		}
	}
	/** indica a esta forma que de vuelta a la pagina. La forma no dara vuelta
	 * a la pagina si en la campa REVERSE no se encuentra ninguna seccion registrada.
	 * el procedimiento de dar vuelta es hacer visible/invisible la capa OVERSE
	 * para que la REVERSE quede tapada/destapada.
	 * 
	 * @param toP pagina destino. <code>OVERSE o REVERSE</code>
	 */
	public void turnPage(int toP) {
		
		if (isReverseAvailable()) {
			setVisible(false);
			page = toP;
			boolean flag = page == OVERSE; 

			Component[] c = getComponentsInLayer(BOTTOM_LAYER[OVERSE].intValue());
			c[0].setVisible(flag);
			c = getComponentsInLayer(BACKGROUND_LAYER[OVERSE].intValue());
			c[0].setVisible(flag);
			c = getComponentsInLayer(IMAGE_LAYER[OVERSE].intValue());
			c[0].setVisible(flag);
			
			// secciones 
			c = getComponentsInLayer(SECTION_LAYER[OVERSE].intValue());
			for (int cc = 0; cc < c.length; cc++) {
				c[cc].setVisible(flag);
				c[cc].setEnabled(flag);
			}
			
			// para evitar que un click en un punto de overse dispare un 
			// inputRequest() de una seccion en reverse
			c = getComponentsInLayer(SECTION_LAYER[REVERSE].intValue());
			for (int cc = 0; cc < c.length; cc++) {
				c[cc].setVisible(!flag);
				c[cc].setEnabled(!flag);
			}
			setVisible(true);
			stages();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		inLoad = true;
		try {
			Thread.sleep(100);
		} catch (Exception e) {
			// nada 
		}
		runMon = new RunnableMonitor(editor);

		// inicializa evaluador con campos del contribuyente
		editor.getProgressPanel().increment(DIMain.bundle.getString("i12"));
		for (int k = 0; k < tp_record.getFieldsCount(); k++) {
			evaluator.putVariable(tp_record.getFieldID(k), 
				FormUtilities.stringFormat(tp_record.getFieldValue(k), false));
		}
		
		// nuevo: evaluador con constantes especiales
		String td = new AppDate().toString();
		evaluator.putVariable("DATE", td);
		evaluator.putVariable("YEAR", td.substring(0, 4));
		evaluator.putVariable("MONTH", td.substring(4, 6));
		evaluator.putVariable("DAY", td.substring(6, 8));
		
		// compilacion de archivo de formas
		if (runMon.stop) {
			return;
		}

		editor.getProgressPanel().increment(DIMain.bundle.getString("c13"));

		try {
			String qfn = ResourceUtilities.getPluginsPath() + form_name + ".xml";
			SAXOutputter sou = new SAXOutputter(new FormReaderHandler(
				this, form_name, editor.getProgressPanel(), runMon));
			sou.output(PrevalentSystemManager.getXMLDocument(qfn));
		} catch (Exception e) {
			String sf = e.getMessage() + "(" + form_name + ")";
			Logger.getLogger("").logp(Level.SEVERE, null, null, sf, e);
		}
		
		editor.getProgressPanel().increment(DIMain.bundle.getString("v03"));

		// secciones
		AbstractSection as = null;
		Component[] c = getAllSections();
		for (int k = 0; k < c.length && !runMon.stop; k++) {
			as = ((AbstractSection) c[k]);
			String id = as.getAttribute("id");
			evaluator.putVariable(id, 
				FormUtilities.stringFormat(getVariableValue(id), false));
		}

		// finalizacion
		if (runMon.stop) {
			return;
		}
		editor.getProgressPanel().increment(DIMain.bundle.getString("r03"));
		inputConclude();
		editor.complete();
		inLoad = false;
	}
}