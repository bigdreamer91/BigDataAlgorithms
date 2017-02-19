import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

public class QueryProcessor
{
	String folder;
	PositionalIndex posIndex;
	HashMap<String,Double> topKDocsNamesRelevance;

	public QueryProcessor(String folder)
	{
		this.folder = folder;
		this.posIndex = new PositionalIndex(folder);
	}

	// using relevance as score
	public ArrayList<String> topKDocs(String query, int k)
	{
		ArrayList<String> topKDocNames = new ArrayList<String>();
		ArrayList<DocNameRelevancePair> topKDocNameRelevancePair = new ArrayList<DocNameRelevancePair>();
		topKDocsNamesRelevance = new HashMap<String,Double>();
		topKDocNameRelevancePair = getDocNameRelevancePairWithRelevance(query, k);
		k = Math.min(k, topKDocNameRelevancePair.size());
		for (int i = 0; i < k; i++)
		{
			topKDocNames.add(topKDocNameRelevancePair.get(i).getDocName());
			topKDocsNamesRelevance.put(topKDocNameRelevancePair.get(i).getDocName(),topKDocNameRelevancePair.get(i).getRelevance());
		}
		return topKDocNames;
	}
	
	public double TPScore(String query, String doc){
		return posIndex.TPScore(query, doc);
	}
	
	public double VSScore(String query, String doc){
		return posIndex.VSScore(query, doc);
	}
	
	public double RelevanceScore(String doc){
		return topKDocsNamesRelevance.get(doc);
	}
	
	// return top k using Relevance score
	public ArrayList<DocNameRelevancePair> getDocNameRelevancePairWithRelevance(String query, int k)
	{
		ArrayList<DocNameRelevancePair> allDocNameRelevancePair = new ArrayList<DocNameRelevancePair>();
		for (String docName : posIndex.fileNameAndFileContentMap.keySet())
		{
			allDocNameRelevancePair.add(new DocNameRelevancePair(docName, posIndex.Relevance(query, docName)));
		}
		
		Collections.sort(allDocNameRelevancePair, new RelevanceCompare());
		
		ArrayList<DocNameRelevancePair> topKDocNameRelevancePair = new ArrayList<DocNameRelevancePair>();
		k = Math.min(k, allDocNameRelevancePair.size());
		for (int i = 0; i < k; i++)
		{
			topKDocNameRelevancePair.add(allDocNameRelevancePair.get(i));
		}
		return topKDocNameRelevancePair;
	}
	
	// return top k using TPScore
	public ArrayList<DocNameRelevancePair> getDocNameRelevancePairWithTPScore(String query, int k)
	{
		ArrayList<DocNameRelevancePair> allDocNameRelevancePair = new ArrayList<DocNameRelevancePair>();
		for (String docName : posIndex.fileNameAndFileContentMap.keySet())
		{
			allDocNameRelevancePair.add(new DocNameRelevancePair(docName, posIndex.TPScore(query, docName)));
		}
		
		Collections.sort(allDocNameRelevancePair, new RelevanceCompare());
		
		ArrayList<DocNameRelevancePair> topKDocNameRelevancePair = new ArrayList<DocNameRelevancePair>();
		k = Math.min(k, allDocNameRelevancePair.size());
		for (int i = 0; i < k; i++)
		{
			topKDocNameRelevancePair.add(allDocNameRelevancePair.get(i));
		}
		return topKDocNameRelevancePair;
	}
	
	// return top k using VSScore
	public ArrayList<DocNameRelevancePair> getDocNameRelevancePairWithVSScore(String query, int k)
	{
		ArrayList<DocNameRelevancePair> allDocNameRelevancePair = new ArrayList<DocNameRelevancePair>();
		for (String docName : posIndex.fileNameAndFileContentMap.keySet())
		{
			allDocNameRelevancePair.add(new DocNameRelevancePair(docName, posIndex.VSScore(query, docName)));
		}
		
		Collections.sort(allDocNameRelevancePair, new RelevanceCompare());
		
		ArrayList<DocNameRelevancePair> topKDocNameRelevancePair = new ArrayList<DocNameRelevancePair>();
		k = Math.min(k, allDocNameRelevancePair.size());
		for (int i = 0; i < k; i++)
		{
			topKDocNameRelevancePair.add(allDocNameRelevancePair.get(i));
		}
		return topKDocNameRelevancePair;
	}
	

