import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

// get file content given a file
public class FileContent
{
	public static String getContent(String fileName)
	{
		String content = "";
		String line = null;
		try
		{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null)
			{
				content += line;
				content += " ";
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex)
		{
			System.out.println("Unable to open file " + fileName);
		} catch (IOException ex)
		{
			System.out.println("Error reading file " + fileName + " ");
		}
		return content;
	}

	public static boolean isCorrectDouble(String str)
	{
		try
		{
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e)
		{
			return false;
		}
	}
	
	public static String deletePeriod(String str)
	{
		int index = str.indexOf(".");
		if (index != -1) 
		{
			if (!isCorrectDouble(str))
			{
				str = str.replace(".", "");
			}				
		}

		return str;
	}

	public static ArrayList<String> getTermsFromString(String str)
	{
		// to lower case
		str = str.toLowerCase();
		// remove punctuations
		str = str.replace(",", "");
		str = str.replace("\"", "");
		str = str.replace("?", "");
		str = str.replace("[", "");
		str = str.replace("]", "");
		str = str.replace("'", "");
		str = str.replace("{", "");
		str = str.replace("}", "");
		str = str.replace(":", "");
		str = str.replace(";", "");
		str = str.replace("(", "");
		str = str.replace(")", "");
		

		ArrayList<String> words = new ArrayList<String>();
		for (String word : str.split(" "))
		{
			word = word.trim(); // remove white space at beginning or ending of the word
			
			// delete "." specially
			word = deletePeriod(word);
			
			words.add(word);

		}
		return words;
	}
	
	public static void main(String[] args)
	{
		System.out.println(isCorrectDouble("9.23"));
	}

}
