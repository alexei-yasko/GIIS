package giis.labs.graphics

import custom.ToolBar
import java.awt.Dimension
import giis.labs.model.ShapeType
import swing._
import event.ButtonClicked
import javax.swing.JColorChooser
import giis.labs.model.shape.Shape

/**
 * Main frame of the application.
 *
 * @author Q-YAA
 */
class GraphicsMainFrame extends MainFrame {

    private val defaultWidth = 800
    private val defaultHeight = 650

    private val startDebugButton = new Button("Debug")
    private val nextDebugStepButton = new Button("Next")
    private val previousDebugStepButton = new Button("Previous")
    private val clearButton = new Button("Clear")
    private val cancelButton = new Button("Cancel")
    private val colorChooseButton = new Button("Choose color")

    private val graphicsScene = new GraphicsScene
    private val graphicsSceneController = new GraphicsSceneController(graphicsScene)
    private val gridPanelComponent: GridPanelComponent =
        new GridPanelComponent(graphicsScene, graphicsSceneController)

    private val lineBrezenhemMenuItem = new RadioMenuItem("Brezenhem")
    private val lineDdaMenuItem = new RadioMenuItem("Dda")
    private val circleMenuItem = new RadioMenuItem("Circle")
    private val bezierMenuItem = new RadioMenuItem("Bezier")
    private val ermitMenuItem = new RadioMenuItem("Ermit")

    private val shapesMenuGroup = new ButtonGroup(
        lineDdaMenuItem,
        lineBrezenhemMenuItem,
        circleMenuItem,
        bezierMenuItem,
        ermitMenuItem
    ) {
        listenTo(
            lineBrezenhemMenuItem,
            lineDdaMenuItem,
            circleMenuItem,
            bezierMenuItem,
            ermitMenuItem
        )

        reactions += {
            case ButtonClicked(`lineDdaMenuItem`) => setShapeType(Shape.LineDda)
            case ButtonClicked(`lineBrezenhemMenuItem`) => setShapeType(Shape.LineBrezenhem)
            case ButtonClicked(`circleMenuItem`) => setShapeType(Shape.Circle)
            case ButtonClicked(`bezierMenuItem`) => setShapeType(Shape.Bezier)
            case ButtonClicked(`ermitMenuItem`) => setShapeType(Shape.Ermit)
        }
    }

    private val buttonPanel = new FlowPanel() {
        contents += colorChooseButton
        contents += clearButton
        contents += cancelButton
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
    setShapeType(Shape.LineDda)
    menuBar = new MenuBar {
        contents += new Menu("Shapes") {
            contents += new Menu("Line") {
                contents += lineDdaMenuItem
                contents += lineBrezenhemMenuItem
            }
            contents += new Menu("Curves"){
                contents += circleMenuItem
                contents += bezierMenuItem
                contents += ermitMenuItem
            }
        }
    }

    listenTo(
        startDebugButton,
        nextDebugStepButton,
        clearButton,
        previousDebugStepButton,
        cancelButton,
        colorChooseButton
    )

    reactions += {
        case ButtonClicked(`clearButton`) => executeAndRepaint(clearScene)
        case ButtonClicked(`startDebugButton`) => changeDebugMode()
        case ButtonClicked(`nextDebugStepButton`) => executeAndRepaint(nextDebugStep)
        case ButtonClicked(`previousDebugStepButton`) => executeAndRepaint(previousDebugStep)
        case ButtonClicked(`cancelButton`) => executeAndRepaint(cancelShapeDrawing)
        case ButtonClicked(`colorChooseButton`) => executeAndRepaint(chooseColor)
    }

    private def chooseColor() {
        val color = JColorChooser.showDialog(this.peer, "Choose color", DrawingContext.color)
        DrawingContext.color_=(color)
    }

    private def changeDebugMode() {
        graphicsSceneController.changeMode()
        nextDebugStepButton.enabled_=(!nextDebugStepButton.enabled)
        previousDebugStepButton.enabled_=(!previousDebugStepButton.enabled)
        cancelButton.enabled_=(!cancelButton.enabled)
    }

    private def nextDebugStep() {

        if (graphicsSceneController.isNextDebugStepEnabled) {
            graphicsSceneController.nextDebugStep()
        }

        if (!graphicsSceneController.isNextDebugStepEnabled) {
            graphicsSceneController.changeMode()

            nextDebugStepButton.enabled_=(b = false)
            previousDebugStepButton.enabled_=(b = false)
            cancelButton.enabled_=(b = true)
        }
    }

    private def previousDebugStep() {
        graphicsSceneController.previousDebugStep()
    }

    private def cancelShapeDrawing() {
        graphicsSceneController.cancelShapeDrawing()
    }

    private def clearScene() {
        graphicsScene.clear()
    }

    private def executeAndRepaint(function: () => Unit) {
        function()
        repaint()
    }

    private def setShapeType(shapeType: ShapeType) {
        DrawingContext.shapeType_=(shapeType)
        graphicsScene.setMaxSelectionBufferSize(shapeType.definingPointQuantity)
    }
}
