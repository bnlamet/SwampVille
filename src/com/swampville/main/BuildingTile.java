package com.swampville.main;

public class BuildingTile {

	String buildingType;
	int xPos, yPos;
	
	int numSecsTillGoodie;
	int numSecsTillGoodieFinal;
	
	boolean goodiePresent = false;
	
	public BuildingTile(String buildingType, int xPos, int yPos, int numSecsTillGoodieFinal) {
		this.buildingType = buildingType;
		this.xPos = xPos;
		this.yPos = yPos;
		this.numSecsTillGoodieFinal = numSecsTillGoodieFinal;
		this.numSecsTillGoodie = numSecsTillGoodieFinal;
	}
	
	@Override
	public String toString() {
		return buildingType + " at (" + this.xPos + ", " + this.yPos + "), goodie in " + this.numSecsTillGoodie;
	}
	
	public void updateInOneSec() {
		if (numSecsTillGoodie == 0) {
			goodiePresent = true;
			this.numSecsTillGoodie = this.numSecsTillGoodieFinal;
		} else {
			this.numSecsTillGoodie--;
		}
	}
	
	public int getNumSecsTillGoodie() {
		if (this.goodiePresent == false) {
			return this.numSecsTillGoodie;
		} else {
			return -1;
		}
	}
	
	public boolean getGoodiePresent() {
		return this.goodiePresent;
	}
	
	public String getBuildingType() {
		return this.buildingType;
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
}
