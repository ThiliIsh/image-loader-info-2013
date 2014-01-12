package ImageLoader;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class Histogram {
	private int channels;
	private int pixels;

	private int[][] freq;
	private int[][] cumFreq;

	private int[] minValue;

	private int[] maxValue;

	private int[] minFreq;
	private int[] maxFreq;

	private double[] meanValue;

	public Histogram() {
		freq = new int[3][256];
		cumFreq = new int[3][256];
		minValue = new int[3];
		maxValue = new int[3];
		minFreq = new int[3];
		maxFreq = new int[3];
		meanValue = new double[3];
		initialize();
	}

	private void initialize() {
		channels = pixels = 0;
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 256; ++j)
				freq[i][j] = cumFreq[i][j] = 0;
		for (int i = 0; i < 3; ++i) {
			minValue[i] = maxValue[i] = 0;
			minFreq[i] = maxFreq[i] = 0;
			meanValue[i] = 0.0;
		}
	}

	public String toString() {
		String res = new String(getClass().getName() + ": " + channels
				+ " channels, " + pixels + " pixels \r\n");

		if (channels == 1) {
			for (int i = 0; i < 256; i++)
				res += freq[0][i] + "\t";
		} else {
			for (int j = 0; j < 3; j++){
			for (int i = 0; i < 256; i++)
				res += freq[j][i] + "\t";
			res+="\r\n";
			}
		}

		return res;
	}

	public void buildHistogram(BufferedImage image) {

		initialize();

		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY)
			channels = 1;
		else
			channels = 3;

		pixels = image.getWidth() * image.getHeight();
		if (pixels > 0) {
			computeFrequencies(image);
			computeStatistics();
		}

	}

	private void computeFrequencies(BufferedImage image) {

		if (image.getType() == BufferedImage.TYPE_BYTE_BINARY
				|| image.getType() == BufferedImage.TYPE_USHORT_GRAY)
			return;

		Raster raster = image.getRaster();
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY) {
			for (int y = 0; y < image.getHeight(); ++y)
				for (int x = 0; x < image.getWidth(); ++x)
					++freq[0][raster.getSample(x, y, 0)];
		} else {
//			if(image.getType() == BufferedImage.TYPE_INT_ARGB ||
//					image.getType() == BufferedImage.TYPE_INT_RGB){
			int[] value = new int[3];
			for (int y = 0; y < image.getHeight(); y++)
				for (int x = 0; x < image.getWidth(); x++) {
					raster.getPixel(x, y, value);
					freq[0][value[0]]++;
					freq[1][value[1]]++;
					freq[2][value[2]]++;
				}
//			}
		}
	}

	private void computeStatistics() {

		int i, j;

		for (i = 0; i < channels; ++i) {

			// Compute cumulative histogram

			cumFreq[i][0] = freq[i][0];
			for (j = 1; j < 256; ++j)
				cumFreq[i][j] = cumFreq[i][j - 1] + freq[i][j];

			// Find minimum value

			for (j = 0; j < 256; ++j) {
				if (freq[i][j] > 0) {
					minValue[i] = j;
					break;
				}
			}

			// Find maximum value

			for (j = 255; j >= 0; --j) {
				if (freq[i][j] > 0) {
					maxValue[i] = j;
					break;
				}
			}

			// Find lowest and highest frequencies, and determine mean

			minFreq[i] = Integer.MAX_VALUE;
			for (j = 0; j < 256; ++j) {
				if (freq[i][j] < minFreq[i])
					minFreq[i] = freq[i][j];
				else if (freq[i][j] > maxFreq[i])
					maxFreq[i] = freq[i][j];
				meanValue[i] += (double) (j * freq[i][j]);
			}
			meanValue[i] /= (double) pixels;

		}

	}
	  public int getMaxFrequency(int ch) {
		    return maxFreq[ch];
		  }
	  public int getFrequency(int ch, int value) {
		    return freq[ch][value];
		  }
	  public int getCumulativeFrequency(int band, int value) {
		    return cumFreq[band][value];
		  }
	  public int getPixelCount() {
		    return pixels;
		  }
}
