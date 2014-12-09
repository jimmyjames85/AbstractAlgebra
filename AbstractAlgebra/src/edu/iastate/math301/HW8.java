package edu.iastate.math301;

import static edu.iastate.math301.util.Util.*;

import java.util.List;
import java.util.TreeSet;

public class HW8
{

	public static TreeSet<Integer> convertToCycleNotation(List<Integer> p)
	{
		TreeSet<Integer> ret = new TreeSet<Integer>();
		String  cycle = "(1";
		int cycleStart = 1;
		
		for(int i=1;i<=p.size();i++)
		{
			int val = p.get(i-1);
			
			if(cycle.endsWith(")"))
				cycleStart=val;
			
			if(i!=val)
			{
				if(val==cycleStart)//close cycle
				{
					cycle+=")";
				}
				else
					cycle+=val;
			}
				
		}
		
		
		
		println(cycle);
		
		return ret;
	}
	public static void main(String args[])
	{
		
		List<Integer> p = (List<Integer>)createSymmetricGroup(4).toArray()[3];
		println(p);
		println (convertToCycleNotation(p));
	}
}
