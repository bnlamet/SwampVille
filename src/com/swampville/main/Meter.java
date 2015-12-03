package com.swampville.main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class Meter extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int capacity;
	int points;
	String name;
	
	/**
	 * @param delta
	 */
	public void changeBy(int delta) {
		
	}
	
	@Override
	public void paint(Graphics g) {
		//Drawing Meters:
		//Make outer rect of meter
		//g.setColor(Color.WHITE);
		//g.drawRect(x, y, width, height);
		//Make inner rect of meter (size depending on how many points the meter has)
		//g.drawRect(x, y, width, height);
		//Set the graphics context's current color to one that reflects the meter's points.
		//g.setColor(new Color(this.points / 255));
		//Fill the inner rectangle with the new current color.
		//g.fillRect(x, y, width, height);
	}
	
	/**
	 * @param name
	 */
	public Meter(String name) {
		this.capacity = 100;
		this.points = 0;
		this.name = name;
		//this.setLocation(x, y); //Pass x and y positions in
	}
	
	/**
	 * @return
	 */
	public int getPoints() {
		return this.points;
	}
	
}
