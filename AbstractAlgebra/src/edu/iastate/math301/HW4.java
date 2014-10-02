package edu.iastate.math301;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


public class HW4
{

	public static <T> Set<T> intersection(Set<T> a, Set<T> b)
	{
		Set<T> ret = new TreeSet<T>();

		if (b.size() < a.size())
		{
			Set<T> tmp = a;
			a = b;
			b = tmp;
		}

		// a now points to the smaller of the two sets

		for (T elem : a)
		{
			if (b.contains(elem))
				ret.add(elem);
		}

		return ret;
	}

	public static long gcd(long a, long b)
	{
		if (a <= 0 || b <= 0)
			throw new IllegalArgumentException();

		while (b > 0)
		{
			long temp = b;
			b = a % b; // % is remainder
			a = temp;
		}
		return a;
	}

	public static long lcm(long a, long b)
	{
		if (a <= 0 || b <= 0)
			throw new IllegalArgumentException();

		return a * (b / gcd(a, b));
	}

	public static Set<Integer> unitsOfN(int n)
	{
		if (n <= 0)
			throw new IllegalArgumentException();

		TreeSet<Integer> ret = new TreeSet<Integer>();

		for (int i = 1; i < n; i++)
			if (gcd(i, n) == 1)
				ret.add(i);

		return ret;
	}

	/**
	 * 
	 * @param a
	 *            - generator
	 * @param n
	 *            - modulus
	 * @return a cyclic set generated by a 
	 * 
	 * <a>={a, a^2, a^3...e}   for a in U(n)
	 */
	public static Set<Integer> genAinUnitsOfN(int a, int n)
	{
		if (a <= 0 || n <= 0)
			throw new IllegalArgumentException();

		TreeSet<Integer> ret = new TreeSet<Integer>();

		a = a % n;

		int cur = a;

		ret.add(cur);
		cur = cur * a % n;
		while (cur != a)
		{
			ret.add(cur);
			cur = cur * a % n;
		}

		return ret;
	}
	
	public static void println()
	{
		println("");
	}
	public static void println(Object o)
	{
		System.out.println(o.toString());
	}

	/**
	 * 
	 * @param a
	 *            - generator
	 * @param n
	 *            - modulus
	 *            
	 * @return the order of a in U(n)
	 * 
	 * (-1 means infinite order)
	 */
	public static int orderOfAforUnitsOfN(int a, int n)
	{
		if (a <= 0 || n <= 0 || a > n)
			throw new IllegalArgumentException();

		int cur = a % n;
		int i = 1;
		while (cur != 1 && i <= n)
		{
			cur = cur * a % n;
			i++;
		}

		if (i == n + 1)
			i = -1;

		return i;
	}

	public static void main(String[] args)
	{
		println("The units of the first 20 integers are:");
		println();
		for(int i=1;i<=20;i++)
			println( i+ ":\t" +unitsOfN(i));
		
		println();
		println("6 is in U(11)");
		println("<6> = " + genAinUnitsOfN(6, 11));
		
		/*
		int mainZn = 20;
		for (int i = 1; i <= mainZn; i++)
		{
			Set<Integer> units_i = unitsOfN(i);
			System.out.println("U(" + i + "):\t" + units_i);

			ArrayList<Integer> a_squared = new ArrayList<Integer>();
			ArrayList<Integer> ordA = new ArrayList<Integer>();
			for (int a : units_i)
			{
				a_squared.add(a * a % i);
				ordA.add(orderOfAforUnitsOfN(a, i));
			}
			System.out.println("a^2:\t" + a_squared);
			System.out.println("ord(a):\t" + ordA);

			if (i > 2)
			{

				int size = units_i.size();

				int half = (Integer) units_i.toArray()[(size + 1) / 2];
				int ordHalf = orderOfAforUnitsOfN(half, i);
				System.out.println("ordHalf=" + ordHalf + "  size=" + size);

			}

			System.out.println();

		}

		int a = 7;
		int m = 5;
		int n = 3;

		int lcm = (int) lcm(m, n);
		int mod = 101;
		println("lcm=" + lcm);

		Set<Integer> setAm = genAinUnitsOfN((int) Math.pow(a, m), mod);
		Set<Integer> setAn = genAinUnitsOfN((int) Math.pow(a, n), mod);
		Set<Integer> m_intersect_n = intersection(setAm, setAn);
		System.out.println();
		Set<Integer> setLcm = genAinUnitsOfN((int) (Math.pow(a, lcm) % mod), mod);

		println(setAm);
		println(setAn);
		println();
		println(m_intersect_n);
		println(setLcm);
		println();
		println();
		*/


		
		

	}
}
