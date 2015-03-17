package mandelbrot;

public class CalcWhite extends CalculationMultiThread
{
	@Override
	protected void calc(int l, Settings s)
	{
		for (int i = l / 10; i < X; i += 10)
		{
			for (int j = l % 10; j < Y; j += 10)
			{
				double ic = (i + offsetX) * fac;
				double jc = (j + offsetY) * fac;
				ItReturn r = iterate(ic, jc, s);
				int iterated = r.getIter();
				if (iterated == lim)
					s.setP(i, j, BLACK);
			}
		}
	}
}
