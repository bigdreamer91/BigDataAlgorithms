import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

class NaiveDifferential{
	private String diffFile;
	private String database;
	private String grams;
	private long avgTime = 0;
	private long filterHit = 0;
	private long filterMiss = 0;
	private long lineCount1 = 0;
	private long dataHit = 0;
	private boolean printRecord = true;
	public NaiveDifferential(String diffFile,String database, String grams){
		this.diffFile = diffFile;
		this.database = database;
		this.grams = grams;
	}
	
	public void setPrint(boolean val){
		printRecord = val;
	}
	
	public void retrieveRecord(String key){
		String line = null;
		int lineCount1 = 0;
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

					fileReader2 = new FileReader(diffFile);
					bufferedReader2 = new BufferedReader(fileReader2);
					line = null;
					while((line = bufferedReader2.readLine())!=null){
						str = line;
						res = str.split(" ",5);
						str = res[0] + " " + res[1] + " " + res[2] + " " + res[3];
						if(key.equals(str)){
							flagFoundDiff = 1;
							if(printRecord)
							   System.out.println(line);
							break;
						}
					}
					bufferedReader2.close();
					fileReader2.close();
					
					if(flagFoundDiff == 0){
						//System.out.println("Record not foud in diffFile.txt, accessing database.txt");
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
							if(printRecord)
							   System.out.println("But not foud in database.txt, key might not be present in database.txt");
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
	
	public static void testNaiveDifferential(NaiveDifferential naivediff){
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
				naivediff.retrieveRecord(line);
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
		
		NaiveDifferential naivediff = new NaiveDifferential(filename,filename2,filename3);
		naivediff.retrieveRecord("Arbuscula dixit . _END_");
		naivediff.testNaiveDifferential(naivediff);
		naivediff.performanceStats();
	}
}