
package com.ae.evaluate;

import java.util.ArrayList;
import java.util.StringTokenizer;

// Referenced classes of package com.rollingsoft.jtoolkit.evaluate:
//			FunctionException

public class FunctionHelper {

	public FunctionHelper() {
	}

	public static String trimAndRemoveQuoteChars(String s, char c) throws FunctionException {
		s = s.trim();
		if(s.charAt(0) == c)
			s = s.substring(1, s.length());
		else
			throw new FunctionException("Value does not start with a quote.");
		if(s.charAt(s.length() - 1) == c)
			s = s.substring(0, s.length() - 1);
		else
			throw new FunctionException("Value does not end with a quote.");
		return s;
	}

	public static ArrayList getDoubles(String s, char c) throws FunctionException {
		ArrayList arraylist = new ArrayList();
		try {
			char ac[] = {
				c
			};
			Double double1;
			for(StringTokenizer stringtokenizer = new StringTokenizer(s, new String(ac)); stringtokenizer.hasMoreTokens(); arraylist.add(double1))
				double1 = new Double(stringtokenizer.nextToken());

		}
		catch(NumberFormatException numberformatexception) {
			throw new FunctionException("Invalid values in string.");
		}
		return arraylist;
	}

	public static ArrayList getStrings(String s, char c) throws FunctionException {
		ArrayList arraylist = new ArrayList();
		try {
			char ac[] = {
				c
			};
			String s1;
			for(StringTokenizer stringtokenizer = new StringTokenizer(s, new String(ac)); stringtokenizer.hasMoreTokens(); arraylist.add(s1))
				s1 = stringtokenizer.nextToken();

		}
		catch(NumberFormatException numberformatexception) {
			throw new FunctionException("Invalid values in string.");
		}
		return arraylist;
	}

	public static ArrayList getOneStringAndOneInteger(String s, char c) throws FunctionException {
		ArrayList arraylist = new ArrayList();
		try {
			char ac[] = {
				c
			};
			StringTokenizer stringtokenizer = new StringTokenizer(s, new String(ac));
			for(int i = 0; stringtokenizer.hasMoreTokens(); i++)
				if(i == 0) {
					String s1 = stringtokenizer.nextToken();
					arraylist.add(s1);
				} else
				if(i == 1) {
					String s2 = stringtokenizer.nextToken().trim();
					Integer integer = new Integer(s2);
					arraylist.add(integer);
				} else {
					throw new FunctionException("Invalid values in string.");
				}

		}
		catch(NumberFormatException numberformatexception) {
			throw new FunctionException("Invalid values in string.");
		}
		return arraylist;
	}

	public static ArrayList getTwoStringsAndOneInteger(String s, char c) throws FunctionException {
		ArrayList arraylist = new ArrayList();
		try {
			char ac[] = {
				c
			};
			StringTokenizer stringtokenizer = new StringTokenizer(s, new String(ac));
			for(int i = 0; stringtokenizer.hasMoreTokens(); i++)
				if(i == 0 || i == 1) {
					String s1 = stringtokenizer.nextToken();
					arraylist.add(s1);
				} else
				if(i == 2) {
					String s2 = stringtokenizer.nextToken().trim();
					Integer integer = new Integer(s2);
					arraylist.add(integer);
				} else {
					throw new FunctionException("Invalid values in string.");
				}

		}
		catch(NumberFormatException numberformatexception) {
			throw new FunctionException("Invalid values in string.");
		}
		return arraylist;
	}

	public static ArrayList getOneStringAndTwoIntegers(String s, char c) throws FunctionException {
		ArrayList arraylist = new ArrayList();
		try {
			char ac[] = {
				c
			};
			StringTokenizer stringtokenizer = new StringTokenizer(s, new String(ac));
			for(int i = 0; stringtokenizer.hasMoreTokens(); i++)
				if(i == 0) {
					String s1 = stringtokenizer.nextToken();
					arraylist.add(s1);
				} else
				if(i == 1 || i == 2) {
					String s2 = stringtokenizer.nextToken().trim();
					Integer integer = new Integer(s2);
					arraylist.add(integer);
				} else {
					throw new FunctionException("Invalid values in string.");
				}

		}
		catch(NumberFormatException numberformatexception) {
			throw new FunctionException("Invalid values in string.");
		}
		return arraylist;
	}
}