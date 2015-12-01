package com.swampville.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class Tile {

	int xPos;
	int yPos;
	JPanel pnl;
	ImageIcon img;
	
	String terrain;
	
	public boolean hasBuilding = false;
	
	static final int tileSideLength = 5;
	
	/**
	 * @param xPos
	 * @param yPos
	 * @param img
	 */
	public Tile(int xPos, int yPos, ImageIcon img) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.pnl = new JPanel();
		this.pnl.setBounds(xPos, yPos, Tile.tileSideLength, Tile.tileSideLength);
		this.img = img;
		JLabel imgLbl = new JLabel(img);
		this.pnl.add(imgLbl);
	}
	
	@Override
	public String toString() {
		return "(" + this.xPos + "," + this.yPos + ")";
	}
	
	public void setImg(String str) throws IOException {
		
		BufferedImage tileImg = ImageIO.read(new File(str));
		
		JLabel lbl = new JLabel(new ImageIcon(tileImg));
		
		this.pnl.add(lbl);
		
		System.out.println("setImg");
		
		this.pnl.revalidate();
		
		this.pnl.repaint();
	}
	
}
