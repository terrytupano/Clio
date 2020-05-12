/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;

/**
 * @author Arnaldo Fuentes
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class FontDemo {

	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		JPanel panel = new JPanel(new BorderLayout());
		final FontSelectionPanel fontSelectionPanel =
			new FontSelectionPanel(
				new Font("Times New Roman", Font.BOLD + Font.ITALIC, 14));
		panel.add(fontSelectionPanel, BorderLayout.CENTER);
		JButton button = new JButton("OK");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(
						frame,
						"Selected font is: "
							+ fontSelectionPanel.getSelectedFont(),
						"Selected Font",
						JOptionPane.INFORMATION_MESSAGE);
				} catch (FontSelectionPanel.InvalidFontException ife) {
					JOptionPane.showMessageDialog(
						frame,
						"You have not selected a valid font",
						"Invalid Font",
						JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.add(button, BorderLayout.SOUTH);
		frame.setContentPane(panel);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.pack();
		frame.show();
	}
}
