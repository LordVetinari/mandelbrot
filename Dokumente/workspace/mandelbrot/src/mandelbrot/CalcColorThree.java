package mandelbrot;

public class CalcColorThree extends CalculationMultiThree
{
	@Override
	protected void setTable()
	{
		table = new ColorTable(true);
	}
}
