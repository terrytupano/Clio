package com.ae.gui.kunststoff;

import javax.swing.plaf.*;

import com.ae.core.*;

/** azul
 * 
 */
public class Constrast extends KunststoffDesktopTheme implements Keeper{

	private final ColorUIResource primary1 = new ColorUIResource(0, 0, 0);
	private final ColorUIResource primary2 = new ColorUIResource(204, 204, 204);
	private final ColorUIResource primary3 = new ColorUIResource(255, 255, 255);

	private final ColorUIResource secondary2 = new ColorUIResource(204, 204, 204);
	private final ColorUIResource secondary3 = new ColorUIResource(255, 255, 255);

	/** nueva instancia 
	 *
	 */
	public Constrast() {
		super();
	}

	public String getName() {
		return "Constrast";
	}

	protected ColorUIResource getPrimary1() { 
		return primary1;
	}

	protected ColorUIResource getPrimary2() {
		return primary2;
	}

	protected ColorUIResource getPrimary3() {
		return primary3;
	}
	/*
	protected ColorUIResource getSecondary1() {
		return secondary1;
	}
	 */
	
	protected ColorUIResource getSecondary2() {
		return secondary2;
	}

	protected ColorUIResource getSecondary3() {
		return secondary3;
	}
	/*
	public ColorUIResource getFocusColor() {
		return focus;
	}
	public ColorUIResource getPrimaryControlShadow() {
		return getPrimary3();
	}

	public ColorUIResource getTextHighlightColor() {
		return hightlightText;
	}
	
	public ColorUIResource getHighlightedTextColor() {
		return getWhite();
	}
	*/
}
