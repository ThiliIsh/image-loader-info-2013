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


public class JRGBBalanceDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSlider rSlider;
	private JTextField rTF;
	private MainFrame parent;
	private ImagePanel parentImagePanel;
	private BufferedImage originalImage = null;
	private JSlider gSlider;
	private JTextField gTF;
	private JSlider bSlider;
	private JTextField bTF;
	private JLabel lblR;
	private JLabel lblG;
	private JLabel lblB;
	
	/**
	 * Create the dialog.
	 */
	public JRGBBalanceDlg(MainFrame frame) {
		
		super(frame, true);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				if (originalImage==null){
					originalImage = parentImagePanel.getImage();
					rSlider.setValue(0);
					gSlider.setValue(0);
					bSlider.setValue(0);
				}

			}
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
		
		parent = frame;
		parentImagePanel = parent.getImagePanel();
			
		setTitle("RGB Balance");
		setBounds(100, 100, 450, 311);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		rSlider = new JSlider();
		rSlider.setMajorTickSpacing(85);
		rSlider.setMinimum(-255);

		rSlider.setMaximum(255);
		rSlider.setValue(0);
		rSlider.setMinorTickSpacing(5);
		rSlider.setPaintTicks(true);
		
		rSlider.setPaintLabels(true);
		
		rSlider.setBounds(66, 27, 294, 49);
		contentPanel.add(rSlider);
		
		rSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				onSlide();
			}
		});
		
		rTF = new JTextField();
		rTF.setEnabled(false);
		rTF.setBounds(370, 27, 54, 20);
		contentPanel.add(rTF);
		rTF.setColumns(10);
		
		gSlider = new JSlider();

		gSlider.setValue(0);
		gSlider.setPaintTicks(true);
		gSlider.setPaintLabels(true);
		gSlider.setMinorTickSpacing(5);
		gSlider.setMinimum(-255);
		gSlider.setMaximum(255);
		gSlider.setMajorTickSpacing(85);
		gSlider.setBounds(66, 88, 294, 49);
		gSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				onSlide();
			}
		});		
		contentPanel.add(gSlider);
		
		gTF = new JTextField();
		gTF.setEnabled(false);
		gTF.setColumns(10);
		gTF.setBounds(370, 88, 54, 20);
		contentPanel.add(gTF);
		
		bSlider = new JSlider();

		bSlider.setValue(0);
		bSlider.setPaintTicks(true);
		bSlider.setPaintLabels(true);
		bSlider.setMinorTickSpacing(5);
		bSlider.setMinimum(-255);
		bSlider.setMaximum(255);
		bSlider.setMajorTickSpacing(85);
		bSlider.setBounds(66, 148, 294, 49);
		bSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				onSlide();
			}
		});		
		contentPanel.add(bSlider);
		
		bTF = new JTextField();
		bTF.setEnabled(false);
		bTF.setColumns(10);
		bTF.setBounds(370, 148, 54, 20);
		contentPanel.add(bTF);
		
		lblR = new JLabel("R:");
		lblR.setBounds(36, 33, 30, 14);
		contentPanel.add(lblR);
		
		lblG = new JLabel("G:");
		lblG.setBounds(36, 94, 30, 14);
		contentPanel.add(lblG);
		
		lblB = new JLabel("B:");
		lblB.setBounds(36, 154, 30, 14);
		contentPanel.add(lblB);
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
	protected void onSlide() {
		
		rTF.setText(""+rSlider.getValue());
		gTF.setText(""+gSlider.getValue());
		bTF.setText(""+bSlider.getValue());
		
		//if(!rSlider.getValueIsAdjusting() && !gSlider.getValueIsAdjusting() && !bSlider.getValueIsAdjusting())
		parentImagePanel.setImage(ImageUtil.RGBbalanceV2(originalImage, rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));

	}
}
