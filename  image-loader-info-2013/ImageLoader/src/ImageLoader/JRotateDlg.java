package ImageLoader;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class JRotateDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSlider rotationSlider;
	private ImagePanel parentImagePanel = null;
	private BufferedImage originalImage = null;
	private JTextField textField;
	private JLabel lblGammaValue;
	private JButton btnAply;
	private JComboBox comboBox;
	private JLabel label;

	/**
	 * Create the dialog.
	 */
	public JRotateDlg(MainFrame frame) {
		super(frame,true);
		parentImagePanel = frame.getImagePanel();
		//originalImage = parentImagePanel.getImage();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				if (originalImage==null){
					originalImage = parentImagePanel.getImage();
					rotationSlider.setValue(0);
					textField.setText(""+0);
				}

			}
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
		
		setTitle("Rotation Dialog");

		setBounds(100, 100, 397, 205);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		rotationSlider = new JSlider();
		rotationSlider.setMinimum(-180);
		rotationSlider.setPaintTicks(true);
		rotationSlider.setPaintLabels(true);
		rotationSlider.setMajorTickSpacing(40);
		rotationSlider.setValue(0);
		rotationSlider.setMaximum(180);
		rotationSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textField.setText(String.valueOf(rotationSlider.getValue()));
				onRotate();
			}

		});
		rotationSlider.setBounds(20, 11, 339, 45);
		contentPanel.add(rotationSlider);

		textField = new JTextField();
		textField.setText("0.0");
		textField.setBounds(121, 64, 71, 20);
		contentPanel.add(textField);
		textField.setColumns(10);

		lblGammaValue = new JLabel("Rotation value:");
		lblGammaValue.setBounds(20, 67, 112, 14);
		contentPanel.add(lblGammaValue);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"TYPE_NEAREST_NEIGHBOR", "TYPE_BILINEAR", "TYPE_BICUBIC"}));
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(121, 89, 238, 20);
		contentPanel.add(comboBox);
		
		label = new JLabel("Interpolation:");
		label.setBounds(30, 92, 102, 14);
		contentPanel.add(label);
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
						onRotate();
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

	protected void onRotate() {
		BufferedImage dest = null;
		dest = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getTransparency());

		double radian = Double.parseDouble(textField.getText()) * Math.PI / 180;

		AffineTransform rotateTf = new AffineTransform();

		rotateTf.translate(originalImage.getWidth() / 2,
				originalImage.getHeight() / 2);
		rotateTf.rotate(radian);
		rotateTf.translate(-originalImage.getWidth() / 2,
				-originalImage.getHeight() / 2);

		AffineTransformOp op = new AffineTransformOp(rotateTf,
				comboBox.getSelectedIndex()+1);
//	    TYPE_NEAREST_NEIGHBOR = 1;
//	    TYPE_BILINEAR = 2;
//	    TYPE_BICUBIC = 3;
		dest = op.filter(originalImage, null);
		

		parentImagePanel.setImage(dest);
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
