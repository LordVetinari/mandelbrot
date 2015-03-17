package mandelbrot;

import java.math.BigDecimal;
import java.math.MathContext;

class CalculationBigDecimal extends CalculationMultiThread
{
	protected static MathContext mc = new MathContext(32);
	static BigDecimal FOUR = new BigDecimal(16);

	@Override
	public ItReturn iterate(double ic, double jc, Settings s)
	{
		lim = 100;
		BigDecimal icBig = new BigDecimal(ic, mc);
		BigDecimal jcBig = new BigDecimal(jc, mc);
		BigDecimal x = icBig;
		BigDecimal y = jcBig;
		BigDecimal temp;
		BigDecimal temp2 = null;
		int i;
		for (i = 0; i < lim; i++)
		{
			temp = x.multiply(y, mc);
			x = x.multiply(x, mc);
			y = y.multiply(y, mc);
			if ((temp2 = x.add(y, mc)).compareTo(FOUR) == 1)
			{
				break;
			}
			x = x.subtract(y, mc).add(icBig, mc);
			y = temp.add(temp, mc).add(jcBig, mc);
		}
		double zn = Math.sqrt(temp2.doubleValue());
		return new ItReturn(i, zn);
	}
}