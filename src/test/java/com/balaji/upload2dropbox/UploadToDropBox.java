package com.balaji.upload2dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;

public class UploadToDropBox {
	private final String APP_KEY = "kt51q1otg9pxxec";
	private final String APP_SECRET = "pitxb3ti7hmk7io";
	private DbxClient client = null;

	public void initialize() {
		String code = null;
		DbxAuthFinish authFinish = null;
		String accessToken = null;
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
				Locale.getDefault().toString());
		// DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config,
		// appInfo);

		/*
		 * String authorizeUrl = webAuth.start();
		 * System.out.println("1. Go to: " + authorizeUrl); System.out
		 * .println("2. Click \"Allow\" (you might have to log in first)");
		 * System.out.println("3. Copy the authorization code."); try { code =
		 * new BufferedReader(new InputStreamReader(System.in))
		 * .readLine().trim(); } catch (IOException e) { e.printStackTrace(); }
		 * 
		 * try { authFinish = webAuth.finish(code); accessToken =
		 * authFinish.accessToken; } catch (DbxException e) {
		 * e.printStackTrace(); }
		 * System.out.println("Access Token: "+accessToken);
		 */
		getUserDetails(config,
				"DH-sJSO8r0kAAAAAAAAACVh13KovnVdVuZxa_OKrGDPzEkiCBNFhNLobf0TB0XUE");
	}

	public void getUserDetails(DbxRequestConfig config, String accessToken) {
		client = new DbxClient(config, accessToken);
		try {
			System.out.println("Linked account: "
					+ client.getAccountInfo().displayName);
			System.out.println("Account Country: "
					+ client.getAccountInfo().country);
			
			//fileUpload(config, accessToken);
			Upload(new File("D:/Doc"));
		} catch (DbxException e) {
			e.printStackTrace();
		}
	}
	
	
	public void Upload(File folder) {
		File file = null;
		File[] subFiles = null;
		InputStream fileInputStream = null;
		DbxEntry.File resultFile = null;
		try {
			for (File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					System.out.println("Directory Created");
					DbxEntry.Folder uploadFolderObj = client.createFolder("/"
							+ fileEntry.getName());
					Upload(fileEntry);
				} else {
					/*resultFile = client.uploadFile("/" + fileEntry.getName(),
							DbxWriteMode.add(), fileEntry.length(),
							new FileInputStream(fileEntry));*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} /*
		 * finally { try { fileInputStream.close(); } catch (IOException e) {
		 * e.printStackTrace(); } }
		 */
	}	
	

	public void fileUpload(DbxRequestConfig config, String accessToken) {
		try {
			File inputFile = new File ("Dummy.txt");
			if (!inputFile.isFile()) {
				inputFile.createNewFile();
			}
			FileInputStream inputStream = new FileInputStream(inputFile);
			System.out.println("" + inputFile);
			DbxEntry.File fileToUpload = client.uploadFile("/DummyUpload/" + inputFile.getName(), DbxWriteMode.add(), inputFile.length(), inputStream);
			//Upload Path should be Mentioned and must not end with "/"
			//System.out.println("Uploaded File " + fileToUpload.toString());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		UploadToDropBox u2DB = new UploadToDropBox();
		u2DB.initialize();
	}
	
	

}
