package giis.labs

import graphics.GridPanel
import swing.{BorderPanel, MainFrame}
import java.awt.Dimension

/**
 * @author Q-YAA
 */
class GraphicsMainFrame extends MainFrame {

    private val defaultWidth = 800
    private val defaultHeight = 600

    title = "Graphics editor"
    size = new Dimension(defaultWidth, defaultHeight)
    preferredSize = new Dimension(defaultWidth, defaultHeight)
    centerOnScreen()

    contents = new BorderPanel {
        add(new GridPanel, BorderPanel.Position.Center)
    }
}
