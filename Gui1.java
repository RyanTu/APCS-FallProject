import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

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

    private JButton start; 
    private JButton reset;

    public int sunCount = 0;
    public int sunflowerNumber = 0;
   
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

        A = new JRadioButton("A");
	//A.setActionCommand("A");
	A.setSelected(true);
	
        B = new JRadioButton("B");
	
        C = new JRadioButton("C");

        D = new JRadioButton("D");

	selection = new ButtonGroup();
	selection.add(A);
	selection.add(B);
	selection.add(C);
        selection.add(D);
	top.add(A);
	top.add(B);
	top.add(C);
	top.add(D);

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
		overall.repaint();
		setSunCount(getSunCount()-100);
		counterChange();
	    }
	    
	}
    }

    
    private class Begin implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    setSunCount(75);
	    counterChange();
	    
	/*
	long startTime = System.nanoTime();
	for (startTime%500 == 0){
System.out.println 
	*/
	}
    }

    private class End implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    setSunCount(0);
	    counterChange();
	    
	    for (int y = 0; y < 9; y++){
		for (int x = 0; x < 5; x++){
		    
		    overall.remove(btn);
		    overall.validate();
		    overall.repaint();
		}
	    }
	    
	    // play.revalidate();
	    //overall.repaint();
	// clearBoard() : method to get rid of everything that has happened 
	}
    }
    
    public void gridMaker() {
	gameBoard = new JPanel();
	overall.add(gameBoard, BorderLayout.CENTER);
		
	play = new JPanel(new GridLayout(5,9)); 
	grid = new JButton[5][9];
	int counterX = 0;
	int counterY = 0;
	for (int y = 0; y < 9; y++){
	    for (int x = 0; x < 5; x++){
		JLabel test = new JLabel();
		String text = String.format("[%d, %d]", counterY, counterX);
		grid[x][y] = new JButton(text);
		grid[x][y].setContentAreaFilled(false);
		grid[x][y].setPreferredSize(new Dimension(125,125));
		grid[x][y].addActionListener(new PlantAdd());
		play.add(grid[x][y]);
		if (counterX < 8){
		    counterX++;
		} else {
		    counterX = 0;
		    counterY++;
		}
	    }
	}
	gameBoard.add(play);
    }

    public void populate() {
	ImageIcon ShooterOne = new ImageIcon("ShooterOne.png");
	//play1.getValueAt(0,0) = new JButton("plant");
	//play1.setIcon(ShooterOne);


    }
    
}
