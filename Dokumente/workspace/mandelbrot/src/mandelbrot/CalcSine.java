package mandelbrot;

public class CalcSine extends CalculationMultiThread
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
			a = x * x;
			b = y * y;
			xtemp = a - b + Math.sin(ic) * Math.cosh(jc);
			ytemp = 2 * x * y + Math.cos(ic) * Math.sinh(jc);
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
