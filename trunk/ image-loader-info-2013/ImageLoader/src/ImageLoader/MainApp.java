package ImageLoader;

import java.awt.image.BufferedImage;

import ImageLoader.util.ImageUtil;
import ImageLoader.util.TimeCounter;


public class MainApp {

	public static void main(String[] args) {
		TimeCounter tc = new TimeCounter();
		
		BufferedImage testIn = ImageUtil.loadImage("./standard_test_images/lena_gray_512.bmp");
		//BufferedImage testIn = ImageUtil.loadImage();
		//BufferedImage testIn2 = ImageUtil.loadImage("./standard_test_images/texture.bmp");
		ImageUtil.displayImage(testIn);
		
		//ImageUtil.displayImage(ImageUtil.negativate(testIn));
		//ImageUtil.displayImage(ImageUtil.colorToGray(testIn));
		//ImageUtil.displayImage(ImageUtil.threshold(testIn,50));
		//ImageUtil.displayImage(ImageUtil.logContrast(testIn));
		tc.start();
		//ImageUtil.displayImage(ImageUtil.brightnessV1(testIn,-20));
		//tc.stopPrMS();
		
		
		//tc.start();
		//ImageUtil.displayImage(ImageUtil.brightnessV2(testIn,-20));
		//tc.stopPrMS();
			
		//ImageUtil.displayImage(ImageUtil.contrast(testIn,0.3f));
		//ImageUtil.displayImage(ImageUtil.contrastStretching(testIn));
		
		//ImageUtil.displayImage(ImageUtil.RGBbalanceV1(testIn, -255, 0, -255));

		//ImageUtil.displayImage(ImageUtil.RGBbalanceV2(testIn, -255, 0, -255));
		
		//ImageUtil.displayImage(ImageUtil.RGB_brightness(testIn, 255, 255, -255));
		//ImageUtil.displayImage(ImageUtil.RGB_brightness(testIn, -255, 0, 0));
		ImageUtil.displayImage(ImageUtil.blur(testIn));
		//ImageUtil.displayImage(ImageUtil.add(testIn,testIn2));
		
		tc.stopPrMS();

	}

}
