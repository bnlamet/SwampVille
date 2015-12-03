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
public class FutureScreen implements ActionListener {

	JFrame frame;
	
	JLabel content;
	
	int totalScoreOfPlayer;
	
	JButton goToLeaderboardScreen;
	
	GameScreen gameScreen;
	
	/**
	 * @param gameScreen
	 * @param frame
	 * @param totalScoreOfPlayer
	 */
	public FutureScreen(GameScreen gameScreen, JFrame frame, int totalScoreOfPlayer) {
		this.gameScreen = gameScreen;
		this.frame = frame;
		this.totalScoreOfPlayer = totalScoreOfPlayer;
		this.callGameScreensUpdateInOneSecManyTimes();
	}
	
	/**
	 * @return
	 */
	public GameScreen getGameScreen() {
		return this.gameScreen;
	}
	
	/**
	 *
	 * 	computes what the future meters will be
		after many calls to gameScreen's method updateInOneSec(),
		wherein a relevant String of text is put in the content JLabel,
		according to good and bad things in the various meters
	 */
	public void callGameScreensUpdateInOneSecManyTimes() {

	}
	
	/**
	 * 		transitions to LeaderboardScreen
		passes totalScoreOfPlayer to LeaderboardScreen
	 */
	public void transitionToLeaderboardScreen() {

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton btnClicked = (JButton) e.getSource();
		
		if (btnClicked.getText() == "Go to Leaderboards") {
			this.transitionToLeaderboardScreen();
		}
	}
	
}
