package com.ae.gui.kunststoff;

import javax.swing.plaf.*;

import com.ae.core.*;

/** azul
 * 
 */
public class ExperienceBlue extends KunststoffDesktopTheme implements Keeper{

	private final ColorUIResource primary1 = new ColorUIResource(49, 106, 196);
	private final ColorUIResource primary2 = new ColorUIResource(166, 202, 240);
	private final ColorUIResource primary3 = new ColorUIResource(195, 212, 232);

	private final ColorUIResource secondary1 = new ColorUIResource(128, 128, 128);
	private final ColorUIResource secondary2 = new ColorUIResource(189, 190, 176);
	private final ColorUIResource secondary3 = new ColorUIResource(236, 233, 216);

	private final ColorUIResource focus = new ColorUIResource(0, 128, 192);
	private final ColorUIResource hightlightText = new ColorUIResource(49, 106, 196);

	/** nueva instancia 
	 *
	 */
	public ExperienceBlue() {
		super();
	}

	public String getName() {
		return "Experience blue";
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
	
	protected ColorUIResource getSecondary1() {
		return secondary1;
	}
	
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
