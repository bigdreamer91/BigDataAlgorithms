import java.util.ArrayList;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */
public class TFIDF
{
	public static int tf(ArrayList<String> doc, String term)
	{
		int termFrequency = 0;
		for (String word : doc)
		{
			if (term.equalsIgnoreCase(word))
				termFrequency++;
		}
		return termFrequency;
	}

	public static int df(ArrayList<ArrayList<String>> docs, String term)
	{
		int docFrequency = 0;
		for (ArrayList<String> doc : docs)
		{
			for (String word : doc)
			{
				if (term.equalsIgnoreCase(word))
				{
					docFrequency++;
					break;
				}
			}
		}
		return docFrequency;
	}

	public static double tfIdf(ArrayList<String> doc, ArrayList<ArrayList<String>> docs, String term)
	{
		return Math.log(1.0 + tf(doc, term)) * Math.log10(1.0 * docs.size() / df(docs, term));
	}

}
