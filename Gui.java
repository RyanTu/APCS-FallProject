import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class Gui extends JFrame /* implements ActionListener */ {
    private Container pane;
    private JFrame frame;
    private JButton b1, b2;
    
    public Gui() {

	frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(new GridLayout());
	pane.setLayout(new GridLayout(9,5));

	b1 = new JButton("");
	pane.add(b1);

    }

    public static void main(String[] args) {
	Gui g = new Gui();
    }
}
