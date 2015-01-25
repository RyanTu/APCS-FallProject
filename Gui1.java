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



    public Gui1() {
	/**
	   Gui1 is the constructor responsible for making the game display. Within
	   it are many methods which create the game board seen when playing. 
	*/

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
	
        timer.scheduleAtFixedRate(new Zombie(), 0, 4000);
	timer.scheduleAtFixedRate(new ProjectileSet(), 0, 2500);
	timer.scheduleAtFixedRate(new ProjectileMove(), 0, 1000);
	timer.scheduleAtFixedRate(new SunUpdate(), 0, 1500);
	timer.scheduleAtFixedRate(new Chomper(), 0, 1000);

	overall.pack();
	overall.setVisible(true);

    }

    public int getSunCount(){
	/**
	   Obtains the current number of "suns" (currency used to insert various
	   plants) available.
	   @return the number of "suns" currently in the game
	*/
	return sunCount;
    }

    public void setSunCount(int sunCount) {
	/** 
	    Applies changes towards the number of "suns" in the game towards the
	    variable holding the number.
	    @param sunCount  the number that the variable "sunCount" is meant to 
	    become
	*/
        this.sunCount = sunCount;
    }

    public void counterChange(){
	/**
	   Reflects any change in the variable holding the number of "suns" in
	   the label which displays the number.
	*/
	counter.setText("Counter: " + getSunCount());
    }

    public int getSf(){
	/**
	   Obtains the number of "Sunflower" plants currently on the field.
	   @return the number of "Sunflower" on the grid
	*/
	return sunflowerNumber;
    }

    public void setSf(int sunflowerNumber){
	this.sunflowerNumber = sunflowerNumber;
    }

    //number of zombies left to kill
    public int getTarget() {
	/**
	   Obtains the number of "zombies" that need to be killed before winning
	   the game.
	   @return the number of "zombies" that need to be defeated
	*/
	return target;
    }

    public void setTarget(int n) {
	this.target = n;
	/**
	   Applies changes towards the number of "zombies" that still need to 
	   be defeated.
	   @param n  the number that the variable "target" is meant to become
	*/
    }

    public void statusChange() {
	/**
	   Edits the label which states the game's current position (whether or
	   not the player has won or lost and how many zombies still need to be
	   played)
	*/
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
	
    private class PlantEdit implements ActionListener{
	/**
	   Contains an action listener which will determine which radio 
	   buttons have been selected and perform that action on the grid. This
	   is the planting mechanism and removing mechanism for the plants in 
	   this game.
	*/
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
		    if((int) btn.getClientProperty("plant") == 1) {
			setSf(getSf()-1);
		    }
		    btn.removeAll();
		    overall.repaint();
		    gameBoard.revalidate();
		}
		System.out.println("Plant: " + btn.getClientProperty("plant"));
		System.out.println("Zombie: " + btn.getClientProperty("zombie"));
		System.out.println("ZombieHealth: " + btn.getClientProperty("zombieHealth"));
	    }
	}
    }

    private class Begin implements ActionListener{
	/**
	   Contains an action listener that corresponds with the "Start" 
	   button. This is responsible for starting the game up.
	*/
	public void actionPerformed(ActionEvent e){
	    if (!isStarted && isEnded) {
		isStarted = true;     
		isEnded = false;
		setSunCount(75);
		counterChange();
	    }
	}
    }

    private class End implements ActionListener{
	/** 
	    Contains an action listener which corresponds with the "Reset" 
	    button.This contains the method used to restart all progress in 
	    the game and to end the game.
	*/
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
    
    public void gridMaker() {	
	/**
	   Initializes the layout and 2D button array utilized in making the 
	   grid.Also feeds variables to signify coordinates into the method 
	   buttonMaker.
	*/

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

    public void buttonMaker(int x, int y) {
	/** 
	    Uses the variables received from various methods to create the 
	    buttons in the grid and to provide them with some client 
	    properties.
	    @param x  the column to place the button in
	    @param y  the row to place the button in
	*/
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
	grid[x][y].setBackground(Color.white);
	play.add(grid[x][y]);
    }
    
    public static void wait(int n) {
	try {
	    Thread.sleep(n);
	} catch (Exception e) {}
    }
    
    private class Zombie extends TimerTask{
	/**
	   Creates movement of zombies from one button to the next.
	*/

	public void run(){
	    if (isStarted){
	        moveZombie();
		statusChange();
		zombieMath += 0.5;
		if (!youLose && zombieMath == 1) {
		    addZombie(8, random.nextInt(5), 10);
		    zombieNum += 1;
		zombieMath = 0.0;
		}
	    }
	}
    }

    
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
			addProjectile(x,y,set);
			if (zombieDie(x,y)){
			    grid[x][y].removeAll();
			    grid[x][y].putClientProperty("zombie", 0);
			}
		    }
		}
	    }
	}
    }

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
    
    private class SunUpdate extends TimerTask{
	public void run(){
	    if (isStarted){
		setSunCount(getSunCount() + ((getSf()/2)+1)*25);
		counterChange(); 
	    }
	}
    }

    private class Chomper extends TimerTask {
	public void run() {
	    if (isStarted) {
		chomp();
	    }
	}
    }

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
	    grid[column][row].putClientProperty("cooldown", 3);
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

    public void addZombie(int column, int row, int health){
	/**
	   Adds "zombies" into the game field from the right.
	   @param column  the column to place the "zombie" in
	   @param row  the row to place the "zombie" in
	   @param health  the amount of endurance the "zombie" should have and 
	   the number of hits the "zombie" can receive before disappearing
	*/
	JLabel image2 = new JLabel();
	image2.setIcon(new ImageIcon("Zombie1.png"));
	grid[column][row].add(image2);
	gameBoard.revalidate();               
	grid[column][row].putClientProperty("zombie", 1);
	grid[column][row].putClientProperty("zombieHealth", health);
	overall.repaint();
    }

    
    public void moveZombie(){
	/**
	   Creates movement for zombies which is utilized in the Move class
	*/
	for (int y = 0; y<5; y++){
	    for (int x = 0; x<9; x++){
		if((Integer) grid[x][y].getClientProperty("zombie")>=1 && x-1 >= 0){
		    System.out.println(x+" "+y);
		    if ((Integer) grid[x-1][y].getClientProperty("plant") > 0) {
			grid[x-1][y].removeAll();
			grid[x-1][y].putClientProperty("plant", 0);
			grid[x-1][y].putClientProperty("plantHealth",0);
			addZombie(x-1, y, (Integer) grid[x][y].getClientProperty("zombieHealth"));
                        grid[x][y].removeAll();                                                          
                        grid[x][y].putClientProperty("zombie", 0);
                        grid[x][y].putClientProperty("zombieHealth", 0);
		    } else {
			addZombie(x-1, y, (Integer) grid[x][y].getClientProperty("zombieHealth"));
			grid[x][y].removeAll();
			grid[x][y].putClientProperty("zombie", 0);
			grid[x][y].putClientProperty("zombieHealth", 0);
		    }
		} if((Integer) grid[x][y].getClientProperty("zombie")==1 && x-1 < 1) {
		    youLose = true;
		} 
	    }	
	}
	gameBoard.revalidate();
	overall.repaint();
    }

    public void addProjectile(int column, int row, int projectileSet){
	if (column < 8){
	    JLabel projimg = new JLabel();
	    projimg.setIcon(new ImageIcon("projectile.png"));
	    if (projectileSet == 1){
		grid[column+1][row].add(projimg);
		grid[column+1][row].putClientProperty("projectile", projectileSet);
	    gameBoard.revalidate();               
	    }
	    if (projectileSet == 2){
		grid[column+1][row].add(projimg);
		grid[column+1][row].putClientProperty("projectile", projectileSet);
		gameBoard.revalidate();
	    }
	}
	overall.repaint();
    }

    public void moveProjectile(int column, int row){
	int x = column;
	int y = row;
	int newZombieHealth = (int) grid[x][y].getClientProperty("zombieHealth") - (int) grid[x][y].getClientProperty("projectile");
	int plantType = (int) grid[x][y].getClientProperty("plant");
	if((Integer) grid[x][y].getClientProperty("projectile")>0 && x < 8){
	    addProjectile(x, y, (int) grid[x][y].getClientProperty("projectile"));
	    grid[x][y].removeAll();                                                          
	    if ((Integer) grid[x][y].getClientProperty("zombie") > 0) {
		addZombie(x, y, newZombieHealth);
            } if (plantType > 0) {
		addPlant(x, y, plantType);
	    }
	    grid[x][y].putClientProperty("projectile", 0);
	}
	if ((int) grid[x][y].getClientProperty("projectile")>0 && x == 8){
	    grid[x][y].removeAll();
	    if ((int) grid[x][y].getClientProperty("zombie")>0){
	        addZombie(x,y,(int) grid[x][y].getClientProperty("zombieHealth"));
	    } if (plantType > 0) {
		addPlant(x, y, plantType);
	    }
	}
	overall.repaint();
	gameBoard.revalidate();
    }
    
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
