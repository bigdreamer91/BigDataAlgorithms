import java.util.ArrayList;

public class NearDuplicates{
	MinHash minH;
	LSH lsh;
	
	public NearDuplicates(){
		
	}
	
	public ArrayList<String> nearDuplicateDetector(String folder,int permutations,double simThresh,String docName){
		int bands = 10;
		minH = new MinHash(folder,permutations);
		System.out.println("Done with minHash Computation");
		ArrayList<String> allDocArrayList = minH.allDocs();
		String []docNames = allDocArrayList.toArray(new String[allDocArrayList.size()]);
		lsh = new LSH(minH.minHashMatrix(), docNames, bands);
		System.out.println("Done with lsh computation");
		ArrayList<String> nearDuplicates = nearDuplicates(simThresh,docName);
		return nearDuplicates;
	}
	
	public ArrayList<String> nearDuplicates(double simThresh, String docName){
		ArrayList<String> temp = lsh.nearDuplicatesOf(docName);
		ArrayList<String> nearDuplicates = new ArrayList<String>();
		for(String tempname: temp){
			double apprxsim = minH.approximateJaccard(docName, tempname);
			if(apprxsim>=simThresh){
				nearDuplicates.add(tempname);
			}
		}
		return nearDuplicates;
	}
	
	public static void main(String[] args){
		String folder = "/Users/geethanjalijeevanatham/Downloads/F16PA2";
		int permuations = 200;
		String docname = "space-0.txt";
		NearDuplicates neardups = new NearDuplicates();
		ArrayList<String> dups = neardups.nearDuplicateDetector(folder,permuations,0.9,docname);
		System.out.println(dups);
		dups = neardups.nearDuplicates(0.9, "baseball51.txt");
		System.out.println(dups);
		dups = neardups.nearDuplicates(0.9, "hockey857.txt");
		System.out.println(dups);
		dups = neardups.nearDuplicates(0.9, "space-205.txt");
		System.out.println(dups);
		dups = neardups.nearDuplicates(0.9, "baseball777.txt");
		System.out.println(dups);
		dups = neardups.nearDuplicates(0.9, "hockey47.txt");
		System.out.println(dups);
		dups = neardups.nearDuplicates(0.9, "space-378.txt");
		System.out.println(dups);
		dups = neardups.nearDuplicates(0.9, "hockey582.txt");
		System.out.println(dups);
		dups = neardups.nearDuplicates(0.9, "baseball292.txt");
		System.out.println(dups);
		dups = neardups.nearDuplicates(0.9, "hockey5.txt");
		System.out.println(dups);
		dups = neardups.nearDuplicates(0.9, "baseball910.txt");
		System.out.println(dups);
	}
}