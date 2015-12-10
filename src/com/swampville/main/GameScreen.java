package com.swampville.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;
import timer.TimeFrame;

public class GameScreen implements Runnable {

	Tile[][] grid;
	JPanel gridPanel;

	JLabel gridLabel;
	
	ArrayList<BuildingTile> buildingTiles;
	
	Tile currentlySelectedTile;

	boolean tileHighlighted = false;

	boolean gameActive = false;
	
	String future;

	int buildAnimationLength = 150;
	int buildAnimationCounter = 0;
	
	int infoAnimationLength = 42;
	int infoAnimationCounter = 0;
	
	int timeLimit = 3;

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
	int animationCount = 0;
	Meter envMeter, pplMeter, timeMeter;
	JLabel moneyLbl;
	boolean gamePaused = false;
	int boatCount; // to prevent overfishing
	int environmentLblDelta;
	int pplLblDelta;
	int moneyLblDelta;
	int oneSecCounter = 0;
	int demoBuilding;
	int[][] buildingCodes = { { 0, 0, 0, 0, 0, 2, 2, 1, 1 }, { 0, 0, 0, 0, 2, 2, 1, 1, 1 },
			{ 0, 0, 0, 0, 2, 1, 1, 1, 1 }, { 0, 0, 0, 0, 2, 2, 2, 1, 1 }, { 2, 0, 0, 2, 2, 2, 2, 2, 1 },
			{ 2, 2, 2, 2, 2, 0, 0, 2, 1 }, { 2, 0, 0, 2, 0, 0, 2, 2, 1 }, { 0, 0, 2, 2, 0, 0, 0, 2, 1 },
			{ 0, 2, 2, 2, 0, 0, 0, 2, 1 }, { 2, 2, 2, 0, 0, 0, 0, 2, 1 }, { 2, 2, 2, 0, 0, 0, 0, 2, 1 },
			{ 0, 2, 2, 0, 0, 2, 2, 2, 1 }, { 2, 2, 2, 2, 2, 2, 2, 1, 1 }, { 0, 0, 0, 2, 2, 1, 1, 1, 1 } };

	MigLayout gameLayout = new MigLayout("fill, insets 0", "[max][max]", "[max][max][max][max][max][max]");

