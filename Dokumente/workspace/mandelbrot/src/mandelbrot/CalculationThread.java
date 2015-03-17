package mandelbrot;

public class CalculationThread extends Calculation
{
	Thread th;

	@Override
	public void cancel(boolean wait)
	{
		if (wait && th != null)
		{
			c = true;
			try
			{
				th.join();
			} catch (InterruptedException ex)
			{
				System.out.println(ex);
			}
		}
		c = true;
	}

	@Override
	public void loop(final Settings s)
	{
		th = new Thread(new Runnable()
		{
			public void run()
			{
				setTable();
				c = false;
				setParameters(s);
				if (lim < MIN_LIM)
				{
					lim = MIN_LIM;
				}
				for (int i = 0; i < 100; i++)
				{
					if (c)
					{
						s.update();
						break;
					}
					calc(f.get(i), s);
					s.update();
				}
				s.ready();
			}
		});
		c = false;
		th.start();
	}
}
