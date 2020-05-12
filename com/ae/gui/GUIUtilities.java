/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.*;

import com.ae.action.*;
import com.ae.core.*;
import com.ae.resource.*;

/** Proporciona metodos para la creacion y alineacion de componentes dentro de
 * las ventanas de entrada de datos
 * 
 */
public class GUIUtilities {
//	public static int GAP = 4;
	
	public static int HORIZONTAL_GAP = 4;
	public static int VERTICAL_GAP = 4;
	
	/*
	 * @parm lab - String para JLabel
	 * @parm jcomp - componente que se coloca al lado de la etiqueta
	 * @parm req - =true es campo es requerido, =false no
	 * @parm ena - valor para jcom.setEnabled(ena);
	 * @parm glue - =true coloca Box.createHorizontalGlue() entre la eteiquta y el
	 * componente, =false, solo coloca Box.createHorizontalStrut(HORIZONTAL_GAP)
	 */
	private static Box coupleInBox(String lab, JComponent jcom, boolean req, boolean ena, boolean glue) {
		Box b = Box.createHorizontalBox();
		JLabel jl = new JLabel(lab);
		Font f = jl.getFont();
		jl.setFont((req ? f.deriveFont(Font.BOLD) : f));
		b.add(jl);
		b.add(Box.createHorizontalStrut(HORIZONTAL_GAP));
		if (glue) {
			b.add(Box.createHorizontalGlue());
		}
		setEnabled(new JComponent[] {jl, jcom}, ena);
		b.add(jcom);
		if (!glue) {
			b.add(Box.createHorizontalGlue());
		}
		return b;
	}
	
	/** invoca <code>getCoupleInBox(lab, jcom, false, true)</code>
	 *  
	 * @see <code>getCoupleInBox(String, JComponent, boolean, boolean)</code>
	 * @param lab etiqueta 
	 * @param jcom componente 
	 * @return box componentes alineados
	 */
	public static Box getCoupleInBox(String lab, JComponent jcom) {
		return getCoupleInBox(lab, jcom, false, true);
	}
	
	/** retorna el par <code>JLabel(lab) JComponent</code> dentro de un 
	 * <code>Box</code> horizontal.
	 * 
	 * @param lab etiqueta
	 * @param jcom componente al que refiere la etiqueta lab
	 * @param req =true, es un campo de entrada obligatoria
	 * @param ena =true, ambos etiqueta y componente habilitados. =false, desabilitados
	 * @return Box
	 */
	public static Box getCoupleInBox(String lab, JComponent jcom, boolean req, boolean ena) {
		return coupleInBox(lab, jcom, req, ena, true);
	}
	
	/** igual a <code>getCoupleInBox(String, JComponent)</code> solo que no
	 * coloca un <code>Box.createHorizontalGlue()</code> entre etiqueta y compoente
	 * 
	 * @see getCoupleInBox(lab, jcom)
	 */
	public static Box getCoupleInBox1(String lab, JComponent jcom) {
		return coupleInBox(lab, jcom, false, true, false);
	}
	
	/** retorna el par <code>JLabel(lab) JComponent</code> dentro de un 
	 * <code>Box</code> vertical alineados hacia la izquierda.
	 * @param lab etiqueta para <code>JLabel</code>
	 * @param jcom componente
	 * @return Box vertical
	 */
	public static Box getCoupleInBox2(String lab, JComponent jcom) {
		Box b1 = Box.createHorizontalBox();
		b1.add(new JLabel(lab));
		b1.add(Box.createHorizontalGlue());
		Box b2 = Box.createHorizontalBox();
		b2.add(jcom);
		b2.add(Box.createHorizontalGlue());
		Box b = Box.createVerticalBox();
		b.add(b1);
		b.add(b2);
		return b;
	}

	/** retorna JButton
	 * 
	 * @param lab
	 * @return
	public static JButton getJButton(String lab) {
		JButton jb = new JButton(lab, ResourceUtilities.getSmallIcon(lab));
		jb.setActionCommand(lab);
		jb.setVerticalTextPosition(AbstractButton.CENTER);
		jb.setHorizontalTextPosition(AbstractButton.RIGHT);
		return jb;
	}
	 */	
	
	public static JButton getJButton(AppAbstractAction act) {
		JButton jb = new JButton(act);
		jb.setMaximumSize(jb.getPreferredSize());
		return jb;
	}

