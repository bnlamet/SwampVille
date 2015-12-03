package com.swampville.main;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import net.miginfocom.swing.MigLayout;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class FutureScreen{

	JFrame frame;
	
	JLabel content;
	
	int totalScoreOfPlayer;
	
	String futureText;
	
	JButton goToLeaderboardScreen;
	
	GameScreen gameScreen;
	
	/**
	 * @param gameScreen
	 * @param frame
	 * @param totalScoreOfPlayer
	 */
	public FutureScreen(JFrame frame, int totalScoreOfPlayer, String future) {
		this.frame = frame;
		futureText = future;
		this.totalScoreOfPlayer = totalScoreOfPlayer;
		
		initScreen();
		frame.revalidate();
	}
	
	private void initScreen(){
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Rectangle screenSize = new Rectangle((new Point(0, 0)), tk.getScreenSize());
		frame.setBounds(screenSize.width / 5, screenSize.height / 4, 900, 500);
		
		ImageIcon img;
		if(totalScoreOfPlayer == 0){
			img = new ImageIcon("src/swampimages/futurebad.png");
		}else{
			img = new ImageIcon("src/swampimages/futuregood.png");
		}
		
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[grow][grow][]"));
		
		JPanel titlePanel = new JPanel();
		frame.getContentPane().add(titlePanel, "cell 0 0 3 1,grow");
		titlePanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		JLabel title = new JLabel(img);
		title.setBounds(titlePanel.getBounds());
		titlePanel.add(title, "cell 0 0,grow");
		
		JTextPane future = new JTextPane();
		future.setOpaque(false);
		future.setFont(new Font("Tahoma", Font.PLAIN, 20));
		future.setEditable(false);
		future.setText(futureText);
		frame.getContentPane().add(future, "cell 0 1,alignx center,aligny center");
		
		JButton btnNewButton = new JButton("Continue");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.playSound("click.wav");
				transitionToLeaderboard(totalScoreOfPlayer);
			}});
		frame.getContentPane().add(btnNewButton, "cell 0 2,alignx center");
		
		frame.setSize(frame.getWidth(), frame.getHeight()+200);
		
		frame.getContentPane().revalidate();
	}
	
	/**
	 * 		transitions to LeaderboardScreen
		passes totalScoreOfPlayer to LeaderboardScreen
	 */
	public void transitionToLeaderboard(int score) {
		// computes total score based on meters,
		// then passes that total score to FutureScreen

		frame.getContentPane().removeAll();
		System.out.println(score);
		new LeaderboardScreen(frame, score);
		frame.getContentPane().revalidate();
	}
	
}
