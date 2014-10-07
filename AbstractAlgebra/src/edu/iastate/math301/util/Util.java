package edu.iastate.math301.util;

import java.util.Set;
import java.util.TreeSet;

public class Util
{

	public static void println()
	{
		println("");
	}
	public static void println(Object o)
	{
		System.out.println(o.toString());
	}
	public static void print(Object o)
	{
		System.out.print(o.toString());
	}
	
	public static long factorial(int n)
	{
		if(n<0)
			throw new IllegalArgumentException("n must not be negative");
		
		if(n==0)
			return 1;
		
		return n*factorial(n-1);
	}
	
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
}
