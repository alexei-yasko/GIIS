package giis.labs.graphics

import swing.{Button, BorderPanel, MainFrame}
import java.awt.{Color, Dimension}
import swing.event.ButtonClicked
import giis.labs.model.{Point, AlgorithmTypeList}
import giis.labs.model.shape.ShapeFactory

/**
 * @author Q-YAA
 */
class GraphicsMainFrame extends MainFrame {

    private val defaultWidth = 800
    private val defaultHeight = 600

    private val drawingButton = new Button("Draw")

    private val graphicsScene = new GraphicsScene
    val gridPanel: GridPanel = new GridPanel(graphicsScene)

    private var algorithmType = AlgorithmTypeList.LineDda

    title = "Graphics editor"
    size = new Dimension(defaultWidth, defaultHeight)
    preferredSize = new Dimension(defaultWidth, defaultHeight)
    centerOnScreen()

    listenTo(drawingButton)

    reactions += {
        case ButtonClicked(`drawingButton`) => draw()
    }

    contents = new BorderPanel {
        add(gridPanel, BorderPanel.Position.Center)
        add(drawingButton, BorderPanel.Position.South)
    }

    private def draw() {
        val shape = ShapeFactory.createShape(List(new Point(0, 0), new Point(0, 0)), algorithmType)
        graphicsScene.addShapeRender(shape.createRender(algorithmType, Color.BLACK))
        gridPanel.repaint()
    }
}
