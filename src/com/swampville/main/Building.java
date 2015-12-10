package com.swampville.main;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public enum Building {
	
	//For now, the meters are just ppl, $, env
	
	//NOTE: The name properties of the different cases 
	//should be set to the respective image asset file names.
	//Meter effects are the numbers and the 4th num is cost
	OILREFINERY(0, 20, -2, 100, "Oil Refinery"),
	WINDFARM(0,1,1,20, "Windfarm"),
	SCHOOL(5,3,0,150, "School"),
	HOUSE(2,0,-1,10, "House"),
	FARM(0,4,4,80, "Farm"),
	BOAT(0,4,4,5, "Boat");
	//etc
	
	static final int oilRefineryNumSecsTillGoodieFinal = 25;
	static final int windFarmNumSecsTillGoodieFinal = 15;
	static final int schoolNumSecsTillGoodieFinal = 10;
	static final int houseNumSecsTillGoodieFinal = 5;
	static final int farmNumSecsTillGoodieFinal = 5;
	static final int boatNumSecsTillGoodieFinal = 7;
	
	int peopleEffect;
	int moneyEffect;
	int enviornmentEffect;
	
	int cost;
	
	String name;
	
	/**
	 * @param meterEffect1
	 * @param meterEffect2
	 * @param meterEffect3
	 */
	Building(int peopleEffect, int moneyEffect, int enviornmentEffect, int cost, String name) {
		this.peopleEffect = peopleEffect;
		this.moneyEffect = moneyEffect;
		this.enviornmentEffect = enviornmentEffect;
		this.cost = cost;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
