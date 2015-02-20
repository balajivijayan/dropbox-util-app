package com.balaji.app.listeners;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JButton;
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
import com.dropbox.core.DbxWriteMode;

public class DeleteFile implements ActionListener {
	private static Logger logger = Logger.getLogger(DeleteFile.class);
	private static DbxClient client = null;
	private BufferedImage imgStream = null;
	private JTree tree = null;
	private JFrame frame = null;
	private DefaultMutableTreeNode root= null;
	private DefaultMutableTreeNode folderName = null;
	private DefaultTreeModel treeModel = null;
	
	public DeleteFile(DbxClient client, BufferedImage imgStream) {
		this.client = client;
		this.imgStream = imgStream;
	}


	public void actionPerformed(ActionEvent e) {
		JButton deleteButton = null;
		JButton cancelButton = null;
		JButton refreshButton = null;
		JPanel buttonpanel = null;
		JPanel panel = null;
		JScrollPane scrollPane =null;
		JLabel descLabel = null;
		try {
			frame = new JFrame();
			deleteButton = new JButton("Delete");
			cancelButton = new JButton("Cancel");
			refreshButton = new JButton("Refresh");
			
			root = new DefaultMutableTreeNode( "Home Folder" );
			treeModel = new DefaultTreeModel(root);
			tree = new JTree(treeModel);
			tree.setPreferredSize(new Dimension(20, 20));
			
			panel = new JPanel();
			buttonpanel = new JPanel();
			panel.setLayout( new BorderLayout() );
			panel.setSize(60, 60);
			buttonpanel.setLayout(new BorderLayout());
			
			scrollPane = new JScrollPane();
			scrollPane.getViewport().add(tree);
			
			descLabel = new JLabel("<html><body>Expand below to view files and folder</body></html>");
			buttonpanel.add(deleteButton, BorderLayout.WEST);
			buttonpanel.add(cancelButton, BorderLayout.CENTER);
			buttonpanel.add(refreshButton);
			buttonpanel.setLayout(new FlowLayout());
			panel.add(scrollPane, BorderLayout.CENTER);
			panel.add(descLabel, BorderLayout.NORTH);
			
			frame.setIconImage(imgStream);
			frame.getContentPane().add(panel);
			frame.getContentPane().add(buttonpanel, BorderLayout.SOUTH);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(400, 400);
			frame.setTitle("Expand Node::Select File to Delete");
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
			
			
			deleteButton.addActionListener(new ActionListener() {
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
					result = deleteFile(jTreeVarSelectedPath);
					if (result) {
						JOptionPane.showMessageDialog(frame, "File deleted successfully");
						jTreeVarSelectedPath = "";
					} else {
						JOptionPane.showMessageDialog(frame, "File could not be deleted");
						jTreeVarSelectedPath = "";
					}
				}
			});
			
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
				}
			});
			
			refreshButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					logger.info("Refresh button Pressed");
					root.removeAllChildren();
					treeModel.reload();
					//root.add(new DefaultMutableTreeNode("Another Child"));
					DbxEntry.WithChildren listing;
					try {
						listing = client.getMetadataWithChildren("/");
						for (DbxEntry child : listing.children) {
							logger.info(child.name);
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
					} catch (DbxException e1) {
						e1.printStackTrace();
					}
				}
			});

		} catch (DbxException e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean deleteFile(String fileName) {
		String replacedPath = null;
		String fileToDelete = null;

		try {
			logger.info("file Path to delete " + fileName);
			replacedPath = fileName.replaceAll("Home Folder", "");
			fileToDelete = replacedPath.substring(0, replacedPath.length() - 1);
			logger.info("file to delete " + fileToDelete);
			client.delete(fileToDelete);
		} catch (DbxException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	public static void deleteFile2(String filePath) {
		File inputFile = null;
		FileInputStream inputStream = null;
		try {
			inputFile = new File(filePath);
			logger.info("input Stream param to delete file " + filePath);
			inputStream = new FileInputStream(filePath);
			client.delete("/"+ inputFile.getParentFile().getName() + "/" + inputFile.getName());
/*			if (result.isFile()) {
				JOptionPane.showMessageDialog(frame, "File upload successful");
			} else {
				JOptionPane.showMessageDialog(frame, "File upload unsuccessful");
			}*/
		} catch (DbxException e) {
			logger.error("Error in deleteFile2() method DeleteFile class", e);
		} catch (FileNotFoundException e) {
			logger.error("Error in deleteFile2() method DeleteFile class", e);
		}
	}
}
