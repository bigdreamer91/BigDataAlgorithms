import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

public class LSH
{
	int[][] minHashMatrix;
	String [] docNames;
	ArrayList<String> docNamesList;
	int bands;
	int [][] bHashTables;
	int permutations;
	int rTupleLength;
	int docNum;
	Map<Integer, HashSet<Integer>>lshsimilardocs = new HashMap<Integer,HashSet<Integer>>();
	Map<Integer, HashSet<Integer>>bandTable = new HashMap<Integer, HashSet<Integer>>();
	
	public LSH(int[][] minHashMatrix, String[] docNames, int bands)
	{
		this.minHashMatrix = minHashMatrix;
		this.docNames = docNames;
		this.bands = bands;
		this.bHashTables = null;
		this.permutations = minHashMatrix.length;
		this.docNum = minHashMatrix[0].length;
		this.rTupleLength = permutations/bands;
		this.docNamesList = new ArrayList<String>(Arrays.asList(docNames));
		computeLSH();
	}
	
	public void computeLSH(){
		int counter;
		String toHash = null;
		FNVHash fnv = new FNVHash();
		long hashvalue;
		int pos;
		int bigPrime = getBiggerPrime();
		for(int j=0;j<permutations;j=j+rTupleLength){
			bandTable.clear();
		    for(int i=0;i<minHashMatrix[0].length;i++){
				counter = j;
				while(counter < j+rTupleLength){
					toHash = minHashMatrix[counter][i]+".";
					counter++;
				}
				hashvalue = fnv.hash64(toHash);
				pos = (int) (hashvalue % bigPrime);
				pos = Math.abs(pos);
				if(!bandTable.containsKey(pos)){
					HashSet<Integer> val = new HashSet<Integer>();
					if(!val.contains(i))
					    val.add(i);
					bandTable.put(pos, val);
				}
				else{
					HashSet<Integer> temp = bandTable.get(pos);
					if(!temp.contains(i))
					    temp.add(i);
					bandTable.put(pos, temp);
				}
			}
		    
		    for(Integer position: bandTable.keySet()){
			    HashSet<Integer> docs = bandTable.get(position);
				for(Integer docNum: docs){
					if(!lshsimilardocs.containsKey(docNum)){
						lshsimilardocs.put(docNum, docs);
					}
					else{
						HashSet<Integer> temp = lshsimilardocs.get(docNum);
						temp.addAll(docs);
						lshsimilardocs.put(docNum, temp);
					}
				}
			}
		}
	}
	
	public int getBiggerPrime()
	{
		int biggerPrime = docNum;
		while(true)
		{
			if (isPrime(biggerPrime))
			{
				break;
			}
			biggerPrime++;
		}
		return biggerPrime;
	}
	
	public boolean isPrime(int n)
	{
		if (n <= 1)
		{
			return false;
		}
		for (int i = 2; i < Math.sqrt(n); i++)
		{
			if (n % i == 0)
			{
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<String> nearDuplicatesOf(String docName){
		ArrayList<String> similarDocs = new ArrayList<String>();
		int index = docNamesList.indexOf(docName);
		HashSet<Integer> temp = lshsimilardocs.get(index);
		for(Integer pos: temp){
			if(!docNamesList.get(pos).equals(docName))
			similarDocs.add(docNamesList.get(pos));
		}
		return similarDocs;
	}
	
	public static void main(String[] args)
	{
		String folder;
		folder = "/Users/geethanjalijeevanatham/Downloads/F16PA2";
		int numPermutations = 600;
		int bands = 30;
		MinHash minHash = new MinHash(folder, numPermutations);
		ArrayList<String> allDocArrayList = minHash.allDocs();
		String []docNames = allDocArrayList.toArray(new String[allDocArrayList.size()]);
		LSH lsh = new LSH(minHash.minHashMatrix(), docNames, bands);
		ArrayList<String> neardups = lsh.nearDuplicatesOf("space-0.txt");
		System.out.println(neardups);
	}
}
