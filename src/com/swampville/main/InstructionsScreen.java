package com.swampville.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class InstructionsScreen implements ActionListener {

	JFrame frame;
	
	String instructions;
	JLabel content;
	
	JButton backButton;
	
	/**
	 * @param frame
	 */
	public InstructionsScreen(JFrame frame) {
		this.frame = frame;
		this.displayInstructions();
	}
	
	/**
	 * 		populate content with instructions
	 */
	public void displayInstructions() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton btnClicked = (JButton) e.getSource();
		if (btnClicked.getText() == "Back") {
				this.transitionToStartScreen();
		}
	}
	
	/**
	 * 		go to StartScreen
	 */
	public void transitionToStartScreen() {

	}
	
}
