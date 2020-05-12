/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.border.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.resource.*;

 /** RightPanel es el contenedor principal de la aplicacion. Este es usado para
 * contener y presentar todos los componentes de interfaz de usuario. 
 * RightPanel contiene un area de encabezado que indica al usuario una breve 
 * de lo que se esta haciendo es este momento. Dicho encabezado tambien contiene
 * una linea por la cual la aplicacion se comunica con el usuario indicando errores
 * y advertencias. 
 */
public class RightPanel extends JPanel {
	
	private JEditorPane titleDoc;
	private JLabel msgLabel;
	private JPanel titlePanel, msgPanel;
	private JToolBar toolBar;
	private JButton[] toolBarBtn, actionBarBtns;
	private AplicationException aplicationE;
	private javax.swing.Timer timer;
	private TimerAction timer_a;
	private ProgressPanel progressP;
	
	/** crea un nuevo RightPanel con docN como documento de encabezado
	 * 
	 * @param docN Nombre del documento encabezado. Si <code>null</code> no 
	 * se presenta la barra de titulo
	 */
	public RightPanel(String docN) {
		super(new BorderLayout());

		this.toolBar = null;
		this.toolBarBtn = null;
		this.actionBarBtns = null;
		this.aplicationE = null;
		this.timer_a = new TimerAction();
		this.timer = new javax.swing.Timer(125, timer_a);
		timer.setRepeats(true);

		this.titleDoc = new JEditorPane();
		titleDoc.setUI(new AppEditorPaneUI(titleDoc, this));
		Dimension dim = new Dimension(0, 68);
		titleDoc.setPreferredSize(dim);
		this.titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(Color.white);
		
		this.msgLabel = new JLabel(" ");
		Dimension dim1 = new Dimension(0, 18);
		msgLabel.setPreferredSize(dim1);
		msgLabel.setBorder(new CompoundBorder(
			new MatteBorder(1, 0, 0, 0, Color.GRAY), new EmptyBorder(1, 2, 1, 2)));
		this.msgPanel = new JPanel(new BorderLayout());
		msgPanel.setBackground(Color.white);
		this.progressP = null;
		
		setDocPanel(docN);

		add(titlePanel, BorderLayout.NORTH);
	}
	
