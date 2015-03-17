package mandelbrot;

public class CalcShip extends CalculationMultiThread
{
	@Override
	protected ItReturn iterate(double ic, double jc, Settings s)
	{
		double x = ic;
		double y = jc;
		double xtemp;
		double ytemp;
		int i;
		double a = 0, b = 0;
		for (i = 0; i < lim; i++)
		{
			x = Math.abs(x);
			y = Math.abs(y);
			a = x * x;
			b = y * y;
			xtemp = a - b + ic;
			ytemp = 2 * x * y + jc;
			if (a + b > THRESHOLD)
			{
				break;
			}
			x = xtemp;
			y = ytemp;
		}
		double zn = Math.sqrt(a + b);
		return new ItReturn(i, zn);
	}
}
