import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class Gui extends JFrame /* implements ActionListener */ {
    
    private JFrame frame;
    private JPanel pane;
    private JButton b1, b2;
    private JLabel mainLabel;
   
    public Gui() {
	
	frame = new JFrame();
	frame.setSize(600,600);
	frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	frame.setLayout(new FlowLayout());

	pane = new JPanel();
	pane.setLayout(new GridLayout(9,5));
	frame.add(pane);

	b1 = new JButton("A");
	pane.add(b1);
	
	frame.setVisible(true);

    }

    public static void main(String[] args) {
	Gui g = new Gui();
    }
}
