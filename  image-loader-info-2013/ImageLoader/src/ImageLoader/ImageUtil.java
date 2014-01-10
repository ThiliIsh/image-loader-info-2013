package ImageLoader;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
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
	public static BufferedImage loadImage(){
		JFileChooser dialog = new JFileChooser("res/images");
		if(dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			try {
				return ImageIO.read(dialog.getSelectedFile());
			} catch (IOException e) {
				System.out.println("Freaky error, very freaky error! (Might not be an image)");
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
	public static void displayImage(){
		displayImage(loadImage());
	}
	
	// display image in separate window.
	public static void displayImage(BufferedImage img) {
		if(img == null){
			return;
		}
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if(nrOfTimesDrown >= 8){
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
	
	public static void showImageProperties(Component parentComponent, BufferedImage img) {
		JOptionPane.showMessageDialog(parentComponent, getImageProperties(img), "Image Properties", JOptionPane.INFORMATION_MESSAGE);
	}	
	
    public static BufferedImage toBufferedImage(Image image) {

    	BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2 = bufferedImage.createGraphics();
    	g2.drawImage(image, 0, 0, null);
    	g2.dispose();

    	return bufferedImage;
    }
	
    protected int printRGB(int pixel) {
        int r = (0x00FF0000 & pixel) >> 16;
        int g = (0x0000FF00 & pixel) >> 8;
        int b = 0x000000FF & pixel;

        System.out.println("R= "+r+" G= "+g+" B= "+b);
        
        return 0xFF000000 | (r << 16) | (g << 8) | b;
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
		BufferedImage flipped = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int y = 0; y < img.getHeight() / 2; y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int pixel = img.getRGB(x, y);
				flipped.setRGB(x, y, img.getRGB(x, (img.getHeight()-1) - y));
				flipped.setRGB(x, (img.getHeight()-1) - y, pixel);
			}
		}
		return flipped;
	}
	
	// flip image vertically
	public static BufferedImage flipImageVertically(BufferedImage img) {
		BufferedImage flipped = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth() / 2; x++) {
				int pixel = img.getRGB(x, y);
				flipped.setRGB(x, y, img.getRGB((img.getWidth()-1) - x, y));
				flipped.setRGB((img.getWidth()-1) - x, y, pixel);
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
// LUT operations
	
	public static BufferedImage negativate(BufferedImage input) {
		BufferedImage dest = null;
		
		int LUTnegative[] = new int[256];
		for (int i = 0; i < 256; i++) {
			LUTnegative[i] = 255- i;
		}
			
			dest = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
			int pixel;
			for (int y = 0; y < input.getHeight(); y++) 
				for (int x = 0; x < input.getWidth(); x++)
					for(int b = 0; b<input.getRaster().getNumBands(); b++){
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
	
	public static BufferedImage colorToGray(BufferedImage input) {
		BufferedImage dest = null;
			
		dest = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		int sum, b;
		for (int y = 0; y < input.getHeight(); y++) 
			for (int x = 0; x < input.getWidth(); x++){
				sum = 0;
					for(b = 0; b<input.getRaster().getNumBands(); b++)
						sum = sum + input.getRaster().getSample(x, y, b);
					
				dest.getRaster().setSample(x, y, 0, sum/b);
				
			}
		
		return dest;
	}
	public static BufferedImage threshold(BufferedImage input, int prag) {
		BufferedImage dest = null;
		
		
		dest = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		int pixel;
		for (int y = 0; y < input.getHeight(); y++) 
			for (int x = 0; x < input.getWidth(); x++){

					pixel = input.getRaster().getSample(x, y, 0);
					if (pixel < prag)
						dest.getRaster().setSample(x, y, 0, 0);
					else
						dest.getRaster().setSample(x, y, 0, 1);
			}
		
		return dest;
	}
	
//	static int normalize(int val, int oldMin, int oldMax, int newMin, int newMax){
//		
//		double c =1.0*(newMax-newMin)/(oldMax-oldMin);
//		return (int) ((val-oldMin)*c + newMin);
//	}
	
	static void normalize(short[] lut, int newMin, int newMax){
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		
		// find max and min
		for(int i=0;i<lut.length;i++){
				if(lut[i] > max)
					max = lut[i];
				if(lut[i] < min)
					min = lut[i];
			}
		
		//  (x-min)(255-0)/(max-min)+0
		//  (x-min)(newMax-newMin)/(max-min)+newMin
		double c =1.0*(newMax-newMin)/(max-min);
		
		//build output
		for(int i=0;i<lut.length;i++){
			lut[i] = (short) ((lut[i]-min)*c+newMin);
			System.out.print(lut[i]+" ");
		}

		System.out.println("\r\n"+"max= "+max+" min= "+min+" c= "+c );		
	}
	
	public static BufferedImage logContrast(BufferedImage input) {
		BufferedImage dest = null;
		
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		int pix;
		
		// find max and min
		for(int i=0;i<input.getWidth();i++)
			for(int j=0;j<input.getHeight();j++){
				pix = input.getRaster().getSample(i, j, 0);
				if(pix > max)
					max = pix;
				if(pix < min)
					min = pix;
			}
		
		System.out.println("max= "+max+" min= "+min );
		
		short LUT_log[] = new short[256];
		
		for (int i = 0; i < 256; i++) {
			LUT_log[i] = (short)((255.0 / Math.log(1.0 + max)) * Math.log(1.0+i));
			
			System.out.print(LUT_log[i] +" ");
		}
		
//		normalize(LUT_log,100,255);

		
			dest = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
			
			ShortLookupTable sLUT = new ShortLookupTable(0, LUT_log);
			LookupOp op = new LookupOp(sLUT, null);
			op.filter(input, dest);
			
		return dest;
	}
	
	static int normalize(int val, int oldMin, int oldMax, int newMin, int newMax){
		// compute scale factor
		double c = 1.0 * (newMax - newMin) / (oldMax - oldMin);
		// apply scale and offset
		return (int)(c * (val - oldMin) + newMin);
	}
	
	public static BufferedImage contrastStretching (BufferedImage input) {
		BufferedImage dest = null;
		
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		int pix;
		
		
		// find max and min
		for(int i=0;i<input.getWidth();i++)
			for(int j=0;j<input.getHeight();j++){
				pix = input.getRaster().getSample(i, j, 0);
				if(pix > max)
					max = pix;
				if(pix < min)
					min = pix;
			}
		
		System.out.println("max= "+max+" min= "+min );
		
		short LUT_stretch[] = new short[256];
		
		for (int i = min; i < max+1; i++) {
			LUT_stretch[i] = (short)normalize(i, min, max, 0, 255);
			
			//System.out.print(LUT_stretch[i] +" ");
		}
		
		for (int i = 0; i < 256; i++) {
			System.out.print(LUT_stretch[i] +" ");
		}
		
		
		dest = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
		
		ShortLookupTable sLUT = new ShortLookupTable(0, LUT_stretch);
		LookupOp op = new LookupOp(sLUT, null);
		op.filter(input, dest);
		
		return dest;
	}
	public static BufferedImage brightnessV1(BufferedImage input, int offset) {
		BufferedImage dest = null;
		
		short LUT_brightness[] = new short[256];
		for (int i = 0; i < 256; i++) {
			if(i + offset > 255){
				LUT_brightness[i] = 255;
			}
			else{
				if(i + offset < 0)
					LUT_brightness[i] = 0;
				else{
					LUT_brightness[i] = (short) (i + offset);
				}
			}
		}
		
		dest = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
		
		ShortLookupTable sLUT = new ShortLookupTable(0, LUT_brightness);
		LookupOp op = new LookupOp(sLUT, null);
		op.filter(input, dest);
		
		return dest;
	}

	public static BufferedImage brightnessV2(BufferedImage input, int offset) {
		BufferedImage dest = null;
		dest = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
		
		
		RescaleOp op = new RescaleOp(1,offset,null);
		op.filter(input, dest);
		
		return dest;
	}	
	
	public static BufferedImage contrast(BufferedImage input, float scale) {
		BufferedImage dest = null;
		
		short LUT_contrast[] = new short[256];
		for (int i = 0; i < 256; i++) {
			if(scale * i  > 255.0){
				LUT_contrast[i] = 255;
			}
			else{
				LUT_contrast[i] = (short) (scale * i);
			}
		}
		
		dest = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
		
		ShortLookupTable sLUT = new ShortLookupTable(0, LUT_contrast);
		LookupOp op = new LookupOp(sLUT, null);
		op.filter(input, dest);
		
		return dest;
	}
	
	public static BufferedImage RGBbalanceV1(BufferedImage input, int rOffset, int gOffset, int bOffset) {
		BufferedImage dest = null;
		
		// one LUT for each band
		short LUT_R[] = new short[256];
		short LUT_G[] = new short[256];
		short LUT_B[] = new short[256];
		
		short LUT_RGB[][] = {LUT_R ,LUT_G, LUT_B};
		
		for (int i = 0; i < 256; i++){
			LUT_R[i] = (short) i;
			LUT_G[i] = (short) i;
			LUT_B[i] = (short) i;
		}
		
		for (int i = 0; i < 256; i++) {
			if(rOffset + i  > 255){
				LUT_R[i] = 255;
			}
			else if (rOffset + i  < 0)
				LUT_R[i] = 0;
			else{
				LUT_R[i] = (short) (rOffset + i);
			}
		}
		for (int i = 0; i < 256; i++) {
			if(gOffset + i  > 255){
				LUT_G[i] = 255;
			}
			else if (gOffset + i  < 0)
				LUT_G[i] = 0;
			else{
				LUT_G[i] = (short) (gOffset + i);
			}
		}
		for (int i = 0; i < 256; i++) {
			if(bOffset + i  > 255){
				LUT_B[i] = 255;
			}
			else if (bOffset + i  < 0)
				LUT_B[i] = 0;
			else{
				LUT_B[i] = (short) (bOffset + i);
			}
		}
		
		dest = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
		ShortLookupTable sLUT = new ShortLookupTable(0, LUT_RGB);
		LookupOp op = new LookupOp(sLUT, null);
		op.filter(input, dest);
		
		return dest;
	}
	public static BufferedImage RGBbalanceV2(BufferedImage input, int rOffset, int gOffset, int bOffset) {
		BufferedImage dest = null;
		
		dest = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
		
		RescaleOp rop = new RescaleOp(new float[]{1,1,1},new float[]{rOffset,gOffset,bOffset},null);

		rop.filter(input, dest);
		
		return dest;
	}
	
}
