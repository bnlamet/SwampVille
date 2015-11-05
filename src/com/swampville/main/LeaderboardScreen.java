package com.swampville.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class LeaderboardScreen implements ActionListener {

	JFrame frame;
	
	int[] currentTopScores;
	
	int totalScoreOfPlayer;
	
	JButton goToStartScreen;
	
	/**
	 * @param frame
	 * @param totalScoreOfPlayer
	 */
	public LeaderboardScreen(JFrame frame, int totalScoreOfPlayer) {
		this.frame = frame;
		this.askForInitials();
		this.totalScoreOfPlayer = totalScoreOfPlayer;
	}
	
	/**
	 * 		TODO: Use Serializable here.
	 */
	public void readCurrentTopScoresFromAppMemory() {

	}
	
	/**
	 * 		Get the serialized current top scores from 
		the currentTopScores in app memory,
		then display these as a ranked list.
	 */
	public void displayCurrentTopScores() {

	}
	
	/**
	 * 		Displays 3 empty lines for player's initials.
		Puts keyboard on screen.
		When player is done entering initials,
		if totalScoreOfPlayer > everything in currentTopScores,
		then put totalScoreOfPlayer in currentTopScores in app memory.
		Make call to displayCurrentTopScores().
	 */
	public void askForInitials() {

	}
	
	/**
	 * 		pass frame to title screen
	 */
	public void transitionToTitleScreen() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton btnClicked = (JButton) e.getSource();
		
		if (btnClicked.getText() == "Go to Start Screen") {
			this.transitionToTitleScreen();
		}
	}
	
}
