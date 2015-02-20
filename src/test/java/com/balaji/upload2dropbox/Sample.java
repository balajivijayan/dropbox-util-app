package com.balaji.upload2dropbox;

import java.io.File;
import java.io.IOException;

public class Sample {
	
	public static void main(String args[]) {
		Sample s = new Sample();
		s.listFilesForFolder(new File("D:/Doc"));
	}
	
	public void listFilesForFolder(final File folder) {
/*		System.out.println(folder.getAbsolutePath());
		try {
			System.out.println(folder.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	System.out.println("/"+fileEntry.getName());
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println("||->"+fileEntry.getName());
	        }
	    }
	}
}
/*
||->INISlistofcountries.pdf
New
||->Game Of Thrones Tab.pdf
New folder
||->1-Attra Foundation Level Training - Introduction To Credit Cards V1.0.ppt
||->20_Quickfire_Exercises.pdf
dummy
||->tamil Guitar tabs.pdf*/
