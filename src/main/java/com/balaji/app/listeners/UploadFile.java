package com.balaji.app.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxWriteMode;

public class UploadFile implements ActionListener {
	private static Logger logger = Logger.getLogger(UploadFile.class);
	private static DbxClient client = null;
	private static JFrame frame = null;
	
	public UploadFile(DbxClient client, JFrame frame) {
		this.client = client;
		this.frame = frame;
	}



	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = null;
		String selectedFilePath = null;
		String replacedFolderPath = null;
		try {
			fileChooser = new JFileChooser();
			fileChooser.showDialog(frame, "Upload");
			fileChooser.setVisible(true);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setDialogTitle("Select file to upload");
			selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
			replacedFolderPath = selectedFilePath.replace("\\", "/");
			logger.info("File to Upload " + replacedFolderPath);
			uploadFile(replacedFolderPath);
			//logger.info("File to Upload " + selectedFilePath);
			//uploadFile(selectedFilePath);
			//uploadFile("C:\\Users\\Daddu\\Documents\\Balaji.txt");
		} catch (Exception e2) {
			logger.error(e2);
		}
	}
	
	public void uploadFile(String filePath) {
		File inputFile = null;
		FileInputStream inputStream = null;
		try {
			inputFile = new File(filePath);
			logger.info("input Stream param " + filePath);
			inputStream = new FileInputStream(filePath);
			DbxEntry.File result = client.uploadFile("/" + inputFile.getName(), DbxWriteMode.add(), inputFile.length(), inputStream);
			logger.info("File uploaded " + result.name);
			if (result.isFile()) {
				JOptionPane.showMessageDialog(frame, "File upload successful");
			} else {
				JOptionPane.showMessageDialog(frame, "File upload unsuccessful");
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public static void uploadFile2(String filePath) {
		File inputFile = null;
		FileInputStream inputStream = null;
		try {
			inputFile = new File(filePath);
			logger.info("input Stream param " + filePath);
			inputStream = new FileInputStream(filePath);
			DbxEntry.File result = client.uploadFile("/"+ inputFile.getParentFile().getName() + "/" + inputFile.getName(), DbxWriteMode.add(), inputFile.length(), inputStream);
			logger.info("File uploaded " + result.name);
/*			if (result.isFile()) {
				JOptionPane.showMessageDialog(frame, "File upload successful");
			} else {
				JOptionPane.showMessageDialog(frame, "File upload unsuccessful");
			}*/
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}