	/**
	 * @param frame
	 * @param difficulty
	 * @throws IOException
	 */
	public GameScreen(JFrame frame, int demoBuilding) throws IOException {

		this.demoBuilding = demoBuilding;
		this.frame = frame;
		this.initializeGameScreen();
		gameActive = true;

		this.buildingTiles = new ArrayList<BuildingTile>();
		
		buildingCodes[2][1] = demoBuilding;
		
		if (demoBuilding == 5) {
			addBuilding("Oil Refinery");
		}
		if (demoBuilding == 6) {
			addBuilding("Windfarm");
		}
		if (demoBuilding == 7) {
			addBuilding("School");
		}
		if (demoBuilding == 8) {
			addBuilding("House");
		}
		if (demoBuilding == 9) {
			addBuilding("Farm");
		}

		ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);
		executor2.scheduleAtFixedRate(this, 0, 1, TimeUnit.MILLISECONDS);
		Collections.synchronizedList(this.buildingTiles);
	}

	@Override
	public void run() {

		// Put code to repaint meters in here, if necessary.

		try {
			this.updateInOneSec();
			this.update();
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
	 * Setup Tile grid on frame. Put buttons and timer on frame.
	 *
	 * Adjusted so that the frame is resized to fit the screen, then the
	 * components are added in a more orderly fashion: first the grid, followed
	 * by the three meters, and finally the famous buttons.
	 */
	public void initializeGameScreen() {

		// Setting the frame's layout before adding everything makes it so that
		// we are not dealing with the title screen's MigLayout anymore, which
		// stops everything from shifting. I think. Regardless, it works as of
		// now.

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Rectangle screenSize = new Rectangle((new Point(0, 0)), tk.getScreenSize());
		frame.setBounds(screenSize.width / 5, screenSize.height / 4, 900, 472);
		frame.getContentPane().setLayout(gameLayout);
		
		frame.getContentPane().setBackground(new Color(68, 102, 0));

		this.initializeGrid();
		this.initializeMeters();

		this.drawGridlines();

		for (Component comp : this.frame.getContentPane().getComponents()) {
			System.out.println(comp);
		}

		//////////////// ADDING STOP TO GAME FRAME/////////////////////////

		stop = new JButton("");
		try {
			BufferedImage stopImg = ImageIO.read(new File("src/swampimages/stop.png"));
			stop.setIcon(new ImageIcon(stopImg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stop.setBackground(new Color(102, 153, 0));
		stop.setOpaque(true);
		stop.setBorderPainted(false);
		stop.addActionListener(new Pauses());
		frame.getContentPane().add(stop, "cell 1 3,grow");

		//////////////// ADDING HAMMER TO GAME FRAME/////////////////////////

		hammer = new JButton("");

		try {
			BufferedImage hammerImg = ImageIO.read(new File("src/swampimages/hammer.png"));
			hammer.setIcon(new ImageIcon(hammerImg));
		} catch (IOException e) {
			e.printStackTrace();
		}

		hammer.setBackground(new Color(102, 153, 0));
		hammer.setOpaque(true);
		hammer.setBorderPainted(false);
		
		hammer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.playSound("click.wav");
				triggerBuildPopup();
				toggleButtons(false);
			}
		});
		frame.getContentPane().add(hammer, "cell 1 4,grow");

		//////////////// ADDING TIME TO GAME FRAME/////////////////////////

		t = new TimeFrame();
		frame.getContentPane().add(t, "cell 1 5,grow, center");

//		new ArcProgress(frame, 120);
		
		/////////////////////////////////////////////////////////////

		// Create pop-up for the building list for later use
		buildPopup = new JFrame();
		createBuildPanel();
	}

	public void initializeMeters() {

		// Create Env. meter
		/*
		 * JPanel environment = new JPanel(); environmentLbl = new JLabel("50");
		 * ImageIcon environmentImg = new
		 * ImageIcon("src/swampimages/environment.png");
		 * environmentLbl.setIcon(environmentImg);
		 * environment.add(environmentLbl);
		 */

		
		JLabel envImg = new JLabel();
		ImageIcon environmentImg = new ImageIcon("src/swampimages/Blue_Recycle_Symbol.png");
		envImg.setIcon(environmentImg);
		envMeter = new Meter(35, 50, 100, 20);
		envMeter.add(envImg, BorderLayout.WEST);

		// Create People meter
		/*
		 * JPanel people = new JPanel(); pplLbl = new JLabel("10"); ImageIcon
		 * peopleImg = new ImageIcon("src/swampimages/people.png");
		 * pplLbl.setIcon(peopleImg); people.add(pplLbl);
		 */

		JLabel pplImg = new JLabel();
		ImageIcon pplIcon = new ImageIcon("src/swampimages/people.png");
		pplImg.setIcon(pplIcon);
		pplMeter = new Meter(35, 49, 100, 20);
		pplMeter.add(pplImg, BorderLayout.WEST);

		// Create Money Meter
		JPanel money = new JPanel();
		money.setBackground(new Color(102, 153, 0));
		money.setOpaque(true);
		
		moneyLbl = new JLabel("100");
		moneyLbl.setFont(new Font("Papyrus", Font.BOLD, 24));
		moneyLbl.setForeground(Color.WHITE);
		ImageIcon moneyImg = new ImageIcon("src/swampimages/Coin.png");
		moneyLbl.setIcon(moneyImg);
		money.add(moneyLbl);

		/*
		 * JPanel maxPop = new JPanel(); maxPopLbl = new JLabel("10"); ImageIcon
		 * maxPopImg = new ImageIcon("src/swampimages/maxpop.png");
		 * maxPopLbl.setIcon(maxPopImg); maxPop.add(maxPopLbl);
		 */

		// Add to frame
		this.frame.getContentPane().add(envMeter, "cell 1 0,grow, center");
		this.frame.getContentPane().add(pplMeter, "cell 1 1,grow");
		this.frame.getContentPane().add(money, "cell 1 2,grow");

		envMeter.setFillWidth(30);
		pplMeter.setFillWidth(2);

		envMeter.repaint();
	}

	public int[] gridCorr(int mouseX, int mouseY) {
		int gridX = mouseX / 50;
		int gridY = mouseY / 50;
		int[] coordinates = { gridX * 50, gridY * 50 };
		return coordinates;
	}

	public void initializeGrid() {
		this.gridPanel = new JPanel();

		// Set up the grid image, put it on a JLabel
		// Add JLabel to subpanel containing grid
		BufferedImage gridImg;
		try {
			gridImg = ImageIO.read(new File("src/swampimages/easyswamp.png"));
			gridLabel = new JLabel();
			gridLabel.setIcon(new ImageIcon(gridImg));
			gridPanel.setLayout(new MigLayout("fill, insets 0", "", ""));
			gridPanel.add(gridLabel, "aligny top");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Add gridPanel to the game frame, in first panel
		this.frame.getContentPane().add(gridPanel, "cell 0 0 1 6,grow");

		// Add mouseListener to track the coordinates of the mouse clicks
		this.gridPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				previousMouseXCorr = mouseXCorr;
				previousMouseYCorr = mouseYCorr;
				mouseXCorr = gridCorr(e.getX(), e.getY())[0];
				mouseYCorr = gridCorr(e.getX(), e.getY())[1];

				if (buildingCodes[mouseXCorr / 50][mouseYCorr / 50] == 2) {
					
					try {
						BufferedImage hammerSlashedImg = ImageIO.read(new File("src/swampimages/Hammer_Slashed.png"));
	                    JOptionPane.showMessageDialog(
	                            frame,
	                            "You can't build here. Select a new tile.",
	                            "", JOptionPane.INFORMATION_MESSAGE,
	                            new ImageIcon(hammerSlashedImg));
	                	
					} catch (IOException i) {
						i.printStackTrace();
					}
					mouseXCorr = previousMouseXCorr;
					mouseYCorr = previousMouseYCorr;
					return;
				} else {
					highlightTileLogic(mouseXCorr, mouseYCorr);
				}
				// For Testing Purposes:
				System.out.println(mouseXCorr + ", " + mouseYCorr);
				
				int x = e.getX();
				int y = e.getY();
				
				for (BuildingTile bt : buildingTiles) {
					if (bt.getGoodiePresent() == true) {
						if (bt.getXPos() == x && bt.getYPos() == y) {
							if (buildAnimationCounter >= buildAnimationLength) {
								try {
									gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/" + bt.getBuildingType() + ".png")),
											bt.getXPos(), bt.getYPos(), gridPanel);
									bt.setGoodiePresent(false);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
				}
				
				
				
			}
		});
	}
	
	private int calculateScore() {

		future = "";

		for (int i = 0; i < 50; i++) {
			updateMeters();
		}
		int finalEnvScore = envMeter.getValue() * 3;
		double finalPplScore = pplMeter.getValue() * 1.5;
		int finalMoney = getMoney();

		int finalScore = finalEnvScore + (int) finalPplScore + finalMoney;

		if (envMeter.getValue() < 50) {
			finalScore -= 2000;

			future += "Your enviornment would be worse off than when you started.\nRemember, you can sustain a population while maintaining a healthy estuary!\nBalance is key!!\n\n";

		}
		if (pplMeter.getValue() < 10) {
			finalScore -= 1000;
			future += "You would end up with less people than you started with!\n Balance is a big part of having a healthy human-enivornment relationship.\n\n";
		}
		if (getMoney() < 0) {
			finalScore -= 10 * Math.abs(getMoney());
			future += "You would be $" + Math.abs(getMoney())
					+ " in debt! Managing an economy and an ecosystem can be hard, but it's doable!\nTry again to see if you can do better!\n\n";
		}

		if (finalScore < 0)
			finalScore = 0;

		if (finalScore == 0)
			future += "Your final score was " + finalScore
					+ "!\nThink you can do better? ... You can't really do worse.";
		else
			future += "Your final score was " + finalScore + "!\nThink you can do better?";

		return finalScore;
	}

	/**
	 * Initialize pausePopup & put in foreground
	 */
	public void pauseGame() {
		gamePaused = true;
		t.getTimer().pauseTimer();
		toggleButtons(false);
	}

	public void resumeGame() {
		gamePaused = false;
		t.getTimer().startTimer();
		toggleButtons(true);
	}

	private void toggleButtons(boolean set) {
		hammer.setEnabled(set);
		stop.setEnabled(set);
	}

	/**
	 * Put up buildPopup. Populate buildPopup with Building options based on
	 * difficulty String. Put in exitPopup & confirmBuild buttons.
	 */
	public void triggerBuildPopup() {

		buildPopup.setVisible(true);
		buildPopup.requestFocus();
		// frame.getContentPane().repaint();
	}

	private void paintAllGoodies() {
		for (BuildingTile bt : this.buildingTiles) {
			if (bt.getGoodiePresent() == true) {
				this.paintGoodie(bt.getBuildingType(), bt.getXPos(), bt.getYPos());
			}
		}
	}
	
	private void update() {
		oneSecCounter++;
		buildAnimationCounter++;
		infoAnimationCounter++;
		try {
			makinMagic();
			updateBuildings();
			paintAllGoodies();
			
			if (buildAnimationCounter >= buildAnimationLength) {
				buildAnimationCounter = 0;
				if (animationCount < 4) {
					animationCount++;
				} else {
					animationCount = 0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calls updateMetersInOneSec(). Updates JLabel timer. If timer at any of
	 * the eventTimes, then call triggerEvent() on respective event. If timer at
	 * 0 seconds, then call transitionToFutureScreen().
	 * 
	 * The if loop with t.getTime() decides when the game ends. *can be
	 * t.getMintues() or t.getSeconds()
	 * 
	 * @throws IOException
	 */
	public void updateInOneSec() throws IOException {
		// System.out.println("updateInOneSec");
		if (gameActive && oneSecCounter >= 1000) {
			oneSecCounter = 0;
			if (gamePaused == false) {
				updateMeters();
				
				for (BuildingTile bt: this.buildingTiles) {
					bt.updateInOneSec();
				}
			}

			System.out.println(this.buildingTiles);
			if (t.getMinutes() == timeLimit) {
				transitionToFutureScreen(calculateScore(), future);
			}

		}
	}

	public void updateBuildings() throws IOException {

		for (int x = 0; x < 14; x++) {
			for (int y = 0; y < 9; y++) {
				if (buildingCodes[x][y] > 3) {

					int xCoor = x * 50;
					int yCoor = y * 50;
					if (buildingCodes[x][y] == 5) { // Oil Refinery
						int yCoorT = yCoor;
						while (yCoorT > yCoor - 50) {
							yCoorT--;
						}
						if (buildAnimationCounter >= buildAnimationLength) {
							this.gridPanel.getGraphics().drawImage(
									ImageIO.read(new File("src/swampimages/Oil Refinery" + animationCount + ".png")),
									xCoor, yCoor, this.gridPanel);
						}
					}
					if (buildingCodes[x][y] == 6) { // Windfarm
						if (buildAnimationCounter >= buildAnimationLength) {
							this.gridPanel.getGraphics().drawImage(
									ImageIO.read(new File("src/swampimages/Windfarm" + animationCount + ".png")), xCoor,
									yCoor, this.gridPanel);
						}
					}
					if (buildingCodes[x][y] == 7) { // School
						if (buildAnimationCounter >= buildAnimationLength) {
							this.gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/School" + animationCount + ".png")), xCoor,
									yCoor, this.gridPanel);
						}
					}
					if (buildingCodes[x][y] == 8) { // House
						if (buildAnimationCounter >= buildAnimationLength) {
							this.gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/House.png")),
									xCoor, yCoor, this.gridPanel);
						}
					}
					if (buildingCodes[x][y] == 9) { // Farm
						if (buildAnimationCounter >= buildAnimationLength) {
							this.gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/Farm" + animationCount + ".png")), xCoor,
									yCoor, this.gridPanel);
						}
					}
					if (buildingCodes[x][y] == 4) { // Boat
						if (buildAnimationCounter >= buildAnimationLength) {
							this.gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/Boat.png")),
									xCoor, yCoor, this.gridPanel);
						}
					}

				}

			}
		}

	}

	public void updateMeters() {
		int peopleModifier = (int) (pplMeter.getValue() * .01);
		int extraCash = moneyLblDelta + (moneyLblDelta * peopleModifier);
		this.moneyLbl.setText(Integer.toString(getMoney() + extraCash));

		envMeter.changeMeter(environmentLblDelta);
		envMeter.repaint();

		pplMeter.changeMeter(pplLblDelta);
		pplMeter.repaint();

	}

	public int getMoney() {
		return Integer.valueOf(moneyLbl.getText());
	}

	public void makinMagic() throws IOException {
		if (this.timeToBuild == true) {
			int xCode = mouseXCorr / 50;
			int yCode = mouseYCorr / 50;
			System.out.print(xCode + "," + yCode + "\n");
			int buildCode = buildingCodes[xCode][yCode];
			System.out.print("Building code: " + buildCode + "\n"); // print the
																	// buildcode
																	// of the
																	// selected
																	// tile

			if (buildCode == 1) { // water
				if (this.currentlySelectedBuilding.equals("Boat")) { // if boat
					if (boatCount >= 5) {
						try {
							BufferedImage sadFishImg = ImageIO.read(new File("src/swampimages/Sad_Fish.png"));
		                    JOptionPane.showMessageDialog(
		                            frame,
		                            "WARNING! Building this boat will cause over-fishing." 
		                            + " \n Over-fishing negatively impacts the environment (-10 env/sec).",
		                            "", JOptionPane.INFORMATION_MESSAGE,
		                            new ImageIcon(sadFishImg));
						} catch (IOException i) {
							i.printStackTrace();
						}
						boatCount += 1;
						this.gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/Boat.png")),
								mouseXCorr, mouseYCorr, this.gridPanel);
						this.addBuilding(this.currentlySelectedBuilding);
						buildingCodes[xCode][yCode] = 4; // set code to occupied
															// by water building

						// change the environment effects of boats
						// over fishing is happening so make the env meter get
						// wrecked
						this.environmentLblDelta -= 10;

						this.timeToBuild = false;
					}

					else {
						boatCount += 1;
						this.gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/Boat.png")),
								mouseXCorr, mouseYCorr, this.gridPanel);
						this.addBuilding(this.currentlySelectedBuilding);
						buildingCodes[xCode][yCode] = 4; // set code to occupied
															// by water building
						this.timeToBuild = false;
					}
				} else { // if not a boat
					try {
						BufferedImage hammerSlashedImg = ImageIO.read(new File("src/swampimages/Hammer_Slashed.png"));
	                    JOptionPane.showMessageDialog(
	                            frame,
	                            "You can't build a building on water. Select a new tile.",
	                            "", JOptionPane.INFORMATION_MESSAGE,
	                            new ImageIcon(hammerSlashedImg));
	                	
					} catch (IOException i) {
						i.printStackTrace();
					}
					this.timeToBuild = false;
				}
			}
			if (buildCode == 0) { // land
				if (this.currentlySelectedBuilding.equals("Boat")) {
					try {
						BufferedImage boatSlashedImg = ImageIO.read(new File("src/swampimages/Boat_Slashed.png"));
	                    JOptionPane.showMessageDialog(
	                            frame,
	                            "You can't build a boat on land. Try again.",
	                            "", JOptionPane.INFORMATION_MESSAGE,
	                            new ImageIcon(boatSlashedImg));
	                	
					} catch (IOException i) {
						i.printStackTrace();
					}
					this.timeToBuild = false;
				} else {

					this.gridPanel.getGraphics().drawImage(
							ImageIO.read(new File("src/swampimages/" + this.currentlySelectedBuilding + ".png")),
							this.mouseXCorr, this.mouseYCorr, this.gridPanel);
					this.addBuilding(this.currentlySelectedBuilding);

					if (this.currentlySelectedBuilding == "Oil Refinery") {
						buildingCodes[xCode][yCode] = 5;
					}
					if (this.currentlySelectedBuilding == "Windfarm") {
						buildingCodes[xCode][yCode] = 6;
					}
					if (this.currentlySelectedBuilding == "School") {
						buildingCodes[xCode][yCode] = 7;
					}
					if (this.currentlySelectedBuilding == "House") {
						buildingCodes[xCode][yCode] = 8;
					}
					if (this.currentlySelectedBuilding == "Farm") {
						buildingCodes[xCode][yCode] = 9;
					}
					this.timeToBuild = false;

				}
			}
			if (buildCode == 2) { // not buildable tile
				JOptionPane.showMessageDialog(frame, "You can't build here. Select a new tile.", "ERROR",
						JOptionPane.WARNING_MESSAGE);
				this.timeToBuild = false;
			}
			if (buildCode > 3) { // occupied
				JOptionPane.showMessageDialog(frame, "This tile is full. Select a new tile.", "ERROR",
						JOptionPane.WARNING_MESSAGE);

				this.timeToBuild = false;
			}
		}
	}

	public void addBuilding(String buildingStr) {

		int newEnvironmentDelta = 0;
		int newPeopleDelta = 0;
		int newMoneyDelta = 0;

		if (buildingStr.equals("Boat")) {
			newEnvironmentDelta = Building.BOAT.enviornmentEffect;
			newPeopleDelta = Building.BOAT.peopleEffect;
			newMoneyDelta = Building.BOAT.moneyEffect;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.BOAT.cost));
			BuildingTile bt = new BuildingTile(buildingStr, this.mouseXCorr, this.mouseYCorr, Building.boatNumSecsTillGoodieFinal);
			this.buildingTiles.add(bt);
		}
		if (buildingStr.equals("Farm")) {
			newEnvironmentDelta = Building.FARM.enviornmentEffect;
			newPeopleDelta = Building.FARM.peopleEffect;
			newMoneyDelta = Building.FARM.moneyEffect;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.FARM.cost));
			BuildingTile bt = new BuildingTile(buildingStr, this.mouseXCorr, this.mouseYCorr, Building.farmNumSecsTillGoodieFinal);
			this.buildingTiles.add(bt);
		}
		if (buildingStr.equals("House")) {
			newEnvironmentDelta = Building.HOUSE.enviornmentEffect;
			newPeopleDelta = Building.HOUSE.peopleEffect;
			newMoneyDelta = Building.HOUSE.moneyEffect;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.HOUSE.cost));
			BuildingTile bt = new BuildingTile(buildingStr, this.mouseXCorr, this.mouseYCorr, Building.houseNumSecsTillGoodieFinal);
			this.buildingTiles.add(bt);
		}
		if (buildingStr.equals("Oil Refinery")) {
			newEnvironmentDelta = Building.OILREFINERY.enviornmentEffect;
			newPeopleDelta = Building.OILREFINERY.peopleEffect;
			newMoneyDelta = Building.OILREFINERY.moneyEffect;
			this.moneyLbl
					.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.OILREFINERY.cost));
			BuildingTile bt = new BuildingTile(buildingStr, this.mouseXCorr, this.mouseYCorr, Building.oilRefineryNumSecsTillGoodieFinal);
			this.buildingTiles.add(bt);
		}
		if (buildingStr.equals("School")) {
			newEnvironmentDelta = Building.SCHOOL.enviornmentEffect;
			newPeopleDelta = Building.SCHOOL.peopleEffect;
			newMoneyDelta = Building.SCHOOL.moneyEffect;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.SCHOOL.cost));
			BuildingTile bt = new BuildingTile(buildingStr, this.mouseXCorr, this.mouseYCorr, Building.schoolNumSecsTillGoodieFinal);
			this.buildingTiles.add(bt);
		}
		if (buildingStr.equals("Windfarm")) {
			newEnvironmentDelta = Building.WINDFARM.enviornmentEffect;
			newPeopleDelta = Building.WINDFARM.peopleEffect;
			newMoneyDelta = Building.WINDFARM.moneyEffect;
			this.moneyLbl.setText(Integer.toString(Integer.valueOf(this.moneyLbl.getText()) - Building.WINDFARM.cost));
			BuildingTile bt = new BuildingTile(buildingStr, this.mouseXCorr, this.mouseYCorr, Building.windFarmNumSecsTillGoodieFinal);
			this.buildingTiles.add(bt);
		}

		this.environmentLblDelta = this.environmentLblDelta + newEnvironmentDelta;
		this.pplLblDelta = this.pplLblDelta + newPeopleDelta;
		this.moneyLblDelta = this.moneyLblDelta + newMoneyDelta;
	}

	private void transitionToFutureScreen(int score, String future) {
		toggleButtons(false);
		gameActive = false;
		if (buildPopup.isVisible())
			buildPopup.setVisible(false);
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		frame.getContentPane().revalidate();
		new FutureScreen(frame, score, future);
	}


	public void triggerEvent() {
		// put up eventPopup
		// update meters based on what the event is
	}

	private void createBuildPanel() {
		// frame.getContentPane().add(buildPopup, "cell 2 1,grow");

		ButtonGroup buildBtns = new ButtonGroup();

		// buildPopup.setBorder(new BevelBorder(BevelBorder.RAISED, null, null,
		// null, null));
		Rectangle frameRect = this.frame.getContentPane().getBounds();
		buildPopup.setBounds(frameRect.x / 4, frameRect.y / 4, frameRect.width / 2, frameRect.height / 2 + 150);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 25, 25, 25, 25, 25, 25, 25, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		buildPopup.setLayout(gridBagLayout);

        java.net.URL url = GameScreen.class.getResource("/swampimages/transOil Refinery.png");
        String html = "<html><body><img src='" + url.toString() +"' height='25' width='25'><p>Oil Refinery</p>";
		JRadioButton rdbtnOilRefinery = new JRadioButton(html);
		rdbtnOilRefinery.setName("Oil Refinery");
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

        url = GameScreen.class.getResource("/swampimages/transWindfarm.png");
        html = "<html><body><img src='" + url.toString() +"' height='25' width='25'><p>Windfarm</p>";
		JRadioButton rdbtnWindTurbine = new JRadioButton(html);
		rdbtnWindTurbine.setName("Windfarm");
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
		
        url = GameScreen.class.getResource("/swampimages/transSchool.png");
        html = "<html><body><img src='" + url.toString() +"' height='25' width='25'><p>School</p>";
		JRadioButton rdbtnSchool = new JRadioButton(html);
		rdbtnSchool.setName("School");
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
		
        url = GameScreen.class.getResource("/swampimages/House.png");
        html = "<html><body><img src='" + url.toString() +"' height='25' width='25'><p>House</p>";
		JRadioButton rdbtnHouse = new JRadioButton(html);
		rdbtnHouse.setName("House");
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

        url = GameScreen.class.getResource("/swampimages/Farm.png");
        html = "<html><body><img src='" + url.toString() +"' height='25' width='25'><p>Farm</p>";
		JRadioButton rdbtnFarm = new JRadioButton(html);
		rdbtnFarm.setName("Farm");
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

        url = GameScreen.class.getResource("/swampimages/Boat.png");
        html = "<html><body><img src='" + url.toString() +"' height='25' width='25'><p>Boat</p>";
		JRadioButton rdbtnBoat = new JRadioButton(html);
		rdbtnBoat.setName("Boat");
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
		
		JButton btnClose = new JButton("");
		try {
			BufferedImage closeImg = ImageIO.read(new File("src/swampimages/Red_X.png"));
			btnClose.setIcon(new ImageIcon(closeImg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.playSound("click.wav");
				buildPopup.setVisible(false);
				toggleButtons(true);
			}
		});
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.insets = new Insets(0, 0, 0, 5);
		gbc_btnClose.gridx = 2;
		gbc_btnClose.gridy = 6;
		buildPopup.add(btnClose, gbc_btnClose);
		
		JButton btnBuildThatShit = new JButton("");
		try {
			BufferedImage hammerImg = ImageIO.read(new File("src/swampimages/hammer.png"));
			btnBuildThatShit.setIcon(new ImageIcon(hammerImg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GridBagConstraints gbc_btnBuildThatShit = new GridBagConstraints();
		gbc_btnBuildThatShit.gridx = 3;
		gbc_btnBuildThatShit.gridy = 6;
		buildPopup.add(btnBuildThatShit, gbc_btnBuildThatShit);
		btnBuildThatShit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Sound.playSound("click.wav");
				//String[] selectedBuildingSplit;
				String selectedBuilding = "";
				for (int i = 0; i < rdBtns.length; i++) {
					if (rdBtns[i].isSelected()) {
						selectedBuilding = rdBtns[i].getName();
					}
				}
				
				System.out.println(selectedBuilding);

				buildPopup.setVisible(false);
				toggleButtons(true);

				currentlySelectedBuilding = selectedBuilding;

				if (currentlySelectedBuilding.equals("Boat")) {
					if (getMoney() - Building.BOAT.cost >= 0)
						timeToBuild = true;
					else
						try {
							BufferedImage needMoneyImg = ImageIO.read(new File("src/swampimages/Coin_Slashed.png"));
		                    JOptionPane.showMessageDialog(
		                            frame,
		                            "You don't have enough money!",
		                            "", JOptionPane.INFORMATION_MESSAGE,
		                            new ImageIcon(needMoneyImg));
		                	
						} catch (IOException i) {
							i.printStackTrace();
						}

				}
				if (currentlySelectedBuilding.equals("Farm")) {
					if (getMoney() - Building.FARM.cost >= 0)
						timeToBuild = true;
					else
						try {
							BufferedImage needMoneyImg = ImageIO.read(new File("src/swampimages/Coin_Slashed.png"));
		                    JOptionPane.showMessageDialog(
		                            frame,
		                            "You don't have enough money!",
		                            "", JOptionPane.INFORMATION_MESSAGE,
		                            new ImageIcon(needMoneyImg));
		                	
						} catch (IOException i) {
							i.printStackTrace();
						}
				}
				if (currentlySelectedBuilding.equals("House")) {
					if (getMoney() - Building.HOUSE.cost >= 0)
						timeToBuild = true;
					else
						try {
							BufferedImage needMoneyImg = ImageIO.read(new File("src/swampimages/Coin_Slashed.png"));
		                    JOptionPane.showMessageDialog(
		                            frame,
		                            "You don't have enough money!",
		                            "", JOptionPane.INFORMATION_MESSAGE,
		                            new ImageIcon(needMoneyImg));
		                	
						} catch (IOException i) {
							i.printStackTrace();
						}
				}
				if (currentlySelectedBuilding.equals("Oil Refinery")) {
					if (getMoney() - Building.OILREFINERY.cost >= 0)
						timeToBuild = true;
					else
						try {
							BufferedImage needMoneyImg = ImageIO.read(new File("src/swampimages/Coin_Slashed.png"));
		                    JOptionPane.showMessageDialog(
		                            frame,
		                            "You don't have enough money!",
		                            "", JOptionPane.INFORMATION_MESSAGE,
		                            new ImageIcon(needMoneyImg));
		                	
						} catch (IOException i) {
							i.printStackTrace();
						}
				}
				if (currentlySelectedBuilding.equals("School")) {
					if (getMoney() - Building.SCHOOL.cost >= 0)
						timeToBuild = true;
					else
						try {
							BufferedImage needMoneyImg = ImageIO.read(new File("src/swampimages/Coin_Slashed.png"));
		                    JOptionPane.showMessageDialog(
		                            frame,
		                            "You don't have enough money!",
		                            "", JOptionPane.INFORMATION_MESSAGE,
		                            new ImageIcon(needMoneyImg));
		                	
						} catch (IOException i) {
							i.printStackTrace();
						}
				}
				if (currentlySelectedBuilding.equals("Windfarm")) {
					if (getMoney() - Building.WINDFARM.cost >= 0)
						timeToBuild = true;
					else
						try {
							BufferedImage needMoneyImg = ImageIO.read(new File("src/swampimages/Coin_Slashed.png"));
		                    JOptionPane.showMessageDialog(
		                            frame,
		                            "You don't have enough money!",
		                            "", JOptionPane.INFORMATION_MESSAGE,
		                            new ImageIcon(needMoneyImg));
		                	
						} catch (IOException i) {
							i.printStackTrace();
						}
				}

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
			Sound.playSound("click.wav");
			t.getTimer().pauseTimer();
			t.getPausePopup().setVisible(true);
			t.getPausePopup().requestFocus();
			t.getPausePopup().setSize(300, 300);
			t.getPausePopup().setLocationRelativeTo(frame);
			t.getPausePopup().setLayout(new MigLayout("", "[max][max]", "[grow][grow]"));
			JLabel paused = new JLabel("Game is paused.");
			paused.setFont(new Font("Papyrus", Font.BOLD, 36));
			gamePaused = true;
			
			
			JButton closePause = new JButton("");
			try {
				BufferedImage closePauseImg = ImageIO.read(new File("src/swampimages/Red_X.png"));
				closePause.setIcon(new ImageIcon(closePauseImg));
			} catch (IOException i) {
				i.printStackTrace();
			}

			JButton quitGame = new JButton("");
			try {
				BufferedImage quitGameImg = ImageIO.read(new File("src/swampimages/Quit_Image.png"));
				quitGame.setIcon(new ImageIcon(quitGameImg));
			} catch (IOException i) {
				i.printStackTrace();
			}
			
			closePause.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Sound.playSound("click.wav");
					t.getPausePopup().getContentPane().removeAll();
					t.getPausePopup().getContentPane().repaint();
					t.getPausePopup().getContentPane().revalidate();
					t.getPausePopup().setVisible(false);
					t.getTimer().startTimer();
					toggleButtons(true);
					gamePaused = false;
				}
			});
			quitGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// for now, go back to homescreen
					Sound.playSound("click.wav");
					transitionToStartScreen();
					t.getPausePopup().setVisible(false);

					// in future:
					// should just go to future screen and leaderboard
				}
			});
			t.getPausePopup().add(paused, "alignx center, aligny center, span 2, wrap");
			t.getPausePopup().add(closePause, "alignx center, aligny center");
			t.getPausePopup().add(quitGame, "alignx center, aligny center");
			t.getPausePopup().getContentPane().repaint();
			t.getPausePopup().getContentPane().revalidate();
		}

	}

	public void transitionToStartScreen() {
		gameActive = false;
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
		} else {// this.tileHighlighted == true
			if (this.previousMouseXCorr == upperLeftX && this.previousMouseYCorr == upperLeftY) {
				return;
			} else {// the player clicked a different tile than the one
					// currently highlighted
				// unhighlight the currently highlighted tile
				gridPanelGraphicsContext.setColor(Color.GRAY);
				gridPanelGraphicsContext.drawRect(this.previousMouseXCorr, this.previousMouseYCorr, 50, 50);

				// highlight the newly clicked tile
				gridPanelGraphicsContext.setColor(Color.YELLOW);
				gridPanelGraphicsContext.drawRect(this.mouseXCorr, this.mouseYCorr, 50, 50);
			}
		}
	}

	public void paintGoodie(String buildingType, int xPos, int yPos) {
		if (buildingType.equals("Boat")) {
			try {
				System.out.println("Adding Fish Goodie");
				this.gridPanel.getGraphics().drawImage(ImageIO.read(new File("src/swampimages/Fish_Goodie.png")), xPos, yPos, this.gridPanel);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	// not needed anymore
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
