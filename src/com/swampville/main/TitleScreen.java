package com.swampville.main;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class TitleScreen {

	JFrame frame;
	
	JButton btnPlay;
	JButton btnHelp;
	JButton btnLeaderboard;
	JRadioButton rdbtnDemoMode;
	
	boolean demoActive = true;

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
		
		frame.setBounds(screenSize.width / 5, screenSize.height / 4, 900, 472);
		frame.getContentPane().setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][][]"));
		
		frame.getContentPane().setBackground(new Color(68, 102, 0));
		
		//frame.getContentPane().setBackground(Color.BLACK);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(new Color(68, 102, 0));
		titlePanel.setOpaque(true);
		frame.getContentPane().add(titlePanel, "cell 0 0 3 1,grow");
		ImageIcon img = new ImageIcon("src/swampimages/swamplogo.png");
		titlePanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		JLabel title = new JLabel(img);
		title.setBackground(new Color(68, 102, 0));
		title.setOpaque(true);
		title.setBounds(titlePanel.getBounds());
		titlePanel.add(title, "cell 0 0,grow");
		
		btnPlay = new JButton("");
		try {
			BufferedImage playImg = ImageIO.read(new File("src/swampimages/Play_Image.png"));
			btnPlay.setIcon(new ImageIcon(playImg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		btnPlay.setBackground(new Color(102, 153, 0));
		btnPlay.setOpaque(true);
		btnPlay.setBorderPainted(false);
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
		frame.add(btnPlay, "cell 1 1, grow");
		
		btnHelp = new JButton("");
		try {
			BufferedImage helpImg = ImageIO.read(new File("src/swampimages/Help_Image.png"));
			btnHelp.setIcon(new ImageIcon(helpImg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		btnHelp.setBackground(new Color(102, 153, 0));
		btnHelp.setOpaque(true);
		btnHelp.setBorderPainted(false);
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.playSound("click.wav");
				instructionsBtnPressed();
			}
		});
		btnHelp.setBounds(163, 191, 117, 29);
		frame.add(btnHelp, "cell 1 1, grow");
		
		btnLeaderboard = new JButton("");
		try {
			BufferedImage leaderboardImg = ImageIO.read(new File("src/swampimages/Leaderboard_Image.png"));
			btnLeaderboard.setIcon(new ImageIcon(leaderboardImg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		btnLeaderboard.setBackground(new Color(102, 153, 0));
		btnLeaderboard.setOpaque(true);
		btnLeaderboard.setBorderPainted(false);
		btnLeaderboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.playSound("click.wav");
				leaderboardsBtnPressed();
			}
		});
		btnLeaderboard.setBounds(163, 250, 117, 29);
		frame.add(btnLeaderboard, "cell 1 1, grow");
		
		rdbtnDemoMode = new JRadioButton("Demo Mode");
		rdbtnDemoMode.setSelected(true);
		frame.getContentPane().add(rdbtnDemoMode, "cell 1 2, growx");
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
		
		if (rdbtnDemoMode.isSelected()){
			new DemoScreen(frame);
		}
		else {
			new GameScreen(frame, 0);
		}
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