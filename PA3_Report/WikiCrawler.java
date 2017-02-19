import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.crypto.spec.IvParameterSpec;
import javax.sql.rowset.FilteredRowSet;

import org.w3c.dom.css.Counter;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */
public class WikiCrawler
{
	String seedUrl;
	String[] keyWords;
	int max;
	String fileName;
	boolean isWeighted;
	int numCrawled;
	HashSet<String> visited;
	HashSet<String> crawlerForbiddenURL = new HashSet<String>();
	Map<String, HashSet<String>>graphAdjacencyList = new HashMap<String, HashSet<String>>();
	static final String BASE_URL = "http://web.cs.iastate.edu/%7Epavan";
	static final String ROBOTS_FILE = "http://web.cs.iastate.edu/%7Epavan/robots.txt";
	boolean checked = false;

	public WikiCrawler(String seedUrl, String[] keyWords, int max,
			String fileName, boolean isWeighted)
	{
		this.seedUrl = seedUrl;
		this.keyWords = keyWords;
		this.max = max;
		this.fileName = fileName;
		this.isWeighted = isWeighted;
		this.numCrawled = 0;
		this.visited = new HashSet<String>();
		this.crawlerForbiddenURL = InitialiseRobotExclusion();
	}

	public HashSet<String> InitialiseRobotExclusion()
	{
		HashSet<String> crawlerForbiddenURL = new HashSet<String>();
		try
		{			
			String webContent = getWebCon(ROBOTS_FILE);			
			String[] lines = webContent.split("\n");
			for (String line : lines)
			{
				if (line.toLowerCase().startsWith("disallow"))
				{
					String forbiddenURL = "";
					int start = line.indexOf(":") + 1;
					int end = line.length();
					forbiddenURL = line.substring(start, end).trim();
					crawlerForbiddenURL.add(forbiddenURL.toLowerCase());					
				}
			}			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return crawlerForbiddenURL;
	}
	
	public ArrayList<Tuple> getTuples(String link)
			throws MalformedURLException, IOException, InterruptedException
	{
		
		
		String url = getCompleteUrl(link);
//		System.err.println("now is requesting page");
		String webContent = getWebCon(url);
		numCrawled++;
//		System.out.println("numCrawled" + ": " + numCrawled);
		if (numCrawled % 10 == 0)
		{
			System.err.println("sleep for 5 seconds");
			Thread.sleep(5000); // for every 10 times request, sleep 5 second
		}
		visited.add(url);
//		System.err.println("now is analysing page");
		ArrayList<String> links = parsePage(link, webContent);
		ArrayList<Tuple> tuples = new ArrayList<>();
		
		for (String eachLink : links)
		{
			if (isWeighted)
			{
				double weight = getWeight(eachLink, webContent);
				tuples.add(new Tuple(eachLink, weight));
			} 
			else
			{
				tuples.add(new Tuple(eachLink, 0));
			}
		}
		return tuples;
	}

	public void crawl() throws InterruptedException, FileNotFoundException,
			UnsupportedEncodingException
	{
		WeightedQueue weightedQueue = new WeightedQueue();
		weightedQueue.add(new Tuple(seedUrl, 0));
		int count = 0;
		double weight = 0.0;
		Tuple nextTuple = null;
		
		try
		{
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.println(max + "");
			while (!weightedQueue.isEmpty() )//&& numCrawled <= max
			{
				Tuple currentTuple = new Tuple(seedUrl,weight);
				if(!checked){
				currentTuple = weightedQueue.extract();
				}
				else{
					currentTuple = nextTuple;
					checked = false;
				}
				
//				System.err.println("now queue size is : " + weightedQueue.size());
//				System.out.println(currentTuple.getUrl() + ",\t"
//						+ currentTuple.getWeight());
				System.out.println(currentTuple.getUrl());

				try
				{
					ArrayList<Tuple> tuples = getTuples(currentTuple.getUrl());
					for (Tuple tuple : tuples)
					{
						// exclude adding visited url into Queue
						if (!visited.contains(tuple.getUrl()))
						{
							weightedQueue.add(tuple);
						}					
						HashSet<String> adjacencyListTemp;
						//writer.println(currentTuple.getUrl() + " " + tuple.getUrl());
						if(graphAdjacencyList.containsKey(currentTuple.getUrl())){
							adjacencyListTemp = graphAdjacencyList.get(currentTuple.getUrl());
							adjacencyListTemp.add(tuple.getUrl());
							graphAdjacencyList.put(currentTuple.getUrl(), adjacencyListTemp);
						}
						else{
							adjacencyListTemp = new HashSet<String>();
							adjacencyListTemp.add(tuple.getUrl());
							graphAdjacencyList.put(currentTuple.getUrl(), adjacencyListTemp);
						}
					}

				} catch (MalformedURLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (numCrawled == max)
				{
					int couter = 0;
					if(weightedQueue.isEmpty()){
						break;
					}
					nextTuple = weightedQueue.extract();
					checked = true;
//					System.err.println("have crawled " + max + " pages. \nEnd...");
//					System.out.println(graphAdjacencyList);
					for(String key : graphAdjacencyList.keySet()){
						HashSet<String> str = new HashSet<String>();
						str.clear();
						for(String item : graphAdjacencyList.get(key)){
							if(graphAdjacencyList.containsKey(item) || item.equals(nextTuple.getUrl())){
								str.add(item);
								count++;
							}
						}
						graphAdjacencyList.put(key, str);
					}
					ArrayList<String> keys = new ArrayList<String>(graphAdjacencyList.keySet());
					
					for(String key : keys){
						if(graphAdjacencyList.get(key).size() == 0){
							graphAdjacencyList.remove(key);
							couter++;
						}
					}
					
					if(graphAdjacencyList.size() == max){
						for(String key : graphAdjacencyList.keySet()){
							HashSet<String> str = new HashSet<String>();
							str.clear();
							for(String item : graphAdjacencyList.get(key)){
								if(graphAdjacencyList.containsKey(item)){
									str.add(item);
									count++;
								}
							}
								graphAdjacencyList.put(key, str);
						}
						checked = false;
						break;
					}
					else{
						numCrawled = numCrawled - couter;
					}
				}

			}
			
			
			for(String key : graphAdjacencyList.keySet()){
				HashSet<String> str = new HashSet<String>();
				for(String item : graphAdjacencyList.get(key)){
					writer.println(key + " " + item);
				}
			}
			writer.close();
			
		} catch (FileNotFoundException | UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println(count);
//		System.out.println(graphAdjacencyList.size());
//		System.out.println(graphAdjacencyList);
	}

	public boolean containsAnyKeyWord(String str)
	{
		for (String word : keyWords)
		{
			if (str.toLowerCase().indexOf(word.toLowerCase()) != -1)
			{
				return true;
			}
		}
		return false;
	}

	public double getDistance(String word, String firstPartWebContent,
			String secondPartWebContent)
	{
		double distb = Double.MAX_VALUE;
		double dista = Double.MAX_VALUE;
//		int index = firstPartWebContent.toLowerCase().indexOf(
//				word.toLowerCase());
		int index = firstPartWebContent.toLowerCase().lastIndexOf(
				word.toLowerCase());
		if (index != -1)
		{
			firstPartWebContent = firstPartWebContent.substring(index
					+ word.length());
			firstPartWebContent = firstPartWebContent.replace("\n", " ");
			distb = firstPartWebContent.split(" ").length;
		}

		index = secondPartWebContent.toLowerCase().indexOf(word.toLowerCase());
		if (index != -1)
		{
			secondPartWebContent = secondPartWebContent.substring(0, index);
			secondPartWebContent = secondPartWebContent.replace("\n", " ");
			dista = secondPartWebContent.split(" ").length;
		}

		return Math.min(dista, distb);
	}

	public double minInArray(ArrayList<Double> dists)
	{
		double min = dists.get(0);
		for (int i = 1; i < dists.size(); i++)
		{
			min = Math.min(min, dists.get(i));
		}
		return min;
	}

	public double getWeight(String link, String webContent)
	{
		double weight = 0;
		if (keyWords.length == 0)
		{
			weight = 0;
			return weight;
		}
		
//		int beginIndex = webContent.indexOf("<p>");
//		if (beginIndex != -1)
//		{
//			webContent = webContent.substring(beginIndex);
//		}
		
		int index = webContent.indexOf(link);

		String anchorText = getAnchorText(link, webContent);
		// System.err.println(anchorText);

		if (containsAnyKeyWord(anchorText) || containsAnyKeyWord(link))
		{
			weight = 1;
			return weight;
		} else
		{
			if (index != -1)
			{
				ArrayList<Double> dists = new ArrayList<>();
//				String firstPartWebContent = webContent.substring(0, index);
//				String secondPartWebContent = webContent.substring(index
//						+ link.length());
				ArrayList<String> twoParts = getSurroundingText(webContent, link);
				String firstPartWebContent = twoParts.get(0);
				String secondPartWebContent = twoParts.get(1);
				for (String word : keyWords)
				{
					double dist = getDistance(word, firstPartWebContent,
							secondPartWebContent);
					dists.add(dist);
				}
				double minDist = minInArray(dists);
				if (minDist > 20)
				{
					weight = 0;
					return weight;
				} else
				{
					weight = 1.0 / (minDist + 2);
					return weight;
				}
			}
		}
		// System.exit(0);

		return weight;
	}

	public ArrayList<String> getSurroundingText(String webContent, String link)
	{
		ArrayList<String> twoParts = new ArrayList<String>();
		String firstPartWebContent = "";
		String secondPartWebContent = "";
		int numCharactors = 1000;
		int index = webContent.indexOf(link);
		if (index != -1)
		{
			if (index - numCharactors >= 0)
			{
				firstPartWebContent = webContent.substring(index - numCharactors, index);
			}
			else {
				firstPartWebContent = webContent.substring(0, index);
			}
			if (index + link.length() + numCharactors < webContent.length())
			{
				
				secondPartWebContent = webContent.substring(index
						+ link.length(), index
						+ link.length() + numCharactors); 
			}
			else {
				secondPartWebContent = webContent.substring(index
						+ link.length());
			}
//			int count = 0;
//			ArrayList<String> beforSurroundingText = new ArrayList<String>();
//			String tmpFirstPartWebContent = webContent.substring(0, index).trim();
//			while (tmpFirstPartWebContent.length() != 0 && count < 20)
//			{
//				index = tmpFirstPartWebContent.lastIndexOf(" ");
//				beforSurroundingText.add(tmpFirstPartWebContent.substring(index+1, endIndex))
//			}
			twoParts.add(firstPartWebContent);
			twoParts.add(secondPartWebContent);
		}
		return twoParts;
	}
	
	public String getAnchorText(String link, String webContent)
	{
		int beginIndex = webContent.indexOf("<p>");
		if (beginIndex != -1)
		{
			webContent = webContent.substring(beginIndex);
		}
		String anchorText = "";
		int index = webContent.indexOf(link);
		if (index != -1)
		{
			webContent = webContent.substring(index);
			index = webContent.indexOf("\">");
			if (index != -1)
			{
				webContent = webContent.substring(index + "\">".length());
				index = webContent.indexOf("</a>");
				if (index != -1)
				{
					anchorText = webContent.substring(0, index);
				}
			}
		}
		return anchorText;
	}

	public ArrayList<String> parsePage(String url, String webContent)
	{
		ArrayList<String> links = new ArrayList<>();
		HashSet<String> linksAlready = new HashSet<String>();
		
		int beginIndex = webContent.indexOf("<p>");
		if (beginIndex != -1)
		{
			webContent = webContent.substring(beginIndex);
			// exclude main page
			int endIndex = webContent.indexOf("/wiki/Main_Page");
			if (endIndex != -1)
			{
				webContent = webContent.substring(0, endIndex);
			}
			
			beginIndex = webContent.indexOf("<a href=\"/wiki/");
			// int count = 0;
			while (beginIndex != -1)
			{
				webContent = webContent.substring(beginIndex
						+ "<a href=\"".length());
				endIndex = webContent.indexOf("\"");
				if (endIndex != -1)
				{
					String link = webContent.substring(0, endIndex);
					// exclude some illegal links  
					if (link.indexOf("#") == -1 && link.indexOf(":") == -1 && !crawlerForbiddenURL.contains(link.toLowerCase()))
					{
						// exclude already existing link 
						if (!linksAlready.contains(link))
						{
							// exclude self loop							
							if (!link.equals(url))
							{
								links.add(link);
								linksAlready.add(link);
							}
						}
							
					}
					webContent = webContent.substring(endIndex + 1);
				}
				beginIndex = webContent.indexOf("<a href=\"/wiki/");
			}
		}

		return links;
	}

	private String getWebCon(String strURL) throws InterruptedException
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			URL url = new URL(strURL);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			in.close();

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

//		numCrawled++;
//		visited.add(strURL);
//		System.err.println("numCrawled" + ": " + numCrawled);
//		if (numCrawled % 10 == 0)
//		{
//			System.err.println("sleep for 5 seconds");
//			Thread.sleep(5000); // for every 10 times request, sleep 1 second
//		}
		return sb.toString();
	}

	private String getCompleteUrl(String link)
	{
		return (BASE_URL + link);
	}

	public static void main(String[] args) throws InterruptedException,
			FileNotFoundException, UnsupportedEncodingException
	{
//		String seedUrl = "/wiki/Mathematics";
//		String[] keyWords = {"Logic", "Axiom", "Proof", "formula", "algebra"};
		String seedUrl = "/wiki/Computer";
		final String[] KEYWORDS = new String[] { "tennis" };
//		{ "tennis", "grand slam", "french open", "australian open",
//				"wimbledon", "US open", "masters" };
		int max = 100;
		String fileName = "WikiComputerGraph.txt";
		boolean isWeighted = true;
		WikiCrawler crawler = new WikiCrawler("/wiki/Seed4.html", KEYWORDS, 10, "WGraph4.txt", true);
		crawler.crawl();
		//System.out.println(crawler.graphAdjacencyList.size());
//		for (String string : crawler.crawlerForbiddenURL)
//		{
//			System.err.println(string);
//		}
	}
}
