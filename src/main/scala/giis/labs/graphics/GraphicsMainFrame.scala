package giis.labs.graphics

import custom.ToolBar
import java.awt.Dimension
import giis.labs.model.{Axis, AxisType, ShapeType}
import swing._
import event.{Key, KeyPressed, ButtonClicked}
import javax.swing.{ImageIcon, JColorChooser}
import giis.labs.model.shape.{Rect3D, Shape}

/**
 * Main frame of the application.
 *
 * @author Q-YAA
 */
class GraphicsMainFrame extends MainFrame {

    private val defaultWidth = 800
    private val defaultHeight = 650

    private val startIcon = new ImageIcon(getClass.getClassLoader.getResource("start.png"))
    private val stopIcon = new ImageIcon(getClass.getClassLoader.getResource("stop.png"))
    private val nextIcon = new ImageIcon(getClass.getClassLoader.getResource("next.png"))
    private val previousIcon = new ImageIcon(getClass.getClassLoader.getResource("previous.png"))

    private val startDebugButton = new Button("Debug")

    private val nextDebugStepButton = new Button()
    nextDebugStepButton.icon_=(nextIcon)
    nextDebugStepButton.preferredSize_=(new Dimension(38, 25))

    private val previousDebugStepButton = new Button()
    previousDebugStepButton.icon_=(previousIcon)
    previousDebugStepButton.preferredSize_=(new Dimension(38, 25))

    private val startStopDebugAnimationButton = new Button()
    startStopDebugAnimationButton.icon_=(startIcon)
    startStopDebugAnimationButton.preferredSize_=(new Dimension(35, 35))

    private val clearButton = new Button("Clear")
    private val cancelButton = new Button("Cancel")
    private val colorChooseButton = new Button("Choose color")
    private val fillColorChooseButton = new Button("Choose fill color")

    private val graphicsScene = new GraphicsScene
    private val graphicsSceneController = new GraphicsSceneController(graphicsScene)

    private val gridPanelComponent: GridPanelComponent =
        new GridPanelComponent(graphicsScene, graphicsSceneController)
    gridPanelComponent.focusable_=(b = true)

    private val lineBrezenhemMenuItem = new RadioMenuItem("Brezenhem")
    private val lineDdaMenuItem = new RadioMenuItem("Dda")
    private val circleMenuItem = new RadioMenuItem("Circle")
    private val bezierMenuItem = new RadioMenuItem("Bezier")
    private val ermitMenuItem = new RadioMenuItem("Ermit")
    private val hyperbolaMenuItem = new RadioMenuItem("Hyperbola")
    private val polygonMenuItem = new RadioMenuItem("Polygon")
    private val fillPolygonByLineMenuItem = new RadioMenuItem("Fill polygon by line")
    private val floodFillPolygonMenuItem = new RadioMenuItem("Flood fill polygon")
    private val rect3DMenuItem = new RadioMenuItem("Rect 3D")

    private val shapesMenuGroup = new ButtonGroup(
        lineDdaMenuItem,
        lineBrezenhemMenuItem,
        circleMenuItem,
        bezierMenuItem,
        ermitMenuItem,
        hyperbolaMenuItem,
        polygonMenuItem,
        fillPolygonByLineMenuItem,
        floodFillPolygonMenuItem,
        rect3DMenuItem
    ) {
        listenTo(
            lineBrezenhemMenuItem,
            lineDdaMenuItem,
            circleMenuItem,
            bezierMenuItem,
            ermitMenuItem,
            hyperbolaMenuItem,
            polygonMenuItem,
            fillPolygonByLineMenuItem,
            floodFillPolygonMenuItem,
            rect3DMenuItem
        )

        reactions += {
            case ButtonClicked(`lineDdaMenuItem`) => setShapeType(Shape.LineDda)
            case ButtonClicked(`lineBrezenhemMenuItem`) => setShapeType(Shape.LineBrezenhem)
            case ButtonClicked(`circleMenuItem`) => setShapeType(Shape.Circle)
            case ButtonClicked(`bezierMenuItem`) => setShapeType(Shape.Bezier)
            case ButtonClicked(`ermitMenuItem`) => setShapeType(Shape.Ermit)
            case ButtonClicked(`hyperbolaMenuItem`) => setShapeType(Shape.Hyperbola)
            case ButtonClicked(`polygonMenuItem`) => setShapeType(Shape.Polygon)
            case ButtonClicked(`fillPolygonByLineMenuItem`) => setShapeType(Shape.FillPolygonByLine)
            case ButtonClicked(`floodFillPolygonMenuItem`) => setShapeType(Shape.FloodFillPolygon)
            case ButtonClicked(`rect3DMenuItem`) => executeAndRepaint(load3DRectOnScene("rect3D.xml"))
        }
    }

    private val buttonPanel = new FlowPanel() {
        contents += fillColorChooseButton
        contents += colorChooseButton
        contents += clearButton
        contents += cancelButton
        contents += startDebugButton

        contents += startStopDebugAnimationButton

        contents += previousDebugStepButton
        contents += nextDebugStepButton
    }

