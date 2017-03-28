import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
//	private Random generator = new Random();
	public static class Globals {
		public static int uncovered_cells = 0;
		  
		}
	public void mousePressed(MouseEvent e) {
		Component c = e.getComponent();
		while (!(c instanceof JFrame)) {
			c = c.getParent();
			if (c == null) {
				return;
			}
		}
		JFrame myFrame = (JFrame) c;
		MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
		Insets myInsets = myFrame.getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		e.translatePoint(-x1, -y1);
		int x = e.getX();
		int y = e.getY();
		myPanel.x = x;
		myPanel.y = y;
		myPanel.mouseDownGridX = myPanel.getGridX(x, y);
		myPanel.mouseDownGridY = myPanel.getGridY(x, y);
		myPanel.repaint();
		switch (e.getButton()) {
		case 1:		//Left mouse button

			break;
		case 3:		//Right mouse button

			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {

		Component c = e.getComponent();
		while (!(c instanceof JFrame)) {
			c = c.getParent();
			if (c == null) {
				return;
			}
		}
		JFrame myFrame = (JFrame)c;
		MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
		if(!myPanel.GameOver){
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);
			boolean memory[][];
			memory = new boolean[9][9];
			//System.out.println(memory[0][0]);
			switch (e.getButton()) {
			case 1:		//Left mouse button


				if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
					//Had pressed outside
					//Do nothing
				} else {
					if ((gridX == -1) || (gridY == -1)) {
						//Is releasing outside
						//Do nothing
					} else {
						if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
							//Released the mouse button on a different cell where it was pressed
							//Do nothing
						} else {
							//Released the mouse button on the same cell where it was pressed
							// DETECTS EACH SQUARE
							//gridX is a number and it goes from 0 to 8 in a 9x9 case 
							//gridY ""
							if (myPanel.BombsOnGrid[myPanel.mouseDownGridX][myPanel.mouseDownGridY]){//if a bomb is selected
								myPanel.GameOver = true;
								System.out.println("Game Over. You hit a mine :(");

								for (int xp = 0; xp < myPanel.TOTAL_COLUMNS; xp++) {//paint bombs black and game is over
									for (int yp = 0; yp < myPanel.TOTAL_ROWS-1; yp++) {
										if(myPanel.BombsOnGrid[xp][yp]){
											myPanel.colorArray[xp][yp]= Color.BLACK;
										}
									}

								}
								myPanel.repaint();
							} else {//if there is not a bomb on the cell clicked and released 
								int count = 0;
								evaluate_cell(gridX,gridY,myPanel, memory);
								for (int xp = 0; xp < myPanel.TOTAL_COLUMNS; xp++) {
									for (int yp = 0; yp < myPanel.TOTAL_ROWS-1; yp++) {
										if(myPanel.uncover[xp][yp]==true){
											count++;
										}
									}
								}
								if(count == 65){

									myPanel.GameOver = true;

									System.out.println("Congratulations! You win the game");
									for (int xp = 0; xp < myPanel.TOTAL_COLUMNS; xp++) {//paint bombs black and game is over
										for (int yp = 0; yp < myPanel.TOTAL_ROWS-1; yp++) {
											if(myPanel.BombsOnGrid[xp][yp]){
												myPanel.colorArray[xp][yp]= Color.BLACK;
											}
										}

									}
								}

								
							}
						}

					}

				}
				myPanel.repaint();

				break;
			case 3:		//Right mouse button

				if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
					//Had pressed outside
					//Do nothing
				} else {
					if ((gridX == -1) || (gridY == -1)) {
						//Is releasing outside
						//Do nothing
					} else {

						if (myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.WHITE)){
							myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.RED;				
						}
						else if (myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.RED)){
							myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.WHITE;
						}

						myPanel.repaint();

					}
				}			





				break;
			default:    //Some other button (2 = Middle mouse button, etc.)
				//Do nothing
				break;


			}
		}
	}
	
	public void evaluate_cell(int gridX, int gridY, MyPanel p, boolean memory[][]){
		int bomb_counter = 0;
		
		memory[gridX][gridY] = true;
		p.uncover[gridX][gridY] = true;
		for(int i=-1 ; i<2 ; i++){
			for(int j=-1 ; j<2 ; j++){
				if(((gridX + i) <= 8 && (gridX + i) >= 0) && ((gridY + j) <= 8 && (gridY + j) >= 0)){
					if (p.BombsOnGrid[gridX+i][gridY+j]){
						bomb_counter++;

					}
				}
			}
		}

		//aqui va lo que imprime el numero o lo pone griz 
		if(bomb_counter == 0){
			
			p.colorArray[gridX][gridY]= Color.GRAY;
			
			for(int i=-1 ; i<2 ; i++){
				for(int j=-1 ; j<2 ; j++){
					if(((gridX + i) <= 8 && (gridX + i) >= 0) && ((gridY + j) <= 8 && (gridY + j) >= 0) && (memory[gridX+i][gridY+j] == false)){

						evaluate_cell(gridX+i, gridY+j, p,memory);
					}
				}
			}
			
		} else {
			p.numberOnGrid[gridX][gridY] = bomb_counter;
		}
		
		return;	
	}
}