	public class RelevanceCompare implements Comparator<DocNameRelevancePair>
	{
		@Override
		public int compare(DocNameRelevancePair o1, DocNameRelevancePair o2)
		{
			if (o2.getRelevance() > o1.getRelevance())
			{
				return 1;
			} 
			else if (o2.getRelevance() < o1.getRelevance())
			{
				return -1;
			}
			return 0;
		}

	}
	
	public class DocNameRelevancePair
	{
		String docName;
		double relevance;
		
		public DocNameRelevancePair(String docName, double relevance)
		{
			this.docName = docName;
			this.relevance = relevance;
		}
		
		public String getDocName()
		{
			return docName;
		}

		public double getRelevance()
		{
			return relevance;
		}
			

	} 

	public static void main(String[] args)
	{
		String folder = "/Users/geethanjalijeevanatham/Downloads/IR";
		int k = 10;
		QueryProcessor queryProc = new QueryProcessor(folder);
		ArrayList<String> topKDocNames;
		String query1;
		
		query1 = "ball";
		topKDocNames = queryProc.topKDocs(query1, k);
		System.out.println("following is one word case for " + query1 + ": \ntop " + k + " doc names relevent with");
		for (String doc : topKDocNames)
		{
			System.out.println(doc);
			System.out.println("TPScore " + queryProc.TPScore(query1, doc));
			System.out.println("VSScore " + queryProc.VSScore(query1, doc));
			System.out.println("Relevance " + queryProc.RelevanceScore(doc));
		}
		System.out.println();
		
		query1 = "ball teams";
		topKDocNames = queryProc.topKDocs(query1, k);
		System.out.println("following is two words case for " + query1 + ": \ntop " + k + " doc names relevent with");
		for (String doc : topKDocNames)
		{
			System.out.println(doc);
			System.out.println("TPScore " + queryProc.TPScore(query1, doc));
			System.out.println("VSScore " + queryProc.VSScore(query1, doc));
			System.out.println("Relevance " + queryProc.RelevanceScore(doc));
		}
		System.out.println();
		
		query1 = "ball teams america";
		topKDocNames = queryProc.topKDocs(query1, k);
		System.out.println("following is three words case for " + query1 + ": \ntop " + k + " doc names relevent with");
		for (String doc : topKDocNames)
		{
			System.out.println(doc);
			System.out.println("TPScore " + queryProc.TPScore(query1, doc));
			System.out.println("VSScore " + queryProc.VSScore(query1, doc));
			System.out.println("Relevance " + queryProc.RelevanceScore(doc));
		}
		System.out.println();
		
		query1 = "associate ball teams america professional league";
		topKDocNames = queryProc.topKDocs(query1, k);
		System.out.println("following is more three words case for " + query1 + ": \ntop " + k + " doc names relevent with");
		for (String doc : topKDocNames)
		{
			System.out.println(doc);
			System.out.println("TPScore " + queryProc.TPScore(query1, doc));
			System.out.println("VSScore " + queryProc.VSScore(query1, doc));
			System.out.println("Relevance " + queryProc.RelevanceScore(doc));
		}
		System.out.println();
		
		// include of propositions in more than three words query
		query1 = "championship of Chicago Association game";
		topKDocNames = queryProc.topKDocs(query1, k);
		System.out.println("following is more three words case for " + query1 + ": \ntop " + k + " doc names relevent with");
		for (String doc : topKDocNames)
		{
			System.out.println(doc);
			System.out.println("TPScore " + queryProc.TPScore(query1, doc));
			System.out.println("VSScore " + queryProc.VSScore(query1, doc));
			System.out.println("Relevance " + queryProc.RelevanceScore(doc));
		}
		System.out.println();
	}
}
