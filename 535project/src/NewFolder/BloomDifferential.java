import java.io.*;
import java.util.Arrays;

class BloomDifferential{
	private String diffFile;
	private String database;
	private String grams;
	private int setSize;
	private int bitsperElement = 10;
	private BloomFilterDet filter;
	private long avgTime = 0;
	private long filterHit = 0;
	private long filterMiss = 0;
	private long lineCount1 = 0;
	private long dataHit = 0;
	private boolean printRecord = true;
  
	
	public BloomDifferential(String diffFile, String database, String grams){
		this.diffFile = diffFile;
		this.database = database;
		this.grams = grams;
		this.setSize = getSize();
		filter = new BloomFilterDet(setSize,bitsperElement);
	}
	
	public void createFilter(){
		String str;
		String[] res;
		String line = null;
		try{
			FileReader fileReaderA = new FileReader(diffFile);
			BufferedReader bufferedReaderA = new BufferedReader(fileReaderA);
			while((line = bufferedReaderA.readLine())!=null){
				str = line;
				res = str.split(" ",5);
				str = res[0] + " " + res[1] + " " + res[2] + " " + res[3];
				filter.add(str);
			}
			bufferedReaderA.close();
			fileReaderA.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + diffFile);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + diffFile + " ");
		}
	}
	
	public void setPrint(boolean val){
		printRecord = val;
	}
	
	public int getSize(){
		int dataSize = 0;
		String line = null;
		try{
			FileReader fileReader = new FileReader(diffFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine())!=null){
				dataSize++;
			}
			bufferedReader.close();
			fileReader.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + diffFile);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + diffFile + " ");
		}
		return dataSize;
	}
	
	public void retrieveRecord(String key){
		String line = null;
		int lineCount2 = 0;
		String str = null;
		String[] res;
		int flagFoundDiff = 0;
		int flagFoundData = 0;
		long startTime = System.currentTimeMillis();
		
		try{
			FileReader fileReader2;
			FileReader fileReader3;
			FileReader fileReader4;
			BufferedReader bufferedReader2;
			BufferedReader bufferedReader3;
			BufferedReader bufferedReader4;
			
				lineCount1++;
				boolean b1 = filter.appears(key);
				if(b1==true){
					//System.out.println("found entry in bloom filter");
					lineCount2++;
					fileReader2 = new FileReader(diffFile);
					bufferedReader2 = new BufferedReader(fileReader2);
					line = null;
					while((line = bufferedReader2.readLine())!=null){
						str = line;
						res = str.split(" ",5);
						str = res[0] + " " + res[1] + " " + res[2] + " " + res[3];
						if(key.equals(str)){
							//System.out.println("Found Record in DiffFile.txt");
							flagFoundDiff = 1;
							if(printRecord)
							   System.out.println(line);
							filterHit++;
							break;
						}
					}
					if(flagFoundDiff == 0){
						if(printRecord)
						   System.out.println("Entry found in bloomfilter But not foud in diffFile.txt");
						filterMiss++;
					}
					bufferedReader2.close();
					fileReader2.close();
				}
				else{
					dataHit++;
					//System.out.print("did not find entry in bloom filter ");
					fileReader3 = new FileReader(database);
					bufferedReader3 = new BufferedReader(fileReader3);
					fileReader4 = new FileReader(grams);
					bufferedReader4 = new BufferedReader(fileReader4);
					String line3 = null;
					String line4 = null;
					String str1 = null;
					while((line4 = bufferedReader4.readLine())!=null){
					    if(line4.equals(key)){
					    	line3 = bufferedReader3.readLine();
					    	while((line3 = bufferedReader3.readLine())!=null){
								str = line3;
								res = str.split(" ",5);
								str = res[0] + " " + res[1] + " " + res[2] + " " + res[3];
								if(key.equals(str)){
									//System.out.println("Found record in database.txt");
									//System.out.println(" ");
									flagFoundData = 1;
									if(printRecord)
									   System.out.println(line3);
									break;
								}
							}
					    	break;
					    }
					}
					
					if(flagFoundData == 0){
						//System.out.println("But not foud in database.txt, key might not be present in database.txt");
					}
					
					bufferedReader3.close();
					fileReader3.close();
					bufferedReader4.close();
					fileReader4.close();
				}
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + diffFile);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + diffFile + " ");
		} 
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		avgTime += totalTime;
	}
	
	public static void testBloomDifferential(BloomDifferential bloomdiff){
		System.out.println("Enter path to text file for testing: ");
		String filename1 = null;
		 try{
			  BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		      filename1 = bufferRead.readLine();
		    }
		    catch(IOException e)
		    {
		        e.printStackTrace();
		    }
		
		String line = null;
		try{
			FileReader fileReaderA = new FileReader(filename1);
			BufferedReader bufferedReaderA = new BufferedReader(fileReaderA);
			while((line = bufferedReaderA.readLine())!=null){
				bloomdiff.retrieveRecord(line);
			}
			bufferedReaderA.close();
			fileReaderA.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file " + filename1);
		}
		catch(IOException ex){
			System.out.println("Error reading file " + filename1 + " ");
		}
		
	}
	
	public void performanceStats(){
		System.out.println("Overall query access run time: " + avgTime + "ms");
		System.out.println("false positive probability " + filterMiss / (dataHit + filterMiss));
	}
	
	public static void main(String[] args){
		String filename = null;
		String filename3 = null;
		String filename2 = null;
		
		if(args.length == 3){
			filename = args[0].toString();
			filename2 = args[1].toString();
			filename3 = args[2].toString();
		}
		else{
			System.out.println("Invalid arguments passed to main function");
			System.exit(0);
		}
	
		BloomDifferential bloomdiff = new BloomDifferential(filename,filename2,filename3);
		bloomdiff.createFilter();
		bloomdiff.retrieveRecord("Arbuscula dixit . _END_");
		bloomdiff.setPrint(false);
		testBloomDifferential(bloomdiff);
		bloomdiff.setPrint(true);
		bloomdiff.performanceStats();
	}
}