/* 
 * Copyright (c) 2003 Arnaldo Fuentes. Todos los derechos reservados.
 */

package com.ae.core;
import com.ae.resource.*;
import java.util.*;
import org.jdom.*;

/** Encapsula todas las constantes usadas dentro de la aplicacion.  
 */
public class AppConstants {

	private static TreeMap constants = new TreeMap();
	private static Hashtable constants1 = new Hashtable();

	// desde archivo a constants	
	static {

		// ciiu
		Document d =
			PrevalentSystemManager.getXMLDocument(
				ResourceUtilities.getPrevaylerPath() + "ciiu.xml");
		inserIntoConstants(d, "ciiu");

		// actividad para personas naturales
		d =
			PrevalentSystemManager.getXMLDocument(
				ResourceUtilities.getPrevaylerPath() + "naactivity.xml");
		inserIntoConstants(d, "naActivity");

		// tooltips
		d =
			PrevalentSystemManager.getXMLDocument(
				ResourceUtilities.getPrevaylerPath() + "tooltips.xml");
		inserIntoConstants(d, "tt");

		// unidades tributarias
		d =
			PrevalentSystemManager.getXMLDocument(
				ResourceUtilities.getPrevaylerPath() + "TUList.xml");
		inserIntoConstants(d, "tu");

		// retenciones
		d =
			PrevalentSystemManager.getXMLDocument(
				ResourceUtilities.getPrevaylerPath() + "1808List.xml");
		inserIntoConstants(d, "ret");

		// alicuotas del iva
		d =
			PrevalentSystemManager.getXMLDocument(
				ResourceUtilities.getPrevaylerPath() + "AliquotList.xml");
		inserIntoConstants(d, "ali_iva");

		putConstant("status01", new AppEntry("s1", "Soltero"));
		putConstant("status02", new AppEntry("s2", "Casado"));

		putConstant("month02", new AppEntry("02", "Febrero"));
		putConstant("month01", new AppEntry("01", "Enero"));
		putConstant("month03", new AppEntry("03", "Marzo"));
		putConstant("month04", new AppEntry("04", "Abril"));
		putConstant("month05", new AppEntry("05", "Mayo"));
		putConstant("month06", new AppEntry("06", "Junio"));
		putConstant("month07", new AppEntry("07", "Julio"));
		putConstant("month08", new AppEntry("08", "Agosto"));
		putConstant("month09", new AppEntry("09", "Septiembre"));
		putConstant("month10", new AppEntry("10", "Octubre"));
		putConstant("month11", new AppEntry("11", "Noviembre"));
		putConstant("month12", new AppEntry("12", "Diciembre"));

		putConstant(
			"corporation01",
			new AppEntry("ca", "Compañía/Sociedad anónima"));
		putConstant(
			"corporation02",
			new AppEntry("cb", "Sociedad de responsabilidad limitada"));
		putConstant(
			"corporation03",
			new AppEntry("cc", "Sociedad en comandita por acciones"));
		putConstant(
			"corporation04",
			new AppEntry("cd", "Sociedad en comandita simple"));
		putConstant("corporation05", new AppEntry("ce", "Sociedad civil"));
		putConstant("corporation06", new AppEntry("cf", "Asociación"));
		putConstant("corporation07", new AppEntry("cg", "Fundación"));
		putConstant("corporation08", new AppEntry("ch", "Cooperativa"));
		putConstant("corporation09", new AppEntry("ci", "Caja de ahorro"));
		putConstant("corporation10", new AppEntry("cj", "Sociedad en nombre colectivo"));
		putConstant("corporation11", new AppEntry("ck", "Corporación"));
		putConstant("corporation12", new AppEntry("cn", "Consorcio"));
		putConstant("corporation13", new AppEntry("cl", "Cuentas en participaciones"));
		putConstant("corporation14", new AppEntry("co", "Comunidad"));
		putConstant("corporation50", new AppEntry("cz", "Otras sociedades"));

		putConstant("activity01", new AppEntry("aa", "Comercial"));
		putConstant("activity03", new AppEntry("ab", "Industrial"));
		putConstant("activity04", new AppEntry("ac", "Bancaria"));
		putConstant("activity05", new AppEntry("ad", "Financiera"));
		putConstant("activity06", new AppEntry("ae", "Reaseguros"));
		putConstant("activity07", new AppEntry("af", "Seguros"));
		putConstant("activity09", new AppEntry("ah", "Refinación"));
		putConstant("activity10", new AppEntry("ai", "Transporte"));
		putConstant("activity11", new AppEntry("aj", "Servicios turisticos"));
		putConstant("activity12", new AppEntry("ak", "Explotación de minas"));
		putConstant("activity13", new AppEntry("am", "Explotación de hidrocarburos"));
		putConstant("activity14", new AppEntry("an", "Actividades agrícolas, pescuarias, pesquera o pisicola"));
		putConstant("activity15", new AppEntry("ao", "Agroindustria"));

		putConstant("person01", new AppEntry("na", "Natural"));
		putConstant("person02", new AppEntry("ju", "Juridica"));

		putConstant("keepTo01", new AppEntry("pn-r", "Naturales residentes"));
		putConstant("keepTo02", new AppEntry("pn-nr", "Naturales no residentes"));
		putConstant("keepTo03", new AppEntry("pj-d", "Jurídicas domiciliadas"));
		putConstant("keepTo04", new AppEntry("pj-nd", "Jurídicas no domiciliadas"));

		putConstant("nation01", new AppEntry("ve", "Venezolano"));
		putConstant("nation02", new AppEntry("ex", "Extranjero"));

		putConstant("tpType01", new AppEntry("or", "Ordinario"));
		putConstant("tpType02", new AppEntry("fo", "Formal"));

		// frecuencias usadas por AmountEditor		
		putConstant("mountFrequency01", new AppEntry("001", "Diarios"));
		putConstant("mountFrequency02", new AppEntry("030", "Mensuales"));
		putConstant("mountFrequency03", new AppEntry("360", "Anuales"));
		putConstant("unitFrequency01", new AppEntry("001", "Dia(s)"));
		putConstant("unitFrequency02", new AppEntry("030", "Mes(es)"));

		putConstant("Disencumbrance01", new AppEntry("tuForHomeRent", "800"));
		putConstant("Disencumbrance01", new AppEntry("tuForHomeLoan", "1000"));

		// AppEntry("desde;hasta", "%;sustraendo")
		putConstant("article50Table01", new AppEntry("0;1000", "06;000"));
		putConstant("article50Table02", new AppEntry("1000;1500", "09;030"));
		putConstant("article50Table03", new AppEntry("1500;2000", "12;075"));
		putConstant("article50Table04", new AppEntry("2000;2500", "16;155"));
		putConstant("article50Table05", new AppEntry("2500;3000", "20;255"));
		putConstant("article50Table07", new AppEntry("3000;4000", "24;375"));
		putConstant("article50Table08", new AppEntry("4000;6000", "29;575"));
		putConstant("article50Table09", new AppEntry("6000;9999", "34;875"));

		// AppEntry("desde;hasta", "%;sustraendo")
		putConstant("article52Table01", new AppEntry("0;2000", "15;000"));
		putConstant("article52Table02", new AppEntry("2000;3000", "22;140"));
		putConstant("article52Table03", new AppEntry("3000;9999", "34;500"));

		// AppEntry("valor del campo tpActEco", "%")
		// se inserta _53 para evitar duplicacion
		putConstant("article53Table01", new AppEntry("ak_53", "60"));
		putConstant("article53Table02", new AppEntry("am_53", "50"));

		putConstant("theme01", new AppEntry(				
			"com.ae.gui.kunststoff.KunststoffDesktopTheme",
			"KunststoffDesktopTheme"));
		putConstant("theme02", new AppEntry(
			"com.ae.gui.kunststoff.ExperienceBlue",
			"Experience Blue"));
		putConstant("theme03", 	new AppEntry(
			"com.ae.gui.kunststoff.Constrast",
			"Constrast"));
		putConstant("theme04", 	new AppEntry(
			"com.ae.gui.kunststoff.RedmondTheme",
			"Redmond 4.9/5.0"));

		
		
		putConstant("savForm01", new AppEntry(new Integer(1), "1 minuto"));
		putConstant("savForm03", new AppEntry(new Integer(3), "3 minutos"));
		putConstant("savForm05", new AppEntry(new Integer(5), "5 minutos"));
		putConstant("savForm10", new AppEntry(new Integer(10), "10 minutos"));

		//		putConstant("aliNum01", new AppEntry(new Integer(SwingConstants.RIGHT), "Derecha"));
		//		putConstant("aliNum02", new AppEntry(new Integer(SwingConstants.LEFT), "Izquierda"));

		// cada una de estas aplicaciones debe tener un documento con nombre
		// ingual a la clave del appentry que indique los requisitos
		// antes de importar movimientos
		putConstant("importApp01",
			new AppEntry("saint45", "Saint contabilidad general Ver. 4.5"));
		putConstant("importApp10",
			new AppEntry("psoft50", "Premium soft contabilidad R/5.0"));
		putConstant("importApp20",
			new AppEntry("a2cont160", "a2 contabilidad versión 1.60"));
		putConstant("importApp30",
			new AppEntry("infocont546", "Infocont Versión 5.46"));

		putConstant("remaind01", new AppEntry(new Integer(1), "1"));
		putConstant("remaind02", new AppEntry(new Integer(2), "2"));
		putConstant("remaind03", new AppEntry(new Integer(3), "3"));
		putConstant("remaind04", new AppEntry(new Integer(4), "4"));
		putConstant("remaind05", new AppEntry(new Integer(5), "5"));

		putConstant("bank01", new AppEntry("0133-0029-13-1000013760", "Federal, 0133-0029-13-1000013760, Arnaldo Fuentes"));
//		putConstant(
//			"bank02",
//			new AppEntry("numero de cuenta_cor", "Corp Banca"));
		
		putConstant("EFuntion01", new AppEntry("", "Ninguno. No alterar monto"));
		putConstant("EFuntion10", new AppEntry("ISign({var})", "Invertir signo"));
		
		putConstant("Font01", new AppEntry("ARIAL.TTF;10", "Arial, 10 pts"));
		putConstant("Font05", new AppEntry("ARIAL.TTF;11", "Arial, 11 pts"));
		putConstant("Font08", new AppEntry("ARIAL.TTF;12", "Arial, 12 pts"));
		putConstant("Font10", new AppEntry("COUR.TTF;10", "Courier New, 10 pts"));
		putConstant("Font15", new AppEntry("COUR.TTF;11", "Courier New, 11 pts"));
		putConstant("Font20", new AppEntry("COUR.TTF;12", "Courier New, 12 pts"));
		putConstant("Font25", new AppEntry("GOTHIC.TTF;10", "Century Gothic, 10 pts"));
		putConstant("Font30", new AppEntry("GOTHIC.TTF;11", "Century Gothic, 11 pts"));
		putConstant("Font35", new AppEntry("GOTHIC.TTF;12", "Century Gothic, 12 pts"));
		
	}

