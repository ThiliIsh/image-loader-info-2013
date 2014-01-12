package ImageLoader;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class JHistogramThresholdDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSlider slider;
	private JTextField textField;
	private JLabel lblThresholdValue;
	private ImagePanel parentImagePanel;
	private BufferedImage originalImage = null;

	/**
	 * Create the dialog.
	 */
	public JHistogramThresholdDlg(MainFrame frame) {
		super(frame, true);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				if (originalImage==null){
					originalImage = parentImagePanel.getImage();
					slider.setValue(130);
					textField.setText(""+130);
				}

			}
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
		
		
		parentImagePanel = frame.getImagePanel();
		originalImage = parentImagePanel.getImage();
		
		setTitle("Histogram Threshold");
		setBounds(100, 100, 450, 210);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		slider = new JSlider();
		slider.setSnapToTicks(true);
		
		slider.setMajorTickSpacing(85);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMaximum(255);
		slider.setBounds(27, 27, 376, 68);
		slider.setValue(128);
		contentPanel.add(slider);
		
		textField = new JTextField();
		textField.setText(String.valueOf(slider.getValue()));
		textField.setBounds(148, 108, 86, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		lblThresholdValue = new JLabel("Threshold value:");
		lblThresholdValue.setBounds(27, 111, 111, 14);
		contentPanel.add(lblThresholdValue);
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
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				onSlide(e);
			}
		});
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
	protected void onSlide(ChangeEvent e) {
		textField.setText(""+slider.getValue());
		parentImagePanel.setImage(ImageUtil.histogramThreshold(originalImage, slider.getValue()));			
	}
}
