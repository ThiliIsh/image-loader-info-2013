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

public class JBitPlaneSlicingDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSlider slider;
	private JTextField textField;
	private MainFrame parent;
	private ImagePanel parentImagePanel;
	private BufferedImage originalImage = null;
	private JLabel lblBitValue;
	
	/**
	 * Create the dialog.
	 */
	public JBitPlaneSlicingDlg(MainFrame frame) {
		
		super(frame, true);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				if (originalImage==null){
					originalImage = parentImagePanel.getImage();
					slider.setValue(0);
					textField.setText("0");
				}

			}
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
		
		
		parent = frame;
		parentImagePanel = parent.getImagePanel();
		//originalImage = parentImagePanel.getImage();
		
		
		setTitle("Bit Plane Slicing");
		setBounds(100, 100, 450, 216);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		slider = new JSlider();
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(1);

		slider.setMaximum(7);
		slider.setValue(0);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		
		slider.setBounds(23, 11, 384, 80);
		contentPanel.add(slider);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				onSlide(e);
			}
		});
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setBounds(146, 99, 104, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		lblBitValue = new JLabel("Bit Level:");
		lblBitValue.setBounds(33, 102, 95, 14);
		contentPanel.add(lblBitValue);
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

		textField.setText("" + slider.getValue());
		
		parentImagePanel.setImage(ImageUtil.getBitPlanes(originalImage, slider.getValue()));			
	}
}
