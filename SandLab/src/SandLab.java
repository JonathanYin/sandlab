import java.awt.*;

//jonathan yin and clement ou
public class SandLab {
	public static void main(String[] args) {
		SandLab lab = new SandLab(120, 80);
		lab.run();
	}

	// TODO add rows & cols to args
	public int rows = 120;
	public int cols = 80;

	// add constants for particle types here
	private static final int EMPTY = 0;
	private static final int METAL = 1;
	private static final int SAND = 2;
	private static final int WATER = 3;
	private static final int PLANT = 4;
	private static final int ACID = 5;
	private static final int CLEAR = 6;
	
	// use the clear button by selecting it and clicking on a square within the grid

	private static final int SCALE_UP = 7;
	
	private int[][] grid;
	private SandDisplay display;

	// constructor
	private SandLab(int numRows, int numCols) {
		String[] names;
		names = new String[8];
		names[EMPTY] = "Empty";
		names[METAL] = "Metal";
		names[SAND] = "Sand";
		names[WATER] = "Water";
		names[PLANT] = "Plant";
		names[ACID] = "Acid";
		names[CLEAR] = "Clear";
		names[SCALE_UP] = "Scale Up";
		display = new SandDisplay("Falling Sand", numRows, numCols, names);
		grid = new int[numRows][numCols]; // initializes grid field
	}

	// 2d array
	private void locationClicked(int row, int col, int tool) {
		grid[row][col] = tool;
	}

	// copies each element of grid into the display
	private void updateDisplay() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (grid[row][col] == 0) {
					display.setColor(row, col, Color.BLACK);
				} else if (grid[row][col] == 1) {
					display.setColor(row, col, Color.GRAY);
				} else if (grid[row][col] == 2) {
					display.setColor(row, col, Color.YELLOW);
				} else if (grid[row][col] == 3) {
					display.setColor(row, col, Color.BLUE);
				} else if (grid[row][col] == 4) {
					display.setColor(row, col, Color.GREEN);
				} else if (grid[row][col] == 5) {
					display.setColor(row, col, Color.CYAN);
				} else if (grid[row][col] == 6) {
					display.setColor(row, col, Color.BLACK);
				}
			}
		}
	}

	// causes one random particle to maybe do something
	private void step() {
		// finds random space
		int randomRow = (int) (Math.random() * rows);
		int randomCol = (int) (Math.random() * cols);

		// sand into empty
		if (grid[randomRow][randomCol] == SAND && randomRow < rows - 1 && grid[randomRow + 1][randomCol] == EMPTY) {
			grid[randomRow][randomCol] = EMPTY;
			grid[randomRow + 1][randomCol] = SAND;
		}

		// water
		if (grid[randomRow][randomCol] == WATER && randomRow < rows - 1 && grid[randomRow + 1][randomCol] == EMPTY) {
			grid[randomRow][randomCol] = EMPTY;
			grid[randomRow + 1][randomCol] = WATER;
		} else if (grid[randomRow][randomCol] == WATER && randomCol != 0 && grid[randomRow][randomCol - 1] == EMPTY
				&& randomCol - 1 > 0) {
			grid[randomRow][randomCol] = EMPTY;
			grid[randomRow][randomCol - 1] = WATER;
		} else if (grid[randomRow][randomCol] == WATER && randomCol < cols - 1
				&& grid[randomRow][randomCol + 1] == EMPTY && randomCol + 1 < rows
				&& grid[randomRow][randomCol] != grid[randomRow][cols - 1]) {
			grid[randomRow][randomCol] = EMPTY;
			grid[randomRow][randomCol + 1] = WATER;
		} else if (grid[randomRow][randomCol] == WATER && randomCol == 0 && grid[randomRow][randomCol + 1] == EMPTY) {
			grid[randomRow][randomCol] = EMPTY;
			grid[randomRow][randomCol + 1] = WATER;
		}

		// sand into water
		if (grid[randomRow][randomCol] == SAND && randomRow < rows - 1 && grid[randomRow + 1][randomCol] == WATER) {
			grid[randomRow][randomCol] = WATER;
			grid[randomRow + 1][randomCol] = SAND;
		}

		// plant
		if (grid[randomRow][randomCol] == PLANT && randomRow != 0) {
			if (randomCol != cols - 1 && grid[randomRow][randomCol + 1] == WATER) {
				grid[randomRow][randomCol + 1] = EMPTY;
				grid[randomRow - 1][randomCol] = PLANT;
			} else if (randomCol != 0 && grid[randomRow][randomCol - 1] == WATER) {
				grid[randomRow][randomCol - 1] = EMPTY;
				grid[randomRow - 1][randomCol] = PLANT;
			} else if (grid[rows - 1][randomCol] == PLANT && randomCol > 0 && grid[rows - 1][randomCol - 1] == WATER) {
				grid[rows - 1][randomCol - 1] = EMPTY;
				grid[randomRow - 1][randomCol] = PLANT;
			}
		}

		// acid
		if (grid[randomRow][randomCol] == ACID && randomRow < rows - 1
				&& (grid[randomRow + 1][randomCol] == EMPTY
						|| (randomCol > 0 && grid[randomRow][randomCol - 1] == EMPTY)
						|| (randomCol < cols - 1 && grid[randomRow][randomCol + 1] == EMPTY))) {
			if (grid[randomRow + 1][randomCol] == EMPTY || grid[randomRow + 1][randomCol] == METAL
					|| grid[randomRow + 1][randomCol] == PLANT) {
				grid[randomRow][randomCol] = EMPTY;
				grid[randomRow + 1][randomCol] = ACID;
			} else if (randomCol != 0 && (grid[randomRow][randomCol - 1] == EMPTY
					|| grid[randomRow][randomCol - 1] == METAL || grid[randomRow][randomCol - 1] == PLANT)
					&& randomCol - 1 > 0) {
				grid[randomRow][randomCol] = EMPTY;
				grid[randomRow][randomCol - 1] = ACID;
			} else if ((grid[randomRow][randomCol + 1] == EMPTY || grid[randomRow][randomCol + 1] == METAL
					|| grid[randomRow][randomCol + 1] == PLANT) && randomCol + 1 < rows) {
				grid[randomRow][randomCol] = EMPTY;
				grid[randomRow][randomCol + 1] = ACID;
			}
		}

		// clear
		if (grid[randomRow][randomCol] == CLEAR) {
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					grid[row][col] = EMPTY;
				}
			}
		}
		
		if (grid[randomRow][randomCol] == SCALE_UP) {
			int x = rows += 10; 
			int y = cols += 5;
			SandLab lab = new SandLab(x, y);
			lab.run();
		}
			
	}

	// updater
	private void run() {
		while (true) {
			for (int i = 0; i < display.getSpeed(); i++)
				step();
			updateDisplay();
			display.repaint();
			display.pause(1); // wait for redrawing and for mouse
			int[] mouseLoc = display.getMouseLocation();
			if (mouseLoc != null) // test if mouse clicked
				locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
		}
	}
}
