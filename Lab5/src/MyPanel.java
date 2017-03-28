import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JPanel;
 
public class MyPanel extends JPanel {
	
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;//initial x coordinate
	private static final int GRID_Y = 25;//initial y coordinate
	private static final int INNER_CELL_SIZE = 29;//size of cells
	public final int TOTAL_COLUMNS = 9;//number of columns
	public final int TOTAL_ROWS = 10;   //Last row has only one cell (number of rows + 1)
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;

	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];//array for each cell color
	public int[][] numberOnGrid = new int[TOTAL_COLUMNS][TOTAL_ROWS]; //array for number on cell
	public Boolean[][] BombsOnGrid = new Boolean[TOTAL_COLUMNS][TOTAL_ROWS];//true or false bomb on cell
	public Boolean[][] uncover = new Boolean[TOTAL_COLUMNS][TOTAL_ROWS];//array for uncover cells with a bomb nearby
	public int NumberOfBombsOnMap = 0;
	public int TotalBombs = (TOTAL_COLUMNS*(TOTAL_ROWS-1))/5;
	public Boolean GameOver= false;

	public Random rangen;



	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		
		rangen = new Random();
		
		
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS-1; y++) {
				colorArray[x][y] = Color.WHITE;
				BombsOnGrid[x][y] = false;
				numberOnGrid[x][y] = 0; 
				uncover[x][y] = false;
			}//The rest of the grid
		}
		
		
		
		while (NumberOfBombsOnMap!=TotalBombs){//pone las bombas random en las cells hasta 16 si es 9x9
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				for (int y = 0; y < TOTAL_ROWS-1; y++) {
					int Random = rangen.nextInt(TOTAL_COLUMNS*(TOTAL_ROWS-1))+1;
					if (((Random == 5) && BombsOnGrid[x][y] !=true)){
						BombsOnGrid[x][y]=true;
						NumberOfBombsOnMap++;
						//colorArray[x][y]=Color.BLACK; quita comments y mira las 16 bombas

					}

				}
			}
		}
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}



		//Draw numbers on cells and Paint cells if no number is required
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS-1; y++) {
				int n = numberOnGrid[x][y];
				Color c = colorArray[x][y];
				if(n > 0){//draw the number on the cell
					g.setColor(c);
					g.drawString(Integer.toString(n), x1 + GRID_X + (x * (INNER_CELL_SIZE )) + INNER_CELL_SIZE/2 , y1 + GRID_Y + (y * (INNER_CELL_SIZE + 2)) + INNER_CELL_SIZE/2);
				} 
				else { // if there is not a number, sets the color of the cell
					
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
			}
		}
//		Paint cell colors
//		for (int x = 0; x < TOTAL_COLUMNS; x++) {
//			for (int y = 0; y < TOTAL_ROWS-1; y++) {
//				Color c = colorArray[x][y];
//
//				g.setColor(c);
//				g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
//
//			}
//		}
		
	}
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
}