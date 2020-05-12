/*
 *
 * infocent
 *
 */
package com.ae.gui.kunststoff;

import javax.swing.plaf.*;

import com.ae.core.*;

public class RedmondTheme extends KunststoffDesktopTheme implements Keeper {
   /*
    * colores
    */
   private final ColorUIResource p1 = new ColorUIResource(8, 36, 107);
   private final ColorUIResource p2 = new ColorUIResource(82, 113, 173);
   private final ColorUIResource p3 = new ColorUIResource(173, 211, 255);
   private final ColorUIResource s1 = new ColorUIResource(132, 130, 132);
   private final ColorUIResource s2 = new ColorUIResource(148, 146, 140);
   private final ColorUIResource s3 = new ColorUIResource(214, 211, 206);
   /*
    * metodos
    */
   public String getName() {
      return "Redmond 4.9/5.0";
   }
   public ColorUIResource getPrimary1() { return p1; }
   public ColorUIResource getPrimary2() { return p2; }
   public ColorUIResource getPrimary3() { return p3; }
   public ColorUIResource getSecondary1() { return s1; }
   public ColorUIResource getSecondary2() { return s2; }
   public ColorUIResource getSecondary3() { return s3; }
}


