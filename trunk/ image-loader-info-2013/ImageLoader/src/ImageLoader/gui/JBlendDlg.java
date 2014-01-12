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
import javax.swing.JFrame;
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
import javax.swing.JFileChooser;
import javax.swing.border.EtchedBorder;

import ImageLoader.MainFrame;
import ImageLoader.util.ImageUtil;

public class JBlendDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSlider blendSlider;
	private ImagePanel parentImagePanel = null;
	private BufferedImage originalImage = null;
	private BufferedImage image1 = null;
	private BufferedImage image2 = null;
	private JTextField textField;
	private JButton btnAply;
	private JButton btnOpenImage_1;
	private JButton btnOpenImage_2;
	private ImagePanel imagePanel1;
	private ImagePanel imagePanel2;
	/**
	 * @wbp.nonvisual location=64,489
	 */
	private final JFileChooser fileChooser = new JFileChooser(".");

	/**
	 * Create the dialog.
	 */
	public JBlendDlg(MainFrame frame) {
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
		
		
		setTitle("Blend Images");

		setBounds(100, 100, 448, 440);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		blendSlider = new JSlider();
		blendSlider.setPaintTicks(true);
		blendSlider.setPaintLabels(true);
		blendSlider.setMajorTickSpacing(10);
		blendSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textField.setText(String.valueOf(blendSlider.getValue()));
				onBlend();
			}

		});
		blendSlider.setBounds(10, 279, 421, 45);
		contentPanel.add(blendSlider);

		textField = new JTextField();
		textField.setText("0.0");
		textField.setBounds(190, 335, 71, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		btnOpenImage_1 = new JButton("Open image 1...");
		btnOpenImage_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOpen(e);
			}
		});
		btnOpenImage_1.setBounds(10, 35, 117, 23);
		contentPanel.add(btnOpenImage_1);
		
		btnOpenImage_2 = new JButton("Open image 2...");
		btnOpenImage_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOpen(e);
			}
		});
		btnOpenImage_2.setBounds(231, 35, 111, 23);
		contentPanel.add(btnOpenImage_2);
		
		imagePanel1 = new ImagePanel();
		imagePanel1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		imagePanel1.setFitToScreen(true);
		imagePanel1.setBounds(10, 64, 200, 200);
		contentPanel.add(imagePanel1);
		
		imagePanel2 = new ImagePanel();
		imagePanel2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		imagePanel2.setFitToScreen(true);
		imagePanel2.setBounds(231, 64, 200, 200);
		contentPanel.add(imagePanel2);
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
						onBlend();
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
				if(e.getSource()==btnOpenImage_1){
					image1=bi;
					imagePanel1.setImage(bi);
				}
				if(e.getSource()==btnOpenImage_2){
					image2=bi;
					imagePanel2.setImage(bi);
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	protected void onBlend() {
		parentImagePanel.setImage(ImageUtil.blend(image1, image2, blendSlider.getValue()/100.0f ));
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
