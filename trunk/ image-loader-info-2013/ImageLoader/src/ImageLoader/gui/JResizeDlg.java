package ImageLoader.gui;

import javax.swing.JPanel;

import java.awt.AWTEvent;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.RenderingHints;

import javax.swing.JDialog;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import java.awt.GridBagConstraints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTabbedPane;

import ImageLoader.MainFrame;

public class JResizeDlg extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jCommandPanel = null;
	private JButton jCancelButton = null;
	private JButton jOkButton = null;
	private JPanel jScalePanel = null;
	private JSlider jScaleYSlider = null;
	private JSlider jScaleXSlider = null;
	private JLabel jScaleXLabel = null;
	private JLabel jScaleYLabel = null;
	private ImagePanel parentImagePanel = null;
	private BufferedImage originalImage = null;
	private JButton jApplyButton = null;
	private JTextField xValTextField;
	private JTextField yValTextField;
	private JTabbedPane tabbedPane;
	private JPanel jSizePanel;
	private JPanel panel_1;
	private JCheckBox chckbxApplyDirectly;
	private JLabel label;
	private JCheckBox jProportionCheckBox;
	private JComboBox comboBox;
	private JLabel lblWidth;
	private JLabel lblHeight;
	private JTextField wTextField;
	private JTextField hTextField;

	/**
	 * @param owner
	 */
	public JResizeDlg(MainFrame frame) {
		super(frame, true);
		parentImagePanel = frame.getImagePanel();
		initialize();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				if (originalImage == null) {
					originalImage = parentImagePanel.getImage();
					jScaleXSlider.setValue(0);
					jScaleYSlider.setValue(0);
					xValTextField.setText("" + 0);
					yValTextField.setText("" + 0);
					wTextField.setText("" + 0);
					hTextField.setText("" + 0);
				}

			}

			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(399, 320);
		this.setModal(true);
		this.setTitle("Resize");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJCommandPanel(), BorderLayout.SOUTH);

			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			jContentPane.add(tabbedPane, BorderLayout.NORTH);
			tabbedPane.addTab("Scale", null, getJScalePanel(), null);

			jSizePanel = new JPanel();
			tabbedPane.addTab("Size", null, jSizePanel, null);
			jSizePanel.setLayout(null);

			lblWidth = new JLabel("Width:");
			lblWidth.setBounds(20, 11, 67, 14);
			jSizePanel.add(lblWidth);

			lblHeight = new JLabel("Height:");
			lblHeight.setBounds(20, 36, 67, 14);
			jSizePanel.add(lblHeight);

			wTextField = new JTextField();
			wTextField.setBounds(97, 8, 90, 20);
			jSizePanel.add(wTextField);
			wTextField.setColumns(10);

			hTextField = new JTextField();
			hTextField.setBounds(97, 33, 90, 20);
			jSizePanel.add(hTextField);
			hTextField.setColumns(10);

			panel_1 = new JPanel();
			jContentPane.add(panel_1, BorderLayout.CENTER);
			panel_1.setLayout(null);

			chckbxApplyDirectly = new JCheckBox("Apply directly");
			chckbxApplyDirectly.setBounds(15, 5, 143, 23);
			panel_1.add(chckbxApplyDirectly);

			label = new JLabel("Interpolation:");
			label.setBounds(164, 9, 116, 14);
			panel_1.add(label);

			jProportionCheckBox = new JCheckBox();
			jProportionCheckBox.setBounds(15, 31, 143, 23);
			jProportionCheckBox.setText("Preserve aspect ratio");
			panel_1.add(jProportionCheckBox);

			comboBox = new JComboBox();
			comboBox.setBounds(164, 34, 188, 20);
			comboBox.setModel(new DefaultComboBoxModel(new String[] {
					"TYPE_NEAREST_NEIGHBOR", "TYPE_BILINEAR", "TYPE_BICUBIC" }));
			comboBox.setSelectedIndex(0);
			panel_1.add(comboBox);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jCommandPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJCommandPanel() {
		if (jCommandPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			jCommandPanel = new JPanel();
			jCommandPanel.setLayout(flowLayout);
			jCommandPanel.add(getJApplyButton(), null);
			jCommandPanel.add(getJOkButton(), null);
			jCommandPanel.add(getJCancelButton(), null);
		}
		return jCommandPanel;
	}

	/**
	 * This method initializes jCancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJCancelButton() {
		if (jCancelButton == null) {
			jCancelButton = new JButton();
			jCancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onCancel();
				}
			});
			jCancelButton.setText("Cancel");
		}
		return jCancelButton;
	}

	/**
	 * This method initializes jOkButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJOkButton() {
		if (jOkButton == null) {
			jOkButton = new JButton();
			jOkButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onOK(e);
				}
			});
			jOkButton.setText("Ok");
		}
		return jOkButton;
	}

	/**
	 * This method initializes jCenterPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJScalePanel() {
		if (jScalePanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 0, 5, 5);
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridx = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(0, 0, 5, 5);
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridx = 2;
			jScalePanel = new JPanel();
			GridBagLayout gbl_jScalePanel = new GridBagLayout();
			gbl_jScalePanel.columnWidths = new int[] { 29, 107, 246, 0 };
			gbl_jScalePanel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
			jScalePanel.setLayout(gbl_jScalePanel);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 0, 5, 5);
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			jScaleXLabel = new JLabel();
			jScaleXLabel.setText("X Scale factor");
			jScalePanel.add(jScaleXLabel, gridBagConstraints2);

			xValTextField = new JTextField();
			xValTextField.setText("0");
			GridBagConstraints gbc_xValTextField = new GridBagConstraints();
			gbc_xValTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_xValTextField.insets = new Insets(0, 0, 5, 5);
			gbc_xValTextField.gridx = 1;
			gbc_xValTextField.gridy = 1;
			jScalePanel.add(xValTextField, gbc_xValTextField);
			xValTextField.setColumns(10);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(0, 0, 5, 5);
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 2;
			jScaleYLabel = new JLabel();
			jScaleYLabel.setText("Y Scale factor");
			jScalePanel.add(jScaleYLabel, gridBagConstraints3);

			yValTextField = new JTextField();
			yValTextField.setText("0");
			GridBagConstraints gbc_yValTextField = new GridBagConstraints();
			gbc_yValTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_yValTextField.insets = new Insets(0, 0, 5, 5);
			gbc_yValTextField.gridx = 1;
			gbc_yValTextField.gridy = 3;
			jScalePanel.add(yValTextField, gbc_yValTextField);
			yValTextField.setColumns(10);
			jScalePanel.add(getJScaleYSlider(), gridBagConstraints);
			jScalePanel.add(getJScaleXSlider(), gridBagConstraints1);
		}
		return jScalePanel;
	}

	/**
	 * This method initializes jScaleYSlider
	 * 
	 * @return javax.swing.JSlider
	 */
	private JSlider getJScaleYSlider() {
		if (jScaleYSlider == null) {
			jScaleYSlider = new JSlider();
			jScaleYSlider.setMajorTickSpacing(25);
			jScaleYSlider.setMinorTickSpacing(10);
			jScaleYSlider.setPaintLabels(true);
			jScaleYSlider.setPaintTicks(true);
			jScaleYSlider.setValue(0);
			jScaleYSlider.setMinimum(-100);
			jScaleYSlider
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(javax.swing.event.ChangeEvent e) {
							if (jProportionCheckBox.isSelected()) {
								jScaleXSlider.setValue(jScaleYSlider.getValue());
							}
							double yFactor;
							if (jScaleYSlider.getValue() < 0)
								yFactor = 1.0 - Math.abs(jScaleYSlider
										.getValue() - 1) / 100.0;
							else
								yFactor = 1.0 + jScaleYSlider.getValue() / 100.0;

							yValTextField.setText(String.valueOf(yFactor));

							if (chckbxApplyDirectly.isSelected())
								onResize();
						}
					});
		}
		return jScaleYSlider;
	}

	/**
	 * This method initializes jScaleXSlider
	 * 
	 * @return javax.swing.JSlider
	 */
	private JSlider getJScaleXSlider() {
		if (jScaleXSlider == null) {
			jScaleXSlider = new JSlider();
			jScaleXSlider.setPaintLabels(true);
			jScaleXSlider.setMajorTickSpacing(25);
			jScaleXSlider.setMinorTickSpacing(10);
			jScaleXSlider.setValue(0);
			jScaleXSlider.setMinimum(-100);
			jScaleXSlider.setPaintTicks(true);
			jScaleXSlider
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(javax.swing.event.ChangeEvent e) {
							if (jProportionCheckBox.isSelected()) {
								jScaleYSlider.setValue(jScaleXSlider.getValue());
							}
							double xFactor;
							if (jScaleXSlider.getValue() < 0)
								xFactor = 1.0 - Math.abs(jScaleXSlider
										.getValue() - 1) / 100.0;
							else
								xFactor = 1.0 + jScaleXSlider.getValue() / 100.0;

							xValTextField.setText(String.valueOf(xFactor));

							if (chckbxApplyDirectly.isSelected())
								onResize();
						}
					});
		}
		return jScaleXSlider;
	}

	private void onResize() {
		double xFactor = 0.0;
		double yFactor = 0.0;
	    int sourceWidth = originalImage.getWidth();
	    int sourceHeight = originalImage.getHeight();	
	    int destWidth = 0;
	    int destHeight = 0;
		//System.out.println(tabbedPane.getSelectedIndex());
		
		if (tabbedPane.getSelectedIndex() == 1) {
			destWidth = Integer.parseInt(wTextField.getText());
			destHeight = Integer.parseInt(hTextField.getText());

		    xFactor = ((double) destWidth) / (double) sourceWidth;
		    yFactor = ((double) destHeight) / (double) sourceHeight;		    

	        if (destWidth <= 0) {
	            xFactor = yFactor;
	            destWidth = (int) Math.rint(xFactor * sourceWidth);
	        }
	        if (destHeight <= 0) {
	            yFactor = xFactor;
	            destHeight = (int) Math.rint(yFactor * sourceHeight);
	        }		    
		    
		}
		if (tabbedPane.getSelectedIndex() == 0) {
			xFactor = Double.parseDouble(xValTextField.getText());
			if (xFactor == 0.0)
				xFactor = 1.0;
			yFactor = Double.parseDouble(yValTextField.getText());
			if (yFactor == 0.0)
				yFactor = 1.0;
			if (jProportionCheckBox.isSelected())
				yFactor = xFactor;
			
			destWidth = (int) Math.rint(xFactor * sourceWidth);
			destHeight = (int) Math.rint(yFactor * sourceHeight);
		} 

		

		BufferedImage dest = null;
		 dest = new BufferedImage(destWidth, destHeight, originalImage.getType());

		AffineTransform scale = new AffineTransform();
		scale.setToScale(xFactor, yFactor);

		AffineTransformOp op = new AffineTransformOp(scale,
				comboBox.getSelectedIndex() + 1);
		// TYPE_NEAREST_NEIGHBOR = 1;
		// TYPE_BILINEAR = 2;
		// TYPE_BICUBIC = 3;

		//dest = op.filter(originalImage, null);
		op.filter(originalImage, dest);

		// TO DO: Image type is wrong after affine transformation!!
		
		parentImagePanel.setImage(dest);
		//parentImagePanel.setImage(ImageUtil.convertImageType(dest, originalImage.getType()));

	}

	/**
	 * This method initializes jApplyButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJApplyButton() {
		if (jApplyButton == null) {
			jApplyButton = new JButton();
			jApplyButton.setText("Apply");
			jApplyButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onResize();
				}
			});
		}
		return jApplyButton;
	}

	private void onOK(ActionEvent e) {
		originalImage = null;
		dispose();
	}

	private void onCancel() {
		parentImagePanel.setImage(originalImage);
		originalImage = null;
		dispose();
	}
}
