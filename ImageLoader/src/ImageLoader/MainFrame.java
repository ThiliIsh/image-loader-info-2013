package ImageLoader;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JCheckBoxMenuItem;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 3543083080286749217L;
	
    // Possible Look & Feels
    private static final String mac      =
            "com.apple.laf.AquaLookAndFeel";
    private static final String nimbus   =
            "javax.swing.plaf.nimbus.NimbusLookAndFeel";
    private static final String metal    =
            "javax.swing.plaf.metal.MetalLookAndFeel";
    private static final String motif    =
            "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    private static final String windows  =
            "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    private static final String gtk  =
            "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
    private static final String web  =
    	"com.alee.laf.WebLookAndFeel";
    
    // The current Look & Feel
    private static String currentLookAndFeel = metal;
	
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
	private final JRGBBalanceDlg rgbBalanceDlg;
	private JMenuItem mntmThreshold;
	private JMenuItem mntmBrightness;
	private JMenuItem mntmContrast;
	private JMenuItem mntmLogcontrast;
	private JMenuItem mntmContraststretching;
	private JMenu mnZoom;
	private JSlider zoomSlider;
	private JMenuItem mntmOriginalSize;
	private JCheckBoxMenuItem chckbxmntmFitToScreen;
	private JButton btnLenacolor;
	private JButton btnLenagray;
	private JButton btnPaste;
	private JButton btnCopy;
	private JMenuItem mntmCopy;
	private JMenuItem mntmPaste;
	private JSeparator separator_1;
	private JMenuItem mntmRgbBalance;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(metal);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		new MainFrame().setVisible(true);
	}
	
	public MainFrame(){
		this(null);
	}

	public MainFrame(Point topLeft) {
		
		
		
		setTitle("Image Loader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 600, 400);
		
		if(topLeft== null)
			setLocationRelativeTo(null);
		else
			setLocation(topLeft);  
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		imagePanel = new ImagePanel();
		JScrollPane scrollPane = new JScrollPane(imagePanel);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		thresholdDlg = new JThresholdDlg(this);
		brightnessDlg = new JBrightnessDlg(this);
		contrastDlg = new JContrastDlg(this);
		rgbBalanceDlg = new JRGBBalanceDlg(this);
		
		fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg","jpeg","png","bmp","gif"));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
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
		mntmSavaAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSaveAs(e);
			}
		});
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
		
		mnZoom = new JMenu("Zoom");
		mnView.add(mnZoom);
		
		zoomSlider = new JSlider();
		
		zoomSlider.setMaximum(200);
		zoomSlider.setMinimum(1);
		zoomSlider.setValue(100);
		zoomSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				imagePanel.setZoomValue(zoomSlider.getValue());
			}
		});

		mnZoom.add(zoomSlider);
		
		mntmOriginalSize = new JMenuItem("Original Size");
		mntmOriginalSize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_MASK));
		mntmOriginalSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setOriginalSize();
			}
		});		
		mnView.add(mntmOriginalSize);
		
		chckbxmntmFitToScreen = new JCheckBoxMenuItem("Fit to Screen");
		chckbxmntmFitToScreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.ALT_MASK));
		chckbxmntmFitToScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxmntmFitToScreen.isSelected())
					imagePanel.setFitToScreen(true);
				else
					imagePanel.setFitToScreen(false);
			}
		});		
		
		mnView.add(chckbxmntmFitToScreen);
		
		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setMnemonic('E');
		menuBar.add(mnEdit);
		
		mntmCopy = new JMenuItem("Copy");
		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCopy(e);
			}
		});
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mntmCopy.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons16x16/copy_clipboard_16.png")));
		mnEdit.add(mntmCopy);
		
		mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onPaste(e);
			}
		});
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mntmPaste.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons16x16/paste_clipboard_16.png")));
		mnEdit.add(mntmPaste);
		
		separator_1 = new JSeparator();
		mnEdit.add(separator_1);
		
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
		
		mntmRgbBalance = new JMenuItem("RGB Balance...");
		mntmRgbBalance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rgbBalanceDlg.setVisible(true);
			}
		});
		mnEdit.add(mntmRgbBalance);
		
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
		btnOpen.setOpaque(false);
		btnOpen.setBorderPainted(false);
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOpen(e);
			}
		});
		btnOpen.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons24x24/folder_open_24.png")));
		toolBar.add(btnOpen);
		
		btnSaveas = new JButton("");
		btnSaveas.setOpaque(false);
		btnSaveas.setBorderPainted(false);
		btnSaveas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSaveAs(e);
			}
		});
		btnSaveas.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons24x24/save_24.png")));
		toolBar.add(btnSaveas);
		
		btnLenagray = new JButton("LenaGray");
		btnLenagray.setOpaque(false);
		btnLenagray.setBorderPainted(false);
		btnLenagray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.loadImage("./standard_test_images/lena_gray_512.bmp"));
			}
		});
		
		btnPaste = new JButton("");
		btnPaste.setBorderPainted(false);
		btnPaste.setOpaque(false);
		btnPaste.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons24x24/paste_clipboard_24.png")));
		btnPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onPaste(e);
			}
		});
		
		btnCopy = new JButton("");
		btnCopy.setBorderPainted(false);
		btnCopy.setOpaque(false);
		btnCopy.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons24x24/copy_clipboard_24.png")));
		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCopy(e);
			}
		});
		toolBar.add(btnCopy);
		toolBar.add(btnPaste);
		toolBar.add(btnLenagray);
		
		btnLenacolor = new JButton("LenaColor");
		btnLenacolor.setOpaque(false);
		btnLenacolor.setBorderPainted(false);
		btnLenacolor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.loadImage("./standard_test_images/lena_color_512.bmp"));
			}
		});
		toolBar.add(btnLenacolor);
		
	}
	
	

	protected void onCopy(ActionEvent e) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

		ImageTransferable it = new ImageTransferable(imagePanel.getImage());
			
		clip.setContents(it, null);
	
	}

	protected void onPaste(ActionEvent e) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

		try {
			//ImageIcon IMG = new ImageIcon((BufferedImage)clip.getData(DataFlavor.imageFlavor));
			BufferedImage bImage = (BufferedImage)clip.getData(DataFlavor.imageFlavor);
			//imagePanel.setImage((BufferedImage) IMG.getImage());
			imagePanel.setImage(bImage);
		} catch (UnsupportedFlavorException e1) {
			JOptionPane.showMessageDialog(this, "No Image on Clipboard!");
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
	
	protected void onSaveAs(ActionEvent ev) {
		int returnVal = fileChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();
			String filename = file.getName();

			String suffix = filename.substring(filename.lastIndexOf('.') + 1);
			suffix = suffix.toLowerCase();
			if (suffix.equals("jpg") || suffix.equals("jpeg")
					|| suffix.equals("gif") || suffix.equals("bmp")
					|| suffix.equals("png")) {
				BufferedImage image = imagePanel.getImage();
				try {
					ImageIO.write(image, suffix, file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(this,
						"Filename must end in .jpg .gif .png or .bmp", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}	
}
