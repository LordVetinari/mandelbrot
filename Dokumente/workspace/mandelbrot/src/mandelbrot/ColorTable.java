package mandelbrot;

// This creates an array of eight colors that can be used
// by the calculation class to set pixels to a specific color
public class ColorTable
{
	java.util.Random r = new java.util.Random();
	int[][] a;
	int j;

	public ColorTable(boolean random)
	{
		a = new int[8][3];
		if (!random)
			fill();
		else
			fillRandom();
	}

	public int[] get(int color)
	{
		return a[color];
	}

	private void fill()
	{
		a[0][0] = 94;
		a[0][1] = 0;
		a[0][2] = 0;

		a[1][0] = 186;
		a[1][1] = 0;
		a[1][2] = 0;

		a[2][0] = 221;
		a[2][1] = 70;
		a[2][2] = 25;

		a[3][0] = 255;
		a[3][1] = 141;
		a[3][2] = 49;

		a[4][0] = 251;
		a[4][1] = 190;
		a[4][2] = 34;

		a[5][0] = 248;
		a[5][1] = 239;
		a[5][2] = 22;

		a[6][0] = 249;
		a[6][1] = 243;
		a[6][2] = 117;

		a[7][0] = 249;
		a[7][1] = 247;
		a[7][2] = 212;
	}

	private void fillRandom()
	{
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				a[i][j] = r.nextInt(255);
			}
		}
	}
}
