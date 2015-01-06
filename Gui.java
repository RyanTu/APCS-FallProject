import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class Gui extends JFrame{
    
    private JFrame frame;
    private JPanel choose, play1, play2;
    private JRadioButton A,B,C,D;
    private JButton[][] grid;
   
    public Gui() {
	
	frame = new JFrame();
	frame.setSize(600,600);
	frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	//frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));

	choose = new JPanel();
	choose.setLayout(new GridLayout(1,4));
	frame.add(choose);

        A = new JRadioButton("A");
	choose.add(A);
        B = new JRadioButton("B");
	choose.add(B);
        C = new JRadioButton("C");
	choose.add(C);
        D = new JRadioButton("D");
	choose.add(D);

	//play2 = new JPanel();

	
	play1 = new JPanel();
	play1.setLayout(new GridLayout(5,9));
	grid = new JButton[5][9];
	for (int y = 0; y < 9; y++){
	    for (int x = 0; x < 5; x++){
		grid[x][y] = new JButton();
		frame.add(grid[x][y]);
	    }
	}
	
	
	frame.setVisible(true);

    }

    public static void main(String[] args) {
	Gui g = new Gui();
    }
}
