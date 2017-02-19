import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.*;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

public class BloomFilterRan
{

	private int setSize;
	private int bitsPerElements;
	private BitSet bits;
	private int filterSize;
	private int dataSize;
	private int numHashes;
	ArrayList<Integer> kRandomA;
	ArrayList<Integer> kRandomB;

	public BloomFilterRan(int setSize, int bitsPerElements)
	{
		this.setSize = setSize;
		this.bitsPerElements = bitsPerElements;
		this.bits = new BitSet(setSize * bitsPerElements);
		this.filterSize = setSize * bitsPerElements;
		this.dataSize = 0;
		this.numHashes = (int) Math.ceil(Math.log(2) * bitsPerElements);
		this.kRandomA = getKRandom(numHashes);
		this.kRandomB = getKRandom(numHashes);
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
	
	public int getAPrimeRandom()
	{
		int BIGGER_PRIME = getBiggerPrime();//setSize * 2;
		Random rnd = new Random();
		int randomPrime;
		randomPrime = rnd.nextInt(BIGGER_PRIME);
		while (true)
		{
			if (isPrime(randomPrime))
			{
				return randomPrime;
			}
			randomPrime = rnd.nextInt(BIGGER_PRIME);
		}
	}

	public int getBiggerPrime()
	{
		int biggerPrime = filterSize-1;
		for (; biggerPrime > 1; biggerPrime--)
		{
			if (isPrime(biggerPrime))
			{
				break;
			}
		}
		return biggerPrime;
	}
	
	public int getIntFromString(String string)
	{
		int hash = 7;
		for (int i = 0; i < string.length(); i++) {
		    hash = hash*31 + string.charAt(i);
		}
		return hash;
	}
	
	public ArrayList<Integer> getKRandom(int numHashes)
	{		
		ArrayList<Integer> arr = new ArrayList<Integer>();			
		for (int i = 0; i < numHashes; i++)
		{
			arr.add(getAPrimeRandom());
		}
		return arr;
	}	

	
	
	public ArrayList<Integer> getHashCodes(String s)
	{
		ArrayList<Integer> arr = new ArrayList<Integer>();
		int BIGGER_PRIME = getBiggerPrime(); 
		
		int originalCode = getIntFromString(s);

		for (int i = 0; i < kRandomA.size(); i++)
		{
			arr.add(Math.abs((kRandomA.get(i) * originalCode + kRandomB.get(i)) % BIGGER_PRIME));
		}
		return arr;
	}

	public void addValue(String value)
	{
		ArrayList<Integer> kHashCodes = getHashCodes(value);
		for (Integer hashCode : kHashCodes)
			bits.set(Math.toIntExact(hashCode), true);
	}

	public void add(String value)
	{
		value = value.toLowerCase();
		if (value != null)
		{
			addValue(value);
			dataSize++;
		}
	}

	public boolean appears(String value)
	{

		if (value == null)
			return false;
		value = value.toLowerCase();
		ArrayList<Integer> kHashCodes = getHashCodes(value);
		for (Integer hashCode : kHashCodes)
		{
			if (bits.get(Math.toIntExact(hashCode)) == false)
			{
				return false;
			}
		}
		return true;
	}

	public int filterSize()
	{
		return filterSize;
	}

	public int dataSize()
	{
		return dataSize;
	}

	public int numHashes()
	{
		return (int) Math.ceil(Math.log(2) * bitsPerElements);
	}

	public static void main(String[] args)
	{		
		//sample main method showing how to use the methods in this class
		int setSize = 1319;
		int bitsPerElements = 10;
		BloomFilterRan filter = new BloomFilterRan(setSize, bitsPerElements);
		
		filter.add("Theory of relativity");
		if(filter.appears("Science")){
			System.out.println("False positive");
		}
		
		// sample code to read from text files, replace path to text file to run this code
		String filename = "E:\\spring 16\\code\\BigData_PA1_V2\\src\\Source.txt";
		String filename1 = "E:\\spring 16\\code\\BigData_PA1_V2\\src\\Test.txt";
		
		String line = null;
		String findVal;
		int dataSize = 0;
		int lineCount = 0;
		int entrycount = 0;
		try{
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine())!=null){
				entrycount++;
				filter.add(line);
			}
			System.out.println(entrycount);
			bufferedReader.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + filename + " ");
		}
		
		String line1 = null;
		int lineCount1 = 0;
		try{
			FileReader fileReader1 = new FileReader(filename1);
			BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
			while((line1 = bufferedReader1.readLine())!=null){
				lineCount1++;
				boolean b = filter.appears(line1);
				if(b==true){
					lineCount++;
					System.out.println(line1);
				}
			}
			System.out.println(lineCount);
			System.out.println(lineCount1);
			bufferedReader1.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + filename + " ");
		}
	}
}
