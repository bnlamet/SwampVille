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
//public class GameScreenTests {
//
//	JFrame testFrame = new JFrame();
//	GameScreen gameScreenTest = new GameScreen(testFrame, "easy");
//	
//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
//	
//	@Test
//	public void transitionToFutureScreenTest() {
//		Component origComps[] = gameScreenTest.frame.getComponents();
//		gameScreenTest.transitionToFutureScreen();
//		Component newComps[] = gameScreenTest.frame.getComponents();
//		assertEquals(false, newComps.equals(origComps));
//	}
//	
//	public void initializeGameScreenTest() {
//		this.gameScreenTest.initializeGameScreen();
//		//then, check that gameScreenTest frame has needed view elements
//	}
//	
//	public void initializeRandomEventsTest() {
//		this.gameScreenTest.initializeRandomEvents();
//		assertEquals(true, this.gameScreenTest.events.length != 0);
//	}
//	
//	public void updateInOneSecTest() {
//		Building[] buildings = new Building[2];
//		buildings[0] = Building.HOUSE;
//		buildings[1] = Building.OILREFINERY;
//		//this.gameScreenTest.setBuildingsToBuildingList(buildings);
//		Meter[] meters = new Meter[3];
//		//gameScreenTest.setMetersToMeterList(meters);
//		this.gameScreenTest.updateInOneSec();
//		//The numbers below need to change, since the building stats changed
//		//assertEquals(true, this.gameScreenTest.getMeters()[0].points == 5);
//		//assertEquals(true, this.gameScreenTest.getMeters()[1].points == 7);
//		//assertEquals(true, this.gameScreenTest.getMeters()[2].points == 9);
//	}
//	
//	public void triggerEventTest() {
//		this.gameScreenTest.triggerEvent();
//		//then, check that gameScreenTest frame has the event popup
//	}
//	
//	public void triggerBuildPopupTest() {
//		this.gameScreenTest.triggerBuildPopup();
//		//then, check that gameScreenTest frame has the build popup
//	}
//
//}
