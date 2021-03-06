package com.swampville.main;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextPane;

import net.miginfocom.swing.MigLayout;

/**
 * @author Mark Betters Jake Fiumara Will Taylor Foster Clark Briana Lamet
 *
 */
public class LeaderboardScreen implements ActionListener {

	JFrame frame;

	int[] currentTopScores = { 0, 0, 0 }; // scores from highest to lowest

	int totalScoreOfPlayer;

	JButton goToStartScreen;

	/**
	 * @param frame
	 * @param totalScoreOfPlayer
	 */
	public LeaderboardScreen(JFrame frame, int totalScoreOfPlayer) {
		this.frame = frame;
		// this.askForInitials();
		this.totalScoreOfPlayer = totalScoreOfPlayer;
		readCurrentTopScoresFromAppMemory();
		saveCurrentScores();
		this.displayScores();
	}

	public void displayScores() {
		int x = currentTopScores[0];
		int y = currentTopScores[1];
		int z = currentTopScores[2];
		

		DecimalFormat formatter = new DecimalFormat("###,###");

		// String scores = Arrays.toString(currentTopScores);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Rectangle screenSize = new Rectangle((new Point(0, 0)), tk.getScreenSize());

		frame.setBounds(screenSize.width / 5, screenSize.height / 4, 900, 500);

		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[grow][grow][grow][grow][]"));

		// Scores
		JTextPane topThree = new JTextPane();
		topThree.setFont(new Font("Tahoma", Font.PLAIN, 31));
		topThree.setEditable(false);
		topThree.setText("Top Three Scores");
		frame.getContentPane().add(topThree, "cell 0 0,alignx center, wrap");

		// Highest Score
		JTextPane scoreOne = new JTextPane();
		scoreOne.setFont(new Font("Tahoma", Font.PLAIN, 31));
		scoreOne.setEditable(false);
		scoreOne.setText(formatter.format(x).toString());
		frame.getContentPane().add(scoreOne, "cell 0 1,alignx center,wrap");
		scoreOne.setOpaque(false);

		// Second Highest Score
		JTextPane scoreTwo = new JTextPane();
		scoreTwo.setFont(new Font("Tahoma", Font.PLAIN, 31));
		scoreTwo.setEditable(false);
		scoreTwo.setText(formatter.format(y).toString());
		frame.getContentPane().add(scoreTwo, "cell 0 2,alignx center,wrap");
		scoreTwo.setOpaque(false);

		// Second Highest Score
		JTextPane scoreThree = new JTextPane();
		scoreThree.setFont(new Font("Tahoma", Font.PLAIN, 31));
		scoreThree.setEditable(false);
		scoreThree.setText(formatter.format(z).toString());
		frame.getContentPane().add(scoreThree, "cell 0 3,alignx center,wrap");
		scoreThree.setOpaque(false);

		// adding back button
		JButton btnNewButton = new JButton("Go to Start Screen");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.playSound("click.wav");
				transitionToTitleScreen();
			}
		});
		frame.getContentPane().add(btnNewButton, "cell 0 4,alignx center,wrap");
		frame.getContentPane().revalidate();
	}

	int[] updateScores() {
		// for(int i=0; i < currentTopScores.length; i++){
		if (totalScoreOfPlayer > currentTopScores[0]) {
			currentTopScores[2] = currentTopScores[1];
			currentTopScores[1] = currentTopScores[0];
			currentTopScores[0] = totalScoreOfPlayer;
		} else if (totalScoreOfPlayer > currentTopScores[1]) {
			currentTopScores[2] = currentTopScores[1];
			currentTopScores[1] = totalScoreOfPlayer;
		} else if (totalScoreOfPlayer > currentTopScores[2]) {
			currentTopScores[2] = totalScoreOfPlayer;
		}

		// }

		return (currentTopScores);
	}

	/**
	 * TODO: Use Serializable here.
	 */
	public void readCurrentTopScoresFromAppMemory() {
		int[] e = currentTopScores;

		try {
			File file = new File("src/scores/scores.ser");
			if (file.length() > 0) {
				FileInputStream fileIn = new FileInputStream("src/scores/scores.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				for (int x = 0; x < e.length; x++) {
					e[x] = in.readInt();
				}
				in.close();
				fileIn.close();
			}
		} catch (IOException i) {
			i.printStackTrace();
			return;
		}
		currentTopScores = e;
		updateScores();
	}

	public void saveCurrentScores() {
		int[] e = currentTopScores;
		try {
			FileOutputStream fileOut = new FileOutputStream("src/scores/scores.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			for (int i : e) {
				out.writeInt(i);
			}
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in /tmp/scores.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}

		//updateScores();

	}

	/**
	 * Get the serialized current top scores from the currentTopScores in app
	 * memory, then display these as a ranked list.
	 */
	public void displayCurrentTopScores() {

	}

	/**
	 * Displays 3 empty lines for player's initials. Puts keyboard on screen.
	 * When player is done entering initials, if totalScoreOfPlayer > everything
	 * in currentTopScores, then put totalScoreOfPlayer in currentTopScores in
	 * app memory. Make call to displayCurrentTopScores().
	 */
	public void askForInitials() {

	}

	/**
	 * pass frame to title screen
	 */
	public void transitionToTitleScreen() {
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		new TitleScreen(frame);

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