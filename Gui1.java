/** 
    The NotPvZ program creates a game interface meant to simulate a 
    simplified version of the game Plants vs. Zombies.
    @author Yasmeen Roumie
    @author Ryan Tu
*/

import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.imageio.*;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;


public class Gui1 extends JFrame{
    
    private JFrame overall;
    /* overall is the frame where all the panels are on */

    private JPanel gameBoard; 
    /* gameBoard is the JPanel intended to be where shooting objects occur. 
       Should there be JLayeredPanel for our purposes? */
    private JPanel top, play, bottom; 
    /* top holds JLabel counter and ButtonGroup selection, 
       play is where JButton[][] grid is */

    private JLabel counter, status;
    private ButtonGroup selection;
    private JRadioButton A,B,C,D,r;

    private JButton[][] grid;

    private JButton start, reset;

    public int sunCount = 0;
    public int sunflowerNumber = 0;
    public int target = 50;
    public int zombieNum = 0;

    public double sunMath = 0.0;
    public double zombieMath = 0.0;

    public String statusLabel = "";

    private Random random = new Random();   
    private boolean isStarted = false, isEnded = true;
    private boolean youLose = false;

    public Timer timer = new Timer();
    public int speed = 1000;



    /**
       Gui1 is the constructor responsible for making the game display. Within it are many methods which create the game board seen when playing and the timers which determine when events occur. 
    */
    public Gui1() {

	overall = new JFrame();
	overall.setSize(600,600);
	overall.setDefaultCloseOperation(EXIT_ON_CLOSE);
	overall.setLayout(new BorderLayout());
	overall.setTitle("Not PvZ");

	top = new JPanel();
	top.setLayout(new GridLayout());
	overall.add(top, BorderLayout.PAGE_START);

	counter = new JLabel("Counter: 0", JLabel.CENTER);
	top.add(counter);

        A = new JRadioButton("Sunflower (50 suns)");
	A.setSelected(true);
	
        B = new JRadioButton("Pea Shooter (100 suns)");
	
        C = new JRadioButton("Chomper (125 suns)");

        D = new JRadioButton("Gatling Pea Shooter (200 suns)");

	r = new JRadioButton("Remove Plant");

	selection = new ButtonGroup();
	selection.add(A);
	selection.add(B);
	selection.add(C);
        selection.add(D);
	selection.add(r);
	top.add(A);
	top.add(B);
	top.add(C);
	top.add(D);
	top.add(r);

	gameBoard = new JPanel();
	overall.add(gameBoard, BorderLayout.CENTER);
	
	gridMaker();

	bottom = new JPanel();
	bottom.setLayout(new GridLayout(1,3));
	overall.add(bottom, BorderLayout.PAGE_END);

	start = new JButton("Start");
	start.addActionListener(new Begin());
	bottom.add(start);

	reset = new JButton("Reset");
	reset.addActionListener(new End());
	bottom.add(reset);
	
	status = new JLabel("Status: 50 left", JLabel.CENTER);
	bottom.add(status, BorderLayout.PAGE_END);
	
	timer.scheduleAtFixedRate(new SunUpdate(), 0, 2000);
        timer.scheduleAtFixedRate(new Zombie(), 0, 4000);
	timer.scheduleAtFixedRate(new ProjectileSet(), 0, 2500);
	timer.scheduleAtFixedRate(new ProjectileMove(), 0, 1000);
	timer.scheduleAtFixedRate(new Chomper(), 0, 1000);
	timer.scheduleAtFixedRate(new killProjs(), 0, 1000);

	overall.pack();
	overall.setVisible(true);

    }

    /**
       Obtains the current number of "suns" (currency used to insert various
       plants) available.
       @return the number of "suns" currently in the game
    */
    public int getSunCount(){
	return sunCount;
    }

    /** 
	Applies changes towards the number of "suns" in the game towards the variable holding the number.
	@param sunCount  the number that the variable "sunCount" is meant to  become
    */
    public void setSunCount(int sunCount) {
        this.sunCount = sunCount;
    }

    /**
       Reflects any change in the variable holding the number of "suns" in the label which displays the number.
    */
    public void counterChange(){
	counter.setText("Counter: " + getSunCount());
    }

    /**
       Obtains the number of "Sunflower" plants currently on the field.
       @return the number of "Sunflower" on the grid
    */
    public int getSf(){
	return sunflowerNumber;
    }

