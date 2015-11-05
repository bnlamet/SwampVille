package com.swampville.main;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public enum Event {

	TORNADO(1,2,3),
	EXPLOSION(4,5,6);
	//etc.
	
	int meterEffect1;
	int meterEffect2;
	int meterEffect3;
	
	/**
	 * @param meterEffect1
	 * @param meterEffect2
	 * @param meterEffect3
	 */
	Event(int meterEffect1, int meterEffect2, int meterEffect3) {
		this.meterEffect1 = meterEffect1;
		this.meterEffect2 = meterEffect2;
		this.meterEffect3 = meterEffect3;
	}
	
}
