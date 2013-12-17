package ImageLoader;

import java.awt.image.BufferedImage;

public class MainApp {

	public static void main(String[] args) {
		
//		BufferedImage testIn = ImageUtil.loadImage("./standard_test_images/lena_gray_512.bmp");
		BufferedImage testIn = ImageUtil.loadImage();
		ImageUtil.displayImage(testIn);
		
		//ImageUtil.displayImage(ImageUtil.negativate(testIn));
		//ImageUtil.displayImage(ImageUtil.colorToGray(testIn));
		//ImageUtil.displayImage(ImageUtil.threshold(testIn,50));
		//ImageUtil.displayImage(ImageUtil.logContrast(testIn));
		// ImageUtil.displayImage(ImageUtil.brightness(testIn,50));
		//ImageUtil.displayImage(ImageUtil.contrast(testIn,0.3f));
		ImageUtil.displayImage(ImageUtil.contrastStretching(testIn));
		
		

	}

}