    private val toolBar = new ToolBar {
        contents += buttonPanel
    }

    contents = new BorderPanel {
        add(gridPanelComponent, BorderPanel.Position.Center)
        add(toolBar, BorderPanel.Position.North)
    }

    nextDebugStepButton.enabled_=(b = false)
    previousDebugStepButton.enabled_=(b = false)
    startStopDebugAnimationButton.enabled_=(b = false)

    title = "Graphics editor"
    size = new Dimension(defaultWidth, defaultHeight)
    preferredSize = new Dimension(defaultWidth, defaultHeight)
    centerOnScreen()
    setShapeType(Shape.Nothing)

    menuBar = new MenuBar {
        contents += new Menu("Shapes") {
            contents += new Menu("Line") {
                contents += lineDdaMenuItem
                contents += lineBrezenhemMenuItem
            }
            contents += new Menu("Curves") {
                contents += circleMenuItem
                contents += bezierMenuItem
                contents += ermitMenuItem
                contents += hyperbolaMenuItem
            }
            contents += new Menu("Polygon") {
                contents += polygonMenuItem
                contents += fillPolygonByLineMenuItem
                contents += floodFillPolygonMenuItem
            }
            contents += new Menu("Shapes 3D") {
                contents += rect3DMenuItem
            }
        }
    }

    listenTo(
        clearButton,
        startDebugButton,

        startStopDebugAnimationButton,

        nextDebugStepButton,
        previousDebugStepButton,
        cancelButton,
        colorChooseButton,
        fillColorChooseButton
    )

    reactions += {
        case ButtonClicked(`clearButton`) => executeAndRepaint(clearScene)
        case ButtonClicked(`startDebugButton`) => changeDebugMode()

        case ButtonClicked(`startStopDebugAnimationButton`) => startStopDebugAnimation()

        case ButtonClicked(`nextDebugStepButton`) => executeAndRepaint(nextDebugStep)
        case ButtonClicked(`previousDebugStepButton`) => executeAndRepaint(previousDebugStep)
        case ButtonClicked(`cancelButton`) => executeAndRepaint(cancelShapeDrawing)
        case ButtonClicked(`colorChooseButton`) => executeAndRepaint(chooseColor)
        case ButtonClicked(`fillColorChooseButton`) => executeAndRepaint(chooseFillColor)
    }

    listenTo(gridPanelComponent.keys)

    reactions += {
        case KeyPressed(_, Key.X, _, _) => executeAndRepaint(rotateRect3D(Axis.Ox))
        case KeyPressed(_, Key.Y, _, _) => executeAndRepaint(rotateRect3D(Axis.Oy))
        case KeyPressed(_, Key.Z, _, _) => executeAndRepaint(rotateRect3D(Axis.Oz))
    }

    def changeDebugMode() {
        graphicsSceneController.changeMode()
        startStopDebugAnimationButton.enabled_=(!startStopDebugAnimationButton.enabled)
        nextDebugStepButton.enabled_=(!nextDebugStepButton.enabled)
        previousDebugStepButton.enabled_=(!previousDebugStepButton.enabled)
        cancelButton.enabled_=(!cancelButton.enabled)
        startStopDebugAnimationButton.icon_=(startIcon)
    }

    private def chooseColor() {
        val color = JColorChooser.showDialog(this.peer, "Choose color", DrawingContext.color)
        DrawingContext.color_=(color)
    }

    private def chooseFillColor() {
        val color = JColorChooser.showDialog(this.peer, "Choose fill color", DrawingContext.color)
        DrawingContext.fillColor_=(color)
    }

    private def nextDebugStep() {

        if (graphicsSceneController.isNextDebugStepEnabled) {
            graphicsSceneController.nextDebugStep()
        }

        checkForDebugFinish()
    }

    private def checkForDebugFinish() {
        if (!graphicsSceneController.isNextDebugStepEnabled) {
            changeDebugMode()
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
        gridPanelComponent.drawingShapeType_=(shapeType)
        graphicsScene.setMaxSelectionBufferSize(shapeType.definingPointQuantity)
    }

    private def startStopDebugAnimation() {
        if (graphicsSceneController.isAnimationRunning) {
            graphicsSceneController.stopDebugAnimation()
            startStopDebugAnimationButton.icon_=(startIcon)
            checkForDebugFinish()
        } else {
            graphicsSceneController.startDebugAnimation(this)
            startStopDebugAnimationButton.icon_=(stopIcon)
        }
    }

    private def load3DRectOnScene(fileName: String)() {
        val rect3D = Rect3D.load(fileName)
        graphicsScene.addShapeRender(rect3D.createRender(DrawingContext.createDrawingContext))
    }

    private def rotateRect3D(axis: AxisType)() {
        val lastShape = graphicsScene.getLastShape

        if (lastShape != null && lastShape.isInstanceOf[Rect3D]) {
            val rect3D = lastShape.asInstanceOf[Rect3D]
            rect3D.rotate(10, axis)
        }
    }
}