	public static JCheckBox getJCheckBox(String txt, Record rcd, int fld) {
		Boolean f = (Boolean) rcd.getFieldValue(fld);
		JCheckBox jcb = new JCheckBox(txt, f.booleanValue());
		setToolTip(rcd.getFieldID(fld), jcb);
		return jcb;
	}
	
	/** construye y retorna un <code>JComboBox</code> estandar.
	 * 
	 * @param val - arreglo de valores
	 * @param rcd - instancia del registro de datos
	 * @param fld - indice del campo el registro para comparar con la lista
	 * de valores (val) y determinar el elemento que sera seleccionado
	 * @param e - <code>true</code> si el <code>JcomboBox</code> es editable
	 * 
	 * @return <code>JComboBox</code>
	 */
	public static JComboBox getJComboBox(AppEntry[] val, Record rcd, int fld, boolean e) {
		int row = 0;
		Object ov = rcd.getFieldValue(fld);
		for (int l = 0; l < val.length; l++) {
			if (val[l].getKey().equals(ov)) {
				row = l;
			}
		}
		JComboBox jcbox = new JComboBox(val);
		jcbox.setSelectedIndex(row);
		jcbox.setEditable(e);
		setToolTip(rcd.getFieldID(fld), jcbox);
		return jcbox;
	}
	
	public static JFormattedTextField getJFormattedTextField(Record rcd, int fld) {
		Object o = rcd.getFieldValue(fld);
		JFormattedTextField jftf = new JFormattedTextField(o);
//		jftf.setColumns(rcd.getFieldLength(fld));
		if (o instanceof java.lang.Number) {
			jftf.setHorizontalAlignment(JTextField.RIGHT);
		}
		setDimensionForTextComponent(jftf, rcd.getFieldLength(fld));
		setToolTip(rcd.getFieldID(fld), jftf);
		return jftf;
	}
	public static JTextArea getJTextArea(int col) {
		JTextArea jta = new JTextArea(" ", 2, col);
		jta.setEnabled(false);		
		jta.setLineWrap(true);
		/// mamarachada temporal
//		JTextField t = new JTextField();
//		jta.setBorder(t.getBorder());
//		jta.setMaximumSize(jta.getPreferredSize());
//		jta.setMinimumSize(jta.getPreferredSize());

		
		return jta;//(JTextArea) setDimensionForTextComponent(jta);
	}
	public static JTextField getJTextField(Record rcd, int fld) {
		JTextField jtf = new JTextField();
		jtf.setText((String) rcd.getFieldValue(fld));
//		jtf.setColumns(rcd.getFieldLength(fld));
		setDimensionForTextComponent(jtf, rcd.getFieldLength(fld));
		setToolTip(rcd.getFieldID(fld), jtf);
		return jtf;
	}
	
	/** Localiza el texto, da formato y asigna el tooltip para el componente
	 * pasado como argumento. El texto descriptivo de la ayuda es fraccionada
	 * cada tanto para evitar que el componente sea demasiado largo.
	 * 
	 * @param tid - identificador de tooltip. 
	 * @param cmp - componente 
	 */
	public static void setToolTip(String tid, JComponent cmp) {
		AppEntry tt = AppConstants.getConstant(tid);
		if (tt != null) {
			String[] stt = ((String) tt.getValue()).split(";");
			String sbr = CoreUtilities.getInsertedBR(stt[1], 100);
			String fstt = "<html> <b>" + stt[0] + "</b><p>" + sbr + "</p></html>";
			cmp.setToolTipText(fstt);
		}
	}

	public static Box getTogetherInBox(JComponent[] jcomps) {
		Box b = Box.createHorizontalBox();
		for(int t = 0; t < jcomps.length; t++) {
			b.add(jcomps[t]);
			b.add(Box.createHorizontalStrut(HORIZONTAL_GAP));
		}
		b.add(Box.createHorizontalGlue());
		return b;
	}

