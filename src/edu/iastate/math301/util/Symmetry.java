package edu.iastate.math301.util;

import static edu.iastate.math301.util.Util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Symmetry implements Comparable<Symmetry>
{

	public int compareTo(Symmetry o)
	{
		return this.toString().compareTo(o.toString());
	}

	private int map[];

	private Symmetry()
	{

	}

	public Symmetry(int map[])
	{
		this.map = new int[map.length];
		for (int i = 0; i < map.length; i++)
			this.map[i] = map[i];
	}

	public Symmetry(String symmetry, int Sn)
	{
		symmetry = symmetry.replaceAll(" ", "");

		ArrayList<Symmetry> list = new ArrayList<Symmetry>();

		int left = symmetry.indexOf("(");
		int right = symmetry.indexOf(")", left);
		while (left != -1 && right != -1 && right > left)
		{
			int mapc[] = new int[Sn];
			for (int i = 0; i < mapc.length; i++)
				mapc[i] = i + 1;

			for (int i = left + 1; i < right; i++)
			{
				int src = symmetry.charAt(i) - '0';
				int dest = symmetry.charAt(i + 1) - '0';
				if (i == right - 1)
					dest = symmetry.charAt(left + 1) - '0';

				mapc[src - 1] = dest;
			}
			list.add(new Symmetry(mapc));

			left = symmetry.indexOf("(", right);
			right = symmetry.indexOf(")", left);
		}

		if (list.size() == 0)
			throw new IllegalArgumentException("no symmetries defined");

		Symmetry product = list.get(list.size() - 1);

		for (int i = list.size() - 2; i >= 0; i--)
		{
			product = product.multOnRightOf(list.get(i));
		}

		this.map = new int[Sn];
		for (int i = 0; i < Sn; i++)
			this.map[i] = product.map[i];
	}

	public String toString()
	{
		TreeSet<Integer> used = new TreeSet<Integer>();
		for (int i = 1; i <= map.length; i++)
			used.add(i);

		String ret = "";
		while (used.size() > 0)
		{
			int start = used.first();
			used.remove(start);
			int last = start;

			String tmp = "(";
			int next = map[start - 1];
			while (next != start)
			{
				tmp += last;
				last = next;
				used.remove(next);
				next = map[last - 1];
			}
			tmp += last + ")";

			if (tmp.length() > 3)
				ret += tmp;
		}

		if (ret.length() == 0)
			ret = "()";
		return ret;
	}

	public Symmetry inverse()
	{
		Symmetry ret = new Symmetry();
		ret.map = new int[map.length];

		for (int i = 0; i < map.length; i++)
			ret.map[map[i] - 1] = i + 1;

		return ret;
	}

	public Symmetry multOnRightOf(Symmetry left)
	{
		if (this.map.length != left.map.length)
			throw new IllegalArgumentException("Not in the same Symmetric group");

		Symmetry ret = new Symmetry();
		ret.map = new int[map.length];

		for (int i = 0; i < ret.map.length; i++)
			ret.map[i] = left.map[this.map[i] - 1];

		return ret;
	}

	public boolean equals(Object other)
	{
		if (other == null || !(other instanceof Symmetry))
			return false;

		boolean ret = true;
		Symmetry othr = (Symmetry) other;

		int i = 0;
		while (ret && i < map.length)
			ret = ret && othr.map[i] == map[i++];

		return ret;
	}

	public Symmetry multOnLeftOf(Symmetry right)
	{
		return right.multOnRightOf(this);
	}

	public boolean isOdd()
	{
		return !isEven();
	}

	public boolean isEven()
	{
		String s = this.toString();
		if (s.equals("()"))
			return true;

		int length = 0;

		int left = s.indexOf("(");
		int right = s.indexOf(")", left);
		while (left != -1 && right != -1)
		{
			length += right - left;
			left = s.indexOf("(", right);
			right = s.indexOf(")", left);
		}

		return length % 2 == 0;
	}

	/*
	 * private List<Integer> permute(List<Integer> a)
	 * {
	 * 
	 * 
	 * return ret;
	 * }
	 * 
	 * 
	 * 
	 * public static TreeSet<Symmetry> produceSn(int n)
	 * {
	 * TreeSet<ArrayList<Symmetry>> ret = new TreeSet<ArrayList<Symmetry>>();
	 * 
	 * // TreeSet<Integer> shelf = new TreeSet<Integer>();
	 * // for(int i=1;i<n;i++)
	 * // shelf.add(i);
	 * ArrayList<Integer> shelf = new ArrayList<Integer>();
	 * 
	 * 
	 * 
	 * // shelf.iterator()
	 * 
	 * 
	 * 
	 * return null;
	 * // return ret;
	 * }
	 */

	private static void permutate(Set<Integer> available, ArrayList<Integer> curPermutation, Set<List<Integer>> store)
	{
		//if nothing available then store current permutation and return
		if (available.size() == 0)
		{
			store.add(cloneList(curPermutation));
			return;
		}

		Iterator<Integer> itr = available.iterator();
		while (itr.hasNext())
		{
			//add to permutation
			int cur = itr.next();
			curPermutation.add(cur);

			//remove from available set
			Set<Integer> nowAvailable = cloneSet(available);
			nowAvailable.remove(cur);

			//remove from permutation and loop again
			permutate(nowAvailable, curPermutation, store);
			if (curPermutation.size() != 0)
				curPermutation.remove(curPermutation.size() - 1);
		}
	}

	/**
	 * groupToCreate
	 * 0 sN
	 * 1 aN
	 * 2 sN/aN
	 */
	private static Set<Symmetry> createGroup(int n, int groupToCreate)
	{
		//Set<List<Integer>> 
		Set<List<Integer>> sN = (Set<List<Integer>>) new TreeSet<List<Integer>>(new Comparator<List<Integer>>()
		{
			@Override
			public int compare(List<Integer> o1, List<Integer> o2)
			{
				return o1.toString().compareTo(o2.toString());
			}
		});

		if (n < 1)
			throw new IllegalArgumentException("n must be positive");

		Set<Integer> available = new TreeSet<Integer>();
		for (int i = 1; i <= n; i++)
			available.add(i);

		ArrayList<Integer> curPermutation = new ArrayList<Integer>();
		permutate(available, curPermutation, sN);

		TreeSet<Symmetry> ret = new TreeSet<Symmetry>();

		for (List<Integer> next : sN)
		{
			int arr[] = new int[n];
			for (int i = 0; i < n; i++)
				arr[i] = next.get(i);

			Symmetry s = new Symmetry(arr);
			if (groupToCreate == 0 || (groupToCreate == 1 && s.isEven()) || (groupToCreate == 2 && s.isOdd()))
				ret.add(s);
		}

		return ret;
	}

	public static Set<Symmetry> createSymmetricGroup(int n)
	{
		return createGroup(n, 0);
	}

	public static Set<Symmetry> createAlternatingGroup(int n)
	{
		return createGroup(n, 1);
	}

	public static Set<Symmetry> createAntiAlternatingGroup(int n)
	{
		return createGroup(n, 2);
	}

	public static Set<Symmetry> createSymmetricSet(String arr[], int n)
	{
		Set<Symmetry> ret = new TreeSet<Symmetry>();
		for (String s : arr)
			ret.add(new Symmetry(s, n));

		return ret;
	}

	//returns true if n is normal in g
	public static boolean hIsNormalInG(Set<Symmetry> h, Set<Symmetry> g)
	{
		boolean normal = true;
		for (Symmetry k : h)
		{
			for (Symmetry gg : g)
			{
				Symmetry gi = gg.inverse();
				Symmetry product = gi.multOnRightOf(k).multOnRightOf(gg);
				if (!h.contains(product))
					normal = false;
			}
		}

		return normal;
	}

	public Set<Symmetry> generateCyclicGroup()
	{
		Set<Symmetry> ret = new TreeSet<Symmetry>();

		Symmetry product = this;
		ret.add(product);
		while (!product.toString().equals("()"))
		{
			product = this.multOnRightOf(product);
			ret.add(product);
		}
		return ret;
	}

	public static boolean isSetASymmetricGroup(Set<Symmetry> set)
	{
		boolean closure = true;
		boolean identity = false;

		for (Symmetry g1 : set)
		{
			if (closure)
				for (Symmetry g2 : set)
				{
					Symmetry product1 = g1.multOnRightOf(g2);
					Symmetry product2 = g2.multOnRightOf(g1);
					if (!set.contains(product1) || !set.contains(product2))
					{
						println(g1);
						println(g2);
						println(product1);
						println(product2);
						closure = false;
						break;
					}
				}

			if (g1.toString().equals("()"))
				identity = true;
		}

		return identity && closure;
	}

	public static Set<Symmetry> generateFromSet(Set<Symmetry> set)
	{
		Set<Symmetry> ret = new TreeSet<Symmetry>();
		for (Symmetry s1 : set)
		{
			ret.add(s1);
			for (Symmetry s2 : set)
			{
				Symmetry p1 = s1.multOnLeftOf(s2);
				Symmetry p2 = s2.multOnLeftOf(s1);
				ret.add(p1);
				ret.add(p2);
			}
		}

		if (isSetASymmetricGroup(ret))
			return ret;
		else
			return generateFromSet(ret);
	}

	public static void main(String[] args)
	{

		int N = 5;
		Set<Symmetry> sN = createSymmetricGroup(N);
		Set<Symmetry> aN = createAlternatingGroup(N);
		Set<Symmetry> naN = createAntiAlternatingGroup(N);
		Set<Symmetry> naNwithe = createAntiAlternatingGroup(N);
		
		naNwithe.add(new Symmetry("()", N));
		
		
		println(isSetASymmetricGroup(naNwithe));
		
		String arrk4[] = { "()", "(12)(34)", "(13)(24)", "(14)(23)" };
		String arrk5[] = { "()", "(12)(34)", "(12)(35)", "(12)(45)", "(13)(24)", "(13)(25)", "(13)(45)", "(14)(23)", "(14)(25)", "(14)(35)", "(15)(23)", "(15)(24)", "(15)(34)",
				"(23)(45)", "(24)(35)", "(25)(34)" };

		Set<Symmetry> k4 = createSymmetricSet(arrk4, 5);
		Set<Symmetry> k5 = createSymmetricSet(arrk5, 5);
		
		println("   S" + N + " = " + sN);
		println("   A" + N + " = " + aN);
		println("S" + N + "\\A" + N + " = " + naN);
		println("S" + N + "\\A" + N + " U {()} = " + naNwithe);

		println("   K4 = " + k4);
		println("   K5 = " + k5);

		println();
		println("K5 normal in S" + N + " " + hIsNormalInG(k5, sN));
		println("K5 normal in A" + N + " " + hIsNormalInG(k5, aN));
		println("K5 normal in S" + N + "\\A" + N + " " + hIsNormalInG(k5, naN));

		Symmetry sym = new Symmetry("(15)(23)", N);
		Set<Symmetry> mySet = sym.generateCyclicGroup();
		sym = new Symmetry("(45)", N);
		mySet.add(sym);
		sym = new Symmetry("(34)", N);
		mySet.add(sym);
		
		mySet = generateFromSet(createSymmetricSet(arrk4, 5));

		println("mySet = " + mySet);

		println("mySet is agroup = " + isSetASymmetricGroup(mySet));
		println("mySet size = " + mySet.size());

		println("K4 is a group = " + isSetASymmetricGroup(k4));
		println("S" + N + "\\A" + N + " is a group = " + isSetASymmetricGroup(naN));

		println("S" + N + "\\A" + N + " U {()} is a group = " + isSetASymmetricGroup(naNwithe));
		//Set<Symmetry> a4 = createSymmetricGroup(N);
		String arr2[] = { "(123)", "(132)" };

		Symmetry s123 = new Symmetry("(12)(31)", N);

		Set<Symmetry> s123cyclicGroup = s123.generateCyclicGroup();
		println(s123cyclicGroup);
		println(hIsNormalInG(s123cyclicGroup, aN));
		//		println(s123.generateCyclicGroup());

	}
}
