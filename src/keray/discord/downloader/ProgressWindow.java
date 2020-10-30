package keray.discord.downloader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ProgressWindow {
	JFrame frame;
	public JProgressBar progressBar;
	public JLabel label;
	
	public ProgressWindow() {
		try {
			SwingUtilities.invokeAndWait(this::initGUI);
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void initGUI() {
		frame = new JFrame("Java Discord Updater");
		progressBar = new JProgressBar();
		label = new JLabel("something went wrong");
		
		progressBar.setPreferredSize(new Dimension(300, 30));

		((JComponent)frame.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
		
		frame.add(progressBar, BorderLayout.CENTER);
		frame.add(label, BorderLayout.NORTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
