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
	
	WINDFARM (1,2,3),
	NATURERESERVE (4,5,6),
	RESEARCHLAB(7,8,9);
	//etc
	
	int meterEffect1;
	int meterEffect2;
	int meterEffect3;
	
	/**
	 * @param meterEffect1
	 * @param meterEffect2
	 * @param meterEffect3
	 */
	Building(int meterEffect1, int meterEffect2, int meterEffect3) {
		this.meterEffect1 = meterEffect1;
		this.meterEffect2 = meterEffect2;
		this.meterEffect3 = meterEffect3;
	}
	
}
