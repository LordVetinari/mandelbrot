package mandelbrot;

public interface SetPixel
{
	// This method is used by the calculation class to set a
	// pixel to a specific color
	public void writePixel(int i, int j, float[] color);

	// This method is used to update the frame. Thus the
	// update method is even triple buffered
	public void update();

	// This method is used by the calculation class to
	// signal the frame that the calculation is over
	public void ready();
}
