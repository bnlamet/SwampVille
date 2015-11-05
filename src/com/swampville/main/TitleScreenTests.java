package com.swampville.main;

import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Component;
import javax.swing.JFrame;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class TitleScreenTests {

	TitleScreen testTitle = new TitleScreen(new JFrame());
	
	@Test
	public void transitionToGameScreenTest() {
		Component origComps[] = testTitle.frame.getComponents();
		testTitle.transitionToGameScreen("easy");
		Component newComps[] = testTitle.frame.getComponents();
		assertEquals(false, newComps.equals(origComps));
	}
	
	@Test
	public void initializeScreenTest() {
		assertEquals(true, testTitle.playBtn.isEnabled());
		assertEquals(true, testTitle.instructionsBtn.isEnabled());
		assertEquals(true, testTitle.leaderboardBtn.isEnabled());
	}
	
	@Test
	public void playBtnPressedTest(){
		assertEquals(false, testTitle.playBtn.isEnabled());
		assertEquals(false, testTitle.instructionsBtn.isEnabled());
		assertEquals(false, testTitle.leaderboardBtn.isEnabled());

		assertEquals(true, testTitle.easyDifficulty.isEnabled());
		assertEquals(true, testTitle.mediumDifficulty.isEnabled());
		assertEquals(true, testTitle.hardDifficulty.isEnabled());
	}
	
	@Test
	public void instructionsBtnPressedTest() {
		assertEquals(false, testTitle.playBtn.isEnabled());
		assertEquals(false, testTitle.instructionsBtn.isEnabled());
		assertEquals(false, testTitle.leaderboardBtn.isEnabled());
	}
	
	@Test
	public void leaderboardsBtnPressedTest() {
		assertEquals(false, testTitle.playBtn.isEnabled());
		assertEquals(false, testTitle.instructionsBtn.isEnabled());
		assertEquals(false, testTitle.leaderboardBtn.isEnabled());		
	}	

}
