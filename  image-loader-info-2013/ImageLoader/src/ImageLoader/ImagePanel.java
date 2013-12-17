package ImageLoader;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 758263471030047003L;
	private BufferedImage img = null;

	public ImagePanel() {
	}

	public BufferedImage getImage() {
		return img;
	}

	public void setImage(BufferedImage img) {
		if(img != null){
			this.img = img;
			setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
			repaint();
		}
	}

	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		if (img != null) {
			g2D.drawImage(img, 0, 0, null);
		}
	}
}