	/** Retorna la constante cuya clave es igual al argumento de entrada.
	 * 
	 * @param key codigo de la constante
	 * @return valor
	 */
	public static AppEntry getConstant(String key) {
		return (AppEntry) constants1.get(key);
	}
	/** retorna una lista de las constantes que coincidan con el tipo especificado
	 * por typ.
	 * 
	 * @param typ - tipo de constantes 
	 * @return lista de constantes dentro del systema
	 */
	public static AppEntry[] getConstantsOfType(String typ) {
		SortedMap sm = constants.subMap(typ + "01", typ + "99999999");
		return (AppEntry[]) ((Collection) sm.values()).toArray(new AppEntry[0]);
	}

	/** Inserta todo el documento como una lista de constantes. Cada elemeto
	 * dentro de la lista debe tener una estructura igual a
	 * <code>&lt;item code="x" text="xxx" &gt;</code> este metodo usa el 
	 * atributo <code>code</code> y lo concatena con el prefijo para 
	 * adicionar la entrada.
	 * 
	 * @param d - documento
	 * @param prefix - prefijo para la identificar al grupo
	 */
	private static void inserIntoConstants(Document d, String prefix) {
		Element ro = d.getRootElement();
		List l = ro.getChildren();
		for (int e = 0; e < l.size(); e++) {
			Element el = (Element) l.get(e);
			String co = el.getAttributeValue("code");
			AppEntry a = new AppEntry(co, el.getAttributeValue("text"));
			putConstant(prefix + co, a);
		}
	}

