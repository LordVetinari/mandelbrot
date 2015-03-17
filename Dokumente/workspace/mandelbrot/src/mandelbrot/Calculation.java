package mandelbrot;

import java.util.ArrayList;
import java.util.Collections;

public class Calculation implements MandelLoop
{
	int lim, X, Y;
	static double THRESHOLD = 65536.0;
	double offsetX, offsetY, fac;
	SetPixel sp;
	ColorTable table;
	ArrayList<Integer> f = new ArrayList<Integer>(100);
	boolean c;
	static float[] BLACK = new float[] { 0, 0, 0 };

	public Calculation()
	{
		for (int n = 0; n < 100; n++)
		{
			f.add(n);
		}
		Collections.shuffle(f);
	}

	public void cancel(boolean wait)
	{

	}

	public void loop(Settings s)
	{
		setTable();
		setParameters(s);
		if (lim < MIN_LIM)
		{
			lim = MIN_LIM;
		}
		for (int i = 0; i < 100; i++)
		{
			calc(f.get(i), s);
			s.update();
		}
		s.ready();
	}

	protected void setParameters(Settings s)
	{
		offsetX = s.getX();
		offsetY = s.getY();
		fac = s.getFac();
		if (fac < (0.0000000000000001))
		{
			System.out
					.println("Please change to BigDecimal class");
		}
		X = s.getSizeX();
		Y = s.getSizeY();
		if (fac > 0.00001)
			lim = (int) (-100 * Math.log(fac));
		else if (fac > 0.000000001)
			lim = (int) (-1000 * Math.log(fac));
		else if (fac > 0.000000000001)
			lim = (int) (-10000 * Math.log(fac));
		else
			lim = (int) (-100000 * Math.log(fac));
	}

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
				double zn = r.getZn();
				if (iterated < lim)
				{
					float[] color = color(iterated, zn);
					s.setP(i, j, color);
				} else
					s.setP(i, j, BLACK);
			}
		}
	}

	protected void setTable()
	{
		table = new ColorTable(false);
	}

	protected float[] color(int iterated, double zn)
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
		int[] color1 = table.get(c % 8);
		int[] color2;
		if (c % 8 < 7)
		{
			color2 = table.get((c % 8) + 1);
		} else
		{
			color2 = table.get(0);
		}
		return interpolate(color1, color2, d);
	}

	protected ItReturn iterate(double ic, double jc,
			Settings s)
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
			xtemp = a - b + ic;
			ytemp = 2 * x * y + jc;
			if (xtemp == x && ytemp == y)
			// This condition tests for single periodicity
			// (see section 2.2.2)
			{
				i = lim;
				break;
			}
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

	// This method uses linear interpolation to determine
	// colors
	protected float[] interpolate(final int[] COLOR1,
			final int[] COLOR2, float fraction)
	{
		final float INT_TO_FLOAT_CONST = 1f / 255f;
		fraction = Math.min(fraction, 1f);
		fraction = Math.max(fraction, 0f);

		final float RED1 = COLOR1[0] * INT_TO_FLOAT_CONST;
		final float GREEN1 = COLOR1[1] * INT_TO_FLOAT_CONST;
		final float BLUE1 = COLOR1[2] * INT_TO_FLOAT_CONST;

		final float RED2 = COLOR2[0] * INT_TO_FLOAT_CONST;
		final float GREEN2 = COLOR2[1] * INT_TO_FLOAT_CONST;
		final float BLUE2 = COLOR2[2] * INT_TO_FLOAT_CONST;

		final float DELTA_RED = RED2 - RED1;
		final float DELTA_GREEN = GREEN2 - GREEN1;
		final float DELTA_BLUE = BLUE2 - BLUE1;

		float red = RED1 + (DELTA_RED * fraction);
		float green = GREEN1 + (DELTA_GREEN * fraction);
		float blue = BLUE1 + (DELTA_BLUE * fraction);

		red = Math.min(red, 1f);
		red = Math.max(red, 0f);
		green = Math.min(green, 1f);
		green = Math.max(green, 0f);
		blue = Math.min(blue, 1f);
		blue = Math.max(blue, 0f);

		float[] color = new float[3];
		color[0] = red * 255f;
		color[1] = green * 255f;
		color[2] = blue * 255f;
		return color;
	}
}
