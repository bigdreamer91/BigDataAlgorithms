import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FalsePositives
{
	private int setSize;
	private int bitsPerElements;
	float finalFalseProbDet = 0.0f;
	float finalFalseProbRan = 0.0f;
	
	public FalsePositives(int setSize,int bitsPerElements){
		this.setSize = setSize;
		this.bitsPerElements = bitsPerElements;
	}
	
	public void getFalsePositiveProbability(String source, String test){
		BloomFilterDet filterDet = new BloomFilterDet(setSize, bitsPerElements);
		BloomFilterRan filterRan = new BloomFilterRan(setSize, bitsPerElements);
		
		for(int i=0;i<2000;i++){
			filterDet = new BloomFilterDet(setSize, bitsPerElements);
			finalFalseProbDet = finalFalseProbDet + testBloomFilterDet(setSize, bitsPerElements, filterDet, source, test);
			filterRan = new BloomFilterRan(setSize, bitsPerElements);
			finalFalseProbRan = finalFalseProbRan + testBloomFilterRan(setSize, bitsPerElements, filterRan, source, test);
		}
		System.out.println("The false probability estimate for BloomFilterDet is " + finalFalseProbDet/2000);
		System.out.println("The false probability estimate for BloomFilterRan is " + finalFalseProbRan/2000);
	}
	
	public static float testBloomFilterDet(int setSize, int bitsPerElements, BloomFilterDet filter, String source, String test)
	{
		
		String filename = source;
		String line = null;
		String findVal;
		int dataSize = 0;
		int lineCount = 0;
		int entrycount = 0;
		float falseProb = 0.0f;
		try{
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine())!=null){
				entrycount++;
				filter.add(line);
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + filename + " ");
		}
		
		String filename1 = test;
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
				}
			}
			falseProb = (float) ((float) lineCount*1.0/lineCount1);
			bufferedReader1.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + filename + " ");
		}
		return falseProb;
	}

	public static float testBloomFilterRan(int setSize, int bitsPerElements, BloomFilterRan filter, String source, String test)
	{
		
		String filename = source;
		String line = null;
		String findVal;
		int dataSize = 0;
		int lineCount = 0;
		int entrycount = 0;
		float falseProb = 0.0f;
		try{
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine())!=null){
				entrycount++;
				filter.add(line);
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + filename + " ");
		}
		
		String filename1 = test;
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
				}
			}
			falseProb = (float) ((float) lineCount*1.0/lineCount1);
			bufferedReader1.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + filename + " ");
		}
		return falseProb;
	}
	
	public static void main(String args[])
	{  
		//sample code showing how to use methods in this class
		int setSize = 10;
		int bitsPerElements = 4;
		String Source = "/Users/geethanjalijeevanatham/Documents/workspace/535project/src/Source1.txt";
		String Test = "/Users/geethanjalijeevanatham/Documents/workspace/535project/src/Test1.txt";
		FalsePositives falsePos = new FalsePositives(setSize,bitsPerElements);
		falsePos.getFalsePositiveProbability(Source,Test);
	}


}
