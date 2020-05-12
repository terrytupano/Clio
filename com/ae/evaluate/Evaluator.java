package com.ae.evaluate;

import com.ae.core.*;
import com.ae.evaluate.functions.math.MathFunctions;
import com.ae.evaluate.functions.string.StringFunctions;
import java.util.*;

public class Evaluator {
	private class h extends m {

		public double a(double d1) {
			return d1 != 1.0D ? 1.0D : 0.0D;
		}

		public h() {
			super("!", 0, true);
		}
	}

	private class f extends m {

		public double a(double d1, double d2) {
			return d1 != 1.0D && d2 != 1.0D ? 0.0D : 1.0D;
		}

		public f() {
			super("||", 1);
		}
	}

	private class j extends m {

		public double a(double d1, double d2) {
			return d1 != 1.0D || d2 != 1.0D ? 0.0D : 1.0D;
		}

		public j() {
			super("&&", 2);
		}
	}

	private class o extends m {

		public double a(double d1, double d2) {
			return d1 == d2 ? 0.0D : 1.0D;
		}

		public String a(String s1, String s2) {
			if (s1.compareTo(s2) != 0)
				return "1.0";
			else
				return "0.0";
		}

		public o() {
			super("!=", 3);
		}
	}

	private class aa extends m {

		public double a(double d1, double d2) {
			return d1 != d2 ? 0.0D : 1.0D;
		}

		public String a(String s1, String s2) {
			if (s1.compareTo(s2) == 0)
				return "1.0";
			else
				return "0.0";
		}

		public aa() {
			super("==", 3);
		}
	}

	private class n extends m {

		public double a(double d1, double d2) {
			return d1 < d2 ? 0.0D : 1.0D;
		}

		public String a(String s1, String s2) {
			if (s1.compareTo(s2) >= 0)
				return "1.0";
			else
				return "0.0";
		}

		public n() {
			super(">=", 4);
		}
	}

	private class i extends m {

		public double a(double d1, double d2) {
			return d1 <= d2 ? 0.0D : 1.0D;
		}

		public String a(String s1, String s2) {
			if (s1.compareTo(s2) > 0)
				return "1.0";
			else
				return "0.0";
		}

		public i() {
			super(">", 4);
		}
	}

	private class d extends m {

		public double a(double d1, double d2) {
			return d1 > d2 ? 0.0D : 1.0D;
		}

		public String a(String s1, String s2) {
			if (s1.compareTo(s2) <= 0)
				return "1.0";
			else
				return "0.0";
		}

		public d() {
			super("<=", 4);
		}
	}

	private class s extends m {

		public double a(double d1, double d2) {
			return d1 >= d2 ? 0.0D : 1.0D;
		}

		public String a(String s1, String s2) {
			if (s1.compareTo(s2) < 0)
				return "1.0";
			else
				return "0.0";
		}

		public s() {
			super("<", 4);
		}
	}

	private class u extends m {

		public double a(double d1, double d2) {
			Double double1 = new Double(d1 % d2);
			return double1.doubleValue();
		}

		public u() {
			super("%", 6);
		}
	}

	private class e extends m {

		public double a(double d1, double d2) {
			Double double1 = new Double(d1 / d2);
			return double1.doubleValue();
		}

		public e() {
			super("/", 6);
		}
	}

	private class k extends m {

		public double a(double d1, double d2) {
			Double double1 = new Double(d1 * d2);
			return double1.doubleValue();
		}

		public k() {
			super("*", 6);
		}
	}

	private class t extends m {

		public double a(double d1, double d2) {
			Double double1 = new Double(d1 - d2);
			return double1.doubleValue();
		}

		public double a(double d1) {
			return -d1;
		}

		public t() {
			super("-", 5, true);
		}
	}

	private class r extends m {

		public double a(double d1, double d2) {
			Double double1 = new Double(d1 + d2);
			return double1.doubleValue();
		}

