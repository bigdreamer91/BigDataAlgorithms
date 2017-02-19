import java.io.File;
import java.nio.file.DirectoryIteratorException;
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
	
	// get absolute file paths given directory
	public static ArrayList<String> getFilePaths(String directory)
	{
		File file = new File(directory);
		File[] tmpFileNames = file.listFiles();
		ArrayList<String> filePaths = new ArrayList<String>();
		for (int i = 0; i < tmpFileNames.length; i++)
		{
			if (tmpFileNames[i].isFile())
			{
				filePaths.add(directory + "/" +tmpFileNames[i].getName());				
			}
			
		}
		return filePaths;
	}
	
	// for testing pre-processing class: FileName, FileContent
	public static void main(String[] args)
	{
		
		String path = "C:\\Users\\leiqi\\Desktop\\Fall 2016\\535\\pa2\\articles\\articles";
		
		// using getFileNames
		String fileAbsolutePath = null;
		ArrayList<String> fileNames = getFileNames(path);
		for (String fileName: fileNames)
		{
			fileAbsolutePath = path + "\\" + fileName;
			System.out.println(fileAbsolutePath);
			String content = FileContent.getContent(fileAbsolutePath);
			System.out.println(content);
			ArrayList<String> words = FileContent.getTermsFromString(content);
			System.out.println(words.toString());
			
			break;
		}
		
		// using getFilePaths
		ArrayList<String> filePaths = getFilePaths(path);
		for (String filePath: filePaths)
		{			
			System.out.println(filePath);
			String content = FileContent.getContent(filePath);
			System.out.println(content);
			ArrayList<String> words = FileContent.getTermsFromString(content);
			System.out.println(words.toString());
			
			break;
		}
	}
}