package ms.xplaneparser.simplegui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.invoke.MethodHandles;

public class HelpAuthorFrame extends JFrame {

	private static final String SCENARIO_LOGO_ICON = "/images/Map_icon.png";
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				HelpAuthorFrame frame = new HelpAuthorFrame();
				frame.setVisible(true);
			} catch (Exception e) {
				logger.error(e.getMessage());
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

		JPanel contentPane = new JPanel();
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

