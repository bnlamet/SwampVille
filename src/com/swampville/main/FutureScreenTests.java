package com.swampville.main;

import static org.junit.Assert.*;

import java.awt.Component;

import javax.swing.JFrame;

import org.junit.Test;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class FutureScreenTests {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void transitionToLeaderboardScreenTest() {
		GameScreen mockGame = new GameScreen(new JFrame(), "easy");
		FutureScreen testFutureScreen = new FutureScreen(mockGame, new JFrame(), 211);
		
		Component origComps[] = testFutureScreen.frame.getComponents();
		testFutureScreen.transitionToLeaderboardScreen();
		Component newComps[] = testFutureScreen.frame.getComponents();
		assertEquals(false, newComps.equals(origComps));
	}

	@Test
	public void callGameScreensUpdateInOneSecManyTimesTest() {
		String difficulty = "easy";
		JFrame testFrame = new JFrame();
		GameScreen mockGame = new GameScreen(testFrame, difficulty);
		FutureScreen mockFutureScreen = new FutureScreen(mockGame, testFrame, 185);
		
		Building[] mockBuildings = new Building[3];
		
		Building house = Building.NATURERESERVE;
		Building office = Building.WINDFARM;
		Building researchLab = Building.RESEARCHLAB;
		
		mockBuildings[0] = house;
		mockBuildings[1] = office;
		mockBuildings[2] = researchLab;
		
		mockGame.setBuildingsToBuildingList(mockBuildings);

		Meter air = new Meter("Air");
		Meter land = new Meter("Land");
		Meter sea = new Meter("Sea");
		Meter animal = new Meter("Animal");
		Meter human = new Meter("Human");
		Meter money = new Meter("Money");
		
		air.changeBy(50);
		land.changeBy(30);
		sea.changeBy(40);
		animal.changeBy(30);
		human.changeBy(20);
		money.changeBy(15);
		
		Meter[] mockMeters = new Meter[6];
		mockMeters[0] = air;
		mockMeters[1] = land;
		mockMeters[2] = sea;
		mockMeters[3] = animal;
		mockMeters[4] = human;
		mockMeters[5] = money;
		
		mockGame.setMetersToMeterList(mockMeters);

		GameScreen mockGameCopy = new GameScreen(new JFrame(), difficulty);
		mockGameCopy.setBuildingsToBuildingList(mockBuildings);
		mockGameCopy.setMetersToMeterList(mockMeters);
		
		FutureScreen mockFutureScreen2 = new FutureScreen(mockGame, new JFrame(), 185);
		
		for (int i = 0; i < 1000; i++) {
			mockGame.updateInOneSec();
		}
		
		for (int i = 0; i < 6; i++) {
			assertEquals(false, mockFutureScreen2.getGameScreen().getMeters()[i].equals(
					mockGameCopy.getMeters()[i]));
			
			mockFutureScreen.callGameScreensUpdateInOneSecManyTimes();
			
			assertEquals(true, mockFutureScreen2.getGameScreen().getMeters()[i].equals(
					mockGameCopy.getMeters()[i]));
		}
		
	}

}