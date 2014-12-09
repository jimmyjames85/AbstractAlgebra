package edu.iastate.math301;

import static edu.iastate.math301.util.Util.*;

public class HW5
{



	public static void main(String[] args)
	{
		println("The units of the first 20 integers are:");
		println();
		for(int i=1;i<=20;i++)
			println( i+ ":\t" +unitsOfN(i));
		
		println();
		println("6 is in U(11)");
		println("<6> = " + gen_a_inUnitsOfN(6, 11));
		
		println();
		println();
		println("---------");
		println();
		
		int bb = 13;
		for(int i=1;i<bb;i++)
		{
			println("<"+i+">= "+gen_a_inUnitsOfN(i, bb));
		}
		
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
