
/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */


import java.util.*;

public class RandomString
{

	private static final char[] symbols;

	static
	{
		StringBuilder tmp = new StringBuilder();
		for (char ch = '0'; ch <= '9'; ++ch)
			tmp.append(ch);
		for (char ch = 'a'; ch <= 'z'; ++ch)
			tmp.append(ch);
		for (char ch = 'A'; ch <= 'Z'; ++ch)
			tmp.append(ch);
		symbols = tmp.toString().toCharArray();
	}

	private final Random random = new Random();

	private final char[] buf;

	public RandomString(int length)
	{
		if (length < 1)
			throw new IllegalArgumentException("length < 1: " + length);
		buf = new char[length];
	}

	public String nextString()
	{
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}
	
	public static void main(String[] args)
	{
		// for test RandomString
		RandomString rndString = new RandomString(20);
		for (int i = 0; i < 10; i++)
		{
			System.out.println(rndString.nextString());
		}
	}
}