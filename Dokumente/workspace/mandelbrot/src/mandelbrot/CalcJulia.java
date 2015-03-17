package mandelbrot;

public class CalcJulia extends CalcCircle
{
	protected double setX(Settings s)
	{
		return s.getJx();
	}

	protected double setY(Settings s)
	{
		return s.getJy();
	}

	@Override
	public void loop(final Settings s)
	{
		newThread(s);
	}
}
