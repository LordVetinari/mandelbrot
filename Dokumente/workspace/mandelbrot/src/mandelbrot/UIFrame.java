package mandelbrot;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class UIFrame extends JFrame
{
	int X, Y; // The window size in X and Y direction,
				// updated when changing the window size
	MandelLoop m = new CalculationMultiThread();
	Settings s;
	long startTime;
	ArrayList<Settings> last = new ArrayList<Settings>();
	private Graphics2D fg, ig, tmpG;
	BufferedImage bi, iTmp;
	WritableRaster wr;
	double tmpFac;
	boolean controlPressed = false;
	// This is the list of different implementations of the
	// calculation
	String[] classList = new String[] {
			"mandelbrot.CalculationMultiThread",
			// "mandelbrot.CalculationThread",
			// "mandelbrot.Calculation",
			"mandelbrot.CalculationMultiThree",
			// "mandelbrot.CalculationBigDecimal",
			// "mandelbrot.CalcWhite",
			// "mandelbrot.CalcRandomColor",
			// "mandelbrot.CalcColorThree",
			"mandelbrot.CalcSine", "mandelbrot.CalcShip",
			"mandelbrot.CalcJulia" };
	// Following come the variables needed for the button
	// bar and everything on it (until "double exp = 1;")
	JFrame buttons = new JFrame("Controls");
	JButton image = new JButton("Screenshot");
	String userLocation;
	JButton undo = new JButton("Undo");
	JButton redo = new JButton("Redo");
	boolean re, un;
	int index;
	JButton cancel = new JButton("Cancel");
	JComboBox<String> classChooser = new JComboBox<>(
			classList);
	JTextField factor = new JTextField(10);
	DecimalFormat formatter = new DecimalFormat("0.#E0");
	double initFac;
	double exp = 1;
	JTextField JuliaText = new JTextField(
			"Parameters for Julia set: ");
	JTextField JxValue = new JTextField("0", 10);
	JTextField JyValue = new JTextField("0", 10);
	JButton center = new JButton("Centre");

	// This provides a timer so that on window resizing the
	// screen doesn't flicker as much. It is based on
	// javax.swing.timer. The full documentation is included
	// in the class file
	UseLater later = new UseLater()
	{
		@Override
		public void useIt()
		{
			X = getSize().width;
			Y = getSize().height;
			initiate();
			fg = (Graphics2D) getGraphics();
			start();
		}
	};

	// The constructor sets the window size, creates the
	// button bar and implements listeners for window
	// resizing and mouse movements
	public UIFrame(String name, int x, int y)
	{
		super(name);
		X = x;
		Y = y;
		setSize(x, y);
		setButtons();
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// On window resizing this calls a timer so the
		// window is only resized every 100 milliseconds
		this.addComponentListener(new java.awt.event.ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				later.useLater(100);
			}
		});

		KeyAdapter ka = new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == 17)
					controlPressed = true;
			}

			public void keyReleased(KeyEvent e)
			{
				if (e.getKeyCode() == 17)
					controlPressed = false;
			}
		};

		// This extends the abstract class MouseAdapter and
		// overrides listeners for mouse dragging, pressing,
		// releasing and wheel movement.
		MouseAdapter ma = new MouseAdapter()
		{
			int sx, sy, dx, dy;

			@Override
			public void mouseDragged(MouseEvent e)
			{
				dx = e.getX() - sx;
				dy = e.getY() - sy;
				draw();
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				sx = e.getX();
				sy = e.getY();
				dx = dy = 0;
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (controlPressed)
				{
					double ic = (e.getX() + s.getX())
							* s.getFac();
					double jc = (e.getY() + s.getY())
							* s.getFac();
					JxValue.setText(Double.toString(ic));
					JyValue.setText(Double.toString(jc));

				} else
				{
					double drx = (s.getX() + X / 2)
							* (tmpFac - 1);
					double dry = (s.getY() + Y / 2)
							* (tmpFac - 1);
					s.setX(s.getX() - (dx - drx));
					s.setY(s.getY() - (dy - dry));
					s.setFac(s.getFac() / tmpFac);
					exp = 1 / s.getFac() * initFac;
					factor.setText("Factor = "
							+ formatter.format(exp));
					if (re)
					{
						re = false;
						for (int i = index; i < last.size(); i++)
						{
							last.remove(i);
						}
					}
					fillList();
					tmpFac = 1.0;
					dx = dy = 0;
					start();
				}
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				int w = e.getWheelRotation();
				double r = Math.pow(2, w);
				tmpFac *= r;
				exp = (1 / (s.getFac() / tmpFac) * initFac);
				factor.setText("Factor = "
						+ formatter.format(exp));
				draw();
			}

			private void draw()
			{
				m.cancel(true);
				int x1 = (int) ((dx + (X - X * tmpFac) / 2));
				int y1 = (int) ((dy + (Y - Y * tmpFac) / 2));
				int x2 = (int) (x1 + X * tmpFac);
				int y2 = (int) (y1 + Y * tmpFac);
				drawTmp(x1, y1, x2, y2);
			}
		};
		addMouseListener(ma);
		addMouseMotionListener(ma);
		addMouseWheelListener(ma);
		addKeyListener(ka);
	}

	// This method is called in the beginning and on window
	// resizing. It sets/resets the values for the
	// calculation and the button bar and creates a Settings
	// object that can be passed on to the calculation
	// class. It also implements a setPixel method that is
	// doublebuffered and can be called from the calculation
	// class via the Settings object.
	private void initiate()
	{
		last.clear();
		index = 0;
		re = un = false;
		double offsetX = -(X * 5.0 / 7.0);
		double offsetY = -(Y / 2.0);
		double fac = Math.max(3.5 / X, 3.0 / Y);
		factor.setText("Factor = " + formatter.format(1));
		initFac = fac;
		s = new Settings(offsetX, offsetY, fac, X, Y);
		firstList();
		tmpFac = 1.0;
		bi = new BufferedImage(X, Y,
				BufferedImage.TYPE_INT_RGB);
		ig = bi.createGraphics();
		wr = bi.getRaster();
		iTmp = new BufferedImage(X, Y,
				BufferedImage.TYPE_INT_RGB);
		tmpG = iTmp.createGraphics();
		s.setSP(new SetPixel()
		{
			@Override
			public void writePixel(int i, int j,
					float[] color)
			{
				wr.setPixel(i, j, color);
			}

			@Override
			public void update()
			{
				paint(fg);
			}

			public void ready()
			{
				stop();
			}
		});
	}

	// This method is used for drawing the temporary image
	// when zooming
	private void drawTmp(int dx1, int dx2, int dy1, int dy2)
	{
		tmpG.setColor(Color.white);
		tmpG.fillRect(0, 0, X, Y);
		tmpG.drawImage(bi, dx1, dx2, dy1, dy2, 0, 0, X, Y,
				this);
		fg.drawImage(iTmp, 0, 0, this);
	}

	// This is used for doublebuffering
	@Override
	public void paint(Graphics g)
	{
		g.drawImage(bi, 0, 0, this);
	}

	// This method disables the buttons first and then
	// starts the calculation by calling the loop method of
	// the MandelLoop object
	private void start()
	{
		double Jx = Double.parseDouble(JxValue.getText());
		s.setJx(Jx);
		double Jy = Double.parseDouble(JyValue.getText());
		s.setJy(Jy);
		m.cancel(true);
		setWhite();
		image.setEnabled(false);
		undo.setEnabled(false);
		redo.setEnabled(false);
		factor.setEnabled(false);
		JxValue.setEnabled(false);
		JyValue.setEnabled(false);
		startTime = System.currentTimeMillis();
		repaint();
		m.loop(s);
	}

	// This method reenables the buttons and calculates the
	// time that the calculation has taken.
	private void stop()
	{
		long endTime = System.currentTimeMillis();
		long timeTaken = endTime - startTime;
		System.out.println(timeTaken);
		image.setEnabled(true);
		if (un)
		{
			undo.setEnabled(true);
		}
		if (re)
		{
			redo.setEnabled(true);
		}
		factor.setEnabled(true);
		if (m instanceof CalcJulia)
		{
			JxValue.setEnabled(true);
			JyValue.setEnabled(true);
		}
	}

	// This method implements listeners for all of the
	// buttons and creates the button bar
	private void setButtons()
	{
		// This is the listener for the screenshot button.
		// It opens a filechooser and lets the user save the
		// image to a chosen location, adding the parameters
		// and the file extension to the file name
		class ImageListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(image);
				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					File fileToSave = fc.getSelectedFile();
					userLocation = fileToSave
							.getAbsolutePath();
				}
				try
				{
					ImageIO.write(bi, "jpg", new File(
							userLocation + ".jpg"));
				}

				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		// This implements a listener for the undo button,
		// the next is for the redo button
		class UndoListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				s.setX(last.get(index - 2).getX());
				s.setY(last.get(index - 2).getY());
				s.setFac(last.get(index - 2).getFac());
				re = true;
				index--;
				un = index > 1;
				start();
			}
		}
		class RedoListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				s.setX(last.get(index).getX());
				s.setY(last.get(index).getY());
				s.setFac(last.get(index).getFac());
				index++;
				un = true;
				re = index != last.size();
				start();
			}
		}

		class CenterListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				initiate();
				start();
			}
		}
		// This sends a signal to the calculation class that
		// the cancel method should be executed. This method
		// is not implemented in the original class, only in
		// the classes that don't use the swing thread to
		// calculate
		class CancelListener implements ActionListener
		{
			public void actionPerformed(ActionEvent evt)
			{
				m.cancel(false);
			}
		}

		// This is the listener for the dropdown list. When
		// a string is selected, it tries to create a new
		// instance of the selected string and assign it to
		// m. Then it calls start(), which calls the loop()
		// method of the MandelLoop object
		class SelectListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m.cancel(true);
				try
				{
					m = (MandelLoop) Class.forName(
							(String) classChooser
									.getSelectedItem())
							.newInstance();
				} catch (Exception ex)
				{
					System.out.println(ex);
				}
				start();
			}
		}

		// The rest of this method adds the buttons, sets
		// their size when required and adds a listener to
		// each
		buttons.setLayout(new FlowLayout());
		buttons.add(image);
		buttons.add(undo);
		buttons.add(redo);
		buttons.add(factor);
		buttons.add(cancel);
		buttons.add(classChooser);
		classChooser.setSelectedIndex(0);
		buttons.add(JuliaText);
		JuliaText.setEditable(false);
		buttons.add(JxValue);
		buttons.add(JyValue);
		buttons.add(center);

		buttons.pack();
		buttons.setVisible(true);
		buttons.setResizable(false);
		buttons.setAlwaysOnTop(true);

		factor.setEditable(false);
		undo.setEnabled(false);
		redo.setEnabled(false);
		image.addActionListener(new ImageListener());
		undo.addActionListener(new UndoListener());
		redo.addActionListener(new RedoListener());
		cancel.addActionListener(new CancelListener());
		classChooser
				.addActionListener(new SelectListener());
		center.addActionListener(new CenterListener());
	}

	// This method is called before the loop is executed. It
	// sets the screen as black, so only the points that
	// don't belong to the set have to be drawn.
	private void setWhite()
	{
		ig.setColor(Color.white);
		ig.fillRect(0, 0, X, Y);
	}

	// The following two methods fill the stack for the undo
	// button. firstList() is executed in the initiate
	// method after the old stack is cleared, fillList() is
	// executed upon every new calculation that is caused by
	// mouse movements
	private void firstList()
	{
		last.add(s);
		index++;
	}

	private void fillList()
	{
		last.add(s);
		index++;
		un = true;
	}

	// The main method creates a new UIFrame object
	public static void main(String args[])
	{
		@SuppressWarnings("unused")
		UIFrame frame = new UIFrame("Mandelbrot Set", 1800,
				1000);
	}
}