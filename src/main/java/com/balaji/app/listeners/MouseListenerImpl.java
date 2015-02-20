package com.balaji.app.listeners;

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

public class MouseListenerImpl implements MouseListener {
	private static Logger logger = Logger.getLogger(MouseListenerImpl.class);
	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			Desktop.getDesktop().browse(new URI("https://github.com/balajivijayan/dropbox-util-app"));
		} catch (IOException | URISyntaxException e1) {
			logger.info("Exception in mousceclicked() method MouseListenerImpl class", e1);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}
