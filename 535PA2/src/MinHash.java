import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.IntToDoubleFunction;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

class MinHash
{
	private String folder;
	private int numPermutations;
	private ArrayList<String> terms;
	int[][] minHashMatrix;
	ArrayList<ArrayList<Integer>> perms;
	ArrayList<String> fileNames;
	
	public MinHash(String folder, int numPermutations)
	{
		this.folder = folder;
		this.numPermutations = numPermutations;
		this.fileNames = allDocs();
		this.terms = getAllTerms();
		this.perms = getKPermutation(numPermutations);
		this.minHashMatrix = computeMinHashMatrix();
	}

	public ArrayList<String> allDocs()
	{
		return FileNames.getFileNames(folder);
	}
	
	public double exactJaccard(String file1, String file2)
	{
		double sim = 0;
		HashSet<String> commonTerms = new HashSet<String>();
		HashSet<String> allTerms = new HashSet<String>();
		file1 = folder + "/" + file1;
		file2 = folder + "/" + file2;
        String content1  = FileContent.getContent(file1);
        ArrayList<String> words1 = FileContent.getTermsFromString(content1);
        String content2 = FileContent.getContent(file2);
        ArrayList<String> words2 = FileContent.getTermsFromString(content2);
        
        allTerms.addAll(words1);
        allTerms.addAll(words2);
        
        commonTerms.addAll(words1);
        commonTerms.retainAll(words2);
        
        sim = (double) commonTerms.size()/allTerms.size();
		return sim;
	}

	public double approximateJaccard(String file1, String file2)
	{
		double sim = 0;
		ArrayList<Integer> file1HashSig = minHashSig(file1);
		ArrayList<Integer> file2HashSig = minHashSig(file2);
		int matchCounter = 0;
		for (int i=0; i<numPermutations; i++)
		{
			if (file1HashSig.get(i).equals(file2HashSig.get(i)))
			{				
				matchCounter++;
			}
		}
		sim = (double)(matchCounter) / numPermutations;
		return sim;
	}
	
	
	public ArrayList<Integer> minHashSig(String filename)
	{
		ArrayList<Integer> sig = new ArrayList<Integer>();
		int index = fileNames.indexOf(filename);
		for (int row=0; row<numPermutations; row++)
		{
			sig.add(minHashMatrix[row][index]);
		}	
		return sig;
	}

	public ArrayList<String> getAllTerms()
	{
		terms = new ArrayList<String>();
		// this hashSet only for fast check if word in terms arrayList
		HashSet<String> setString = new HashSet<String>();
		ArrayList<String> filePaths = FileNames.getFilePaths(folder);
		for (String filePath : filePaths)
		{
			String content = FileContent.getContent(filePath);
			ArrayList<String> words = FileContent.getTermsFromString(content);
			for (String word : words)
			{
				if (!setString.contains(word))
				{
					terms.add(word);
					setString.add(word);
				}
			}
		}
		return terms;
	}

	public ArrayList<Integer> getAPermutation()
	{
		ArrayList<Integer> perm = new ArrayList<Integer>();
		for (int i = 0; i < numTerms(); i++)
		{
			perm.add(i);
		}
		Collections.shuffle(perm);
		return perm;
	}
	
	public ArrayList<ArrayList<Integer>> getKPermutation(int numPermutations)
	{
		ArrayList<ArrayList<Integer>> perms = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < numPermutations; i++)
		{
			perms.add(getAPermutation());
		}		
		return perms;
	}

	public HashMap<String, Integer> hashTerms()
	{
		HashMap<String, Integer> hTerms = new HashMap<String, Integer>();
		int index = 0;
		for (String term: terms)
		{
			hTerms.put(term, index++);
		}		
		return hTerms;
	}
	
	public int [][] computeMinHashMatrix()
	{
		minHashMatrix = new int[numPermutations][fileNames.size()];
		HashMap<String, Integer> hTerms = hashTerms();
		int column = 0; // which Document
		for (String fileName : fileNames)
		{
			String filePath = folder + "/" + fileName;
			String content = FileContent.getContent(filePath);
			ArrayList<String> words = FileContent.getTermsFromString(content);
			ArrayList<Integer> indexs = new ArrayList<Integer>();
			int index;
			for (String word : words)
			{
				index = hTerms.get(word);
				indexs.add(index);
			}
			
			for (int row = 0; row < numPermutations; row++)
			{
				ArrayList<Integer> perm = perms.get(row);
				ArrayList<Integer> values = new ArrayList<Integer>();
				
				for (int i = 0; i < indexs.size(); i++)
				{
					values.add(perm.get(indexs.get(i)));
				}
				int minValue = Collections.min(values);

				minHashMatrix[row][column] = minValue;
			}
			
			column++;
		}
		return minHashMatrix;
	}
	
	public int[][] minHashMatrix()
	{
		return minHashMatrix;
	}

	public int numTerms()
	{
		return terms.size();
	}

	public int numPermutations()
	{
		return numPermutations;
	}

	public static void main(String[] args) throws InterruptedException
	{
		String folder = "/Users/geethanjalijeevanatham/Downloads/space";
		int numPermutations = 600;
		MinHash minH = new MinHash(folder, numPermutations);
		ArrayList<String> docNames = minH.allDocs();
		
		// test for two similarities
		double appSim = minH.approximateJaccard(minH.allDocs().get(100), minH.allDocs().get(120));
		double exaSim = minH.exactJaccard(minH.allDocs().get(100), minH.allDocs().get(120));
		System.out.println(appSim);
		System.out.println(exaSim);
	}
}
