/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.action;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.border.*;

import org.jdom.*;

import com.ae.form.*;
import com.ae.gui.*;
import com.ae.resource.*;

/** Presenta un dialogo indicando la forma correcta de alinear la planilla 
 * en la impresora. Una vez alineada, imprime.  
 */
public class PrintFormAction extends AppAbstractAction implements ConfirmationAction {
	
	private FormEditor form_e;
	private JDialog dialog;
	private PrinterJob pjob; 
	private PageFormat pagef;
	
	public PrintFormAction(FormEditor fe) {
		super("i05", "Print", AppAbstractAction.NO_SCOPE, "tt16");
		this.form_e = fe;
		this.dialog = null;
		
		this.pjob = PrinterJob.getPrinterJob();
		Form fo = form_e.getForm();
		this.pagef = pjob.defaultPage();
		Paper pa = pagef.getPaper();
		
		// carta 8.5 * 11
//		pa.setImageableArea(0.0, 0.0, 8.5 * 72, 11 * 72);
		pa.setImageableArea(0.0, 0.0, pa.getWidth(), pa.getHeight());

		pagef.setPaper(pa);
		pjob.setPrintable(fo, pagef);
		pjob.setJobName(form_e.getFormName());
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		// dialogo
		Form form = form_e.getForm();
		String fnam = form_e.getFormName();
		String face = "overse.image";
		if (form.getVisibleFace() == Form.REVERSE) {
			face = "reverse.image";
		}
		Hashtable ht = PrevalentSystemManager.getFormElements(fnam);
		Element el = (Element) ht.get(face);
		Element e2 = (Element) ht.get("gv.pre-dive");
		Color bc = (Color) form.getAttribute("panel.background");
		ImageIcon ii = 
			ResourceUtilities.getFormImage(fnam, el.getAttributeValue("name"));

		// imagen		
		AlingImage ai = new AlingImage(ii, e2.getAttributeValue("value"), bc); 
		RightPanel rp = new RightPanel("PrintForm");
		rp.add(ai);
		
		AceptAction aa = new AceptAction(this);
		rp.setActionBar(new AppAbstractAction[] {
			new PrintDialogAction(pjob), aa, new CancelAction(this)});
		this.dialog = getDialog(rp, new JButton(aa), "i06");
		dialog.show();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.ae.core.ConfirmationAction#actionPerformed2(javax.swing.AbstractAction)
	 */
	public void actionPerformed2(AbstractAction aa) {
		if (aa instanceof AceptAction) {
			try {
				pjob.print();
			} catch (Exception e) {
				Logger.getLogger("").logp(Level.WARNING, null, null, e.getMessage(), e);
			}
		}
		dialog.dispose();
	}
	
	/** encargada de determinar la porcion de formulario que se debe mostrar 
	 * segun la direccion de impresion. esta direccion es determinada por el
	 * valor globar <code>gv.pre-dive</code>
	 * 
	 */
	private class AlingImage extends JPanel {
		
		private Image image;
		private Dimension dimension;
		private String position;
		private Color bgColor;

		/** nueva imagen alineada.
		 * 
		 * @param ii - imagen
		 * @param dir - direccion
		 * @param cb - Color para el fondo
		 */		
		public AlingImage(ImageIcon ii, String dir, Color cb) {
			super(new BorderLayout());
			this.image = ii.getImage();
			this.dimension = new Dimension(400, 200);
			this.position = dir;
			this.bgColor = cb;
			setMinimumSize(dimension);
			setPreferredSize(dimension);
			setMaximumSize(dimension);
	
			// altera paintComponent(Graphics) para mostrar imagen
			JPanel jp = new JPanel() {
				public void paintComponent(Graphics g) {
					Graphics2D g2d = (Graphics2D) g;
					
					if (position.equals("bottom")) {
						g2d.rotate(Math.PI);
						g2d.drawImage(
							image, 
							-image.getWidth(this), 
							-image.getHeight(this), 
							bgColor, 
							this
						);
						g2d.rotate(Math.PI);
					} else {
						g2d.drawImage(image, 0, 0, bgColor, this);
					}
				}
			};
			jp.setBorder(new MatteBorder(0,1,0,0, Color.GRAY));
			
			// pre-picado & flecha
			JPanel npanel = new JPanel(new BorderLayout());
			JLabel jlpd = new JLabel(ResourceUtilities.getIcon("pre-dive"));
			jlpd.setOpaque(true);
			jlpd.setBackground(bgColor);
			jlpd.setBorder(new MatteBorder(1,1,0,0, Color.GRAY));
			npanel.add(jlpd, BorderLayout.CENTER);
			npanel.add(new JLabel(ResourceUtilities.getIcon("NorthWestArrow")), BorderLayout.WEST);
			
			add(npanel, BorderLayout.NORTH);
			add(Box.createHorizontalStrut(16), BorderLayout.WEST);
			add(jp, BorderLayout.CENTER);
			
		}
	}		
}