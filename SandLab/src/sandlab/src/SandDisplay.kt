import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Image
import java.awt.event.*
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import kotlin.math.pow

class SandDisplay(title: String?, private val numRows: Int, private val numCols: Int, buttonNames: Array<String?>) : JComponent(), MouseListener, MouseMotionListener, ActionListener, ChangeListener {
    private val image: Image
    private val cellSize: Int
    var tool = 1
        private set
    var mouseLocation: IntArray? = null
        private set
    private val buttons: Array<JButton?>
    private val slider: JSlider
    // returns number of times to step between repainting and processing mouse input
    var speed: Int
        private set

    public override fun paintComponent(g: Graphics) {
        g.drawImage(image, 0, 0, null)
    }

    fun pause(milliseconds: Int) {
        try {
            Thread.sleep(milliseconds.toLong())
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    fun setColor(row: Int, col: Int, color: Color?) {
        val g = image.graphics
        g.color = color
        g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize)
    }

    override fun mouseClicked(e: MouseEvent) {}
    override fun mousePressed(e: MouseEvent) {
        mouseLocation = toLocation(e)
    }

    override fun mouseReleased(e: MouseEvent) {
        mouseLocation = null
    }

    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}
    override fun mouseMoved(e: MouseEvent) {}
    override fun mouseDragged(e: MouseEvent) {
        mouseLocation = toLocation(e)
    }

    private fun toLocation(e: MouseEvent): IntArray? {
        val row = e.y / cellSize
        val col = e.x / cellSize
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) return null
        val loc = IntArray(2)
        loc[0] = row
        loc[1] = col
        return loc
    }

    override fun actionPerformed(e: ActionEvent) {
        tool = e.actionCommand.toInt()
        for (button in buttons) button!!.isSelected = false
        (e.source as JButton).isSelected = true
    }

    override fun stateChanged(e: ChangeEvent) {
        speed = computeSpeed(slider.value)
    }

    // returns speed based on sliderValue
// speed of 0 returns 10^3
// speed of 100 returns 10^6
    private fun computeSpeed(sliderValue: Int): Int {
        return 10.0.pow(0.03 * sliderValue + 3).toInt()
    }

    init {
        speed = computeSpeed(50)
        // determine cell size
        cellSize = 1.coerceAtLeast(600 / numRows.coerceAtLeast(numCols))
        image = BufferedImage(numCols * cellSize, numRows * cellSize, BufferedImage.TYPE_INT_RGB)
        val frame = JFrame(title)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane.layout = BoxLayout(frame.contentPane, BoxLayout.PAGE_AXIS)
        val topPanel = JPanel()
        topPanel.layout = BoxLayout(topPanel, BoxLayout.LINE_AXIS)
        frame.contentPane.add(topPanel)
        preferredSize = Dimension(numCols * cellSize, numRows * cellSize)
        addMouseListener(this)
        addMouseMotionListener(this)
        topPanel.add(this)
        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS)
        topPanel.add(buttonPanel)
        buttons = arrayOfNulls(buttonNames.size)
        for (i in buttons.indices) {
            buttons[i] = JButton(buttonNames[i])
            buttons[i]!!.actionCommand = "" + i
            buttons[i]!!.addActionListener(this)
            buttonPanel.add(buttons[i])
        }
        buttons[tool]!!.isSelected = true
        slider = JSlider(JSlider.HORIZONTAL, 0, 100, 50)
        slider.addChangeListener(this)
        slider.majorTickSpacing = 1
        slider.paintTicks = true
        val labelTable = Hashtable<Int, JLabel>()
        labelTable[0] = JLabel("Slow")
        labelTable[100] = JLabel("Fast")
        slider.labelTable = labelTable
        slider.paintLabels = true
        frame.contentPane.add(slider)
        frame.pack()
        frame.isVisible = true
    }
}