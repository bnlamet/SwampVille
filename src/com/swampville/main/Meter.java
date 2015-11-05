package com.swampville.main;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class Meter {

	int capacity;
	int points;
	String name;
	
	/**
	 * @param delta
	 */
	public void changeBy(int delta) {
		
	}
	
	/**
	 * @param name
	 */
	public Meter(String name) {
		this.capacity = 100;
		this.points = 0;
		this.name = name;
	}
	
	/**
	 * @return
	 */
	public int getPoints() {
		return this.points;
	}
	
}
