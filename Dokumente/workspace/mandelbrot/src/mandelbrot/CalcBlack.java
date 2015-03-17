package mandelbrot;

import java.util.Random;

// This class overrides the color method. It doesn't use the
// interpolation method to determine the color of a point but
// rather chooses randomly from several preset colors
public class CalcBlack extends CalculationMultiThread
{
	Random ra = new Random();

	@Override
	protected float[] color(int iterated, double zn)
	{
		int r = ra.nextInt(12);
		switch (r + 1)
		{
		case 1:
			return new float[] { 255, 192, 0 };
		case 2:
			return new float[] { 72, 61, 139 };
		case 3:
			return new float[] { 0, 0, 153 };
		case 4:
			return new float[] { 124, 252, 0 };
		case 5:
			return new float[] { 178, 34, 34 };
		case 6:
			return new float[] { 208, 32, 144 };
		case 7:
			return new float[] { 168, 163, 205 };
		case 8:
			return new float[] { 45, 118, 128 };
		case 9:
			return new float[] { 100, 67, 20 };
		case 10:
			return new float[] { 189, 133, 201 };
		case 11:
			return new float[] { 79, 42, 37 };
		case 12:
			return new float[] { 79, 39, 65 };
		default:
			return null;
		}
	}
}
