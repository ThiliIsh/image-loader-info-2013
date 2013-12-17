package ImageLoader;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 3543083080286749217L;
	
	private JPanel contentPane;
	private ImagePanel imagePanel;
	private JMenu mnView;
	private JMenu mnHelp;
	private JMenuItem mntmSavaAs;
	private JMenuItem mntmProperties;
	private JMenuItem mntmClose;
	private JSeparator separator;
	private JToolBar toolBar;
	private JButton btnOpen;
	private JButton btnSaveas;
	/**
	 * @wbp.nonvisual location=44,419
	 */
	private final JFileChooser fileChooser = new JFileChooser(".");
	private BufferedImage mainImage = null;
	private JMenuItem mntmNegativate;
	private JMenuItem mntmTogray;
	private JMenuItem mntmAbout;
	/**
	 * @wbp.nonvisual location=129,419
	 */
	private final JAboutDlg aboutDlg = new JAboutDlg();
	/**
	 * @wbp.nonvisual location=189,419
	 */
	private final JThresholdDlg thresholdDlg;
	private final JBrightnessDlg brightnessDlg;
	private final JContrastDlg contrastDlg;
	private JMenuItem mntmThreshold;
	private JMenuItem mntmBrightness;
	private JMenuItem mntmContrast;
	private JMenuItem mntmLogcontrast;
	private JMenuItem mntmContraststretching;

	public static void main(String[] args) {
		new MainFrame().setVisible(true);
	}
	
	public MainFrame(){
		this(new Point(450, 100));
	}

	public MainFrame(Point topLeft) {
		
		
		
		setTitle("Image Loader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocation(topLeft);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		imagePanel = new ImagePanel();
		contentPane.add(imagePanel, BorderLayout.CENTER);
		
		thresholdDlg = new JThresholdDlg(this);
		brightnessDlg = new JBrightnessDlg(this);
		contrastDlg = new JContrastDlg(this);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnOpen = new JMenu("Open...");
		mnOpen.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons16x16/folder_open_16.png")));
		mnOpen.setMnemonic('O');
		mnFile.add(mnOpen);
		
		JMenuItem mntmNewWindow = new JMenuItem("New Window");
		mnOpen.add(mntmNewWindow);
		
		JMenuItem mntmOpen = new JMenuItem("Image Here");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnOpen.add(mntmOpen);
		
		JMenuItem mntmOpenInNew = new JMenuItem("Image In Different Window");
		mnOpen.add(mntmOpenInNew);
		
		mntmSavaAs = new JMenuItem("Sava as...");
		mntmSavaAs.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons16x16/save_16.png")));
		mntmSavaAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSavaAs);
		
		mntmProperties = new JMenuItem("Properties");
		mnFile.add(mntmProperties);
		
		separator = new JSeparator();
		mnFile.add(separator);
		
		mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		mntmOpenInNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ImageUtil.displayImage();
			}
		});
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOpen(e);
				
//				if(imagePanel == null){
//					imagePanel = new ImagePanel();
//					contentPane.add(imagePanel, BorderLayout.CENTER);
//				}
//				imagePanel.setImage(ImageUtil.loadImage());
//				pack();
			}
		});
		mntmNewWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Point p = new Point(getLocation());
				p.x += 50;
				p.y += 50;
				new MainFrame(p).setVisible(true);
			}
		});
		
		mnView = new JMenu("View");
		mnView.setMnemonic('V');
		menuBar.add(mnView);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenu mnFlip = new JMenu("Flip");
		mnEdit.add(mnFlip);
		
		JMenuItem mntmHorizontal = new JMenuItem("Horizontally");
		mntmHorizontal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(imagePanel != null){
					imagePanel.setImage(ImageUtil.flipImageHorizontally(imagePanel.getImage()));	
				}
			}
		});
		mnFlip.add(mntmHorizontal);
		
		JMenuItem mntmVertical = new JMenuItem("Vertically");
		mntmVertical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(imagePanel != null){
					imagePanel.setImage(ImageUtil.flipImageVertically(imagePanel.getImage()));		
				}
			}
		});
		mnFlip.add(mntmVertical);
		
		mntmNegativate = new JMenuItem("Negativate");
		mntmNegativate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.negativate(imagePanel.getImage()));
			}
		});
		mnEdit.add(mntmNegativate);
		
		mntmTogray = new JMenuItem("ToGray");
		mntmTogray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.colorToGray(imagePanel.getImage()));
			}
		});
		mnEdit.add(mntmTogray);
		
		mntmThreshold = new JMenuItem("Threshold...");
		mntmThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thresholdDlg.setVisible(true);
				//new JThresholdDlg(this).setVisible(true);
			}
		});
		mnEdit.add(mntmThreshold);
		
		mntmBrightness = new JMenuItem("Brightness...");
		mntmBrightness.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				brightnessDlg.setVisible(true);
			}
		});
		
		mntmLogcontrast = new JMenuItem("LogContrast");
		mntmLogcontrast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.logContrast(imagePanel.getImage()));
			}
		});
		mnEdit.add(mntmLogcontrast);
		
		mntmContraststretching = new JMenuItem("ContrastStretching");
		mntmContraststretching.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.contrastStretching(imagePanel.getImage()));
			}
		});
		mnEdit.add(mntmContraststretching);
		mnEdit.add(mntmBrightness);
		
		mntmContrast = new JMenuItem("Contrast...");
		mntmContrast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contrastDlg.setVisible(true);
			}
		});
		mnEdit.add(mntmContrast);
		
		mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aboutDlg.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);
		
		setContentPane(contentPane);
		
		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		btnOpen = new JButton("");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOpen(e);
			}
		});
		btnOpen.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons24x24/folder_open_24.png")));
		toolBar.add(btnOpen);
		
		btnSaveas = new JButton("");
		btnSaveas.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons24x24/save_24.png")));
		toolBar.add(btnSaveas);
		
		
	}

	protected void onOpen(ActionEvent e) {
		
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			try {
				mainImage = ImageIO.read(fileChooser.getSelectedFile());
				imagePanel.setImage(mainImage);
				
			} catch (IOException ex) {
				System.out.println("Freaky error, very freaky error! (Might not be an image)");
				ex.printStackTrace();
			}
		}
		
	}

	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public void setImagePanel(ImagePanel imagePanel) {
		this.imagePanel = imagePanel;
	}
}
