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
    private boolean isStarted;
    private boolean youLose = false;

    public Timer timer = new Timer();
    public int speed = 1000;



    public Gui1() {

	overall = new JFrame();
	overall.setSize(600,600);
	overall.setDefaultCloseOperation(EXIT_ON_CLOSE);
	overall.setLayout(new BorderLayout());
	overall.setTitle("Plants vs. Zombies");

	top = new JPanel();
	top.setLayout(new GridLayout());
	overall.add(top, BorderLayout.PAGE_START);

	counter = new JLabel("Counter: 0", JLabel.CENTER);
	top.add(counter);

        A = new JRadioButton("Sunflower (25 suns)");
	//A.setActionCommand("A");
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
        
	/*

	play1.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
		    if (e.getClickCount() == 2) {
			JTable target = (JTable)e.getSource();
			int row = target.getSelectedRow();
			int column = target.getSelectedColumn();
			sunCount += 5;//do something here
			counterChange();
		    }
		}
	    });
	
	*/


	status = new JLabel(statusChange(), JLabel.CENTER);
	overall.add(status, BorderLayout.EAST);

	timer.scheduleAtFixedRate(new Move(), 0, 5000);

	overall.pack();
	overall.setVisible(true);

    }

    public int getSunCount(){
	return sunCount;
    }

    public void setSunCount(int sunCount) {
        this.sunCount = sunCount;
    }

    public void counterChange(){
	counter.setText("Counter: " + getSunCount());
    }

    public int getSf(){
	return sunflowerNumber;
    }

    public void setSf(int sunflowerNumber){
	this.sunflowerNumber = sunflowerNumber;
    }

    public void getImage(){

    }

    public void setImage(){

    }

    //number of zombies left to kill
    public int getTarget() {
	return target;
    }

    public void setTarget(int n) {
	this.target = n;
    }

    public String statusChange() {
	if (youLose == true) {
	    return "Status: You lose";
	} else if (getTarget() == 0) {
	    return "Status: You win";
	} else {
	    return "Status: " + getTarget();
	}
    }
	
    private class PlantEdit implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    JButton btn = (JButton) e.getSource();
	    Object plantHere = btn.getClientProperty("plant");
	    Object zombieHere = btn.getClientProperty("zombie");
	    //	    JLabel image = new JLabel();
	    if (A.isSelected() && getSunCount()>=50 && (Integer) plantHere == 0 && (Integer) zombieHere == 0){
		//if ((Integer) btn.getClientProperty("plant") != 0) {
		    JLabel label = new JLabel();
		    label.setIcon(new ImageIcon("Sunflower.png"));
		    btn.add(label);
		    gameBoard.revalidate();
		    //overall.repaint();
		    setSunCount(getSunCount()-50);
		    counterChange();
		    setSf(getSf()+1);
		    //btn.putClientProperty("sunflower", 1);
		    btn.putClientProperty("plant", 1);//1=sunflower	    
		    //}
	    }

	    if (B.isSelected() && getSunCount()>=100 && (Integer) plantHere == 0 && (Integer) zombieHere == 0){
		JLabel label1 = new JLabel();
		label1.setIcon(new ImageIcon("ShooterOne.png"));
		btn.add(label1);
		gameBoard.revalidate();
		//overall.repaint();
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
                //overall.repaint();                                                             
                setSunCount(getSunCount()-200);
                counterChange();
		btn.putClientProperty("plant", 4);
            }
	    if (r.isSelected()){
		//play.remove(btn);
		//buttonMaker((Integer) btn.getClientProperty("column"), (Integer) btn.getClientProperty("row"));
		//play.remove(btn);
		//grid[btn.getClientProperty("column")][btn.getClientProperty("row")] = new JButton;
		
		//image.setIcon(null);
		btn.removeAll();
		gameBoard.revalidate();
	    }
	    
	    JButton btn1 = (JButton) e.getSource();
	    System.out.println("clicked column " + btn1.getClientProperty("column") + ", row " + btn1.getClientProperty("row"));
	    //btn1.putClientProperty("column", (int)btn1.getClientProperty("column")+1);
	    System.out.println(btn1.getClientProperty("column"));
	    btn1.validate();
	    overall.repaint();
	}
    }

    private class Begin implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    if (isStarted == false) {
		isStarted = true;           
		setSunCount(10000);
		counterChange();
		
		addZombie(8, random.nextInt(5), 10);
		//zombieMove();
	    }
	    /*   
	    t.scheduleAtFixedRate(new TimerTask() {
		    public void run() {
		    }
		}, 2*60*1000, 2*60*1000);	
            */
	}
    }

    private class End implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    setSunCount(0);
	    counterChange();
	    
	    gameBoard.remove(play);
	    gameBoard.validate();
	    gameBoard.repaint();
	    
	    gridMaker();
	    isStarted = false;
	    //timer.reset();
	}
    }
    
    public void gridMaker() {	
	play = new JPanel(new GridLayout(5,9)); 
	grid = new JButton[9][5];
	image = new JLabel();
	int counterX = 0;
	int counterY = 0;
	for (int y = 0; y < 5; y++){
	    for (int x = 0; x < 9; x++){
		buttonMaker(x, y);

		//JLabel test = new JLabel();
		//String text = String.format("[%d, %d]", y, x);
		
		//grid[x][y] = new JButton(/*text*/);
		/*grid[x][y].add(image);
		grid[x][y].setContentAreaFilled(false);
		grid[x][y].setPreferredSize(new Dimension(125,125));
		grid[x][y].addActionListener(new PlantEdit());
		grid[x][y].putClientProperty("column", x);
		grid[x][y].putClientProperty("row", y);
		play.add(grid[x][y];*/
		/*
		  if (counterX < 8){
		counterX++;
		    } else {
		counterX = 0;
		    counterY++;
		    }
		*/
	    }
	}
	gameBoard.add(play);
    }

    public void buttonMaker(int x, int y) {
	grid[x][y] = new JButton(String.format("[%d,%d]", y, x));
	grid[x][y].add(image);                                                                                          
	//grid[x][y].setContentAreaFilled(false);                                                                         
	grid[x][y].setPreferredSize(new Dimension(125,125));
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
	public void run(){
	    statusChange();
	    zombieMove();
	    // statusChange();
	    /*
	    setSunCount(getSunCount() + 25);
	    counterChange();
	    sunMath += 0.5;
	    */
            zombieMath += 0.5;
	    if (zombieNum <= 5 && zombieMath == 1) {
		addZombie(8, random.nextInt(5), 10);
		zombieNum += 1;
		zombieMath = 0.0;
	    }
	    int sunflower = 0;
	    for (int y = 0; y<5; y++){
		for (int x = 0; x<9; x++){
		    if ((Integer) grid[x][y].getClientProperty("plant") == 1) {
			sunflower++;
		    }
		}
	    }
	    setSunCount(getSunCount() + ((sunflower/2)+1)*25);
	    counterChange(); 
	    /* 
	    if (sunMath >= 1) {
		setSunCount(getSunCount() + 25);
		sunMath = 0;
	    }
	    */
	    //sunMath += 0.2;
	    //zombieMath += 0.5;
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
	public boolean isVisible() {
	    return visible;
	}
	
    }
    //ClassLoader cl = this.getClass().getClassLoader();

    public void addZombie(int column, int row, int health){
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
	for (int y = 0; y<5; y++){
	    for (int x = 0; x<9; x++){
		if((Integer) grid[x][y].getClientProperty("zombie")==1 && x-1 >= 0){
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
			//grid[x][y].setIcon(null);
			grid[x][y].putClientProperty("zombie", 0);
			grid[x][y].putClientProperty("zombieHealth", 0);
			//gameBoard.revalidate();
		    }
		} if((Integer) grid[x][y].getClientProperty("zombie")==1 && x-1 < 0) {
		    youLose = true;
		} else System.out.println("clear");
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
