/** 
    The NotPvZ program creates a game interface meant to simulate a 
    simplified version of the game Plants vs.&nbsp; Zombies.
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

    private JLabel counter, image, status;
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
    private boolean isStarted, isEnded;
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

	counter = new JLabel("Counter", JLabel.CENTER);
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

	status = new JLabel("Status", JLabel.LEFT);
	//status.setText(statusChange);
	overall.add(status, BorderLayout.EAST);

	timer.scheduleAtFixedRate(new Move(), 0, 1000);

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
	if (youLose == true) {
	    statusLabel = "Status: You lose";
	} else if (getTarget() == 0) {
	    statusLabel = "Status: You win";
	} else {
	    statusLabel = "Status: " + getTarget();
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
	    JButton btn = (JButton) e.getSource();
	    Object plantHere = btn.getClientProperty("plant");
	    Object zombieHere = btn.getClientProperty("zombie");
	    if (A.isSelected() && getSunCount()>=50 && (Integer) plantHere == 0 && (Integer) zombieHere == 0){
		    JLabel label = new JLabel();
		    label.setIcon(new ImageIcon("Sunflower.png"));
		    btn.add(label);
		    gameBoard.revalidate();
		    overall.repaint();
		    setSunCount(getSunCount()-50);
		    counterChange();
		    setSf(getSf()+1);
		    btn.putClientProperty("plant", 1);
	    }

	    if (B.isSelected() && getSunCount()>=100 && (Integer) plantHere == 0 && (Integer) zombieHere == 0){
		JLabel label1 = new JLabel();
		label1.setIcon(new ImageIcon("ShooterOne.png"));
		btn.add(label1);
		gameBoard.revalidate();
		overall.repaint();
		setSunCount(getSunCount()-100);
		counterChange();
		btn.putClientProperty("plant", 2);
	    }

	    if (C.isSelected() && getSunCount()>=125 && (Integer) plantHere == 0 && (Integer) zombieHere == 0){
		JLabel label2 = new JLabel();
                label2.setIcon(new ImageIcon("Chomper.png"));
                btn.add(label2);
                gameBoard.revalidate();
                //overall.repaint();                                                             
                setSunCount(getSunCount()-125);
                counterChange();
                btn.putClientProperty("plant", 3);
            }
	    
	    if (D.isSelected() && getSunCount()>=200 && (Integer) plantHere == 0 && (Integer) zombieHere == 0){
                JLabel label3 = new JLabel();
		label3.setIcon(new ImageIcon("GatlingPea.png"));
                btn.add(label3);
                gameBoard.revalidate();
                overall.repaint();                                                             
                setSunCount(getSunCount()-200);
                counterChange();
		btn.putClientProperty("plant", 4);
            }
	    if (r.isSelected()){
		if((int) btn.getClientProperty("plant") == 1) {
		    setSf(getSf()-1);
		}
		btn.removeAll();
		gameBoard.revalidate();
	    }
	    
	    JButton btn1 = (JButton) e.getSource();
	    System.out.println("clicked column " + btn1.getClientProperty("column") + ", row " + btn1.getClientProperty("row"));
	    System.out.println(btn1.getClientProperty("column"));
	    btn1.validate();
	    overall.repaint();
	}
    }

    private class Begin implements ActionListener{
	/**
	   Contains an action listener that corresponds with the "Start" 
	   button. This is responsible for starting the game up.
	*/
	public void actionPerformed(ActionEvent e){
	    if (isStarted == false && isEnded == true) {
		isStarted = true;     
		isEnded = false;
		setSunCount(10000);
		counterChange();
		
		addZombie(8, random.nextInt(5), 10);
		//zombieMove();
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
	    if (isEnded == false && isStarted == true) {
		setSunCount(0);
		counterChange();
		
		gameBoard.remove(play);
		gameBoard.validate();
		gameBoard.repaint();
		
		gridMaker();
		isStarted = false;
		isEnded = true;
	    }
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
	image = new JLabel();
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
	grid[x][y] = new JButton(String.format("[%d,%d]", y, x));
	grid[x][y].add(image);                                                                                                           grid[x][y].setPreferredSize(new Dimension(125,125));
	grid[x][y].addActionListener(new PlantEdit());
	grid[x][y].putClientProperty("column", x);
	grid[x][y].putClientProperty("row", y);
	grid[x][y].putClientProperty("zombie", 0);
	grid[x][y].putClientProperty("zombieHealth",-1);
	grid[x][y].putClientProperty("plant", 0);
	grid[x][y].putClientProperty("plantHealth", -1);
	play.add(grid[x][y]);
    }
    
    public static void wait(int n) {
	try {
	    Thread.sleep(n);
	} catch (Exception e) {}
    }
    

    
    private class Move extends TimerTask{
	/**
	   Creates movement of images from one button to the next.
	*/

	public void run(){
	    //statusChange();
	    zombieMove();
            statusChange();
	    zombieMath += 0.5;
	    if (zombieNum <= 5 && zombieMath == 1) {
		addZombie(8, random.nextInt(5), 10);
		zombieNum += 1;
		zombieMath = 0.0;
	    }
	    setSunCount(getSunCount() + ((getSf()/2)+1)*25);
	    counterChange(); 
	}
    }

    
    private class Projectile {
	private String projectile = "projectile.png";

	private int dx, dy, x, y;
	private Image image;

	public Projectile() {
	    ImageIcon img = new ImageIcon(this.getClass().getResource(projectile));
	    image = img.getImage();
	    x=40;
	    y=60;
	}
	public void move() {
	    x+=dx;
	    y+=dy;
	}
	public int getX() {
	    return x;
	}
	public int getY() {
	    return y;
	}
	public Image getImage() {
	    return image;
	}
	/*
	public boolean isVisible() {
	    return visible;
	}
	*/
	
    }
    //ClassLoader cl = this.getClass().getClassLoader();

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
	//Image img = ImageIO.read(getClass().getResource("Zombie.png"));
	//ImageIcon img = new ImageIcon(getClass().getResource("Zombie.png"));
	//grid[column][row].setIcon(img);
	gameBoard.revalidate();               
	grid[column][row].putClientProperty("zombie", 1);
	grid[column][row].putClientProperty("zombieHealth", health);
    }

    
    public void zombieMove(){
	/**
	   Creates movement for zombies which is utilized in the Move class
	*/
	for (int y = 0; y<5; y++){
	    for (int x = 0; x<9; x++){
		if((Integer) grid[x][y].getClientProperty("zombie")==1 && x-1 >= 0){
		    //System.out.println(x+" "+y);
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
			//grid[x][y].setIcon(null);
			grid[x][y].putClientProperty("zombie", 0);
			grid[x][y].putClientProperty("zombieHealth", 0);
			//gameBoard.revalidate();
		    }
		} if((Integer) grid[x][y].getClientProperty("zombie")==1 && x-1 < 1) {
		    youLose = true;
		} //else System.out.println("clear");
	    }	
	}
	gameBoard.revalidate();
	overall.repaint();
	//revalidate
    }
    
    //don't know if needed??
    public boolean toDie(JButton button) {
	boolean die = false;
	if ((Integer) button.getClientProperty("zombieHealth") == 0) {
	    die = true;
	} else if ((Integer) button.getClientProperty("plantHealth") == 0) {
	    die = true;
	}/* else if ((Integer) button.getClientProperty("")) {

}*/
	return die;
    }


    public void populate() {
	ImageIcon ShooterOne = new ImageIcon("ShooterOne.png");
	//play1.getValueAt(0,0) = new JButton("plant");
	//play1.setIcon(ShooterOne);
    }
    


}
