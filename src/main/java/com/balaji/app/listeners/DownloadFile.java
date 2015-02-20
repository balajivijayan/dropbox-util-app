package com.balaji.app.listeners;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;

public class DownloadFile implements ActionListener{
	private static Logger logger = Logger.getLogger(DownloadFile.class);
	private DbxClient client = null;
	private BufferedImage imgStream = null;
	private JFrame frame = null;
	private JTree tree = null;
	private DefaultMutableTreeNode root= null;
	private DefaultMutableTreeNode folderName = null;
	private DefaultTreeModel treeModel = null;
	

	public DownloadFile(DbxClient client, BufferedImage bufImg) {
		this.client = client;
		this.imgStream = bufImg;
	}



	public void actionPerformed(ActionEvent e) {
		generateFileManagerUI();
	}
	
	public void generateFileManagerUI() {

		JButton downLoadButton = null;
		JButton cancelButton = null;
		JPanel buttonpanel = null;
		JPanel panel = null;
		JScrollPane scrollPane;
		try {
			frame = new JFrame();
			downLoadButton = new JButton("Download");
			cancelButton = new JButton("Cancel");
			
			root = new DefaultMutableTreeNode( "Home Folder" );
			treeModel = new DefaultTreeModel(root);
			tree = new JTree(treeModel);
			panel = new JPanel();
			buttonpanel = new JPanel();
			panel.setLayout( new BorderLayout() );
			buttonpanel.setLayout(new BorderLayout());
			
			scrollPane = new JScrollPane();
			scrollPane.getViewport().add(tree);
			
			buttonpanel.add(downLoadButton, BorderLayout.WEST);
			buttonpanel.add(cancelButton, BorderLayout.CENTER);
			buttonpanel.setLayout(new FlowLayout());
			panel.add(scrollPane, BorderLayout.CENTER );
			panel.add(new JLabel("<html><body>Expand below to view files and folder</body></html>"), BorderLayout.NORTH);
			
			frame.setIconImage(imgStream);
			frame.getContentPane().add(panel);
			frame.getContentPane().add(buttonpanel, BorderLayout.SOUTH);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(400, 400);
			frame.setTitle("Select File to Download");
			frame.setBackground(Color.gray);
			
			DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
			for (DbxEntry child : listing.children) {
				if (child.isFolder()) {
					logger.info(child.name);
					root.add(folderName = new DefaultMutableTreeNode(child.name));
					DbxEntry.WithChildren listingFolderChilds = client.getMetadataWithChildren("/" +child.name);
					for (DbxEntry subChilds : listingFolderChilds.children) {
						logger.info("|->"+subChilds.name);
						folderName.add(new DefaultMutableTreeNode(subChilds.name));
					}
				} else {
					root.add(new DefaultMutableTreeNode(child.name));
				}
			}
			
			
			downLoadButton.addActionListener(new ActionListener() {
				boolean result = false;
				String jTreeVarSelectedPath = "";

				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode selectedElement 
					   =(DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
					logger.info(selectedElement.getUserObject()); 
					logger.info(tree.getSelectionPath().toString());
					Object[] paths = tree.getSelectionPath().getPath();
				    for (int i = 0; i<paths.length; i++) {
				        jTreeVarSelectedPath += paths[i] + "/";
				    }
					result = downLoadFile(jTreeVarSelectedPath);
					if (result) {
						JOptionPane.showMessageDialog(frame, "File downloaded successfully");
						jTreeVarSelectedPath = "";
					} else {
						JOptionPane.showMessageDialog(frame, "File could not be downloaded");
						jTreeVarSelectedPath = "";
					}
				}
			});
			
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
				}
			});
			
		} catch (DbxException e1) {
			e1.printStackTrace();
		}
	
	}
	
	public boolean downLoadFile(String fileName) {
		String replacedPath = null;
		String fileToDownload = null;
		JFileChooser saveLocationChooser = null;
		File saveLocationPath = null;
		DbxEntry.File result = null;
		FileOutputStream fileOutputStream = null;
		try {
			saveLocationChooser = new JFileChooser();
			saveLocationChooser.showSaveDialog(frame);
			saveLocationPath = saveLocationChooser.getSelectedFile();
			logger.info("file Path to download " + fileName);
			replacedPath = fileName.replaceAll("Home Folder", "");
			fileToDownload = replacedPath.substring(0, replacedPath.length() - 1);
			fileOutputStream = new FileOutputStream(saveLocationPath);
			logger.info("file to Download " + fileToDownload);
			result = client.getFile(fileToDownload, null, fileOutputStream);
			if (result.isFile()) {
				return true;
			} else {
				return false;
			}
		} catch (DbxException e) {
			e.printStackTrace();
			return false;
		} catch(IOException e1) {
			logger.info("Exception in downLoadFile() methon in DownloadFile class", e1);
			return false;
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