    /**
       Applies changes towards the number of "Sunflower" plants in the game towards the variable keeping track of the number.
       @param sunflowerNumber  the number that the variable "sunflowerNumber" is meant to become
    */
    public void setSf(int sunflowerNumber){
	this.sunflowerNumber = sunflowerNumber;
    }

    /**
       Obtains the number of "zombies" that need to be killed before winning the game.
       @return the number of "zombies" that need to be defeated
    */
    public int getTarget() {
	return target;
    }

    /**
       Applies changes towards the number of "zombies" that still need to  be defeated.
       @param n  the number that the variable "target" is meant to become
    */
    public void setTarget(int n) {
	this.target = n;
    }

    /**
       Edits the label which states the game's current position (whether or not the player has won or lost and how many zombies still need to be played)
    */
    public void statusChange() {
	if (youLose) {
	    statusLabel = "Status: You lose";
	    isStarted = false;
	    isEnded = true;
	} else if (getTarget() <= 0) {
	    statusLabel = "Status: You win";
	    isStarted = false;
	    isEnded = true;
	} else {
	    statusLabel = "Status: " + getTarget() + " left";
	}
	status.setText(statusLabel);
    }
	
    /**
       Contains an action listener which will determine which radio buttons have been selected and perform that action on the grid. This is the planting mechanism and removing mechanism for the plants in this game.
    */
    private class PlantEdit implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    if (!youLose){
		JButton btn = (JButton) e.getSource();
		int col = (int) btn.getClientProperty("column");
		int row = (int) btn.getClientProperty("row");
		Object plantHere = btn.getClientProperty("plant");
		Object zombieHere = btn.getClientProperty("zombie");
		if (A.isSelected() && getSunCount()>=50 && (Integer) plantHere == 0 && (Integer) zombieHere == 0){
		    addPlant(col, row, 1);
		    setSunCount(getSunCount()-50);
		}

		if (B.isSelected() && getSunCount()>=100 && (Integer) plantHere == 0 && (Integer) zombieHere == 0){
		    addPlant(col, row, 2);
		    setSunCount(getSunCount()-100);
		}

		if (C.isSelected() && getSunCount()>=125 && (Integer) plantHere == 0 && (Integer) zombieHere == 0){
		    addPlant(col, row, 3);
		    setSunCount(getSunCount()-125);
		}
	    
