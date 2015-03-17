package mandelbrot;

public interface MandelLoop
{
	// lim = 100 * (int) Math.log(1 / fac); This makes sure
	// that lim does not become too small
	static public int MIN_LIM = 30;

	// This method is called when it is desired that the
	// calculation thread is stopped
	public void cancel(boolean wait);

	// This method starts the calculation
	public void loop(Settings s);
}