		public String a(String s1, String s2) {
			String s3 =
				new String(
					s1.substring(0, s1.length() - 1)
						+ s2.substring(1, s2.length()));
			return s3;
		}

		public double a(double d1) {
			return d1;
		}

		public r() {
			super("+", 5, true);
		}
	}

	private class v extends m {

		public v() {
			super(")", 0);
		}
	}

	private class p extends m {

		public p() {
			super("(", 0);
		}
	}

	private class c {

		public Function _mthif() {
			return a;
		}

		public String _mthdo() {
			return _flddo;
		}

		public m a() {
			return _fldif;
		}

		private Function a;
		private String _flddo;
		private m _fldif;

		public c(Function function, String s1, m m1) {
			a = null;
			_flddo = null;
			_fldif = null;
			a = function;
			_flddo = s1;
			_fldif = m1;
		}
	}

	private abstract class m {

		public double a(double d1, double d2) {
			return 0.0D;
		}

		public String a(String s1, String s2) throws EvaluateException {
			throw new EvaluateException("i08");
		}

		public double a(double d1) {
			return 0.0D;
		}

		public String _mthif() {
			return a;
		}

		public int _mthfor() {
			return _fldif;
		}

		public int _mthdo() {
			return a.length();
		}

		public boolean a() {
			return _flddo;
		}

		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (!(obj instanceof m))
				throw new IllegalStateException("Invalid operator object.");
			m m1 = (m) obj;
			return a.equals(m1._mthif());
		}

		public String toString() {
			return _mthif();
		}

		private String a;
		private int _fldif;
		private boolean _flddo;

		public m(String s1, int i1) {
			a = null;
			_fldif = 0;
			_flddo = false;
			a = s1;
			_fldif = i1;
		}

