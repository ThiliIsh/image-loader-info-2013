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

public class JContrastDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSlider slider;
	private JTextField textField;
	private MainFrame parent;
	private ImagePanel parentImagePanel;
	private BufferedImage originalImg;
	
	/**
	 * Create the dialog.
	 */
	public JContrastDlg(MainFrame frame) {
		
		super(frame, true);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				originalImg = parentImagePanel.getImage();
			}
		});
		
		
		parent = frame;
		parentImagePanel = parent.getImagePanel();
		originalImg = parentImagePanel.getImage();
		
		
		
		setTitle("Contrast");
		setBounds(100, 100, 450, 155);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		slider = new JSlider();
		slider.setMinimum(1);

		slider.setMaximum(2000);
		slider.setValue(1000);
		slider.setMinorTickSpacing(20);
		slider.setPaintTicks(true);
		
		slider.setPaintLabels(true);
		
		slider.setBounds(23, 29, 249, 49);
		contentPanel.add(slider);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				onSlide();
			}
		});
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setBounds(282, 32, 86, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
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
						parentImagePanel.setImage(originalImg);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	protected void onSlide() {
		float scaleVal;
		scaleVal = 1.0f * slider.getValue()/1000.0f;
		textField.setText(""+scaleVal);
		
		
		parentImagePanel.setImage(ImageUtil.contrast(originalImg, scaleVal));
		
		
	}
}
