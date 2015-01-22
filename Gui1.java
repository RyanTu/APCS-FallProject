import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.Timer;

public class Gui1 extends JFrame{
    
    private JFrame overall;
    /* overall is the frame where all the panels are on */

    private JPanel gameBoard; 
    /* gameBoard is the JPanel intended to be where shooting objects occur. 
       Should there be JLayeredPanel for our purposes? */
    private JPanel top, play, bottom; 
    /* top holds JLabel counter and ButtonGroup selection, 
       play is where JButton[][] grid is */

    private JLabel counter; 
    private ButtonGroup selection;
    private JRadioButton A,B,C,D;

    private JButton[][] grid;

    private JButton start, reset;

    public int sunCount = 0;
    public int sunflowerNumber = 0;

    private Random random = new Random();   
    private boolean isStarted;
    //public Timer t = new Timer();

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

	selection = new ButtonGroup();
	selection.add(A);
	selection.add(B);
	selection.add(C);
        selection.add(D);
	top.add(A);
	top.add(B);
	top.add(C);
	top.add(D);

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
	
    private class PlantAdd implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    JButton btn = (JButton) e.getSource();
	    if (A.isSelected() && getSunCount()>=50){
		JLabel image = new JLabel();
		image.setIcon(new ImageIcon("Sunflower.png"));
		btn.add(image);
		gameBoard.revalidate();
		//overall.repaint();
		setSunCount(getSunCount()-50);
		counterChange();
		setSf(getSf()+1);
	    }

	    if (B.isSelected() && getSunCount()>=100){
		JLabel image = new JLabel();
		image.setIcon(new ImageIcon("ShooterOne.png"));
		btn.add(image);
		gameBoard.revalidate();
		//overall.repaint();
		setSunCount(getSunCount()-100);
		counterChange();
	    }

	    if (C.isSelected() && getSunCount()>=125){
                JLabel image = new JLabel();
                image.setIcon(new ImageIcon("Chomper.png"));
                btn.add(image);
                gameBoard.revalidate();
                //overall.repaint();                                                             
                setSunCount(getSunCount()-125);
                counterChange();
            }
	    
	    if (D.isSelected() && getSunCount()>=200){
                JLabel image = new JLabel();
                image.setIcon(new ImageIcon("GatlingPea.png"));
                btn.add(image);
                gameBoard.revalidate();
                //overall.repaint();                                                             
                setSunCount(getSunCount()-200);
                counterChange();
            }
	   
	    
	    JButton btn1 = (JButton) e.getSource();
	    System.out.println("clicked column " + btn1.getClientProperty("column") + ", row " + btn1.getClientProperty("row"));
	    btn1.putClientProperty("column", (int)btn1.getClientProperty("column")+1);
	    System.out.println(btn1.getClientProperty("column"));
	    btn1.validate();
	    overall.repaint();
	}
    }

    private class Begin implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    isStarted = true;           
	    setSunCount(75);
	    counterChange();
		
	    addZombie();
	     
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
	int counterX = 0;
	int counterY = 0;
	for (int y = 0; y < 5; y++){
	    for (int x = 0; x < 9; x++){
		//JLabel test = new JLabel();
		//String text = String.format("[%d, %d]", y, x);
		grid[x][y] = new JButton(/*text*/);
		grid[x][y].setContentAreaFilled(false);
		grid[x][y].setPreferredSize(new Dimension(125,125));
		grid[x][y].addActionListener(new PlantAdd());
		grid[x][y].putClientProperty("column", x);
		grid[x][y].putClientProperty("row", y);
		play.add(grid[x][y]);
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
    
    public static void wait(int n) {
	try {
	    Thread.sleep(n);
	} catch (Exception e) {}
    }
    
    /*
    timer = new timer(speed, this);
    timer.setInitialDelay(pause);
    timer.start();
    */

    public void addZombie(){

	    JLabel image = new JLabel();
	    image.setIcon(new ImageIcon("Zombie1.png"));
	    grid[8][random.nextInt(5)].add(image);
	    gameBoard.revalidate();               
	                             
    }

    public void populate() {
	ImageIcon ShooterOne = new ImageIcon("ShooterOne.png");
	//play1.getValueAt(0,0) = new JButton("plant");
	//play1.setIcon(ShooterOne);


    }
    


}
