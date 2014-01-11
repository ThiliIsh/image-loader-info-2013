package ImageLoader;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ImageUtil {
	private static int nrOfTimesDrown = 0;

	// load image from File Chooser
	public static BufferedImage loadImage() {
		JFileChooser dialog = new JFileChooser("res/images");
		if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				return ImageIO.read(dialog.getSelectedFile());
			} catch (IOException e) {
				System.out
						.println("Freaky error, very freaky error! (Might not be an image)");
				e.printStackTrace();
			}
		}
		return null;
	}

	// load image from path
	public static BufferedImage loadImage(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("Error: Probably can't find file: " + path);
			e.printStackTrace();
		}
		return img;
	}

	// display image from File Chooser in separate window.
	public static void displayImage() {
		displayImage(loadImage());
	}

	// display image in separate window.
	public static void displayImage(BufferedImage img) {
		if (img == null) {
			return;
		}

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (nrOfTimesDrown >= 8) {
			nrOfTimesDrown = 0;
		}
		frame.setLocation(nrOfTimesDrown * 50, nrOfTimesDrown * 50);
		nrOfTimesDrown++;

		ImagePanel panel = new ImagePanel();
		panel.setImage(img);

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	// get image properties
	public static String getImageProperties(BufferedImage img) {
		StringBuilder info = new StringBuilder();
		if (img == null)
			info.append("No valide image!");
		else {
			info.append("Width: " + img.getWidth() + " Height: "
					+ img.getHeight() + "\r\n");
			info.append("Pixel size: " + img.getColorModel().getPixelSize()
					+ "bits \r\n");
			info.append("Number of components: "
					+ img.getColorModel().getNumComponents() + "\r\n");
			info.append("Color components: "
					+ img.getColorModel().getNumColorComponents() + "\r\n");
			info.append("Number of bands: " + img.getRaster().getNumBands()
					+ "\r\n");
		}
		return info.toString();
	}

	public static void showImageProperties(Component parentComponent,
			BufferedImage img) {
		JOptionPane.showMessageDialog(parentComponent, getImageProperties(img),
				"Image Properties", JOptionPane.INFORMATION_MESSAGE);
	}

	public static BufferedImage toBufferedImage(Image image) {

		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();

		return bufferedImage;
	}

	protected int printRGB(int pixel) {
		int r = (0x00FF0000 & pixel) >> 16;
		int g = (0x0000FF00 & pixel) >> 8;
		int b = 0x000000FF & pixel;

		System.out.println("R= " + r + " G= " + g + " B= " + b);

		return 0xFF000000 | (r << 16) | (g << 8) | b;
	}

	public static BufferedImage getScreenShot() {
		BufferedImage screenShot = null;
		Robot robot;

		try {
			robot = new Robot();
			screenShot = robot.createScreenCapture(new Rectangle(Toolkit
					.getDefaultToolkit().getScreenSize()));
		} catch (AWTException e) {
			e.printStackTrace();
		}

		return screenShot;
	}	
	
	// processing
	public static BufferedImage simpleNoise(BufferedImage img) {

		BufferedImage dest = null;
		Random rand = new Random();

		dest = new BufferedImage(img.getWidth(), img.getHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);

		for (int x = 0; x < dest.getWidth(); x++)
			for (int y = 0; y < dest.getHeight(); y++) {
				dest.getRaster().setSample(x, y, 0, rand.nextInt(255));
				dest.getRaster().setSample(x, y, 1, rand.nextInt(255));
				dest.getRaster().setSample(x, y, 2, rand.nextInt(255));
				dest.getRaster().setSample(x, y, 3, 255);
			}
		return dest;

	}

	// flip image horizontally
	public static BufferedImage flipImageHorizontally(BufferedImage img) {
		BufferedImage flipped = new BufferedImage(img.getWidth(),
				img.getHeight(), img.getType());
		for (int y = 0; y < img.getHeight() / 2; y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int pixel = img.getRGB(x, y);
				flipped.setRGB(x, y, img.getRGB(x, (img.getHeight() - 1) - y));
				flipped.setRGB(x, (img.getHeight() - 1) - y, pixel);
			}
		}
		return flipped;
	}

	// flip image vertically
	public static BufferedImage flipImageVertically(BufferedImage img) {
		BufferedImage flipped = new BufferedImage(img.getWidth(),
				img.getHeight(), img.getType());
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth() / 2; x++) {
				int pixel = img.getRGB(x, y);
				flipped.setRGB(x, y, img.getRGB((img.getWidth() - 1) - x, y));
				flipped.setRGB((img.getWidth() - 1) - x, y, pixel);
			}
		}
		return flipped;
	}

	public static BufferedImage pixelate(BufferedImage img, int size) {
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(),
				img.getType());
		if (((img.getWidth() % size) != 0) || (img.getHeight() % size) != 0) {
			System.out.println("inappropriate pixel size");
			return null;
		}
		for (int x = 0; x < img.getWidth(); x += size) {
			for (int y = 0; y < img.getHeight(); y += size) {

				int px = 0;

				for (int xi = 0; xi < size; xi++) {
					for (int yi = 0; yi < size; yi++) {
						px += img.getRGB(x, y);
						px = px / 2;
					}
				}

				for (int xi = 0; xi < size; xi++) {
					for (int yi = 0; yi < size; yi++) {
						dest.setRGB(x + xi, y + yi, px);
					}
				}
			}
		}

		return dest;
	}

	public static BufferedImage colorToGray(BufferedImage input) {
		BufferedImage dest = null;

		dest = new BufferedImage(input.getWidth(), input.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		int sum, b;
		for (int y = 0; y < input.getHeight(); y++)
			for (int x = 0; x < input.getWidth(); x++) {
				sum = 0;
				for (b = 0; b < input.getRaster().getNumBands(); b++)
					sum = sum + input.getRaster().getSample(x, y, b);

				dest.getRaster().setSample(x, y, 0, sum / b);

			}

		return dest;
	}

	public static BufferedImage toGrayscale(BufferedImage img, int version) {
		// version 0 - averaged (media aritmetica)
		// version 1 - weighted (media ponderata)
		// To Do: working only for BGR formats !!!
		BufferedImage dest = null;
		int red, green, blue;
		if ((img.getType() == BufferedImage.TYPE_INT_BGR)
				|| (img.getType() == BufferedImage.TYPE_3BYTE_BGR)
				|| (img.getType() == BufferedImage.TYPE_4BYTE_ABGR)
				|| (img.getType() == BufferedImage.TYPE_4BYTE_ABGR_PRE)) {

			dest = new BufferedImage(img.getWidth(), img.getHeight(),
					BufferedImage.TYPE_BYTE_GRAY);

			for (int x = 0; x < img.getWidth(); x++)
				for (int y = 0; y < img.getHeight(); y++) {
					red = img.getRaster().getSample(x, y, 0);
					green = img.getRaster().getSample(x, y, 1);
					blue = img.getRaster().getSample(x, y, 2);

					switch (version) {
					case 0: {
						dest.getRaster().setSample(x, y, 0,
								(red + green + blue) / 3);
						break;
					}
					case 1: {
						dest.getRaster().setSample(x, y, 0,
								(red * 0.3 + green * 0.59 + blue * 0.11));

						break;
					}
					}
				}
		} else {
			JOptionPane.showMessageDialog(null,
					"Wrong image format! No color image", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return (dest == null) ? img : dest;
	}

	// LUT operations

	public static BufferedImage negativate(BufferedImage input) {
		BufferedImage dest = null;

		int LUTnegative[] = new int[256];
		for (int i = 0; i < 256; i++) {
			LUTnegative[i] = 255 - i;
		}

		dest = new BufferedImage(input.getWidth(), input.getHeight(),
				input.getType());
		int pixel;
		for (int y = 0; y < input.getHeight(); y++)
			for (int x = 0; x < input.getWidth(); x++)
				for (int b = 0; b < input.getRaster().getNumBands(); b++) {
					pixel = input.getRaster().getSample(x, y, b);
					dest.getRaster().setSample(x, y, b, LUTnegative[pixel]);
				}
		return dest;
	}

	public static BufferedImage posterize(BufferedImage img) {
		BufferedImage dest = null;
		dest = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		short[] posterizeLut = new short[256];
		for (short i = 0; i < 256; i++) {
			posterizeLut[i] = (short) (i - (i % 32));
		}

		ShortLookupTable lut = new ShortLookupTable(0, posterizeLut);
		LookupOp lop = new LookupOp(lut, null);
		lop.filter(img, dest);

		return dest;
	}

	public static BufferedImage threshold(BufferedImage input, int value) {
		BufferedImage dest = null;

		if (input.getType() != BufferedImage.TYPE_BYTE_GRAY) {
			JOptionPane.showMessageDialog(null, "Not a Gray image!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return input;
		} else {

			dest = new BufferedImage(input.getWidth(), input.getHeight(),
					BufferedImage.TYPE_BYTE_BINARY);
			int pixel;
			for (int y = 0; y < input.getHeight(); y++)
				for (int x = 0; x < input.getWidth(); x++) {

					pixel = input.getRaster().getSample(x, y, 0);
					if (pixel < value)
						dest.getRaster().setSample(x, y, 0, 0);
					else
						dest.getRaster().setSample(x, y, 0, 1);
				}
		}
		return dest;
	}

	public static BufferedImage logContrast(BufferedImage input) {
		BufferedImage dest = null;

		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		int pix;

		// find max and min
		for (int i = 0; i < input.getWidth(); i++)
			for (int j = 0; j < input.getHeight(); j++) {
				pix = input.getRaster().getSample(i, j, 0);
				if (pix > max)
					max = pix;
				if (pix < min)
					min = pix;
			}

		short LUT_log[] = new short[256];

		for (int i = 0; i < 256; i++) {
			LUT_log[i] = (short) ((255.0 / Math.log(1.0 + max)) * Math
					.log(1.0 + i));
//			LUT_log[i] = (short) ((255.0 / Math.log(1.0 + 255)) * Math
//					.log(1.0 + i));

			System.out.print(LUT_log[i] + " ");
		}

		normalize(LUT_log,0,255);

		dest = new BufferedImage(input.getWidth(), input.getHeight(),
				input.getType());

		ShortLookupTable sLUT = new ShortLookupTable(0, LUT_log);
		LookupOp op = new LookupOp(sLUT, null);
		op.filter(input, dest);

		return dest;
	}
	
	public static BufferedImage expContrast(BufferedImage img) {
		BufferedImage dest = null;

		dest = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		short[] exp_LUT = new short[256];
		// build LUT
		for (short i = 0; i < 256; i++) {
//			double t = (double) i / 255.0; // scale value to range 0..1
//			t = (double) ((Math.exp(t) - 1.0) / (Math.exp(1.0) - 1.0)); //compute exp
//			t = t * 255.0; // scale back to 0..255
//			exp_LUT[i] = (short) t;
			exp_LUT[i] = (short) (255.0*((Math.exp(i/255.) - 1.0) / (Math.exp(1.0) - 1.0)));
			
			//System.out.print(exp_LUT[i] + " ");
		}
		//System.out.println();
		ShortLookupTable lut = new ShortLookupTable(0, exp_LUT);
		LookupOp lop = new LookupOp(lut, null);
		lop.filter(img, dest);

		return dest;
	}
	
	public static BufferedImage gammaContrast(BufferedImage img, double gamma) {
		BufferedImage dest = null;

		dest = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		short[] gamma_LUT = new short[256];
		for (short i = 0; i < 256; i++) {
			double a = (double) i / 255.0; // scale to [0..1]
			double b = Math.pow(a, 1.0/gamma); // gamma function
			gamma_LUT[i] = (short) Math.round(b * 255.0); // scale back to [0..255]
			
			//System.out.print(gamma_LUT[i] + " ");

		}
		//System.out.println();
		ShortLookupTable lut = new ShortLookupTable(0, gamma_LUT);
		LookupOp lop = new LookupOp(lut, null);
		lop.filter(img, dest);

		return dest;
	}	
	
	// linear normalize an int value
	static int normalize(int val, int oldMin, int oldMax, int newMin, int newMax) {
		// compute scale factor
		double c = 1.0 * (newMax - newMin) / (oldMax - oldMin);
		// apply scale and offset
		return (int) (c * (val - oldMin) + newMin);
	}
	
	// normalize a LUT
	static void normalize(short[] lut, int newMin, int newMax) {
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;

		// find max and min
		for (int i = 0; i < lut.length; i++) {
			if (lut[i] > max)
				max = lut[i];
			if (lut[i] < min)
				min = lut[i];
		}

		// (x-min)(255-0)/(max-min)+0
		// (x-min)(newMax-newMin)/(max-min)+newMin
		double c = 1.0 * (newMax - newMin) / (max - min);

		System.out.println("\r\n"+"max= "+max+" min= "+min+" c= "+c );
		// build output
		for (int i = 0; i < lut.length; i++) {
			lut[i] = (short) ((lut[i] - min) * c + newMin);
			System.out.print(lut[i] + " ");
		}
		// System.out.println("\r\n"+"max= "+max+" min= "+min+" c= "+c );
	}
	
	// normalize/stretch contrast
	public static BufferedImage contrastStretching(BufferedImage input) {
		BufferedImage dest = null;

		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		int pix;

		// find max and min
		for (int i = 0; i < input.getWidth(); i++)
			for (int j = 0; j < input.getHeight(); j++) {
				pix = input.getRaster().getSample(i, j, 0);
				if (pix > max)
					max = pix;
				if (pix < min)
					min = pix;
			}

		System.out.println("Contrast stretch: max= " + max + " min= " + min);

		short LUT_stretch[] = new short[256];

		for (int i = min; i < max + 1; i++) {
			LUT_stretch[i] = (short) normalize(i, min, max, 0, 255);

			// System.out.print(LUT_stretch[i] +" ");
		}

		for (int i = 0; i < 256; i++) {
			System.out.print(LUT_stretch[i] + " ");
		}

		dest = new BufferedImage(input.getWidth(), input.getHeight(),
				input.getType());

		ShortLookupTable sLUT = new ShortLookupTable(0, LUT_stretch);
		LookupOp op = new LookupOp(sLUT, null);
		op.filter(input, dest);

		return dest;
	}

	public static BufferedImage brightnessV1(BufferedImage input, int offset) {
		BufferedImage dest = null;

		short LUT_brightness[] = new short[256];
		for (int i = 0; i < 256; i++) {
			if (i + offset > 255) {
				LUT_brightness[i] = 255;
			} else {
				if (i + offset < 0)
					LUT_brightness[i] = 0;
				else {
					LUT_brightness[i] = (short) (i + offset);
				}
			}
		}

		dest = new BufferedImage(input.getWidth(), input.getHeight(),
				input.getType());

		ShortLookupTable sLUT = new ShortLookupTable(0, LUT_brightness);
		LookupOp op = new LookupOp(sLUT, null);
		op.filter(input, dest);

		return dest;
	}

	public static BufferedImage brightnessV2(BufferedImage input, int offset) {
		BufferedImage dest = null;
		dest = new BufferedImage(input.getWidth(), input.getHeight(),
				input.getType());

		RescaleOp op = new RescaleOp(1, offset, null);
		op.filter(input, dest);

		return dest;
	}

	public static BufferedImage contrast(BufferedImage input, float scale) {
		BufferedImage dest = null;

		short LUT_contrast[] = new short[256];
		for (int i = 0; i < 256; i++) {
			if (scale * i > 255.0) {
				LUT_contrast[i] = 255;
			} else {
				LUT_contrast[i] = (short) (scale * i);
			}
		}

		dest = new BufferedImage(input.getWidth(), input.getHeight(),
				input.getType());

		ShortLookupTable sLUT = new ShortLookupTable(0, LUT_contrast);
		LookupOp op = new LookupOp(sLUT, null);
		op.filter(input, dest);

		return dest;
	}

	public static BufferedImage RGBbalanceV1(BufferedImage input, int rOffset,
			int gOffset, int bOffset) {
		BufferedImage dest = null;

		// one LUT for each band
		short LUT_R[] = new short[256];
		short LUT_G[] = new short[256];
		short LUT_B[] = new short[256];

		short LUT_RGB[][] = { LUT_R, LUT_G, LUT_B };

		for (int i = 0; i < 256; i++) {
			LUT_R[i] = (short) i;
			LUT_G[i] = (short) i;
			LUT_B[i] = (short) i;
		}

		for (int i = 0; i < 256; i++) {
			if (rOffset + i > 255) {
				LUT_R[i] = 255;
			} else if (rOffset + i < 0)
				LUT_R[i] = 0;
			else {
				LUT_R[i] = (short) (rOffset + i);
			}
		}
		for (int i = 0; i < 256; i++) {
			if (gOffset + i > 255) {
				LUT_G[i] = 255;
			} else if (gOffset + i < 0)
				LUT_G[i] = 0;
			else {
				LUT_G[i] = (short) (gOffset + i);
			}
		}
		for (int i = 0; i < 256; i++) {
			if (bOffset + i > 255) {
				LUT_B[i] = 255;
			} else if (bOffset + i < 0)
				LUT_B[i] = 0;
			else {
				LUT_B[i] = (short) (bOffset + i);
			}
		}

		dest = new BufferedImage(input.getWidth(), input.getHeight(),
				input.getType());
		ShortLookupTable sLUT = new ShortLookupTable(0, LUT_RGB);
		LookupOp op = new LookupOp(sLUT, null);
		op.filter(input, dest);

		return dest;
	}

	public static BufferedImage RGBbalanceV2(BufferedImage input, int rOffset,
			int gOffset, int bOffset) {
		BufferedImage dest = null;

		dest = new BufferedImage(input.getWidth(), input.getHeight(),
				input.getType());

		RescaleOp rop = new RescaleOp(new float[] { 1, 1, 1 }, new float[] {
				rOffset, gOffset, bOffset }, null);

		rop.filter(input, dest);

		return dest;
	}

	public static BufferedImage histogramThreshold(BufferedImage img,
			int threshold) {

		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(),
				img.getType());

		int reds[] = new int[256];
		int greens[] = new int[256];
		int blues[] = new int[256];

		// For each color band (R, G, B)
		// Find out how many pixels correspond to a gray level, (from 0 to 255)

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int px = img.getRGB(x, y);

				int red = ((px >> 16) & 0xFF);
				reds[red]++;

				int green = ((px >> 8) & 0xFF);
				greens[green]++;

				int blue = (px & 0xFF);
				blues[blue]++;
			}
		}

		// Find most common gray level values for each color band
		int mostCommonRed = 0;
		int mostCommonBlue = 0;
		int mostCommonGreen = 0;

		for (int i = 0; i < 256; i++) {
			if (reds[i] > mostCommonRed) {
				mostCommonRed = i;
			}

			if (blues[i] > mostCommonBlue) {
				mostCommonBlue = i;
			}

			if (greens[i] > mostCommonGreen) {
				mostCommonGreen = i;
			}
		}

		// Take only the pixels in threshold range of the most common value for
		// each band
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int pix = img.getRGB(x, y);

				int red = ((pix >> 16) & 0xFF);
				int green = ((pix >> 8) & 0xFF);
				int blue = (pix & 0xFF);
				int val = 0;

				if (((red - threshold < mostCommonRed) && (red + threshold > mostCommonRed))
						|| ((blue - threshold < mostCommonBlue) && (blue
								+ threshold > mostCommonBlue))
						|| ((green - threshold < mostCommonGreen) && (green
								+ threshold > mostCommonGreen))) {

					val = pix;
				} else {
					val = 0;
				}

				dest.setRGB(x, y, val);
			}
		}

		return dest;
	}
	
