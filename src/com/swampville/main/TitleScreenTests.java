//package com.swampville.main;
//
//import static org.junit.Assert.*;
//import org.junit.Test;
//import java.awt.Component;
//
///**
// * @author Mark Betters
//Jake Fiumara
//Will Taylor
//Foster Clark
//Briana Lamet
// *
// */
//public class TitleScreenTests {
//
//	TitleScreen testTitle = new TitleScreen();
//	
//	@Test
//	public void transitionToGameScreenTest() {
//		Component origComps[] = testTitle.frame.getComponents();
//		//testTitle.transitionToGameScreen("easy");
//		Component newComps[] = testTitle.frame.getComponents();
//		assertEquals(false, newComps.equals(origComps));
//	}
//	
//	@Test
//	public void initializeScreenTest() {
//		assertEquals(true, testTitle.btnPlay.isEnabled());
//		assertEquals(true, testTitle.btnHelp.isEnabled());
//		assertEquals(true, testTitle.btnLeaderboard.isEnabled());
//	}
//	
//	@Test
//	public void playBtnPressedTest(){
//		assertEquals(false, testTitle.btnPlay.isEnabled());
//		assertEquals(false, testTitle.btnHelp.isEnabled());
//		assertEquals(false, testTitle.btnLeaderboard.isEnabled());
//
//		assertEquals(true, testTitle.btnEasy.isEnabled());
//		assertEquals(true, testTitle.btnMedium.isEnabled());
//		assertEquals(true, testTitle.btnHard.isEnabled());
//	}
//	
//	@Test
//	public void instructionsBtnPressedTest() {
//		assertEquals(false, testTitle.btnPlay.isEnabled());
//		assertEquals(false, testTitle.btnHelp.isEnabled());
//		assertEquals(false, testTitle.btnHelp.isEnabled());
//	}
//	
//	@Test
//	public void leaderboardsBtnPressedTest() {
//		assertEquals(false, testTitle.btnPlay.isEnabled());
//		assertEquals(false, testTitle.btnHelp.isEnabled());
//		assertEquals(false, testTitle.btnLeaderboard.isEnabled());		
//	}	
//
//}
