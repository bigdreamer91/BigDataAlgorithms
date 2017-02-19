import java.io.File;
import java.util.ArrayList;

/**
*
* @author Lei Qi & Geethanjali Jeevanatham
*/

public class FileNames
{
	// get file names in given directory
	public static ArrayList<String> getFileNames(String directory)
	{
		File file = new File(directory);
		File[] tmpFileNames = file.listFiles();
		ArrayList<String> fileNames = new ArrayList<String>();
		for (int i = 0; i < tmpFileNames.length; i++)
		{
			if (tmpFileNames[i].isFile())
			{
				fileNames.add(tmpFileNames[i].getName());				
			}
			
		}
		return fileNames;
	}	
	
}