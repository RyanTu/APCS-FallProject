import javax.swing.JFrame; 
import javax.swing.JButton;
import java.awt.GridLayout; 

public class Grid {
    JFrame frame = new JFrame(); 
    JButton[][] grid; 

    public Grid(int width, int length) { 
	frame.setLayout(new GridLayout(width,length)); 
	grid = new JButton[width][length]; 
	for (int y = 0; y < length; y++) { 
	    for (int x = 0; x < width; x++) { 
		grid[x][y] = new JButton(); // makes it possible to select spot to plant 
		frame.add(grid[x][y]); //adds button to grid 
	    } 
	} 
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	frame.pack(); 
	frame.setVisible(true);  
    } 
    public static void main(String[] args) { 
	new Grid(5, 9);
    }
}