// Convolve operations
	
	public static BufferedImage blur(BufferedImage img) {
		BufferedImage dest = null;
		dest = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		float val = 1.0f / 25.0f;
		float[] blurKernel = new float[25];
		for (int i = 0; i < blurKernel.length; i++) {
			blurKernel[i] = val;
		}
		//ConvolveOp cv = new ConvolveOp(new Kernel(5, 5, blurKernel));
		 ConvolveOp cv= new ConvolveOp(new Kernel(5, 5,
		 blurKernel),ConvolveOp.EDGE_NO_OP,null);
		// ConvolveOp cv= new ConvolveOp(new Kernel(5, 5,
		// blurKernel),ConvolveOp.EDGE_NO_OP,null);

		cv.filter(img, dest);
		return dest;
	}

	public static BufferedImage sharpen(BufferedImage img) {
		BufferedImage dest = null;
		dest = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		float[] sharpKernel = { 0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f,
				-1.0f, 0.0f };

		ConvolveOp cv = new ConvolveOp(new Kernel(3, 3, sharpKernel));

		cv.filter(img, dest);
		return dest;
	}

	public static BufferedImage edge(BufferedImage img) {
		BufferedImage dest = null;
		dest = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		float[] edgeKernel = { 	0.0f, -1.0f, 0.0f, 
								-1.0f,	4.0f, -1.0f, 
								0.0f, -1.0f, 0.0f };

		ConvolveOp cv = new ConvolveOp(new Kernel(3, 3, edgeKernel));

		cv.filter(img, dest);
		return dest;
	}

	public static BufferedImage edgeW(BufferedImage bi) {
		BufferedImage buff = new BufferedImage(bi.getWidth(), bi.getHeight(),
				bi.getType());

		Kernel kernel = new Kernel(3, 3, new float[] { -1f, 0f, 1f, -2f, 0f,
				2f, -1f, 0f, 1f });

		ConvolveOp op = new ConvolveOp(kernel);
		op.filter(bi, buff);

		return buff;
	}

	public static BufferedImage edgeH(BufferedImage bi) {
		BufferedImage buff = new BufferedImage(bi.getWidth(), bi.getHeight(),
				bi.getType());

		Kernel kernel = new Kernel(3, 3, new float[] { -1f, -2f, -1f, 0f, 0f,
				0f, 1f, 2f, 1f });

		ConvolveOp op = new ConvolveOp(kernel);
		op.filter(bi, buff);

		return buff;
	}

	static public BufferedImage GoL(BufferedImage image){
		//BufferedImage dest = null;
		int COLOR = 0x484848;
		final ConvolveOp convolve = new ConvolveOp(new Kernel(3, 3, new float[]{
		        1, 1, 1,
		        1, 0.5f, 1,
		        1, 1, 1}));

		final LookupOp lookup;
	    final short[] lookupTable = new short[256];
	    lookupTable[180] = 0x48;
	    lookupTable[216] = 0x48;
	    lookupTable[252] = 0x48;
	    lookup = new LookupOp(new ShortLookupTable(0, lookupTable), null);

	    //nextGeneration
		    image=convolve.filter(image, null);
		    lookup.filter(image, image);
		    //generation++;
		return image;

	}	
