package mandelbrot;

/**
 * A helper class to enable the Calculation.iterate method
 * to return two values, an int and a double
 */
public class ItReturn
{
	private int iter;
	private double zn;

	public ItReturn(int first, double second)
	{
		iter = first;
		zn = second;
	}

	public int getIter()
	{
		return iter;
	}

	public double getZn()
	{
		return zn;
	}
}
