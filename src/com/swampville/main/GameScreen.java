package com.swampville.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class GameScreen implements ActionListener {

	Tile[][] grid;
	Meter[] meters;
	Building[] buildings;
	JButton stop;
	JButton hammer;
	JLabel timer;
	
	JPanel buildPopup;
	
	JPanel eventPopup;
	
	JPanel pausePopup;
	
	String difficulty;
	
	int frameCount = 0;
	
	int[] eventTimes;
	
	Event[] events;
	
	JFrame frame;
	
	/**
	 * @param frame
	 * @param difficulty
	 */
	public GameScreen(JFrame frame, String difficulty) {
		this.frame = frame;
		this.difficulty = difficulty;
		this.initializeRandomEvents();
		this.initializeGameScreen();
	}
	
	/**
	 * @return
	 */
	public Meter[] getMeters() {
		return this.meters;
	}
	
	/**
	 * 		setup Tile grid on frame
		put buttons and timer on frame
	 */
	public void initializeGameScreen() {

	}
	
	/**
	 * 		initializes events
		initialize eventTimes
	 */
	public void initializeRandomEvents() {

	}
	
	/**
	 * 		for each Tile in grid, 
		if Tile has a Building, 
		get Building's meterEffects,
		then update meters according to meterEffects.
	 */
	public void updateMetersInOneSec() {

	}
	
	/**
	 * 		for Tile in grid,
		if Tile has a Building,
		then repaint Building (animate)
	 */
	public void updateGridInOneSec() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton btnClicked = (JButton) e.getSource();
		
		if (btnClicked.getText() == "Stop") {
			this.pauseGame();
		}
		
		if (btnClicked.getText() == "Hammer") {
			this.triggerBuildPopup();
		}
		
	}
	
	/**
	 * 		init. pausePopup & put in foreground of frame
	 */
	public void pauseGame() {

	}
	
	/**
	 * 		put up buildPopup
		populate buildPopup with Building options based on difficulty String
		put in exitPopup & confirmBuild btns
		TODO: Make private class for buildPopup that has ActionListener 
		for the interactive view elements on it.
		Append Buildings selected to be built to the buildings property.
	 */
	
	/**
	 * triggers the build popup
	 */
	public void triggerBuildPopup() {
		
	}
	
	/**
	 * 		calls updateGridInOneSec() & updateMetersInOneSec()
		updates JLabel timer
		if timer @ any of the eventTimes
		if @ 0 seconds, then call transitionToFutureScreen()
	 */
	public void updateInOneSec() {

	}
	
	/**
	 * 		computes total score based on meters, 
		then passes that total score to FutureScreen
	 */
	public void transitionToFutureScreen() {

	}
	
	/**
	 * 		put up eventPopup
		update meters based on what the event is
	 */
	public void triggerEvent() {

	}
	
	/**
	 * 		called every frame
		if frameCount mod frameRate is 0, then call updateInOneSec()
	 */
	public void update() {

	}
	
	/**
	 * @param buildings
	 * This method is used for testing purposes only.
	 */
	public void setBuildingsToBuildingList(Building[] buildings) {
		this.buildings = buildings;
		
	}
	
	/**
	 * @param meters
	 */
	public void setMetersToMeterList(Meter[] meters) {
		this.meters = meters;
	}
}
