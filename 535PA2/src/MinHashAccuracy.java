import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
*
* @author Lei Qi & Geethanjali Jeevanatham
*/

public class MinHashAccuracy
{	
	public static int accuracy(String folder, int numPermutations, double epsilon)
	{
		int counter = 0;
		MinHash minH = new MinHash(folder, numPermutations);
		ArrayList<String> fileNames = minH.allDocs();
		for (String fileName1 : fileNames)
		{
			for (String fileName2 : fileNames)
			{				
				double appSim = minH.approximateJaccard(fileName1, fileName2);
				double exaSim = minH.exactJaccard(fileName1, fileName2);
				double diff = Math.abs(appSim-exaSim);
				if (diff > epsilon)
				{
					counter++;
				}
			}
		}	
		
		return counter;
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException
	{
		String folder = "/Users/geethanjalijeevanatham/Downloads/space";
		int []numPermutationsArray = new int[]{400, 600, 800};		
		double []epsilonArray = new double []{0.04, 0.07, 0.09};
		PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
		for (int numPermutations: numPermutationsArray)
		{
			for (double epsilon : epsilonArray)
			{
				int counter = MinHashAccuracy.accuracy(folder, numPermutations, epsilon);
				System.out.println(numPermutations + ", " + epsilon + ", " + counter + "\n");
				writer.println(numPermutations + ", " + epsilon + ", " + counter + "\n");
			}
		}
		writer.close();
	}
	
}
