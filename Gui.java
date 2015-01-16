import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class Gui extends JFrame{
    
    private JFrame overall;
    private JPanel gameBoard;
    private JPanel top, play;
    private ButtonGroup selection;
    private JRadioButton A,B,C,D;
    private JButton[][] grid;
    private JLabel counter;

    public int sunCount = 0;
   
    public Gui() {

	overall = new JFrame();
	overall.setSize(600,600);
	overall.setDefaultCloseOperation(EXIT_ON_CLOSE);
	overall.setLayout(new BorderLayout());
	

	ActionListener listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    System.out.println("Button selected: " + e.getActionCommand());
		    paintComponent(gameBoard);
		    
	        }
	    };
    

	top = new JPanel();
	top.setLayout(new GridLayout());
	overall.add(top, BorderLayout.PAGE_START);

	counter = new JLabel("Counter: 0", JLabel.CENTER);
	top.add(counter);

        A = new JRadioButton("A");
	A.setActionCommand("A");
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
	//A.addActionListener(this);

	gameBoard = new JPanel();
	overall.add(gameBoard, BorderLayout.CENTER);
		
	play = new JPanel(new GridLayout(5,9));	
	//play.setOpaque(false);
	grid = new JButton[9][9];
	int counterX = 0;
	int counterY = 0;
	for (int y = 0; y < 9; y++){
	    for (int x = 0; x < 5; x++){
		String text = String.format("[%d, %d]", counterY, counterX);
		grid[x][y] = new JButton(text);
		grid[x][y].setContentAreaFilled(false);
		grid[x][y].setPreferredSize(new Dimension(100,100));
		grid[x][y].addActionListener(listener);
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
	//overall.add(play);
        
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

   
	
	public void paintComponent(Graphics g){
	    super.paintComponent(g);
	    g.setColor(Color.green);
	    g.fillOval(x,y,30,30);
	}
    
    /* TO DO: Figure out how to give an area on JPanel gameBoard the properties
       of a class using action listener
              Figure out how to insert an object using action listener*/
    
    public void populate() {
	ImageIcon ShooterOne = new ImageIcon("ShooterOne.png");
	//play1.getValueAt(0,0) = new JButton("plant");
	//play1.setIcon(ShooterOne);


    }
    

}
