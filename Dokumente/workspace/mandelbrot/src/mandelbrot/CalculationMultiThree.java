package mandelbrot;

public class CalculationMultiThree extends CalculationMultiThread
{
	@Override
	protected ItReturn iterate(double ic, double jc, Settings s)
	{
		double x = ic;
		double y = jc;
		double xtemp;
		double ytemp;
		int i;
		double a = 0, b = 0, c = 0, d = 0;
		for (i = 0; i < lim; i++)
		{
			a = x * x * x;
			c = 3 * x * y * y;
			b = y * y * y;
			d = 3 * x * x * y;

			xtemp = a - c + ic;
			ytemp = - b + d + jc;
			if (x * x + y * y > THRESHOLD)
			{
				break;
			}
			x = xtemp;
			y = ytemp;
		}
		double zn = Math.sqrt(x * x + y * y);
		return new ItReturn(i, zn);
	}
}
