package edu.iastate.math301;

import static edu.iastate.math301.util.Util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class HW6
{
	private static Set<Integer> cloneSet(Set<Integer> src)
	{
		Set<Integer> ret = new TreeSet<Integer>();

		for (int i : src)
			ret.add(i);

		return ret;
	}

	private static List<Integer> cloneList(List<Integer> src)
	{
		ArrayList<Integer> ret = new ArrayList<Integer>();

		for (int i : src)
			ret.add(i);

		return ret;
	}

	/*
	 * private static void generateSubsets(SymmetricGroup sN) {
	 * for(List<Integer> x: sN) { boolean go=true; while(go) {
	 * 
	 * } } }
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

	public static Set<List<Integer>> createSymmetricGroup(int n)
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

		return sN;
	}

	public static void main(String[] args)
	{

		int n = 5;

		Set<List<Integer>> sN = createSymmetricGroup(n);

		int count = 0;
		for (List<Integer> perm : sN)
		{
			count++;
			
			print(perm + "\t");
			if (count % n == 0)
				println();
		}

		println();
		println(factorial(n));

	}
}