		if (D.isSelected() && getSunCount()>=200 && (Integer) plantHere == 0 && (Integer) zombieHere == 0){
		    addPlant(col, row, 4);
		    setSunCount(getSunCount()-200);
		}
		if (r.isSelected()){
		    int type = (int) btn.getClientProperty("zombie");
		    btn.removeAll();		    
		    if((int) btn.getClientProperty("plant") == 1) {
			setSf(getSf()-1);
		    } else if ((int) btn.getClientProperty("plant") > 0) {
			btn.putClientProperty("plant", 0);
		    } else if ((int) btn.getClientProperty("zombie") > 0) {
			int newZombieHealth = (int) btn.getClientProperty("zombieHealth");
			addZombie(col, row, newZombieHealth, type);
		    }
		    btn.putClientProperty("plant", 0);
		    overall.repaint();
		    gameBoard.revalidate();
		}
		counterChange();
		overall.repaint();
	    }
	}
    }

    /**
       Contains an action listener that corresponds with the "Start" button. This is responsible for starting the game up.
    */
    private class Begin implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    if (!isStarted && isEnded) {
		isStarted = true;     
		isEnded = false;
		setSunCount(75);
		counterChange();
	    }
	}
    }

    /** 
	Contains an action listener which corresponds with the "Reset" button.This contains the method used to restart all progress in the game and to end the game.
    */
    private class End implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    setSunCount(0);
	    counterChange();
		
	    gameBoard.remove(play);
	    gameBoard.validate();
	    gameBoard.repaint();
		
	    gridMaker();
	    isStarted = false; 
	    isEnded = true;
	    youLose = false;
	    statusChange();
	}
    }
    
    /**
       Initializes the layout and 2D button array utilized in making the grid.Also feeds variables to signify coordinates into the method buttonMaker.
    */
    public void gridMaker() {	

	play = new JPanel(new GridLayout(5,9)); 
	grid = new JButton[9][5];
	int counterX = 0;
	int counterY = 0;
	for (int y = 0; y < 5; y++){
	    for (int x = 0; x < 9; x++){
		buttonMaker(x, y);
	    }
	}
	gameBoard.add(play);
    }

    /** 
	Uses the variables received from various methods to create the buttons in the grid and to provide them with some client properties.
	@param x  the column to place the button in
	@param y  the row to place the button in
    */
    public void buttonMaker(int x, int y) {
	grid[x][y] = new JButton(/*String.format("[%d,%d]", y, x)*/);  
	grid[x][y].setPreferredSize(new Dimension(125,125));
        grid[x][y].setContentAreaFilled(false);
	grid[x][y].addActionListener(new PlantEdit());
	grid[x][y].putClientProperty("column", x);
	grid[x][y].putClientProperty("row", y);
	grid[x][y].putClientProperty("zombie", 0);
	grid[x][y].putClientProperty("zombieHealth",-1);
	grid[x][y].putClientProperty("plant", 0);
	grid[x][y].putClientProperty("plantHealth", -1);
	grid[x][y].putClientProperty("projectile",0);
	grid[x][y].putClientProperty("cooldown", 0);
	grid[x][y].putClientProperty("parentAlive", 0);
	grid[x][y].setBackground(Color.WHITE);
	play.add(grid[x][y]);
    }
    
    /**
       Creates movement of zombies from one button to the next.
    */
    private class Zombie extends TimerTask{

	public void run(){
	    if (isStarted){
		moveZombie();
		statusChange();
		zombieMath += 0.5;
		if (!youLose && zombieMath == 1 && target > 25) {
		    addZombie(8, random.nextInt(5), 10, 1);
		    zombieNum += 1;
		    zombieMath = 0.0;
		} if (!youLose && zombieMath == 0.5 && target <= 25) {
		    addZombie(8, random.nextInt(5), 15, 2);
		    zombieNum++;
		    zombieMath = 0.0;
		} if (!youLose && zombieMath == 1 && target <= 25) {
		    addZombie(8, random.nextInt(5), 10, 1);
		    zombieNum++;
		    zombieMath = 0.0;
		}
	    }
	}
    }

    /**
       Determines whether or not a projectile should no longer be displayed based on the status of the shooter it is derived from.
    */
    private class killProjs extends TimerTask {
	public void run() {
	    if (isStarted) {
		killProjectiles();
	    }
	}
    }

    /**
       Places a projectile onto the screen based on whether or not a shooter plant has been planted.
    */ 
    private class ProjectileSet extends TimerTask{
	public void run(){
	    if (isStarted){
		for (int y = 4; y>=0; y--){
		    for (int x = 8; x>=0; x--){
			int set = 0;
			if ((int) grid[x][y].getClientProperty("plant") == 2){
			    set = 1;
			}
			if ((int) grid[x][y].getClientProperty("plant") == 4){
			    set = 2;
			}
			if (set == 1 || set == 2) {
			    addProjectile(x,y,set);
			}
			if (zombieDie(x,y)){
			    grid[x][y].removeAll();
			    grid[x][y].putClientProperty("zombie", 0);
			}
		    }
		}
	    }
	}
    }
    /**
       Moves an existing projectile from one button to another.
    */
    private class ProjectileMove extends TimerTask{
	public void run(){
	    if (isStarted){
		for (int y = 4; y>=0; y--){
		    for (int x = 8; x>=0; x--){
		        moveProjectile(x,y);
			if (zombieDie(x,y)){
			    grid[x][y].removeAll();
			    grid[x][y].putClientProperty("zombie", 0);
			}
		    }
		}
	    }
	}
    }

    /**
       Changes the sun counter regularly.
    */   
    private class SunUpdate extends TimerTask{

	public void run(){
	    if (isStarted){
		setSunCount(getSunCount() + ((getSf()/2)+1)*25);
		counterChange(); 
	    }
	}
    }

    /**
       Allows the chomper to perform its ability at regular intervals.
    */
    private class Chomper extends TimerTask {
	public void run() {
	    if (isStarted) {
		chomp();
	    }
	}
    }
    /**
       Places a certain type of plant on a button depending on the radio buttons selected.
       @param column  the column to place the plant in
       @param row  the row to place the plant in
       @param type  the kind of plant to place
       Key: 1 = Sunflower, 2 = Pea Shooter, 3 = Chomper, 4 = Gatling Pea Shooter
    */
    public void addPlant(int column, int row, int type) {

	if (type == 1){
	    JLabel label = new JLabel();
	    label.setIcon(new ImageIcon("Sunflower.png"));
	    grid[column][row].add(label);
	    gameBoard.revalidate();
	    overall.repaint();
	    counterChange();
	    setSf(getSf()+1);
	    grid[column][row].putClientProperty("plant", 1);
	}

	if (type == 2){
	    JLabel label1 = new JLabel();
	    label1.setIcon(new ImageIcon("ShooterOne.png"));
	    grid[column][row].add(label1);
	    gameBoard.revalidate();
	    overall.repaint();
	    counterChange();
	    grid[column][row].putClientProperty("plant", 2);
	}

	if (type == 3){
	    JLabel label2 = new JLabel();
	    label2.setIcon(new ImageIcon("Chomper.png"));
	    grid[column][row].add(label2);
	    grid[column][row].putClientProperty("cooldown", 0);
	    gameBoard.revalidate();
	    overall.repaint();
	    counterChange();
	    grid[column][row].putClientProperty("plant", 3);
	}

	if (type == 4){
	    JLabel label3 = new JLabel();
	    label3.setIcon(new ImageIcon("GatlingPea.png"));
	    grid[column][row].add(label3);
	    gameBoard.revalidate();
	    overall.repaint();
	    counterChange();
	    grid[column][row].putClientProperty("plant", 4);
	}
    }

    /**
       Adds "zombies" into the game field from the right.
       @param column  the column to place the "zombie" in
       @param row  the row to place the "zombie" in
       @param health  the amount of endurance the "zombie" should have and the number of hits the "zombie" can receive before disappearing
       @param type  the kind of zombie that should be added
       Key: 1 = Normal, 2 = Conehead
    */
    public void addZombie(int column, int row, int health, int type){
	JLabel image2 = new JLabel();
	if (type == 1 && (int) grid[column][row].getClientProperty("zombie") == 0) {
	    image2.setIcon(new ImageIcon("Zombie1.png"));
	    grid[column][row].add(image2);
	    gameBoard.revalidate();               
	    grid[column][row].putClientProperty("zombie", 1);
	} if (type == 2 && (int) grid[column][row].getClientProperty("zombie") == 0) {
	    image2.setIcon(new ImageIcon("conehead.png"));
            grid[column][row].add(image2);
            gameBoard.revalidate();      
            grid[column][row].putClientProperty("zombie", 2);
	}
	grid[column][row].putClientProperty("zombieHealth", health);
	overall.repaint();
    }

    /**
       Creates movement for zombies which is utilized in the Move class
    */
    public void moveZombie(){
	for (int y = 0; y<5; y++){
	    for (int x = 0; x<9; x++){
		if((Integer) grid[x][y].getClientProperty("zombie")>=1 && x-1 >= 0){
		    System.out.println(x+" "+y);
		    int type = (int) grid[x][y].getClientProperty("zombie");
		    if ((Integer) grid[x-1][y].getClientProperty("plant") > 0) {
			grid[x-1][y].removeAll();
			grid[x-1][y].putClientProperty("plant", 0);
			grid[x-1][y].putClientProperty("plantHealth",0);
			addZombie(x-1, y, (Integer) grid[x][y].getClientProperty("zombieHealth"), type);
                        grid[x][y].removeAll();                                                          
                        grid[x][y].putClientProperty("zombie", 0);
                        grid[x][y].putClientProperty("zombieHealth", 0);
		    } else {
			addZombie(x-1, y, (Integer) grid[x][y].getClientProperty("zombieHealth"), type);
			grid[x][y].removeAll();
			grid[x][y].putClientProperty("zombie", 0);
			grid[x][y].putClientProperty("zombieHealth", 0);
		    }
		} if((Integer) grid[x][y].getClientProperty("zombie")>=1 && x-1 < 1) {
		    youLose = true;
		} 
	    }	
	}
	gameBoard.revalidate();
	overall.repaint();
    }

    /** 
	Creates the projectile in front of a shooter plant.
	@param column  the column to place the projectile in
	@param row  the row to place the projectile in
	@param projectileSet  the strength the projectile should have
    */
    public void addProjectile(int column, int row, int projectileSet){
	if (column < 8){
	    grid[column+1][row].putClientProperty("parentAlive", 100+(column*10)+row);
	    JLabel projimg = new JLabel();
	    projimg.setIcon(new ImageIcon("projectile.png"));
	    grid[column+1][row].add(projimg);
	    grid[column+1][row].putClientProperty("projectile", projectileSet);
	    gameBoard.revalidate();               
	}
	overall.repaint();
    }

    /**
       Moves existing projectiles to the right and applies effects on 
       zombies if applicable in a situation.
       @param column  the column the projectile is currently on
       @param row  the row the projectile is currently on
    */
    public void moveProjectile(int column, int row){
	int x = column;
	int y = row;
	int newZombieHealth = (int) grid[x][y].getClientProperty("zombieHealth") - (int) grid[x][y].getClientProperty("projectile");
	int plantType = (int) grid[x][y].getClientProperty("plant");
	int type = (int) grid[x][y].getClientProperty("zombie");
	if ((Integer) grid[x][y].getClientProperty("parentAlive") > 0) {
	    if ((Integer) grid[x][y].getClientProperty("projectile")>0 && x < 8){
		addProjectile(x, y, (int) grid[x][y].getClientProperty("projectile"));
		grid[x+1][y].putClientProperty("parentAlive",(int) grid[x][y].getClientProperty("parentAlive"));
		grid[x][y].removeAll(); 
		if ((Integer) grid[x][y].getClientProperty("zombie") > 0) {
		    addZombie(x, y, newZombieHealth, type);
		} if (plantType > 0) {
		    addPlant(x, y, plantType);
		}
		grid[x][y].putClientProperty("projectile", 0);
		grid[x][y].putClientProperty("parentAlive", 0);
	    }
	} else if ((int) grid[x][y].getClientProperty("projectile")>0 && x == 8){
	    grid[x][y].removeAll();
	    if ((int) grid[x][y].getClientProperty("zombie")>0){
		addZombie(x,y,newZombieHealth, type);
	    } if (plantType > 0) {
		addPlant(x, y, plantType);
	    }
	    grid[x][y].putClientProperty("projectile", 0);
	    grid[x][y].putClientProperty("parentAlive", 0);
	}
	overall.repaint();
	gameBoard.revalidate();
    }
    
    /** 
	Allows chomper plants to defeat zombies in front of them and placing limits on their ability to do so.
    */
    public void chomp() {
	for (int row = 0; row < 5; row++) {
	    for (int column = 0; column < 9; column++) {
		if ((int) grid[column][row].getClientProperty("plant") == 3) {
		    System.out.println((int) grid[column][row].getClientProperty("cooldown"));
		    if ((int) grid[column][row].getClientProperty("cooldown") > 0) {
			grid[column][row].putClientProperty("cooldown", (int) grid[column][row].getClientProperty("cooldown")-1);
		    } else if ((int) grid[column][row].getClientProperty("cooldown") == 0) {
			if ((int) grid[column+1][row].getClientProperty("zombie") > 0) {
			    grid[column+1][row].putClientProperty("zombie", 0);
			    grid[column+1][row].putClientProperty("zombieHealth", 0);
			    grid[column+1][row].removeAll();
			    grid[column+1][row].putClientProperty("cooldown", 3);
			    setTarget(getTarget() - 1);
			    statusChange();
			}
		    }
		}
	    }
	}
    }

    /**
       Gets rid of any projectiles coming from a shooter that has been eliminated.
    */
    public void killProjectiles() {
	for (int y = 0; y<5; y++){
            for (int x = 0; x<9; x++){
		if ((int) grid[x][y].getClientProperty("parentAlive") - 100 >= 0 && (int) grid[x][y].getClientProperty("projectile") > 0) {
		    int cc = ((int) grid[x][y].getClientProperty("parentAlive") - 100)/10;
		    int rr = ((int) grid[x][y].getClientProperty("parentAlive") - 100)%10;
		    if ((int) grid[cc][rr].getClientProperty("plant") == 0 || zombieBehind(x,y) == true || (int) grid[x][y].getClientProperty("zombie") > 0) {
			grid[x][y].putClientProperty("parentAlive", 0);
		    }
		}
	    }
        }
    }

    /**
       Determines if a projectile has passed beyond a zombie it should have hit.
       @param col  the column the projectile is in
       @param row  the row the projectile is in
       @return the boolean which determines if the projectile has passed a zombie
    */
    public boolean zombieBehind(int col, int row) {
	boolean zombehind = false;
	for (int x = 0; x < col; x++) {
	    if ((int) grid[x][row].getClientProperty("zombie") > 0) {
		zombehind = true;
	    }
	}
	return zombehind;
    }

    /**
       Establishes whether or not the image on a button should disappear.
       @param column  the column the zombie is on
       @param row  the row the zombie is on
       @return the boolean which determines whether or not the object disappears
    */
    public boolean zombieDie(int column, int row) {
	boolean die = false;
	if ((Integer) grid[column][row].getClientProperty("zombieHealth") == 0 && (Integer) grid[column][row].getClientProperty("zombie") >= 1) {
	    die = true;
	    setTarget(getTarget()-1);
	    statusChange();
	}
	return die;
    }
}
