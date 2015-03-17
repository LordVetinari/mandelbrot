package mandelbrot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

/**
 * @author Holger Gutermuth & Georg Pollitt
 */
abstract public class UseLater implements Runnable
{
	/**
	 * Timer-Class for additional delay with mmUseLater.
	 */
	@SuppressWarnings("serial")
	static class MmLittleLater extends javax.swing.Timer
	{
		/**
		 * @param ms
		 *            milliseconds for delay
		 * @param use
		 *            action manager that wants to use the
		 *            timer
		 */
		MmLittleLater(int ms, final UseLater use)
		{
			super(ms, null);
			addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					use.littleLater = null;
					SwingUtilities.invokeLater(use);
				}
			});
			setRepeats(false);
			super.start();
		}
	}

	/**
	 * Here the desired action takes place
	 */
	abstract public void useIt();

	/**
	 * Internal flag, true, when action is requested.
	 */
	boolean used;
	/**
	 * Timer that can be used on demand
	 */
	MmLittleLater littleLater;
	/**
	 * A chain of events can be triggered by inserting a new
	 * event here that has to be checked first.
	 */
	UseLater before;

	/**
	 * Run event if registered. First before is checked.
	 */
	public void run()
	{
		if (before != null)
		{
			before.run();
		}
		if (used)
		{
			used = false;
			if (littleLater != null)
			{
				littleLater.stop();
				littleLater = null;
			}
			useIt();
		}
	}

	/**
	 * Request delayed event. For ms > 0 an additional timer
	 * is used, otherwise invokeLater.
	 * 
	 * @param ms
	 *            milliseconds.
	 */
	public void useLater(int ms)
	{
		if (!used)
		{
			used = true;
			if (ms > 0)
			{
				littleLater = new MmLittleLater(ms, this);
			} else
			{
				SwingUtilities.invokeLater(this);
			}
		}
	}

	/**
	 * This event is always executed. First before is
	 * checked and executed if existing and waiting.
	 * Invocation may be done from another thread, then
	 * invokeAndWait is used.
	 */
	public void useDirekt()
	{
		used = true;
		if (SwingUtilities.isEventDispatchThread())
		{
			// MmLog.log("evtdispatch direct");
			run();
		} else
		{
			try
			{
				// MmLog.log("evtdispatch wait");
				SwingUtilities.invokeAndWait(this);
			} catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public boolean used()
	{
		return used;
	}

	/**
	 * Creates another MmUseLater before every run. bef ==
	 * null deletes the chain, otherwise the new element
	 * will be put in front of the potentially already
	 * existing chain.
	 * 
	 * @param bef
	 */
	public void setBefore(UseLater bef)
	{
		if (bef == null)
		{
			before = null;
		} else
		{
			bef.before = before;
			before = bef;
		}
	}

	public void reset()
	{
		used = false;
		before = null;
		if (littleLater != null)
		{
			littleLater.stop();
			littleLater = null;
		}
	}
}