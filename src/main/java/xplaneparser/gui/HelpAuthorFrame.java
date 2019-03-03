package xplaneparser.gui;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.lang.invoke.MethodHandles;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpAuthorFrame extends JFrame {

	private static final String SCENARIO_LOGO_ICON = "/images/Map_icon.png";
	private JPanel contentPane;
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					HelpAuthorFrame frame = new HelpAuthorFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HelpAuthorFrame() {

		setType(Type.UTILITY);
		setBounds(100, 100, 300, 348);
		String author = "Maurizio Sodano";
		String email = "maurizio.sodano@gmail.com";
		String message1 = "Author : " + author;
		String message2 = "email : " + email;
		String title = "About the author";
		setTitle(title);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		Image img = Toolkit.getDefaultToolkit().getImage(HelpAuthorFrame.class.getResource(SCENARIO_LOGO_ICON));
		ImageIcon icon = new ImageIcon(img);
		JLabel picLabel = new JLabel(icon);
		contentPane.add(picLabel);

		JLabel lblAuthor = new JLabel(message1);
		contentPane.add(lblAuthor);

		JLabel lblEmail = new JLabel(message2);
		contentPane.add(lblEmail);
	}

}

