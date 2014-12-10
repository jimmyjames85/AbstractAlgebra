package edu.iastate.math301.util;

import java.util.ArrayList;
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
	
	public int getN()
	{
		return map.length;
	}
	
	public Set<Integer> fixedPointSet()
	{
		TreeSet<Integer> ret = new TreeSet<Integer>();
		
		for(int i=1;i<=map.length;i++)
		{
			if(actOn(i)==i)
				ret.add(i);
		}
		return ret;
	}	
	public int actOn(int n)
	{
		if(n<=0 || n>this.map.length)
			throw new IllegalArgumentException();
		
		return this.map[n-1];
	}

	public Symmetry(int map[])
	{
		this.map = new int[map.length];
		for (int i = 0; i < map.length; i++)
			this.map[i] = map[i];
	}

	public Symmetry(String symmetry, int permutationSize)
	{
		symmetry = symmetry.replaceAll(" ", "");

		ArrayList<Symmetry> list = new ArrayList<Symmetry>();

		int left = symmetry.indexOf("(");
		int right = symmetry.indexOf(")", left);
		while (left != -1 && right != -1 && right > left)
		{
			int mapc[] = new int[permutationSize];
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

		this.map = new int[permutationSize];
		for (int i = 0; i < permutationSize; i++)
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

	public SymmetricSet generateCyclicGroup()
	{
		SymmetricSet ret = new SymmetricSet(map.length);

		Symmetry product = this;
		ret.add(product);
		while (!product.toString().equals("()"))
		{
			product = this.multOnRightOf(product);
			ret.add(product);
			
			
		}
		return ret;
	}

}
