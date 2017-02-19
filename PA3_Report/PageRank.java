import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

public class PageRank{
	String filename;
	int numVertices;
	double approximationFactor;
	Map<String, HashSet<String>>graphIndegreeList = new HashMap<String, HashSet<String>>();
	Map<String, HashSet<String>>graphAdjacencyList = new HashMap<String, HashSet<String>>();
	Map<String, Double>pagerank = new HashMap<String, Double>();
	Map<String, Double>pagerankNext = new HashMap<String, Double>();
	boolean isConverged = false;
	boolean fileReadComplete = false;
	int numEdges = 0;
	int numIterations = 0;
	
	public PageRank(String fileName, double aprroximationFactor){
		this.filename = fileName;
		this.approximationFactor = aprroximationFactor;
		fileRead();
		if(graphAdjacencyList.size() > 0 && graphIndegreeList.size() > 0 && pagerank.size() > 0){
			fileReadComplete = true;
		}
		calcRank();
	}
	
	public double pageRankOf(String vertexName){
		double pagerankVal = 0;
		if(isConverged){
			pagerankVal = pagerankNext.get(vertexName);
		}
		else{
			System.err.println("pageRank not computed yet");
		}
		return pagerankVal;
	}
	
	public int outDegreeOf(String vertexName){
		int outDegree = 0;
		if(fileReadComplete){
			outDegree = graphAdjacencyList.get(vertexName).size();
		}
		else{
			System.err.println("error in reading file");
		}
		return outDegree;
	}
	
	public int inDegreeOf(String vertexName){
		int inDegree = 0;
		if(fileReadComplete){
			inDegree = graphIndegreeList.get(vertexName).size();
		}
		else{
			System.err.println("error in reading file");
		}
		return inDegree;
	}
	
	public int numEdges(){
		return numEdges;
	}
	
	public class PageRankCompare implements Comparator<String>{

		@Override
		public int compare(String o1, String o2) {
			if(pageRankOf(o2) > pageRankOf(o1)) return 1;
			else if(pageRankOf(o2) < pageRankOf(o1)) return -1;
			return 0;
		}
		
	}
	
	public class OutDegreeCompare implements Comparator<String>{

		@Override
		public int compare(String o1, String o2) {
			return outDegreeOf(o2) - outDegreeOf(o1);
		}
	}
	
	public class InDegreeCompare implements Comparator<String>{

		@Override
		public int compare(String o1, String o2) {
			return inDegreeOf(o2) - inDegreeOf(o1);
		}
		
	}
	
	public String[] topKPageRank(int k){
		String[] topKPageRankArray = new String[k];
		ArrayList<String> topKPageRankList = new ArrayList<String>(pagerankNext.keySet());
		Collections.sort(topKPageRankList, new PageRankCompare());
		for(int i=0;i<k;i++){
			topKPageRankArray[i] = topKPageRankList.get(i);
		}
		return topKPageRankArray;
	}
	
	public String[] topKInDegree(int k){
		String[] topKIndegreeArray = new String[k];
		ArrayList<String> topKInDegreeList = new ArrayList<String>(graphIndegreeList.keySet());
		Collections.sort(topKInDegreeList, new InDegreeCompare());
		for(int i=0;i<k;i++){
			topKIndegreeArray[i] = topKInDegreeList.get(i);
		}
		return topKIndegreeArray;
	}
	
	public String[] topKOutDegree(int k){
		String[] topKOutDegreeArray = new String[k];
		ArrayList<String> topKOutDegreeList = new ArrayList<String>(graphAdjacencyList.keySet());
		Collections.sort(topKOutDegreeList, new OutDegreeCompare());
		for(int i=0;i<k;i++){
			topKOutDegreeArray[i] = topKOutDegreeList.get(i);
		}
		return topKOutDegreeArray;
	}
	
	public int numIterations(){
		if(isConverged) return numIterations;
		else System.err.println("method called before pageRank computed");
		
		return numIterations;
	}
	
