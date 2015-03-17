package mandelbrot;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CalculationMultiThread extends
		CalculationThread
{
	int limit = 10;
	@Override
	public void loop(final Settings s)
	{
		newThread(s);
	}

	protected void newThread(final Settings s)
	{
		th = new Thread(new Runnable()
		{
			public void run()
			{
				setTable();
				final Future<?>[] futures = new Future[100];
				c = false;
				setParameters(s);
				if (lim < MIN_LIM)
				{
					lim = MIN_LIM;
				}
				final ExecutorService e = Executors
						.newFixedThreadPool(Runtime
								.getRuntime()
								.availableProcessors() - 1);

				for (int i = 0; i < 100; i++)
				{
					final int fi = i;
					if (c)
					{
						s.update();
						e.shutdownNow();
						break;
					}
					futures[i] = e.submit(new Runnable()
					{
						public void run()
						{
							if (c)
								return;
							calc(f.get(fi), s);
							s.update();
						}
					});
				}
				for (Future<?> future : futures)
				{
					if (!future.isDone())
					{
						try
						{
							future.get();
						} catch (InterruptedException
								| ExecutionException e1)
						{
						}
					}
				}
				e.shutdown();
				s.ready();
			}
		});
		th.start();
	}
}
