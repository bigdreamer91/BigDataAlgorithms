import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

public class PositionalIndex
{
	String folder;
	ArrayList<ArrayList<String>> allDocsContents;
	
	HashMap<String, ArrayList<String>> fileNameAndFileContentMap; // key is fileName : value is array of words
	ArrayList<String> allTerms;
	HashMap<String, Integer> allTermsIndex; // for fast get the position of a term in feature vector
	HashMap<String, HashMap<String, ArrayList<Integer>>> postingsLists;

	public PositionalIndex(String folder)
	{
		System.err.println("initializing");
		this.folder = folder;
		this.postingsLists = constructPostingsLists();		
		System.err.println("initializing done");
	}

	public HashMap<String, HashMap<String, ArrayList<Integer>>> constructPostingsLists()
	{
		allDocsContents = new ArrayList<>();
		fileNameAndFileContentMap = new HashMap<String, ArrayList<String>>();
		allTerms = new ArrayList<String>();
		allTermsIndex = new HashMap<String, Integer>();
		HashMap<String, HashMap<String, ArrayList<Integer>>> postingsLists = new HashMap<String, HashMap<String, ArrayList<Integer>>>();

		HashMap<String, ArrayList<Integer>> postingForOneTerm;
		ArrayList<Integer> positions;
		ArrayList<String> fileNames = FileNames.getFileNames(folder);
		int count = 0;
		for (String fileName : fileNames)
		{
			//System.out.println(++count + ", " + fileName);
			String content = FileContent.getContent(folder + "/" + fileName);
			// all the words are lower case returned by getTermsFromString
			ArrayList<String> words = FileContent.getTermsFromString(content);
			
			for (int i = 0; i < words.size(); i++)
			{	
				if (!postingsLists.containsKey(words.get(i)))
				{
					postingForOneTerm = new HashMap<String, ArrayList<Integer>>();
					if (!postingForOneTerm.containsKey(fileName))
					{
						positions = new ArrayList<Integer>();
						positions.add(i + 1);
						postingForOneTerm.put(fileName, positions);
					}
					else 
					{
						positions = postingForOneTerm.get(fileName);
						positions.add(i + 1);
						postingForOneTerm.put(fileName, positions);
					}
					postingsLists.put(words.get(i), postingForOneTerm);
				}
				else
				{
					postingForOneTerm = postingsLists.get(words.get(i));
					if (!postingForOneTerm.containsKey(fileName))
					{
						positions = new ArrayList<Integer>();
						positions.add(i + 1);
						postingForOneTerm.put(fileName, positions);
					}
					else 
					{
						positions = postingForOneTerm.get(fileName);
						positions.add(i + 1);
						postingForOneTerm.put(fileName, positions);
					}
					postingsLists.put(words.get(i), postingForOneTerm);					
				}				
			}
			// following two lines for initialize allDocsContents and fileNameAndFileContentMap
			allDocsContents.add(words); //why add all the words, individual terms can be added without repeating words ?
			fileNameAndFileContentMap.put(fileName, words);
		}
		// initialize allTerms
		int index = 0;
		for (String term : postingsLists.keySet())
		{
			allTerms.add(term);
			allTermsIndex.put(term, index++);
		}
		return postingsLists;
	}

	
	
	/* 	
	 * following commented three methods (getPostingsLists, getAllDocsContents
	 	getAllTerms) are written in first round.
		Now these three methods will not be used and their utilities
		are replaced by constructPostingsLists above
	 * *
	 */
	
