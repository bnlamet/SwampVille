package com.swampville.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;
import timer.TimeFrame;

public class GameScreen implements Runnable {

	Tile[][] grid;
	JPanel gridPanel;
	
	Tile currentlySelectedTile;
	
	boolean tileHighlighted = false;
	
	String currentlySelectedBuilding, difficulty;
	boolean timeToBuild = false;
	int gridWidth;
	int gridHeight;
	Meter[] meters;
	Collection<Building> buildings;
	JButton stop, hammer, destroy;
	JLabel timer;
	JFrame frame, buildPopup;
	JPanel gamePanel, eventPopup, pausePopup;
	int frameCount = 0;
	int[] eventTimes;
	Event[] events;
	int mouseXCorr, mouseYCorr;
	int previousMouseXCorr, previousMouseYCorr;
	TimeFrame t;
	
	JLabel environmentLbl;
	JLabel pplLbl;
	JLabel moneyLbl;
	boolean gamePaused = false;
	int boatCount; //to prevent overfishing
	int environmentLblDelta;
	int pplLblDelta;
	int moneyLblDelta;
	int[][] buildingCodes = {{0,0,0,0,0,2,2,1,1},{0,0,0,0,2,2,1,1,1},{0,0,0,0,2,1,1,1,1},{0,0,0,0,2,2,2,1,1},{2,0,0,2,2,2,2,2,1},{2,2,2,2,2,0,0,2,1},{2,0,0,2,0,0,2,2,1},{0,0,2,2,0,0,0,2,1},{0,2,2,2,0,0,0,2,1},{2,2,2,0,0,0,0,2,1},{2,2,2,0,0,0,0,2,1},{0,2,2,0,0,2,2,2,1},{2,2,2,2,2,2,2,1,1},{0,0,0,2,2,1,1,1,1}};
	
	MigLayout gameLayout = new MigLayout("fill, insets 0", "[max][max]", "[max][max][max][max][max][max]");
	
	/**
	 * @param frame
	 * @param difficulty
	 * @throws IOException
	 */
	public GameScreen(JFrame frame, String difficulty) throws IOException {
		
		this.difficulty = difficulty;
		this.frame = frame;
		this.initializeRandomEvents();
		this.initializeGameScreen();
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
	}
	
