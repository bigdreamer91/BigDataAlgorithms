import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import javax.print.attribute.Size2DSyntax;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

public class WeightedQueue
{
	LinkedList<Tuple> weightQueue;
	HashMap<String, Double> urls;

	public WeightedQueue()
	{
		weightQueue = new LinkedList<>();
		urls = new HashMap<String, Double>();
	}

	public boolean isEmpty()
	{
		return weightQueue.size() == 0;
	}

	public boolean add(Tuple newTuple)
	{
//		if (contains(newTuple) == false)
		if (urls.containsKey(newTuple.getUrl()) == false)
		{
			
			if (weightQueue.isEmpty())
			{
				weightQueue.add(newTuple);
				urls.put(newTuple.getUrl(), newTuple.getWeight());
				return true;
			}
			else
			{				
				int index = findProperIndex(newTuple);
//				System.err.println(index);
				weightQueue.add(index, newTuple);
				urls.put(newTuple.getUrl(), newTuple.getWeight());
				return true;
			}		
		} 
		else
		{
			return false;
		}
	}

	public int findProperIndex(Tuple newTuple)
	{
		for (int i = weightQueue.size()-1; i >= 0; i--)
		{
			if (newTuple.getWeight() > weightQueue.get(i).getWeight())
			{
				continue;
			}
			return i+1;
		}
		return 0;
	}
	
	public Tuple extract()
	{
		return weightQueue.poll();
	}

	public int size()
	{
		return weightQueue.size();
	}
	
	public boolean contains(Tuple newTuple)
	{
		for (Tuple tuple : weightQueue)
		{
			if (tuple.getUrl().equals(newTuple.getUrl()))
			{
				return true;
			}
		}
		return false;
	}

	// for test
	public static void main(String[] args)
	{
		
		WeightedQueue weightedQueue = new WeightedQueue();
		weightedQueue.add(new Tuple("www.54.com", 55));
		weightedQueue.add(new Tuple("www.1.com", 100));
		weightedQueue.add(new Tuple("www.54.com", 54));
		weightedQueue.add(new Tuple("www.6.c11om", 59));
		weightedQueue.add(new Tuple("www.6.com", 6));
		weightedQueue.add(new Tuple("www.77.c0011om", 59));
		weightedQueue.add(new Tuple("www.97.c0011om", 59));
		weightedQueue.add(new Tuple("www.00000", 59));
		weightedQueue.add(new Tuple("www.122.com", 100));
		weightedQueue.add(new Tuple("www.122.com", 100));
		while (!weightedQueue.isEmpty())
		{
			Tuple tmpTuple = weightedQueue.extract();
			System.out
					.println(tmpTuple.getUrl() + ",\t" + tmpTuple.getWeight());
		}
	}
}