	/*
	public HashMap<String, HashMap<String, ArrayList<Integer>>> getPostingsLists()
	{
		System.err.println("in getPostingsLists");
		HashMap<String, HashMap<String, ArrayList<Integer>>> postingsLists = new HashMap<String, HashMap<String, ArrayList<Integer>>>();

		HashMap<String, ArrayList<Integer>> postingForOneTerm;
		ArrayList<Integer> postingForOneDocList;
		ArrayList<String> words;
		int count = 0;
		for (String term : allTerms)
		{
			System.out.println(++count + ", " + term);
			postingForOneTerm = new HashMap<String, ArrayList<Integer>>();
			for (String docName : fileNameAndFileContentMap.keySet())
			{
				postingForOneDocList = new ArrayList<Integer>();
				words = fileNameAndFileContentMap.get(docName);
				for (int i = 0; i < words.size(); i++)
				{
					if (term.equalsIgnoreCase(words.get(i)))
					{
						postingForOneDocList.add(i + 1);
					}
				}
				if (postingForOneDocList.size() != 0)
				{
					postingForOneTerm.put(docName, postingForOneDocList);
				}
			}
			postingsLists.put(term, postingForOneTerm);
		}

		return postingsLists;
	}

	public ArrayList<ArrayList<String>> getAllDocsContents()
	{
		System.err.println("in getAllDocsContents");
		ArrayList<ArrayList<String>> allDocsContents = new ArrayList<>();
		fileNameAndFileContentMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> fileNames = FileNames.getFileNames(folder);
		int count = 0;
		for (String fileName : fileNames)
		{
			System.out.println(++count + ", " + fileName);
			String content = FileContent.getContent(folder + "/" + fileName);
			ArrayList<String> words = FileContent.getTermsFromString(content);
			ArrayList<String> termsInDoc = new ArrayList<String>();
			for (String word : words)
			{
				termsInDoc.add(word);
			}
			allDocsContents.add(termsInDoc);
			fileNameAndFileContentMap.put(fileName, termsInDoc);
		}
		System.err.println("getAllDocsContents done");
		System.err.println("doc num is " + allDocsContents.size());
		return allDocsContents;
	}

	public ArrayList<String> getAllTerms()
	{
		System.err.println("in getAllTerms");
		ArrayList<String> allTerms = new ArrayList<String>();
		// this hashSet only for fast check if word in terms arrayList
		HashSet<String> allTermsSet = new HashSet<String>();
		for (ArrayList<String> docContent : allDocsContents)
		{
			for (String word : docContent)
			{
				if (!allTermsSet.contains(word))
				{
					allTerms.add(word);
					allTermsSet.add(word);
				}
			}
		}
		System.err.println("getAllTerms done");
		System.err.println("term num is " + allTerms.size());
		return allTerms;
	}
	*/
	
	
	
	// I suppose doc means Filename not the content of a doc
	public int termFrequency(String term, String doc)
	{
		
//		ArrayList<String> words = fileNameAndFileContentMap.get(doc);
//		return TFIDF.tf(words, term);
		term = term.toLowerCase();		
		HashMap<String, ArrayList<Integer>> postingForOneTerm = null;
		ArrayList<Integer> positions = null;
		if (postingsLists.containsKey(term))
		{
			postingForOneTerm = postingsLists.get(term);
			if (postingForOneTerm.containsKey(doc)) 
			{
				positions = postingForOneTerm.get(doc);	
				return positions.size();
			}
			else 
			{
				return 0;
			}
			
		}
		else
		{
			return 0;
		}
		
	}

	public int docFrequency(String term)
	{
//		return TFIDF.df(allDocsContents, term);
		term = term.toLowerCase();
		HashMap<String, ArrayList<Integer>> postingForOneTerm = null;
		if (postingsLists.containsKey(term))
		{
			postingForOneTerm = postingsLists.get(term);
			return postingForOneTerm.size();
		}
		else
		{
			return 0;
		}
	}

	// because I suppose doc in PA4 is docName,
	// so I create another method for query specially
	// note that we don't need to create a special
	// doc frequency method for query
	public int termFrequencyForQuery(String term, String query)
	{
		ArrayList<String> words = FileContent.getTermsFromString(query);
		return TFIDF.tf(words, term);
	}

	public double weightForQuery(String t, String query)
	{
		int df = docFrequency(t);
		if (df == 0)
		{
			return 0;
		}
		return Math.log(1.0 + termFrequencyForQuery(t, query))
				* Math.log10(1.0 * allDocsContents.size() / docFrequency(t));
	}

	public double weight(String t, String doc)
	{
		int df = docFrequency(t);
		if (df == 0)
		{
			return 0;
		}
		return Math.log(1.0 + termFrequency(t, doc))
				* Math.log10(1.0 * allDocsContents.size() / docFrequency(t));
	}

	public String postingForOneDocToString(String docName,
			ArrayList<Integer> postingForOneDoc)
	{
		String string = "<" + docName + ": ";
		for (Integer p : postingForOneDoc)
		{
			string += p.toString();
			string += ",";
		}
		string = string.substring(0, string.length() - 1); // delete last ","
		string += ">";
		return string;
	}

