package com.swampville.main;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JPanel;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
@SuppressWarnings("serial")
public class Meter extends JPanel {

	/**
	 * 
	 */
	
	final int maxValue = 500;
	
	int fillWidth;
	
	int value;
	
	int width, height,x ,y;
	
	/**
	 * @param name
	 */
	public Meter(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		fillWidth = width;
	}
	
	/**
	 * @param delta
	 */
	public void changeMeter(double amnt){
		
		//Change actual value
		if(value+amnt >= maxValue)
			value = maxValue;
		else if(value+amnt <=0)
			value = 0;
		else
			value+=amnt;
		
		//Change meter
		if(amnt>0)
			amnt = (int)Math.ceil((amnt/5));
		if(amnt < 0)
			amnt = (int)Math.floor((amnt/5)); 
		
		if(fillWidth+amnt <= 0){
			fillWidth=0;
		}else if(fillWidth+amnt >= width){
			fillWidth = width;
		}else{
			fillWidth+=amnt;
		}
	}
	
	public int getValue(){
		return value;
	}
	
	public void setFillWidth(int val){
		fillWidth = val;
		value = fillWidth*5;
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    
	    g.drawRect(x, y, width, height);
	    Paint redtowhite = new GradientPaint(x,y,Color.RED,x+width, 10,Color.GREEN);
	    Graphics2D g2 = (Graphics2D)g;
	    g2.setPaint(redtowhite);
	    g2.fillRect(x, y, fillWidth, height);
	  
	}
	
}