	@Override
	public void run() {
		
		//Put code to repaint meters in here, if necessary.
		
		try {
			this.updateInOneSec();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 */
	public Meter[] getMeters() {
		return this.meters;
	}
	
	public void setBuildingsToBuildingList(Collection<Building> buildings) {
		this.buildings = buildings;
	}
	
	public void setMetersToMeterList(Meter[] meters) {
		this.meters = meters;
	}
	
	/**
	 *Setup Tile grid on frame.
	 *Put buttons and timer on frame.
	 *
	 *Adjusted so that the frame is resized to fit the screen, 
	 *then the components are added in a more orderly fashion:
	 *first the grid, followed by the three meters, and finally
	 *the famous buttons.
	 */
	public void initializeGameScreen() {
		
		// Setting the frame's layout before adding everything makes it so that 
		// we are not dealing with the title screen's MigLayout anymore, which
		// stops everything from shifting. I think. Regardless, it works as of now.
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Rectangle screenSize = new Rectangle((new Point(0,0)), tk.getScreenSize());
		frame.setBounds(screenSize.width / 5, screenSize.height / 4, 900, 480);
		frame.getContentPane().setLayout(gameLayout);
		
		this.initializeGrid();
		this.initializeMeters();
		
		this.drawGridlines();
		
		for (Component comp: this.frame.getContentPane().getComponents()) {
			System.out.println(comp);
		}
		
		////////////////ADDING STOP TO GAME FRAME/////////////////////////
		
		stop = new JButton("Pause");
		
		try {
			BufferedImage stopImg = ImageIO.read(new File("src/swampimages/stop.png"));
			stop.setIcon(new ImageIcon(stopImg));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		stop.addActionListener(new Pauses());
		frame.getContentPane().add(stop, "cell 1 3,grow");

		////////////////ADDING HAMMER TO GAME FRAME/////////////////////////
		
		hammer = new JButton("Build");
		
		
		try {
			BufferedImage hammerImg = ImageIO.read(new File("src/swampimages/hammer.png"));
			hammer.setIcon(new ImageIcon(hammerImg));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		hammer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				triggerBuildPopup();
				hammer.setEnabled(false);
			}
		});
		frame.getContentPane().add(hammer, "cell 1 4,grow");
		
		////////////////ADDING TIME TO GAME FRAME/////////////////////////
		
		t = new TimeFrame();
        frame.getContentPane().add(t, "cell 1 5,grow");
		
		/////////////////////////////////////////////////////////////
		
        // Create pop-up for the building list for later use
		buildPopup = new JFrame();
		createBuildPanel();
	}
	
	public void initializeMeters() {
		
		//Create Env. meter
		JPanel environment = new JPanel();
		environmentLbl = new JLabel("0");
		ImageIcon environmentImg = new ImageIcon("src/swampimages/environment.png");
		environmentLbl.setIcon(environmentImg);
		environment.add(environmentLbl);
		
		//Create People meter
		JPanel people = new JPanel();
		pplLbl = new JLabel("0");
		ImageIcon peopleImg = new ImageIcon("src/swampimages/people.png");
		pplLbl.setIcon(peopleImg);
		people.add(pplLbl);
		
		//Create Money Meter
		JPanel money = new JPanel();
		moneyLbl = new JLabel("0");
		ImageIcon moneyImg = new ImageIcon("src/swampimages/money.png");
		moneyLbl.setIcon(moneyImg);
		money.add(moneyLbl);
		
		//Add to frame
		this.frame.getContentPane().add(environment, "cell 1 0,grow");
		this.frame.getContentPane().add(people, "cell 1 1,grow");
		this.frame.getContentPane().add(money, "cell 1 2,grow");
		
//		this.frame.getContentPane().add(new JButton("test"), "cell 1 0 3 1,grow");
//		JLabel testLabel = new JLabel("testing");
//		JPanel testPanel = new JPanel();
//		testPanel.add(testLabel);
//		this.frame.getContentPane().add(testPanel, "cell 1 1 3 1,grow");
//		this.frame.getContentPane().add(new JButton("test3"), "cell 1 2 3 1,grow");
	}
	
	public int[] gridCorr(int mouseX, int mouseY){
		int gridX = mouseX/50;
		int gridY = mouseY/50;
		int[] coordinates = {gridX*50,gridY*50};
		return coordinates;
	}
	
	public void initializeGrid() {
		this.gridPanel = new JPanel();
		
		//Set up the grid image, put it on a JLabel
		//Add JLabel to subpanel containing grid
		BufferedImage gridImg;
		try {
			gridImg = ImageIO.read(new File("src/swampimages/easyswamp.png"));
			JLabel gridLabel = new JLabel();
			gridLabel.setIcon(new ImageIcon(gridImg));
			gridPanel.setLayout(new MigLayout("fill, insets 0","",""));
			gridPanel.add(gridLabel,"aligny top");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		//Add gridPanel to the game frame, in first panel
		this.frame.getContentPane().add(gridPanel, "cell 0 0 1 6,grow");
		
		//Add mouseListener to track the coordinates of the mouse clicks
		this.gridPanel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				previousMouseXCorr = mouseXCorr;
				previousMouseYCorr = mouseYCorr;
				mouseXCorr = gridCorr(e.getX(), e.getY())[0];
				mouseYCorr = gridCorr(e.getX(), e.getY())[1];
				
				if (buildingCodes[mouseXCorr/50][mouseYCorr/50] == 2){
					JOptionPane.showMessageDialog(frame,"You can't build here. Select a new tile.",
							"ERROR", JOptionPane.WARNING_MESSAGE);
					mouseXCorr = previousMouseXCorr;
					mouseYCorr = previousMouseYCorr;
					return;
				} else {
					highlightTileLogic(mouseXCorr, mouseYCorr);
				}
				//For Testing Purposes:
				System.out.println(mouseXCorr + ", " + mouseYCorr);
			}
		});
	}
	
	/**
	 *Initialize list of events.
	 *Initialize eventTimes.
	 */
	public void initializeRandomEvents() {
		
	}
	
	/**
	 *For each Tile in the grid, 
	 *if the Tile has a Building on it, 
	 *get that Building's meterEffects,
	 *then update the meters according 
	 *to those meterEffects.
	 */
	public void updateMetersInOneSec() {
		
	}
	
	/**
	 * Initialize pausePopup & put in foreground
	 */
	public void pauseGame() {
		
		
		
	}
	
	/**
	 * Put up buildPopup.
	 * Populate buildPopup with Building options
	 * based on difficulty String.
	 * Put in exitPopup & confirmBuild buttons.
	 */
	public void triggerBuildPopup() {
		
		buildPopup.setVisible(true);
		buildPopup.requestFocus();
		//frame.getContentPane().repaint();
	}
	
	/**
	 * Calls updateMetersInOneSec().
	 * Updates JLabel timer.
	 * If timer at any of the eventTimes,
	 * then call triggerEvent() on respective event.
	 * If timer at 0 seconds, 
	 * then call transitionToFutureScreen().
	 * 
	 * The if loop with t.getTime() decides when the 
	 * game ends.
	 * *can be t.getMintues() or t.getSeconds()
	 * 
	 * @throws IOException 
	 */
	public void updateInOneSec() throws IOException {
		//System.out.println("updateInOneSec");
		if(gamePaused == false){
			updateMeters();
			makinMagic(); //build stuff
		}
		////////////////ADJUST END TIME/////////////////////////
		
		if (t.getMinutes() == 3) {
			transitionToFutureScreen();
		}
		
	}
	
	public void updateMeters(){
		this.pplLbl.setText(Integer.toString(Integer.valueOf(this.pplLbl.getText()) + this.pplLblDelta));
		this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) + this.moneyLblDelta));
		this.environmentLbl.setText(Integer.toString(Integer.valueOf(this.environmentLbl.getText()) + this.environmentLblDelta));
	}
	
	public void makinMagic() throws IOException{
		if (this.timeToBuild == true) {
			int xCode = mouseXCorr/50;
			int yCode = mouseYCorr/50;
			System.out.print(xCode + "," + yCode + "\n");
			int buildCode = buildingCodes[xCode][yCode];
			System.out.print("Building code: " + buildCode + "\n"); //print the buildcode of the selected tile
			
			if(buildCode == 1){ //water
				if(this.currentlySelectedBuilding.equals("Boat")){ //if boat
					if(boatCount >= 5){
						JOptionPane.showMessageDialog(frame,"WARNING! Building this boat will cause over-fishing."
								+ " \n Over-fishing negatively impacts the environment (-10 env/sec).",
								"DANGER! DANGER!", JOptionPane.ERROR_MESSAGE);
						boatCount += 1;
						this.gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/Boat.png")), mouseXCorr, mouseYCorr, this.gridPanel);
						this.addBuilding(this.currentlySelectedBuilding);
						buildingCodes[xCode][yCode] = 4; //set code to occupied by water building
						
						//change the environment effects of boats
						//over fishing is happening so make the env meter get wrecked
						this.environmentLblDelta-=10;
						
						this.timeToBuild = false;
					}
					
					else{
						boatCount += 1;
						this.gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/Boat.png")), mouseXCorr, mouseYCorr, this.gridPanel);
						this.addBuilding(this.currentlySelectedBuilding);
						buildingCodes[xCode][yCode] = 4; //set code to occupied by water building
						this.timeToBuild = false;
					}
				}
				else{ //if not a boat
					JOptionPane.showMessageDialog(frame,"You can't build a building on water. Try again.",
							"ERROR", JOptionPane.WARNING_MESSAGE);
					this.timeToBuild = false;
				}
			}
			if(buildCode == 0){ //land
				if(this.currentlySelectedBuilding.equals("Boat")){
					JOptionPane.showMessageDialog(frame,"You can't build a boat on land. Try again.",
							"ERROR", JOptionPane.WARNING_MESSAGE);
					this.timeToBuild = false;
				}
				else{
					this.gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/" + this.currentlySelectedBuilding + ".png")), this.mouseXCorr, this.mouseYCorr, this.gridPanel);
					this.addBuilding(this.currentlySelectedBuilding);
					buildingCodes[xCode][yCode] = 3; //set code to occupied by land building
					this.timeToBuild = false;
				}
			}
			if(buildCode == 2){ //not buildable tile
				JOptionPane.showMessageDialog(frame,"You can't build here. Select a new tile.",
						"ERROR", JOptionPane.WARNING_MESSAGE);
				this.timeToBuild = false;
			}
			if((buildCode == 3)||(buildCode == 4)){ //occupied
				JOptionPane.showMessageDialog(frame,"This tile is full. Select a new tile.",
						"ERROR", JOptionPane.WARNING_MESSAGE);
				this.timeToBuild = false;
			}
		}
	}
	
	public void addBuilding(String buildingStr) {
		
		int newEnvironmentDelta = 0;
		int newPeopleDelta = 0;
		int newMoneyDelta = 0;
		
		if (buildingStr.equals("Boat")) {
			newEnvironmentDelta = Building.BOAT.meterEffect1;
			newPeopleDelta = Building.BOAT.meterEffect2;
			newMoneyDelta = Building.BOAT.meterEffect3;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.BOAT.cost));
		}
		if (buildingStr.equals("Farm")) {
			newEnvironmentDelta = Building.FARM.meterEffect1;
			newPeopleDelta = Building.FARM.meterEffect2;
			newMoneyDelta = Building.FARM.meterEffect3;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.FARM.cost));
		}
		if (buildingStr.equals("House")) {
			newEnvironmentDelta = Building.HOUSE.meterEffect1;
			newPeopleDelta = Building.HOUSE.meterEffect2;
			newMoneyDelta = Building.HOUSE.meterEffect3;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.HOUSE.cost));
		}
		if (buildingStr.equals("Oil Refinery")) {
			newEnvironmentDelta = Building.OILREFINERY.meterEffect1;
			newPeopleDelta = Building.OILREFINERY.meterEffect2;
			newMoneyDelta = Building.OILREFINERY.meterEffect3;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.OILREFINERY.cost));
		}
		if (buildingStr.equals("School")) {
			newEnvironmentDelta = Building.SCHOOL.meterEffect1;
			newPeopleDelta = Building.SCHOOL.meterEffect2;
			newMoneyDelta = Building.SCHOOL.meterEffect3;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.SCHOOL.cost));
		}
		if (buildingStr.equals("Windfarm")) {
			newEnvironmentDelta = Building.WINDFARM.meterEffect1;
			newPeopleDelta = Building.WINDFARM.meterEffect2;
			newMoneyDelta = Building.WINDFARM.meterEffect3;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.WINDFARM.cost));
		}
		
		this.environmentLblDelta = this.environmentLblDelta + newEnvironmentDelta;
		this.pplLblDelta = this.pplLblDelta + newPeopleDelta;
		this.moneyLblDelta = this.moneyLblDelta + newMoneyDelta;
	}
	
	public void transitionToFutureScreen() {
		//computes total score based on meters, 
		//then passes that total score to FutureScreen
		
		frame.getContentPane().removeAll();
		JLabel future = new JLabel("FUTUREEEEEE");
		frame.getContentPane().add(future);
		frame.getContentPane().revalidate();
	}
	
	public void triggerEvent() {
		//put up eventPopup
		//update meters based on what the event is
	}
	
	private void createBuildPanel() {
		//frame.getContentPane().add(buildPopup, "cell 2 1,grow");
		
		ButtonGroup buildBtns = new ButtonGroup();
		
		//buildPopup.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		Rectangle frameRect = this.frame.getContentPane().getBounds();
		buildPopup.setBounds(frameRect.x / 4, frameRect.y / 4, frameRect.width / 2, frameRect.height / 2);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{16, 129, 63, 0, 0};
		gridBagLayout.rowHeights = new int[]{25, 25, 25, 25, 25, 25, 25, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		buildPopup.setLayout(gridBagLayout);
		
		/*JLabel lblPic = new JLabel("What is this???");
		GridBagConstraints gbc_lblPic = new GridBagConstraints();
		gbc_lblPic.anchor = GridBagConstraints.NORTH;
		gbc_lblPic.insets = new Insets(0, 0, 5, 5);
		gbc_lblPic.gridx = 0;
		gbc_lblPic.gridy = 0;
		buildPopup.add(lblPic, gbc_lblPic);*/
		/*
		JLabel lbltitleDesc = new JLabel("Costs");
		GridBagConstraints gbc_lbltitleDesc = new GridBagConstraints();
		gbc_lbltitleDesc.insets = new Insets(0, 0, 5, 5);
		gbc_lbltitleDesc.gridx = 2;
		gbc_lbltitleDesc.gridy = 0;
		buildPopup.add(lbltitleDesc, gbc_lbltitleDesc);
		
		JLabel gbc_lbltitleMeterEffect = new JLabel("Meter Effects");
		GridBagConstraints gbc_lbltitleMeterEffect = new GridBagConstraints();
		gbc_lbltitleMeterEffect.insets = new Insets(0, 0, 5, 0);
		gbc_lbltitleMeterEffect.gridx = 3;
		gbc_lbltitleMeterEffect.gridy = 0;
		buildPopup.add(lbltitleMeterEffect, gbc_lbltitleMeterEffect);
		*/
		
		JRadioButton rdbtnOilRefinery = new JRadioButton("Oil Refinery");
		GridBagConstraints gbc_rdbtnOilRefinery = new GridBagConstraints();
		gbc_rdbtnOilRefinery.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnOilRefinery.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnOilRefinery.gridx = 1;
		gbc_rdbtnOilRefinery.gridy = 0;
		buildPopup.add(rdbtnOilRefinery, gbc_rdbtnOilRefinery);
		buildBtns.add(rdbtnOilRefinery);
		
		JLabel lblOilDesc = new JLabel("Costs $100");
		GridBagConstraints gbc_lblOilDesc = new GridBagConstraints();
		gbc_lblOilDesc.insets = new Insets(0, 0, 5, 5);
		gbc_lblOilDesc.gridx = 2;
		gbc_lblOilDesc.gridy = 0;
		buildPopup.add(lblOilDesc, gbc_lblOilDesc);
		
		JLabel lblOilMeterEffect = new JLabel("-2 env/sec, +$20/sec");
		GridBagConstraints gbc_lblOilMeterEffect = new GridBagConstraints();
		gbc_lblOilMeterEffect.insets = new Insets(0, 0, 5, 0);
		gbc_lblOilMeterEffect.gridx = 3;
		gbc_lblOilMeterEffect.gridy = 0;
		buildPopup.add(lblOilMeterEffect, gbc_lblOilMeterEffect);
		
		JRadioButton rdbtnWindTurbine = new JRadioButton("Windfarm");
		GridBagConstraints gbc_rdbtnWindTurbine = new GridBagConstraints();
		gbc_rdbtnWindTurbine.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnWindTurbine.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnWindTurbine.gridx = 1;
		gbc_rdbtnWindTurbine.gridy = 1;
		buildPopup.add(rdbtnWindTurbine, gbc_rdbtnWindTurbine);
		buildBtns.add(rdbtnWindTurbine);

		JLabel label = new JLabel("Costs $20");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 1;
		buildPopup.add(label, gbc_label);
		
		JLabel label_1 = new JLabel("+1 env/sec, +$1/sec,");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 0);
		gbc_label_1.gridx = 3;
		gbc_label_1.gridy = 1;
		buildPopup.add(label_1, gbc_label_1);
		
		JRadioButton rdbtnSchool = new JRadioButton("School");
		GridBagConstraints gbc_rdbtnSchool = new GridBagConstraints();
		gbc_rdbtnSchool.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnSchool.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnSchool.gridx = 1;
		gbc_rdbtnSchool.gridy = 2;
		buildPopup.add(rdbtnSchool, gbc_rdbtnSchool);
		buildBtns.add(rdbtnSchool);

		JLabel label_2 = new JLabel("Costs $150");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 2;
		gbc_label_2.gridy = 2;
		buildPopup.add(label_2, gbc_label_2);
		
		JLabel label_3 = new JLabel("+5 ppl/sec, +$3/sec");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.insets = new Insets(0, 0, 5, 0);
		gbc_label_3.gridx = 3;
		gbc_label_3.gridy = 2;
		buildPopup.add(label_3, gbc_label_3);
		
		JRadioButton rdbtnHouse = new JRadioButton("House");
		GridBagConstraints gbc_rdbtnHouse = new GridBagConstraints();
		gbc_rdbtnHouse.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnHouse.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnHouse.gridx = 1;
		gbc_rdbtnHouse.gridy = 3;
		buildPopup.add(rdbtnHouse, gbc_rdbtnHouse);
		buildBtns.add(rdbtnHouse);

		JLabel label_4 = new JLabel("Costs $10");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 2;
		gbc_label_4.gridy = 3;
		buildPopup.add(label_4, gbc_label_4);
		
		JLabel label_5 = new JLabel("-1 env/sec, +2 ppl/sec");
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.insets = new Insets(0, 0, 5, 0);
		gbc_label_5.gridx = 3;
		gbc_label_5.gridy = 3;
		buildPopup.add(label_5, gbc_label_5);
		
		JRadioButton rdbtnFarm = new JRadioButton("Farm");
		GridBagConstraints gbc_rdbtnFarm = new GridBagConstraints();
		gbc_rdbtnFarm.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnFarm.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnFarm.gridx = 1;
		gbc_rdbtnFarm.gridy = 4;
		buildPopup.add(rdbtnFarm, gbc_rdbtnFarm);
		buildBtns.add(rdbtnFarm);

		JLabel label_6 = new JLabel("Costs $80");
		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.insets = new Insets(0, 0, 5, 5);
		gbc_label_6.gridx = 2;
		gbc_label_6.gridy = 4;
		buildPopup.add(label_6, gbc_label_6);
		
		JLabel label_7 = new JLabel("+4 env/sec, +$4/sec");
		GridBagConstraints gbc_label_7 = new GridBagConstraints();
		gbc_label_7.insets = new Insets(0, 0, 5, 0);
		gbc_label_7.gridx = 3;
		gbc_label_7.gridy = 4;
		buildPopup.add(label_7, gbc_label_7);
		
		JRadioButton rdbtnBoat = new JRadioButton("Boat");
		GridBagConstraints gbc_rdbtnBoat = new GridBagConstraints();
		gbc_rdbtnBoat.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnBoat.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnBoat.gridx = 1;
		gbc_rdbtnBoat.gridy = 5;
		buildPopup.add(rdbtnBoat, gbc_rdbtnBoat);
		buildBtns.add(rdbtnBoat);

		JLabel label_8 = new JLabel("Costs $5");
		GridBagConstraints gbc_label_8 = new GridBagConstraints();
		gbc_label_8.insets = new Insets(0, 0, 5, 5);
		gbc_label_8.gridx = 2;
		gbc_label_8.gridy = 5;
		buildPopup.add(label_8, gbc_label_8);
		
		JLabel label_9 = new JLabel("+4 env/sec, +$4/sec");
		GridBagConstraints gbc_label_9 = new GridBagConstraints();
		gbc_label_9.insets = new Insets(0, 0, 5, 0);
		gbc_label_9.gridx = 3;
		gbc_label_9.gridy = 5;
		buildPopup.add(label_9, gbc_label_9);
		
		final JRadioButton[] rdBtns = new JRadioButton[6];
		rdBtns[0] = rdbtnOilRefinery;
		rdBtns[1] = rdbtnWindTurbine;
		rdBtns[2] = rdbtnSchool;
		rdBtns[3] = rdbtnHouse;
		rdBtns[4] = rdbtnFarm;
		rdBtns[5] = rdbtnBoat;
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buildPopup.setVisible(false);
				hammer.setEnabled(true);
			}
		});
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.insets = new Insets(0, 0, 0, 5);
		gbc_btnClose.gridx = 2;
		gbc_btnClose.gridy = 6;
		buildPopup.add(btnClose, gbc_btnClose);
		
		JButton btnBuildThatShit = new JButton("Build");
		GridBagConstraints gbc_btnBuildThatShit = new GridBagConstraints();
		gbc_btnBuildThatShit.gridx = 3;
		gbc_btnBuildThatShit.gridy = 6;
		buildPopup.add(btnBuildThatShit, gbc_btnBuildThatShit);	
		btnBuildThatShit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedBuilding = "";
				for (int i = 0; i < rdBtns.length; i++) {
					if (rdBtns[i].isSelected()) {
						selectedBuilding = rdBtns[i].getText();
					}
				}
				
				buildPopup.setVisible(false);
				hammer.setEnabled(true);
				
				currentlySelectedBuilding = selectedBuilding;
				
				timeToBuild = true;
			}
		});
		
		buildPopup.setLocationRelativeTo(frame);
		buildPopup.getRootPane().setBorder(BorderFactory.createBevelBorder(0));
		buildPopup.setVisible(false);
		buildPopup.setUndecorated(true);
		

	}
	
	public class Pauses implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			t.getTimer().pauseTimer();
			t.getPausePopup().setVisible(true);
			t.getPausePopup().requestFocus();
			t.getPausePopup().setSize(300,300);
			t.getPausePopup().setLocationRelativeTo(frame);
			t.getPausePopup().setLayout(new MigLayout("", "[max][max]", "[grow][grow]"));
			JLabel paused = new JLabel("Game is paused.");
			paused.setFont(new Font("Papyrus",Font.BOLD,36));
			gamePaused = true;
			JButton closePause = new JButton("Resume");
			JButton quitGame = new JButton("Quit");
			closePause.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					t.getPausePopup().getContentPane().removeAll();
					t.getPausePopup().getContentPane().repaint();
					t.getPausePopup().getContentPane().revalidate();
					t.getPausePopup().setVisible(false);
					t.getTimer().startTimer();
					gamePaused = false;
				}
			});
			quitGame.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//for now, go back to homescreen
					transitionToStartScreen();
					t.getPausePopup().setVisible(false);
					
					//in future:
					//should just go to future screen and leaderboard
				}
			});
			t.getPausePopup().add(paused, "alignx center, aligny center, span 2, wrap");
			t.getPausePopup().add(closePause, "alignx center, aligny center");
			t.getPausePopup().add(quitGame,"alignx center, aligny center");
			t.getPausePopup().getContentPane().repaint();
			t.getPausePopup().getContentPane().revalidate();
		}
		
	}

	
	public void transitionToStartScreen() {
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		frame.getContentPane().revalidate();
		new TitleScreen(frame);
	}
	
	public void highlightTileLogic(int upperLeftX, int upperLeftY) {
		Graphics gridPanelGraphicsContext = this.gridPanel.getGraphics();
		if (this.tileHighlighted == false) {
			gridPanelGraphicsContext.setColor(Color.YELLOW);
			gridPanelGraphicsContext.drawRect(upperLeftX, upperLeftY, 50, 50);
			this.tileHighlighted = true;
			return;
		} else {//this.tileHighlighted == true
			if (this.previousMouseXCorr == upperLeftX && this.previousMouseYCorr == upperLeftY) {
				return;
			} else {//the player clicked a different tile than the one currently highlighted
				//unhighlight the currently highlighted tile
				gridPanelGraphicsContext.setColor(Color.GRAY);
				gridPanelGraphicsContext.drawRect(this.previousMouseXCorr, this.previousMouseYCorr, 50, 50);
				
				//highlight the newly clicked tile
				gridPanelGraphicsContext.setColor(Color.YELLOW);
				gridPanelGraphicsContext.drawRect(this.mouseXCorr, this.mouseYCorr, 50, 50);
			}
		}
	}
	
	//not needed anymore
	public void drawGridlines() {
		Graphics gridPanelGraphicsContext = this.gridPanel.getGraphics();
		gridPanelGraphicsContext.setColor(Color.GRAY);
		int xCoor = 0;
		int xCount = 0;
		while (xCount < 13) {
			int yCount = 0;
			int yCoor = 0;
			while (yCount < 8) {
				xCoor = xCount * 50;
				yCoor = yCount * 50;
				gridPanelGraphicsContext.drawRect(xCoor, yCoor, 50, 50);
				yCount++;
				System.out.println("Drawing gridlines");
			}
			xCount++;
		}
	}
	
	
	
}

