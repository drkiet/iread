package com.drkiet.ireader;

import java.io.IOException;

import javax.swing.SwingUtilities;

import com.drkiet.ireader.main.MainFrame;

/**
 * Intelligent Reader Program
 *
 */
public class IntelReader {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame mainFrame = new MainFrame();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
