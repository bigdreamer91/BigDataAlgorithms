import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

class BloomFilterDet
{

	private int setSize;
	private int bitsPerElements;
	private BitSet bits;
	private int filterSize;
	private int dataSize;
	private int numHashes;
	ArrayList<Long> kRandomValues;

	public BloomFilterDet(int setSize, int bitsPerElements)
	{
		this.setSize = setSize;
		this.bitsPerElements = bitsPerElements;
		this.bits = new BitSet(setSize * bitsPerElements);
		this.filterSize = setSize * bitsPerElements;
		this.dataSize = 0;
		this.numHashes = (int) Math.ceil(Math.log(2) * bitsPerElements);
		this.kRandomValues = getKRandomValues();
	}

	public ArrayList<Long> getKRandomValues()
	{
		int k = (int) Math.ceil(Math.log(2) * bitsPerElements);		

		ArrayList<Long> arr = new ArrayList<Long>();
		for (int i = 0; i < k; i++)
		{	
			arr.add(1 + (long) ((100 - 1 + 1) * Math.random()));
		}

		return arr;
	}

	public ArrayList<Long> getHashCodes(String s)
	{
		ArrayList<Long> arr = new ArrayList<Long>();
		FNVHash fnv = new FNVHash();
		long originalCode = Math.abs(fnv.hash64(s));
		int k = (int) Math.ceil(Math.log(2) * bitsPerElements);

		for (int i=1;i<=k;i++)
		{
			int hi, lo;
			hi = (int) (originalCode >> 32);
	        lo = (int) originalCode;
	        lo = Math.abs(i * lo) ;
	        long val2 = (((long) hi) << 32) | (lo & 0xffffffffL);
			arr.add((Math.abs(val2)) % filterSize);
		}
		return arr;
	}

	public void addValue(String value)
	{
		ArrayList<Long> kHashCodes = getHashCodes(value);
		for (Long hashCode : kHashCodes)
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
		ArrayList<Long> kHashCodes = getHashCodes(value);
		for (Long hashCode : kHashCodes)
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

		int setSize = 1320;
		int bitsPerElements = 10;
		BloomFilterDet filter = new BloomFilterDet(setSize, bitsPerElements);
		
		String filename = "./Source.txt";
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
		
		String filename1 = "./Test.txt";
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
