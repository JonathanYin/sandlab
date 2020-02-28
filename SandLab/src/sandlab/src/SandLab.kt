import java.awt.Color

class SandLab private constructor(numRows: Int, numCols: Int) {
    // TODO add rows & cols to args
    private val rows = 120
    private val cols = 80
    private val grid: Array<IntArray>
    // use the clear button by selecting it and clicking on a square within the grid
    private val display: SandDisplay

    // 2d array
    private fun locationClicked(row: Int, col: Int, tool: Int) {
        grid[row][col] = tool
    }

    // copies each element of grid into the display
    private fun updateDisplay() {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                when {
                    grid[row][col] == 0 -> {
                        display.setColor(row, col, Color.BLACK)
                    }
                    grid[row][col] == 1 -> {
                        display.setColor(row, col, Color.GRAY)
                    }
                    grid[row][col] == 2 -> {
                        display.setColor(row, col, Color.YELLOW)
                    }
                    grid[row][col] == 3 -> {
                        display.setColor(row, col, Color.BLUE)
                    }
                    grid[row][col] == 4 -> {
                        display.setColor(row, col, Color.GREEN)
                    }
                    grid[row][col] == 5 -> {
                        display.setColor(row, col, Color.CYAN)
                    }
                    grid[row][col] == 6 -> {
                        display.setColor(row, col, Color.BLACK)
                    }
                }
            }
        }
    }

    // causes one random particle to maybe do something
    private fun step() { // finds random space
        val randomRow = (Math.random() * rows).toInt()
        val randomCol = (Math.random() * cols).toInt()
        // sand into empty
        if (grid[randomRow][randomCol] == SAND && randomRow < rows - 1 && grid[randomRow + 1][randomCol] == EMPTY) {
            grid[randomRow][randomCol] = EMPTY
            grid[randomRow + 1][randomCol] = SAND
        }
        // water
        if (grid[randomRow][randomCol] == WATER && randomRow < rows - 1 && grid[randomRow + 1][randomCol] == EMPTY) {
            grid[randomRow][randomCol] = EMPTY
            grid[randomRow + 1][randomCol] = WATER
        } else if (grid[randomRow][randomCol] == WATER && randomCol != 0 && grid[randomRow][randomCol - 1] == EMPTY && randomCol - 1 > 0) {
            grid[randomRow][randomCol] = EMPTY
            grid[randomRow][randomCol - 1] = WATER
        } else if (grid[randomRow][randomCol] == WATER && randomCol < cols - 1 && grid[randomRow][randomCol + 1] == EMPTY && randomCol + 1 < rows && grid[randomRow][randomCol] != grid[randomRow][cols - 1]) {
            grid[randomRow][randomCol] = EMPTY
            grid[randomRow][randomCol + 1] = WATER
        } else if (grid[randomRow][randomCol] == WATER && randomCol == 0 && grid[randomRow][randomCol + 1] == EMPTY) {
            grid[randomRow][randomCol] = EMPTY
            grid[randomRow][randomCol + 1] = WATER
        }
        // sand into water
        if (grid[randomRow][randomCol] == SAND && randomRow < rows - 1 && grid[randomRow + 1][randomCol] == WATER) {
            grid[randomRow][randomCol] = WATER
            grid[randomRow + 1][randomCol] = SAND
        }
        // plant
        if (grid[randomRow][randomCol] == PLANT && randomRow != 0) {
            if (randomCol != cols - 1 && grid[randomRow][randomCol + 1] == WATER) {
                grid[randomRow][randomCol + 1] = EMPTY
                grid[randomRow - 1][randomCol] = PLANT
            } else if (randomCol != 0 && grid[randomRow][randomCol - 1] == WATER) {
                grid[randomRow][randomCol - 1] = EMPTY
                grid[randomRow - 1][randomCol] = PLANT
            } else if (grid[rows - 1][randomCol] == PLANT && randomCol > 0 && grid[rows - 1][randomCol - 1] == WATER) {
                grid[rows - 1][randomCol - 1] = EMPTY
                grid[randomRow - 1][randomCol] = PLANT
            }
        }
        // acid
        if (grid[randomRow][randomCol] == ACID && randomRow < rows - 1 && (grid[randomRow + 1][randomCol] == EMPTY || randomCol > 0 && grid[randomRow][randomCol - 1] == EMPTY
                        || randomCol < cols - 1 && grid[randomRow][randomCol + 1] == EMPTY)) {
            if (grid[randomRow + 1][randomCol] == EMPTY || grid[randomRow + 1][randomCol] == METAL || grid[randomRow + 1][randomCol] == PLANT) {
                grid[randomRow][randomCol] = EMPTY
                grid[randomRow + 1][randomCol] = ACID
            } else if (randomCol != 0 && (grid[randomRow][randomCol - 1] == EMPTY || grid[randomRow][randomCol - 1] == METAL || grid[randomRow][randomCol - 1] == PLANT)
                    && randomCol - 1 > 0) {
                grid[randomRow][randomCol] = EMPTY
                grid[randomRow][randomCol - 1] = ACID
            } else if ((grid[randomRow][randomCol + 1] == EMPTY || grid[randomRow][randomCol + 1] == METAL || grid[randomRow][randomCol + 1] == PLANT) && randomCol + 1 < rows) {
                grid[randomRow][randomCol] = EMPTY
                grid[randomRow][randomCol + 1] = ACID
            }
        }
        // clear
        if (grid[randomRow][randomCol] == CLEAR) {
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    grid[row][col] = EMPTY
                }
            }
        }
    }

    // updater
    private fun run() {
        while (true) {
            for (i in 0 until display.speed) step()
            updateDisplay()
            display.repaint()
            display.pause(1) // wait for redrawing and for mouse
            val mouseLoc = display.mouseLocation
            if (mouseLoc != null) // test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.tool)
        }
    }

    companion object {
        // add constants for particle types here
        private const val EMPTY = 0
        private const val METAL = 1
        private const val SAND = 2
        private const val WATER = 3
        private const val PLANT = 4
        private const val ACID = 5
        private const val CLEAR = 6
        @JvmStatic
        fun main(args: Array<String>) {
            val lab = SandLab(120, 80)
            lab.run()
        }
    }

    // constructor
    init {
        val names: Array<String?> = arrayOfNulls(7)
        names[EMPTY] = "Empty"
        names[METAL] = "Metal"
        names[SAND] = "Sand"
        names[WATER] = "Water"
        names[PLANT] = "Plant"
        names[ACID] = "Acid"
        names[CLEAR] = "Clear"
        display = SandDisplay("Falling Sand", numRows, numCols, names)
        grid = Array(numRows) { IntArray(numCols) } // initializes grid field
    }
}