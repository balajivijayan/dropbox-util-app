package com.balaji.app.foldersync;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

public class FileListener implements ActionListener{
	private static Logger logger = Logger.getLogger(FileListener.class);
	private FileAlterationMonitor monitor = null;
	private JFrame frame= null;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		getInputFromUser();
	}
	
	public FileListener(JFrame frame) {
		this.frame = frame;
	}

	public void getInputFromUser() {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.showDialog(frame, "Select");
			fileChooser.setDialogTitle("Select the folder to sync with Drop Box account");

			String path = fileChooser.getSelectedFile().getAbsolutePath();
			String replacedFolderPath = path.replace("\\", "/");
			logger.info("Folder sent to file monitor class" + replacedFolderPath);

			initializeFileMonitor(replacedFolderPath);
			
		} catch (Exception e) {
			logger.info("Exception in method getInputFromUser() in FileListener class", e);
		}
	}

	public void initializeFileMonitor(String folderToMonitor) {
		final File fileDirectory = new File(folderToMonitor);
		FileAlterationObserver observer = new FileAlterationObserver(
				fileDirectory);
		observer.addListener(new FileAlterationObserverImpl());

		monitor = new FileAlterationMonitor();
		monitor.addObserver(observer);
		
		try {
			JOptionPane.showMessageDialog(frame, "Synchronization Started");
			monitor.start();
		} catch (Exception e) {
			logger.info("Exception in method initializeFilemonitor() in FileListener class", e);
		}
	}
	
	public void exit() {
		try {
			monitor.stop();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*	public static void main(String[] args) {
		FileListener fl = new FileListener();
		System.out.println("Application Started");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter Start/Stop to Start/Stop application");
		while (true) {
			String input = null;
			try {
				input = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (input.equalsIgnoreCase("quit")) {
				fl.exit();
			} else {
				fl.getInputFromUser();
			}
		}

	}*/
}
