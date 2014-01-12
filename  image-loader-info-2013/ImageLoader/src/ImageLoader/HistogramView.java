package ImageLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import javax.swing.*;

public class HistogramView extends JLabel {

	private static final int HIST_WIDTH = 300;
	private static final int HIST_HEIGHT = 140;
	private static final int TICK_INTERVAL = 51;
	private static final int TICK_SIZE = 2;

	private Histogram histogram;
	private int band;
	private int xOrigin = (HIST_WIDTH - 256) / 2;
	private int ySize = HIST_HEIGHT - 40;
	private int yOrigin = ySize + 10;
	private Color histColor;

	public HistogramView(Histogram newHistogram, int newBand) {
		histogram = newHistogram;
		band = newBand;
	}

	public HistogramView() {
		histogram = null;
		band = 0;
	}

	public void setHistogram(Histogram newHistogram, int newBand) {
		histogram = newHistogram;
		band = newBand;
		repaint();
	}

	public void setHistogram(Histogram newHistogram) {
		setHistogram(newHistogram, 0);
	}

	public void setColor(Color theColor) {
		histColor = theColor;
	}

	public int getValue(Point point) {
		return Math.min(Math.max(point.x - xOrigin, 0), 255);
	}

	public void paintComponent(Graphics graphics) {
		if (histogram == null)
			return;

		double scale = ((double) ySize) / histogram.getMaxFrequency(band);
		if (histColor != null)
			graphics.setColor(histColor);
		for (int x = 0; x < 256; ++x) {
			int y = (int) Math.round(scale * histogram.getFrequency(band, x));
			if (y > 0)
				graphics.drawLine(x + xOrigin, yOrigin, x + xOrigin, yOrigin
						- y);
		}
		drawAxis(graphics, xOrigin, yOrigin + 1);
	}

	public void drawAxis(Graphics graphics, int x, int y) {
		Color oldColor = graphics.getColor();
		graphics.setColor(Color.black);
		graphics.drawLine(x, y, x + 255, y);
		for (int t = 0; t < 256; t += TICK_INTERVAL) {
			graphics.drawLine(x + t, y, x + t, y + TICK_SIZE);
			drawTickLabel(graphics, String.valueOf(t), x + t, y + TICK_SIZE + 2);
		}
		graphics.setColor(oldColor);
	}

	public void drawTickLabel(Graphics graphics, String label, int x, int y) {
		FontMetrics metrics = graphics.getFontMetrics();
		Rectangle2D box = (Rectangle2D) metrics
				.getStringBounds(label, graphics);
		graphics.drawString(label, x - (int) box.getWidth() / 2,
				y + (int) box.getHeight());
	}

}
