/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */

public class Tuple
{
	String url;
	double weight;

	public Tuple(String url, double weight)
	{
		this.url = url;
		this.weight = weight;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public double getWeight()
	{
		return weight;
	}

	public void setWeight(double weight)
	{
		this.weight = weight;
	}

}