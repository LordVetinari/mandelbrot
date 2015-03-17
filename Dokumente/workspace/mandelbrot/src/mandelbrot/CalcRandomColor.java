package mandelbrot;

public class CalcRandomColor extends CalculationMultiThread
{
	@Override
	protected void setTable()
	{
		table = new ColorTable(true);
	}
}
