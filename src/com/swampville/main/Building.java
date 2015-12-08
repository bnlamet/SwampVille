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
	OILREFINERY(0, 15, -5, 350, "Oil Refinery"),
	WINDFARM(0,1,4,50, "Windfarm"),
	SCHOOL(4,-1,0,100, "School"),
	HOUSE(2,1,-1,10, "House"),
	FARM(0,4,1,80, "Farm"),
	BOAT(1,4,0,80, "Boat");
	//etc
	
	int peopleEffect;
	int moneyEffect;
	int environmentEffect;
	
	int cost;
	
	String name;
	
	/**
	 * @param meterEffect1
	 * @param meterEffect2
	 * @param meterEffect3
	 */
	Building(int peopleEffect, int moneyEffect, int environmentEffect, int cost, String name) {
		this.peopleEffect = peopleEffect;
		this.moneyEffect = moneyEffect;
		this.environmentEffect = environmentEffect;
		this.cost = cost;
		this.name = name;
	}
	
	public int getCost(){
		return this.cost;
	}
	
	public String getEffects(){
		String str = "";
		if(peopleEffect != 0){
			if(peopleEffect > 0)
				str += "+" + peopleEffect + "ppl/sec  ";
			else
				str+= peopleEffect + "ppl/sec  ";
		}
		
		if(moneyEffect != 0){
			if(moneyEffect > 0)
				str += "+$" + moneyEffect + "/sec  ";
			else
				str+= "$"+moneyEffect + "/sec  ";
		}
		
		if(environmentEffect != 0){
			if(environmentEffect > 0)
				str += "+" + environmentEffect + "env/sec  ";
			else
				str+= environmentEffect + "env/sec  ";
		}
		
		return str;
			
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
