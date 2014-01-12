package ImageLoader.gui;

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


import ImageLoader.MainFrame;
import ImageLoader.util.Histogram;
import ImageLoader.util.ImageUtil;

import com.alee.managers.language.data.Text;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class JHistogramDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private MainFrame parent;
	private Histogram histogram = null;
	private HistogramView histogramView = null;
	private ImagePanel parentImagePanel;
	private BufferedImage originalImage = null;
	private JPanel panel;
	private JLabel lblBand;
	private JComboBox comboBox;
	private DefaultComboBoxModel comboBoxModel= null;
	private JButton btnEqualise;
	
	/**
	 * Create the dialog.
	 */
	public JHistogramDlg(MainFrame frame) {
		
		super(frame, true);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				if (originalImage==null){
					originalImage = parentImagePanel.getImage();
					histogram.buildHistogram(originalImage);
					histogramView.setHistogram(histogram);
					
					
					int nrBands =	originalImage.getData().getNumBands();
					String[] bandLabels = new String[nrBands];
					for (int i = 0; i < bandLabels.length; i++) {
						bandLabels[i] = ""+i;
					}
					comboBoxModel = new DefaultComboBoxModel(bandLabels);
					comboBox.setModel(comboBoxModel);
					comboBox.setSelectedIndex(0);
					
					System.out.print(histogram);
				}

			}
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
		
		
		parent = frame;
		parentImagePanel = parent.getImagePanel();
		histogram = new Histogram();
		histogramView = new HistogramView();
		histogramView.setHistogram(histogram);
		
		setTitle("Histogram");
		setBounds(100, 100, 450, 216);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		contentPanel.add(histogramView,BorderLayout.CENTER);
		
		panel = new JPanel();
		contentPanel.add(panel, BorderLayout.EAST);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblBand = new JLabel("Band:");
		panel.add(lblBand);
		
		comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				onSelectedBand(e);
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2"}));
		comboBox.setSelectedIndex(0);
		panel.add(comboBox);
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
				
				btnEqualise = new JButton("Equalise");
				btnEqualise.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onEqualise();
					}
				});
				buttonPane.add(btnEqualise);
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


	protected void onEqualise() {
		parentImagePanel.setImage(ImageUtil.histogramEqualise(originalImage));
	}

	protected void onSelectedBand(ItemEvent e) {
		histogramView.setHistogram(histogram, comboBox.getSelectedIndex());
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
