package mandelbrot;

// This class creates a random color for every point that
// escapes
public class CalcBlack2 extends CalcBlack
{
	@Override
	protected float[] color(int iterated, double zn)
	{
		return new float[] { ra.nextInt(255),
				ra.nextInt(255), ra.nextInt(255) };
	}
}
