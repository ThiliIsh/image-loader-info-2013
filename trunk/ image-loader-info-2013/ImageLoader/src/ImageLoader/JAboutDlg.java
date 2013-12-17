package ImageLoader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JAboutDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblAplicatieDeProcesare;
	private JLabel lblInfoAnul;
	private JLabel lblNewLabel;


	/**
	 * Create the dialog.
	 */
	public JAboutDlg() {
		setModal(true);
		setTitle("About");
		setBounds(100, 100, 450, 134);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblAplicatieDeProcesare = new JLabel("Aplicatie de procesare a imaginilor");
		lblAplicatieDeProcesare.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAplicatieDeProcesare.setBounds(105, 11, 238, 22);
		contentPanel.add(lblAplicatieDeProcesare);
		
		lblInfoAnul = new JLabel("Copyright Info anul 3, 2013");
		lblInfoAnul.setBounds(131, 44, 167, 14);
		contentPanel.add(lblInfoAnul);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(JAboutDlg.class.getResource("/ImageLoader/icons24x24/home_green_24.png")));
		lblNewLabel.setBounds(64, 11, 31, 41);
		contentPanel.add(lblNewLabel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
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
		}
	}
}
