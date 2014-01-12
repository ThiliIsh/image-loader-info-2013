package ImageLoader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


import com.alee.managers.language.data.Text;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

public class JGammaContrastDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSlider slider;
	private JTextField textField;
	private MainFrame parent;
	private ImagePanel parentImagePanel;
	private BufferedImage originalImage = null;
	private JLabel lblGammaValue;
	
	/**
	 * Create the dialog.
	 */
	public JGammaContrastDlg(MainFrame frame) {
		
		super(frame, true);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				if (originalImage==null){
					originalImage = parentImagePanel.getImage();
					slider.setValue(100);
					textField.setText(""+1.0);
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
		
		
		setTitle("Gamma Contrast");
		setBounds(100, 100, 450, 216);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		slider = new JSlider();
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(100);

		slider.setMaximum(500);
		slider.setValue(100);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		
		slider.setBounds(23, 11, 384, 80);
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( 0 ), new JLabel("0.0") );
		labelTable.put( new Integer( 100 ), new JLabel("1.0") );
		labelTable.put( new Integer( 500 ), new JLabel("5.0") );
		slider.setLabelTable( labelTable );
		contentPanel.add(slider);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				onSlide(e);
			}
		});
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setBounds(132, 99, 104, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		lblGammaValue = new JLabel("Gamma value:");
		lblGammaValue.setBounds(33, 102, 110, 14);
		contentPanel.add(lblGammaValue);
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
	private void onSlide(ChangeEvent e) {
		double gamma = slider.getValue() / 100.0;
		textField.setText("" + gamma);
		
		parentImagePanel.setImage(ImageUtil.gammaContrast(originalImage, gamma));			
	}
}
