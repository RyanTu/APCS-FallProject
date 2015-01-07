import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class Gui extends JFrame{
    
    private JFrame overall;
    private JPanel choose, play1, play2;
    private JRadioButton A,B,C,D;
    private JButton[][] grid;
   
    public Gui() {
	
	overall = new JFrame();
	overall.setSize(600,600);
	overall.setDefaultCloseOperation(EXIT_ON_CLOSE);
	//overall.setLayout(new BoxLayout(overall,BoxLayout.Y_AXIS));
	

	choose = new JPanel();
	choose.setLayout(new GridLayout(1,4));
	//overall.add(choose);

        A = new JRadioButton("A");
	choose.add(A);
        B = new JRadioButton("B");
	choose.add(B);
        C = new JRadioButton("C");
	choose.add(C);
        D = new JRadioButton("D");
	choose.add(D);

	play2 = new JPanel();
	play2.setLayout(new GridLayout(5,9));
	play2.setBorder(BorderFactory.createLineBorder(Color.black));
	overall.add(play2);

	/* Layout issues
	play1 = new JPanel(new GridLayout(5,9));
	grid = new JButton[5][9];
	for (int y = 0; y < 9; y++){
	    for (int x = 0; x < 5; x++){
		grid[x][y] = new JButton();
		overall.add(grid[x][y]);
	    }
	}
	*/
	
	overall.setVisible(true);

    }

    public static void main(String[] args) {
	Gui g = new Gui();
    }
}
