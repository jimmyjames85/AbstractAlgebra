package edu.iastate.math301.util;

import static edu.iastate.math301.util.Util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SymmetricSet extends TreeSet<Symmetry>
{

	private static final long serialVersionUID = 1L;

	private int permutationSize;

	public SymmetricSet(int permutationSize)
	{
		super();
		this.permutationSize = permutationSize;
	}

	public int getPermutationSize()
	{
		return permutationSize;
	}

	public boolean isAbelian()
	{
		boolean abelian = true;
		for (Symmetry g1 : this)
		{
			for (Symmetry g2 : this)
			{
				Symmetry p1 = g1.multOnLeftOf(g2);
				Symmetry p2 = g2.multOnLeftOf(g1);

				if (!(p1.equals(p2)))
				{
					/*
					 * println(g1 + " " + g2 + " =" + p1);
					 * println(g2 + " " + g1 + " =" + p2);
					 * println();
					 */
					abelian = false;
				}
			}
		}

		return abelian;

	}

	public boolean isAGroup()
	{
		boolean closure = true;
		boolean identity = false;

		for (Symmetry g1 : this)
		{
			if (closure)
				for (Symmetry g2 : this)
				{
					Symmetry product1 = g1.multOnRightOf(g2);
					Symmetry product2 = g2.multOnRightOf(g1);
					if (!this.contains(product1) || !this.contains(product2))
					{
						closure = false;
						break;
					}
				}

			if (g1.toString().equals("()"))
				identity = true;
		}

		return identity && closure;
	}

	public boolean isNormalSubgroupInG(SymmetricSet G)
	{
		return isNormalInS(G) && isAGroup();
	}

	public SymmetricSet center()
	{
		SymmetricSet ret = new SymmetricSet(permutationSize);

		for (Symmetry x : this)
		{
			boolean xBelongs = true;

			for (Symmetry g : this)
			{
				Symmetry product1 = g.multOnRightOf(x);
				Symmetry product2 = x.multOnRightOf(g);

				if (!product1.equals(product2))
				{
					//println(product1 + " " + product2);
					//println(product1 + " " + g);
					xBelongs = false;
				}

				if (!xBelongs)
					break;
			}

			if (xBelongs)
				ret.add(x);
		}

		return ret;
	}

	public boolean isNormalInS(SymmetricSet S)
	{
		boolean normal = true;
		for (Symmetry k : this)
		{
			for (Symmetry g : S)
			{

				Symmetry product = k.multOnLeftOf(g.inverse());
				product = g.multOnLeftOf(product);
				if (!this.contains(product))
					normal = false;

			}
		}
		return normal;
	}

	public Symmetry identity()
	{
		return new Symmetry("()", permutationSize);
	}

	public boolean isAGSet(Set<Integer> X)
	{

		Symmetry e = identity();
		for (int x : X)
		{
			//first check
			if (e.actOn(x) != x)
				return false;
			for (Symmetry g1 : this)
			{
				for (Symmetry g2 : this)
				{
					//second check
					Symmetry g1g2 = g1.multOnLeftOf(g2);
					int a1 = g1g2.actOn(x);
					int a2 = g1.actOn(g2.actOn(x));
					if (a1 != a2)
						return false;
				}
			}
		}
		return true;
	}

	public SymmetricSet generateGroup()
	{
		SymmetricSet ret = new SymmetricSet(permutationSize);
		for (Symmetry s1 : this)
		{
			ret.add(s1);
			for (Symmetry s2 : this)
			{
				Symmetry p1 = s1.multOnLeftOf(s2);
				Symmetry p2 = s2.multOnLeftOf(s1);
				ret.add(p1);
				ret.add(p2);
			}
		}

		if (ret.isAGroup())
			return ret;
		else
			return ret.generateGroup();
	}

	public static SymmetricSet createSymmetricSet(String arr[], int permutationSize)
	{
		SymmetricSet ret = new SymmetricSet(permutationSize);
		for (String s : arr)
			ret.add(new Symmetry(s, permutationSize));
		return ret;
	}

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
	private static SymmetricSet createGroup(int permutationSize, int groupToCreate)
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

		if (permutationSize < 1)
			throw new IllegalArgumentException("n must be positive");

		Set<Integer> available = new TreeSet<Integer>();
		for (int i = 1; i <= permutationSize; i++)
			available.add(i);

		ArrayList<Integer> curPermutation = new ArrayList<Integer>();
		permutate(available, curPermutation, sN);

		SymmetricSet ret = new SymmetricSet(permutationSize);

		for (List<Integer> next : sN)
		{
			int arr[] = new int[permutationSize];
			for (int i = 0; i < permutationSize; i++)
				arr[i] = next.get(i);

			Symmetry s = new Symmetry(arr);
			if (groupToCreate == 0 || (groupToCreate == 1 && s.isEven()) || (groupToCreate == 2 && s.isOdd()))
				ret.add(s);
		}

		return ret;
	}

	public SymmetricSet leftCoset(Symmetry g)
	{
		SymmetricSet ret = new SymmetricSet(permutationSize);

		for (Symmetry s : this)
			ret.add(g.multOnLeftOf(s));

		return ret;
	}

	public SymmetricSet rightCoset(Symmetry g)
	{
		SymmetricSet ret = new SymmetricSet(permutationSize);

		for (Symmetry s : this)
			ret.add(g.multOnRightOf(s));

		return ret;
	}

	public String info(SymmetricSet P)
	{

		String isAbelian = "f";
		if (isAbelian())
			isAbelian = "t";

		String isNormalSubroupInG = "f";
		if (isNormalSubgroupInG(P))
			isNormalSubroupInG = "t";

		String isNormalInS = "f";
		if (isNormalInS(P))
			isNormalInS = "t";

		String isAGroup = "f";
		if (isAGroup())
			isAGroup = "t";

		String ret = "isAbelian:" + isAbelian + " isGroup:" + isAGroup + " isNrmlSubG(P):" + isNormalSubroupInG + " isNrmlInP:" + isNormalInS;
		return ret;

	}

	public static SymmetricSet createSymmetricGroup(int n)
	{
		return createGroup(n, 0);
	}

	public static SymmetricSet createAlternatingGroup(int n)
	{
		return createGroup(n, 1);
	}

	public static SymmetricSet createAntiAlternatingGroup(int n)
	{
		return createGroup(n, 2);
	}

	public static SymmetricSet createK4()
	{
		String arrk4[] = { "()", "(12)(34)", "(13)(24)", "(14)(23)" };
		return createSymmetricSet(arrk4, 4);
	}

	public static SymmetricSet createK5()
	{
		String arrk5[] = { "()", "(12)(34)", "(12)(35)", "(12)(45)", "(13)(24)", "(13)(25)", "(13)(45)", "(14)(23)", "(14)(25)", "(14)(35)", "(15)(23)", "(15)(24)", "(15)(34)",
				"(23)(45)", "(24)(35)", "(25)(34)" };
		return createSymmetricSet(arrk5, 5);
	}

	public static SymmetricSet createD4()
	{
		int n = 4;
		String sr1 = "(";
		for (int i = 1; i <= n; i++)
			sr1 += i;
		sr1 += ")";

		Symmetry r1 = new Symmetry(sr1, n);
		Symmetry s1 = new Symmetry("(12)(34)", n);
		SymmetricSet set = new SymmetricSet(n);
		set.add(r1);
		set.add(s1);
		return set.generateGroup();
	}

	public static SymmetricSet createD3()
	{
		int n = 3;
		String sr1 = "(";
		for (int i = 1; i <= n; i++)
			sr1 += i;
		sr1 += ")";

		Symmetry r1 = new Symmetry(sr1, n);
		Symmetry s1 = new Symmetry("(12)", n);
		SymmetricSet set = new SymmetricSet(n);
		set.add(r1);
		set.add(s1);
		return set.generateGroup();
	}

	public boolean add(String symmetry)
	{
		return this.add(new Symmetry(symmetry, this.permutationSize));
	}
	public SymmetricSet stabilizerOf(int x)
	{
		SymmetricSet ret = new SymmetricSet(permutationSize);

		for (Symmetry s : this)
		{
			if (s.actOn(x) == x)
				ret.add(s);
		}

		return ret;
	}

	public Set<Integer> orbitOf(int n)
	{
		TreeSet<Integer> ret = new TreeSet<Integer>();

		for (Symmetry s : this)
			ret.add(s.actOn(n));

		return ret;
	}

	public static SymmetricSet generateCyclicGroup(Symmetry s)
	{
		return s.generateCyclicGroup();
	}

	public static SymmetricSet generateCyclicGroup(String s, int n)
	{
		return (new Symmetry(s, n)).generateCyclicGroup();
	}

	public static void end()
	{
		System.exit(1);
	}

	public static void main(String[] args)
	{
		
		SymmetricSet D3 = SymmetricSet.createD3();
		SymmetricSet D4 = SymmetricSet.createD4();
		SymmetricSet K4 = SymmetricSet.createK4();

		SymmetricSet S3 = SymmetricSet.createSymmetricGroup(3);
		SymmetricSet S4 = SymmetricSet.createSymmetricGroup(4);
		SymmetricSet S5 = SymmetricSet.createSymmetricGroup(5);
		SymmetricSet S6 = SymmetricSet.createSymmetricGroup(6);
		SymmetricSet A4 = SymmetricSet.createAlternatingGroup(4);
		//String arr[] = { "()", "(123)", "(132)", "(45)", "(123)(45)", "(132)(45)" };
		//String arr[]= {"()", "(12)(3456)", "(35)(46)", "(12)(3654)"};
		
		
		
		String arr[]= {"()","(12)","(345)","(354)","(12)(345)","(12)(354)"};
		SymmetricSet G = SymmetricSet.createSymmetricSet(arr, 6);

		G = new SymmetricSet(6);
		G.add("(12)");
		G.add("(345)");
		G = G.generateGroup();
		println (G);
		
		
		for (int i=1;i<=G.getPermutationSize();i++)
			println(G.orbitOf(i));

/*
		println();
		for (Symmetry g : G)
			println(g + "       " + g.fixedPointSet());
*/
		/*
		 * 
		 * G = SymmetricSet.createSymmetricSet(arr2, 6);
		 * println(G);
		 * println(G.info(S6));
		 * println(G.center());
		 */

	}
}
