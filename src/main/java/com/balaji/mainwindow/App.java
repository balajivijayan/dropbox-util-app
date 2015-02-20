package com.balaji.mainwindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.balaji.utils.ConfigFile;

public class App {
	private final static Logger logger = Logger.getLogger(App.class);
	public static final String PROPERTY_FILE = "./conf/dropbox.properties";

	static {
		loadConfig();
	}

	public static void loadConfig() {
		Properties props = new Properties();
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(new File(PROPERTY_FILE)); 
					//App.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
			if (inputStream == null) {
				logger.info(PROPERTY_FILE + " file is missing");
			}
			
			props.load(inputStream);
			ConfigFile.APP_KEY = props.getProperty("dropbox.app.key");
			ConfigFile.APP_SECRET = props.getProperty("dropbox.app.secret");
			ConfigFile.APP_ACCESS_TOKEN = props.getProperty("dropbox.app.accesstoken");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		CreateGUI gui = new CreateGUI();
		if (ConfigFile.APP_ACCESS_TOKEN == null || ConfigFile.APP_ACCESS_TOKEN.isEmpty()) {
			gui.generateAccessToken();
		} else {
			logger.info("Application Started");
			gui.startApplication(ConfigFile.APP_ACCESS_TOKEN);
		}
	}

}