	public String postingsList(String t)
	{
		t = t.toLowerCase();
		HashMap<String, ArrayList<Integer>> postingForOneTerm;
		ArrayList<Integer> postingForOneDocList;
		ArrayList<String> words;

		postingForOneTerm = new HashMap<String, ArrayList<Integer>>();
		for (String docName : fileNameAndFileContentMap.keySet())
		{
			postingForOneDocList = new ArrayList<Integer>();
			words = fileNameAndFileContentMap.get(docName);
			for (int i = 0; i < words.size(); i++)
			{
				if (t.equalsIgnoreCase(words.get(i)))
				{
					postingForOneDocList.add(i + 1);
				}
			}
			if (postingForOneDocList.size() != 0)
			{
				postingForOneTerm.put(docName, postingForOneDocList);
			}
		}

		// HashMap<String, ArrayList<Integer>> postingForOneTerm =
		// postingsLists.get(t);
		String string = "[";
		for (String docName : postingForOneTerm.keySet())
		{
			string += postingForOneDocToString(docName,
					postingForOneTerm.get(docName));
			string += ",";
		}
		string = string.substring(0, string.length() - 1); // delete last ","
		string += "]";
		return string;
	}

	public double VSScore(String query, String doc)
	{
		query = query.trim();
		double cosineSimilarity = 0.0;

		double[] docVectorForQuery = new double[allTerms.size()];
		double[] docVectorForDoc = new double[allTerms.size()];

		ArrayList<String> queryWords = FileContent.getTermsFromString(query);
		for (int i = 0; i < queryWords.size(); i++)
		{
			if (allTermsIndex.containsKey(queryWords.get(i)))
			{
				docVectorForQuery[allTermsIndex.get(queryWords.get(i))] =  weightForQuery(queryWords.get(i), query);
			}
			else
			{
				// nothing to do which means ignore this word in query
				// because it is not in dictionary
				// so we don't know where we should assign weight in feature vector
			}
		}

		// doc name provided is in our folder
		if (fileNameAndFileContentMap.containsKey(doc))
		{			
			ArrayList<String> docWords = fileNameAndFileContentMap.get(doc);
			for (int i = 0; i < docWords.size(); i++)
			{
				// words in docWords must in allTermsIndex, so we don't need check
				docVectorForDoc[allTermsIndex.get(docWords.get(i))] =  weight(docWords.get(i), doc);
			}
//			System.out.println(docVectorForQuery[allTermsIndex.get(docWords.get(i))]);
		}
		else
		{
			// doc name provided is not in our folder,
			// so docVectorForDoc keeps 0 value which means do nothing here
		}
		cosineSimilarity = CosineSimilarity.cosineSimilarity(docVectorForQuery,
				docVectorForDoc);
		return cosineSimilarity;
	}

	
	public double TPScore(String query, String doc)
	{
		query = query.trim();
		double promixitySimilarity = 0.0;
		
		ArrayList<String> queryWords = FileContent.getTermsFromString(query);
		if (queryWords.size() <= 1)
		{
			return 0;
		}
		else
		{
			double sumDistance = 0;
			for (int i = 0; i < queryWords.size()-1; i++)
			{
				sumDistance += dist(queryWords.get(i), queryWords.get(i+1), doc);
			}
			promixitySimilarity = queryWords.size() / sumDistance;
		}
		
		return promixitySimilarity;
	}

	public double dist(String term1, String term2, String doc)
	{
		//System.out.println(term1 + "  " + term2); 
		double distance = 17;
		HashMap<String, ArrayList<Integer>> postingForOneTerm1 = null;
		ArrayList<Integer> positions1 = null;
		HashMap<String, ArrayList<Integer>> postingForOneTerm2 = null;
		ArrayList<Integer> positions2 = null;
		// process term1
		if (allTermsIndex.containsKey(term1))
		{
			postingForOneTerm1 = postingsLists.get(term1);
			
			if (postingForOneTerm1.containsKey(doc))
			{
				positions1 = postingForOneTerm1.get(doc);
			}
			else 
			{
				// term1 is not in doc, should return 17
				return 17;
			}
		}
		else
		{
			// because one term is not in dictionary, so it can not be in doc,
			// according description in PA4, should return 17
			return 17;
		}
		
		// process term2
		if (allTermsIndex.containsKey(term2))
		{
			postingForOneTerm2 = postingsLists.get(term2);
			
			if (postingForOneTerm2.containsKey(doc))
			{
				positions2 = postingForOneTerm2.get(doc);
			}
			else 
			{
				// term2 is not in doc, should return 17
				return 17;
			}
		}
		else
		{
			// because one term is not in dictionary, so it can not be in doc,
			// according description in PA4, should return 17
			return 17;
		}
		
		
		// following compute distance when two terms in doc
		// first check if term2 doesn't appear after term1, if so return 17
		int maxIndex = positions2.get(positions2.size()-1); // max index of term2
		int minIndex = positions1.get(0); // min index of term1 ???
		//System.out.println(positions2);
		//System.out.println(positions1);
		//System.out.println("maxIndex "+maxIndex);
		//System.out.println("minIndex "+minIndex );
		if (minIndex >= maxIndex)
		{
			return 17;
		}
		// term2 appear after term1, should compute distance according to formula in PA4
		else
		{
			ArrayList<Integer> distanceList = new ArrayList<Integer>();
			for (Integer pos2 : positions2)
			{
				for (Integer pos1 : positions1)
				{
					if (pos2 < pos1) 
					{
						// pos1 > pos2, so all following pos1 is larger than pos2
						// so break
						break;
					}
					else
					{
						// compute difference between pos1 and pos2
						// add difference into distanceList
						distanceList.add(pos2 - pos1);
					}
				}
			}
			int minDistance = minInArray(distanceList);
			distance = Math.min(minDistance, 17);			
		}
		return distance;
	}
	
