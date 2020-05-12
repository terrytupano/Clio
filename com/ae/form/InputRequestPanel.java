
package com.ae.form;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.*;

import com.ae.form.sections.*;

/** panel que actua como dialogo de entrada de parametros de una seccion dentro de
 * una forma. Instancias de esta clase son creadas por cada seccion y no deben ser
 * compartidas. 
 * 
 */
public class InputRequestPanel extends JPanel implements ActionListener {

	private InputRequestPanelBorder border;
	private JComponent firstC;
	private JButton jb_article, jb_clear, jb_acept, jb_cancel;
	private AbstractSection section;	//instancia a la que se reporta
	private JLabel titleLabel;	// titulo
	private Component focus = null;
	private Form form;

	public static int ACEPT = 0;
	public static int CANCEL = 1;
	public static int CLEAR_DATE = 3;

	/** nueva instancia. 
	 * 
	 * @param f forma 
	 */
	public InputRequestPanel(Form f) {
		super();
		setLayout(new BorderLayout());
		setOpaque(false);

		this.titleLabel = FormUtilities.getTitleLabel(f);
		this.section = null;
		this.focus = null;
		this.form = f;
		Color panelColor = (Color) form.getAttribute("panel.background");
		Color borderColor = (Color) form.getAttribute("title.background");

		JToolBar toolBar = new JToolBar();
		toolBar.setUI(new MetalToolBarUI());
		toolBar.setFloatable(false);
		toolBar.add(Box.createHorizontalGlue());
		toolBar.setBackground(panelColor);
		this.jb_clear = FormUtilities.getJButton(form, "b01", "Clear");
		this.jb_article = FormUtilities.getJButton(form, "a21", "Article");
		this.jb_acept = FormUtilities.getJButton(form, "a05", "Acept");
		this.jb_cancel = FormUtilities.getJButton(form, "c05", "Cancel");

		JButton[] jb = new JButton[] {
			jb_article, jb_clear, jb_acept, jb_cancel
		};
		
		for (int j = 0; j < jb.length; j++) {
			toolBar.addSeparator();
			toolBar.add(jb[j]);
			jb[j].addActionListener(this);
		}

		this.border = new InputRequestPanelBorder(form);
		setBorder(border);
		add(titleLabel, BorderLayout.NORTH);
		add(toolBar, BorderLayout.SOUTH);
		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		JButton jb = (JButton) ae.getSource();
		setVisible(false);
		// aceptar
		if (jb == jb_acept) {
			section.inputConclude(ACEPT);
		}
		// cancelar
		if (jb == jb_cancel) {
			section.inputConclude(CANCEL);
		}
		// limpiar (solo para clases DateInput)
		if (jb == jb_clear) {
			section.inputConclude(CLEAR_DATE);
		}
		form.remove(this);
		setVisible(true);
		
   }
	/** adiciona componente a este InputRequestPanel. estos son creados por las
	 * secciones y son añadidas como conponentes de entrada de parametros. 
	 * 
	 * @param jc  - el componente a adicionar
	 * @param foc - Componente con el foco de entrada
	 */
	public void add(JComponent jc, JComponent foc) {
		jc.setBorder(new EmptyBorder(FormUtilities.HORIZONTAL_GAP, 
			FormUtilities.VERTICAL_GAP, 
			FormUtilities.HORIZONTAL_GAP, FormUtilities.VERTICAL_GAP));
		add(jc, BorderLayout.CENTER);
		Dimension d = getPreferredSize();		
		setBounds(10,10, d.width, d.height);
		this.focus = foc;

	}
	/** solicitud de entrada. Este metodo presenta el dialogo de entrada con los
	 * componentes creados por la seccion solicitante. 
	 * 
	 * @param f Seccion al que se notificara mediante <code>inputConclude(boolean)</code> 
	 * que la entrada de datos a finalizado.
	 */
	public void inputRequest(AbstractSection f, Point p) {
		this.section = f;
		Rectangle r = getBounds();
		Rectangle rf = form.getBounds();
		if (p.x > (rf.width / 2)) {
			r.x = p.x - r.width;
			border.setArrow(SwingConstants.WEST);
		} else {
			r.x = p.x;
			border.setArrow(SwingConstants.EAST);
		}
		r.y = p.y - (r.height / 2) - 9;
		setBounds(r);
		form.add(this, JLayeredPane.MODAL_LAYER);

		focus.requestFocus();
	}

	/** recibe los atributos de la seccion como argumento de entrada y de este,
	 * se extraen los que pertenecen a <code>InputRequestPanel</code> para 
	 * configuran esta instancia. Si esta seccion es de clase "DateInput"
	 * se activa el boton para limpiar la fecha
	 *  
	 * @param atts - Atributos de la seccion
	 */
	public void setAttributes(Hashtable atts) {
		titleLabel.setText((String) atts.get("title"));
		String cls = (String) atts.get("class");
		jb_clear.setVisible(cls.equals("DateInput"));

		String art = (String) atts.get("article");
		jb_article.setVisible(art != null);
	}
}