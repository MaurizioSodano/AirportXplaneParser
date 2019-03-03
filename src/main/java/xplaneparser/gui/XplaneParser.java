package xplaneparser.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.lang.invoke.MethodHandles;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xplaneparser.parser.Parser;

public class XplaneParser extends JFrame {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
	private static final String OPEN_XPLANE = "Open Xplane/Flight Gears Airport file (apt.dat)";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
		
			XplaneParser dialog = new XplaneParser();
			
			UIManager.put("swing.boldMetal", Boolean.FALSE);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	/**
	 * Create the dialog.
	 */
	public XplaneParser() {
		setBounds(100, 100, 512, 264);
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(event -> fireOpenButton());
		
		JMenuBar menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		menuBar.add(horizontalGlue);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem menuItem = new JMenuItem("About Xplane Airport Parser");
		menuItem.addActionListener(event -> {
			HelpAuthorFrame frame = new HelpAuthorFrame();
			frame.pack();
			frame.setVisible(true);
		});
		mnHelp.add(menuItem);
		getContentPane().add(btnOpen, BorderLayout.SOUTH);

	}
	
	private void fireOpenButton() {
		JFrame frame = new JFrame();
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setDialogTitle(OPEN_XPLANE);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.dat (Airport structure)", "dat");
		fileChooser.setFileFilter(filter);
		String filePath = "";

		int returnVal = fileChooser.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			filePath = fileChooser.getSelectedFile().getPath();
		}

		parseXplane(filePath);
	}

	private void parseXplane(String filePath) {
		Parser parser=new Parser();
		parser.parse(filePath);
		
	}

}
