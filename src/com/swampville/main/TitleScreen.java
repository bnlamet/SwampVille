package com.swampville.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class TitleScreen {

	JFrame frame;
	
	JButton btnPlay;
	JButton btnHelp;
	JButton btnLeaderboard;

	Sound s = new Sound();
	
	/**
	 * The title screen is the first thing seen by the user. 
	 * It displays the image of our title, and creates three buttons 
	 * when initializeScreen() is called. 
	 * @param frame
	 * This frame will be used throughout the application lifecycle.
	 */
	public TitleScreen() {
		this.frame = new JFrame();
		this.initializeScreen();
	}	
	
	public TitleScreen(JFrame frame) {
		this.frame =  frame;
		this.initializeScreen();
		frame.revalidate();
	}
	
	/**
	 * Invoked by the TitleScreen constructor.
	 * This puts our title image on the frame, and creates three buttons - 
	 * play, instructions, and leaderboard - which allow the user
	 * to maneuver through the application. 
	 */
	public void initializeScreen() {
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Rectangle screenSize = new Rectangle((new Point(0,0)), tk.getScreenSize());
		
		frame.setBounds(screenSize.width / 5, screenSize.height / 4, 900, 500);
		frame.getContentPane().setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][][][]"));
		
		//frame.getContentPane().setBackground(Color.BLACK);
		
		JPanel titlePanel = new JPanel();
		frame.getContentPane().add(titlePanel, "cell 0 0 3 1,grow");
		ImageIcon img = new ImageIcon("src/swampimages/swamplogo.png");
		titlePanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		JLabel title = new JLabel(img);
		title.setBounds(titlePanel.getBounds());
		titlePanel.add(title, "cell 0 0,grow");
		
		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.playSound("click.wav");
				try {
					playBtnPressed();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPlay.setBounds(163, 150, 117, 29);
		frame.add(btnPlay, "cell 1 1,growx");
		
		btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.playSound("click.wav");
				instructionsBtnPressed();
			}
		});
		btnHelp.setBounds(163, 191, 117, 29);
		frame.add(btnHelp, "cell 1 2,growx");
		
		btnLeaderboard = new JButton("Leaderboard");
		btnLeaderboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.playSound("click.wav");
				leaderboardsBtnPressed();
			}
		});
		btnLeaderboard.setBounds(163, 250, 117, 29);
		frame.add(btnLeaderboard, "cell 1 3,growx");
	}
	
	/**
	 * @param difficulty
	 * Removes titlePanel from frame, 
	 * then creates a new GameScreen controller,
	 * passing the frame and the difficulty param to it.
	 * @throws IOException 
	 */
	public void transitionToGameScreen(String difficulty) throws IOException {
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		new GameScreen(frame, difficulty);
	}
	
	/**
	 * When the play button is pressed, this method is invoked 
	 * in order to display difficulty buttons to the user.
	 * @throws IOException 
	 */
	public void playBtnPressed() throws IOException {
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		this.transitionToGameScreen("Easy");
	}
	
	/**
	 * When the instructions button is pressed, this method is invoked 
	 * in order to transition to InstructionScreen.
	 */
	public void instructionsBtnPressed() {
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		new InstructionsScreen(frame);
		
//		Weird implementation, just for kicks
//		JTextPane txtpnInstructions = new JTextPane();
//		txtpnInstructions.setText("Instructions");
//		txtpnInstructions.setBounds(166, 131, 117, 16);
//		frame.getContentPane().add(txtpnInstructions);
			
	}
	/**
	 * When the play button is pressed, this method is invoked 
	 * in order to transition to LeaderboardScreen. 
	 */
	public void leaderboardsBtnPressed() {
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		new LeaderboardScreen(frame, -1);
		
//		Use negative integer in order to test and
//		limit the case of going to leaderboard without playing
		
	}
	
}