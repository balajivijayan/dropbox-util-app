package com.balaji.app.foldersync;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import com.balaji.app.listeners.DeleteFile;
import com.balaji.app.listeners.UploadFile;

public class FileAlterationObserverImpl implements FileAlterationListener {
	private static Logger logger = Logger.getLogger(FileAlterationObserver.class);
	
	public void onDirectoryChange(File arg0) {

	}

	public void onDirectoryCreate(File file) {
		
	}

	public void onDirectoryDelete(File arg0) {
		
	}

	public void onFileChange(File arg0) {
		logger.info("File " + arg0.getName() +", changed in " + arg0.getAbsolutePath());
		UploadFile.uploadFile2(arg0.getAbsolutePath());
	}

	public void onFileCreate(File arg0) {
		logger.info("File " + arg0.getName() +", created in " + arg0.getAbsolutePath());
		UploadFile.uploadFile2(arg0.getAbsolutePath());
	}

	public void onFileDelete(File arg0) {
		logger.info("File " + arg0.getName() +", deleted in " + arg0.getAbsolutePath());
		DeleteFile.deleteFile2(arg0.getAbsolutePath());
	}

	public void onStart(FileAlterationObserver arg0) {
		logger.info("File Monitoring Started on " + arg0.getDirectory());
	}

	public void onStop(FileAlterationObserver arg0) {
		
	}

}
