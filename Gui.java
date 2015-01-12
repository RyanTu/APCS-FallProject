import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class Gui extends JFrame /*implements ActionListener*/ {
    
    private JFrame overall;
    private JPanel top, play1;
    private JTable play2;
    private ButtonGroup selection;
    private JRadioButton A,B,C,D;
    private JButton[][] grid;
    private JLabel counter;

    public int sunCount = 9;
   
    public Gui() {
	
	overall = new JFrame();
	overall.setSize(600,600);
	overall.setDefaultCloseOperation(EXIT_ON_CLOSE);
	overall.setLayout(new BorderLayout());
	//overall.setLayout(new BoxLayout(overall,BoxLayout.Y_AXIS));
	

	top = new JPanel();
	top.setLayout(new GridLayout(1,4));
	overall.add(top, BorderLayout.PAGE_START);

	counter = new JLabel("Counter: 0", JLabel.RIGHT);
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

        /*
       	public void actionPerformed(ActionEvent e){
	    //play2.setCellRenderer(new DefaultTableCellRenderer() {
	    
	}
	*/

	/*
	play2 = new JPanel();
	play2.setLayout(new GridLayout(5,9));
	play2.setBorder(BorderFactory.createLineBorder(Color.black));
	overall.add(play2);
	*/

	play2 = new JTable(5,9);
	for (int i = 0; i < 5; i++) {
	    play2.setRowHeight(i, 75);
	}
	play2.setRowSelectionAllowed(false);
	play2.setEnabled(false);
	play2.setShowGrid(true);
	play2.setGridColor(Color.BLUE);
	overall.add(play2, BorderLayout.CENTER);


	play2.addMouseListener(new MouseAdapter() {
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

	
	/*
	play1 = new JPanel(new GridLayout(5,9));
	grid = new JButton[5][9];
	for (int y = 0; y < 9; y++){
	    for (int x = 0; x < 5; x++){
		grid[x][y] = new JButton();
		play1.add(grid[x][y]);
	    }
	}
	overall.add(play1, BorderLayout.CENTER);
	*/
	

	overall.pack();
	overall.setVisible(true);

    }

    public int getSunCount(){
	return sunCount;
    }

    public void counterChange(){
	counter.setText("Counter: " + getSunCount());
    }

    public static void main(String[] args) {
	Gui g = new Gui();
    }
}
