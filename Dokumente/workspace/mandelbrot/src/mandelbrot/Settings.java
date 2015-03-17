package mandelbrot;

// This class provides a way for the UIFrame to pass values
// to the calculation class. A settings object is handed
// over when calling the loop() function. It also provides
// methods for the calculation class to set a pixel
// (see also SetPixel.java)
public class Settings
{
	private double offsetX;
	private double offsetY;
	private double fac;
	private int sizeX;
	private int sizeY;
	private double Jx;
	private double Jy;
	private SetPixel sp;

	public Settings(double x, double y, double f, int sx,
			int sy)
	{
		offsetX = x;
		offsetY = y;
		fac = f;
		sizeX = sx;
		sizeY = sy;
	}

	public double getX()
	{
		return offsetX;
	}

	public double getY()
	{
		return offsetY;
	}

	public double getFac()
	{
		return fac;
	}

	public int getSizeX()
	{
		return sizeX;
	}

	public int getSizeY()
	{
		return sizeY;
	}

	public double getJx()
	{
		return Jx;
	}

	public double getJy()
	{
		return Jy;
	}

	public void setP(int x, int y, float[] color)
	{
		sp.writePixel(x, y, color);
	}

	public void update()
	{
		sp.update();
	}

	public void ready()
	{
		sp.ready();
	}

	public void setX(double x)
	{
		offsetX = x;
	}

	public void setY(double y)
	{
		offsetY = y;
	}

	public void setFac(double f)
	{
		fac = f;
	}

	public void setSizeX(int s)
	{
		sizeX = s;
	}

	public void setSizeY(int s)
	{
		sizeY = s;
	}

	public void setJx(double x)
	{
		Jx = x;
	}

	public void setJy(double x)
	{
		Jy = x;
	}

	public void setSP(SetPixel s)
	{
		sp = s;
	}
}