	public void fileRead(){
		String line;
		ArrayList<String> words = new ArrayList();
		HashSet<String> adjacencyListTemp;
		int pos = 0;
		double pagerankVal = 0;
		try{
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String vertices = bufferedReader.readLine();
			numVertices = Integer.parseInt(vertices);
			
			//For each line split line into 2 add first part as key to graphadjacencyList second part is added to hashset as outgoing link
			while ((line = bufferedReader.readLine()) != null)
			{
				numEdges = numEdges + 1;
				for(String word : line.split(" ")){
					word = word.trim();
					words.add(word);
				}
				
				//forming adjacency list of the graph
				if(graphAdjacencyList.containsKey(words.get(pos))){
					adjacencyListTemp = graphAdjacencyList.get(words.get(pos));
					adjacencyListTemp.add(words.get(pos + 1));
					graphAdjacencyList.put(words.get(pos), adjacencyListTemp);
				}
				else{
					adjacencyListTemp = new HashSet<String>();
					adjacencyListTemp.add(words.get(pos + 1));
					graphAdjacencyList.put(words.get(pos), adjacencyListTemp);
				}
				
				//forming graph indegree list
				if(graphIndegreeList.containsKey(words.get(pos+1))){
					adjacencyListTemp = graphIndegreeList.get(words.get(pos + 1));
					adjacencyListTemp.add(words.get(pos));
					graphIndegreeList.put(words.get(pos + 1), adjacencyListTemp);
				}
				else{
					adjacencyListTemp = new HashSet<String>();
					adjacencyListTemp.add(words.get(pos));
					graphIndegreeList.put(words.get(pos + 1), adjacencyListTemp);
				}
				
				//initializing pagerank P_{0} with 1/N for each vertex
				pagerankVal = (double)1 / numVertices;
				pagerank.put(words.get(pos), pagerankVal);
				words.clear();
			}
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename);
		}
		catch (IOException ex)
		{
			System.out.println("Error reading file " + filename + " ");
		}
	}
	
	//to calculate page rank between pagerank (n) and pagerankNext (n+1)
	public void calcRank(){
		double normSum = 0;
		//pagerank formula
			while(!isConverged){
				numIterations++;
				calcPageRank(numVertices);
				normSum = 0;
				
				//Norm(P_{n+1} - P_{n})
			    for(String key : pagerank.keySet()){
					normSum = normSum + Math.abs(pagerankNext.get(key) - pagerank.get(key));
				}
					
				if(normSum <= approximationFactor){
					isConverged = true;
				}
				else{
					for(String key: pagerank.keySet()){
						pagerank.put(key, pagerankNext.get(key));
					}
				}
			}
	}
	
	public void calcPageRank(int numVertices){
		HashSet<String> adjacencyListTemp = new HashSet<String>();
		pagerankNext.clear();
		double convergence = 0.85;
		double val = (double)(1 - convergence)/numVertices;
		double temp;
		double tempVal;
		
		
		//asserting if graphAdjacencyList.size() > 0 and graphIndegreeSize > 0 and pagerank.size > 0 before computing
		if(fileReadComplete){
			//p_{n+1} = 1-convergence/N
			for(String key: graphAdjacencyList.keySet()){
			    pagerankNext.put(key, val);
			}
			
			//if not sink node step
			for(String key : graphAdjacencyList.keySet()){
				if(graphAdjacencyList.get(key).size()!=0){
					adjacencyListTemp = graphAdjacencyList.get(key);
					for(String itemAdj : adjacencyListTemp){
						if(pagerankNext.containsKey(itemAdj)){
							temp = (double)pagerankNext.get(itemAdj);
							tempVal = (double)temp + ((convergence * pagerank.get(key))/adjacencyListTemp.size());
							pagerankNext.put(itemAdj, tempVal);
						}
					}
				}
				//sink node step
				else if(graphAdjacencyList.get(key).size()==0){
					for(String page : graphAdjacencyList.keySet()){
						temp = (double)pagerankNext.get(page);
						tempVal = (double)temp + ((convergence * pagerank.get(key))/numVertices);
						pagerankNext.put(page, tempVal);
					}
				}
			}
		}	
	}
	
	public static void main(String[] args){
		String filename = "E:\\spring 16\\code\\BigData_PA3\\WikiMathematicsGraph.txt";
		double approximationFactor = 0.01;
		PageRank newRank = new PageRank(filename,approximationFactor);
//		System.out.println(newRank.graphAdjacencyList.size());
		System.out.println("number of iterations to converge to approximation - " + newRank.numIterations());
		String[] list = newRank.topKPageRank(10);
		System.out.println(Arrays.toString(list));
		System.out.println(newRank.pageRankOf("/wiki/Mathematics"));
		System.out.println(newRank.inDegreeOf("/wiki/Mathematics"));
		System.out.println(newRank.outDegreeOf("/wiki/Mathematics"));
	}
}