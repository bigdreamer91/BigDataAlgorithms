import java.util.ArrayList;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

public class MinHashTime
{
	public static String timer(String folder, int numPermutations)
	{
		long timeExaJaccard;
		long timeAppJaccardPart1; // time to create minHashMatrix
		long timeAppJaccardPart2; // time to estimate similarity

		MinHash minH = new MinHash(folder, numPermutations);
		ArrayList<String> fileNames = minH.allDocs();

		// for timeExaJaccard
		long startTime = System.currentTimeMillis();
		for (String fileName1 : fileNames)
		{
			for (String fileName2 : fileNames)
			{
				minH.exactJaccard(fileName1, fileName2);
			}
		}
		long endTime = System.currentTimeMillis();
		timeExaJaccard = (endTime - startTime) / 1000;
		System.err.println(timeExaJaccard);
		
		// for timeAppJaccardPart1
		startTime = System.currentTimeMillis();
		MinHash minH2 = new MinHash(folder, numPermutations);
		endTime = System.currentTimeMillis();
		timeAppJaccardPart1 = (endTime - startTime) / 1000;
		System.err.println(timeAppJaccardPart1);
		
		fileNames = minH2.allDocs();

		// for timeAppJaccardPart2
		startTime = System.currentTimeMillis();
		for (String fileName1 : fileNames)
		{
			for (String fileName2 : fileNames)
			{
				minH.approximateJaccard(fileName1, fileName2);
			}
		}
		endTime = System.currentTimeMillis();
		timeAppJaccardPart2 = (endTime - startTime) / 1000;
		System.err.println(timeAppJaccardPart2);
		return "timeExaJaccard: " + timeExaJaccard
				+ "\ntimeAppJaccard Of computing MinHashMatrix: "
				+ timeAppJaccardPart1
				+ "\ntimeAppJaccard Of computing similarity: "
				+ timeAppJaccardPart2;
	}

	public static void main(String[] args)
	{
		String folder = "/Users/geethanjalijeevanatham/Downloads/space";
		
		// follow the question, report time for each method when numPermutaions = 600
		System.out.println(MinHashTime.timer(folder, 600));

	}

}
