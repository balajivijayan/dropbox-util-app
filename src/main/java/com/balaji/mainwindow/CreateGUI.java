package com.balaji.mainwindow;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.balaji.app.foldersync.FileListener;
import com.balaji.app.listeners.DeleteFile;
import com.balaji.app.listeners.DownloadFile;
import com.balaji.app.listeners.MouseListenerImpl;
import com.balaji.app.listeners.UploadFile;
import com.balaji.utils.ConfigFile;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;

public class CreateGUI {
	private final static Logger logger = Logger.getLogger(CreateGUI.class);
	private JFrame frame;
	private String oauthCode;

	public void generateAccessToken() {
		JPanel generatePanel = null;
		JLabel instructionLabel = null;
		JButton generateButton = null;
		Font labelFont = null;
		try {
			
		    try {
		        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    } catch (ClassNotFoundException e) {
		    	logger.info("Exception in startApplication() method in createGUI class", e);
		    } catch (InstantiationException e) {
		    	logger.info("Exception in startApplication() method in createGUI class", e);
		    } catch (IllegalAccessException e) {
		        e.printStackTrace();
		    } catch (UnsupportedLookAndFeelException e) {
		    	logger.info("Exception in startApplication() method in createGUI class", e);
		    }
			
			frame = new JFrame("FileDropr");
			generatePanel = new JPanel();
			instructionLabel = new JLabel();
			generateButton = new JButton("Generate Access Token");
			labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
			instructionLabel.setFont(labelFont);
			instructionLabel
					.setText("<html><body><u>Steps to provide access to MyFileSyncer application:</u> "
							+ "<br> 1. Click Generate Token. <br>"
							+ "2. You'll be redirected to Drop Box's website.<br>"
							+ "3. Sign in/Sign Up to your Drop Box account and click on Allow Access to MyFileSyncer application.<br>"
							+ "5. Copy the generated code and paste it in the prompt box of the application.</body></html> ");

/*			imagePath = "/imgicon.png";
			imgStream = CreateGUI.class.getResourceAsStream(imagePath);
			myImg = ImageIO.read(imgStream);
			frame.setIconImage(myImg);*/
			
	        frame.setIconImage(ImageIO.read(new FileInputStream("./images/imgicon.png")));
			
			generatePanel.add(generateButton);
			frame.getContentPane().add(BorderLayout.NORTH, instructionLabel);
			frame.getContentPane().add(BorderLayout.SOUTH, generatePanel);
			frame.setVisible(true);
			frame.setSize(300, 300);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
			
			generateButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					DbxAppInfo appInfo = null;
					DbxRequestConfig config = null;
					DbxWebAuthNoRedirect webAuth = null;
					String authorizeUrl = null;

					DbxAuthFinish authFinish = null;
					String accessToken = null;
					try {
						appInfo = new DbxAppInfo(ConfigFile.APP_KEY,
								ConfigFile.APP_SECRET);
						config = new DbxRequestConfig("JavaTutorial/1.0",
								Locale.getDefault().toString());
						webAuth = new DbxWebAuthNoRedirect(config, appInfo);
						authorizeUrl = webAuth.start();
						Desktop.getDesktop().browse(
								new URL(authorizeUrl).toURI());

						oauthCode = JOptionPane
								.showInputDialog("Enter the Access Code");
						authFinish = webAuth.finish(oauthCode);
						accessToken = authFinish.accessToken;
						ConfigFile.APP_ACCESS_TOKEN = accessToken;

						logger.info("Access Token " + accessToken);
						frame.dispose();
						storeAccessToken(accessToken);
						
					} catch (DbxException e1) {
						logger.info("Exception in generateAccessToken()#generateButton.addActionListener() method in createGUI class", e1);
					} catch (MalformedURLException e1) {
						logger.info("Exception in generateAccessToken()#generateButton.addActionListener() method in createGUI class", e1);
					} catch (IOException e1) {
						logger.info("Exception in generateAccessToken()#generateButton.addActionListener() method in createGUI class", e1);
					} catch (URISyntaxException e1) {
						logger.info("Exception in generateAccessToken()#generateButton.addActionListener() method in createGUI class", e1);
					}
				}
			});
		} catch (IOException e2) {
			logger.info("Exception in generateAccessToken() method in createGUI class", e2);
		}

	}

	public void storeAccessToken(String accessToken) {
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			/*output = new FileOutputStream("src/main/resources/"
					+ App.PROPERTY_FILE);*/
			output = new FileOutputStream(App.PROPERTY_FILE);
			prop.setProperty("dropbox.app.key", ConfigFile.APP_KEY);
			prop.setProperty("dropbox.app.secret", ConfigFile.APP_SECRET);
			prop.setProperty("dropbox.app.accesstoken", accessToken);
			prop.store(output, "Updated the keys");
			startApplication(accessToken);
			logger.info("Property Stored");
		} catch (Exception e) {
			logger.info("Exception in storeAccessToken() method in createGUI class", e);
		}
	}

	public void startApplication(String accessToken) {
		DbxClient client = null;
		JPanel mainPanel = null;
		//String imagePath = null;
		InputStream imgStream = null;
		BufferedImage myImg = null;
		DbxRequestConfig config = null;
		JButton uploadButton = null;
		JButton deleteButton = null;
		JButton downloadButton = null;
		JButton syncButton = null;
		JPanel welcomePanel = null;
		JPanel bottomPanel = null;
		JLabel infoLabel = null;
		try {
			
		    try {
		        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    } catch (ClassNotFoundException e) {
		    	logger.info("Exception in startApplication() method in createGUI class", e);
		    } catch (InstantiationException e) {
		    	logger.info("Exception in startApplication() method in createGUI class", e);
		    } catch (IllegalAccessException e) {
		        e.printStackTrace();
		    } catch (UnsupportedLookAndFeelException e) {
		    	logger.info("Exception in startApplication() method in createGUI class", e);
		    }
			
			frame = new JFrame("FileDropr");
			
			mainPanel = new JPanel();
			welcomePanel = new JPanel();
			bottomPanel = new JPanel();
			
			//imagePath = "./images/imgicon.png";
			imgStream = new FileInputStream("./images/imgicon.png");
			myImg = ImageIO.read(imgStream);
			frame.setIconImage(myImg);
			
			//frame.setIconImage(ImageIO.read(new FileInputStream("./images/imgicon.png")));
			config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString()); 
			client = new DbxClient(config, accessToken);
	
			logger.info("Linked account: " + client.getAccountInfo().displayName);
			logger.info("Account Country: " + client.getAccountInfo().country);
			
			uploadButton = new JButton("Upload File");
			deleteButton = new JButton("Delete File");
			downloadButton = new JButton("Download File");
			syncButton = new JButton("Sync Folder");
			infoLabel = new JLabel("<html><head><title></title></head><body><p><span style=\"font-size:9px;\"><strong>Author:</strong> Balaji Vijayan</span></p><p><span style=\"font-size:9px;\"><strong>Git Repo:</strong>&nbsp;<a href=\"https://github.com/balajivijayan/dropbox-util-app.git\">https://github.com/balajivijayan/dropbox-util-app</a></span></p><p>&nbsp;</p></body></html>");
			infoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			infoLabel.addMouseListener(new MouseListenerImpl());
			
			mainPanel.add(BorderLayout.NORTH, uploadButton);
			mainPanel.add(BorderLayout.CENTER, deleteButton);
			mainPanel.add(BorderLayout.SOUTH, downloadButton);
			mainPanel.add(BorderLayout.AFTER_LAST_LINE, syncButton);
			mainPanel.setLayout(new FlowLayout());
			welcomePanel.setLayout(new FlowLayout());
			welcomePanel.add(new JLabel("<html><body><center>Welcome " + client.getAccountInfo().displayName+"! </center><br><br>" +
					"Select option below to proceed<body><html>"), BorderLayout.CENTER);
			
			bottomPanel.setLayout(new FlowLayout());
			bottomPanel.add(infoLabel);
			
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(welcomePanel, BorderLayout.NORTH);
			frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
			
			uploadButton.addActionListener(new UploadFile(client, frame));
			deleteButton.addActionListener(new DeleteFile(client, myImg));
			downloadButton.addActionListener(new DownloadFile(client, myImg));
			syncButton.addActionListener(new FileListener(frame));
			
			frame.setVisible(true);
			frame.setSize(400, 250);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

		} catch (Exception e) {
			logger.info("Exception in startApplication() method in createGUI class", e);
		}

	}

	/*
	 * public static void main(String args[]) { CreateGUI go = new CreateGUI();
	 * go.storeAccessToken(
	 * "DH-sJSO8r0kAAAAAAAAACVh13KovnVdVuZxa_OKrGDPzEkiCBNFhNLobf0TB0XUE"); }
	 */

}
