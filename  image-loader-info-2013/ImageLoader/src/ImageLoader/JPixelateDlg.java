package ImageLoader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;

public class JPixelateDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSlider slider;
	private JTextField textField;
	private MainFrame parent;
	private ImagePanel parentImagePanel;
	private BufferedImage originalImage = null;
	private JLabel lblPixelSize;
	
	/**
	 * Create the dialog.
	 */
	public JPixelateDlg(MainFrame frame) {
		
		super(frame, true);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				if (originalImage==null){
					originalImage = parentImagePanel.getImage();
					slider.setValue(8);
				}

			}
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
		
		
		parent = frame;
		parentImagePanel = parent.getImagePanel();
		originalImage = parentImagePanel.getImage();
		
		
		
		setTitle("Pixelate");
		setBounds(100, 100, 396, 185);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		slider = new JSlider();
		slider.setMinimum(1);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(7);
		slider.setPaintLabels(true);

		slider.setMaximum(64);
		slider.setValue(8);
		
		slider.setBounds(23, 11, 333, 49);
		contentPanel.add(slider);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				onSlide(e);
			}
		});
		
		textField = new JTextField();
		textField.setText("8");
		textField.setEnabled(false);
		textField.setBounds(140, 71, 216, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		lblPixelSize = new JLabel("Pixel size:");
		lblPixelSize.setBounds(23, 74, 66, 14);
		contentPanel.add(lblPixelSize);
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
	private void onOK(ActionEvent e){
		originalImage = null;
		dispose();
	}
	private void onCancel(){
		parentImagePanel.setImage(originalImage);
		originalImage = null;
		dispose();
	}
	private void onSlide(ChangeEvent e){
		int val = slider.getValue();
		if(((originalImage.getWidth() % val)!=0) || (originalImage.getHeight() % val)!=0){
			textField.setText(""+val+" : inappropriate pixel size");
		}
		else{
			parentImagePanel.setImage(ImageUtil.pixelate(originalImage, slider.getValue()));
			textField.setText(""+val);
		}
		}
}