////////////////////////////////////
	
	public static BufferedImage blend(BufferedImage img1, BufferedImage img2,
			float weight) {
		BufferedImage dest = null;
		if (img1.getType() != img2.getType()) {
			JOptionPane.showMessageDialog(null, "Image type mismatch!");
			return null;
		}

		int w = Math.min(img1.getWidth(), img2.getWidth());
		int h = Math.min(img1.getHeight(), img2.getHeight());

		dest = new BufferedImage(w, h, img1.getType());

		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++) {
				for (int i = 0; i < img1.getRaster().getNumBands(); i++) {

					float gray = weight * img1.getRaster().getSample(x, y, i)
							+ (1.0f - weight)
							* img2.getRaster().getSample(x, y, i);
					dest.getRaster().setSample(x, y, i, Math.round(gray));

				}
			}
		return dest;
	}

	public static BufferedImage logical(BufferedImage img, BufferedImage mask,
			int logicOp) {
		BufferedImage dest = null;
		if (mask.getType() != BufferedImage.TYPE_BYTE_GRAY) {
			JOptionPane.showMessageDialog(null, "No binary image!", null,
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		int w = Math.min(img.getWidth(), mask.getWidth());
		int h = Math.min(img.getHeight(), mask.getHeight());

		dest = new BufferedImage(w, h, img.getType());

		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++) {
				int binary = mask.getRaster().getSample(x, y, 0);

				for (int i = 0; i < img.getRaster().getNumBands(); i++) {
					int gray = img.getRaster().getSample(x, y, i);
					switch (logicOp) {
					case 0: {
						dest.getRaster().setSample(x, y, i, gray & binary);
						break;
					}
					case 1: {
						dest.getRaster().setSample(x, y, i, gray | binary);
						break;
					}
					case 2: {
						dest.getRaster().setSample(x, y, i, gray ^ binary);
						break;
					}
					}

				}
			}
		return dest;
	}

	static public BufferedImage getBitPlanes(BufferedImage bi, int plane) {
		if (bi.getType() != BufferedImage.TYPE_BYTE_GRAY) {
			JOptionPane.showMessageDialog(null, "Not a Gray image!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return bi;
		}
		
		int level = 0;

		switch (plane) {
		case 0:
			level = 1;
			break;
		case 1:
			level = 2;
			break;
		case 2:
			level = 4;
			break;
		case 3:
			level = 8;
			break;
		case 4:
			level = 16;
			break;
		case 5:
			level = 32;
			break;
		case 6:
			level = 64;
			break;
		case 7:
			level = 128;
			break;
		default:
			return null;
		}

		int width = bi.getWidth();
		int height = bi.getHeight();

		//BufferedImage img = new BufferedImage(width, height, bi.getType());
		BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		int px;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++){
			//	img.setRGB(x, y, ((bi.getRGB(x, y) & level) / level) * 255);
				px = (bi.getRaster().getSample(x, y, 0) & level);
				dest.getRaster().setSample(x, y, 0, (px>0) ? 1 : 0);
			}

		return dest;
	}	
}
