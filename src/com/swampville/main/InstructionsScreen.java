package com.swampville.main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

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
	 * 	populate content with instructions
	 */
	public void displayInstructions() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 1000, 700);
		
	    frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[grow][grow][]"));
		
		JPanel titlePanel = new JPanel();
		frame.getContentPane().add(titlePanel, "cell 0 0 3 1,grow");
		ImageIcon img = new ImageIcon("src/swampimages/swamplogo.png");
		titlePanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		JLabel title = new JLabel(img);
		title.setBounds(titlePanel.getBounds());
		titlePanel.add(title, "cell 0 0,grow");
		
		JTextPane txtpnYouOwnA = new JTextPane();
		txtpnYouOwnA.setFont(new Font("Tahoma", Font.PLAIN, 31));
		txtpnYouOwnA.setEditable(false);
		txtpnYouOwnA.setText("You own a plot of land next to an estuary! Your job is to build a succesfull city, but keep the enivornment healthy! You have a grid to build on, every building has benefits and drawbacks. The meters to the side will show you how the enviornment is doing, your population, and how much money you have. The higher the meters the higher your score! Good luck!");
		frame.getContentPane().add(txtpnYouOwnA, "cell 0 1,alignx center,aligny center");
		
		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				transitionToStartScreen();
			}});
		frame.getContentPane().add(btnNewButton, "cell 0 2,alignx center");
		
		frame.getContentPane().revalidate();
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
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		new TitleScreen(frame);
	}
	
}
