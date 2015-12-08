//package com.swampville.main;
//
//import static org.junit.Assert.*;
//
//import java.awt.Component;
//
//import javax.swing.JFrame;
//
//import org.junit.Test;
//
///**
// * @author Mark Betters
//Jake Fiumara
//Will Taylor
//Foster Clark
//Briana Lamet
// *
// */
//public class LeaderboardScreenTests {
//
//	LeaderboardScreen testLeaderBoard = new LeaderboardScreen(new JFrame(), 58);
//
//	@Test
//	public void transitionToTitleScreenTest() {
//		Component origComps[] = testLeaderBoard.frame.getComponents();
//		testLeaderBoard.transitionToTitleScreen();
//		Component newComps[] = testLeaderBoard.frame.getComponents();
//		assertEquals(false, newComps.equals(origComps));
//	}
//
//	@Test
//	public void displayCurrentScoreTest() {
//		//Check to see that the ranked list
//		//of current top scores is on the frame.
//	}
//
//	@Test
//	public void askForInitialsTest() {
//		//Check to see that blank spaces appear on the frame.
//		//Check to see that Keyboard appears on the frame.
//	}
//	
//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
//
//}