	/** establece dimenciones para los componentes de clase <code>JTextComponent</code>.
	 * Si una instancia de <code>JTextField</code> sobrepasa las 30 columnas, no se 
	 * modifica el ancho ya que se asume que se ve mejor.
	 * 
	 * @param jtc - componente de texto
	 * @param col - columnas
	 */	
	public static void setDimensionForTextComponent(JTextComponent jtc, int col) {
		if (jtc instanceof JTextField && col > 29) {
			((JTextField) jtc).setColumns(col);
		} else {
			Dimension d = jtc.getPreferredSize();
			d.width = (col * 10) * 80 / 100;
			jtc.setPreferredSize(d);
//			jtc.setMinimumSize(d);
			jtc.setMaximumSize(d);
		}
	}
	/** coloca en un Box los botones btns creando una barra de botones justificados
	 * hacia la derecha. Este metodo es usuado para agrupar los botones que seran 
	 * colocados en la parte inferior de un panel de componentes. 
	 * 
	 * @param btns botones que seran colocados dentro del Box
	 * @param actL ActionListener para estos botones
	 * @return Box con botones ordenados
	static public Box putButtonsInBox(JButton[] btns) {
		Box b = Box.createHorizontalBox();
		b.add(Box.createHorizontalGlue());
		for (int j = 0; j < btns.length; j++) {
			b.add(Box.createHorizontalStrut(HORIZONTAL_GAP));
			b.add(btns[j]);
		}
		Box b1 = Box.createVerticalBox();
		b1.add(Box.createVerticalStrut(VERTICAL_GAP));
		b1.add(b);
		return b1;
	}
	 */
	public static void setEmptyBorder(JComponent comp) {
		comp.setBorder(new EmptyBorder(
			HORIZONTAL_GAP, VERTICAL_GAP, HORIZONTAL_GAP, VERTICAL_GAP));
	}
	/** Habilita/inhabilita los componentes cmps. Si alguno de estos es instancia
	 * de <code>Box o JPanel</code> se realiza la operacion a los componentes que
	 * contienen en forma recursiva.
	 * 
	 * @param cmps componentes a habilitar/inhabilitar
	 * @param ena = true abilitar, inhabilitar si =false
	 */
	public static void setEnabled(Component[] cmps, boolean ena) {
		for (int e = 0; e < cmps.length; e++) {
			cmps[e].setEnabled(ena);
			if (cmps[e] instanceof Box || cmps[e] instanceof JPanel) {
				Component[] cmps1 = ((Container) cmps[e]).getComponents();
				setEnabled(cmps1, ena);
			}
		}
	}
	/** copiado de <code>Color.brighter()</code> pero con el factor modificado
	 * para obtener un mejor degradado
	 * 
	 * @return color un poco mas brillante
	 */
	public static Color brighter(Color c) {
		double FACTOR = 0.92;
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		int i = (int)(1.0/(1.0-FACTOR));
		if ( r == 0 && g == 0 && b == 0) {
		   return new Color(i, i, i);
		}
		if ( r > 0 && r < i ) r = i;
		if ( g > 0 && g < i ) g = i;
		if ( b > 0 && b < i ) b = i;

		return new Color(Math.min((int)(r/FACTOR), 255),
						 Math.min((int)(g/FACTOR), 255),
						 Math.min((int)(b/FACTOR), 255));
	}
	
	/** retorna un <code>Box</code> con formato preestablecido para los
	 * componentes que se encuentran en la parte inferior de las ventanas
	 * de dialogo.
	 * 
	 * @param jc - Generalmente, un contenedor con los botones ya añadidos
	 * @return <code>Box</code> listo para adicionar a la parte inferirior
	 */
	public static Box getBoxForButtons(JComponent jc, boolean eb) {
		Box b1 = Box.createVerticalBox();
		b1.add(Box.createVerticalStrut(VERTICAL_GAP));
		b1.add(new JSeparator(JSeparator.HORIZONTAL));
		b1.add(Box.createVerticalStrut(VERTICAL_GAP));
		b1.add(jc);
		if (eb) {
			setEmptyBorder(b1);
		}
		return b1;
	}
	
	/** establece el encho de las columnas de la tabla pasada como primer
	 * argumento al valor especificado en el segundo.
	 * 
	 * @param jt - tabla
	 * @param w - arreglo de enteros con el ancho de la columna segun su 
	 * posicion. si alguno de ellos es < 1, se omite.
	 */
	public static void fixTableColumn(JTable jt, int[] w) {
		TableColumnModel cm = jt.getColumnModel();
		TableColumn tc; 
		for (int i = 0; i < w.length; i++) {
			tc = tc = cm.getColumn(i);
			if (w[i] > 0) {
				tc.setPreferredWidth(w[i]);
			}
		}
	}
}
