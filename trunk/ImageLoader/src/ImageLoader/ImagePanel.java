package ImageLoader;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class ImagePanel extends JPanel {
	private BufferedImage img=null;
	private boolean fitToScreen=false;
	private double zoomFactor=1.0;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2= (Graphics2D)g;
		
		if(img!=null){
			if(fitToScreen){
				double ws = 1.0 * getWidth() / img.getWidth();
				double hs = 1.0 * getHeight() / img.getHeight();
				double scale = Math.min(ws, hs);
				g2.drawImage(img, 0, 0, (int) (img.getWidth() * scale)+1, (int) (img.getHeight() * scale)+1, null);
			}
			else{ //draw original size
				if (zoomFactor != 1.0) {
					//scale(g2);//scale
					g2.drawImage(img, 0, 0, (int) (img.getWidth() * zoomFactor)+1, (int) (img.getHeight() * zoomFactor)+1, null);
					
				} else {//draw original size
					g2.drawImage(img, 0, 0, this);
					
				}
			}
		}
	}
	
	public void setZoomValue(int value) {
		zoomFactor = (double) value / 100.0;
		setPreferredSize(new Dimension(
				(int) (img.getWidth() * zoomFactor) + 1,
				(int) (img.getHeight() * zoomFactor) + 1));
		revalidate();
		repaint();
	}
	
	public void setOriginalSize() {
		fitToScreen = false;
		zoomFactor = 1.0;
		if (img != null)
			setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		revalidate();
		repaint();
	}
	
	public BufferedImage getImage() {
		return img;
	}

	public void setImage(BufferedImage img) {
		this.img = img;
		zoomFactor = 1.0;
		if (fitToScreen)
			setPreferredSize(null);
		else
			setPreferredSize((img==null) ? null : new Dimension(img.getWidth(),img.getHeight()));
		revalidate();
		repaint();
	}

	public boolean isFitToScreen() {
		return fitToScreen;
	}

	public void setFitToScreen(boolean fitToScreen) {
		this.fitToScreen = fitToScreen;
		setPreferredSize(fitToScreen?null:new Dimension(img.getWidth(),img.getHeight()));
		revalidate();
		repaint();
	}
	
	
}
