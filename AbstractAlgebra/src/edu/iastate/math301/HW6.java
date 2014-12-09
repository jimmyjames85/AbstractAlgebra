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
