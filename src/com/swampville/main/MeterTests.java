package com.swampville.main;

import static org.junit.Assert.*;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class MeterTests {

	public void testChangeBy() {
		Meter testMeter = new Meter("Test Meter Uno");
		testMeter.changeBy(5);
		Meter compareMeter = new Meter("Test Meter Dos");
		compareMeter.changeBy(2);
		testMeter.changeBy(-3);
	
		assertTrue(compareMeter.getPoints() == testMeter.getPoints());
	}
	
} 