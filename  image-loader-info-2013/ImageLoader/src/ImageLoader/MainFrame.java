package ImageLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.PrintJob;
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
	private static final String mac = "com.apple.laf.AquaLookAndFeel";
	private static final String nimbus = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
	private static final String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
	private static final String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	private static final String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	private static final String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	private static final String web = "com.alee.laf.WebLookAndFeel";

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
	private final JPixelateDlg pixelateDlg;
	private final JHistogramThresholdDlg histogramThresholdDlg;
	private final JHistogramDlg histogramDlg;
	private final JGammaContrastDlg gammaContrastDlg;
	private final JBitPlaneSlicingDlg bitPlaneSlicingDlg;
	private final JResizeDlg resizeDlg;
	private final JRotateDlg rotateDlg;
	private final JBlendDlg blendDlg;
	private final JLogicalDlg logicalDlg;
	
	
	
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
	private JMenuItem mntmPrint;
	private JMenu mnNoise;
	private JMenuItem mntmSimpleNoise;
	private JMenuItem mntmPixelate;
	private JMenu mnToGrayscale;
	private JMenuItem mntmAveraged;
	private JMenuItem mntmWeighted;
	private JMenuItem mntmHistogramThreshold;
	private JMenuItem mntmPosterize;
	private JMenuItem mntmExponentialContrast;
	private JMenuItem mntmGammaContrast;
	private JMenu mnConvolutionOperations;
	private JMenuItem mntmBlur;
	private JMenuItem mntmSharpen;
	private JMenuItem mntmEdge;
	private JMenuItem mntmScreenShot;
	private JMenuItem mntmBitPlaneSlicing;
	private JMenuItem mntmResize;
	private JMenuItem mntmRotate;
	private JMenuItem mntmHistogram;
	private JMenuItem mntmBlend;
	private JMenuItem mntmLogicalOperations;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(metal);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		new MainFrame().setVisible(true);
	}

	public MainFrame() {
		this(new Point(100,10));
	}

	public MainFrame(Point topLeft) {

		setTitle("Image Loader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 700, 500);

		if (topLeft == null)
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
		pixelateDlg = new JPixelateDlg(this);
		histogramThresholdDlg = new JHistogramThresholdDlg(this);
		gammaContrastDlg = new JGammaContrastDlg(this);
		bitPlaneSlicingDlg = new JBitPlaneSlicingDlg(this);
		resizeDlg = new JResizeDlg(this);
		rotateDlg = new JRotateDlg(this);
		histogramDlg = new JHistogramDlg(this);
		blendDlg = new JBlendDlg(this);
		logicalDlg = new JLogicalDlg(this);

		
		
		fileChooser.setFileFilter(new FileNameExtensionFilter("Image files",
				"jpg", "jpeg", "png", "bmp", "gif"));

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

		JMenu mnOpen = new JMenu("Open...");
		mnOpen.setIcon(new ImageIcon(MainFrame.class
				.getResource("/ImageLoader/icons16x16/folder_open_16.png")));
		mnOpen.setMnemonic('O');
		mnFile.add(mnOpen);

		JMenuItem mntmNewWindow = new JMenuItem("New Window");
		mnOpen.add(mntmNewWindow);

		JMenuItem mntmOpen = new JMenuItem("Image Here");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		mnOpen.add(mntmOpen);

		JMenuItem mntmOpenInNew = new JMenuItem("Image In Different Window");
		mnOpen.add(mntmOpenInNew);

		mntmSavaAs = new JMenuItem("Sava as...");
		mntmSavaAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSaveAs(e);
			}
		});
		mntmSavaAs.setIcon(new ImageIcon(MainFrame.class
				.getResource("/ImageLoader/icons16x16/save_16.png")));
		mntmSavaAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmSavaAs);
		
		mntmPrint = new JMenuItem("Print...");
		mntmPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printSelectedImage();
			}
		});
		mntmPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mnFile.add(mntmPrint);

		mntmProperties = new JMenuItem("Properties");
		mntmProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageUtil.showImageProperties(null, imagePanel.getImage());
			}
		});
		mntmProperties.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_MASK));
		mnFile.add(mntmProperties);

		separator = new JSeparator();
		mnFile.add(separator);

		mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onClose(e);
			}
		});
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmClose);
		mntmOpenInNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ImageUtil.displayImage();
			}
		});
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOpen(e);

				// if(imagePanel == null){
				// imagePanel = new ImagePanel();
				// contentPane.add(imagePanel, BorderLayout.CENTER);
				// }
				// imagePanel.setImage(ImageUtil.loadImage());
				// pack();
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
		mntmOriginalSize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				InputEvent.ALT_MASK));
		mntmOriginalSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setOriginalSize();
			}
		});
		mnView.add(mntmOriginalSize);

		chckbxmntmFitToScreen = new JCheckBoxMenuItem("Fit to Screen");
		chckbxmntmFitToScreen.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F, InputEvent.ALT_MASK));
		chckbxmntmFitToScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxmntmFitToScreen.isSelected())
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
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				InputEvent.CTRL_MASK));
		mntmCopy.setIcon(new ImageIcon(MainFrame.class
				.getResource("/ImageLoader/icons16x16/copy_clipboard_16.png")));
		mnEdit.add(mntmCopy);

		mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onPaste(e);
			}
		});
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				InputEvent.CTRL_MASK));
		mntmPaste
				.setIcon(new ImageIcon(
						MainFrame.class
								.getResource("/ImageLoader/icons16x16/paste_clipboard_16.png")));
		mnEdit.add(mntmPaste);
		
		mntmScreenShot = new JMenuItem("Screen Shot");
		mntmScreenShot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.getScreenShot());
			}
		});
		mnEdit.add(mntmScreenShot);

		separator_1 = new JSeparator();
		mnEdit.add(separator_1);

		JMenu mnFlip = new JMenu("Flip");
		mnEdit.add(mnFlip);

		JMenuItem mntmHorizontal = new JMenuItem("Horizontally");
		mntmHorizontal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (imagePanel != null) {
					imagePanel.setImage(ImageUtil
							.flipImageHorizontally(imagePanel.getImage()));
				}
			}
		});
		mnFlip.add(mntmHorizontal);

		JMenuItem mntmVertical = new JMenuItem("Vertically");
		mntmVertical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (imagePanel != null) {
					imagePanel.setImage(ImageUtil
							.flipImageVertically(imagePanel.getImage()));
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
		
		mnNoise = new JMenu("Noise");
		mnEdit.add(mnNoise);
		
		mntmSimpleNoise = new JMenuItem("Simple Noise");
		mntmSimpleNoise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.simpleNoise(imagePanel.getImage()));
			}
		});
		mnNoise.add(mntmSimpleNoise);
		
		mntmPixelate = new JMenuItem("Pixelate...");
		mntmPixelate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pixelateDlg.setVisible(true);
			}
		});
		mnEdit.add(mntmPixelate);
		mnEdit.add(mntmNegativate);
		
		mnToGrayscale = new JMenu("To Grayscale");
		mnEdit.add(mnToGrayscale);
				
				mntmAveraged = new JMenuItem("Averaged");
				mntmAveraged.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						imagePanel.setImage(ImageUtil.toGrayscale(imagePanel.getImage(),0));
					}
				});
				mnToGrayscale.add(mntmAveraged);
				
				mntmWeighted = new JMenuItem("Weighted");
				mntmWeighted.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						imagePanel.setImage(ImageUtil.toGrayscale(imagePanel.getImage(),1));
					}
				});
				mnToGrayscale.add(mntmWeighted);
		
				mntmTogray = new JMenuItem("Grayscale (average all bands)");
				mnToGrayscale.add(mntmTogray);
				mntmTogray.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						imagePanel.setImage(ImageUtil.colorToGray(imagePanel.getImage()));
					}
				});

		mntmThreshold = new JMenuItem("Threshold...");
		mntmThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				thresholdDlg.setVisible(true);
				// new JThresholdDlg(this).setVisible(true);
			}
		});
		mnEdit.add(mntmThreshold);

		mntmBrightness = new JMenuItem("Brightness...");
		mntmBrightness.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				brightnessDlg.setVisible(true);
			}
		});

		mntmLogcontrast = new JMenuItem("Logarithmic Contrast");
		mntmLogcontrast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.logContrast(imagePanel.getImage()));
			}
		});
		
		mntmBitPlaneSlicing = new JMenuItem("Bit Plane Slicing...");
		mntmBitPlaneSlicing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bitPlaneSlicingDlg.setVisible(true);
			}
		});
		mnEdit.add(mntmBitPlaneSlicing);
		mnEdit.add(mntmLogcontrast);
		
		mntmExponentialContrast = new JMenuItem("Exponential Contrast");
		mntmExponentialContrast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.expContrast(imagePanel.getImage()));
			}
		});
		mnEdit.add(mntmExponentialContrast);
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
		
				mntmContraststretching = new JMenuItem("ContrastStretching");
				mntmContraststretching.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						imagePanel.setImage(ImageUtil.contrastStretching(imagePanel
								.getImage()));
					}
				});
				
				mntmGammaContrast = new JMenuItem("Gamma Contrast...");
				mntmGammaContrast.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						gammaContrastDlg.setVisible(true);
					}
				});
				mnEdit.add(mntmGammaContrast);
				mnEdit.add(mntmContraststretching);
		mnEdit.add(mntmRgbBalance);
		
		mntmPosterize = new JMenuItem("Posterize");
		mntmPosterize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.posterize(imagePanel.getImage()));				
			}
		});
		mnEdit.add(mntmPosterize);
		
		mnConvolutionOperations = new JMenu("Convolution Operations");
		mnEdit.add(mnConvolutionOperations);
		
		mntmBlur = new JMenuItem("Blur");
		mntmBlur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.blur(imagePanel.getImage()));	
			}
		});
		mnConvolutionOperations.add(mntmBlur);
		
		mntmSharpen = new JMenuItem("Sharpen");
		mntmSharpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.sharpen(imagePanel.getImage()));	
			}
		});
		mnConvolutionOperations.add(mntmSharpen);
		
		mntmEdge = new JMenuItem("Edge");
		mntmEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil.edge(imagePanel.getImage()));	
			}
		});
		mnConvolutionOperations.add(mntmEdge);
		
		mntmResize = new JMenuItem("Resize...");
		mntmResize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resizeDlg.setVisible(true);
			}
		});
		
		mntmHistogram = new JMenuItem("Histogram...");
		mntmHistogram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				histogramDlg.setVisible(true);
			}
		});
		mnEdit.add(mntmHistogram);
		
		mntmHistogramThreshold = new JMenuItem("Histogram Threshold...");
		mntmHistogramThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				histogramThresholdDlg.setVisible(true);
			}
		});
		mnEdit.add(mntmHistogramThreshold);
		mnEdit.add(mntmResize);
		
		mntmRotate = new JMenuItem("Rotate...");
		mntmRotate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rotateDlg.setVisible(true);
			}
		});
		mnEdit.add(mntmRotate);
		
		mntmLogicalOperations = new JMenuItem("Logical Operations...");
		mntmLogicalOperations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logicalDlg.setVisible(true);
			}
		});
		mnEdit.add(mntmLogicalOperations);
		
		mntmBlend = new JMenuItem("Blend...");
		mntmBlend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				blendDlg.setVisible(true);
			}
		});
		mnEdit.add(mntmBlend);

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
		btnOpen.setIcon(new ImageIcon(MainFrame.class
				.getResource("/ImageLoader/icons24x24/folder_open_24.png")));
		toolBar.add(btnOpen);

		btnSaveas = new JButton("");
		btnSaveas.setOpaque(false);
		btnSaveas.setBorderPainted(false);
		btnSaveas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSaveAs(e);
			}
		});
		btnSaveas.setIcon(new ImageIcon(MainFrame.class
				.getResource("/ImageLoader/icons24x24/save_24.png")));
		toolBar.add(btnSaveas);

		btnLenagray = new JButton("");
		btnLenagray.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons24x24/lena_gray_24.png")));
		btnLenagray.setOpaque(false);
		btnLenagray.setBorderPainted(false);
		btnLenagray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil
						.loadImage("./standard_test_images/lena_gray_512.bmp"));
			}
		});

		btnPaste = new JButton("");
		btnPaste.setBorderPainted(false);
		btnPaste.setOpaque(false);
		btnPaste.setIcon(new ImageIcon(MainFrame.class
				.getResource("/ImageLoader/icons24x24/paste_clipboard_24.png")));
		btnPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onPaste(e);
			}
		});

		btnCopy = new JButton("");
		btnCopy.setBorderPainted(false);
		btnCopy.setOpaque(false);
		btnCopy.setIcon(new ImageIcon(MainFrame.class
				.getResource("/ImageLoader/icons24x24/copy_clipboard_24.png")));
		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCopy(e);
			}
		});
		toolBar.add(btnCopy);
		toolBar.add(btnPaste);
		toolBar.add(btnLenagray);

		btnLenacolor = new JButton("");
		btnLenacolor.setIcon(new ImageIcon(MainFrame.class.getResource("/ImageLoader/icons24x24/lena_color_24.png")));
		btnLenacolor.setOpaque(false);
		btnLenacolor.setBorderPainted(false);
		btnLenacolor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagePanel.setImage(ImageUtil
						.loadImage("./standard_test_images/lena_color_512.bmp"));
			}
		});
		toolBar.add(btnLenacolor);

	}

	protected void onClose(ActionEvent e) {
		int option;
		option = JOptionPane.showConfirmDialog(this, "Are you sure you wish to exit?","Close",JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.YES_OPTION)
			System.exit(0);
		else
			; // do nothing
	}

	protected void onCopy(ActionEvent e) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

		ImageTransferable it = new ImageTransferable(imagePanel.getImage());

		clip.setContents(it, null);

	}

	protected void onPaste(ActionEvent e) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

		try {
			// ImageIcon IMG = new
			// ImageIcon((BufferedImage)clip.getData(DataFlavor.imageFlavor));
			BufferedImage bImage = (BufferedImage) clip.getData(DataFlavor.imageFlavor);
			
			// imagePanel.setImage((BufferedImage) IMG.getImage());
			
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

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				mainImage = ImageIO.read(fileChooser.getSelectedFile());
				imagePanel.setImage(mainImage);

			} catch (IOException ex) {
				System.out
						.println("Freaky error, very freaky error! (Might not be an image)");
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

	/**
	 * Prints the currently selected image.
	 */
	private void printSelectedImage() {
		if (imagePanel != null && imagePanel.getImage() !=null) {
			PrintJob job = getToolkit().getPrintJob(this, getTitle(), null);
			if (job != null) {
				Graphics pg = (Graphics) job.getGraphics();
				if (pg != null) {
					Image awtImage = (BufferedImage) imagePanel.getImage();
					pg.drawImage(awtImage, 0, 0, Color.white, this);
					pg.dispose();
					job.end();
				}
			}
		}
		else
			JOptionPane.showMessageDialog(this,"Nothing to print!","Error",JOptionPane.ERROR_MESSAGE);
	}
}
