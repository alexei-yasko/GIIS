package giis.labs.graphics

import java.awt.{Color, Dimension}
import giis.labs.model.{AlgorithmType, AlgorithmTypeList}
import giis.labs.model.shape.ShapeFactory
import swing._
import event.ButtonClicked

/**
 * @author Q-YAA
 */
class GraphicsMainFrame extends MainFrame {

    private val defaultWidth = 800
    private val defaultHeight = 600

    private val drawingButton = new Button("Draw")

    private val graphicsScene = new GraphicsScene
    private val gridPanel: GridPanelComponent = new GridPanelComponent(graphicsScene)

    private var algorithmType: AlgorithmType = AlgorithmTypeList.LineDda

    private val lineBrezenhemMenuItem = new RadioMenuItem("Brezenhem")
    private val lineDdaMenuItem = new RadioMenuItem("Dda")

    private val algorithmsMenuGroup = new ButtonGroup(lineDdaMenuItem, lineBrezenhemMenuItem) {
        listenTo(lineBrezenhemMenuItem, lineDdaMenuItem)
        reactions += {
            case ButtonClicked(`lineDdaMenuItem`) => algorithmType = AlgorithmTypeList.LineDda
            case ButtonClicked(`lineBrezenhemMenuItem`) => algorithmType = AlgorithmTypeList.LineBrezenhem
        }
    }

    algorithmsMenuGroup.select(lineDdaMenuItem)

    title = "Graphics editor"
    size = new Dimension(defaultWidth, defaultHeight)
    preferredSize = new Dimension(defaultWidth, defaultHeight)
    centerOnScreen()
    menuBar = new MenuBar {
        contents += new Menu("Algorithms") {
            contents += new Menu("Line") {
                contents += lineDdaMenuItem
                contents += lineBrezenhemMenuItem
            }
        }
    }

    listenTo(drawingButton)

    reactions += {
        case ButtonClicked(`drawingButton`) => draw()
    }

    contents = new BorderPanel {
        add(gridPanel, BorderPanel.Position.Center)
        add(drawingButton, BorderPanel.Position.South)
    }

    private def draw() {
        val shape = ShapeFactory.createShape(gridPanel.selectedPointSet.toList, algorithmType)

        if (shape != null) {
            graphicsScene.addShapeRender(shape.createRender(algorithmType, Color.BLACK))
            gridPanel.removeSelectedPoints(shape.getPointList)
        }

        gridPanel.repaint()
    }
}
