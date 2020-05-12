/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.gui.wizard;

import java.awt.*;
import javax.swing.*;

import com.ae.gui.*;
import com.ae.resource.*;
import com.ae.core.*;

/** un <code>WizardPanel</code> es una extencion de <code>JPanel</code> con 
 * un formato predefinido con el estilo para una ventana del tipo 
 * "asistente para ...". Sub clases de esta deben implementar los metodos
 * abstracto y usar los metodos provistos aqui para establecer los atributos
 * distintivos del panel.
 * 
 */
public abstract class WizardPanel extends JPanel {

	private int type;
	public static int WELLCOME = 0;
	public static int NORMAL = 1;
	
	/** nueva instancia
	 *
	 */
	public WizardPanel(int t) {
		super();
		this.type = t;
		setLayout(new BorderLayout());
		if (type == WELLCOME) {
			setBackground(Color.WHITE);
			GUIUtilities.setEmptyBorder(this);
			ImageIcon ii = ResourceUtilities.getIcon("Import32");
			JLabel jl = new JLabel(ii);
			jl.setVerticalAlignment(JLabel.TOP);
			add(jl, BorderLayout.WEST);
		}
	}

	/** establece el texto informativo que distinque este paso dentro de 
	 * la secuencia. Usar texto html para dar formato.
	 * 
	 * @param info - texto informativo
	 */
	public void setText(String info) {
		JLabel jl = new JLabel(info);
		Box b1 = Box.createVerticalBox();
		b1.add(jl);
		b1.add(Box.createVerticalStrut(GUIUtilities.VERTICAL_GAP));
		if (type == NORMAL) {
			add(b1, BorderLayout.NORTH);
		} else {
			GUIUtilities.setEmptyBorder(b1);
			add(b1, BorderLayout.CENTER);
		}
	}
	
	/** Coloca el componente que contiene los componentes de entrada para
	 * este paso. Si este panes es de tipo <code>WELLCOME</code> este metodo
	 * no tiene efecto 
	 * 
	 * pendiente - verificador aqui -
	 * 
	 * @param cmp - componente
	 */
	public void setInputComponent(Component cmp) {
		if (type == NORMAL) {
			add(cmp, BorderLayout.CENTER);
		}
	}
	
	/** retorna los datos de entrada seleccionados o introducidos por el usuario
	 * dentro de este panel de entrada. La clase del objeto depende de la implementacion
	 * de la instancia. por tanto, es responsabilidad del solicitante 
	 * conocer de antemano la clase del objeto que retorna este metodo
	 * para el panel especifico del cual solicida datos.
	 * 
	 * @return Datos
	 */
	public abstract Object getData() ;
	
	/** este metodo es invocado antes de que <code>WizardsContainer</code>
	 * haga visibles este panel. Sub clases deben implementar este metodo
	 * cuando es necesario conocer la informacion descrita en(los) panel(es)
	 * anteriores a este. Use el metodo 
	 * <code>WizardContainer.getPanel(int).getData()</code> para ello. 
	 *
	 */
	public abstract void initializePanel(WizardContainer arg0) ;

	/** Este metodo es invocado por <code>WizardContainer</code> 
	 * antes de presentar el siguiete panel dentro del asistente o de finalizar
	 * este. Sub clases implementan este metodo para retornar cualquier excepcion
	 * ocurrido debido a error u omision en los datos de entrada. 
	 * 
	 * @return excepcion o <code>null</code> si la validacion es correcta.
	 */	
	public abstract AplicationException validateFields() ;
}
