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
	
	
	public boolean isNormalInG(SymmetricSet G)
	{
		boolean normal = true;
		for (Symmetry k : this)
		{
			for (Symmetry g : G)
			{
				
				Symmetry product = k.multOnLeftOf(g.inverse());
				product = g.multOnLeftOf(product);
				if (!this.contains(product))
					normal = false;

			}
		}
		return normal;
	}
	
	public SymmetricSet generateGroup()
	{
		SymmetricSet ret = new SymmetricSet();
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

		if (this.isAGroup())
			return (SymmetricSet)ret;
		else
			return ret.generateFromSet();
	}
	
	
	public SymmetricSet generateFromSet()
	{
		SymmetricSet ret = new SymmetricSet();
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
			return ret.generateFromSet();
	}		
	
	
	public static SymmetricSet createSymmetricSet(String arr[], int n)
	{
		SymmetricSet ret = new SymmetricSet();
		for (String s : arr)
			ret.add(new Symmetry(s, n));
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
	private static SymmetricSet createGroup(int n, int groupToCreate)
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

		SymmetricSet ret =  new SymmetricSet();

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
	
	public SymmetricSet leftCoset(Symmetry g)
	{
		SymmetricSet ret = new SymmetricSet();
		
		for(Symmetry s :this)
			ret.add(g.multOnLeftOf(s));

		return ret;
	}
	
	public SymmetricSet rightCoset(Symmetry g)
	{
		SymmetricSet ret = new SymmetricSet();
		
		for(Symmetry s :this)
			ret.add(g.multOnRightOf(s));

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
		return  createSymmetricSet(arrk4, 4);
	}


	public static SymmetricSet createK5()
	{
		String arrk5[] = { "()", "(12)(34)", "(12)(35)", "(12)(45)", "(13)(24)", "(13)(25)", "(13)(45)", "(14)(23)", "(14)(25)", "(14)(35)", "(15)(23)", "(15)(24)", "(15)(34)",
				"(23)(45)", "(24)(35)", "(25)(34)" };
		return  createSymmetricSet(arrk5, 5);
	}

	
	public static SymmetricSet createD4()
	{
		int n=4;
		String sr1 = "(";
		for(int i=1;i<=n;i++)
			sr1+=i;
		sr1+=")";
		
		Symmetry r1 = new Symmetry(sr1, n);
		Symmetry s1 = new Symmetry("(12)(34)", n);
		SymmetricSet set = new SymmetricSet();
		set.add(r1);
		set.add(s1);
		return set.generateFromSet();
	}

	public static void main(String[] args)
	{
		SymmetricSet D4 = SymmetricSet.createD4();
		SymmetricSet K4 = SymmetricSet.createK4();
		SymmetricSet S4 = SymmetricSet.createSymmetricGroup(4);
		
		
		
		Symmetry h = new Symmetry("(12)",4);
		SymmetricSet H = h.generateCyclicGroup();
		
		for(Symmetry g : S4)
		{
			Symmetry product = h.multOnLeftOf(g.inverse());
			product = g.multOnLeftOf(product);
			H.add(product);
		}
		
		for(Symmetry g: S4)
		{
			SymmetricSet leftCoset = H.leftCoset(g);
			SymmetricSet rightCoset = H.rightCoset(g);
			println (leftCoset);
			println(rightCoset);
		}
		println(H.isNormalInG(S4));

		/*

		
		
		
		println(S4.isNormalInG(K4));
		println(D4.isAGroup());*/
		

	}	
	
}
