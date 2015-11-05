package com.swampville.main;

import java.awt.Image;
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
public class TitleScreen implements ActionListener {

	JFrame frame;
	
	JButton playBtn;
	JButton instructionsBtn;
	JButton leaderboardBtn;
	
	JButton easyDifficulty;
	JButton mediumDifficulty;
	JButton hardDifficulty;
	
	Image bckgdImage;
	
	/**
	 * @param frame
	 */
	public TitleScreen(JFrame frame) {
		this.frame = frame;
		this.initializeScreen();
	}
	
	/**
	 * 		put first 3 btns on screen and bckgd image
		and puts them in frame
	 */
	public void initializeScreen() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton btnClicked = (JButton) e.getSource();
		
		if (btnClicked.getText() == "Play") {
			this.playBtnPressed();
		}
		if (btnClicked.getText() == "Instructions") {
			this.instructionsBtnPressed();
		}
		if (btnClicked.getText() == "Leaderboards") {
			this.leaderboardsBtnPressed();
		}
		int len = btnClicked.getText().length();
		if (btnClicked.getText().substring(len - 10, len-1) == "Difficulty") {
			String difficulty = btnClicked.getText().substring(0, len - 10);
			transitionToGameScreen(difficulty);
		}
	}
	
	/**
	 * @param difficulty
	 * 		trans to GameScreen and set GameScreen's attr difficulty to the difficulty param passed in

	 */
	public void transitionToGameScreen(String difficulty) {
	}
	
	/**
	 * 		take other btns off
		put difficulty btns on screen
	 */
	public void playBtnPressed() {

	}
	
	/**
	 * 		transitions to InstructionScreen
		and pass frame to it
	 */
	public void instructionsBtnPressed() {

	}
	
	/**
	 * 		transitions to LeaderboardsScreen
		and pass frame to it
	 */
	public void leaderboardsBtnPressed() {

	}
	
	
}