	public double Relevance(String query, String doc)
	{
		return 0.6 * TPScore(query, doc) + 0.4 * VSScore(query, doc);
	}
	
	// help method to find min in array
	public int minInArray(ArrayList<Integer> dists)
	{
		int min = dists.get(0);
		for (int i = 1; i < dists.size(); i++)
		{
			min = Math.min(min, dists.get(i));
		}
		return min;
	}
	
	// main method for test
	public static void main(String[] args)
	{
		String folder = "/Users/geethanjalijeevanatham/Downloads/IR";
		PositionalIndex posIndex = new PositionalIndex(folder);
		/*System.out.println(posIndex.termFrequency("Association",
				"16-inch_softball.txt"));
		System.out.println(posIndex.docFrequency("Association"));
		System.out.println(posIndex.weight("Association",
				"16-inch_softball.txt"));
		
		System.out.println(posIndex.termFrequency("teams",
				"16-inch_softball.txt"));
		System.out.println(posIndex.docFrequency("teams"));
		System.out.println(posIndex.weight("teams",
				"16-inch_softball.txt"));
		
		System.out.println(posIndex.termFrequency("ball",
				"16-inch_softball.txt"));
		System.out.println(posIndex.docFrequency("ball"));
		System.out.println(posIndex.weight("ball",
				"16-inch_softball.txt"));
		
		// word not in dictionary, all values should be 0 returned
		System.out.println(posIndex.termFrequency("ball.",
				"16-inch_softball.txt"));
		System.out.println(posIndex.docFrequency("ball."));
		System.out.println(posIndex.weight("ball.",
				"16-inch_softball.txt"));*/
		
		
//		System.err.println(posIndex.postingsList("Association")); 
//		System.err.println(posIndex.postingsList("teams")); 
//		System.err.println(posIndex.postingsList("ball")); 
	
//		System.err.println(posIndex.postingsLists.get("Association".toLowerCase()).size());

		String query1 = "ball sizes";
		//String query1 = "ball sizes ranged from 12 to 17 inches in circumference. The 16 inch softball";
		String query2 = "and Yale students invented the game while waiting to hear the results of the annual Harvard-Yale football game. Until the turn of the 20th century, ball sizes ranged from 12 to 17 inches in circumference. The 16 inch softball was eventually adopted in Chicago because it didn't travel as far as the popular 12 or 14 inch balls. This allowed for play on smaller playgrounds or even indoors accommodating the Chicago landscape and climate. Another";
		/*System.err.println("cosineSimilarity is "
				+ posIndex.VSScore("Association teams ball",
						"16-inch_softball.txt"));
		
		System.err.println("cosineSimilarity is "
				+ posIndex.VSScore(query1,
						"16-inch_softball.txt"));
		
		System.err.println("cosineSimilarity is "
				+ posIndex.VSScore(query2,
						"16-inch_softball.txt"));*/
		
		System.out.println("TPScore is "
				+ posIndex.TPScore("Association teams ball",
						"16-inch_softball.txt"));
		
		/*System.err.println("TPScore is "
				+ posIndex.TPScore(query1,
						"16-inch_softball.txt"));
		
		System.err.println("TPScore is "
				+ posIndex.TPScore(query2,
						"16-inch_softball.txt"));
		
		System.err.println("Relevance is "
				+ posIndex.Relevance("Association teams ball",
						"16-inch_softball.txt"));
		
		System.err.println("Relevance is "
				+ posIndex.Relevance(query1,
						"16-inch_softball.txt"));
		
		System.err.println("Relevance is "
				+ posIndex.Relevance(query2,
						"16-inch_softball.txt"));*/
		

	}
}
