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
	public static void main(String args[])
	{
		int setSize = 100000;
		int bitsPerElements = 20;
		

		// generate Source File and Test File Randomly
		/*
		String fileName = ".\\src\\Source.txt";
		FalsePositives.generateRandomString(setSize, bitsPerElements, fileName);
		
		ArrayList<String> arrString = new ArrayList<String>();
		FalsePositives.readFile(fileName, arrString);
		System.out.println("Source file size is " + arrString.size());

		String testFileName = ".\\src\\Test.txt";
		FalsePositives.generateRandomStringNotInArrString(setSize/2, bitsPerElements, testFileName, arrString);
		*/
		// end generate Source File and Test File Randomly
		
		
		// test BloomFilterDet
		BloomFilterDet filterDet = new BloomFilterDet(setSize, bitsPerElements);
		testBloomFilterDet(setSize, bitsPerElements, filterDet);
		
		// test BloomFilterRan
		BloomFilterRan filterRan = new BloomFilterRan(setSize, bitsPerElements);
		testBloomFilterRan(setSize, bitsPerElements, filterRan);
		
	}

	public static void readFile(String filePath, ArrayList<String> arrString)
	{
		System.out.println("reading file: " + filePath);
		try
		{
			File filename = new File(filePath);
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line = br.readLine();

			while (line != null)
			{
//				System.out.println(line);
				arrString.add(line.trim());
				line = br.readLine();
			}
			br.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void generateRandomString(int number, int bitLength,
			String fileName)
	{
		try
		{
			File writename = new File(fileName);
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));

			// test for strings that's been added into bloom filter
			ArrayList<String> arrString = new ArrayList<String>();
			for (int i = 0; i < number; i++)
			{
				RandomString rndString = new RandomString((int) (1 + bitLength
						* Math.random()));
				String value = rndString.nextString();
				if (arrString.indexOf(value) == -1)
				{
					arrString.add(value);

				}

			}

			int counter = 0;
			for (String value : arrString)
			{
				counter++;
				System.out.println(value);
				out.write(value + "\n");
			}

			out.flush();
			out.close();
			System.out.println("total number of string is " + counter);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void generateRandomStringNotInArrString(int number, int bitLength,
			String fileName, ArrayList<String> arrString)
	{
		try
		{
			File writename = new File(fileName);
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));

			// generate string not in arrString
			ArrayList<String> arrStringNot = new ArrayList<String>();
			for (int i = 0; i < number; i++)
			{
				RandomString rndString = new RandomString((int) (1 + bitLength
						* Math.random()));
				String value = rndString.nextString();
				if (arrString.indexOf(value) == -1)
				{
					arrStringNot.add(value);
				}

			}

			int counter = 0;
			for (String value : arrStringNot)
			{
				counter++;
				System.out.println(value);
				out.write(value + "\n");
			}

			out.flush();
			out.close();
			System.out.println("total number of string is " + counter);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void testBloomFilterDet(int setSize, int bitsPerElements, BloomFilterDet filter)
	{
		
		String filename = "/Users/geethanjalijeevanatham/Documents/workspace/535project/src/Source.txt";
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
//			System.out.println(entrycount);
			bufferedReader.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + filename + " ");
		}
		
		String filename1 = "/Users/geethanjalijeevanatham/Documents/workspace/535project/src/Test.txt";
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
//					System.out.println(line1);
				}
			}
			System.out.println(lineCount);
			System.out.println(lineCount1);
			System.out.println("false positive probability is " + lineCount*1.0/lineCount1);
			bufferedReader1.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + filename + " ");
		}
	}

	public static void testBloomFilterRan(int setSize, int bitsPerElements, BloomFilterRan filter)
	{
		
		String filename = "/Users/geethanjalijeevanatham/Documents/workspace/535project/src/Source.txt";
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
//			System.out.println(entrycount);
			bufferedReader.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + filename + " ");
		}
		
		String filename1 = "/Users/geethanjalijeevanatham/Documents/workspace/535project/src/Test.txt";
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
//					System.out.println(line1);
				}
			}
			System.out.println(lineCount);
			System.out.println(lineCount1);
			System.out.println("false positive probability is " + lineCount*1.0/lineCount1);
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
