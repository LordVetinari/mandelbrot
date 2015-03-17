package mandelbrot;

public class CalcCircle extends CalculationMultiThread
{
	@Override
	protected ItReturn iterate(double ic, double jc, Settings s)
	{
		double x = ic;
		double y = jc;
		double xtemp;
		double ytemp;
		int i;
		double addi = setX(s);
		double addr = setY(s);
		double a = 0, b = 0;
		for (i = 0; i < lim; i++)
		{
			a = x * x;
			b = y * y;
			xtemp = a - b + addr;
			ytemp = 2 * x * y + addi;
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

	protected double setX(Settings s)
	{
		return 0;
	}
	
	protected double setY(Settings s)
	{
		return 0;
	}

	@Override
	public float[] color(int iterated, double zn)
	{
		double nu = Math.log(Math.log(zn) / Math.log(2))
				/ Math.log(2);
		double cd = iterated + 1 - nu;
		int c = (int) cd;
		if (c < 0)
		{
			c = 0;
		}
		float d = (float) cd % 1;
		int[] color1 = table.get(c % 3);
		int[] color2;
		if (c % 3 < 2)
		{
			color2 = table.get((c % 3) + 1);
		} else
		{
			color2 = table.get(0);
		}
		return interpolate(color1, color2, d);
	}

	@Override
	protected void setTable()
	{
		table = new ColorTable(true);
	}
}
