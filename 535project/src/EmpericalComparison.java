public class EmpericalComparison{
	private String diffFile;
	private String database;
	private String grams;
	private BloomDifferential bloomdiff;
	private NaiveDifferential naivediff;
	
	EmpericalComparison(String diffFile,String database,String grams){
		this.diffFile = diffFile;
		this.database = database;
		this.grams = grams;
		bloomdiff = new BloomDifferential(diffFile,database,grams);
		naivediff = new NaiveDifferential(diffFile,database,grams);
	}
	
	public void testEmpericalCompare(){
		bloomdiff.testBloomDifferential(bloomdiff);
		naivediff.testNaiveDifferential(naivediff);
	}
	
	public void testEmpericalCompareKey(String key){
		bloomdiff.retrieveRecord(key);
		naivediff.retrieveRecord(key);
	}
	
	public void performanceCompare(){
		bloomdiff.performanceStats();
		naivediff.performanceStats();
	}
	
	public static void main(String[] args){
		
		//sample main showing how to use methods in this class
		String filename = "/Users/geethanjalijeevanatham/Documents/workspace/535project/src/DiffFile.txt";
		String filename3 = "/Users/geethanjalijeevanatham/Documents/workspace/535project/src/database.txt";
		String filename2 = "/Users/geethanjalijeevanatham/Documents/workspace/535project/src/grams.txt";		
		EmpericalComparison empericalCompare = new EmpericalComparison(filename,filename3,filename2);
		empericalCompare.testEmpericalCompareKey("Arbuscula dixit . _END_");
		empericalCompare.testEmpericalCompare();
		empericalCompare.performanceCompare();
	}
}