		public m(String s1, int i1, boolean flag) {
			a = null;
			_fldif = 0;
			_flddo = false;
			a = s1;
			_fldif = i1;
			_flddo = flag;
		}
	}

	private class l {

		public m a() {
			return _fldif;
		}

		public void a(m m1) {
			_fldif = m1;
		}

		public m _mthif() {
			return a;
		}

		public void _mthif(m m1) {
			a = m1;
		}

		private m _fldif;
		private m a;

		public l(m m1, m m2) {
			_fldif = null;
			a = null;
			_fldif = m1;
			a = m2;
		}
	}

	private class b {

		public String a() {
			return a;
		}

		public void a(String s1) {
			a = s1;
		}

		public m _mthif() {
			return _fldif;
		}

		public void a(m m1) {
			_fldif = m1;
		}

		private String a;
		private m _fldif;

		public b(String s1) {
			a = null;
			_fldif = null;
			a = s1;
		}
	}

	private class q {

		public Object _mthif() {
			return _flddo;
		}

		public void a(Object obj) {
			_flddo = obj;
		}

		public Object a() {
			return _fldfor;
		}

		public void _mthif(Object obj) {
			_fldfor = obj;
		}

		public m _mthdo() {
			return _fldif;
		}

		public m _mthfor() {
			return a;
		}

		public void a(m m1) {
			_fldif = m1;
		}

		public void _mthif(m m1) {
			a = m1;
		}

		public String a(boolean flag) throws EvaluateException {
			String s1 = null;
			String s2 = null;
			Double double1 = null;
			if (_flddo instanceof q) {
				s2 = ((q) _flddo).a(flag);
				try {
					double1 = new Double(s2);
					s2 = null;
				} catch (NumberFormatException numberformatexception) {
					double1 = null;
				}
			} else if (_flddo instanceof b) {
				b b1 = (b) _flddo;
				s2 = b1.a();
				s2 = Evaluator.this.a(s2);
				if (!Evaluator.this._mthif(s2)) {
					try {
						double1 = new Double(s2);
						s2 = null;
					} catch (NumberFormatException numberformatexception1) {
						throw new EvaluateException("e07");
					}
					if (b1._mthif() != null)
						double1 =
							new Double(b1._mthif().a(double1.doubleValue()));
				} else if (b1._mthif() != null)
					throw new EvaluateException("i10");
			} else if (_flddo instanceof c) {
				c c1 = (c) _flddo;
				Function function = c1._mthif();
				String s4 = c1._mthdo();
				s4 = Evaluator.this.a(s4);
				try {
					s2 = function.execute(_fldint, s4);
					try {
						Double double3 = new Double(s2);
						if (c1.a() != null)
							double3 =
								new Double(c1.a().a(double3.doubleValue()));
						s2 = double3.toString();
					} catch (NumberFormatException numberformatexception3) {
						if (flag)
							s2 =
								Evaluator.this._fldfor
									+ s2
									+ Evaluator.this._fldfor;
						if (c1.a() != null)
							throw new EvaluateException("i10");
					}
				} catch (FunctionException functionexception) {
					throw new EvaluateException("e08");
				}
				if (!Evaluator.this._mthif(s2))
					try {
						double1 = new Double(s2);
						s2 = null;
					} catch (NumberFormatException numberformatexception4) {
						throw new EvaluateException("e07");
					}
			} else if (_flddo != null)
				throw new EvaluateException("e07");
			String s3 = null;
			Double double2 = null;
			if (_fldfor instanceof q) {
				s3 = ((q) _fldfor).a(flag);
				try {
					double2 = new Double(s3);
					s3 = null;
				} catch (NumberFormatException numberformatexception2) {
					double2 = null;
				}
			} else if (_fldfor instanceof b) {
				b b2 = (b) _fldfor;
				s3 = ((b) _fldfor).a();
				s3 = Evaluator.this.a(s3);
				if (!Evaluator.this._mthif(s3)) {
					try {
						double2 = new Double(s3);
						s3 = null;
					} catch (NumberFormatException numberformatexception5) {
						throw new EvaluateException("e07");
					}
					if (b2._mthif() != null)
						double2 =
							new Double(b2._mthif().a(double2.doubleValue()));
				} else if (b2._mthif() != null)
					throw new EvaluateException("i10");
			} else if (_fldfor instanceof c) {
				c c2 = (c) _fldfor;
				Function function1 = c2._mthif();
				String s5 = c2._mthdo();
				s5 = Evaluator.this.a(s5);
				try {
					s3 = function1.execute(_fldint, s5);
					try {
						Double double4 = new Double(s3);
						if (c2.a() != null)
							double4 =
								new Double(c2.a().a(double4.doubleValue()));
						s3 = double4.toString();
					} catch (NumberFormatException numberformatexception6) {
						if (flag)
							s3 =
								Evaluator.this._fldfor
									+ s3
									+ Evaluator.this._fldfor;
						if (c2.a() != null)
							throw new EvaluateException("i10");
					}
				} catch (FunctionException functionexception1) {
					throw new EvaluateException("e08");
				}
				if (!Evaluator.this._mthif(s3))
					try {
						double2 = new Double(s3);
						s3 = null;
					} catch (NumberFormatException numberformatexception7) {
						throw new EvaluateException("e07");
					}
			} else if (_fldfor != null)
				throw new EvaluateException("e07");
			if (double1 != null && double2 != null) {
				double d1 =
					_fldif.a(double1.doubleValue(), double2.doubleValue());
				if (_mthfor() != null)
					d1 = _mthfor().a(d1);
				s1 = (new Double(d1)).toString();
			} else if (s2 != null && s3 != null)
				s1 = _fldif.a(s2, s3);
			else if (double1 != null && double2 == null) {
				double d2 = -1D;
				if (a != null)
					d2 = a.a(double1.doubleValue());
				s1 = (new Double(d2)).toString();
			} else {
				throw new EvaluateException("e07");
			}
			return s1;
		}

		private Object _flddo;
		private Object _fldfor;
		private m _fldif;
		private m a;
		private Evaluator _fldint;

		public q(Evaluator evaluator1) {
			_flddo = null;
			_fldfor = null;
			_fldif = null;
			a = null;
			_fldint = null;
			_fldint = evaluator1;
		}
	}

	private class g {

		public m a() {
			return a;
		}

		public int _mthif() {
			return _fldif;
		}

		private m a;
		private int _fldif;

		public g(m m1, int i1) {
			a = null;
			_fldif = -1;
			a = m1;
			_fldif = i1;
		}
	}

	public Evaluator() {
		this('\'', true, true, true);
	}

	public Evaluator(char c1, boolean flag, boolean flag1, boolean flag2) {
		_fldbyte = new ArrayList();
		_fldtry = new HashMap();
		_flddo = new HashMap();
		_flddo1 = new HashMap();
		_fldfor = '\'';
		_fldchar = new p();
		_fldcase = new v();
		_fldnew = null;
		_previousOperatorStack = null;
		_previousOperandStack = null;
		_operatorStack = null;
		_operandStack = null;
		_mthif();
		_fldelse = flag;
		a();
		_fldlong = flag1;
		_fldgoto = flag2;
		_mthdo();
		setQuoteCharacter(c1);
	}

	public char getQuoteCharacter() {
		return _fldfor;
	}

	public void setQuoteCharacter(char c1) {
		if (c1 == '\'' || c1 == '"')
			_fldfor = c1;
		else
			throw new IllegalArgumentException("Invalid quote character.");
	}

	public void putFunction(Function function) {
		isNameValid(function.getName());
		Function function1 = (Function) _fldtry.get(function.getName());
		if (function1 == null)
			_fldtry.put(function.getName(), function);
		else
			throw new IllegalArgumentException("A function with the same name already exists.");
	}

	public Function getFunction(String s1) {
		return (Function) _fldtry.get(s1);
	}

	public void removeFunction(String s1) {
		if (_fldtry.containsKey(s1))
			_fldtry.remove(s1);
		else
			throw new IllegalArgumentException("The function does not exist.");
	}

	public void clearFunctions() {
		_fldtry.clear();
		_mthdo();
	}
	
	/**
	 * 
	 * @param s1 - nombre de variable
	 * @param s2 - valor
	 */
	public void putVariable(String s1, String s2) {
		isNameValid(s1);
		_flddo.put(s1, s2);
	}

	public String getVariable(String s1) {
		return (String) _flddo.get(s1);
	}

	/** retorn nombre de variables 
	 * 
	 * @return nombre de valirables 
	 */
	public String[] getVariables() {
		Set s = _flddo1.keySet();
		return (String[]) s.toArray(new String[s.size()]);
	}

	public void removeVaraible(String s1) {
		if (_flddo.containsKey(s1)) {
			_flddo.remove(s1);
			_flddo1.remove(s1);
		} else
			throw new IllegalArgumentException("The variable does not exist.");
	}

	public void clearVariables() {
		_flddo.clear();
//		_flddo1.clear();
		a();
	}

	public String evaluate(String s1) throws EvaluateException {
		return evaluate(s1, true, true);
	}

	public String evaluate() throws EvaluateException {
		String s1 = _fldnew;
		if (s1 == null || s1.length() == 0)
			throw new EvaluateException("n05");
		else
			return evaluate(s1, true, true);
	}

	public String evaluate(String s1, boolean flag, boolean flag1)
		throws EvaluateException {
		parse(s1, flag);
		String s2 = a(_operatorStack, _operandStack, flag1);
		if (_mthif(s2) && !flag)
			s2 = s2.substring(1, s2.length() - 1);
		return s2;
	}

	public String evaluate(boolean flag, boolean flag1)
		throws EvaluateException {
		String s1 = _fldnew;
		if (s1 == null || s1.length() == 0)
			throw new EvaluateException("n05");
		else
			return evaluate(s1, flag, flag1);
	}

	public boolean getBooleanResult(String s1) throws EvaluateException {
		String s2 = evaluate(s1);
		try {
			Double double1 = new Double(s2);
			return double1.doubleValue() == 1.0D;
		} catch (NumberFormatException numberformatexception) {
			return false;
		}
	}

	public double getNumberResult(String s1) throws EvaluateException {
		String s2 = evaluate(s1);
		Double double1 = null;
		try {
			double1 = new Double(s2);
		} catch (NumberFormatException numberformatexception) {
			throw new EvaluateException("e09");
		}
		return double1.doubleValue();
	}

	public void parse(String s1) throws EvaluateException {
		parse(s1, true);
	}

	public void parse(String s1, boolean flag) throws EvaluateException {
		boolean flag1 = true;
		if (!s1.equals(_fldnew)) {
			_fldnew = s1;
		} else {
			flag1 = false;
			_operatorStack = (Stack) _previousOperatorStack.clone();
			_operandStack = (Stack) _previousOperandStack.clone();
		}
		try {
			if (flag1) {
				_operandStack = new Stack();
				_operatorStack = new Stack();
				boolean flag2 = false;
				boolean flag3 = false;
				m m1 = null;
				int i1 = s1.length();
				for (int j1 = 0; j1 < i1;) {
					m m2 = null;
					int k1 = -1;
					if (a(s1.charAt(j1))) {
						j1++;
					} else {
						g g1 = a(s1, j1, ((m) (null)));
						if (g1 != null) {
							m2 = g1.a();
							k1 = g1._mthif();
						}
						if (k1 > j1 || k1 == -1) {
							j1 = a(s1, j1, k1, _operandStack, m1);
							flag2 = true;
							flag3 = false;
							m1 = null;
						}
						if (k1 == j1) {
							if (g1.a().a() && (flag3 || j1 == 0)) {
								j1 = a(k1, g1.a());
								if (m1 == null)
									m1 = g1.a();
								else
									throw new EvaluateException("c12");
							} else {
								j1 =
									a(
										s1,
										k1,
										m2,
										_operatorStack,
										_operandStack,
										flag2,
										m1);
								m1 = null;
							}
							if (!(g1.a() instanceof v)) {
								flag2 = false;
								flag3 = true;
							}
						}
					}
				}

				_previousOperatorStack = (Stack) _operatorStack.clone();
				_previousOperandStack = (Stack) _operandStack.clone();
			}

		} catch (Exception exception) {
			_fldnew = "";
			throw new EvaluateException("e10");
		}

		// extrae las variables dentro de una expresion
		int i = 0;
		int i1 = 0;
		while (i != -1) {
			i = s1.indexOf('{', i1);
			if (i != -1) {
				i1 = s1.indexOf('}', i);
				if (i1 != -1) {
					String s2 = s1.substring(i + 1, i1);
					// si no esta se inserta
					if (_flddo1.get(s2) == null) {
						_flddo1.put(s2, "");
					}
					if (_flddo.get(s2) == null) {
						_flddo.put(s2, "");
					}
					i = i1;
				}
			}
		}
	}

	private void _mthif() {
		_fldbyte.add(_fldchar);
		_fldbyte.add(_fldcase);
		_fldbyte.add(new r());
		_fldbyte.add(new t());
		_fldbyte.add(new k());
		_fldbyte.add(new e());
		_fldbyte.add(new aa());
		_fldbyte.add(new o());
		_fldbyte.add(new d());
		_fldbyte.add(new s());
		_fldbyte.add(new n());
		_fldbyte.add(new i());
		_fldbyte.add(new j());
		_fldbyte.add(new f());
		_fldbyte.add(new h());
		_fldbyte.add(new u());
	}

	private int a(String s1, int i1, int j1, Stack stack, m m1)
		throws EvaluateException {
		String s2 = null;
		int k1 = -1;
		if (j1 == -1) {
			s2 = s1.substring(i1).trim();
			k1 = s1.length();
		} else {
			s2 = s1.substring(i1, j1).trim();
			k1 = j1;
		}
		if (s2.length() == 0) {
			throw new EvaluateException("e07");
		} else {
			b b1 = new b(s2);
			b1.a(m1);
			stack.push(b1);
			return k1;
		}
	}

	private int a(
		String s1,
		int i1,
		m m1,
		Stack stack,
		Stack stack1,
		boolean flag,
		m m2)
		throws EvaluateException {
		if (flag && (m1 instanceof p)) {
			g g1 = a(s1, i1, stack1, stack, m1);
			m1 = g1.a();
			i1 = g1._mthif() + m1._mthdo();
			g1 = a(s1, i1, ((m) (null)));
			if (g1 != null) {
				m1 = g1.a();
				i1 = g1._mthif();
			} else {
				return i1;
			}
		}
		if (m1 instanceof p) {
			l l1 = new l(m1, m2);
			stack.push(l1);
		} else if (m1 instanceof v) {
			l l2 = null;
			if (stack.size() > 0)
				l2 = (l) stack.peek();
			while (l2 != null && !(l2.a() instanceof p)) {
				a(stack1, stack);
				if (stack.size() > 0)
					l2 = (l) stack.peek();
				else
					l2 = null;
			}
			if (stack.isEmpty())
				throw new EvaluateException("e07");
			l l5 = (l) stack.pop();
			if (!(l5.a() instanceof p))
				throw new EvaluateException("e07");
			if (l5._mthif() != null) {
				Object obj = stack1.pop();
				q q1 = a(obj, null, ((m) (null)));
				q1._mthif(l5._mthif());
				stack1.push(q1);
			}
		} else {
			if (stack.size() > 0) {
				for (l l3 = (l) stack.peek();
					l3 != null && l3.a()._mthfor() >= m1._mthfor();
					) {
					a(stack1, stack);
					if (stack.size() > 0)
						l3 = (l) stack.peek();
					else
						l3 = null;
				}

			}
			l l4 = new l(m1, m2);
			stack.push(l4);
		}
		int j1 = i1 + m1._mthdo();
		return j1;
	}

	private int a(int i1, m m1) {
		int j1 = i1 + m1._mthif().length();
		return j1;
	}

	private g a(String s1, int i1, Stack stack, Stack stack1, m m1)
		throws EvaluateException {
		g g1 = a(s1, i1 + 1, _fldcase);
		if (g1 == null || !(g1.a() instanceof v))
			throw new EvaluateException("e07");
		int j1 = g1._mthif();
		String s2 = s1.substring(i1 + 1, j1);
		b b1 = (b) stack.pop();
		m m2 = b1._mthif();
		String s3 = null;
		if (b1 instanceof b)
			s3 = b1.a();
		else
			throw new EvaluateException("e07");
		Function function;
		try {
			Double.valueOf(s3.substring(0, 1));
			throw new EvaluateException("a17");
		} catch (NumberFormatException numberformatexception) {
			function = (Function) _fldtry.get(s3);
		}
		if (function == null) {
			throw new EvaluateException("a18"); // (index=" + i1 + ").");
		} else {
			c c1 = new c(function, s2, m2);
			stack.push(c1);
			return g1;
		}
	}

	private void a(Stack stack, Stack stack1) {
		Object obj = null;
		Object obj1 = null;
		m m1 = null;
		if (stack.size() > 0)
			obj = stack.pop();
		if (stack.size() > 0)
			obj1 = stack.pop();
		m1 = ((l) stack1.pop()).a();
		q q1 = a(obj1, obj, m1);
		stack.push(q1);
	}

	private String a(Stack stack, Stack stack1, boolean flag)
		throws EvaluateException {
		String s1 = null;
		for (; stack.size() > 0; a(stack1, stack));
		if (stack1.size() != 1)
			throw new EvaluateException("e07");
		Object obj = stack1.pop();
		if (obj instanceof q)
			s1 = ((q) obj).a(flag);
		else if (obj instanceof b) {
			b b1 = (b) obj;
			s1 = ((b) obj).a();
			s1 = a(s1);
			if (!_mthif(s1)) {
				Double double1 = null;
				try {
					double1 = new Double(s1);
				} catch (Exception exception) {
					throw new EvaluateException("e07");
				}
				if (b1._mthif() != null)
					double1 = new Double(b1._mthif().a(double1.doubleValue()));
				s1 = double1.toString();
			} else if (b1._mthif() != null)
				throw new EvaluateException("i10");
		} else if (obj instanceof c) {
			c c1 = (c) obj;
			Function function = c1._mthif();
			String s2 = c1._mthdo();
			s2 = a(s2);
			try {
				s1 = function.execute(this, s2);
				try {
					Double double2 = new Double(s1);
					if (c1.a() != null)
						double2 = new Double(c1.a().a(double2.doubleValue()));
					s1 = double2.toString();
				} catch (NumberFormatException numberformatexception) {
					if (flag)
						s1 = _fldfor + s1 + _fldfor;
					if (c1.a() != null)
						throw new EvaluateException("i10");
				}
			} catch (FunctionException functionexception) {
				throw new EvaluateException("e08");
			}
		} else {
			throw new EvaluateException("e07");
		}
		return s1;
	}

	private q a(Object obj, Object obj1, m m1) {
		q q1 = new q(this);
		q1.a(obj);
		q1._mthif(obj1);
		q1.a(m1);
		return q1;
	}

	private g a(String s1, int i1, m m1) {
		int j1 = s1.length();
		int k1 = 0;
		for (int l1 = i1; l1 < j1; l1++) {
			if (s1.charAt(l1) == _fldfor)
				k1++;
			if (k1 % 2 != 1) {
				int i2 = _fldbyte.size();
				for (int j2 = 0; j2 < i2; j2++) {
					m m2 = (m) _fldbyte.get(j2);
					if (m1 == null || m1.equals(m2)) {
						String s2 = m2._mthif();
						if (m2._mthdo() == 2) {
							int k2 = -1;
							if (l1 + 2 <= s1.length())
								k2 = l1 + 2;
							else
								k2 = s1.length();
							if (s1.substring(l1, k2).equals(m2._mthif())) {
								g g2 = new g(m2, l1);
								return g2;
							}
						} else if (s1.charAt(l1) == m2._mthif().charAt(0)) {
							g g1 = new g(m2, l1);
							return g1;
						}
					}
				}

			}
		}

		return null;
	}

	private String a(String s1) throws EvaluateException {
		if (s1.indexOf('{') < 0 && s1.indexOf('}') < 0)
			return s1;
		String s2 = s1;
		for (Iterator iterator = _flddo.keySet().iterator();
			iterator.hasNext();
			) {
			String s3 = (String) iterator.next();
			String s4 = (String) _flddo.get(s3);
			String s5 = '{' + s3 + '}';
			s2 = a(s2, s5, s4);
		}

		int i1 = s2.indexOf('{');
		if (i1 > -1)
			throw new EvaluateException("a19");
		int j1 = s2.indexOf('}');
		if (j1 > -1)
			throw new EvaluateException("a19");
		else
			return s2;
	}

	private static String a(String s1, String s2, String s3) {
		String s4 = s1;
		if (s4 != null) {
			int i1 = 0;
			for (int k1 = s4.indexOf(s2, i1); k1 > -1;) {
				StringBuffer stringbuffer =
					new StringBuffer(
						s4.substring(0, k1) + s4.substring(k1 + s2.length()));
				stringbuffer.insert(k1, s3);
				s4 = stringbuffer.toString();
				int j1 = k1 + s3.length();
				if (j1 < s4.length())
					k1 = s4.indexOf(s2, j1);
				else
					k1 = -1;
			}

		}
		return s4;
	}

	private boolean _mthif(String s1) throws EvaluateException {
		if (s1.length() > 1
			&& s1.charAt(0) == _fldfor
			&& s1.charAt(s1.length() - 1) == _fldfor)
			return true;
		if (s1.indexOf(_fldfor) >= 0)
			throw new EvaluateException("i09");
		else
			return false;
	}

	private boolean a(char c1) {
		return c1 == ' '
			|| c1 == '\t'
			|| c1 == '\n'
			|| c1 == '\r'
			|| c1 == '\f';
	}

	public void isNameValid(String s1) throws IllegalArgumentException {
		if (s1.startsWith("0"))
			throw new IllegalArgumentException("a17");
		if (s1.startsWith("1"))
			throw new IllegalArgumentException("a17");
		if (s1.startsWith("2"))
			throw new IllegalArgumentException("a17");
		if (s1.startsWith("3"))
			throw new IllegalArgumentException("a17");
		if (s1.startsWith("4"))
			throw new IllegalArgumentException("a17");
		if (s1.startsWith("5"))
			throw new IllegalArgumentException("a17");
		if (s1.startsWith("6"))
			throw new IllegalArgumentException("a17");
		if (s1.startsWith("7"))
			throw new IllegalArgumentException("a17");
		if (s1.startsWith("8"))
			throw new IllegalArgumentException("a17");
		if (s1.startsWith("9"))
			throw new IllegalArgumentException("a17");
		if (s1.indexOf('\'') > -1)
			throw new IllegalArgumentException("a17");
		if (s1.indexOf('"') > -1)
			throw new IllegalArgumentException("a17");
		if (s1.indexOf('{') > -1)
			throw new IllegalArgumentException("a20");
		if (s1.indexOf('}') > -1)
			throw new IllegalArgumentException("a20");
		for (Iterator iterator = _fldbyte.iterator(); iterator.hasNext();) {
			m m1 = (m) iterator.next();
			if (s1.indexOf(m1._mthif()) > -1)
				throw new IllegalArgumentException(DIMain.bundle.getString("a20"));
		}

		if (s1.indexOf("!") > -1)
			throw new IllegalArgumentException(DIMain.bundle.getString("a20"));
		if (s1.indexOf("~") > -1)
			throw new IllegalArgumentException(DIMain.bundle.getString("a20"));
		if (s1.indexOf("^") > -1)
			throw new IllegalArgumentException(DIMain.bundle.getString("a20"));
		if (s1.indexOf(",") > -1)
			throw new IllegalArgumentException(DIMain.bundle.getString("a20"));
		else
			return;
	}

	private void _mthdo() {
		if (_fldlong) {
			MathFunctions mathfunctions = new MathFunctions();
			mathfunctions.load(this);
		}
		if (_fldgoto) {
			StringFunctions stringfunctions = new StringFunctions();
			stringfunctions.load(this);
		}
	}

	private void a() {
		if (_fldelse) {
			putVariable("E", (new Double(2.7182818284590451D)).toString());
			putVariable("PI", (new Double(3.1415926535897931D)).toString());
		}
	}

	private int a;
	private int _fldint;
	private int _fldif;
	public static final char SINGLE_QUOTE = 39;
	public static final char DOUBLE_QUOTE = 34;
	public static final char OPEN_BRACE = 123;
	public static final char CLOSED_BRACE = 125;
	private List _fldbyte;
	private Map _fldtry;
	private Map _flddo;
	private Map _flddo1;
	private char _fldfor;
	private m _fldchar;
	private m _fldcase;
	private boolean _fldelse;
	private boolean _fldlong;
	private boolean _fldgoto;
	private String _fldnew;
	Stack _previousOperatorStack;
	Stack _previousOperandStack;
	Stack _operatorStack;
	Stack _operandStack;

}