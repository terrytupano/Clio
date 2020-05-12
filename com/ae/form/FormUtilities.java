/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.form;

import java.awt.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.*;

import com.ae.core.*;
import com.ae.resource.*;


public class FormUtilities {
	
	public static int HORIZONTAL_GAP = 4;
	public static int VERTICAL_GAP = 4;
	
	/** Retorna componentes alineados dentro de un Box
	 * 
	 * @param f - forma
	 * @parm lab - String para JLabel
	 * @parm jcomp - componente que se coloca al lado de la etiqueta
	 * @parm glue - =true coloca Box.createHorizontalGlue() entre la eteiquta y el
	 * componente, =false, solo coloca Box.createHorizontalStrut(HORIZONTAL_GAP)
	 */
	public static Box getCoupleInBox(Form f, String lab, JComponent jcom, boolean glue) {
		Box b = Box.createHorizontalBox();
		JLabel jl = getJLabel(f, lab);
		b.add(jl);
		b.add(Box.createHorizontalStrut(HORIZONTAL_GAP));
		if (glue) {
			b.add(Box.createHorizontalGlue());
		}
		b.add(jcom);
		if (!glue) {
			b.add(Box.createHorizontalGlue());
		}
		return b;
	}
	
	/** Retorna <code>JButton</code> usados para las acciones dentro de 
	 * <code>InputRequestPanel</code>
	 * 
	 */
	public static JButton getJButton(Form f, String lab, String ico) {
		JButton jb = new JButton(DIMain.bundle.getString(lab), ResourceUtilities.getSmallIcon(ico));
		jb.setUI(new MetalButtonUI());		
//		jb.setActionCommand(lab);
		jb.setVerticalTextPosition(AbstractButton.CENTER);
		jb.setHorizontalTextPosition(AbstractButton.RIGHT);
		jb.setContentAreaFilled(false);
		jb = (JButton) setDefault(jb, f);
		return jb;
	}
	
	public static JComboBox getJComboBox(Form f, AppEntry[] ent) {
		JComboBox jcbox = new JComboBox(ent);
		MetalComboBoxUI ui = new MetalComboBoxUI();
		jcbox.setUI((MetalComboBoxUI) MetalComboBoxUI.createUI(jcbox));
		jcbox.setFont((Font) f.getAttribute("panel.font"));
		jcbox.setBackground((Color) f.getAttribute("panel.background"));
		jcbox.setForeground((Color) f.getAttribute("panel.foreground"));
		jcbox.setSelectedIndex(0);
		return jcbox;
	}
	
	/** Retorna <code>JFormattedTextField</code>
	 * 
	 */
	public static JFormattedTextField getJFormattedTextField(Form f, Object val) {
		JFormattedTextField jftf = new JFormattedTextField();
		jftf.setUI(new MetalTextFieldUI());		
		jftf.setValue(val);
//		jftf.addFocusListener(new AppFocusListener());
		jftf.setHorizontalAlignment(JTextField.RIGHT);
		jftf = (JFormattedTextField) setDefault(jftf, f);
		jftf.setColumns(10);
		jftf.setMinimumSize(jftf.getPreferredSize());
		jftf.setMaximumSize(jftf.getPreferredSize());
		return jftf;
	}
	
	/** retorn <code>JLabel</code>
	 * 
	 */
	public static JLabel getJLabel(Form f, String lab) {
		String lab1 = "<html>" + CoreUtilities.getInsertedBR(lab, 50) + "</html>";
		JLabel jl = new JLabel(lab1);
		jl.setForeground((Color) f.getAttribute("panel.foreground"));
		jl.setFont((Font) f.getAttribute("panel.font"));
		return jl;
	}

	/** retorna <code>JPanel</code>
	 * 
	 * @param f - forma
	 * @param bor - =true, el panel devuelto tiene un borde
	 * @return panel
	 */	
	public static JPanel getJPanel(Form f, boolean bor) {
		JPanel jp = new JPanel();
		jp.setBackground((Color) f.getAttribute("panel.background"));
		if (bor) {
			jp.setBorder(new EmptyBorder(HORIZONTAL_GAP, VERTICAL_GAP, 
				HORIZONTAL_GAP, VERTICAL_GAP));
		}
		return jp;
	}

	/** retorna <code>JCheckBox</code>
	 * 
	 * @param f - Forma
	 * @param txt - id de texto
	 * @return componente
	 */
	public static JCheckBox getJCheckBox(Form f, String txt) {
		JCheckBox jcb = new JCheckBox(DIMain.bundle.getString(txt));
		jcb.setUI(new MetalCheckBoxUI());		
		jcb.setForeground((Color) f.getAttribute("panel.foreground"));
		jcb.setBackground((Color) f.getAttribute("panel.background"));
		jcb.setFont((Font) f.getAttribute("panel.font"));
		return jcb;
	}

	/** Retorna <code>JTextField</code> 
	 * 
	 * @param f - Forma 
	 * @param col - columnas
	 * @return - componentes
	 */	
	public static JTextField getJTextField(Form f, int col) {
		JTextField jtf = new JTextField();
		jtf.setUI(new MetalTextFieldUI());
//		jtf.addFocusListener(new AppFocusListener());		
		jtf = (JTextField) setDefault(jtf, f);
		jtf.setColumns(col);
		jtf.setMinimumSize(jtf.getPreferredSize());
		jtf.setMaximumSize(jtf.getPreferredSize());
		return jtf;
	}
	
	/** retorna <code>JLabel</code> usada como barra de titulo 
	 * para <code>InputRequesPanel</code>
	 * 
	 */
	public static JLabel getTitleLabel(Form f) {
//		ImageIcon ii = ResourceUtilities.getSmallIcon("edit");
		JLabel jl = new JLabel(" ", JLabel.LEFT);
		jl.setOpaque(true);
		jl.setBackground((Color) f.getAttribute("title.background"));
		jl.setForeground((Color) f.getAttribute("title.foreground"));
		jl.setFont((Font) f.getAttribute("title.font"));
		jl.setBorder(new EmptyBorder(0, 1, 0, 1));
		return jl;
	}

	/** establece atributos basicos para todos los componentes que seran
	 * presentados durante la endicion de una forma.
	 * 
	 * @param cmp - componente
	 * @param f - instancia de <code>Form</code>
	 * @return componente con atributos establecidos
	 */
	private static JComponent setDefault(JComponent cmp, Form f) {
		cmp.setFont((Font) f.getAttribute("panel.font"));
		cmp.setBackground((Color) f.getAttribute("panel.background"));
		cmp.setForeground((Color) f.getAttribute("panel.foreground"));
		cmp.setBorder(new CompoundBorder(
			new LineBorder((Color) f.getAttribute("panel.foreground")), 
			new EmptyBorder(1, 1, 1, 1)));
		return cmp;
	}
	/** Retorna la representacion String del objeto val
	 * 
	 * @param val - Valor a convertir
	 * @param f - Si = true y val es instancia de <code>Double</code>, 
	 * lo valores numericos son formateados segun Locale.
	 * @return la representacion String del valor val.
	 */
	public static String stringFormat(Object val, boolean f) {
		String strVal = null;
		if (val instanceof java.lang.Double) {
			NumberFormat numFormat = NumberFormat.getInstance();
			numFormat.setMaximumFractionDigits(2);
			double d = ((Double) val).doubleValue();
			strVal = (f) ? numFormat.format(d) : String.valueOf(d);  
		} else {
			strVal = val.toString();
		}
		return strVal;
	}	
}
