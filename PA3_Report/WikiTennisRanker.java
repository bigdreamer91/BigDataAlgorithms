import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */
public class WikiTennisRanker{
	String filename;
	public WikiTennisRanker(String fileName){
		this.filename = fileName;
		double approximationFactor = 0.01;
		callWikiTennisRanker(approximationFactor);
		approximationFactor = 0.005;
		callWikiTennisRanker(approximationFactor);
	}
	
	public void callWikiTennisRanker(double approximationFactor){
		PageRank newRank = new PageRank(filename, approximationFactor);
		int kVal = 10;
		HashSet<String> pageRank = new HashSet<String>(Arrays.asList(newRank.topKPageRank(kVal)));
		HashSet<String> outDegree = new HashSet<String>(Arrays.asList(newRank.topKOutDegree(kVal)));
		HashSet<String> inDegree = new HashSet<String>(Arrays.asList(newRank.topKInDegree(kVal)));
		System.out.println("For approximationFactor - " + approximationFactor);
		System.out.println("Number of Iterations of page rank algorithm to converge to epsilon - " + newRank.numIterations());
		System.out.println("top 10 pages with highest page ranks - " + pageRank);
		System.out.println("top 10 pages with highest out degree - " + outDegree);
		System.out.println("top 10 pages with highest in degree - " + inDegree);
		
		kVal = 100;
		pageRank = new HashSet<String>(Arrays.asList(newRank.topKPageRank(kVal)));
		outDegree = new HashSet<String>(Arrays.asList(newRank.topKOutDegree(kVal)));
		inDegree = new HashSet<String>(Arrays.asList(newRank.topKInDegree(kVal)));
		System.out.println();
		System.out.println("Computing Jaccard Similarity for top 100 highest page rank, out degree and in degree sets");
		System.out.println("Jaccard Sim - topInDegree and topOutDegree " + computeJaccardSim(inDegree,outDegree));
		System.out.println("Jaccard Sim - topPageRank and topOutDegree " + computeJaccardSim(pageRank,outDegree));
		System.out.println("Jaccard Sim - topPageRank and topInDegree " + computeJaccardSim(pageRank,inDegree));
		System.out.println("\n \n");
	}
	
	public double computeJaccardSim(HashSet<String> setOne, HashSet<String> setTwo){
		HashSet<String>allTerms = new HashSet<String>();
		int unionSize = setOne.size() + setTwo.size();
		allTerms.addAll(setOne);
		allTerms.addAll(setTwo);
		setOne.retainAll(setTwo);
		return ((double)setOne.size()/allTerms.size());
	}
	
	public static void main(String[] args){
		String filename = "/Users/geethanjalijeevanatham/Downloads/wikiTennis.txt";
		WikiTennisRanker newRank = new WikiTennisRanker(filename);
	}
}