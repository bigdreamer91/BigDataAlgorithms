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
		bloomdiff = new BloomDifferential(this.diffFile,this.database,this.grams);
		naivediff = new NaiveDifferential(this.diffFile,this.database,this.grams);
	}
	public void testEmpericalCompare(){
		//bloomdiff.setPrint(false);
		bloomdiff.testBloomDifferential(bloomdiff);
		//naivediff.setPrint(false);
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
		
		EmpericalComparison empericalCompare = new EmpericalComparison(filename,filename2,filename3);
		empericalCompare.testEmpericalCompareKey("Arbuscula dixit . _END_");
		empericalCompare.testEmpericalCompare();
		empericalCompare.performanceCompare();
	}
}