	/** adiciona la entrada al arbol de constantes. <code>Hashtable</code> constants1
	 * permite localizar <code>appEntry</code> usando la clave. Colocada aqui para evitar 
	 * la creacion constante de Object[] durante la ejecucion de <code>getConstant()</code>;
	 * 
	 * @param typ - identificador de tipo de constante
	 * @param ape - constante
	 */
	private static void putConstant(String typ, AppEntry ape) {
		constants.put(typ, ape);
		constants1.put(ape.getKey(), ape);
	}
	
	/** retorna <code>AppEntry</code> con años desde (argumento - 
	 * año actual) hasta el año actual.
	 * 
	 * ------------------
	 * nota: este metodo deberia modificarse para retornar desde el 
	 * menor año en la lista de unidades tributarias hasta el año actual.
	 * --------------------
	 * 
	 * @param offs - nro de años a retornar
	 * @return - años
	 */
	public static AppEntry[] getYears(int offs) {
		int sy = (new AppDate()).getFromCalendar(GregorianCalendar.YEAR);
		sy = sy - offs + 1;
		AppEntry[] ay = new AppEntry[offs];
		for (int a = sy; a < sy + offs; a++) {
			ay[a - sy] = new AppEntry(String.valueOf(a), String.valueOf(a));
		}
		return ay;		
	}
}
