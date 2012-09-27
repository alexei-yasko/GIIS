package giis.labs.graphics

import java.awt.{Color, Dimension}
import giis.labs.model.{ShapeType, ShapeTypeList}
import swing._
import event.ButtonClicked

/**
 * @author Q-YAA
 */
class GraphicsMainFrame extends MainFrame {

    private val defaultWidth = 800
    private val defaultHeight = 600

    private val drawingButton = new Button("Draw")
    private val startDebugButton = new Button("Debug")
    private val nextDebugStepButton = new Button("Next")
    private val clearButton = new Button("Clear")

    private val graphicsScene = new GraphicsScene
    private val graphicsSceneController = new GraphicsSceneController(graphicsScene)
    private val gridPanelComponent: GridPanelComponent = new GridPanelComponent(graphicsScene)

    private var shapeType: ShapeType = null

    private val lineBrezenhemMenuItem = new RadioMenuItem("Brezenhem")
    private val lineDdaMenuItem = new RadioMenuItem("Dda")

    private val shapesMenuGroup = new ButtonGroup(lineDdaMenuItem, lineBrezenhemMenuItem) {
        listenTo(lineBrezenhemMenuItem, lineDdaMenuItem)
        reactions += {
            case ButtonClicked(`lineDdaMenuItem`) => setShapeType(ShapeTypeList.LineDda)
            case ButtonClicked(`lineBrezenhemMenuItem`) => setShapeType(ShapeTypeList.LineBrezenhem)
        }
    }

    private val buttonPanel = new FlowPanel() {
        contents += clearButton
        contents += drawingButton
        contents += startDebugButton
        contents += nextDebugStepButton
    }

    contents = new BorderPanel {
        add(gridPanelComponent, BorderPanel.Position.Center)
        add(buttonPanel, BorderPanel.Position.South)
    }

    shapesMenuGroup.select(lineDdaMenuItem)
    nextDebugStepButton.enabled_=(b = false)

    title = "Graphics editor"
    size = new Dimension(defaultWidth, defaultHeight)
    preferredSize = new Dimension(defaultWidth, defaultHeight)
    centerOnScreen()
    setShapeType(ShapeTypeList.LineDda)
    menuBar = new MenuBar {
        contents += new Menu("Shapes") {
            contents += new Menu("Line") {
                contents += lineDdaMenuItem
                contents += lineBrezenhemMenuItem
            }
        }
    }

    listenTo(drawingButton, startDebugButton, nextDebugStepButton, clearButton)

    reactions += {
        case ButtonClicked(`clearButton`) => clearScene()
        case ButtonClicked(`drawingButton`) => draw()
        case ButtonClicked(`startDebugButton`) => startDebug()
        case ButtonClicked(`nextDebugStepButton`) => nextDebugStep()
    }

    private def draw() {
        graphicsSceneController.drawShape(shapeType, Color.BLACK)

        gridPanelComponent.repaint()
    }

    private def startDebug() {
        if (graphicsSceneController.isDebugEnabled(shapeType)) {
            graphicsSceneController.drawShapeInDebugMode(shapeType, Color.BLACK)

            nextDebugStepButton.enabled_=(b = true)
            drawingButton.enabled_=(b = false)
        }
    }

    private def nextDebugStep() {

        if (graphicsSceneController.isNextDebugStepEnabled) {
            graphicsSceneController.nextDebugStep()
        }

        if (!graphicsSceneController.isNextDebugStepEnabled) {
            nextDebugStepButton.enabled_=(b = false)
            drawingButton.enabled_=(b = true)
        }

        gridPanelComponent.repaint()
    }

    private def clearScene() {
        graphicsSceneController.clearScene()
        gridPanelComponent.repaint()
    }

    private def setShapeType(shapeType: ShapeType) {
        this.shapeType = shapeType
        graphicsSceneController.setMaxSelectionBufferSize(shapeType.definingPointQuantity)
    }
}