	/** establece un documento para el panel informativo (parte superior
	 * de la ventana. Si el argumento de entrada es <code>null</code>
	 * el panel completo desaparece
	 * 
	 * @param dn - nombre del documento
	 */	
	public void setDocPanel(String dn) {
		if (dn != null) {
			try {
				titleDoc.setEditable(false);
				titleDoc.setPage(ResourceUtilities.getURL(dn));
			} catch(Exception e) {
				Logger.getLogger("").logp(Level.SEVERE, null, null, e.getMessage(), e);
			}
			msgPanel.add(msgLabel, BorderLayout.NORTH);
			msgPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.CENTER);
			titlePanel.add(titleDoc, BorderLayout.NORTH);
			titlePanel.add(msgPanel, BorderLayout.CENTER);
			// SOUTH reservado para barra de herramientas
			titleDoc.setVisible(true);
			msgPanel.setVisible(true);
		} else {
			titleDoc.setVisible(false);
			msgPanel.setVisible(false);
		}

	}
	
	/** construye una barra de herramientas con los componentes pasados como 
	 * argumentos de entrada. 
	 *
	 *  no va _________
	 * Si un <code>JButton</code> tiene una accion asociada
	 * y esta es una instancia de <code>DeleteRecordAction</code> se adiciona 
	 * separadores antes y despues por se una accion peligrosa
	 * 
	 * @param cmps
	 */
	public void setToolBar(Component[] cmps) {
		Vector v_btn = new Vector();
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
	
		toolBar.setRollover(true);
		for (int i = 0; i < cmps.length; i++) {
			cmps[i].setFocusable(false);
			if (cmps[i] instanceof JButton) {
				JButton jb = (JButton) cmps[i];
				v_btn.add(jb);
				/*
				if (jb.getAction() instanceof com.ae.datainput.DeleteRecordAction) {
					toolBar.addSeparator();
					toolBar.add(jb);
					toolBar.addSeparator();
				}*/
			}
			toolBar.add(cmps[i]);
		}
	//	GUIUtilities.setEmptyBorder(toolBar);
		titlePanel.add(toolBar, BorderLayout.SOUTH);
		toolBarBtn = (JButton[]) v_btn.toArray(new JButton[v_btn.size()]);
	}
	
	/** retorna arreglo con los botones que componen la barra de herraminetas
	 * 
	 * @return botones dentro de la barra de herramientas
	 */
	public JButton[] getToolBarButtons() {
		return toolBarBtn;
	}
	
	/** crea botones de accion para este panel
	 * a diferencia de setToolBar, este metodo crea los botones en la parte 
	 * inferior de la ventana. Este metodo es generalmente llamado por instancias 
	 * tipo RECORD para colocar los botones de accion en la parte inferior de panel.
	 * 
	 * @param btns - lista acciones a añadir
	 * @return pulsador que es instancia de <code>AceptAction</code>
	 */
	public JButton setActionBar(AbstractAction[] btns) {
		JButton a = null;
		Box b = Box.createHorizontalBox();
		b.add(Box.createHorizontalGlue());
		for (int j = 0; j < btns.length; j++) {
			b.add(Box.createHorizontalStrut(GUIUtilities.HORIZONTAL_GAP));
			JButton jb = new JButton(btns[j]);
			if (btns[j] instanceof NextAction) {
				jb.setHorizontalTextPosition(JLabel.LEFT);
			}
			if (btns[j] instanceof AceptAction || btns[j] instanceof OkAction ||
				btns[j] instanceof NextAction) {
				a = jb;
			}
			b.add(jb);
		}
		/*
		Box b1 = Box.createVerticalBox();
		b1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		b1.add(b);
		GUIUtilities.setEmptyBorder(b1);
		*/
		super.add(GUIUtilities.getBoxForButtons(b, true), BorderLayout.SOUTH);
		return a;
	}

	/** Localiza la exepcion identificada con el argumento de entrada y 
	 * ejecuta <code>showMessage(AplicationException)</code>
	 *  
	 * @param msgid - identificador de mensaje.
	 */
	public void showMessage(String msgid) {
		AplicationException ae = null;
		if (msgid != null) {
			ae = (AplicationException) PrevalentSystemManager.exceptions.get(msgid);
		}
		showExceptionMessage(ae);
	}
	
	/** presenta el texto de la excepcion den el area de mensajes. si el
	 * argumento de entrada es <code>null</code> se limpia esta area
	 * 
	 * @param appe - instancia de la exepcion
	 */
	public void showExceptionMessage(AplicationException appe) {
		this.aplicationE = appe;
		if (aplicationE != null) {
			msgLabel.setIcon(aplicationE.getExceptionIcon());
			msgLabel.setText(aplicationE.getMessage());
			msgLabel.setOpaque(true);
			msgLabel.setBackground(aplicationE.getExceptionColor());
			timer_a.initialize(aplicationE);
			timer.start();
		} else {
			timer.stop();
			msgLabel.setOpaque(false);
			msgLabel.setIcon(null);
			msgLabel.setText(" ");
		}
	}
	
	/** Retorna true si este se esta mostrando un mensaje de error. Un mesaje de
	 * error todos menos instancias de <code>InformationException</code>
	 * 
	 * @return true si se esta presentando un error
	 */
	public boolean isShowingError() {
		return !((aplicationE instanceof InformationException) || aplicationE == null);
	}

	/** Adicion el compoenente. Antes de adicionar, Coloca un borde con formata
	 * preestablecido.
	 * 
	 * @param comp - Compoenente a adicionar
	 */	
	public void add(JComponent comp) {
		GUIUtilities.setEmptyBorder(comp);
		addWithoutBorder(comp);
	}
	
	/** igual que <code>add(JComponent)</code> pero sin borde
	 * 
	 * @param comp - componente
	 */
	public void addWithoutBorder(JComponent comp) {
		if (comp != progressP && progressP != null) {
			super.remove(progressP);
			progressP = null;
		}
		if (toolBar != null) {
			toolBar.setEnabled((progressP == null));
		}
		super.add(comp, BorderLayout.CENTER);
	}
	
	/** instala y retorna una instancia de <code>ProgressPanel</code>
	 * 
	 * @param com - combo. Ver progressPanel
	 * @return instancia instalada
	 */
	public ProgressPanel installProgressBar(int com) {
		this.progressP = new ProgressPanel(com);
		this.add(progressP);
		return progressP;
	}

	/** retorna barra de progreso o null si esta no ha sido instalada
	 * 
	 */	
	public ProgressPanel getProgressPanel() {
		return progressP;
	}
	
	/** solo para procesar temporizador. esta clase controla el tiempo de 
	 * presentacion de la exepcion. Si el tiempo ya ha sido alcanzado
	 * se ejecuta <code>RightPanel.showMessage(null)</code>
	 * 
	 */
	public class TimerAction implements ActionListener {
		
		private Color[] colors;
		private int offset;
		private long mark;
		private int clr_c;
		
		public TimerAction() {
			this.clr_c = -1;
			this.offset = -1;
			this.mark = 0;
		}
		
		/** inicializa valores para iniciar entrega de colores.
		 * 
		 * @param ae
		 */
		public void initialize(AplicationException ae) {
			Color bas = ae.getExceptionColor();
			colors = new Color[6];
			colors[0] = bas;
			colors[1] = GUIUtilities.brighter(bas);
			colors[2] = GUIUtilities.brighter(colors[1]);
			colors[3] = GUIUtilities.brighter(colors[2]);
			colors[4] = GUIUtilities.brighter(colors[1]);
			colors[5] = GUIUtilities.brighter(bas);
			this.clr_c = -1;
			this.mark = new Date().getTime();
			this.offset = ae.getMiliSeconds();
		}
		
		/*
		 *  (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */		
		public void actionPerformed(ActionEvent ae) {
			if (mark + offset > ae.getWhen()) {
				clr_c = (clr_c == colors.length - 1) ? 0 : ++clr_c;
				msgLabel.setBackground(colors[clr_c]);
				msgLabel.repaint();
			} else {
				showMessage(null);
			}
		}
	}
}