package ImageLoader.gui;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.SliderUI;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import ImageLoader.MainFrame;
import ImageLoader.util.ImageUtil;

public class JLogicalDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private ImagePanel parentImagePanel = null;
	private BufferedImage originalImage = null;
	private BufferedImage image1 = null;
	private JButton btnAply;
	private JButton btnOpenBinaryImage;
	private ImagePanel imagePanel1;
	private JComboBox comboBox;

	private final JFileChooser fileChooser = new JFileChooser(".");
	/**
	 * Create the dialog.
	 */
	public JLogicalDlg(MainFrame frame) {
		super(frame, true);
		parentImagePanel = frame.getImagePanel();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				if (originalImage==null){
					originalImage = parentImagePanel.getImage();

				}

			}
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
		setModal(true);
		setTitle("Logical Operations Dialog");
		

		setBounds(100, 100, 237, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		btnOpenBinaryImage = new JButton("Open binary image...");
		btnOpenBinaryImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOpen(e);
			}
		});
		btnOpenBinaryImage.setBounds(10, 11, 200, 23);
		contentPanel.add(btnOpenBinaryImage);
		
		imagePanel1 = new ImagePanel();
		imagePanel1.setFitToScreen(true);
		imagePanel1.setBounds(10, 40, 200, 200);
		contentPanel.add(imagePanel1);
		
		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onLogicalOperation(e);
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"AND", "OR", "XOR"}));
		comboBox.setBounds(10, 251, 200, 20);
		contentPanel.add(comboBox);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onOK(e);
					}
				});

				btnAply = new JButton("Apply");
				btnAply.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onLogicalOperation(e);
					}
				});
				buttonPane.add(btnAply);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onCancel();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	protected void onOpen(ActionEvent e) {
		
		int op = fileChooser.showOpenDialog(null);
		if (op == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedImage bi = ImageIO.read(fileChooser.getSelectedFile());
				if(bi.getType()==BufferedImage.TYPE_BYTE_GRAY){
					image1=bi;
					imagePanel1.setImage(bi);
				}
				else{
					JOptionPane.showMessageDialog(null, "No binary image!",null,JOptionPane.ERROR_MESSAGE);
					return;
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	protected void onLogicalOperation(ActionEvent e) {
		parentImagePanel.setImage(ImageUtil.logical(originalImage, image1, comboBox.getSelectedIndex() ));
	}
	
	private void onOK(ActionEvent e){
		originalImage = null;
		dispose();
	}
	private void onCancel(){
		parentImagePanel.setImage(originalImage);
		originalImage = null;
		dispose();
	}
}
