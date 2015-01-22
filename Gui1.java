import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.image.*;
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

    private JLabel counter, image; 
    private ButtonGroup selection;
    private JRadioButton A,B,C,D,r;

    private JButton[][] grid;

    private JButton start, reset;

    public int sunCount = 0;
    public int sunflowerNumber = 0;

    private Random random = new Random();   
    private boolean isStarted;

    public Timer timer = new Timer();
    public int speed = 1000;

    public Gui1() {

	overall = new JFrame();
	overall.setSize(600,600);
	overall.setDefaultCloseOperation(EXIT_ON_CLOSE);
	overall.setLayout(new BorderLayout());

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


	timer.scheduleAtFixedRate(new Move(), 0, 500);

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
	
    private class PlantEdit implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    JButton btn = (JButton) e.getSource();
	    //	    JLabel image = new JLabel();
	    if (A.isSelected() && getSunCount()>=50){
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon("Sunflower.png"));
		btn.add(label);
		gameBoard.revalidate();
		//overall.repaint();
		setSunCount(getSunCount()-50);
		counterChange();
		setSf(getSf()+1);
		btn.putClientProperty("sunflower", 1);
	    }

	    if (B.isSelected() && getSunCount()>=100){
		image.setIcon(new ImageIcon("ShooterOne.png"));
		btn.add(image);
		gameBoard.revalidate();
		//overall.repaint();
		setSunCount(getSunCount()-100);
		counterChange();
		btn.putClientProperty("peashooter", 1);
	    }

	    if (C.isSelected() && getSunCount()>=125){
                image.setIcon(new ImageIcon("Chomper.png"));
                btn.add(image);
                gameBoard.revalidate();
                //overall.repaint();                                                             
                setSunCount(getSunCount()-125);
                counterChange();
                btn.putClientProperty("chomper", 1);
            }
	    
	    if (D.isSelected() && getSunCount()>=200){
                image.setIcon(new ImageIcon("GatlingPea.png"));
                btn.add(image);
                gameBoard.revalidate();
                //overall.repaint();                                                             
                setSunCount(getSunCount()-200);
                counterChange();
		btn.putClientProperty("gatlingpea", 1);
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
		setSunCount(75);
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
	play.add(grid[x][y]);
    }
    
    public static void wait(int n) {
	try {
	    Thread.sleep(n);
	} catch (Exception e) {}
    }
    

    
    private class Move extends TimerTask{
	public void run(){
	    zombieMove();
	}
    }

    public void addZombie(int column, int row, int health){
	JLabel image2 = new JLabel();
	//int roww = random.nextInt(5);
	image2.setIcon(new ImageIcon("Zombie1.png"));
	grid[column][row].add(image2);
	gameBoard.revalidate();               
	grid[column][row].putClientProperty("zombie", 1);
	grid[column][row].putClientProperty("zombieHealth", health);
    }

    
    public void zombieMove(){
	for (int y = 0; y<5; y++){
	    for (int x = 0; x<9; x++){
		if((Integer) grid[x][y].getClientProperty("zombie")==1){
		    addZombie((x-1), y, (Integer) (grid[x][y].getClientProperty("health")));
		    //grid[x-1][y].ad
		    //grid[x-1][y].putClientProperty("zombie", 1);
		    grid[x][y].removeAll();
		    grid[x][y].putClientProperty("zombie", 0);
		    grid[x][y].putClientProperty("zombieHealth", 0);
		}
	    }	
	}
    }
    

    public void populate() {
	ImageIcon ShooterOne = new ImageIcon("ShooterOne.png");
	//play1.getValueAt(0,0) = new JButton("plant");
	//play1.setIcon(ShooterOne);
    }
    


}
