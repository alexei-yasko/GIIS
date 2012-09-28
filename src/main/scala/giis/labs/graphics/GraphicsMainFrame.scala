package giis.labs.graphics

import custom.ToolBar
import java.awt.{Color, Dimension}
import giis.labs.model.{ShapeType, ShapeTypeList}
import swing._
import event.ButtonClicked

/**
 * @author Q-YAA
 */
class GraphicsMainFrame extends MainFrame {

    private val defaultWidth = 800
    private val defaultHeight = 650

    private val drawingButton = new Button("Draw")
    private val startDebugButton = new Button("Debug")
    private val nextDebugStepButton = new Button("Next")
    private val previousDebugStepButton = new Button("Previous")
    private val clearButton = new Button("Clear")

    private val graphicsScene = new GraphicsScene
    private val graphicsSceneController = new GraphicsSceneController(graphicsScene)
    private val gridPanelComponent: GridPanelComponent = new GridPanelComponent(graphicsScene, graphicsSceneController)

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
        contents += previousDebugStepButton
    }

    private val toolBar = new ToolBar {
        contents += buttonPanel
    }

    contents = new BorderPanel {
        add(gridPanelComponent, BorderPanel.Position.Center)
        add(toolBar, BorderPanel.Position.North)
    }

    shapesMenuGroup.select(lineDdaMenuItem)
    nextDebugStepButton.enabled_=(b = false)
    previousDebugStepButton.enabled_=(b = false)

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

    listenTo(drawingButton, startDebugButton, nextDebugStepButton, clearButton, previousDebugStepButton)

    reactions += {
        case ButtonClicked(`clearButton`) => clearScene()
        case ButtonClicked(`drawingButton`) => draw()
        case ButtonClicked(`startDebugButton`) => startDebug()
        case ButtonClicked(`nextDebugStepButton`) => nextDebugStep()
        case ButtonClicked(`previousDebugStepButton`) => previousDebugStep()
    }

    private def draw() {
        graphicsSceneController.drawShape(shapeType, Color.BLACK)

        gridPanelComponent.repaint()
    }

    private def startDebug() {
        if (graphicsSceneController.isDebugEnabled(shapeType)) {
            graphicsSceneController.drawShapeInDebugMode(shapeType, Color.BLACK)

            nextDebugStepButton.enabled_=(!nextDebugStepButton.enabled)
            previousDebugStepButton.enabled_=(!previousDebugStepButton.enabled)
            drawingButton.enabled_=(!drawingButton.enabled)
        }
    }

    private def nextDebugStep() {

        if (graphicsSceneController.isNextDebugStepEnabled) {
            graphicsSceneController.nextDebugStep()
        }

        if (!graphicsSceneController.isNextDebugStepEnabled) {
            nextDebugStepButton.enabled_=(b = false)
            previousDebugStepButton.enabled_=(b = false)
            drawingButton.enabled_=(b = true)
        }

        gridPanelComponent.repaint()
    }

    private def previousDebugStep() {
        graphicsSceneController.previousDebugStep()
        gridPanelComponent.repaint()
    }

    private def clearScene() {
        graphicsScene.clear()
        gridPanelComponent.repaint()
    }

    private def setShapeType(shapeType: ShapeType) {
        this.shapeType = shapeType
        graphicsScene.setMaxSelectionBufferSize(shapeType.definingPointQuantity)
    }
}
